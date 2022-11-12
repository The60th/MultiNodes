package com.the60th.multinodes.land.ownership.owner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.the60th.multinodes.land.ownership.Ownership;
import com.the60th.multinodes.land.ownership.Priority;
import com.the60th.multinodes.land.tile.Tile;

import java.util.UUID;

public class PlayerOwner extends Owner{
    public UUID playerId;
    public PlayerOwner(UUID playerId) {
        super(Priority.PLAYER);
        this.playerId = playerId;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("playerId",this.playerId.toString());
        return jsonObject;
    }

    public static PlayerOwner fromJson(String json){
        JsonObject obj = new Gson().fromJson(json,JsonObject.class);
        return new PlayerOwner(UUID.fromString(String.valueOf(obj.get("playerId")).replaceAll("\"","")));
    }
}
