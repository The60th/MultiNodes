package com.the60th.multinodes.events;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.core.cache.TileKey;
import com.the60th.multinodes.core.cache.TileValue;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ExecutionException;

public class ChunkListeners extends RegistrableListener {
    private final MultiNodes plugin;
    public ChunkListeners(MultiNodes plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) throws ExecutionException {
        //Create this from a chunk
        Chunk chunk = event.getChunk();
        TileKey key = new TileKey(chunk.getChunkKey());
        //This will load it by default and provide the tile value if needed
        //TODO Async this
        new BukkitRunnable(){
            @Override
            public void run() {
                //Loads to cache
                CacheManager.getInstance().getCache().getUnchecked(key.getKey());
            }
        }.runTaskLaterAsynchronously(plugin,1L);
        //Add chunk to cache
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        Chunk chunk = event.getChunk();
        TileKey key = new TileKey(chunk.getChunkKey()); // Create a tile key here from the chunk
        //TODO Async this
        new BukkitRunnable(){

            @Override
            public void run() {
                CacheManager.getInstance().getCache().invalidate(key.getKey());
            }
        }.runTaskLaterAsynchronously(plugin,1L);
        //Remove chunk from cache
    }


}