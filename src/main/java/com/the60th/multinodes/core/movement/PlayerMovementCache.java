package com.the60th.multinodes.core.movement;

import com.the60th.multinodes.MultiNodes;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMovementCache {

    private HashMap<UUID, PlayerMovementInfo> movementCache = new HashMap<UUID, PlayerMovementInfo>();
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
                PlayerMovementInfo playerMovementInfo = new PlayerMovementInfo(uuid);
                movementCache.put(uuid, playerMovementInfo);
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
                PlayerMovementInfo playerMovementInfo = movementCache.get(uuid);

                if(!playerMovementInfo.updateLocation()) return;
                //Update the location only every X times

                playerMovementInfo.hasSufficientMovement().thenAccept(val -> {
                    if (!val) return;

                    if (!playerMovementInfo.chunkChange()) return;
                    //Update the player that the chunk has changed.
                    //TODO Player has moved enough, chunk has change perform that task
                    playerMovementInfo.chunkChangeTask();

                });
                //Update movement cache
            }
        }.runTaskLaterAsynchronously(plugin, 1L);
    }


}
