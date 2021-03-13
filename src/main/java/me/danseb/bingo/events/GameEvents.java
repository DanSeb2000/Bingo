package me.danseb.bingo.events;

import me.danseb.bingo.Core;
import me.danseb.bingo.game.GameManager;
import me.danseb.bingo.game.GameState;
import me.danseb.bingo.game.Teams;
import me.danseb.bingo.world.WorldManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

/**
 * With GameEvents I refer to modified Spigot
 * events for the plugin, not custom events,
 * that, for now, are not implemented.
 */
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
                player.teleport(worldManager.getSpawn());
                break;
            case PLAYING:
                if (gameManager.getPlayerTeam(player.getUniqueId()) == Teams.NONE){
                    gameManager.setPlayerTeam(player, Teams.SPEC);
                    player.teleport(gameManager.getTeamsLocation().get(Teams.SPEC));
                    player.setGameMode(GameMode.SPECTATOR);
                }
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
                event.setRespawnLocation(worldManager.getSpawn());
                break;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (gameManager.getGameState() == GameState.STARTING){
            event.setCancelled(true);
        }
    }
}
