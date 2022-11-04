package com.the60th.multinodes.core.cache;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TileKey {
    //TODO Anything else needed to act as the key?
    long key;


    public TileKey(long key){
        this.key = key;
    }

    public long getKey() {
        return key;
    }
    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key",this.key);
        return jsonObject;
    }

    public static TileKey fromJson(String json){
        JsonObject obj = new Gson().fromJson(json,JsonObject.class);
        return new TileKey(Long.parseLong(String.valueOf(obj.get("key")).replaceAll("\"","")));

    }
}
