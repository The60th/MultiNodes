package com.the60th.multinodes.command;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.cache.CacheManager;
import com.the60th.multinodes.cache.TileKey;
import com.the60th.multinodes.cache.TileValue;
import com.the60th.multinodes.command.cloud.NodeCommandManager;
import com.the60th.multinodes.database.RedisConnection;
import com.the60th.multinodes.tiles.TileManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdNodes {
    private final MultiNodes plugin;
    private final MinecraftHelp<CommandSender> help;

    public CmdNodes(MultiNodes plugin, NodeCommandManager commandManager){
        this.plugin = plugin;
        this.help = new MinecraftHelp<>(
                "Nodes help",
                Audience::audience,
                commandManager.getManager()
        );
    }

    @CommandMethod("nodes dump")
    @CommandDescription("Dump the cache")
    public void dump(Player player){
        CacheManager.getInstance().getCache().asMap().forEach((key,val) ->{
            player.sendMessage("Key: " + key);
            player.sendMessage("value: " + val.getKey() + "  --  " + val.getOwner());
        });
    }
    @CommandMethod("nodes load|load2 <key> <value>")
    @CommandDescription("Load the current node you are in")
    public void load(final Player player, final @Argument("key") String key, final @Argument("value") String value ){
        printLocalNode(player);
        //RedisConnection.getInstance().getConnection().set(key,value);
    }

    @CommandMethod("nodes claim|claims <key>")
    @CommandDescription("Claim the current node you are in")
    public void claim(final Player player,final @Argument("key") String key){
        Chunk chunk = player.getLocation().getChunk();
        TileKey key1 = new TileKey(chunk.getChunkKey());
        TileValue value1 = CacheManager.getInstance().getCache().getUnchecked(key1.getKey());
        value1.setOwner(player.getName());
        player.sendMessage(Component.text("Claimed node: \n Key - ")
                .append(Component.text(value1.getKey()))
                .append(Component.text("\n Owner: - "))
                .append(Component.text(value1.getOwner()))
                .color(NamedTextColor.YELLOW));

        //Save to redis
        TileManager.claimTile(chunk,key1, value1);
        //String value = RedisConnection.getInstance().getConnection().get(key);
        //player.sendMessage(Component.text("Key of: " ).append(Component.text(key)).color(NamedTextColor.YELLOW));
        //player.sendMessage(Component.text("Value of: ").append(Component.text(value)).color(NamedTextColor.YELLOW));
    }

    @CommandMethod("nodes owner")
    @CommandDescription("set an owner")
    public void owner(Player player){
        Chunk chunk = player.getLocation().getChunk();
        TileKey key1 = new TileKey(chunk.getChunkKey());
        TileValue value1 = CacheManager.getInstance().getCache().getUnchecked(key1.getKey());
        value1.setOwner(player.getName());
        printLocalNode(player);
        TileManager.claimTile(chunk,key1, value1);

    }
    @CommandMethod("nodes info")
    @CommandDescription("Node info")
    public void info(final Player player){
        printLocalNode(player);
        //RedisConnection.getInstance().getConnection().set(key,value);
    }

    private void printLocalNode(Player player) {
        TileKey key1 = new TileKey(player.getLocation().getChunk().getChunkKey());
        TileValue value1 = CacheManager.getInstance().getCache().getUnchecked(key1.getKey());
        player.sendMessage(Component.text("Current node: \n Key - ")
                .append(Component.text(value1.getKey()))
                .append(Component.text("\n Owner: - "))
                .append(Component.text(value1.getOwner()))
                .color(NamedTextColor.YELLOW));
    }
}
