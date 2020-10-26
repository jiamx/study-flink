package transformation;

import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.common.io.InputFormat;
import org.apache.flink.api.common.io.statistics.BaseStatistics;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.operators.ReduceOperator;
import org.apache.flink.api.java.tuple.*;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.io.InputSplit;
import org.apache.flink.core.io.InputSplitAssigner;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.apache.flink.api.java.aggregation.Aggregations.MIN;
import static org.apache.flink.api.java.aggregation.Aggregations.SUM;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/5/9
 *  @Time: 10:36
 *  
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        // 用于批处理的执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataSet<Tuple2<String, Integer>> source = env.fromElements(
                Tuple2.of("jack", 20),
                Tuple2.of("Tom", 21),
                Tuple2.of("Robin", 25),
                Tuple2.of("Bob", 30));

      /*  ReduceOperator<Tuple2<String, Integer>> tuple2Reduce = source.minBy(1);
        tuple2Reduce.print();*/

        source.groupBy(0)
                .minBy(1)
                .print();
        /*//[id,x,y],坐标值
        DataSet<Tuple3<Integer, Integer, Integer>> coords1 = env.fromElements(
                Tuple3.of(1, 20, 18),
                Tuple3.of(2, 15, 20),
                Tuple3.of(3, 25, 10));
        DataSet<Tuple3<Integer, Integer, Integer>> coords2 = env.fromElements(
                Tuple3.of(1, 20, 18),
                Tuple3.of(2, 15, 20),
                Tuple3.of(3, 25, 10));
        // 求任意两点之间的欧氏距离

        coords1.cross(coords2)
                .with(new CrossFunction<Tuple3<Integer, Integer, Integer>, Tuple3<Integer, Integer, Integer>, Tuple3<Integer, Integer, Double>>() {
                    @Override
                    public Tuple3<Integer, Integer, Double> cross(Tuple3<Integer, Integer, Integer> val1, Tuple3<Integer, Integer, Integer> val2) throws Exception {
                        // 计算欧式距离
                        double dist = sqrt(pow(val1.f1 - val2.f1, 2) + pow(val1.f2 - val2.f2, 2));
                        // 返回两点之间的欧式距离
                        return Tuple3.of(val1.f0,val2.f0,dist);
                    }
                }).print();*/

       /* DataSource<Tuple2<String, Long>> source = env.fromElements(
                Tuple2.of("Flink", 1L),
                Tuple2.of("Flink", 1L),
                Tuple2.of("Hadoop", 1L),
                Tuple2.of("Spark", 1L),
                Tuple2.of("Flink", 1L));*/
        /*// 用户id，购买商品名称，购买商品数量
        DataSource<Tuple3<Integer,String,Integer>> source1 = env.fromElements(
                Tuple3.of(1,"item1",2),
                Tuple3.of(2,"item2",3),
                Tuple3.of(3,"item2",4));
        //商品名称与商品单价
        DataSource<Tuple2<String, Integer>> source2 = env.fromElements(
                Tuple2.of("item1", 10),
                Tuple2.of("item2", 20),
                Tuple2.of("item3", 15));

        source1.coGroup(source2)
                .where(1)
                .equalTo(0)
                .with(new CoGroupFunction<Tuple3<Integer,String,Integer>, Tuple2<String,Integer>, Tuple2<String,Double>>() {
                    // 每个Iterable存储的是分好组的数据，即相同key的数据组织在一起
                    @Override
                    public void coGroup(Iterable<Tuple3<Integer, String, Integer>> first, Iterable<Tuple2<String, Integer>> second, Collector<Tuple2<String, Double>> out) throws Exception {
                        //存储每种商品购买数量
                        int sum = 0;
                        for(Tuple3<Integer, String, Integer> val1:first){
                        sum += val1.f2;

                    }
                    // 每种商品数量 * 商品单价
                    for(Tuple2<String, Integer> val2:second){
                        out.collect(Tuple2.of(val2.f0,sum * val2.f1.doubleValue()));

                        }
                    }
                }).print();*/
       /*
        source1.join(source2)
                .where(1)
                .equalTo(0)
                .with(new JoinFunction<Tuple3<Integer,String,Integer>, Tuple2<String,Integer>, Tuple3<Integer,String,Double>>() {
                    // 用户每种商品购物总金额
                    @Override
                    public Tuple3<Integer, String, Double> join(Tuple3<Integer, String, Integer> first, Tuple2<String, Integer> second) throws Exception {
                        return Tuple3.of(first.f0,first.f1,first.f2 * second.f1.doubleValue());
                    }
                }).print();
*/
        // 去第一个和第三个元素
        // source.project(0, 2).print();
       /* source.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long value) throws Exception {
                return value % 2 == 0;
            }
        }).print();*/
      /*  source.mapPartition(new MapPartitionFunction<String, Long>() {
            @Override
            public void mapPartition(Iterable<String> values, Collector<Long> out) throws Exception {
                long c = 0;
                for (String value : values) {
                    c++;
                }
                //输出每个分区元素个数
                out.collect(c);
            }
        }).print();
*/

     /*   // 数据源
        DataSet<String> stringDataSource = env.fromElements("hello Flink What is Apache Flink");

        // 转换
        AggregateOperator<Tuple2<String, Integer>> wordCnt = stringDataSource
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        String[] split = value.split(" ");
                        for (String word : split) {
                            out.collect(Tuple2.of(word, 1));
                        }
                    }
                })
                .groupBy(0)
                .sum(1);
        // 输出
        wordCnt.print();*/
    }


}
