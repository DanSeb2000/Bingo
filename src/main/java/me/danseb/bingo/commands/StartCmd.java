package me.danseb.bingo.commands;

import me.danseb.bingo.MainBingo;
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
    private final GameManager gameManager = MainBingo.getInstance().getGameManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (gameManager.getGameState() == GameState.WAITING) {
            if (sender.hasPermission("bingo.start")){
                gameManager.preStartGame(true);
            } else {
                sender.sendMessage(Language.NOT_PREMISSION.getMessage());
            }
        } else {
            switch (gameManager.getGameState()){
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
