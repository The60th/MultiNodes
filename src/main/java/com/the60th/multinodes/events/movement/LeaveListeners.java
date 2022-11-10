package com.the60th.multinodes.events.movement;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.events.RegistrableListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListeners extends RegistrableListener {
    private final MultiNodes plugin;
    public LeaveListeners(MultiNodes plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        plugin.playerMovementCache.remove(player);
    }
}
