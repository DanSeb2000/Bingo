package me.danseb.bingo.events;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class GameEvents implements Listener {
    public HashMap<UUID, Location> playerLoc = new HashMap<>();

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event){
        GameState gs = Core.getInstance().getGameManager().getGameState();
        System.out.println(gs);
        switch (gs){
            case LOADING:
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Game still loading.");
                break;
            case WAITING:
            case STARTING:
            case PLAYING:
                event.allow();
                break;
            case ENDING:
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Game ending.");
                break;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        switch (Core.getInstance().getGameManager().getGameState()) {
            case WAITING:
                player.teleport(Core.getInstance().getWorldManager().getSPAWN());
                Core.getInstance().getGameManager().getTeams().put(player.getUniqueId(), "NONE");
                break;
            case PLAYING:
                break;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
    }

}
