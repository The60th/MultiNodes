package com.the60th.multinodes.core.crosstalk;

import com.github.puregero.multilib.MultiLib;
import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.core.tiles.ClaimManager;
import com.the60th.multinodes.land.tile.TileKey;

import java.util.concurrent.ExecutionException;

public class Propagate {
    private static final String chunkChannel = "chunkChannel";

    public static void notifyChunk(TileKey key){
        MultiNodes.getLog().info("Propagating chunk notification");
        MultiLib.notify(chunkChannel,key.toJson().toString());
    }

    public static void listen() {
        MultiLib.onString(MultiNodes.getInstance(), chunkChannel, data -> {
            //Do something here
            //TODO Logger
            //One of our chunks has updated on a remote server we need to query for that chunk
            MultiNodes.getLog().info("Receiving chunk notification");
            TileKey key = TileKey.fromJson(data);
            try {
                //This will load the chunk into cache
                CacheManager.getInstance().getCache().invalidate(key.getKey());
                CacheManager.getInstance().getCache().get(key.getKey());

                ClaimManager.claimActions(key);

            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
