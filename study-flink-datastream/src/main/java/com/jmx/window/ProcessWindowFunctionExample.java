package com.jmx.window;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/5/6
 *  @Time: 18:12
 *  
 */
public class ProcessWindowFunctionExample {

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

        input.keyBy(t -> t.f0)
             .window(TumblingEventTimeWindows.of(Time.seconds(10)))
             .process(new MyProcessWindowFunction())
             .print();
        env.execute("ProcessWindowFunctionExample");
    }

    private static class MyProcessWindowFunction extends ProcessWindowFunction<Tuple3<Long, Integer, Long>,Tuple3<Long,String,Integer>,Long,TimeWindow> {
        @Override
        public void process(
                Long aLong,
                Context context,
                Iterable<Tuple3<Long, Integer, Long>> elements,
                Collector<Tuple3<Long, String, Integer>> out) throws Exception {
            int count = 0;
            for (Tuple3<Long, Integer, Long> in: elements) {
                count++;
            }
            // 统计每个窗口数据个数，加上窗口输出
            out.collect(Tuple3.of(aLong,"" + context.window(),count));


        }
    }
}
