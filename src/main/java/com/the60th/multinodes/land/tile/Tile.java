package com.the60th.multinodes.land.tile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.the60th.multinodes.core.tiles.ClaimManager;
import com.the60th.multinodes.land.node.Node;
import com.the60th.multinodes.land.ownership.Ownership;
import com.the60th.multinodes.land.ownership.owner.PlayerOwner;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Tile {
    //TODO values?
    String owner = "empty";
    long key;

    Ownership ownership = new Ownership();
    boolean ownerShipSet = false;

    //The node this tile belongs to
    Node node;


    public Tile(long key){
        this.key = key;
    }
    public Tile(long key, String owner){
        this.key = key;
        this.owner = owner;
    }

    public void setOwnership(Ownership ownership){
        this.ownerShipSet = true;
        this.ownership = ownership;
    }

    public Ownership getOwnership(){
        return ownership;
    }

    public String getOwner(){
        return this.owner;
    }

    public long getKey() {
        return key;
    }

    public void setNodeId(UUID uuid){
        this.node = ClaimManager.getMagicNode(uuid);
    }
    public void setOwner(Player owner) {
        this.owner = owner.getName();
        this.ownerShipSet = true;
        this.ownership = new Ownership();
        this.ownership.addOwner(new PlayerOwner(owner.getUniqueId()));
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key",this.key);
        jsonObject.addProperty("owner",this.owner);
        jsonObject.addProperty("node_id",this.node.getId().toString());

        JsonObject ownerShip = ownership.toJson();
        jsonObject.add("ownership",ownerShip);


        return jsonObject;
    }

    public static Tile fromJson(String json){
        JsonObject obj = new Gson().fromJson(json,JsonObject.class);
        Ownership ownership = Ownership.fromJson(String.valueOf(obj.get("ownership")).replaceAll("\"",""));

        Tile t = new Tile(Long.parseLong(String.valueOf(obj.get("key")).replaceAll("\"",""))
                ,String.valueOf(obj.get("owner")).replaceAll("\"",""));
        t.setOwnership(ownership);
        if(obj.get("node_id") != null){
            String nodeId = obj.get("node_id").getAsString();
            t.setNodeId(UUID.fromString(nodeId));
        }
        return t;
    }
}
