package transformation;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/5/12
 *  @Time: 14:01
 *  
 */
public class BroadcastExample {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        ArrayList<Tuple2<Integer,String>> RawBroadCastData = new ArrayList<>();

        RawBroadCastData.add(new Tuple2<>(1,"jack"));
        RawBroadCastData.add(new Tuple2<>(2,"tom"));
        RawBroadCastData.add(new Tuple2<>(3,"Bob"));

        // 模拟数据源，[userId,userName]
        DataSource<Tuple2<Integer, String>> userInfoBroadCastData = env.fromCollection(RawBroadCastData);

        ArrayList<Tuple2<Integer,Double>> rawUserAount = new ArrayList<>();

        rawUserAount.add(new Tuple2<>(1,1000.00));
        rawUserAount.add(new Tuple2<>(2,500.20));
        rawUserAount.add(new Tuple2<>(3,800.50));

        // 处理数据：用户id，用户购买金额 ，[UserId,amount]
        DataSet<Tuple2<Integer, Double>> userAmount = env.fromCollection(rawUserAount);

        // 转换为map集合类型的DataSet
        DataSet<HashMap<Integer, String>> userInfoBroadCast = userInfoBroadCastData.map(new MapFunction<Tuple2<Integer, String>, HashMap<Integer, String>>() {

            @Override
            public HashMap<Integer, String> map(Tuple2<Integer, String> value) throws Exception {
                HashMap<Integer, String> userInfo = new HashMap<>();
                userInfo.put(value.f0, value.f1);
                return userInfo;
            }
        });

       DataSet<String> result = userAmount.map(new RichMapFunction<Tuple2<Integer, Double>, String>() {
            // 存放广播变量返回的list集合数据
            List<HashMap<String, String>> broadCastList = new ArrayList<>();
            // 存放广播变量的值
            HashMap<String, String> allMap = new HashMap<>();

            @Override

            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                //获取广播数据,返回的是一个list集合
                this.broadCastList = getRuntimeContext().getBroadcastVariable("userInfo");
                for (HashMap<String, String> value : broadCastList) {
                    allMap.putAll(value);
                }
            }

            @Override
            public String map(Tuple2<Integer, Double> value) throws Exception {
                String userName = allMap.get(value.f0);
                return "用户id： " + value.f0 + " | "+ "用户名： " + userName + " | " + "购买金额： " + value.f1;
            }
        }).withBroadcastSet(userInfoBroadCast, "userInfo");

        result.print();
    }

}
