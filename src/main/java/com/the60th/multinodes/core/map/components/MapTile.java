package com.the60th.multinodes.core.map.components;

import com.the60th.multinodes.land.tile.Tile;

public class MapTile {

    long chunkKey;
    MapTileType tileType;

    private MapTile(long chunkKey){
        this.chunkKey = chunkKey;
    }


    public static MapTile mapTileFromTile(Tile tile){
        return new MapTile(tile.getKey());
    }

}
