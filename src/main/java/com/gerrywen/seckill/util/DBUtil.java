package com.gerrywen.seckill.util;

import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * program: spring-boot-seckill->DBUtil
 * description:
 * author: gerry
 * created: 2020-03-07 09:31
 **/
public class DBUtil {

    private static Properties props;

    static {
        try {
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("application-dev.yml");
            props = new Properties();
            props.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() throws Exception {
        String url = props.getProperty("url");
        String username = "root";
        String password ="root";
        String driver = props.getProperty("driver-class-name");
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }
}
