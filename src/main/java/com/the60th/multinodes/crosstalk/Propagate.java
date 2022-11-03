package com.the60th.multinodes.crosstalk;

import com.github.puregero.multilib.MultiLib;
import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.cache.CacheManager;
import com.the60th.multinodes.cache.TileKey;
import org.bukkit.Chunk;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class Propagate {

    public static void setUp(){
        listen();
    }

    private static final String chunkChannel = "chunkChannel";

    private static void notifyChunk(Chunk chunk, TileKey key){
        MultiLib.notify(chunk,chunkChannel,key.toString());
    }

    private static void listen() {
        MultiLib.onString(MultiNodes.getInstance(), chunkChannel, data -> {
            //Do something here
            //TODO Logger
            //One of our chunks has updated on a remote server we need to query for that chunk

            TileKey key = TileKey.TileKeyFromString(data);
            try {
                //This will load the chunk into cache
                CacheManager.getInstance().getCache().get(key);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
