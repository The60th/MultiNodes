package com.the60th.multinodes.core.tiles;

import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.land.tile.TileKey;
import com.the60th.multinodes.land.tile.Tile;
import com.the60th.multinodes.core.crosstalk.Propagate;
import com.the60th.multinodes.core.database.RedisConnection;

public class ClaimManager {

    public static void claimTile(TileKey tileKey, Tile value){
        RedisConnection.getInstance().addTile(tileKey,value);
        CacheManager.getInstance().getCache().put(tileKey.getKey(),value);
        Propagate.notifyChunk(tileKey);
    }


}
