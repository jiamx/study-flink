package com.jmx.processfunction;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/5/4
 *  @Time: 21:23
 *  
 */
public class Test {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        // 模拟数据源[userId,behavior,product]
        env.fromElements(
                Tuple3.of(1L, "buy", "iphone"),
                Tuple3.of(1L, "cart", "huawei"),
                Tuple3.of(1L, "buy", "logi"),
                Tuple3.of(1L, "fav", "oppo"),
                Tuple3.of(2L, "buy", "huawei"),
                Tuple3.of(2L, "buy", "onemore"),
                Tuple3.of(2L, "fav", "iphone")).print();
        env.execute("123");
    }
}
