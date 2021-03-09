package me.danseb.bingo.game.schedulers;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The ending scheduler.
 * This is the last scheduler in
 * the game, kicks the players,
 * deletes the custom world and
 * shut the server down.
 */
public class EndingScheduler extends BukkitRunnable {
    int i = 0;
    @Override
    public void run() {
        if (!Core.getInstance().getGameManager().getGameState().equals(GameState.ENDING))
            cancel();

        if (i == 15){
            for (Player player : Bukkit.getOnlinePlayers())
                player.kickPlayer("Game Ending");
        } else if (i == 20){
            Core.getInstance().getWorldManager().deleteWorldFiles();
        } else if (i == 25){
            Bukkit.shutdown();
            cancel();
        }
        i++;
    }

    public EndingScheduler(){
        runTaskTimer(Core.getInstance(), 0L, 20L);
    }
}
