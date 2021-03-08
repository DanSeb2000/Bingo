package me.danseb.bingo.game.schedulers;

import lombok.Getter;
import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameState;
import me.danseb.bingo.game.Teams;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class TimeScheduler extends BukkitRunnable {

    @Override
    public void run() {
        if (Core.getInstance().getGameManager().getGameState() != GameState.PLAYING) cancel();

        for (Player p : Bukkit.getOnlinePlayers()){
            //p.setScoreboard(Core.getInstance().getPluginUtils().setScoreboard());
            String time = Core.getInstance().getPluginUtils().getCurrentTime();
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(time));
            if (time.equals("25:00"))
                Core.getInstance().getGameManager().endGame(Teams.NONE);
        }
    }

    public TimeScheduler() {
        runTaskTimer(Core.getInstance(), 20L, 20L);
    }
}
