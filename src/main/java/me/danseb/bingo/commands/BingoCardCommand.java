package me.danseb.bingo.commands;

import me.danseb.bingo.inventories.BingoInv;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BingoCardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only a player can execute that command");
            return true;
        }
        BingoInv.BINGO_INV.open((Player)sender);
        return true;
    }
}
