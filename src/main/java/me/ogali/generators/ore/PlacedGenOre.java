package me.ogali.generators.ore;

import lombok.Getter;
import me.ogali.generators.domain.AbstractGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class PlacedGenOre implements Generatable, Mineable {

    private final Location location;
    private final int genAmountMultiplier;

    private String oreType;
    private int oreAmount;

    public PlacedGenOre(Location location, int genAmountMultiplier) {
        this.location = location;
        this.genAmountMultiplier = genAmountMultiplier;
    }

    @Override
    public void generate(Material material) {
        oreAmount = oreAmount + genAmountMultiplier;

        if (oreAmount >= 10) {
            location.getBlock().setType(material);
        }
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.clone().add(0, 1, 0), 10);
    }

    @Override
    public void mine(Player player, AbstractGenerator abstractGenerator) {
        if (oreAmount - 1 == -1) {
            if (!player.isSneaking()) return;
            player.getInventory().addItem(new ItemStack(Material.COARSE_DIRT));
            location.getBlock().setType(Material.AIR);
            abstractGenerator.getApplicableOreList().remove(this);
            return;
        }
        oreAmount--;
        if (oreAmount < 5) {
            location.getBlock().setType(Material.COARSE_DIRT);
        }
        player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, oreAmount));
    }

}
