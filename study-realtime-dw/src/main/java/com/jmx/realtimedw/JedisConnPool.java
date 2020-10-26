package com.jmx.realtimedw;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/10/25
 *  @Time: 16:28
 *  
 */
public  class JedisConnPool {

    public  static JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
          config.setMaxTotal(60);
          //最大空闲连接数
          config.setMaxIdle(10);
          config.setTestOnBorrow(true);
        return config;
    }
    // GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password
    public  static Jedis getConn(){
        JedisPool jedisPool = new JedisPool(getJedisPoolConfig(), "192.168.10.203", 6379, 3000, "123qwe");
        return  jedisPool.getResource();
    }


}
