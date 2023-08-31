package me.ogali.generators.ore.domain;

import lombok.Data;
import me.ogali.generators.domain.Placeable;
import me.ogali.generators.ore.Generatable;
import me.ogali.generators.ore.Mineable;
import org.bukkit.inventory.ItemStack;

@Data
public abstract class GenOre implements Generatable, Mineable, Placeable {

    private final String id;
    private final ItemStack displayItem;
    private final double genSpeedMultiplier;

}
