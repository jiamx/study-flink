package com.jmx.api;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/19
 *  @Time: 19:04
 *  
 */
public class MyTimestampsAndWatermarks implements AssignerWithPeriodicWatermarks<UserBehavior> {

    private long maxOutofOrderness = 60 * 1000;      // 定义1分钟的容忍间隔时间，即允许数据的最大乱序时间
    private long currentMaxTs = Long.MIN_VALUE;      // 观察到的最大时间戳

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        // 生成具有1分钟容忍度的水位线
        return new Watermark(currentMaxTs - maxOutofOrderness);
    }

    @Override
    public long extractTimestamp(UserBehavior element, long previousElementTimestamp) {
        //获取当前记录的时间戳
        long currentTs = element.timestamp;
        // 更新最大的时间戳
        currentMaxTs = Math.max(currentMaxTs, currentTs);
        // 返回记录的时间戳
        return currentTs;
    }
}
