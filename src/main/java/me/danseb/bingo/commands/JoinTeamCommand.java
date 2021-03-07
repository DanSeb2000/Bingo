package me.danseb.bingo.commands;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.Teams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinTeamCommand implements CommandExecutor {
    private final GameManager gameManager;

    public JoinTeamCommand (){
        gameManager = Core.getInstance().getGameManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Teams team = Teams.fromName(args[0]);
                if (gameManager.setPlayerTeam(player, team))
                    sender.sendMessage("Succefully changed to team " + team);
                else
                    sender.sendMessage(team + " is not a team (Available: RED/BLUE/YELLOW/GREEN/SPEC)");
            }
        }
        return true;
    }

}
