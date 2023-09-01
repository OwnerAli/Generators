package me.ogali.generators.range;

import lombok.Data;
import org.bukkit.Location;

@Data
public class Range {

    private final int x;
    private final int y;
    private final int z;
    private final boolean includeCorners;

    public boolean locationWithinRange(Location genLocation, Location placedGenOreLocation) {
        if (includeCorners) {
            return locationWithinRangeIncludingCorners(genLocation, placedGenOreLocation);
        }
        return locationWithinRangeExcludingCorners(genLocation, placedGenOreLocation);
    }

    private boolean locationWithinRangeIncludingCorners(Location genLocation, Location placedGenOreLocation) {
        int differenceInXCoord = Math.abs(genLocation.getBlockX() - placedGenOreLocation.getBlockX());
        int differenceInYCoord = Math.abs(genLocation.getBlockY() - placedGenOreLocation.getBlockY());
        int differenceInZCoord = Math.abs(genLocation.getBlockZ() - placedGenOreLocation.getBlockZ());

        return differenceInXCoord <= x || differenceInYCoord <= y || differenceInZCoord <= z;
    }

    private boolean locationWithinRangeExcludingCorners(Location genLocation, Location placedGenOreLocation) {
        int xDistance = Math.abs(genLocation.getBlockX() - placedGenOreLocation.getBlockX());
        int zDistance = Math.abs(genLocation.getBlockZ() - placedGenOreLocation.getBlockZ());
        int horizontalDistance = xDistance + zDistance;

        boolean verticalCheck = Math.abs(genLocation.getBlockY() - placedGenOreLocation.getBlockY()) <= y;

        return horizontalDistance <= x && verticalCheck;
    }

}
