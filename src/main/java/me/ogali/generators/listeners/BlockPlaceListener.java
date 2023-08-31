package me.ogali.generators.listeners;

import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.domain.Placeable;
import me.ogali.generators.registries.GenOreRegistry;
import me.ogali.generators.registries.GeneratorRegistry;
import me.ogali.generators.utils.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    private final GeneratorsPlugin generatorsPlugin;

    public BlockPlaceListener(GeneratorsPlugin generatorsPlugin) {
        this.generatorsPlugin = generatorsPlugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (generatorsPlugin.getGeneratorRegistry().getGeneratorByItem(event.getItemInHand()).isEmpty()
                && generatorsPlugin.getGenOreRegistry().getGenOreFromItem(event.getItemInHand()).isEmpty()) return;

        GeneratorRegistry generatorRegistry = generatorsPlugin.getGeneratorRegistry();
        GenOreRegistry genOreRegistry = generatorsPlugin.getGenOreRegistry();

        ItemStack itemInHand = event.getItemInHand();
        Location blockPlacedLocation = event.getBlockPlaced().getLocation();
        Material blockPlacedMaterial = event.getBlockPlaced().getType();
        Player player = event.getPlayer();

        genOreRegistry.getGenOreFromItem(itemInHand)
                .ifPresent(genOre -> generatorRegistry.getNearestGenerator(blockPlacedLocation)
                        .ifPresent(generator -> {
                            if (generator.getRange().locationNotWithinRange(generator.getPlacedLocation(),
                                    blockPlacedLocation)) return;
                            genOre.place(player, generator, blockPlacedLocation);
                        }));



        if (blockPlacedMaterial == Material.COARSE_DIRT) {
            generatorRegistry.getNearestGenerator(blockPlacedLocation)
                            .ifPresent(generator -> {
                                if (generator.getRange().locationNotWithinRange(generator.getPlacedLocation(),
                                        blockPlacedLocation)) return;

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
