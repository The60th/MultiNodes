package com.the60th.multinodes.land.ownership.owner;

import com.google.gson.JsonObject;
import com.the60th.multinodes.land.ownership.Priority;

public class NodeOwner extends Owner{
    public NodeOwner(Priority priority) {
        super(priority);
    }

    @Override
    public JsonObject toJson() {
        return null;
    }
}
