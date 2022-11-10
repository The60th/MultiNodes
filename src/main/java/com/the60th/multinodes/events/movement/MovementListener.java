package com.the60th.multinodes.events.movement;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.events.RegistrableListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener extends RegistrableListener {
    private final MultiNodes plugin;
    public MovementListener(MultiNodes plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        //TODO Async method
        plugin.playerMovementCache.update(player);
    }
}
