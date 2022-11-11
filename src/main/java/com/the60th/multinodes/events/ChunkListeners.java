package com.the60th.multinodes.events;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.land.tile.TileKey;
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
        //This will load it by default and provide the tile value if needed
        new BukkitRunnable(){
            @Override
            public void run() {
                //Loads to cache
                Chunk chunk = event.getChunk();
                TileKey key = new TileKey(chunk.getChunkKey());
                CacheManager.getInstance().getCache().getUnchecked(key.getKey());
            }
        }.runTaskLaterAsynchronously(plugin,1L);
        //Add chunk to cache
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        new BukkitRunnable(){
            @Override
            public void run() {
                Chunk chunk = event.getChunk();
                TileKey key = new TileKey(chunk.getChunkKey()); // Create a tile key here from the chunk
                CacheManager.getInstance().getCache().invalidate(key.getKey());
            }
        }.runTaskLaterAsynchronously(plugin,1L);
        //Remove chunk from cache
    }


}
