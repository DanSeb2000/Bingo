package me.danseb.bingo.utils;

import jdk.internal.jline.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Teleport Utils
 *
 * This class is not mine, I'll credit
 * Mezy, from UhcCore, because that were
 * I got it.
 * Github: https://github.com/Mezy/UhcCore
 *
 * Here I get a safe spot for the players
 * to spawn in the custom world.
 */
public class TeleportUtils {

    private static Location getGroundLocation(Location loc, boolean allowCaves) {
        World w = loc.getWorld();
        loc.setY(0.0D);
        if (allowCaves) {
            while (loc.getBlock().getType() != Material.AIR)
                loc = loc.add(0.0D, 1.0D, 0.0D);
        } else {
            loc = w.getHighestBlockAt(loc).getLocation();
        }
        loc = loc.add(0.5D, 0.0D, 0.5D);
        return loc;
    }

    private static Location newRandomLocation(World world, double maxDistance) {
        Random r = new Random();
        double x = 2.0D * maxDistance * r.nextDouble() - maxDistance;
        double z = 2.0D * maxDistance * r.nextDouble() - maxDistance;
        return new Location(world, x, 250.0D, z);
    }

    public static Location findRandomSafeLocation(World world, double maxDistance) {
        maxDistance -= 10.0D;
        Location location = null;
        int i = 0;
        while (location == null) {
            i++;
            Location randomLoc = newRandomLocation(world, maxDistance);
            location = findSafeLocationAround(randomLoc, 10);
            if (i > 20)
                return randomLoc;
        }
        location = location.add(0.0D, 2.0D, 0.0D);
        return location;
    }

    @Nullable
    private static Location findSafeLocationAround(Location loc, int searchRadius) {
        boolean nether = loc.getWorld().getEnvironment() == World.Environment.NETHER;
        for (int x = -searchRadius; x <= searchRadius; x += 3) {
            for (int z = -searchRadius; z <= searchRadius; z += 3) {
                Location betterLocation = getGroundLocation(loc.clone().add(new Vector(x, 0, z)), nether);
                if (!nether || betterLocation.getBlockY() <= 120) {
                    Material material = betterLocation.clone().add(0.0D, -1.0D, 0.0D).getBlock().getType();
                    if (!material.equals(Material.STATIONARY_LAVA) &&
                            !material.equals(Material.STATIONARY_WATER))
                        return betterLocation;
                }
            }
        }
        return null;
    }
}
