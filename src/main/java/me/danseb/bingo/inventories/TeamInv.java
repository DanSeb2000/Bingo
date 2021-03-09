package me.danseb.bingo.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.Teams;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Team Inventory
 * The team selector inventory.
 * Same as the Bingo Inventory it uses SmartInv to
 * create it, credit to it's creator: MinusKube
 * Github: https://github.com/MinusKube/SmartInvs
 */
public class TeamInv implements InventoryProvider {
    private final GameManager gameManager;
    public static final SmartInventory TEAM_INV = SmartInventory.builder()
            .id("teamInv")
            .provider(new TeamInv())
            .size(1, 9)
            .title(ChatColor.BLACK + "Select your team")
            .build();

    public TeamInv(){
        this.gameManager = Core.getInstance().getGameManager();
    }
    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, ClickableItem.of(
                new ItemStack(Material.WOOL, 1, (short) 14),
                e -> {
                    if (gameManager.setPlayerTeam(player, Teams.RED))
                        player.sendMessage("Succefully changed to red team.");
                    player.closeInventory();
                }));
        contents.set(0, 1, ClickableItem.of(
                new ItemStack(Material.WOOL, 1, (short) 11),
                e -> {
                    if(gameManager.setPlayerTeam(player, Teams.BLUE))
                        player.sendMessage("Succefully changed to blue team.");
                    player.closeInventory();
                }));
        contents.set(0, 2, ClickableItem.of(
                new ItemStack(Material.WOOL, 1, (short) 4),
                e -> {
                    if (gameManager.setPlayerTeam(player, Teams.YELLOW))
                        player.sendMessage("Succefully changed to yellow team.");
                    player.closeInventory();
                }));
        contents.set(0, 3, ClickableItem.of(
                new ItemStack(Material.WOOL, 1, (short) 5),
                e -> {
                    if (gameManager.setPlayerTeam(player, Teams.GREEN))
                        player.sendMessage("Succefully changed to green team.");
                    player.closeInventory();
                }));

        for (int i = 4; i < 7; i++) {
            contents.set(0, i, ClickableItem.empty(
                    new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
        }
        contents.set(0, 7, ClickableItem.of(
                new ItemStack(Material.EYE_OF_ENDER, 1),
                e -> {
                    if (gameManager.setPlayerTeam(player, Teams.SPEC))
                        player.sendMessage("Succefully changed to spectator.");
                    player.closeInventory();
                }));
        contents.set(0, 8, ClickableItem.of(
                new ItemStack(Material.BARRIER, 1),
                e -> player.closeInventory()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
