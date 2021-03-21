package me.danseb.bingo.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.Teams;
import me.danseb.bingo.utils.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            .manager(Core.getInstance().getInvManager())
            .id("teamInv")
            .provider(new TeamInv())
            .size(1, 9)
            .title(Language.SELECT_TEAM.getMessage())
            .build();

    public TeamInv(){
        this.gameManager = Core.getInstance().getGameManager();
    }
    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack itema = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta itemMetaA = itema.getItemMeta();
        itemMetaA.setDisplayName(Teams.RED.getColoredName());
        itema.setItemMeta(itemMetaA);
        contents.set(0, 0, ClickableItem.of(
                itema,
                e -> {
                    if (gameManager.setPlayerTeam(player, Teams.RED))
                        player.sendMessage(Language.CHANGED_TEAM.getMessage()
                                .replace("%team%", Teams.RED.getColored()));
                    player.closeInventory();
                }));

        ItemStack itemb = new ItemStack(Material.WOOL, 1, (short) 11);
        ItemMeta itemMetaB = itemb.getItemMeta();
        itemMetaB.setDisplayName(Teams.BLUE.getColoredName());
        itemb.setItemMeta(itemMetaB);
        contents.set(0, 1, ClickableItem.of(
                itemb,
                e -> {
                    if(gameManager.setPlayerTeam(player, Teams.BLUE))
                        player.sendMessage(Language.CHANGED_TEAM.getMessage()
                                .replace("%team%", Teams.BLUE.getColored()));
                    player.closeInventory();
                }));

        ItemStack itemc = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta itemMetaC = itemc.getItemMeta();
        itemMetaC.setDisplayName(Teams.YELLOW.getColoredName());
        itemc.setItemMeta(itemMetaC);
        contents.set(0, 2, ClickableItem.of(
                itemc,
                e -> {
                    if (gameManager.setPlayerTeam(player, Teams.YELLOW))
                        player.sendMessage(Language.CHANGED_TEAM.getMessage()
                                .replace("%team%", Teams.YELLOW.getColored()));
                    player.closeInventory();
                }));

        ItemStack itemd = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta itemMetaD = itemd.getItemMeta();
        itemMetaD.setDisplayName(Teams.GREEN.getColoredName());
        itemd.setItemMeta(itemMetaD);
        contents.set(0, 3, ClickableItem.of(
                itemd,
                e -> {
                    if (gameManager.setPlayerTeam(player, Teams.GREEN))
                        player.sendMessage(Language.CHANGED_TEAM.getMessage()
                                .replace("%team%", Teams.GREEN.getColored()));
                    player.closeInventory();
                }));

        for (int i = 4; i < 7; i++) {
            contents.set(0, i, ClickableItem.empty(
                    new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
        }

        ItemStack iteme = new ItemStack(Material.EYE_OF_ENDER, 1);
        ItemMeta itemMetaE = iteme.getItemMeta();
        itemMetaE.setDisplayName(Teams.SPEC.getColoredName());
        iteme.setItemMeta(itemMetaE);
        contents.set(0, 7, ClickableItem.of(
                iteme,
                e -> {
                    if (gameManager.setPlayerTeam(player, Teams.SPEC))
                        player.sendMessage(Language.CHANGED_SPEC.getMessage());
                    player.closeInventory();
                }));

        ItemStack itemf = new ItemStack(Material.BARRIER, 1);
        ItemMeta itemMetaF = itemf.getItemMeta();
        itemMetaF.setDisplayName(Language.EXIT.getMessage());
        itemf.setItemMeta(itemMetaF);
        contents.set(0, 8, ClickableItem.of(
                itemf,
                e -> player.closeInventory()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
