package com.the60th.multinodes;

import com.the60th.multinodes.command.cloud.NodeCommandManager;
import com.the60th.multinodes.config.NodesConfig;
import com.the60th.multinodes.core.crosstalk.Propagate;
import com.the60th.multinodes.core.movement.PlayerMovementCache;
import com.the60th.multinodes.events.ChunkListeners;
import com.the60th.multinodes.events.RegistrableListener;
import com.the60th.multinodes.events.movement.JoinListener;
import com.the60th.multinodes.events.movement.LeaveListeners;
import com.the60th.multinodes.events.movement.MovementListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MultiNodes extends JavaPlugin {
    public static String PLUGIN_NAME = "MultiNodes";

    private static MultiNodes instance;

    private static Logger log = Bukkit.getLogger();
    private NodeCommandManager commandManager;
    public  PlayerMovementCache playerMovementCache;

    public static MultiNodes getInstance(){
        return instance;
    }
    @Override
    public void onEnable() {
        log = getLogger();
        log.info("onEnable");
        // Plugin startup logic
        instance = this;
        NodesConfig.load(this);
        this.commandManager = new NodeCommandManager(this);
        Propagate.listen();
        registerListeners();

        this.playerMovementCache = new PlayerMovementCache(instance);
    }

    public static Logger getLog(){
        return log;
    }

    @Override
    public void onDisable() {
        //RedisConnection.getInstance().shutDown();
        // Plugin shutdown logic
        deregisterListeners();
    }

    private RegistrableListener[] listeners = new RegistrableListener[]{
            new ChunkListeners(this),
            new JoinListener(this),
            new LeaveListeners(this),
            new MovementListener(this)
    };

    private void registerListeners() {
        for (RegistrableListener listener : listeners) {
            listener.register();
        }
    }
    private void deregisterListeners() {
        for (RegistrableListener listener : listeners) {
            listener.deregister();
        }
    }
}
