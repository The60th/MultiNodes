package com.the60th.multinodes.database;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.cache.CacheManager;
import com.the60th.multinodes.cache.TileKey;
import com.the60th.multinodes.cache.TileValue;
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
        return TileValue.TileValueFromString(this.connection.get(key.toString()));
    }

    public void addTile(TileKey key, TileValue value){
        this.connection.set("key","value");
    }

    private RedisConnection(){
        //do stuff here

        //TODO
        //Load from config
        String hostName = "hostName";
        String password = "password";
        int port = 123;
        //TODO Not sure if we can keep a single connection open like this long term honestly?
        connection = new Jedis(hostName,port);
        connection.auth(password);
        MultiNodes.getInstance().getLogger().log(Level.ALL, Strings.REDIS_CONNECTION);
    }
    //TODO Need a way to serialize chunkKeys and chunkValues into strings for writing to Redis


    public static RedisConnection getInstance() {
        return instance;
    }
}
