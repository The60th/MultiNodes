package com.the60th.multinodes.core.map.components;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.core.cache.CacheManager;
import com.the60th.multinodes.util.Palette;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class PlayerMap {
    private final String OBJ_NAME = "MAP_OBJ";

    private final String TEAM_NAME = "teamM";
    private final int MAP_SIZE = 7;
    //7 works!
    //11 we need to reposition the center
    Player player;
    long[][] mapKeys = new long[MAP_SIZE][MAP_SIZE];
    MapTile[][] mapTiles = new MapTile[MAP_SIZE][MAP_SIZE];

    boolean tilesLoaded = false;

    boolean mapOn = false;

    public PlayerMap(Player player){
        this.player = player;
        initMap();

    }

    private void toggleScoreBoardOn(){

        new BukkitRunnable(){
            @Override
            public void run() {
                Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective objective = board.registerNewObjective(OBJ_NAME,Criteria.DUMMY, Component.text("displayname"));
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                ChatColor[] colors = ChatColor.values();
                int score = 20;
                for (int i = 0; i < MAP_SIZE; i++){
                    //TODO No idea if this works at all !_!
                    board.registerNewTeam(TEAM_NAME + i).addEntry(colors[i].toString());
                    objective.getScore(colors[i].toString()).setScore(score);
                    score--;
                }
                player.setScoreboard(board);
                updateDisplay();
            }
        }.runTaskLater(MultiNodes.getInstance(),1L);


    }

    private void toggleScoreBoardOff(){
        Objects.requireNonNull(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR)).unregister();
    }

    private void initMap(){
        mapKeys = getMapKeys(this.player);
        getTileKeys().thenAccept(tiles->{
           this.mapTiles = tiles;
           this.tilesLoaded = true;
            toggleScoreBoardOn();
        });
    }

    public void mapOn(){
        mapOn = true;
        player.sendMessage(Component.text("Turning scoreboard map on!").color(Palette.YELLOW_GREEN_CRAYOLA));
    }
    public void mapOff(){
        player.sendMessage(Component.text("Turning scoreboard map off!").color(Palette.YELLOW_GREEN_CRAYOLA));
        mapOn = false;
        toggleScoreBoardOff();
    }

    //Update the map for a player
    //Called when during a moveEvent
    //Or by the MapManager to propagate a change to all tiles
    public void updateDisplay(){
        sendScoreBoard();
    }

    private void sendScoreBoard(){
        //The scoreboard set up

        //Check if the scoreboard exists, if it doesn't create it
        Scoreboard scoreboard = player.getScoreboard();
        if(scoreboard.getObjective(OBJ_NAME) == null){
            //Error?
            toggleScoreBoardOn();
            return;
        }

        //TODO build the map here.
        TextColor color;
        if(tracker == 0) {
            color = Palette.TANGERINE;
            tracker++;
        }else{
            color = Palette.DEEP_SPACE;
            tracker = 0;
        }

        for (int i = 0; i < MAP_SIZE; i++){
            //Objects.requireNonNull(scoreboard.getTeam(TEAM_NAME + i)).suffix(Component.text("123123").color(color));
        }

        Component[] lines = new Component[MAP_SIZE];
        Component component = Component.text("");
        for (int x = 0; x < MAP_SIZE; x++){
            for(int z = 0; z < MAP_SIZE; z++){
                //TODO Not a typo!
                //Flip the x/z reading to shift matrix to be north top row, east  right, south bottom, east left
                component = component.append(mapTiles[z][x].getTranslatedComponent(player));
            }
            lines[x] = component;
            component = Component.text("");
        }

        for (int x = 0; x < MAP_SIZE; x++){
            Objects.requireNonNull(scoreboard.getTeam(TEAM_NAME + x)).suffix(lines[x]);
        }

    }
    private int tracker = 0;
    /**
     * Update the internal values for the map
     *
     * */
    //Call this in move events and when a tile changes.
    public void updateData(){ //TODO Async
        if(!mapOn) return;
        //Take the current map keys, find the new map keys, compare the diff
        //Update the map keys,
        //Update the tile keys
        //Call updateDisplay async

        mapKeys = getMapKeys(this.player);
        getTileKeys().thenAccept(tiles->{
            this.mapTiles = tiles;
            updateDisplay();
        });
        //TODO after updating the value always queue a display update for players.
    }


    private long[][] getMapKeys(Player player) {
        World world = player.getWorld();
        int baseX = player.getLocation().getChunk().getX();
        int baseZ = player.getLocation().getChunk().getZ();

        //TODO This minus 3 should be half of MAP_SIZE rounded down
        int shiftedBaseX = baseX - 3;
        int shiftedBaseZ = baseZ - 3;

        long[][] keys = new long[MAP_SIZE][MAP_SIZE];

        for(int x = 0; x < MAP_SIZE;x++ ){
            for(int z = 0; z < MAP_SIZE; z++){
                keys[x][z] = world.getChunkAt(shiftedBaseX+x,shiftedBaseZ+z).getChunkKey();
            }
        }
        return keys;
    }

    //TODO I need to understand completable futures better
    private CompletableFuture<MapTile[][]> getTileKeys(){
        CompletableFuture<MapTile[][]> future = new CompletableFuture<>();
        MapTile[][] array = new MapTile[MAP_SIZE][MAP_SIZE];

        Executors.newCachedThreadPool().submit(() -> {
            for (int x = 0; x< MAP_SIZE; x++){
                for (int z = 0; z < MAP_SIZE; z++){
                    array[x][z] = MapTile.mapTileFromTile(CacheManager.getInstance().getCache().getUnchecked(mapKeys[x][z]));
                }
            }
            future.complete(array);
            return null;
        });
        return future;
    }

    public void updateTile(long key){
        if(!mapOn) return;
        for(int i = 0; i < MAP_SIZE; i++){
            for(int j = 0; j < MAP_SIZE; j++){
                if(mapKeys[i][j] == key) {
                    //TODO
                    //Update this specific tile.

                    long key1 = key;
                }
            }
        }
    }
}
