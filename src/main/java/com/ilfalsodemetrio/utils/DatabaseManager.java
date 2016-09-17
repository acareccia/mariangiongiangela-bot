package com.ilfalsodemetrio.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by lbrtz on 13/09/16.
 */
public class DatabaseManager {
    private static Jedis getConnection() throws URISyntaxException {
        URI redisURI = new URI(System.getenv("REDIS_URL"));
        Jedis jedis = new Jedis(redisURI);
        return jedis;
    }

    //
    public static JedisPool getPool() {
        URI redisURI = null;
        try {
            redisURI = new URI(System.getenv("REDIS_URL"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        JedisPool pool = new JedisPool(poolConfig, redisURI);
        return pool;
    }

    public static void write(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            jedis.set(key,value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static String read(String key) {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static void writeObject(String key, Object value) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(value);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            jedis.set(key.getBytes(),bos.toByteArray());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static Object readObject(String key) {
        byte[] objBytes;
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            objBytes = jedis.get(key.getBytes());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(objBytes);
        ObjectInput in = null;

        try {
            in = new ObjectInputStream(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object o = null;
        try {
            o = in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o;
    }

}
