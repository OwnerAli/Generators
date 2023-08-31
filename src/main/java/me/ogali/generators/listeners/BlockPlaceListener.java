package me.ogali.generators.listeners;

import me.ogali.generators.domain.Placeable;
import me.ogali.generators.ore.PlacedGenOre;
import me.ogali.generators.registries.GeneratorRegistry;
import me.ogali.generators.utils.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    private final GeneratorRegistry generatorRegistry;

    public BlockPlaceListener(GeneratorRegistry generatorRegistry) {
        this.generatorRegistry = generatorRegistry;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Location blockPlacedLocation = event.getBlockPlaced().getLocation();
        Material blockPlacedMaterial = event.getBlockPlaced().getType();
        Player player = event.getPlayer();

        if (blockPlacedMaterial == Material.COARSE_DIRT) {
            generatorRegistry.getNearestGenerator(blockPlacedLocation)
                            .ifPresent(generator -> {
                                if (generator.getRange().locationNotWithinRange(generator.getPlacedLocation(),
                                        blockPlacedLocation)) return;

                                generator.getApplicableOreList().add(new PlacedGenOre(blockPlacedLocation, 1));
                                player.sendMessage("GEN ORE PLACED!");
                            });
            return;
        }

        generatorRegistry.getGeneratorByItem(event.getItemInHand())
                .ifPresent(generator -> {
                    generatorRegistry.getNearestGenerator(blockPlacedLocation)
                            .ifPresent(nearestGenerator -> {
                                if (generator.getRange().locationNotWithinRange(nearestGenerator.getPlacedLocation(), blockPlacedLocation)) return;
                                Chat.tell(player, "&cYou can't place this generator here, it's too close to another one!");
                                event.setCancelled(true);
                            });
                    if (!(generator instanceof Placeable placeable)) return;
                    placeable.place(player, blockPlacedLocation);
                });
    }

}
