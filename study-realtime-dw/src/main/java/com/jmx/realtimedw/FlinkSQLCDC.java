
package com.jmx.realtimedw;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;


/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/8/11
 *  @Time: 17:35
 *  
 */

public class FlinkSQLCDC {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

        TableResult tableResult = tableEnv.executeSql("CREATE TABLE test_table (\n" +
                " user_id BIGINT,\n" +
                " item_id BIGINT,\n" +
                " cat_id BIGINT,\n" +
                " action STRING,\n" +
                " province INT,\n" +
                " ts BIGINT\n" +
                ") WITH (\n" +
                " 'connector' = 'kafka',\n" +
                " 'topic' = 'user_behavior',\n" +
                " 'properties.bootstrap.servers' = '192.168.10.204:9092',\n" +
                " 'properties.group.id' = 'testGroup',\n" +
                " 'format' = 'json',\n" +
                " 'scan.startup.mode' = 'latest-offset'\n" +
                ")");

        // 查询表
        Table table = tableEnv.sqlQuery("select * from test_table");
       table.printSchema();
        //tableEnv.execute("flink 1.11.0 demo");
    }
}
