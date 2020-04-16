package com.jmx.api;

import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.co.RichCoMapFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;


/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/14
 *  @Time: 10:37
 *  
 */
public class DataStreamApi {


    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        DataStreamSource<UserBehavior> userBehavior = env.addSource(new MysqlSource());


        /**
         * map算子
         * DataStream → DataStream
         * 输入一个元素，返回一个元素
         */
        SingleOutputStreamOperator<String> userBehaviorMap = userBehavior.map(new RichMapFunction<UserBehavior, String>() {
            @Override
            public String map(UserBehavior value) throws Exception {

                String action = "";
                switch (value.action) {
                    case "pv":
                        action = "浏览";
                    case "cart":
                        action = "加购";
                    case "fav":
                        action = "收藏";
                    case "buy":
                        action = "购买";

                }

                return action;
            }
        });
        //userBehaviorMap.print();

        /**
         * flatMap算子
         * DataStream → DataStream
         * 输入一个元素，返回零个、一个或多个元素
         */
        SingleOutputStreamOperator<UserBehavior> userBehaviorflatMap = userBehavior.flatMap(new RichFlatMapFunction<UserBehavior, UserBehavior>() {
            @Override
            public void flatMap(UserBehavior value, Collector<UserBehavior> out) throws Exception {
                if (value.gender.equals("女")) {
                    out.collect(value);
                }
            }
        });
        // userBehaviorflatMap.print();

        /**
         * 过滤算子，对数据进行判断，符合条件即返回true的数据会被保留，否则被过滤
         * DataStream → DataStream
         */
        SingleOutputStreamOperator<UserBehavior> userBehaviorFilter = userBehavior.filter(new RichFilterFunction<UserBehavior>() {
            @Override
            public boolean filter(UserBehavior value) throws Exception {
                return value.action.equals("buy");//保留购买行为的数据
            }
        });
        //userBehaviorFilter.print();
        /**
         * 从逻辑上将流划分为不相交的分区。具有相同键的所有记录都分配给同一分区。
         * 在内部，keyBy（）是通过哈希分区实现的。
         * DataStream→KeyedStream
         * 定义键值有3中方式：
         * (1)使用字段位置，如keyBy(1)，此方式是针对元组数据类型，比如tuple，使用元组相应元素的位置来定义键值;
         * (2)字段表达式,用于元组、POJO以及样例类;
         * (3)键值选择器，即keySelector，可以从输入事件中提取键值
         */
        SingleOutputStreamOperator<Tuple2<String, Integer>> userBehaviorkeyBy = userBehavior.map(new RichMapFunction<UserBehavior, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(UserBehavior value) throws Exception {
                return Tuple2.of(value.action.toString(), 1);
            }
        }).keyBy(0) // scala元组编号从1开始，java元组编号是从0开始
           .sum(1); //滚动聚合
        //userBehaviorkeyBy.print();


        /**
         * 对数据进行滚动聚合操作，结合当前元素和上一次Reduce返回的值进行聚合，然后返回一个新的值.
         * KeyedStream → DataStream
         * 将一个ReduceFunction应用在一个keyedStream上,每到来一个事件都会与当前reduce的结果进行聚合，
         * 产生一个新的DataStream,该算子不会改变数据类型，因此输入流与输出流的类型永远保持一致
         */
        SingleOutputStreamOperator<Tuple2<String, Integer>> userBehaviorReduce = userBehavior.map(new RichMapFunction<UserBehavior, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(UserBehavior value) throws Exception {
                return Tuple2.of(value.action.toString(), 1);
            }
        }).keyBy(0) // scala元组编号从1开始，java元组编号是从0开始
          .reduce(new RichReduceFunction<Tuple2<String, Integer>>() {
              @Override
              public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                  return Tuple2.of(value1.f0,value1.f1 + value2.f1);//滚动聚合,功能与sum类似
              }
          });
        //userBehaviorReduce.print();

        /**
         * Aggregations(滚动聚合),滚动聚合转换作用于KeyedStream流上，
         * 生成一个包含聚合结果(比如sum求和，min最小值)的DataStream，
         * 滚动聚合的转换会为每个流过该算子的key值保存一个聚合结果，
         * 当有新的元素流过该算子时，会根据之前的结果值和当前的元素值，更新相应的结果值
         * sum():滚动聚合流过该算子的指定字段的和；
         * min():滚动计算流过该算子的指定字段的最小值
         * max():滚动计算流过该算子的指定字段的最大值
         * minBy():滚动计算当目前为止流过该算子的最小值，返回该值对应的事件；
         * maxBy():滚动计算当目前为止流过该算子的最大值，返回该值对应的事件；
         *
         * KeyedStream → DataStream
         */

        /**
         * 将多条流合并，新的的流会包括所有流的数据，值得注意的是，两个流的数据类型必须一致，
         * 另外，来自两条流的事件会以FIFO(先进先出)的方式合并，所以并不能保证两条流的顺序，
         * 此外，union算子不会对数据去重，每个输入事件都会被发送到下游算子。
         * DataStream* → DataStream
         */

        //userBehaviorkeyBy.union(userBehaviorReduce).print();//将两条流union在一起，可以支持多条流(大于2)的union

        /**
         * 将两个流的事件进行组合，返回一个ConnectedStreams对象，两个流的数据类型可以不一致,
         * ConnectedStreams对象提供了类似于map(),flatMap()功能的算子，如CoMapFunction与CoFlatMapFunction
         * 分别表示map()与flatMap算子，这两个算子会分别作用于两条流，注意：CoMapFunction 或CoFlatMapFunction被调用的时候并不能控制事件的顺序
         * 只要有事件流过该算子，该算子就会被调用
         *DataStream,DataStream → ConnectedStreams
         */

        ConnectedStreams<UserBehavior, Tuple2<String, Integer>> behaviorConnectedStreams = userBehaviorFilter.connect(userBehaviorkeyBy);

        SingleOutputStreamOperator<Tuple3<String, String, Integer>> behaviorConnectedStreamsmap = behaviorConnectedStreams.map(new RichCoMapFunction<UserBehavior, Tuple2<String, Integer>, Tuple3<String, String, Integer>>() {
            @Override
            public Tuple3<String, String, Integer> map1(UserBehavior value1) throws Exception {

                return Tuple3.of("first", value1.action, 1);
            }

            @Override
            public Tuple3<String, String, Integer> map2(Tuple2<String, Integer> value2) throws Exception {

                return Tuple3.of("second", value2.f0, value2.f1);
            }
        });
        // behaviorConnectedStreamsmap.print();


        /**
         *
         *将流分割成两条或多条流，与union相反。
         * 分割之后的流与输入流的数据类型一致，
         * 对于每个到来的事件可以被路由到0个、1个或多个输出流中。可以实现过滤与复制事件的功能，
         * DataStream.split()接收一个OutputSelector函数，用来定义分流的规则，即将满足不同条件的流分配到用户命名的一个输出
         *DataStream → SplitStream
         */

        SplitStream<UserBehavior> userBehaviorSplitStream = userBehavior.split(new OutputSelector<UserBehavior>() {
            @Override
            public Iterable<String> select(UserBehavior value) {
                ArrayList<String> userBehaviors = new ArrayList<String>();

                if (value.action.equals("buy")) {

                    userBehaviors.add("buy");
                } else {

                    userBehaviors.add("other");
                }

                return userBehaviors;
            }
        });
        //userBehaviorSplitStream.select("buy").print();

        userBehavior.addSink(new MysqlSink());

        env.execute(DataStreamApi.class.getSimpleName());


    }


}
















