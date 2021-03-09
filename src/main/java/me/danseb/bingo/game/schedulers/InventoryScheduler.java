package me.danseb.bingo.game.schedulers;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

/**
 * The inventory scheduler
 * This scheduler check the inventory
 * of every player in the match every
 * 2 ticks, and so check if the player got
 * a new bingo item to add.
 *
 * It's automatically cancels if the
 * game is finishing.
 */
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
                        Teams team = gameManager.getPlayerTeam(player.getUniqueId());
                        Set<GameItems> items = gameManager.getGottenItems().computeIfAbsent(team,
                                team1 -> new HashSet<>());
                        if (!(items.contains(gameItem))) {
                            Bukkit.broadcastMessage("The team "+ team.getColoredName() +"�f got an item! in "+ Core.getInstance()
                                    .getPluginUtils().getCurrentTime());
                            items.add(gameItem);
                            gameManager.getGottenItems().put(team, items);
                            gameManager.teamGotItem(team);
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
