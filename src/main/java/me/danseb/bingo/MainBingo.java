package me.danseb.bingo;

import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import me.danseb.bingo.commands.BingoCardCmd;
import me.danseb.bingo.commands.JoinTeamCmd;
import me.danseb.bingo.commands.SetSpawnCmd;
import me.danseb.bingo.commands.StartCmd;
import me.danseb.bingo.events.Listeners;
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
public class MainBingo extends JavaPlugin {

    private static MainBingo instance;
    private GameManager gameManager;
    private WorldManager worldManager;
    private PluginUtils pluginUtils;
    private InventoryManager invManager;
    private boolean forceDisable = false;
    private Random random;

    @Override
    public void onLoad() {
        instance = this;
        PluginUtils.sendLog(Language.INFO.getMessage(), Language.LOADING.getMessage());
        /*
        String serverVersion = getServer().getVersion();

        if (!serverVersion.contains("1.12")) {
            PluginUtils.sendLog(Language.ERROR.getMessage(), Language.VERSION_ERROR.getMessage()
                    .replace("%version%", serverVersion));
            PluginUtils.sendLog(Language.INFO.getMessage(), Language.DISABLING.getMessage());
            forceDisable = true;
        }*/
    }

    @Override
    public void onEnable() {
        if (forceDisable) {
            Bukkit.getPluginManager().disablePlugin(instance);
            return;
        }

        Settings.load();
        Language.load();

        invManager = new InventoryManager(instance);
        invManager.init();
        random = new Random();
        pluginUtils = new PluginUtils();
        worldManager = new WorldManager();
        gameManager = new GameManager();

        Bukkit.getScheduler().runTaskLater(this, () -> gameManager.newGame(), 1L);

        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getCommand("bingo").setExecutor(new BingoCardCmd());
        getCommand("start").setExecutor(new StartCmd());
        getCommand("team").setExecutor(new JoinTeamCmd());
        getCommand("setspawn").setExecutor(new SetSpawnCmd());

        PluginUtils.sendLog(Language.INFO.getMessage(), Language.ENABLING_SUCCESS.getMessage());
    }

    @Override
    public void onDisable() {
        if (forceDisable) {
            return;
        }
        PluginUtils.sendLog(Language.INFO.getMessage(), Language.DISABLING.getMessage());
    }

    public static MainBingo getInstance() {
        return instance;
    }
}
