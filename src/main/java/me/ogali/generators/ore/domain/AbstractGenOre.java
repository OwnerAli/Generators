package me.ogali.generators.ore.domain;

import lombok.Data;
import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.ore.Generatable;
import me.ogali.generators.ore.Mineable;
import me.ogali.generators.ore.Placeable;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@Data
public abstract class AbstractGenOre implements Placeable, Mineable, Generatable {

    private final String id;
    private final int genAmountMultiplier;
    private ItemStack displayItem;

    public ItemStack getItem() {
        ItemMeta itemMeta = displayItem.getItemMeta();

        if (itemMeta == null) return null;

        itemMeta.getPersistentDataContainer().set(new NamespacedKey(GeneratorsPlugin.getInstance(), "genore-" + getId()),
                PersistentDataType.STRING, getId());
        displayItem.setItemMeta(itemMeta);
        return displayItem;
    }

}
