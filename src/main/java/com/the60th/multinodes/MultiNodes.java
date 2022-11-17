package com.the60th.multinodes;

import com.the60th.multinodes.command.cloud.NodeCommandManager;
import com.the60th.multinodes.config.NodesConfig;
import com.the60th.multinodes.core.crosstalk.Propagate;
import com.the60th.multinodes.core.mongo.LandsDatabase;
import com.the60th.multinodes.core.movement.PlayerMovementCache;
import com.the60th.multinodes.events.chunks.ChunkListeners;
import com.the60th.multinodes.events.RegistrableListener;
import com.the60th.multinodes.events.movement.JoinListener;
import com.the60th.multinodes.events.movement.LeaveListeners;
import com.the60th.multinodes.events.movement.MovementListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.logging.Logger;

public final class MultiNodes extends JavaPlugin {
    public static String PLUGIN_NAME = "MultiNodes";

    private static MultiNodes INSTANCE;

    private static Logger log = Bukkit.getLogger();
    private NodeCommandManager commandManager;
    public  PlayerMovementCache playerMovementCache;

    private LandsDatabase database;

    public static MultiNodes getINSTANCE(){
        return INSTANCE;
    }
    @Override
    public void onEnable() {
        log = getLogger();
        log.info("onEnable");
        // Plugin startup logic
        INSTANCE = this;
        NodesConfig.load(this);
        this.commandManager = new NodeCommandManager(this);
        Propagate.listen();
        registerListeners();
        database = new LandsDatabase(this);

        this.playerMovementCache = new PlayerMovementCache(INSTANCE);
    }

    public static Logger getLog(){
        return log;
    }

    @Override
    public void onDisable() {
        //RedisConnection.getInstance().shutDown();
        // Plugin shutdown logic
        deregisterListeners();
        closeDatabase();
    }
    private void closeDatabase() {
        try {
            database.close();
        } catch (InterruptedException e) {
            MultiNodes.getLog().severe("Database was interrupted while shutting down.");
            e.printStackTrace();
        }
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
    public static @NonNull MultiNodes get() {
        return INSTANCE;
    }
    public static void debug(@NonNull Runnable runnable) {
        if (NodesConfig.get().debug()) runnable.run();
    }
    public @NonNull LandsDatabase getDatabase() {
        return database;
    }
    public @NonNull NodeCommandManager getCommandManager() {
        return commandManager;
    }

}
