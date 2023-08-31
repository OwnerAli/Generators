package me.ogali.generators.registries;

import me.ogali.generators.domain.AbstractGenerator;
import me.ogali.generators.domain.impl.PlaceableAbstractGenerator;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;
import java.util.stream.Collectors;

public class GeneratorRegistry {

    private final Map<String, AbstractGenerator> generatorMap = new HashMap<>();
    private final Map<Location, PlaceableAbstractGenerator> placedGeneratorsMap = new HashMap<>();

    public void registerGenerator(AbstractGenerator abstractGenerator) {
        generatorMap.put(abstractGenerator.getId(), abstractGenerator);
    }

    public void registerPlaceableGenerator(PlaceableAbstractGenerator placeableAbstractGenerator) {
        placedGeneratorsMap.put(placeableAbstractGenerator.getPlacedLocation(), placeableAbstractGenerator);
    }

    public AbstractGenerator getGeneratorByLocation(Location location) {
        return placedGeneratorsMap.get(location);
    }

    public Optional<AbstractGenerator> getGeneratorById(String id) {
        return Optional.ofNullable(generatorMap.get(id));
    }

    public Optional<PlaceableAbstractGenerator> getNearestGenerator(Location location) {
        return placedGeneratorsMap.values()
                .stream()
                .min(Comparator.comparingDouble(gen -> gen.getPlacedLocation().distance(location)));
    }

    public Optional<AbstractGenerator> getGeneratorByItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) return Optional.empty();

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        for (NamespacedKey key : persistentDataContainer.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("generators")) continue;
            if (!key.getKey().contains("generator-")) continue;
            return getGeneratorById(key.getKey().split("-")[1]);
        }
        return Optional.empty();
    }

    public List<String> getRegisteredIdList() {
        return generatorMap.values()
                .stream()
                .map(AbstractGenerator::getId)
                .collect(Collectors.toList());
    }

}
