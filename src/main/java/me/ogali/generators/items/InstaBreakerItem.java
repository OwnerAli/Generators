package me.ogali.generators.items;

import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InstaBreakerItem extends ItemBuilder {

    public InstaBreakerItem() {
        super(Material.GOLDEN_PICKAXE);
        setName("&6&lInsta-Breaker 5000");
        addLoreLines("This item can be used to", "instantly pickup GenOre",
                "without having to mine all ore.");
        glowing();
        addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    }

    public ItemStack getItem() {
        ItemStack itemStack = build();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) return null;

        itemMeta.getPersistentDataContainer()
                .set(new NamespacedKey(GeneratorsPlugin.getInstance(), "insta_breaker"),
                        PersistentDataType.STRING, "insta_breaker");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
