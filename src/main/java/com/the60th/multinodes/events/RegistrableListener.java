package com.the60th.multinodes.events;

import com.the60th.multinodes.MultiNodes;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class RegistrableListener implements Listener {
    protected final MultiNodes plugin;

    public RegistrableListener(MultiNodes plugin){
        this.plugin = plugin;
    }

    public void register(){
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    public void deregister(){
        HandlerList.unregisterAll(this);
    }
}
