package com.the60th.multinodes.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

@ConfigSerializable
public class NodesConfig {
    private static final String CONFIG_FILE = "config.conf";

    // ---CONFIG PROPERTIES---
    private boolean debug = true;
    @Comment("Internal settings for managing plugin internals. Don't touch unless you are a tech fleeper!")
    private InternalSettings internalSettings = new InternalSettings();

    public InternalSettings getInternalSettings(){
        return internalSettings;
    }

    public boolean debug() {
        return debug;
    }


    // ---SINGLETON STUFF---
    private static volatile transient NodesConfig INSTANCE;

    public static @NonNull NodesConfig get() {
        return INSTANCE;
    }

    /**
     * Load the config and replace the singleton instance with the newly
     * loaded instance.
     *
     * Please note: THIS METHOD BLOCKS, DO NOT CALL IT ON THE MAIN THREAD
     *
     * @param plugin The plugin instance that this config belongs to
     */
    public static void load(@NonNull JavaPlugin plugin) {
        try {
            INSTANCE = loadConfig(plugin);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Encountered a fatal error while loading config.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the config from the PersonaPlugin/config.conf file, or creates
     * and populates the file with the defaults specified here if the
     * file does not exist.
     *
     * Please note: THIS METHOD BLOCKS, DO NOT CALL IT ON THE MAIN THREAD
     *
     * @param plugin The plugin instance that this config belongs to
     * @return An instance of the persona config
     * @throws IOException In case there is some parsing/file IO error
     */
    public static NodesConfig loadConfig(JavaPlugin plugin) throws IOException {
        File file = new File(plugin.getDataFolder() + File.separator + CONFIG_FILE);
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(file.toPath())
                .build();
        if (!file.exists()) {
            plugin.getLogger().log(Level.INFO, "No config.conf detected, creating a new one...");
            file.getParentFile().mkdirs();
            file.createNewFile();

            NodesConfig res = new NodesConfig();
            CommentedConfigurationNode node = loader.load();
            node.get(NodesConfig.class, INSTANCE);
            loader.save(node);
            return res;
        } else {
            return loader.load().get(NodesConfig.class);
        }
    }
}
