package com.the60th.multinodes.core.redis;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.land.tile.TileKey;
import com.the60th.multinodes.land.tile.Tile;
import com.the60th.multinodes.util.Strings;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class RedisConnection {
    private static final RedisConnection instance = new RedisConnection();


/*    public TileValue getTileValue(TileKey key){
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
    }*/

    public void addTile(TileKey key, Tile value){
        asyncCommands.set(String.valueOf(key.getKey()),value.toJson().toString());
    }

    public RedisAsyncCommands<String,String> asyncCommands;
    private RedisConnection(){
        //do stuff here

        //TODO
        //Load from config
        String hostName = "127.0.0.1";
        String password = "password";
        int port = 6379;
        //TODO Not sure if we can keep a single connection open like this long term honestly?
        //connection = new Jedis(hostName,port);
        //connection.auth(password);
        RedisClient redisClient = RedisClient
                .create("redis://127.0.0.1:6379/");
        StatefulRedisConnection<String, String> connection
                = redisClient.connect();
        asyncCommands = connection.async();


        MultiNodes.getInstance().getLogger().log(Level.ALL, Strings.REDIS_CONNECTION);
    }
    //TODO Need a way to serialize chunkKeys and chunkValues into strings for writing to Redis

    public Tile getResult(TileKey key) throws ExecutionException, InterruptedException {
        RedisFuture<String> result = asyncCommands.get(String.valueOf(key.getKey()));
        Tile tile;
        String val = result.get();
        if(val == null){
            //Create new tileValue
            //MultiNodes.getLog().info("Creating new node");
            tile = new Tile(key.getKey());
            //Sync back to redis
            addTile(key, tile);
        }else{
            tile = Tile.fromJson(val);
            //MultiNodes.getLog().info("Loading node");
        }
        return tile;
    }

    public static RedisConnection getInstance() {
        return instance;
    }
}
