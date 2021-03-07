package me.danseb.bingo.game;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import me.danseb.bingo.Core;
import me.danseb.bingo.game.schedulers.InventoryScheduler;
import me.danseb.bingo.game.schedulers.TimeScheduler;
import me.danseb.bingo.utils.PluginUtils;
import me.danseb.bingo.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

@Getter
public class GameManager implements Listener {

    private GameState gameState;
    private GameManager gameManager;
    private HashMap<UUID, Set<GameItems>> gottenItems = new HashMap<>();
    private HashMap<UUID, int[]> rowsCompleted = new HashMap<>();
    private HashMap<UUID, int[]> filesCompleted = new HashMap<>();
    private HashMap<UUID, String> teams = new HashMap<>();
    private Set<UUID> red = new HashSet<>();
    private Set<UUID> blue = new HashSet<>();
    private Set<UUID> yellow = new HashSet<>();
    private Set<UUID> green = new HashSet<>();
    private Set<UUID> spec = new HashSet<>();
    private Location RED_LOCATION;
    private Location BLUE_LOCATION;
    private Location YELLOW_LOCATION;
    private Location GREEN_LOCATION;
    private long startTime;

    public GameManager() {
        gameManager = this;
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
        deleteOldStats();
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
        this.RED_LOCATION = TeleportUtils.findRandomSafeLocation(Bukkit.getWorld(
                Core.getInstance().getWorldManager().getMAP_ID()), Core.getInstance().getWorldManager().getBORDER());
        this.BLUE_LOCATION = TeleportUtils.findRandomSafeLocation(Bukkit.getWorld(
                Core.getInstance().getWorldManager().getMAP_ID()), Core.getInstance().getWorldManager().getBORDER());
        this.GREEN_LOCATION = TeleportUtils.findRandomSafeLocation(Bukkit.getWorld(
                Core.getInstance().getWorldManager().getMAP_ID()), Core.getInstance().getWorldManager().getBORDER());
        this.YELLOW_LOCATION = TeleportUtils.findRandomSafeLocation(Bukkit.getWorld(
                Core.getInstance().getWorldManager().getMAP_ID()), Core.getInstance().getWorldManager().getBORDER());

        int ii = new Random().nextInt(3)+1;
        for (Player player : Bukkit.getOnlinePlayers()){
            if (!teams.containsKey(player.getUniqueId())){
                switch (ii){
                    case 1:
                        setPlayerTeam(player, "RED");
                        ii++;
                        break;
                    case 2:
                        setPlayerTeam(player, "BLUE");
                        ii++;
                        break;
                    case 3:
                        setPlayerTeam(player, "YELLOW");
                        ii++;
                        break;
                    case 4:
                        setPlayerTeam(player, "GREEN");
                        ii = 1;
                        break;
                }
            }
        }

        for (UUID uuid : teams.keySet()) {
            String team = getPlayerTeam(uuid);
            switch (team) {
                case "RED":
                    red.add(uuid);
                    break;
                case "BLUE":
                    blue.add(uuid);
                    break;
                case "YELLOW":
                    yellow.add(uuid);
                    break;
                case "GREEN":
                    green.add(uuid);
                    break;
                case "NONE":
                    spec.add(uuid);
                    break;
            }
        }

        Bukkit.broadcastMessage("Starting...");
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i == 5) {
                    Bukkit.broadcastMessage("Teleporting red team to a new location...");
                    for (UUID uuid : red) {
                        Bukkit.getPlayer(uuid).teleport(RED_LOCATION);
                    }
                } else if (i == 10) {
                    Bukkit.broadcastMessage("Teleporting blue team to a new location...");
                    for (UUID uuid : blue) {
                        Bukkit.getPlayer(uuid).teleport(BLUE_LOCATION);
                    }
                } else if (i == 15) {
                    Bukkit.broadcastMessage("Teleporting yellow team to a new location...");
                    for (UUID uuid : yellow) {
                        Bukkit.getPlayer(uuid).teleport(YELLOW_LOCATION);
                    }
                } else if (i == 20) {
                    Bukkit.broadcastMessage("Teleporting green team to a new location...");
                    for (UUID uuid : green) {
                        Bukkit.getPlayer(uuid).teleport(GREEN_LOCATION);
                    }
                }

                if (i > 25 && i < 30) {
                    Bukkit.broadcastMessage("Starting in " + (30 - i) + " second(s)");
                } else if (i == 30) {
                    Bukkit.broadcastMessage("Starting now!");
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

    public void endGame() {
        setGameState(GameState.ENDING);
    }

    private void deleteOldStats() {
        for (World world : Bukkit.getServer().getWorlds()) {
            File playerdata = new File(world.getName() + "/playerdata");
            if (playerdata.exists() && playerdata.isDirectory() && playerdata.listFiles() != null)
                for (File playerFile : playerdata.listFiles())
                    playerFile.delete();
            File stats = new File(world.getName() + "/stats");
            if (stats.exists() && stats.isDirectory() && playerdata.listFiles() != null)
                for (File statFile : stats.listFiles())
                    statFile.delete();
            File advancements = new File(world.getName() + "/advancements");
            if (advancements.exists() && advancements.isDirectory() && playerdata.listFiles() != null)
                for (File advancementFile : advancements.listFiles())
                    advancementFile.delete();
        }
    }

    public String getPlayerTeam(UUID uuid) {
        return teams.get(uuid);
    }

    public boolean setPlayerTeam(Player player, String team) {
        switch (team) {
            case "RED":
            case "BLUE":
            case "YELLOW":
            case "GREEN":
                if (teams.containsKey(player.getUniqueId())) {
                    teams.replace(player.getUniqueId(), team);
                } else {
                    teams.put(player.getUniqueId(), team);
                }
                return true;
            default:
                return false;
        }
    }

    public void playerGotItem(Player player){
        Bukkit.broadcastMessage("entered playerGotItem()");
        Set<GameItems> items = getGottenItems().get(player.getUniqueId());

        GameItems[][] bingoItems = new GameItems[5][5];

        List<List<GameItems>> gameItems = Lists.partition(BingoManager.GAME_ITEMS, 5);

        for (int i = 0; i < gameItems.size(); i++){
            List<GameItems> item = gameItems.get(i);
            for (int x = 0; x < item.size(); x++)
                bingoItems[i][x] = item.get(x);
        }

        int[] rows = checkBingoHorizontalLines(items, bingoItems);
        if (!rowsCompleted.containsKey(player.getUniqueId()))
            rowsCompleted.put(player.getUniqueId(), new int[]{0, 0, 0, 0, 0});

        for (int i = 0; i < 5; i++){
            if (rows[i] == 5 && rowsCompleted.get(player.getUniqueId())[i] != 5){
                Bukkit.broadcastMessage("The player " + player.getName() + " completed a row!");
                player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                rowsCompleted.put(player.getUniqueId(), rows);
            }
        }

        int[] files = checkBingoVerticalLines(items, bingoItems);
        if (!filesCompleted.containsKey(player.getUniqueId()))
            filesCompleted.put(player.getUniqueId(), new int[]{0, 0, 0, 0, 0});

        for (int i = 0; i < 5; i++){
            if (files[i] == 5 && filesCompleted.get(player.getUniqueId())[i] != 5){
                Bukkit.broadcastMessage("The player " + player.getName() + " completed a file!");
                player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                filesCompleted.put(player.getUniqueId(), files);
            }
        }
    }

    public int[] checkBingoHorizontalLines(Set<GameItems> playerItems, GameItems[][] gameItems){
        int[] row = {0, 0, 0, 0, 0};

        for (int i = 0; i < 4; i++){
            for (int x = 0; x <= 4; x++)
                if (playerItems.contains(gameItems[i][x])){
                    row[i]++;
                }
        }

        Bukkit.broadcastMessage(row[0] +" - "+ row[1] +" - "+ row[2] +" - "+ row[3] +" - "+ row[4]);

        return row;
    }

    public int[] checkBingoVerticalLines(Set<GameItems> playerItems, GameItems[][] gameItems){
        int[] files = {0, 0, 0, 0, 0};

        for (int i = 0; i < 4; i++){
            for (int x = 0; x <= 4; x++)
                if (playerItems.contains(gameItems[x][i])){
                    files[i]++;
                }
        }

        Bukkit.broadcastMessage(files[0] +" - "+ files[1] +" - "+ files[2] +" - "+ files[3] +" - "+ files[4]);

        return files;
    }
}
