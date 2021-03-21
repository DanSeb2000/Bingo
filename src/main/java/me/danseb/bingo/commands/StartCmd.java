package me.danseb.bingo.commands;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.GameState;
import me.danseb.bingo.utils.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * The /start command
 * Force start the game with the
 * current players in the match.
 */
public class StartCmd implements CommandExecutor {
    GameManager gm = Core.getInstance().getGameManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (gm.getGameState() == GameState.WAITING) {
            if (sender.hasPermission("bingo.start")){
                gm.preStartGame();
            } else {
                sender.sendMessage(Language.NOT_PREMISSION.getMessage());
            }
        } else {
            switch (gm.getGameState()){
                case LOADING:
                    sender.sendMessage(Language.ERROR_START_LOADING.getMessage());
                    break;
                case STARTING:
                    sender.sendMessage(Language.ERROR_START_STARTING.getMessage());
                    break;
                case PLAYING:
                    sender.sendMessage(Language.ERROR_START_PLAYING.getMessage());
                    break;
                case ENDING:
                    sender.sendMessage(Language.ERROR_START_ENDING.getMessage());
                    break;
            }
        }
        return true;
    }
}
