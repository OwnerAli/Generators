package me.ogali.generators.domain.impl.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.domain.AbstractGenerator;
import me.ogali.generators.domain.impl.PlaceableAbstractGenerator;
import me.ogali.generators.range.Range;
import me.ogali.generators.runnables.GeneratorOreGenRunnable;
import me.ogali.generators.runnables.ParticleRunnable;
import me.ogali.generators.utils.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@Getter
@Setter
public class PlaceableBlockGenerator extends PlaceableAbstractGenerator {

    private ItemStack itemStack;

    public PlaceableBlockGenerator(String id, long genSpeedInSeconds, Material generatableMaterial, Range range, Particle particle, String customDropsDropId) {
        super(id, genSpeedInSeconds, generatableMaterial, range, particle, customDropsDropId);
    }

    public PlaceableBlockGenerator(AbstractGenerator abstractGenerator, Location location) {
        super(abstractGenerator.getId(), abstractGenerator.getGenSpeedInSeconds(), abstractGenerator.getGeneratableMaterial(),
                abstractGenerator.getRange(), abstractGenerator.getParticle(), abstractGenerator.getCustomDropsDropId());
        setPlacedLocation(location);
    }

    public ItemStack getItem() {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) return null;

        itemMeta.getPersistentDataContainer().set(new NamespacedKey(GeneratorsPlugin.getInstance(), "generator-" + getId()),
                PersistentDataType.STRING, getId());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void start(PlaceableAbstractGenerator placeableAbstractGenerator) {
        new ParticleRunnable(placeableAbstractGenerator.getPlacedLocation(), getParticle()).start();
        new GeneratorOreGenRunnable(placeableAbstractGenerator).start();
    }

    @Override
    public void place(Player player, Location location) {
        PlaceableBlockGenerator placeableAbstractGenerator = new PlaceableBlockGenerator(this, location);
        GeneratorsPlugin.getInstance().getGeneratorRegistry().registerPlaceableGenerator(placeableAbstractGenerator);
        Chat.tell(player, "&aGenerator placed!");
        start(placeableAbstractGenerator);
    }

}
