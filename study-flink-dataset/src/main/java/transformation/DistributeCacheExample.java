package transformation;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/5/12
 *  @Time: 15:21
 *  
 */
public class DistributeCacheExample {
    public static void main(String[] args) throws Exception {
        //获取运行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        /**
         *  注册一个本地文件
         *   文件内容为：
         *   1,"jack"
         *   2,"tom"
         *   3,"Bob"
         */
        env.registerCachedFile("file:///E://userinfo.txt", "localFileUserInfo", true);

        ArrayList<Tuple2<Integer,Double>> rawUserAount = new ArrayList<>();

        rawUserAount.add(new Tuple2<>(1,1000.00));
        rawUserAount.add(new Tuple2<>(2,500.20));
        rawUserAount.add(new Tuple2<>(3,800.50));

        // 处理数据：用户id，用户购买金额 ，[UserId,amount]
        DataSet<Tuple2<Integer, Double>> userAmount = env.fromCollection(rawUserAount);

        DataSet<String> result= userAmount.map(new RichMapFunction<Tuple2<Integer, Double>, String>() {
            // 保存缓存数据
            HashMap<String, String> allMap = new HashMap<String, String>();

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                // 获取分布式缓存的数据
                File userInfoFile = getRuntimeContext().getDistributedCache().getFile("localFileUserInfo");
                List<String> userInfo = FileUtils.readLines(userInfoFile);
                for (String value : userInfo) {

                    String[] split = value.split(",");
                    allMap.put(split[0], split[1]);
                }


            }

            @Override
            public String map(Tuple2<Integer, Double> value) throws Exception {
                String userName = allMap.get(value.f0);

                return "用户id： " + value.f0 + " | " + "用户名： " + userName + " | " + "购买金额： " + value.f1;
            }
        });

        result.print();

    }
}
