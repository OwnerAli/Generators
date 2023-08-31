package me.ogali.generators.runnables;

import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.domain.AbstractGenerator;
import me.ogali.generators.domain.impl.PlaceableAbstractGenerator;
import me.ogali.generators.ore.PlacedGenOre;
import me.ogali.generators.utils.Chat;
import org.bukkit.scheduler.BukkitRunnable;

public class GeneratorOreGenRunnable extends BukkitRunnable {

    private final PlaceableAbstractGenerator placeableAbstractGenerator;

    public GeneratorOreGenRunnable(PlaceableAbstractGenerator placeableAbstractGenerator) {
        this.placeableAbstractGenerator = placeableAbstractGenerator;
    }

    public void start() {
        runTaskTimer(GeneratorsPlugin.getInstance(), placeableAbstractGenerator.getGenSpeedInSeconds() * 20, placeableAbstractGenerator.getGenSpeedInSeconds() * 20);
    }

    @Override
    public void run() {
        placeableAbstractGenerator.generate();
    }

}
