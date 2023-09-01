package me.ogali.generators.listeners;

import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.domain.Placeable;
import me.ogali.generators.domain.impl.PlaceableAbstractGenerator;
import me.ogali.generators.registries.GenOreRegistry;
import me.ogali.generators.registries.GeneratorRegistry;
import me.ogali.generators.utils.Chat;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

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
        Player player = event.getPlayer();

        genOreRegistry.getGenOreFromItem(itemInHand)
                .ifPresent(genOre -> generatorRegistry.getNearestGenerator(blockPlacedLocation)
                        .ifPresent(generator -> {
                            if (!generator.getRange().locationWithinRange(generator.getPlacedLocation(),
                                    blockPlacedLocation)) return;
                            genOre.place(player, generator, blockPlacedLocation);
                        }));

        generatorRegistry.getGeneratorByItem(event.getItemInHand())
                .ifPresent(generator -> {
                    Optional<PlaceableAbstractGenerator> nearestGenerator = generatorRegistry.getNearestGenerator(blockPlacedLocation);
                    if (nearestGenerator.isPresent()) {
                        if (generator.getRange().locationWithinRange(nearestGenerator.get().getPlacedLocation(), blockPlacedLocation)) return;
                        Chat.tell(player, "&cYou can't place this generator here, it's too close to another one!");
                        event.setCancelled(true);
                        return;
                    }
                    if (!(generator instanceof Placeable placeable)) return;
                    placeable.place(player, blockPlacedLocation);
                });
    }

}
