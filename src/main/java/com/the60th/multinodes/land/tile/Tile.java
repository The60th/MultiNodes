package com.the60th.multinodes.land.tile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.the60th.multinodes.land.ownership.Ownership;

public class Tile {
    //TODO values?
    String owner = "empty";
    long key;

    Ownership ownership = new Ownership();

    public Tile(long key){
        this.key = key;
    }
    public Tile(long key, String owner){
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


        JsonObject ownerShip = ownership.toJson();
        jsonObject.add("ownership",ownerShip);


        return jsonObject;
    }

    public static Tile fromJson(String json){
        JsonObject obj = new Gson().fromJson(json,JsonObject.class);
        Ownership ownership = Ownership.fromJson(String.valueOf(obj.get("ownership")).replaceAll("\"",""));

        return new Tile(Long.parseLong(String.valueOf(obj.get("key")).replaceAll("\"",""))
                ,String.valueOf(obj.get("owner")).replaceAll("\"",""));

    }
}
