package me.danseb.bingo.utils;

import me.danseb.bingo.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public enum Language {
    PLAYERS_CAN_ENTER("PreGame.PlayersCanEnter", "&6Players can enter now."),
    SELECT_TEAM("PreGame.SelectTeam", "&0Select your team"),
    EXIT("PreGame.Exit", "&fExit"),
    CHANGED_TEAM("PreGame.ChangedTeam", "&eSuccefully changed to %team% &eteam."),
    CHANGED_SPEC("PreGame.ChangedSpec","&eSuccefully changed to spectator."),
    TELEPORING_TEAMS("PreGame.TeleportingTeams", "&bTeleporting teams to a new location..."),
    STARTING("PreGame.Starting", "&eStarting..."),
    STARTING_IN("PreGame.StartingIn", "&eStarting in %second% &esecond(s)"),
    STARTING_NOW("PreGame.Start", "&eStarting now!"),
    GOT_ROW("Game.RowCompleted", "&bThe %team% &bteam completed a row!"),
    GOT_FILE("Game.LineCompleted", "&bThe %team% &bteam completed a file!"),
    GOT_ITEM("Game.ItemGot", "&bThe %team% &bteam got an item! in &6%time%"),
    GOT_BY("Game.GotBy", "&fGot by:"),
    WINNER("Game.Winner", "&eThe %team% &eteam won the game!"),
    NO_WINNERS("Game.NoWinners", "&cThe game has ended with no winners."),
    ENDING_IN("Game.EndingIn", "&eEnding in %second% &esecond(s)"),
    ENDING("Game.Ending", "The game is ending"),

    INFO("Prefix.Info","Info"),
    ERROR("Prefix.Error", "Error"),

    VERSION_ERROR("Log.VersionError",
            "&4This server version &7(&f%version%&7) &4is not compatible with Bingo!"),
    DISABLING("Log.Disabling", "&fDisabling Bingo!"),
    LOADING("Log.Loading", "&fLoading Bingo!"),
    ENABLING_SUCCESS("Log.Enabled", "&fBingo! succefully enabled."),
    WORLD_CREATING("Log.CreatingMap", "&fCreating map: (%map%)"),
    WORLD_CREATED("Log.CreatedMap", "&fMap created."),

    RED("Team.Red", "Red"),
    BLUE("Team.Blue", "Blue"),
    YELLOW("Team.Yellow", "Yellow"),
    GREEN("Team.Green", "Green"),
    SPEC("Team.Spec", "Spectator"),

    PLAYER_COMMAND("Commands.OnlyPlayer",
            "&cOnly a player can execute that command."),
    BINGOCARD_NOTSTARTED("Commands.BingoCardNotStarted",
            "&cThis command can be executed during the game."),
    JOIN_NOT_A_TEAM("Commands.NotATeam",
            "&c%string% &cis not a team &7(Available: RED/BLUE/YELLOW/GREEN/SPEC)"),
    ERROR_START_LOADING("Commands.ErrorLoading",
            "&cError, the game is loading."),
    ERROR_START_STARTING("Commands.ErrorStarting",
            "&cError, the game is already starting."),
    ERROR_START_PLAYING("Commands.ErrorPlaying",
            "&cError, the game has started."),
    ERROR_START_ENDING("Commands.ErrorEnding",
            "&cError, the game is finishing."),
    ERROR_NOT_WAITING_STATE("Commands.OnlyWaitingTeam",
            "&cYou can't change your team."),
    SPAWN_CHANGED("Commands.SpawnChanged",
            "&eSuccefully changed spawnpoint.")

    ;

    private final String path;
    private String message;

    Language(String path, String message) {
        this.path = path;
        this.message = message;
    }

    public static void load() {
        File file = new File(Core.getInstance().getDataFolder(), "language.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        for (Language value : values()) {
            String message = value.getMessage();
            if (fileConfiguration.contains(value.path)) {
                value.setMessage(fileConfiguration.getString(value.path, message));
            } else {
                fileConfiguration.set(value.path, message);
            }
        }
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getMessage() {
        return c(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static void broadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(player, message);
        }
        Bukkit.getConsoleSender().sendMessage(c(message));
    }

    public static void send(CommandSender commandSender, String msg) {
        commandSender.sendMessage(c(msg));
    }

    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }


}
