package com.the60th.multinodes.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.the60th.multinodes.database.RedisConnection;
import org.jetbrains.annotations.NotNull;

public class CacheManager {
    private static final CacheManager instance = new CacheManager();

    private static final int CAPACITY = 1000;
    private final LoadingCache<TileKey, TileValue> tileCache;

    CacheLoader<TileKey, TileValue> loader = new CacheLoader<TileKey,TileValue>() {
        @Override
        public @NotNull TileValue load(@NotNull TileKey key){
            //So this runs when we call get/getUnchecked
            //So .get() will call this method if the value doesn't already exist
            //TODO Implement this
            RedisConnection.getInstance().getTileValue(key);
            return new TileValue();
        }
    };
    private CacheManager() {
        //TODO Optimize settings
        tileCache = CacheBuilder.newBuilder()
                .maximumSize(CAPACITY)
                .build(loader);

    }

    public LoadingCache<TileKey, TileValue> getCache(){
        return this.tileCache;
    }

    public static CacheManager getInstance() {
        return instance;
    }
}
