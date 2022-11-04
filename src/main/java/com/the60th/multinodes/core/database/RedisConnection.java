package com.the60th.multinodes.core.database;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.core.cache.TileKey;
import com.the60th.multinodes.core.cache.TileValue;
import com.the60th.multinodes.util.Strings;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;

public class RedisConnection {
    private static final RedisConnection instance = new RedisConnection();

    private Jedis connection;

    public void shutDown(){
        this.connection.close();
    }

    public TileValue getTileValue(TileKey key){
        TileValue value;
        String load = this.connection.get(String.valueOf(key.getKey()));
        if(load == null){
            //Create new tileValue
            System.out.println("Failed to load node creating new node");
            value = new TileValue(key.getKey());
            addTile(key,value);
        }else{
            value = TileValue.fromJson(load);
        }

        return value;
    }

    public void addTile(TileKey key, TileValue value){
        this.connection.set(String.valueOf(key.getKey()),value.toJson().toString());
    }

    private RedisConnection(){
        //do stuff here

        //TODO
        //Load from config
        String hostName = "127.0.0.1";
        String password = "password";
        int port = 6379;
        //TODO Not sure if we can keep a single connection open like this long term honestly?
        connection = new Jedis(hostName,port);
        //connection.auth(password);

        MultiNodes.getInstance().getLogger().log(Level.ALL, Strings.REDIS_CONNECTION);
    }
    //TODO Need a way to serialize chunkKeys and chunkValues into strings for writing to Redis


    public Jedis getConnection() {
        return connection;
    }

    public static RedisConnection getInstance() {
        return instance;
    }


    //TODO TEST
    //Test Method for basic Redis connection
    public static void main(String[] args){
        Jedis jedis = new Jedis("127.0.0.1",6379);
        //jedis.auth("root");

        System.out.println("Connected!");
    }
}
