package com.gerrywen.seckill.third.redis.config;

import com.gerrywen.seckill.third.redis.properties.RedisProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

/**
 * program: spring-cloud-mall->RedisConfig
 * description:
 * author: gerry
 * created: 2019-11-26 20:43
 **/
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Autowired
    private RedisProperties redisProperties;

    private final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getSimpleName());
                sb.append(":");
                sb.append(method.getName());
                if (ArrayUtils.isNotEmpty(params)) {
                    List p = Arrays.asList(params);
                    String[] args = parameterNameDiscoverer.getParameterNames(method);
                    sb.append(":");
                    List<String> paramsStr = new ArrayList<>();
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] == null || args[i] == null) continue;
                        //???????????????????????????????????????toString??????????????????MBaseModel????????????
                        paramsStr.add(args[i] + ":" + params[i].toString());
                    }
                    sb.append(String.join(":", paramsStr));
                }
                System.out.println("redis key=========" + sb.toString());
                return sb.toString();
            }
        };
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        if (1 == 1) {//??????????????????????????????????????????????????????
            RedisStandaloneConfiguration redis = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
            redis.setPassword(RedisPassword.of(redisProperties.getPassword()));
            return new LettuceConnectionFactory(redis);
        }
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("spring.redis.cluster.nodes", redisProperties.getNodes());
        source.put("spring.redis.cluster.timeout", redisProperties.getTimeOut());
        source.put("spring.redis.cluster.max-redirects", redisProperties.getMaxRedirects());
        RedisClusterConfiguration redisClusterConfiguration;
        redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
        return new LettuceConnectionFactory(redisClusterConfiguration);
    }


    @Primary
    @Bean
    CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //???????????????RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        //??????CacheManager????????????????????????JdkSerializationRedisSerializer,?????????RedisCacheConfiguration??????????????????StringRedisSerializer?????????key???JdkSerializationRedisSerializer?????????value,???????????????????????????????????????
        //ClassLoader loader = this.getClass().getClassLoader();
        //JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        //RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
        //RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        defaultCacheConfig.usePrefix();
        defaultCacheConfig.prefixKeysWith("ts");
        //??????????????????????????????30???
        defaultCacheConfig.entryTtl(Duration.ofSeconds(30));
        //?????????RedisCacheManager
        return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
    }

    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory, RedisSerializer fastJson2JsonRedisSerializer) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        //redis   ????????????
//        redisTemplate.setEnableTransactionSupport(true);
        //hash  ??????jdk  ????????????
        redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer/*new JdkSerializationRedisSerializer()*/);
        //StringRedisSerializer  key  ?????????
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //keySerializer  ???key????????????????????????????????????StringSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //  valueSerializer
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}