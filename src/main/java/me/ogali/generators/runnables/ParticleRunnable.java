package me.ogali.generators.runnables;

import me.ogali.generators.GeneratorsPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleRunnable extends BukkitRunnable {

    private final Location location;
    private final Particle particle;

    public ParticleRunnable(Location location, Particle particle) {
        this.location = location;
        this.particle = particle;
    }

    public void start() {
        runTaskTimer(GeneratorsPlugin.getInstance(), 0, 40);
    }

    @Override
    public void run() {
        location.getWorld().spawnParticle(particle, location.clone().add(0, 1, 0), 10);
    }

}
