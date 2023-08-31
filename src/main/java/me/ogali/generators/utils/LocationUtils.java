package me.ogali.generators.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class LocationUtils {

    public static Location getRandomLocationOnBlock(Location blockLocation) {
        return getRandomLocationWithinDesiredRange(-1, 1, blockLocation);
    }

    public static Location getRandomLocationWithinDesiredRange(double minRange, double maxRange, Location location) {
        Random random = new Random();
        World world = location.getWorld();

        double xOffset = minRange + (random.nextDouble() * (maxRange - minRange));
        double zOffset = minRange + (random.nextDouble() * (maxRange - minRange));

        double xLocationWithOffset = location.getX() + xOffset;
        double yLocationWithOffset = location.getY();
        double zLocationWithOffset = location.getZ() + zOffset;

        return new Location(world, xLocationWithOffset, yLocationWithOffset, zLocationWithOffset);
    }

}
