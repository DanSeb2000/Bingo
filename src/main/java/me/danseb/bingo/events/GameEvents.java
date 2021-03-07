package me.danseb.bingo.events;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.GameState;
import me.danseb.bingo.game.Teams;
import me.danseb.bingo.world.WorldManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GameEvents implements Listener {
    private final GameManager gameManager;
    private final WorldManager worldManager;

    public GameEvents(){
        gameManager = Core.getInstance().getGameManager();
        worldManager = Core.getInstance().getWorldManager();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event){
        GameState gs = gameManager.getGameState();
        switch (gs){
            case LOADING:
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Game still loading");
                break;
            case ENDING:
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Game ending");
                break;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        switch (gameManager.getGameState()) {
            case WAITING:
                player.teleport(worldManager.getSPAWN());
                gameManager.setPlayerTeam(player, Teams.NONE);
                break;
            case PLAYING:
                break;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        switch (gameManager.getGameState()){
            case PLAYING:
            case ENDING:
                event.setRespawnLocation(gameManager.playerLoc.get(event.getPlayer().getUniqueId()));
                break;
            default:
                event.setRespawnLocation(worldManager.getSPAWN());
                break;
        }
    }
}
