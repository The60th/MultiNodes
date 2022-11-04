package com.the60th.multinodes;

import com.the60th.multinodes.command.cloud.NodeCommandManager;
import com.the60th.multinodes.database.RedisConnection;
import org.bukkit.plugin.java.JavaPlugin;

public final class MultiNodes extends JavaPlugin {

    private static MultiNodes instance;

    private NodeCommandManager commandManager;
    public static MultiNodes getInstance(){
        return instance;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        commandManager = new NodeCommandManager(this);
    }

    @Override
    public void onDisable() {
        //RedisConnection.getInstance().shutDown();
        // Plugin shutdown logic
    }
}
