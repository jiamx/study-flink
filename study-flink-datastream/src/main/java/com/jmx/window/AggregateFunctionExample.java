package com.jmx.window;

import org.apache.flink.api.common.functions.AggregateFunction;
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
 *  @Time: 16:58
 *  
 */
public class AggregateFunctionExample {
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
             .aggregate(new MyAggregateFunction()).print();
        env.execute("AggregateFunctionExample");

    }

    private static class MyAggregateFunction implements AggregateFunction<Tuple3<Long, Integer, Long>,Tuple2<Long,Integer>,Tuple2<Long,Integer>> {
        /**
         * 创建一个累加器,初始化值
         * @return
         */
        @Override
        public Tuple2<Long, Integer> createAccumulator() {
            return Tuple2.of(0L,0);
        }

        /**
         *
         * @param value 输入的元素值
         * @param accumulator 中间结果值
         * @return
         */
        @Override
        public Tuple2<Long, Integer> add(Tuple3<Long, Integer, Long> value, Tuple2<Long, Integer> accumulator) {
            return Tuple2.of(value.f0,value.f1 + accumulator.f1);
        }

        /**
         * 获取计算结果值
         * @param accumulator
         * @return
         */
        @Override
        public Tuple2<Long, Integer> getResult(Tuple2<Long, Integer> accumulator) {
            return Tuple2.of(accumulator.f0,accumulator.f1);
        }

        /**
         * 合并中间结果值
         * @param a 中间结果值a
         * @param b 中间结果值b
         * @return
         */
        @Override
        public Tuple2<Long, Integer> merge(Tuple2<Long, Integer> a, Tuple2<Long, Integer> b) {
            return Tuple2.of(a.f0,a.f1 + b.f1);
        }
    }
}
