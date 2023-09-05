package me.ogali.generators.ore;

import me.ogali.generators.domain.AbstractGenerator;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface Mineable {

    void mine(Player player, AbstractGenerator abstractGenerator, Event event);

}
