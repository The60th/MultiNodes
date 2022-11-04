package com.the60th.multinodes.core.cache;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TileValue {
    //TODO values?
    String owner = "empty";
    long key;

    public TileValue(long key){
        this.key = key;
    }
    public TileValue(long key, String owner){
        this.key = key;
        this.owner = owner;
    }

    public String getOwner(){
        return this.owner;
    }

    public long getKey() {
        return key;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key",this.key);
        jsonObject.addProperty("owner",this.owner);
        return jsonObject;
    }

    public static TileValue fromJson(String json){
        JsonObject obj = new Gson().fromJson(json,JsonObject.class);
        return new TileValue(Long.parseLong(String.valueOf(obj.get("key")).replaceAll("\"",""))
                ,String.valueOf(obj.get("owner")).replaceAll("\"",""));

    }
}
