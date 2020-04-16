package com.jmx.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/15
 *  @Time: 10:32
 *  
 */
public class TestProperties {

    private static String driver;
    private static String url;
    private static String user;
    private static String pass;


    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();

        URL fileUrl = TestProperties.class.getClassLoader().getResource("mysql.ini");
        FileInputStream inputStream = new FileInputStream(new File(fileUrl.toURI()));
        properties.load(inputStream);
        inputStream.close();

        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        user = properties.getProperty("user");
        pass = properties.getProperty("pass");

        System.out.println(properties);
        System.out.println(driver + url + user + pass);

    }
}
