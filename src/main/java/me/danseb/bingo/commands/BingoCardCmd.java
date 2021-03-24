package me.danseb.bingo.commands;

import me.danseb.bingo.MainBingo;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.GameState;
import me.danseb.bingo.inventories.BingoInv;
import me.danseb.bingo.utils.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The /bingo command
 * It's a simple class tbh that
 * only opens an inventory.
 */
public class BingoCardCmd implements CommandExecutor {
    private final GameManager gameManager;

    public BingoCardCmd(){
        gameManager = MainBingo.getInstance().getGameManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(Language.PLAYER_COMMAND.getMessage());
            return true;
        }
        if (gameManager.getGameState() == GameState.PLAYING){
            BingoInv.BINGO_INV.open((Player)sender);
        } else {
            sender.sendMessage(Language.BINGOCARD_NOTSTARTED.getMessage());
        }
        return true;
    }
}
