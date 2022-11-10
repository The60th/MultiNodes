package com.the60th.multinodes.events.movement;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.events.RegistrableListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener extends RegistrableListener {
    private final MultiNodes plugin;
    public JoinListener(MultiNodes plugin) {
        super(plugin);
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();
        plugin.playerMovementCache.add(player);
    }
}
