package me.danseb.bingo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter @AllArgsConstructor
public class GameItems {
    private Material type;
    private short durability;
    private boolean checkDurability;

    public GameItems(Material type){
        this.type = type;
        this.durability = 0;
        this.checkDurability = false;
    }

    public GameItems(Material type, short durability){
        this.type = type;
        this.durability = durability;
        this.checkDurability = true;
    }

}
