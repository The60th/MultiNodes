package com.the60th.multinodes.core.tiles;

import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.core.cache.TileKey;
import com.the60th.multinodes.core.cache.TileValue;
import com.the60th.multinodes.core.crosstalk.Propagate;
import com.the60th.multinodes.core.database.RedisConnection;
import org.bukkit.Chunk;

public class TileManager {

    public static void claimTile(Chunk chunk, TileKey tileKey, TileValue value){
        RedisConnection.getInstance().addTile(tileKey,value);
        CacheManager.getInstance().getCache().put(tileKey.getKey(),value);
        Propagate.notifyChunk(tileKey);
    }
}
