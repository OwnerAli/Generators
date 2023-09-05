package me.ogali.generators.ore.domain.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.customdrops.api.CustomDropsAPI;
import me.ogali.generators.domain.AbstractGenerator;
import me.ogali.generators.ore.domain.AbstractGenOre;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class PlacedGenOre extends AbstractGenOre {

    private Location location;
    @Setter
    private int oreAmount;

    public PlacedGenOre(String id, int genAmountMultiplier) {
        super(id, genAmountMultiplier);
    }

    public PlacedGenOre(AbstractGenOre abstractGenOre, Location location) {
        super(abstractGenOre.getId(), abstractGenOre.getGenAmountMultiplier());
        this.location = location;
        setDisplayItem(abstractGenOre.getDisplayItem());
    }

    @Override
    public void place(Player player, AbstractGenerator generator, Location location) {
        generator.getApplicableOreList().add(new PlacedGenOre(this, location));
    }

    @Override
    public void mine(Player player, AbstractGenerator abstractGenerator, Event event) {
        if (breakGenOre(player, abstractGenerator)) return;
        decrementAndUpdateGenOre();
        dropGenDrops(event, abstractGenerator);
    }

    @Override
    public void generate(Material material, AbstractGenerator abstractGenerator) {
        incrementAndUpdateGenOre(material);
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.clone().add(0, 1.5, 0), 10);
    }

    @Override
    public String getType() {
        return "placedGenOre";
    }

    private void incrementAndUpdateGenOre(Material material) {
        oreAmount = oreAmount + getGenAmountMultiplier();

        if (oreAmount >= 10 && !location.getBlock().getType().equals(material)) {
            location.getBlock().setType(material);
        }
    }

    private void decrementAndUpdateGenOre() {
        oreAmount--;

        if (oreAmount < 5 && !location.getBlock().getType().equals(getDisplayItem().getType())) {
            location.getBlock().setType(getDisplayItem().getType());
        }
    }

    private boolean breakGenOre(Player player, AbstractGenerator abstractGenerator) {
        if (isGenOreInstaBreaker(player.getInventory().getItemInMainHand())) {
            removeOreFromGenAndGive(player, abstractGenerator);
            return true;
        }

        if (oreAmount - 1 != -1) return false;
        if (!player.isSneaking()) return false;

        removeOreFromGenAndGive(player, abstractGenerator);
        return true;
    }

    private boolean isGenOreInstaBreaker(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return false;

        ItemMeta itemMeta = itemStack.getItemMeta();
        for (NamespacedKey key : itemMeta.getPersistentDataContainer().getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("generators")) continue;
            if (!key.getKey().contains("insta_breaker")) continue;
            return true;
        }
        return false;
    }

    private void removeOreFromGenAndGive(Player player, AbstractGenerator abstractGenerator) {
        location.getBlock().setType(Material.AIR);
        player.getInventory().addItem(getItem());
        abstractGenerator.getApplicableOreList().remove(this);
    }

    private void dropGenDrops(Event event, AbstractGenerator abstractGenerator) {
        // TODO: Add check CustomDrops is enabled
        CustomDropsAPI.getInstance().getDropById(abstractGenerator.getCustomDropsDropId())
                .drop(event);
    }

}