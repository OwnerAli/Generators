package me.ogali.generators.domain;

import lombok.Getter;
import me.ogali.generators.ore.domain.impl.PlacedGenOre;
import me.ogali.generators.range.Range;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractGenerator implements Generatable {

    private final String id;
    private final long genSpeedInSeconds;
    private final Material generatableMaterial;
    private final Range range;
    private final Particle particle;
    private final List<PlacedGenOre> applicableOreList;

    public AbstractGenerator(String id, long genSpeedInSeconds, Material generatableMaterial, Range range, Particle particle) {
        this.id = id;
        this.generatableMaterial = generatableMaterial;
        this.particle = particle;
        this.range = range;
        this.genSpeedInSeconds = genSpeedInSeconds;
        this.applicableOreList = new ArrayList<>();
    }

    @Override
    public void generate() {
        applicableOreList.forEach(placedGenOre -> placedGenOre.generate(Material.EMERALD_ORE));
    }

}