package me.ogali.generators.domain;

import lombok.Getter;
import me.ogali.generators.ore.domain.impl.PlacedGenOre;
import me.ogali.generators.range.Range;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public abstract class AbstractGenerator implements Generatable {

    private final String id;
    private final long genSpeedInSeconds;
    private final Material generatableMaterial;
    private final Range range;
    private final Particle particle;
    private final String customDropsDropId;
    private final List<PlacedGenOre> applicableOreList;

    public AbstractGenerator(String id, long genSpeedInSeconds, Material generatableMaterial, Range range, Particle particle, String customDropsDropId) {
        this.id = id;
        this.generatableMaterial = generatableMaterial;
        this.particle = particle;
        this.range = range;
        this.genSpeedInSeconds = genSpeedInSeconds;
        this.customDropsDropId = customDropsDropId;
        this.applicableOreList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void generate() {
        Collections.synchronizedList(applicableOreList).forEach(placedGenOre -> placedGenOre.generate(generatableMaterial, this));
    }

}