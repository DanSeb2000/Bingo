package me.danseb.bingo.commands;

import me.danseb.bingo.Core;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String team = args[0].toUpperCase();
                if (Core.getInstance().getGameManager().setPlayerTeam(player, team))
                    sender.sendMessage("Succefully changed to team " + team);
                else
                    sender.sendMessage(team + " is not a team (Available: RED/BLUE/YELLOW/GREEN)");
            }
        }
        return true;
    }
}
