package com.jmx.processfunction;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/5/3
 *  @Time: 15:27
 *  
 */
public class KeyedProcessFunctionExample {
    private static class TopViewPerWindow extends KeyedProcessFunction<Tuple, Tuple3<String, Long, Double>, String> {
        // 存储最近一次传感器温度
        private ValueState<Double> lastTemp;
        // 存储当前计时器时间戳
        private ValueState<Long> currentTimer;


        @Override
        public void open(Configuration parameters) throws Exception {

            // lastTemp状态描述符
            ValueStateDescriptor<Double> lastTempDescriptor = new ValueStateDescriptor<>("lastTemp-state", Double.class,Double.valueOf(0));
            lastTemp = getRuntimeContext().getState(lastTempDescriptor);
            // currentTimer状态描述符
            ValueStateDescriptor<Long> currentTimerDescriptor = new ValueStateDescriptor<>("currentTimer-state", Long.class, 0L);
            currentTimer = getRuntimeContext().getState(currentTimerDescriptor);

        }


        @Override
        public void processElement(Tuple3<String, Long, Double> value, Context ctx, Collector<String> out) throws Exception {

            // 获取上一次的温度
            Double preTemp = lastTemp.value();

            // 更新最近的温度状态
            lastTemp.update(value.f2);
            // 当前计时器
            Long cruTimer = currentTimer.value();
            if(preTemp == 0 || value.f2 < preTemp){
                // 温度下降,删除当前计时器
                ctx.timerService().deleteProcessingTimeTimer(cruTimer);
                currentTimer.clear();
            }else if(value.f2 > preTemp && cruTimer == 0){

                // 温度升高并且还未注册计时器
                // 以当前时间+1秒设置处理时间计时器
                long processingTime = ctx.timerService().currentProcessingTime() + 10;
                ctx.timerService().registerProcessingTimeTimer(processingTime);
                // 将计时器保存到状态

                currentTimer.update(processingTime);
            }

        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {

            out.collect("传感器温度： '" + ctx.getCurrentKey() + "在1s处理时间内单调上升了'");
            // 回调函数被调用，清空计时器状态
            currentTimer.clear();


        }

    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        // SensorReading(id: String, timestamp: Long, temperature: Double)
        env.fromElements(
                Tuple3.of("Sensor1", 1588491228L,20.5),
                Tuple3.of("Sensor1",  1588491228L,21.5),
                Tuple3.of("Sensor1",  1588491228L,30.5),
                Tuple3.of("Sensor1",  1588492488L,29.6),
                Tuple3.of("Sensor1",  1588492488L,18.8),
                Tuple3.of("Sensor1",  1588492908L,19.1),
                Tuple3.of("Sensor1",  1588492908L,20.2))
                .keyBy(0)
                .process(new TopViewPerWindow())
                .print();


        env.execute("KeyedProcessFunctionExample ");
    }


}
