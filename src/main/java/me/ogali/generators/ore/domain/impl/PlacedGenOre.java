package me.ogali.generators.ore.domain.impl;

import lombok.Getter;
import me.ogali.generators.domain.AbstractGenerator;
import me.ogali.generators.ore.domain.AbstractGenOre;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class PlacedGenOre extends AbstractGenOre {

    private Location location;
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
    public void mine(Player player, AbstractGenerator abstractGenerator) {
        synchronized (abstractGenerator.getApplicableOreList()) {
            if (oreAmount - 1 == -1) {
                if (!player.isSneaking()) return;
                player.getInventory().addItem(getItem());
                location.getBlock().setType(Material.AIR);
                abstractGenerator.getApplicableOreList().remove(this);
                return;
            }
            oreAmount--;
            if (oreAmount < 5) {
                location.getBlock().setType(getDisplayItem().getType());
            }
            player.getInventory().addItem(new ItemStack(abstractGenerator.getGeneratableMaterial(), oreAmount));
        }
    }


    @Override
    public void generate(Material material) {
        oreAmount = oreAmount + getGenAmountMultiplier();

        if (oreAmount >= 10) {
            location.getBlock().setType(material);
        }
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.clone().add(0, 1, 0), 10);
    }

}