package com.jmx.api;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/16
 *  @Time: 22:53
 *  
 */
public class MysqlSink extends RichSinkFunction<UserBehavior> {
    PreparedStatement pps;
    public Connection conn;
    private String driver;
    private String url;
    private String user;
    private String pass;
    /**
     * 在open() 方法初始化连接
     *
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        //初始化数据库连接参数
        Properties properties = new Properties();
        URL fileUrl = TestProperties.class.getClassLoader().getResource("mysql.ini");
        FileInputStream inputStream = new FileInputStream(new File(fileUrl.toURI()));
        properties.load(inputStream);
        inputStream.close();
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        user = properties.getProperty("user");
        pass = properties.getProperty("pass");
        //获取数据连接
        conn = getConnection();
        String insertSql = "insert into user_behavior values(?, ?, ?, ?,?, ?, ?, ?);";
        pps = conn.prepareStatement(insertSql);
    }

    /**
     * 实现关闭连接
     */
    @Override
    public void close() {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pps != null) {
            try {
                pps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 调用invoke() 方法，进行数据插入
     *
     * @param value
     * @param context
     * @throws Exception
     */
    @Override
    public void invoke(UserBehavior value, Context context) throws Exception {
        pps.setLong(1, value.userId);
        pps.setLong(2, value.itemId);
        pps.setInt(3, value.catId);
        pps.setInt(4, value.merchantId);
        pps.setInt(5, value.brandId);
        pps.setString(6, value.action);
        pps.setString(7, value.gender);
        pps.setLong(8, value.timestamp);
        pps.executeUpdate();
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws IOException {
        Connection connnection = null;

        try {
            //加载驱动
            Class.forName(driver);
            //获取连接
            connnection = DriverManager.getConnection(
                    url,
                    user,
                    pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connnection;


    }
}
