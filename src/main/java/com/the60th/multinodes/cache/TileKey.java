package com.the60th.multinodes.cache;

public class TileKey {
    //TODO Anything else needed to act as the key?
    long key;

    public TileKey(){

    }
    public TileKey(long key){
        this.key = key;
    }


    public String toString(){
        return "TileKey";
    }
    public static TileKey TileKeyFromString(String string){
        return new TileKey();
    }
}
