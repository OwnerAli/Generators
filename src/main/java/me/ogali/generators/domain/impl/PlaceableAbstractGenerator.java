package me.ogali.generators.domain.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.generators.domain.AbstractGenerator;
import me.ogali.generators.domain.Placeable;
import me.ogali.generators.range.Range;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

@Getter
public abstract class PlaceableAbstractGenerator extends AbstractGenerator implements Placeable {

    @Setter
    private Location placedLocation;

    public PlaceableAbstractGenerator(String id, long genSpeedInSeconds, Range range, Particle particle) {
        super(id, genSpeedInSeconds, range, particle);
    }

}
