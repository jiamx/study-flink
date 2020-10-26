package com.jmx.realtimedw;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/10/25
 *  @Time: 16:17
 *  
 */
public class TestRedisConn {
    public static void main(String[] args) {
        Jedis conn = JedisConnPool.getConn();
        String k1 = conn.get("k1");
        Set<String> key4 = conn.smembers("key4");
        String next = key4.iterator().next();
        System.out.println(next);
        System.out.println(k1);
        conn.set("key3", "hello redis");
        Set<String> keys = conn.keys("*");
        if (keys.iterator().hasNext()) {
            System.out.println(keys.iterator().next());
        }
    }
}
