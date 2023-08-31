package me.ogali.generators.domain;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Placeable {

    void place(Player player, Location location);

}
