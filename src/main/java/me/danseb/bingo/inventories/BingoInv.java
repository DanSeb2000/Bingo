package me.danseb.bingo.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.danseb.bingo.Core;
import me.danseb.bingo.game.BingoManager;
import me.danseb.bingo.game.GameItems;
import me.danseb.bingo.game.Teams;
import me.danseb.bingo.utils.Language;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Bingo Inventory
 * The bingo card, I use SmartInvs to
 * create it, credit to it's creator: MinusKube
 * Github: https://github.com/MinusKube/SmartInvs
 *
 * Also a dependency.
 */
public class BingoInv implements InventoryProvider {
    public static final SmartInventory BINGO_INV = SmartInventory.builder()
            .manager(Core.getInstance().getInvManager())
            .id("bingoInv")
            .provider(new BingoInv())
            .size(5, 9)
            .title(ChatColor.GOLD + "Bingo!")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        ArrayList<GameItems> items = BingoManager.GAME_ITEMS;

        int y = 0;
        for (int i = 0; i <= 4; i++) {
            for (int x = 2; x <= 6; x++) {
                GameItems item = items.get(y);
                ItemStack itemStack = new ItemStack(item.getType(), 1, item.getDurability());

                ItemMeta meta = itemStack.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.WHITE + Language.GOT_BY.getMessage());

                for (Teams team : Core.getInstance().getGameManager().getGottenItems().keySet()) {
                    for (GameItems itemGot : Core.getInstance().getGameManager().getGottenItems().get(team)) {
                        if (itemGot.equals(item)) {
                            lore.add(team.getColoredName());
                        }
                    }
                }

                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                contents.set(i, x, ClickableItem.empty(itemStack));
                y++;
            }
        }
        contents.fillColumn(0, ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
        contents.fillColumn(1, ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
        contents.fillColumn(7, ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
        contents.fillColumn(8, ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
