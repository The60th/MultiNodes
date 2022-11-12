package com.the60th.multinodes.land.ownership.owner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.the60th.multinodes.land.ownership.Priority;

import java.util.UUID;

public class EmptyOwner extends Owner{
    String empty = "empty";
    public EmptyOwner(Priority priority) {
        super(priority);
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empty",this.empty);
        return jsonObject;
    }

    public static EmptyOwner fromJson(String json){
        JsonObject obj = new Gson().fromJson(json,JsonObject.class);
        return new EmptyOwner(Priority.C);
    }
}
