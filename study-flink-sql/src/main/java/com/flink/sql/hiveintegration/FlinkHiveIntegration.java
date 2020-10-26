/*
package com.flink.sql.hiveintegration;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

*/
/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/3/31
 *  @Time: 13:22
 *  
 *//*

public class FlinkHiveIntegration {

    public static void main(String[] args) throws Exception {

        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .useBlinkPlanner() // 使用BlinkPlanner
                .inBatchMode() // Batch模式，默认为StreamingMode
                .build();

        //使用StreamingMode
       */
/* EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .useBlinkPlanner() // 使用BlinkPlanner
                .inStreamingMode() // StreamingMode
                .build();*//*


        TableEnvironment tableEnv = TableEnvironment.create(settings);

        String name = "myhive";      // Catalog名称，定义一个唯一的名称表示
        String defaultDatabase = "qfbap_ods";  // 默认数据库名称
        String hiveConfDir = "/opt/modules/apache-hive-2.3.4-bin/conf";  // hive-site.xml路径
        String version = "2.3.4";       // Hive版本号

        HiveCatalog hive = new HiveCatalog(name, defaultDatabase, hiveConfDir, version);

        tableEnv.registerCatalog("myhive", hive);
        tableEnv.useCatalog("myhive");
        // 创建数据库
        String createDbSql = "CREATE DATABASE IF NOT EXISTS myhive.test123";

        tableEnv.sqlUpdate(createDbSql);


    }

}
*/
