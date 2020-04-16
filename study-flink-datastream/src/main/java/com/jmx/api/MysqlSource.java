package com.jmx.api;


import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/14
 *  @Time: 17:34
 * note: RichParallelSourceFunction与SourceContext必须加泛型
 */
public class MysqlSource extends RichParallelSourceFunction<UserBehavior> {
    public Connection conn;
    public PreparedStatement pps;
    private String driver;
    private String url;
    private String user;
    private String pass;


    /**
     * 该方法只会在最开始的时候被调用一次
     * 此方法用于实现获取连接
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
        String scanSQL = "SELECT * FROM user_behavior_log";
        pps = conn.prepareStatement(scanSQL);


    }

    @Override
    public void run(SourceContext<UserBehavior> ctx) throws Exception {
        ResultSet resultSet = pps.executeQuery();
        while (resultSet.next()) {
            ctx.collect(UserBehavior.of(
                    resultSet.getLong("user_id"),
                    resultSet.getLong("item_id"),
                    resultSet.getInt("cat_id"),
                    resultSet.getInt("merchant_id"),
                    resultSet.getInt("brand_id"),
                    resultSet.getString("action"),
                    resultSet.getString("gender"),
                    resultSet.getLong("timestamp")));

        }

    }

    @Override
    public void cancel() {

    }

    /**
     * 实现关闭连接
     */
    @Override
    public void close() {
        if (pps != null) {
            try {
                pps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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


