package me.danseb.bingo.commands;

import me.danseb.bingo.Core;
import me.danseb.bingo.utils.Language;
import me.danseb.bingo.utils.Settings;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (!player.hasPermission("bingo.setspawn")){
            sender.sendMessage(Language.NOT_PREMISSION.getMessage());
            return true;
        }

        Location location = player.getLocation();
        String locate = location.getWorld().getName()
                +", "+ location.getBlockX()
                +", "+ location.getBlockY()
                +", "+ location.getBlockZ()
                +", "+ location.getYaw()
                +", "+ location.getPitch();

        Settings.WORLD_SPAWN.setObject(locate);
        Core.getInstance().getWorldManager().setSpawn(Settings.WORLD_SPAWN.asLocation());
        sender.sendMessage(Language.SPAWN_CHANGED.getMessage());
        return true;
    }
}
