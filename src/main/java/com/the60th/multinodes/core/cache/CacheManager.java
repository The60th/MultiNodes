package com.the60th.multinodes.core.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.the60th.multinodes.core.database.RedisConnection;
import org.jetbrains.annotations.NotNull;

public class CacheManager {
    private static final CacheManager instance = new CacheManager();

    private static final int CAPACITY = 1000;
    private final LoadingCache<Long, TileValue> tileCache;

    CacheLoader<Long, TileValue> loader = new CacheLoader<Long,TileValue>() {
        @Override
        public @NotNull TileValue load(@NotNull Long key){
            //So this runs when we call get/getUnchecked
            //So .get() will call this method if the value doesn't already exist
            //TODO Implement this
            return RedisConnection.getInstance().getTileValue(new TileKey(key));
        }
    };
    private CacheManager() {
        //TODO Optimize settings
        tileCache = CacheBuilder.newBuilder()
                .maximumSize(CAPACITY)
                .build(loader);

    }

    public LoadingCache<Long, TileValue> getCache(){
        return this.tileCache;
    }

    public static CacheManager getInstance() {
        return instance;
    }
}
