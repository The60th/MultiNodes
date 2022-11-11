package com.the60th.multinodes.command.map;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.command.cloud.NodeCommandManager;
import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.core.map.MapManager;
import com.the60th.multinodes.land.tile.Tile;
import com.the60th.multinodes.util.Palette;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

public class CmdMap {
    private final MultiNodes plugin;
    private final MinecraftHelp<CommandSender> help;

    public CmdMap(MultiNodes plugin, NodeCommandManager commandManager){
        this.plugin = plugin;
        this.help = new MinecraftHelp<>(
                "Nodes Map",
                Audience::audience,
                commandManager.getManager()
        );
    }


    @CommandMethod("nodes map")
    @CommandDescription("View a map of nearby nodes")
    public void map(Player player){
        player.sendMessage(Component.text("Starting mapper").color(Palette.YELLOW_GREEN_CRAYOLA));
        MapManager.create(player);

    }

    private void buildMap(){

    }

    //TODO call this Async
    private Collection<Tile> getTiles(Player player) {
        int[] offset = {-1,0,1};

        World world = player.getWorld();
        int baseX = player.getLocation().getChunk().getX();
        int baseZ = player.getLocation().getChunk().getZ();

        Collection<Chunk> chunksAroundPlayer = new HashSet<>();

        for(int x : offset) {
            for(int z : offset) {
                Chunk chunk = world.getChunkAt(baseX + x, baseZ + z);
                chunksAroundPlayer.add(chunk);
            }
        }

        Collection<Tile> tiles = new HashSet<>();
        chunksAroundPlayer.forEach(chunk -> {
            tiles.add(CacheManager.getInstance().getCache().getUnchecked(chunk.getChunkKey()));
        });
        return tiles;
    }


}
