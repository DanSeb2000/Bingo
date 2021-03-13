package me.danseb.bingo;

import lombok.Getter;
import me.danseb.bingo.commands.BingoCardCommand;
import me.danseb.bingo.commands.JoinTeamCommand;
import me.danseb.bingo.commands.SetSpawnCommand;
import me.danseb.bingo.commands.StartCommand;
import me.danseb.bingo.events.GameEvents;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.utils.Language;
import me.danseb.bingo.utils.PluginUtils;
import me.danseb.bingo.utils.Settings;
import me.danseb.bingo.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

/**
 * Hello! I'm DanSeb2000
 * I see that you decided to decompile this plugin for wharever reason, maybe see what's inside,
 * maybe to stole something or see what's wrong with my code to tell everyone that this code
 * is very basic/newie to be published here.
 *
 * Don't worry, i don't care what you will do, feel free to explore everything, for me this
 * plugin is a proof that I'm learning, maybe I'll be better in the future, maybe not and
 * I'll suck, whatever I'll be happy seeing back at this and know that this work as I expected.
 */
@Getter
public class Core extends JavaPlugin {

    private static Core instance;
    private GameManager gameManager;
    private WorldManager worldManager;
    private PluginUtils pluginUtils;
    private boolean forceDisable = false;
    private Random random;

    @Override
    public void onLoad() {
        instance = this;
        PluginUtils.sendLog(Language.INFO.getMessage(), Language.LOADING.getMessage());
        String serverVersion = getServer().getVersion();
        /*
         * If the server version is not 1.12
         * the plugin will close the server, you
         * must use 1.12
         */
        if (!serverVersion.contains("1.12")) {
            PluginUtils.sendLog(Language.ERROR.getMessage(), Language.VERSION_ERROR.getMessage()
                    .replace("%version%", serverVersion));
            PluginUtils.sendLog(Language.INFO.getMessage(), Language.DISABLING.getMessage());
            forceDisable = true;
        }
    }

    @Override
    public void onEnable() {
        if (forceDisable) {
            Bukkit.getPluginManager().disablePlugin(instance);
            return;
        }


        Settings.load();
        Language.load();

        this.random = new Random();
        this.pluginUtils = new PluginUtils();
        this.worldManager = new WorldManager();
        this.gameManager = new GameManager();

        Bukkit.getScheduler().runTaskLater(this, () -> this.gameManager.newGame(), 1L);

        getServer().getPluginManager().registerEvents(new GameEvents(), this);
        getCommand("bingo").setExecutor(new BingoCardCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("team").setExecutor(new JoinTeamCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());

        PluginUtils.sendLog(Language.INFO.getMessage(), Language.ENABLING_SUCCESS.getMessage());
    }

    @Override
    public void onDisable() {
        if (forceDisable) {
            return;
        }
        PluginUtils.sendLog(Language.INFO.getMessage(), Language.DISABLING.getMessage());
    }

    public static Core getInstance() {
        return instance;
    }
}
