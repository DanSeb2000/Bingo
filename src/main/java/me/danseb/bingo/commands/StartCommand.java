package me.danseb.bingo.commands;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameManager gm = Core.getInstance().getGameManager();
        if (gm.getGameState() == GameState.WAITING) {
            gm.preStartGame();
        } else {
            switch (gm.getGameState()){
                case LOADING:
                    sender.sendMessage("Error, the game is loading.");
                    break;
                case STARTING:
                    sender.sendMessage("Error, the game is already starting.");
                    break;
                case PLAYING:
                    sender.sendMessage("Error, the game has started.");
                    break;
                case ENDING:
                    sender.sendMessage("Error, the game is finishing.");
                    break;
                default:
                    sender.sendMessage("Error?");
            }
        }

        return true;
    }
}
