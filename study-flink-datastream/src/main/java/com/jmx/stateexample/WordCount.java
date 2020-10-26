package com.jmx.stateexample;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/25
 *  @Time: 21:15
 *  
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        DataStreamSource<String> streamSource = env.socketTextStream("localhost", 9999);
        SingleOutputStreamOperator<Tuple2<String,Integer>> words = streamSource.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String,Integer>> out) throws Exception {
                String[] splits = value.split("\\s");
                for (String word : splits) {
                    out.collect(Tuple2.of(word, 1));
                }
            }
        });
        words.keyBy(0).sum(1).print();
        env.execute("WC");
    }
}
