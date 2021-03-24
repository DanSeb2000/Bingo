package me.danseb.bingo.game;

import me.danseb.bingo.MainBingo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Bingo Manager... or game setter
 * I created this class with more in mind,
 * but all goes to GameManager, maybe
 * in the future I'll delete this class.
 */
public class BingoManager {
    public static ArrayList<GameItems> GAME_ITEMS = new ArrayList<>();

    public BingoManager(int difficulty) {
        GAME_ITEMS = setGameItems(difficulty);
    }

    private ArrayList<GameItems> setGameItems(int difficulty) {
        ArrayList<GameItems> allItems = new ArrayList<>();
        ArrayList<GameItems> randomizedItems = new ArrayList<>();

        if (MainBingo.getInstance().getServer().getVersion().contains("1.8")) switch (difficulty) {
            case 0:
                allItems.addAll(Arrays.asList(BingoItems1_8.EASY_ITEMS_1_8));
                break;
            case 2:
                allItems.addAll(Arrays.asList(BingoItems1_8.HARD_ITEMS_1_8));
                break;
            default:
                allItems.addAll(Arrays.asList(BingoItems1_8.NORMAL_ITEMS_1_8));
                break;
        } else switch (difficulty) {
            case 0:
                allItems.addAll(Arrays.asList(BingoItems.EASY_ITEMS_1_9));
                break;
            case 2:
                allItems.addAll(Arrays.asList(BingoItems.HARD_ITEMS_1_9));
                break;
            default:
                allItems.addAll(Arrays.asList(BingoItems.NORMAL_ITEMS_1_9));
                break;
        }

        for (int i = 0; i < 25; i++) {
            randomizedItems.add(i, allItems.get(i));
        }
        Collections.shuffle(randomizedItems);

        return randomizedItems;
    }

}
