package com.the60th.multinodes.core.player;

import com.the60th.multinodes.config.NodesConfig;
import com.the60th.multinodes.util.Palette;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class PlayerInfo {



    private Location lastLocation;

    private long lastChunkKey;
    private boolean chunkChange = false;

    private int callsSinceLastUpdate;

    private UUID playerUUID;

    PlayerInfo(UUID uuid){
        this.playerUUID = uuid;

        //Update the locations
        Player player = Bukkit.getPlayer(this.playerUUID);
        if(player == null) return;
        this.lastLocation = player.getLocation();
    }

     boolean updateLocation(){
        //Check delta
        Player player  = Bukkit.getPlayer(this.playerUUID);
        assert player != null;
        callsSinceLastUpdate++;
        if(callsSinceLastUpdate < NodesConfig.get().getMovementSettings().getUpdateThreshold()) return false;
        callsSinceLastUpdate = 0;

        Location currentLoc = player.getLocation();

        if(lastChunkKey != currentLoc.getChunk().getChunkKey()){
            chunkChange = true;
            lastChunkKey = currentLoc.getChunk().getChunkKey();
        }
        this.lastLocation = currentLoc;
        return true;
    }

    //Once true chunk change should stay true until we access the value
    boolean chunkChange(){
        if(this.chunkChange){
            this.chunkChange = false;
            return true;
        }else{
            return false;
        }
    }

    //TODO Not quite sure if this is done properly tbh
    CompletableFuture<Boolean> hasSufficientMovement(){
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        Player player  = Bukkit.getPlayer(this.playerUUID);
        assert player != null;
        Location currentLoc = player.getLocation();
        double delta = lastLocation.distanceSquared(currentLoc);
        if(delta < NodesConfig.get().getMovementSettings().getDeltaThreshold()){
            completableFuture.complete(false);
        }else{
            completableFuture.complete(true);
        }
        return completableFuture;
    }

    //This method is always called ASYNC
    void chunkChangeTask(){
        actionBarTextTask();
        scoreBoardMapTask();
    }

    private void actionBarTextTask(){
        Player player = Bukkit.getPlayer(this.playerUUID);
        assert  player != null;
        player.sendMessage(Component.text("Updating your actionbar task").color(Palette.ASH_GRAY));

    }

    private void scoreBoardMapTask(){
        Player player = Bukkit.getPlayer(this.playerUUID);
        assert  player != null;
        player.sendMessage(Component.text("Updating your scoreboard task").color(Palette.ASH_GRAY));

    }



}
