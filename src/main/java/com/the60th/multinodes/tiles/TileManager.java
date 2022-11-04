package com.the60th.multinodes.tiles;

import com.the60th.multinodes.cache.CacheManager;
import com.the60th.multinodes.cache.TileKey;
import com.the60th.multinodes.cache.TileValue;
import com.the60th.multinodes.crosstalk.Propagate;
import com.the60th.multinodes.database.RedisConnection;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class TileManager {

    public static void claimTile(Chunk chunk, TileKey tileKey, TileValue value){
        RedisConnection.getInstance().addTile(tileKey,value);
        CacheManager.getInstance().getCache().put(tileKey.getKey(),value);
        Propagate.getInstance().notifyChunk(tileKey);
    }
}
