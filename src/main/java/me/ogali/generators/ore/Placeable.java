package me.ogali.generators.ore;

import me.ogali.generators.domain.AbstractGenerator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Placeable {

    void place(Player player, AbstractGenerator generator, Location location);

}
