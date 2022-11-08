package com.the60th.multinodes.land.node;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.the60th.multinodes.land.tile.Tile;
import com.the60th.multinodes.land.tile.TileKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a node which is a collection of tiles (minecraft chunks)
 *
 * */
public class Node {

    //The long is the key gotten from a tileKey
    private HashMap<Long, Tile> tiles = new HashMap<>();

    String displayName;
    UUID id;

    public Node(){

    }

    public Node(String displayName, UUID id, HashMap<Long, Tile> tiles){
        this.displayName = displayName;
        this.id = id;
        this.tiles = tiles;

    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();

        return jsonObject;
    }

    public static Node fromJson(String json){
        JsonObject obj = new Gson().fromJson(json,JsonObject.class);
        return new Node();
    }
}
