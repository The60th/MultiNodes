package com.the60th.multinodes.core.player;

import com.the60th.multinodes.MultiNodes;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMovementCache {

    private HashMap<UUID, PlayerInfo> movementCache = new HashMap<UUID, PlayerInfo>();
    private MultiNodes plugin;

    public PlayerMovementCache(MultiNodes plugin) {
        this.plugin = plugin;
    }

    public void add(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                //Loads to cache
                UUID uuid = player.getUniqueId();
                PlayerInfo playerInfo = new PlayerInfo(uuid);
                movementCache.put(uuid, playerInfo);
            }
        }.runTaskLaterAsynchronously(plugin, 1L);
    }

    public void remove(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                //Remove from movement cache
                UUID uuid = player.getUniqueId();
                movementCache.remove(uuid);
            }
        }.runTaskLaterAsynchronously(plugin, 1L);
    }

    //TODO Asybc
    public void update(Player player) {

        new BukkitRunnable() {
            @Override
            public void run() {
                UUID uuid = player.getUniqueId();
                PlayerInfo playerInfo = movementCache.get(uuid);

                if(!playerInfo.updateLocation()) return;
                //Update the location only every X times

                playerInfo.hasSufficientMovement().thenAccept(val -> {
                    if (!val) return;

                    if (!playerInfo.chunkChange()) return;
                    //Update the player that the chunk has changed.
                    //TODO Player has moved enough, chunk has change perform that task
                    playerInfo.chunkChangeTask();

                });
                //Update movement cache
            }
        }.runTaskLaterAsynchronously(plugin, 1L);
    }


}
