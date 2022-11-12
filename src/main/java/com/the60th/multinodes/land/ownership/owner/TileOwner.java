package com.the60th.multinodes.land.ownership.owner;

import com.google.gson.JsonObject;
import com.the60th.multinodes.land.ownership.Priority;

public class TileOwner extends Owner{
    public TileOwner() {
        super(Priority.B);
    }

    @Override
    public JsonObject toJson() {
        return null;
    }
}
