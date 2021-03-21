package me.danseb.bingo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

/**
 * Game items
 * This is to customize the bingo
 * items, maybe I'll implement more
 * things here.
 */
@Getter @AllArgsConstructor
public class GameItems {
    private final Material type;
    private final short durability;
    private final boolean checkDurability;

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
