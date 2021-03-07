package me.danseb.bingo.game.schedulers;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.BingoManager;
import me.danseb.bingo.game.GameItems;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class InventoryScheduler extends BukkitRunnable {
    GameManager gameManager = Core.getInstance().getGameManager();
    @Override
    public void run() {

        if (gameManager.getGameState() == GameState.ENDING)
            cancel();

        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory inventory = player.getInventory();
            for (ItemStack item : inventory.getContents()) {
                if (item == null) continue;
                for (GameItems gameItem : BingoManager.GAME_ITEMS) {
                    if (item.getType() == gameItem.getType()) {
                        if (gameItem.isCheckDurability() && item.getDurability() != gameItem.getDurability()) continue;
                        Set<GameItems> items = gameManager.getGottenItems().computeIfAbsent(player.getUniqueId(),
                                uuid -> new HashSet<>());
                        if (!(items.contains(gameItem))) {
                            Bukkit.broadcastMessage(player.getName() + " got an item! in "+ Core.getInstance().getPluginUtils().getCurrentTime());
                            items.add(gameItem);
                            gameManager.getGottenItems().put(player.getUniqueId(), items);
                            gameManager.playerGotItem(player);
                            item.setAmount(item.getAmount()-1);
                        }
                    }
                }
            }
        }
    }

    public InventoryScheduler() {
        runTaskTimer(Core.getInstance(), 1L, 2L);
    }
}
