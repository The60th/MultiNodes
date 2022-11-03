package com.the60th.multinodes;

import com.sun.org.apache.xpath.internal.operations.Mult;
import com.the60th.multinodes.database.RedisConnection;
import org.bukkit.plugin.java.JavaPlugin;

public final class MultiNodes extends JavaPlugin {

    private static MultiNodes instance;

    public static MultiNodes getInstance(){
        return instance;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
    }

    @Override
    public void onDisable() {
        RedisConnection.getInstance().shutDown();
        // Plugin shutdown logic
    }
}
