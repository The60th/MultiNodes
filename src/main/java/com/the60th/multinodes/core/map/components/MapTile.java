package com.the60th.multinodes.core.map.components;

import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.land.tile.Tile;
import com.the60th.multinodes.util.Palette;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class MapTile {

    long chunkKey;
    MapTileType tileType = MapTileType.NORMAL;
    char symbol = '█';
    private MapTile(long chunkKey){
        this.chunkKey = chunkKey;
    }

    public Component getTranslatedComponent(Player player){
        long playerKey = player.getLocation().getChunk().getChunkKey();

        if(playerKey == chunkKey){
            return Component.text(symbol).color(Palette.MIDDLE_GREEN_YELLOW);
        }
        Tile tile = CacheManager.getInstance().getCache().getUnchecked(chunkKey);

        if(tile.getOwner().equalsIgnoreCase("the60th")){
            return Component.text(symbol).color(Palette.TANGERINE);
        }else if(tile.getOwner().equalsIgnoreCase("Wukio13IsMyQueen")){
            return Component.text(symbol).color(Palette.ASH_GRAY);
        }
        return Component.text(symbol).color(Palette.DEEP_SPACE);
    }
    public static MapTile mapTileFromTile(Tile tile){
        return new MapTile(tile.getKey());
    }

}
