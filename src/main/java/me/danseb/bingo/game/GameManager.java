package me.danseb.bingo.game;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import me.danseb.bingo.Core;
import me.danseb.bingo.game.schedulers.EndingScheduler;
import me.danseb.bingo.game.schedulers.InventoryScheduler;
import me.danseb.bingo.game.schedulers.TimeScheduler;
import me.danseb.bingo.utils.PluginUtils;
import me.danseb.bingo.utils.TeleportUtils;
import me.danseb.bingo.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class GameManager implements Listener {

    private GameState gameState;
    private GameManager gameManager;
    private WorldManager worldManager;
    public HashMap<UUID, Location> playerLoc = new HashMap<>();
    private HashMap<Teams, Set<GameItems>> gottenItems = new HashMap<>();
    private HashMap<Teams, int[]> rowsCompleted = new HashMap<>();
    private HashMap<Teams, int[]> filesCompleted = new HashMap<>();
    private HashMap<Teams, Set<UUID>> teams = new HashMap<>();
    private HashMap<Teams, Location> teamsLocation = new HashMap<>();
    private long startTime;
    private boolean oneLineWin = true;

    public GameManager() {
        gameManager = this;
        worldManager = Core.getInstance().getWorldManager();
    }

    public synchronized GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(@NonNull GameState gameState) {
        if (this.gameState == gameState)
            return;
        this.gameState = gameState;
    }

    public void newGame() {
        setGameState(GameState.LOADING);
        Core.getInstance().getWorldManager().createNewMap();
        preGame();
    }

    public void preGame() {
        setGameState(GameState.WAITING);
        new BingoManager(2);
        PluginUtils.sendLog("Info", "Players can now enter.");
    }

    public void preStartGame() {
        setGameState(GameState.STARTING);
        this.teamsLocation.put(Teams.RED, TeleportUtils
                .findRandomSafeLocation(Bukkit.getWorld(worldManager.getMAP_ID()),
                        worldManager.getBORDER()));
        this.teamsLocation.put(Teams.BLUE, TeleportUtils
                .findRandomSafeLocation(Bukkit.getWorld(worldManager.getMAP_ID()),
                        worldManager.getBORDER()));
        this.teamsLocation.put(Teams.GREEN, TeleportUtils
                .findRandomSafeLocation(Bukkit.getWorld(worldManager.getMAP_ID()),
                        worldManager.getBORDER()));
        this.teamsLocation.put(Teams.YELLOW, TeleportUtils
                .findRandomSafeLocation(Bukkit.getWorld(worldManager.getMAP_ID()),
                        worldManager.getBORDER()));
        this.teamsLocation.put(Teams.SPEC, new Location(Bukkit.
                getWorld(worldManager.getMAP_ID()), 0, 100, 0));

        int ii = new Random().nextInt(3)+1;
        for (Player player : Bukkit.getOnlinePlayers()){
            if (getPlayerTeam(player.getUniqueId()).equals(Teams.NONE)){
                switch (ii){
                    case 1:
                        setPlayerTeam(player, Teams.RED);
                        ii++;
                        break;
                    case 2:
                        setPlayerTeam(player, Teams.BLUE);
                        ii++;
                        break;
                    case 3:
                        setPlayerTeam(player, Teams.GREEN);
                        ii++;
                        break;
                    case 4:
                        setPlayerTeam(player, Teams.YELLOW);
                        ii = 1;
                        break;
                }
            }
        }

        Bukkit.broadcastMessage("§fStarting...");
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i == 5) {
                    Bukkit.broadcastMessage("§fTeleporting red team to a new location...");
                    if (teams.containsKey(Teams.RED)){
                        for (UUID uuid : teams.get(Teams.RED)) {
                            Bukkit.getPlayer(uuid).teleport(teamsLocation.get(Teams.RED));
                            playerLoc.put(uuid, teamsLocation.get(Teams.RED));
                        }
                    }
                } else if (i == 10) {
                    Bukkit.broadcastMessage("§fTeleporting blue team to a new location...");
                    if (teams.containsKey(Teams.BLUE)) {
                        for (UUID uuid : teams.get(Teams.BLUE)) {
                            Bukkit.getPlayer(uuid).teleport(teamsLocation.get(Teams.BLUE));
                            playerLoc.put(uuid, teamsLocation.get(Teams.BLUE));
                        }
                    }
                } else if (i == 15) {
                    Bukkit.broadcastMessage("§fTeleporting yellow team to a new location...");
                    if (teams.containsKey(Teams.YELLOW)) {
                        for (UUID uuid : teams.get(Teams.YELLOW)) {
                            Bukkit.getPlayer(uuid).teleport(teamsLocation.get(Teams.YELLOW));
                            playerLoc.put(uuid, teamsLocation.get(Teams.YELLOW));
                        }
                    }
                } else if (i == 20) {
                    Bukkit.broadcastMessage("§fTeleporting green team to a new location...");
                    if (teams.containsKey(Teams.GREEN)) {
                        for (UUID uuid : teams.get(Teams.GREEN)) {
                            Bukkit.getPlayer(uuid).teleport(teamsLocation.get(Teams.GREEN));
                            playerLoc.put(uuid, teamsLocation.get(Teams.GREEN));
                        }
                    }
                }

                if (i > 25 && i < 30) {
                    Bukkit.broadcastMessage("§fStarting in " + (30 - i) + " second(s)");
                } else if (i == 30) {
                    Bukkit.broadcastMessage("§fStarting now!");
                    if (teams.containsKey(Teams.SPEC)) {
                        for (UUID uuid : teams.get(Teams.SPEC)) {
                            Bukkit.getPlayer(uuid).setGameMode(GameMode.SPECTATOR);
                            Bukkit.getPlayer(uuid).teleport(teamsLocation.get(Teams.SPEC));
                            playerLoc.put(uuid, teamsLocation.get(Teams.SPEC));
                        }
                    }
                    startGame();
                    cancel();
                }
                i++;
            }
        }.runTaskTimer(Core.getInstance(), 20L, 20L);

    }

    public void startGame() {
        setGameState(GameState.PLAYING);
        startTime = System.currentTimeMillis();
        new InventoryScheduler();
        new TimeScheduler();
    }

    public void endGame(Teams winner) {
        setGameState(GameState.ENDING);
        switch (winner){
            case RED:
            case BLUE:
            case GREEN:
            case YELLOW:
                Bukkit.broadcastMessage("§fThe "+ winner.getTeamName() +"§f team won the game!");
                break;
            default:
                Bukkit.broadcastMessage("§fThe game has ended with no winners");
                break;
        }

        new EndingScheduler();
    }


    public Teams getPlayerTeam(UUID uuid) {
        AtomicReference<Teams> playerTeam = new AtomicReference<>(Teams.NONE);
        teams.forEach((team, uuids) -> {
            if(uuids.contains(uuid)) {
                playerTeam.set(team);
            }
        });
        return playerTeam.get();
    }

    public boolean setPlayerTeam(Player player, Teams team) {
        Teams oldTeam = getPlayerTeam(player.getUniqueId());
        Bukkit.broadcastMessage(oldTeam.toString());
        if (oldTeam == Teams.NONE){
            teams.computeIfAbsent(oldTeam, teams -> new HashSet<>()).add(player.getUniqueId());
        }
        teams.computeIfPresent(oldTeam, (teams, uuid) -> new HashSet<>())
                .remove(player.getUniqueId());
        return teams.computeIfAbsent(team, teams -> new HashSet<>()).add(player.getUniqueId());
    }

    public void teamGotItem(Teams team){
        Set<GameItems> items = getGottenItems().get(team);
        GameItems[][] bingoItems = new GameItems[5][5];
        List<List<GameItems>> gameItems = Lists.partition(BingoManager.GAME_ITEMS, 5);

        for (int i = 0; i < gameItems.size(); i++){
            List<GameItems> item = gameItems.get(i);
            for (int x = 0; x < item.size(); x++)
                bingoItems[i][x] = item.get(x);
        }

        int[] rows = checkBingoHorizontalLines(items, bingoItems);
        if (!rowsCompleted.containsKey(team))
            rowsCompleted.put(team, new int[]{0, 0, 0, 0, 0});

        for (int i = 0; i < 5; i++){
            if (rows[i] == 5 && rowsCompleted.get(team)[i] != 5){
                Bukkit.broadcastMessage("§fThe " + team.getTeamName() + "§f team completed a row!");
                //team.getLocation().getWorld().spawnEntity(team, EntityType.FIREWORK);
                rowsCompleted.put(team, rows);
                if (oneLineWin){
                    endGame(team);
                }
            }
        }

        int[] files = checkBingoVerticalLines(items, bingoItems);
        if (!filesCompleted.containsKey(team))
            filesCompleted.put(team, new int[]{0, 0, 0, 0, 0});

        for (int i = 0; i < 5; i++){
            if (files[i] == 5 && filesCompleted.get(team)[i] != 5){
                Bukkit.broadcastMessage("§fThe " + team.getTeamName() + "§f team completed a file!");
                //team.getLocation().getWorld().spawnEntity(team.getLocation(), EntityType.FIREWORK);
                filesCompleted.put(team, files);
                if (oneLineWin){
                    endGame(team);
                }
            }
        }
    }

    public int[] checkBingoHorizontalLines(Set<GameItems> teamItems, GameItems[][] gameItems){
        int[] row = {0, 0, 0, 0, 0};

        for (int i = 0; i <= 4; i++){
            for (int x = 0; x <= 4; x++)
                if (teamItems.contains(gameItems[i][x])){
                    row[i]++;
                }
        }
        return row;
    }

    public int[] checkBingoVerticalLines(Set<GameItems> teamItems, GameItems[][] gameItems){
        int[] files = {0, 0, 0, 0, 0};

        for (int i = 0; i <= 4; i++){
            for (int x = 0; x <= 4; x++)
                if (teamItems.contains(gameItems[x][i])){
                    files[i]++;
                }
        }
        return files;
    }
}
