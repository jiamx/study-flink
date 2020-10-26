package com.jmx.window;

import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/5/6
 *  @Time: 17:26
 *  
 */
public class FoldFunctionExample {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // 模拟数据源
        SingleOutputStreamOperator<Tuple3<Long, Integer, Long>> input = env.fromElements(
                Tuple3.of(1L, 10, 1588491228L),
                Tuple3.of(1L, 15, 1588491229L),
                Tuple3.of(1L, 20, 1588491238L),
                Tuple3.of(1L, 25, 1588491248L),
                Tuple3.of(2L, 10, 1588491258L),
                Tuple3.of(2L, 30, 1588491268L),
                Tuple3.of(2L, 20, 1588491278L)).assignTimestampsAndWatermarks(new AscendingTimestampExtractor<Tuple3<Long, Integer, Long>>() {
            @Override
            public long extractAscendingTimestamp(Tuple3<Long, Integer, Long> element) {
                return element.f2 * 1000;
            }
        });

        input.keyBy(0)
             .window(TumblingEventTimeWindows.of(Time.seconds(10)))
             .fold("用户",new FoldFunction<Tuple3<Long, Integer, Long>,String>() {
                 @Override
                 public String fold(String accumulator, Tuple3<Long, Integer, Long> value) throws Exception {
                    // 为第一个元素的值拼接一个"用户"字符串,进行输出
                     return accumulator + value.f0 ;
                 }
             }).print();

        env.execute("FoldFunctionExample");

    }
}
