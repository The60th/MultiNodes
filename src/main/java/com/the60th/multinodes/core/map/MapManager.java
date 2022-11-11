package com.the60th.multinodes.core.map;

import com.the60th.multinodes.core.map.components.PlayerMap;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MapManager {

    private static HashMap<UUID, PlayerMap> maps = new HashMap<>();

    /**
     * This should be called upon the change of a node
     * */
    public static void updateSingleTile(long key){
        for (PlayerMap map : maps.values()) {
            map.updateData();
        }
    }


    /**
     * This should be called whenever a player moves.
     * */
    public static void updateOnMove(Player player){
        maps.get(player.getUniqueId()).updateData();
    }


    public static void create(Player player){
        if (maps.containsKey(player.getUniqueId())) return;
        maps.put(player.getUniqueId(),new PlayerMap(player));
        maps.get(player.getUniqueId()).mapOn();
    }

    public static void remove(Player player){
        maps.remove(player.getUniqueId());
    }
}
