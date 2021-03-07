package me.danseb.bingo;

import lombok.Getter;
import me.danseb.bingo.commands.BingoCardCommand;
import me.danseb.bingo.commands.JoinTeamCommand;
import me.danseb.bingo.commands.StartCommand;
import me.danseb.bingo.events.GameEvents;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.utils.PluginUtils;
import me.danseb.bingo.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Core extends JavaPlugin {
    private static Core instance;
    private GameManager gameManager;
    private WorldManager worldManager;
    private PluginUtils pluginUtils;
    private static int version;
    private boolean forceDisable = false;

    @Override
    public void onLoad() {
        instance = this;
        PluginUtils.sendLog("Info", "Loading Bingo!");
        String serverVersion = getServer().getVersion();
        if (!serverVersion.contains("1.12")) {
            PluginUtils.sendLog("Error", "This server version (" + serverVersion + ") is not compatible with Bingo!");
            PluginUtils.sendLog("Info", "Disabling Bingo!");
            forceDisable = true;
            return;
        }
        loadServerVersion();
        PluginUtils.sendLog("Info", "Bingo! loaded.");
    }

    @Override
    public void onEnable() {
        if (forceDisable) {
            Bukkit.getPluginManager().disablePlugin(instance);
            return;
        }
        this.gameManager = new GameManager();
        this.worldManager = new WorldManager();
        this.pluginUtils = new PluginUtils();
        Bukkit.getScheduler().runTaskLater(this, () -> this.gameManager.newGame(), 1L);
        getServer().getPluginManager().registerEvents(new GameEvents(), this);
        getCommand("bingo").setExecutor(new BingoCardCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("team").setExecutor(new JoinTeamCommand());
        PluginUtils.sendLog("Info", "Bingo! succefully enabled.");

    }

    @Override
    public void onDisable() {
        if (forceDisable) {
            return;
        }
        PluginUtils.sendLog("Info", "Disabling Bingo!");
        PluginUtils.sendLog("Info", "Bingo! succefully disabled.");
    }

    private void loadServerVersion() {
        String versionString = Bukkit.getBukkitVersion();
        version = 0;
        for (int i = 8; i <= 17; i++) {
            if (versionString.contains("1." + i))
                version = i;
        }
        if (version == 0) {
            version = 8;
            PluginUtils.sendLog("Warn", "Failed to detect server version (" + versionString + ")");
        } else {
            PluginUtils.sendLog("Info", "Server version 1." + version + " detected.");
        }
    }

    public static Core getInstance() {
        return instance;
    }

    public static int getVersion() {
        return version;
    }

}
