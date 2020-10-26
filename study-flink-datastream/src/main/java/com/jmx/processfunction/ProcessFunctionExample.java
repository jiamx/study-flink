package com.jmx.processfunction;

import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/5/4
 *  @Time: 16:24
 *  
 */
public class ProcessFunctionExample {


    // 定义side output标签
    static final OutputTag<UserBehaviors> buyTags = new OutputTag<UserBehaviors>("buy") {
    };
    static final OutputTag<UserBehaviors> cartTags = new OutputTag<UserBehaviors>("cart") {
    };
    static final OutputTag<UserBehaviors> favTags = new OutputTag<UserBehaviors>("fav") {
    };

    static class SplitStreamFunction extends ProcessFunction<UserBehaviors, UserBehaviors> {

        @Override
        public void processElement(UserBehaviors value, Context ctx, Collector<UserBehaviors> out) throws Exception {
            switch (value.behavior) {

                case "buy":
                    ctx.output(buyTags, value);
                    break;
                case "cart":
                    ctx.output(cartTags, value);
                    break;
                case "fav":
                    ctx.output(favTags, value);
                    break;
                default:
                    out.collect(value);
            }

        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        // 模拟数据源[userId,behavior,product]
        SingleOutputStreamOperator<UserBehaviors> splitStream = env.fromElements(
                new UserBehaviors(1L, "buy", "iphone"),
                new UserBehaviors(1L, "cart", "huawei"),
                new UserBehaviors(1L, "buy", "logi"),
                new UserBehaviors(1L, "fav", "oppo"),
                new UserBehaviors(2L, "buy", "huawei"),
                new UserBehaviors(2L, "buy", "onemore"),
                new UserBehaviors(2L, "fav", "iphone")).process(new SplitStreamFunction());


        //获取分流之后购买行为的数据

        splitStream.getSideOutput(buyTags).print("data_buy");
        //获取分流之后加购行为的数据
        splitStream.getSideOutput(cartTags).print("data_cart");
        //获取分流之后收藏行为的数据
        splitStream.getSideOutput(favTags).print("data_fav");

        env.execute("ProcessFunctionExample");
    }


}
