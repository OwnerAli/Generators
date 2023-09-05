package me.ogali.generators.files;

import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.files.domain.JsonFile;
import me.ogali.generators.ore.domain.AbstractGenOre;
import me.ogali.generators.ore.domain.impl.PlacedGenOre;
import me.ogali.generators.utils.Serialization;
import org.bukkit.inventory.ItemStack;

public class GenOresFile extends JsonFile<AbstractGenOre> {

    public GenOresFile() {
        super("genores");
    }

    @Override
    public void saveToFile(AbstractGenOre abstractGenOre) {
        String id = abstractGenOre.getId();

        set(id + ".genAmountMultiplier", abstractGenOre.getGenAmountMultiplier());
        set(id + ".displayItem", Serialization.serialize(abstractGenOre.getDisplayItem()));
    }

    @Override
    public void loadFromFile() {
        singleLayerKeySet().forEach(genOreId -> {
            int genAmountMultiplier = getInt(genOreId + ".genAmountMultiplier");
            ItemStack displayItem = Serialization.deserialize(getString(genOreId + ".displayItem"));

            PlacedGenOre placedGenOre = new PlacedGenOre(genOreId, genAmountMultiplier);
            placedGenOre.setDisplayItem(displayItem);

            GeneratorsPlugin.getInstance().getGenOreRegistry().registerGenOre(placedGenOre);
        });
    }

}
