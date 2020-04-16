package com.jmx.connector;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/1
 *  @Time: 18:29
 *  
 */
public class KafkaConnector {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();
        // 开启checkpoint，时间间隔为毫秒
        senv.enableCheckpointing(5000L);
        // 选择状态后端
        senv.setStateBackend((StateBackend) new FsStateBackend("file:///E://checkpoint"));
        //senv.setStateBackend((StateBackend) new FsStateBackend("hdfs://kms-1:8020/checkpoint"));

        Properties props = new Properties();
        // kafka broker地址
        props.put("bootstrap.servers", "kms-2:9092,kms-3:9092,kms-4:9092");
        // 仅kafka0.8版本需要配置
        props.put("zookeeper.connect", "kms-2:2181,kms-3:2181,kms-4:2181");
        // 消费者组
        props.put("group.id", "test");
        // 自动偏移量提交
        props.put("enable.auto.commit", true);
        // 偏移量提交的时间间隔，毫秒
        props.put("auto.commit.interval.ms", 5000);
        // kafka 消息的key序列化器
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // kafka 消息的value序列化器
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 指定kafka的消费者从哪里开始消费数据
        // 共有三种方式，
        // #earliest
        // 当各分区下有已提交的offset时，从提交的offset开始消费；
        // 无提交的offset时，从头开始消费

        // #latest
        // 当各分区下有已提交的offset时，从提交的offset开始消费；
        // 无提交的offset时，消费新产生的该分区下的数据

        // #none
        // topic各分区都存在已提交的offset时，
        // 从offset后开始消费；
        // 只要有一个分区不存在已提交的offset，则抛出异常
        props.put("auto.offset.reset", "latest");
        FlinkKafkaConsumer011<String> consumer1 = new FlinkKafkaConsumer011<>(
                "qfbap_ods.code_city",
                new SimpleStringSchema(),
                props);
        // 最早的数据开始消费
        // 该模式下，Kafka 中的 committed offset 将被忽略，不会用作起始位置。
        // consumer1.setStartFromEarliest();

        // 消费者组最近一次提交的偏移量，默认。
        // 如果找不到分区的偏移量，那么将会使用配置中的 auto.offset.reset 设置
        //consumer1.setStartFromGroupOffsets();

        // 最新的数据开始消费
        // 该模式下，Kafka 中的 committed offset 将被忽略，不会用作起始位置。
        //consumer1.setStartFromLatest();

        // 指定具体的偏移量时间戳,毫秒
        // 对于每个分区，其时间戳大于或等于指定时间戳的记录将用作起始位置。
        // 如果一个分区的最新记录早于指定的时间戳，则只从最新记录读取该分区数据。
        // 在这种模式下，Kafka 中的已提交 offset 将被忽略，不会用作起始位置。
        //consumer1.setStartFromTimestamp(1585047859000L);


        // 为每个分区指定偏移量
        /*Map<KafkaTopicPartition, Long> specificStartOffsets = new HashMap<>();
        specificStartOffsets.put(new KafkaTopicPartition("qfbap_ods.code_city", 0), 23L);
        specificStartOffsets.put(new KafkaTopicPartition("qfbap_ods.code_city", 1), 31L);
        specificStartOffsets.put(new KafkaTopicPartition("qfbap_ods.code_city", 2), 43L);
        consumer1.setStartFromSpecificOffsets(specificStartOffsets);*/
        /**
         *
         * 请注意：当 Job 从故障中自动恢复或使用 savepoint 手动恢复时，
         * 这些起始位置配置方法不会影响消费的起始位置。
         * 在恢复时，每个 Kafka 分区的起始位置由存储在 savepoint 或 checkpoint 中的 offset 确定
         *
         */

        FlinkKafkaConsumer<String> consumer2 = new FlinkKafkaConsumer<>(
                "qfbap_ods.code_city",
                new SimpleStringSchema(),
                props);
        //设置checkpoint后在提交offset
        // 该值默认为true，
        consumer2.setCommitOffsetsOnCheckpoints(true);

        //DataStreamSource<String> source1 = senv.addSource(consumer1);

        DataStreamSource<String> source2 = senv.addSource(consumer2);


        //source1.print();
        source2.print();

        senv.execute("test kafka connector");


    }
}
