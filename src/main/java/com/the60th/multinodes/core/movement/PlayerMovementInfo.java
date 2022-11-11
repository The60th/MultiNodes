package com.the60th.multinodes.core.movement;

import com.the60th.multinodes.config.NodesConfig;
import com.the60th.multinodes.core.map.MapManager;
import com.the60th.multinodes.util.Palette;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class PlayerMovementInfo {


    private Location lastLocation;

    private long lastChunkKey;
    private boolean chunkChange = false;

    private int callsSinceLastUpdate;

    private UUID playerUUID;

    PlayerMovementInfo(UUID uuid) {
        this.playerUUID = uuid;

        //Update the locations
        Player player = Bukkit.getPlayer(this.playerUUID);
        if (player == null) return;
        this.lastLocation = player.getLocation();
    }

    boolean updateLocation() {
        Player player = Bukkit.getPlayer(this.playerUUID);
        assert player != null;
        callsSinceLastUpdate++;
        if (callsSinceLastUpdate < NodesConfig.get().getMovementSettings().getUpdateThreshold()) return false;
        callsSinceLastUpdate = 0;
        Location currentLoc = player.getLocation();
        double delta = lastLocation.distanceSquared(currentLoc);
        if (delta < NodesConfig.get().getMovementSettings().getDeltaThreshold()) {
            return false;
        }

        if (lastChunkKey != currentLoc.getChunk().getChunkKey()) {
            chunkChange = true;
            lastChunkKey = currentLoc.getChunk().getChunkKey();
        }
        this.lastLocation = currentLoc;
        return true;
    }

    //Once true chunk change should stay true until we access the value
    boolean chunkChange() {
        if (this.chunkChange) {
            this.chunkChange = false;
            return true;
        } else {
            return false;
        }
    }

    //TODO Not quite sure if this is done properly tbh
    CompletableFuture<Boolean> hasSufficientMovement() {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        Player player = Bukkit.getPlayer(this.playerUUID);
        assert player != null;
        completableFuture.complete(true);
        return completableFuture;
    }

    //This method is always called ASYNC
    void chunkChangeTask() {
        actionBarTextTask();
        scoreBoardMapTask();
    }

    private void actionBarTextTask() {
        Player player = Bukkit.getPlayer(this.playerUUID);
        assert player != null;
        player.sendMessage(Component.text("Updating your actionbar task").color(Palette.ASH_GRAY));
        player.sendActionBar(Component.text("Chunk Key: ")
                .append(Component.text(player.getLocation().getChunk().getChunkKey()))
                .color(Palette.YELLOW_GREEN_CRAYOLA));
    }

    private void scoreBoardMapTask() {
        Player player = Bukkit.getPlayer(this.playerUUID);
        assert player != null;
        player.sendMessage(Component.text("Updating your scoreboard task").color(Palette.ASH_GRAY));

        //TODO Not tested
        MapManager.updateOnMove(player);
    }


}
