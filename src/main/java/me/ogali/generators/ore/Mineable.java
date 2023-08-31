package me.ogali.generators.ore;

import me.ogali.generators.domain.AbstractGenerator;
import org.bukkit.entity.Player;

public interface Mineable {

    void mine(Player player, AbstractGenerator abstractGenerator);

}
