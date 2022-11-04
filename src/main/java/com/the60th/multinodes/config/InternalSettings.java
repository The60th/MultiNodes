package com.the60th.multinodes.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class InternalSettings {
    @Comment("Test value")
    private long testValue = 100L;

    public long getTestValue(){
        return testValue;
    }
}
