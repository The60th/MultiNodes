package com.the60th.multinodes.core.map.components;

import com.the60th.multinodes.util.Palette;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.awt.*;

public enum MapTileType {
    HOME('H'), CONTESTED('C'), NORMAL('N'), OTHER('O');

    char symbol;
    char enemySymbol;
    TextColor enemyColor;
    TextColor allyColor;
    MapTileType(char symbol){
        this.symbol = symbol;
        this.enemySymbol = symbol;
        this.allyColor = Palette.MIDDLE_GREEN_YELLOW;
        this.enemyColor = Palette.ASH_GRAY;
    }
}
