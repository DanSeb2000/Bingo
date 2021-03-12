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
public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameManager gm = Core.getInstance().getGameManager();
        if (gm.getGameState() == GameState.WAITING) {
            gm.preStartGame();
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
