package me.ogali.generators.ore;

import me.ogali.generators.domain.AbstractGenerator;
import org.bukkit.Material;

public interface Generatable {
    
    void generate(Material material, AbstractGenerator abstractGenerator);
    
}
