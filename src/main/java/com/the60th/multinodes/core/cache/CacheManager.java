package com.the60th.multinodes.core.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.the60th.multinodes.core.database.RedisConnection;
import com.the60th.multinodes.land.tile.TileKey;
import com.the60th.multinodes.land.tile.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class CacheManager {
    private static final CacheManager instance = new CacheManager();

    private static final int CAPACITY = 1000;
    private final LoadingCache<Long, Tile> tileCache;

    CacheLoader<Long, Tile> loader = new CacheLoader<Long, Tile>() {
        @Override
        public @NotNull Tile load(@NotNull Long key){
            //So this runs when we call get/getUnchecked
            //So .get() will call this method if the value doesn't already exist
            //TODO Implement this
            try {
                return RedisConnection.getInstance().getResult(new TileKey(key));
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            //return RedisConnection.getInstance().getTileValue(new TileKey(key));
        }
    };
    private CacheManager() {
        //TODO Optimize settings
        tileCache = CacheBuilder.newBuilder()
                .maximumSize(CAPACITY)
                .build(loader);

    }

    public LoadingCache<Long, Tile> getCache(){
        return this.tileCache;
    }

    public static CacheManager getInstance() {
        return instance;
    }
}
