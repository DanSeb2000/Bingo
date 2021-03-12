package me.danseb.bingo.game;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import me.danseb.bingo.Core;
import me.danseb.bingo.game.schedulers.EndingScheduler;
import me.danseb.bingo.game.schedulers.InventoryScheduler;
import me.danseb.bingo.game.schedulers.TimeScheduler;
import me.danseb.bingo.utils.Language;
import me.danseb.bingo.utils.PluginUtils;
import me.danseb.bingo.utils.Settings;
import me.danseb.bingo.utils.TeleportUtils;
import me.danseb.bingo.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Game Manager
 *
 * Oh god, this is huge, but almost everything is here, for now it will stay like this,
 * I have plans to still optimize this and order it better.
 */
@Getter
public class GameManager implements Listener {

    private GameState gameState;
    private GameManager gameManager;
    private WorldManager worldManager;
    public HashMap<UUID, Location> playerLoc = new HashMap<>();
    private HashMap<Teams, Set<GameItems>> gottenItems = new HashMap<>();
    private HashMap<Teams, int[]> rowsCompleted = new HashMap<>();
    private HashMap<Teams, int[]> filesCompleted = new HashMap<>();
    private HashMap<Teams, Set<UUID>> teams;
    private HashMap<Teams, Location> teamsLocation = new HashMap<>();
    private long startTime;

    public GameManager() {
        gameManager = this;
        worldManager = Core.getInstance().getWorldManager();
        teams = new HashMap<Teams, Set<UUID>>() {
            {
                put(Teams.SPEC, new HashSet<>());
                put(Teams.RED, new HashSet<>());
                put(Teams.YELLOW, new HashSet<>());
                put(Teams.GREEN, new HashSet<>());
                put(Teams.BLUE, new HashSet<>());
            }
        };
    }

    public synchronized GameState getGameState() {
        return this.gameState;
    }

    /**
     * Sets a state for the game, depending of every state the plugin will do one
     * or other things.
     *
     * @param gameState States from the Enums, non null.
     */
    public void setGameState(@NonNull GameState gameState) {
        if (this.gameState == gameState)
            return;
        this.gameState = gameState;
    }

    /**
     * First thing to run, here the custom map will execute and after
     * that players can enter the server.
     */
    public void newGame() {
        setGameState(GameState.LOADING);
        worldManager.createNewMap();
        preGame();
    }

    /**
     * Pre game, or the waiting zone
     * Here the plugin will wait to the signal of starting the game.
     */
    public void preGame() {
        setGameState(GameState.WAITING);
        PluginUtils.sendLog(Language.INFO.getMessage(), Language.PLAYERS_CAN_ENTER.getMessage());
    }

    /**
     * Before the oficial start of the game here will teleport all the players to the
     * map an will generate the bingo card, also if a player didn't select a team this
     * will automatically and randomlly select one team for the player.
     */
    public void preStartGame() {
        setGameState(GameState.STARTING);
        World world = Bukkit.getWorld(worldManager.getMapId());

        world.setTime(0);
        for (Teams team : Teams.values()) {
            if (team == Teams.SPEC) {
                this.teamsLocation.put(Teams.SPEC, new Location(world, 0, 100, 0));
            } else if (team != Teams.NONE) {
                this.teamsLocation.put(team, TeleportUtils
                        .findRandomSafeLocation(world,
                                worldManager.getBorder()));
            }
        }

        Random random = new Random();
        for (Player player : Bukkit.getOnlinePlayers()) {
            int randomTeam = random.nextInt(4);
            if (getPlayerTeam(player.getUniqueId()).equals(Teams.NONE)) {
                setPlayerTeam(player, Teams.values()[randomTeam]);
            }
            player.setGameMode(GameMode.SPECTATOR);
        }

        Bukkit.broadcastMessage(Language.STARTING.getMessage());
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i == 5) {
                    Bukkit.broadcastMessage(Language.TELEPORING_TEAMS.getMessage());
                    teleportTeam(Teams.RED);
                    teleportTeam(Teams.BLUE);
                    teleportTeam(Teams.GREEN);
                    teleportTeam(Teams.YELLOW);
                }

                if (i % 5 == 0 && i <= 25){
                    Bukkit.broadcastMessage(Language.STARTING_IN.getMessage()
                            .replace("%second%", String.valueOf(30-i)));
                }
                if (i > 25 && i < 30) {
                    Bukkit.broadcastMessage(Language.STARTING_IN.getMessage()
                            .replace("%second%", String.valueOf(30-i)));
                } else if (i == 30) {
                    Bukkit.broadcastMessage(Language.STARTING_NOW.getMessage());
                    teleportTeam(Teams.SPEC);
                    new BingoManager(Settings.DIFFICULTY.asInt());
                    startGame();
                    cancel();
                }
                i++;
            }
        };

        runnable.runTaskTimer(Core.getInstance(), 20L, 20L);

    }

    /**
     * The game will start here, Time and Inventory schedulers will start and
     * the game oficially works.
     */
    public void startGame() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        setGameState(GameState.PLAYING);
        startTime = System.currentTimeMillis();
        new InventoryScheduler();
        new TimeScheduler();
    }

    /**
     * The EndGameTM
     * If anything reach here, this will end the game with the specified team.
     *
     * @param winner Winner team, set NONE to end the game with no winners.
     */
    public void endGame(@NonNull Teams winner) {
        setGameState(GameState.ENDING);
        if (winner == Teams.NONE) {
            Bukkit.broadcastMessage(Language.NO_WINNERS.getMessage());
        } else {
            Bukkit.broadcastMessage(Language.WINNER.getMessage()
                    .replace("%team%", winner.getColoredName()));
        }

        new EndingScheduler();
    }

    private void teleportTeam(Teams team){
        for (UUID uuid : teams.get(team)) {
            Bukkit.getPlayer(uuid).teleport(teamsLocation.get(team));
            playerLoc.put(uuid, teamsLocation.get(team));
        }
    }

    /**
     * Method that
     * @return the player team
     * @param uuid Player Unique ID
     */
    public Teams getPlayerTeam(UUID uuid) {
        AtomicReference<Teams> playerTeam = new AtomicReference<>(Teams.NONE);
        teams.forEach((team, uuids) -> {
            if (uuids.contains(uuid)) {
                playerTeam.set(team);
            }
        });
        return playerTeam.get();
    }

    /**
     * Set the player's team.
     * @param player Player to change the team.
     * @param team Team of the player.
     * @return Boolean of the success, if false there was an error.
     */
    public boolean setPlayerTeam(Player player, Teams team) {
        if (team == null){
            return false;
        }
        Teams oldTeam = getPlayerTeam(player.getUniqueId());
        if (teams.get(oldTeam) != null){
            teams.get(oldTeam).remove(player.getUniqueId());
        }
        return teams.get(team).add(player.getUniqueId());
    }

    /**
     * A method that checks the team items to continue the game or finish
     * it if the team completed the required goal.
     *
     * @param team Team that got the item.
     */
    public void teamGotItem(Teams team) {
        Set<GameItems> items = getGottenItems().get(team);
        GameItems[][] bingoItems = new GameItems[5][5];
        List<List<GameItems>> gameItems = Lists.partition(BingoManager.GAME_ITEMS, 5);

        for (int i = 0; i < gameItems.size(); i++) {
            List<GameItems> item = gameItems.get(i);
            for (int x = 0; x < item.size(); x++)
                bingoItems[i][x] = item.get(x);
        }

        int[] rows = checkBingoRows(items, bingoItems);
        if (!rowsCompleted.containsKey(team))
            rowsCompleted.put(team, new int[]{0, 0, 0, 0, 0});

        for (int i = 0; i < 5; i++) {
            if (rows[i] == 5 && rowsCompleted.get(team)[i] != 5) {
                Bukkit.broadcastMessage(Language.GOT_ROW.getMessage()
                        .replace("%team%", team.getColoredName()));
                //team.getLocation().getWorld().spawnEntity(team, EntityType.FIREWORK);
                rowsCompleted.put(team, rows);
                if (!Settings.FULLCARD.asBoolean()) {
                    endGame(team);
                }
            }
        }

        int[] files = checkBingoLines(items, bingoItems);
        if (!filesCompleted.containsKey(team))
            filesCompleted.put(team, new int[]{0, 0, 0, 0, 0});

        for (int i = 0; i < 5; i++) {
            if (files[i] == 5 && filesCompleted.get(team)[i] != 5) {
                Bukkit.broadcastMessage(Language.GOT_FILE.getMessage()
                        .replace("%team%", team.getColoredName()));
                //team.getLocation().getWorld().spawnEntity(team.getLocation(), EntityType.FIREWORK);
                filesCompleted.put(team, files);
                if (!Settings.FULLCARD.asBoolean()) {
                    endGame(team);
                }
            }
        }

        if (Settings.FULLCARD.asBoolean()){
            int file = 0, row = 0;
            for (int i = 0; i<5; i++){
                if (filesCompleted.get(team)[i] == 5){
                    file++;
                }
                if (rowsCompleted.get(team)[i] == 5){
                    row++;
                }
            }
            if (file == 5 && row == 5){
                endGame(team);
            }
        }
    }

    /**
     * Check rows of the card.
     * @param teamItems The items of the current team.
     * @param gameItems A X and Y coords of the game items.
     * @return An array of int, that works as coordinates.
     */
    public int[] checkBingoRows(Set<GameItems> teamItems, GameItems[][] gameItems) {
        int[] row = {0, 0, 0, 0, 0};

        for (int i = 0; i <= 4; i++) {
            for (int x = 0; x <= 4; x++)
                if (teamItems.contains(gameItems[i][x])) {
                    row[i]++;
                }
        }
        return row;
    }

    /**
     * Check lines of the card.
     * @param teamItems The items of the current team.
     * @param gameItems A X and Y coords of the game items.
     * @return An array of int, that works as coordinates.
     */
    public int[] checkBingoLines(Set<GameItems> teamItems, GameItems[][] gameItems) {
        int[] files = {0, 0, 0, 0, 0};

        for (int i = 0; i <= 4; i++) {
            for (int x = 0; x <= 4; x++)
                if (teamItems.contains(gameItems[x][i])) {
                    files[i]++;
                }
        }
        return files;
    }
}
