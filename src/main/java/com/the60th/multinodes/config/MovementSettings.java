package com.the60th.multinodes.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class MovementSettings {
    @Comment("How many movement events do we ignore before updating our tracked values")
    private int updateThreshold = 5;

    @Comment("The movement delta before we register that a player has moved")
    private double deltaThreshold = 1.5;


    public int getUpdateThreshold(){
        return updateThreshold;
    }

    public double getDeltaThreshold() {
        return deltaThreshold;
    }

}