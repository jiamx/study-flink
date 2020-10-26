package com.jmx.api;

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/19
 *  @Time: 21:32
 *  
 */
public class MyPunctuatedAssigner implements AssignerWithPunctuatedWatermarks<UserBehavior> {
    private long maxOutofOrderness = 60 * 1000;      // 定义1分钟的容忍间隔时间，即允许数据的最大乱序时间
    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(UserBehavior element, long extractedTimestamp) {
        // 如果读取数据的用户行为是购买，就生成水位线
        if(element.action.equals("buy")){
           return new Watermark(extractedTimestamp - maxOutofOrderness);
        }else{
            // 不发出水位线
            return null;
        }

    }

    @Override
    public long extractTimestamp(UserBehavior element, long previousElementTimestamp) {
        return element.timestamp;
    }
}
