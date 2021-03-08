package me.danseb.bingo.world;

import lombok.Getter;
import me.danseb.bingo.Core;
import me.danseb.bingo.utils.PluginUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Getter
public class WorldManager {
    private final String MAP_ID = UUID.randomUUID().toString();
    private final int SEED = Core.getInstance().getRandom().nextInt();
    private final Location SPAWN = new Location(Bukkit.getWorld("world"), 0, 100, 0);
    private final int BORDER = 1000;

    public void createNewMap(){
        deleteOldStats();

        PluginUtils.sendLog("Info", "Creating map: ("+ MAP_ID +")");
        String settings = "{\"coordinateScale\":684.412,\"heightScale\":684.412,\"lowerLimitScale\":512.0," +
                "\"upperLimitScale\":512.0,\"depthNoiseScaleX\":200.0,\"depthNoiseScaleZ\":200.0," +
                "\"depthNoiseScaleExponent\":0.5,\"mainNoiseScaleX\":80.0,\"mainNoiseScaleY\":160.0," +
                "\"mainNoiseScaleZ\":80.0,\"baseSize\":8.5,\"stretchY\":12.0,\"biomeDepthWeight\":1.0," +
                "\"biomeDepthOffset\":0.0,\"biomeScaleWeight\":1.0,\"biomeScaleOffset\":0.0,\"seaLevel\":63," +
                "\"useCaves\":true,\"useDungeons\":true,\"dungeonChance\":100,\"useStrongholds\":true," +
                "\"useVillages\":true,\"useMineShafts\":false,\"useTemples\":true,\"useRavines\":true," +
                "\"useWaterLakes\":true,\"waterLakeChance\":4,\"useLavaLakes\":true,\"lavaLakeChance\":80," +
                "\"useLavaOceans\":false,\"fixedBiome\":-1,\"biomeSize\":2,\"riverSize\":4,\"dirtSize\":33," +
                "\"dirtCount\":0,\"dirtMinHeight\":0,\"dirtMaxHeight\":256,\"gravelSize\":33,\"gravelCount\":8," +
                "\"gravelMinHeight\":0,\"gravelMaxHeight\":256,\"graniteSize\":33,\"graniteCount\":0," +
                "\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":0," +
                "\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":0," +
                "\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0," +
                "\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64," +
                "\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8," +
                "\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8," +
                "\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1," +
                "\"lapisCenterHeight\":16,\"lapisSpread\":16}";

        WorldCreator worldCreator = new WorldCreator(MAP_ID);

        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.type(WorldType.CUSTOMIZED);
        worldCreator.generateStructures(true);
        worldCreator.seed(SEED);
        worldCreator.generatorSettings(settings);

        worldCreator.createWorld();

        World playWorld = Bukkit.getWorld(MAP_ID);
        playWorld.getWorldBorder().setCenter(0,0);
        playWorld.getWorldBorder().setSize(BORDER*2);

        PluginUtils.sendLog("Info", "Map created.");
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

    public void deleteWorldFiles(){
        World world = Bukkit.getWorld(Core.getInstance().getWorldManager().getMAP_ID());
        if (world != null) {
            world.setAutoSave(false);
            Bukkit.unloadWorld(world, false);
            for (Chunk chunk : world.getLoadedChunks()){
                chunk.unload(false);
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    FileUtils.deleteDirectory(new File(Core.getInstance().getServer().getWorldContainer()
                            + File.separator + Core.getInstance().getWorldManager().getMAP_ID()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLaterAsynchronously(Core.getInstance(), 3L);
    }
}
