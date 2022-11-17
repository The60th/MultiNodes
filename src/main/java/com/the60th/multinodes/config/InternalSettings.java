package com.the60th.multinodes.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class InternalSettings {
    @Comment("Test value")
    private long testValue = 100L;
    @Comment("The time between periodic writes to the database to update persona data, in milliseconds")
    private long updatePeriodMillis = 5000L;
    @Comment("The maximum number of records that can be written to the database in one operation. (this doesnt do anything)")
    private int numMaxWrites = 100;
    @Comment("How long should the plugin wait for the thread workers to shutdown? In milliseconds")
    private long shutdownMillis = 3000L;
    private int maxApiCacheSize = 600;
    private int initialApiCacheSize = 100;
    private int maxPlayerDataCacheSize = 1000;

    public int getInitialApiCacheSize() {
        return initialApiCacheSize;
    }

    public int getMaxApiCacheSize() {
        return maxApiCacheSize;
    }

    public int getMaxPlayerDataCacheSize() {
        return maxPlayerDataCacheSize;
    }

    public long getShutdownMillis() {
        return shutdownMillis;
    }

    public long getUpdatePeriodMillis() {
        return updatePeriodMillis;
    }

    public int getNumMaxWrites() {
        return numMaxWrites;
    }
    public long getTestValue(){
        return testValue;
    }
}
