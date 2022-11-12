package com.the60th.multinodes.land.ownership.owner;

import com.google.gson.JsonObject;
import com.the60th.multinodes.land.ownership.Ownership;
import com.the60th.multinodes.land.ownership.Priority;

public abstract class Owner {

    public final Priority priority;

    public Owner(Priority priority){
        this.priority = priority;
    }

    public abstract JsonObject toJson();

}
