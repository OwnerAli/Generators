package me.ogali.generators.range;

import lombok.Data;
import org.bukkit.Location;

@Data
public class Range {

    private final int x;
    private final int y;
    private final int z;

    public boolean locationNotWithinRange(Location genLocation, Location placedGenOreLocation) {
        int differenceInXCoord = Math.abs(genLocation.getBlockX() - placedGenOreLocation.getBlockX());
        int differenceInYCoord = Math.abs(genLocation.getBlockY() - placedGenOreLocation.getBlockY());
        int differenceInZCoord = Math.abs(genLocation.getBlockZ() - placedGenOreLocation.getBlockZ());

        return differenceInXCoord > x || differenceInYCoord > y || differenceInZCoord > z;
    }

}
