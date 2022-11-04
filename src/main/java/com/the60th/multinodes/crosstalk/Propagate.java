package com.the60th.multinodes.crosstalk;

import com.github.puregero.multilib.MultiLib;
import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.cache.CacheManager;
import com.the60th.multinodes.cache.TileKey;
import org.bukkit.Chunk;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class Propagate {
    private static final String chunkChannel = "chunkChannel";

    public static void notifyChunk(TileKey key){
        System.out.println("Notify on " + chunkChannel);
        MultiLib.notify(chunkChannel,key.toJson().toString());
    }

    public static void listen() {
        MultiLib.onString(MultiNodes.getInstance(), chunkChannel, data -> {
            //Do something here
            //TODO Logger
            //One of our chunks has updated on a remote server we need to query for that chunk
            System.out.println("Incoming multilib message");
            TileKey key = TileKey.fromJson(data);
            try {
                //This will load the chunk into cache
                CacheManager.getInstance().getCache().invalidate(key.getKey());
                CacheManager.getInstance().getCache().get(key.getKey());
                System.out.println("Added tile to cache via multlib message");
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
