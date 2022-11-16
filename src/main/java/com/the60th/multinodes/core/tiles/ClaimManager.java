package com.the60th.multinodes.core.tiles;

import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.core.map.MapManager;
import com.the60th.multinodes.land.node.Node;
import com.the60th.multinodes.land.tile.TileKey;
import com.the60th.multinodes.land.tile.Tile;
import com.the60th.multinodes.core.crosstalk.Propagate;
import com.the60th.multinodes.core.redis.RedisConnection;

import java.util.ArrayList;
import java.util.UUID;

public class ClaimManager {
    public static ArrayList<Node> activeNodes = new ArrayList<>();
    /**
     * Call this method when ever a tile changes on the local server.
     *
     * */
    public static void claimTile(TileKey tileKey, Tile value){
        //Update Redis, Cache and Notify other servers.
        RedisConnection.getInstance().addTile(tileKey,value);
        CacheManager.getInstance().getCache().put(tileKey.getKey(),value);
        Propagate.notifyChunk(tileKey);

        //Handler for other methods that need to know when a chunk is changed
        claimActions(tileKey);
    }


    /**
     * Handler to run code when a chunk claims
     * This method will only be called by ClaimManger.claimTile and Propagate.listen
     * ClaimManger will call it for local changes
     * Propgate.listen will call it for remote changes that are being relayed
     * Update map,
     * Etc
     * */
    public static void claimActions(TileKey tileKey){
        MapManager.updateSingleTile(tileKey.getKey());
    }

    //TODO Tester method for node interactions
    public static void createNode(String displayName, long homeChunk){
        Node node = new Node(displayName, homeChunk);
        activeNodes.add(node);

    }

    public static Node getMagicNode(UUID uuid){
        return new Node();
    }
}
