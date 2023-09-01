package me.ogali.generators.listeners;

import me.ogali.generators.GeneratorsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Objects;

public class BlockBreakListener implements Listener {

    private final GeneratorsPlugin generatorsPlugin;

    public BlockBreakListener(GeneratorsPlugin generatorsPlugin) {
        this.generatorsPlugin = generatorsPlugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        generatorsPlugin.getGeneratorRegistry().getNearestGenerator(event.getBlock().getLocation())
                .ifPresent(generator -> generator.getApplicableOreList().stream()
                        .filter(Objects::nonNull)
                        .filter(genOre -> genOre.getLocation() != null)
                        .filter(genOre -> event.getBlock().getLocation().equals(genOre.getLocation()))
                        .forEach(genOre -> {
                            event.setCancelled(true);
                            event.setDropItems(false);
                            event.setExpToDrop(0);
                            genOre.mine(event.getPlayer(), generator);
                        }));
    }

}
