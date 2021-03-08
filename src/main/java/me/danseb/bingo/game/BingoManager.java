package me.danseb.bingo.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BingoManager {
    public static ArrayList<GameItems> GAME_ITEMS = new ArrayList<>();

    public BingoManager(int difficulty) {
        GAME_ITEMS = setGameItems(difficulty);
    }

    private ArrayList<GameItems> setGameItems(int difficulty) {
        ArrayList<GameItems> allItems = new ArrayList<>();
        ArrayList<GameItems> randomizedItems = new ArrayList<>();

        if (difficulty == 0) allItems.addAll(Arrays.asList(BingoItems.EASY_ITEMS));
        else if (difficulty == 1) allItems.addAll(Arrays.asList(BingoItems.NORMAL_ITEMS));
        else if (difficulty == 2) allItems.addAll(Arrays.asList(BingoItems.HARD_ITEMS));

        for (int i = 0; i < 25; i++) {
            randomizedItems.add(i, allItems.get(i));
        }
        Collections.shuffle(randomizedItems);

        return randomizedItems;
    }

}
