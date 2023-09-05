package me.ogali.generators.registries;

import me.ogali.generators.ore.domain.AbstractGenOre;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;
import java.util.stream.Collectors;

public class GenOreRegistry {

    private final Map<String, AbstractGenOre> genOreMap = new HashMap<>();

    public void registerGenOre(AbstractGenOre abstractGenOre) {
        genOreMap.put(abstractGenOre.getId(), abstractGenOre);
    }

    public Optional<AbstractGenOre> getGenOreById(String id) {
        return Optional.ofNullable(genOreMap.get(id));
    }

    public Optional<AbstractGenOre> getGenOreFromItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) return Optional.empty();

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        for (NamespacedKey key : persistentDataContainer.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("generators")) continue;
            if (!key.getKey().contains("genore-")) continue;
            return getGenOreById(key.getKey().split("-")[1]);
        }
        return Optional.empty();
    }

    public Collection<AbstractGenOre> getRegisteredGenOres() {
        return genOreMap.values();
    }

    public List<String> getRegisteredIdList() {
        return genOreMap.values()
                .stream()
                .map(AbstractGenOre::getId)
                .collect(Collectors.toList());
    }

}
