package com.the60th.multinodes.land.node;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.land.tile.Tile;
import com.the60th.multinodes.land.tile.TileKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a node which is a collection of tiles (minecraft chunks)
 *
 * */
public class Node {

    //The long is the key gotten from a tileKey
    private HashMap<Long, Tile> tiles = new HashMap<>();
    private long homeChunk;
    String displayName;
    UUID id;

    public Node(){

    }

    public Node(String displayName, UUID id, long homeChunk,ArrayList<Long> chunks){
        this.displayName = displayName;
        this.id = id;
        this.homeChunk = homeChunk;

        //TODO Might need some async shit
        chunks.forEach(chunk ->{
            tiles.put(chunk, CacheManager.getInstance().getCache().getUnchecked(chunk));
        });

        //Async

    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        //For json we only need a list of chunk keys for storage and don't need to store the tile object itself
        JsonArray jsonArray = new JsonArray();
        for (Tile tiles: tiles.values()) {
            jsonArray.add(tiles.getKey());
        }
        jsonObject.add("chunks",jsonArray);
        jsonObject.addProperty("homechunk",homeChunk);
        jsonObject.addProperty("id",id.toString());
        jsonObject.addProperty("name",displayName);
        return jsonObject;
    }

    public static Node fromJson(String json){
        JsonObject obj = new Gson().fromJson(json,JsonObject.class);
        long homeChunk = obj.get("homechunk").getAsLong();
        String name = obj.get("name").getAsString();
        UUID id = UUID.fromString(obj.get("id").getAsString());
        JsonArray array = obj.getAsJsonArray("chunks");
        ArrayList<Long> chunks = new ArrayList<>();
        array.forEach(ele ->{
            chunks.add(ele.getAsLong());
        });

        return new Node(name,id,homeChunk,chunks);
    }
}
