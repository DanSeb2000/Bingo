package me.danseb.bingo.utils;

import me.danseb.bingo.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PluginUtils {

    public static void sendLog(String prefix, String message) {
        Core.getInstance().getServer().getConsoleSender()
                .sendMessage(
                        ChatColor.AQUA
                        + "[Bingo!] - "
                        + ChatColor.GOLD
                        + "[" + prefix + "] "
                        + ChatColor.RESET
                        + message);
    }

    public String getCurrentTime(){
        long currentTime = System.currentTimeMillis() - Core.getInstance().getGameManager().getStartTime();
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        Date resultdate = new Date(currentTime);
        return sdf.format(resultdate);
    }

    public Scoreboard setScoreboard(){
        ScoreboardManager sbm = Bukkit.getScoreboardManager();
        Scoreboard sb = sbm.getNewScoreboard();
        Objective o = sb.registerNewObjective("White", "");

        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName(ChatColor.GREEN+"Bingo!");

        Score score1 = o.getScore(ChatColor.GRAY + "Time:");
        Score score2 = o.getScore(ChatColor.DARK_AQUA + getCurrentTime());
        Score score3 = o.getScore(ChatColor.GRAY + "Teams:");
        Score score4 = o.getScore(ChatColor.DARK_AQUA + "4");

        score1.setScore(1);
        score2.setScore(2);
        score3.setScore(3);
        score4.setScore(4);

        return sb;
    }
}
