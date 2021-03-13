package me.danseb.bingo.game.schedulers;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.GameState;
import me.danseb.bingo.utils.Language;
import me.danseb.bingo.utils.PluginUtils;
import me.danseb.bingo.utils.Settings;
import me.danseb.bingo.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The ending scheduler.
 * This is the last scheduler in the game, kicks the players,
 * deletes the custom world and shut the server down.
 */
public class EndingScheduler extends BukkitRunnable {
    private Core plugin;
    private GameManager gameManager;
    private WorldManager worldManager;
    int i = 0;

    @Override
    public void run() {
        if (!gameManager.getGameState().equals(GameState.ENDING))
            cancel();

        if (i % 5 == 0 && i <= 10){
            Bukkit.broadcastMessage(Language.ENDING_IN.getMessage()
                    .replace("%second%", String.valueOf(15-i)));
        } else if (i > 10 && i < 15){
            Bukkit.broadcastMessage(Language.ENDING_IN.getMessage()
                    .replace("%second%", String.valueOf(15-i)));

        }
        if (i == 15){
            for (Player player : Bukkit.getOnlinePlayers())
                player.kickPlayer(Language.ENDING.getMessage());
        } else if (i == 20){
            if (!(worldManager.deleteWorldFiles(worldManager.getMapId()))){
                PluginUtils.sendLog(Language.ERROR.getMessage(),
                        "§4[THIS IS NOT A BUG] §7-§c Couldn't delete world, deleting next start...");
                Settings.OLD_WORLD.setObject(worldManager.getMapId());
            } else {
                PluginUtils.sendLog(Language.INFO.getMessage(), "World Deleted.");
                Settings.OLD_WORLD.setObject("0");
            }
        } else if (i == 25){
            Bukkit.shutdown();
            cancel();
        }
        i++;
    }

    public EndingScheduler(){
        plugin = Core.getInstance();
        gameManager = plugin.getGameManager();
        worldManager = plugin.getWorldManager();
        runTaskTimer(Core.getInstance(), 0L, 20L);
    }
}
