package me.ogali.generators.files;

import de.leonhard.storage.sections.FlatFileSection;
import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.domain.AbstractGenerator;
import me.ogali.generators.domain.impl.PlaceableAbstractGenerator;
import me.ogali.generators.domain.impl.impl.PlaceableBlockGenerator;
import me.ogali.generators.files.domain.JsonFile;
import me.ogali.generators.ore.domain.impl.PlacedGenOre;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class PlacedGeneratorsFile extends JsonFile<PlaceableAbstractGenerator> {

    public PlacedGeneratorsFile() {
        super("placedGenerators");
    }

    @Override
    public void saveToFile(PlaceableAbstractGenerator placeableAbstractGenerator) {
        String id = placeableAbstractGenerator.getId();

        set(id + ".placedLocation", placeableAbstractGenerator.getPlacedLocation().serialize());
        saveGenOreList(placeableAbstractGenerator);
    }

    @Override
    public void loadFromFile() {
        List<PlaceableAbstractGenerator> generatorsToRegister = new ArrayList<>();

        singleLayerKeySet().forEach(generatorId -> GeneratorsPlugin.getInstance().getGeneratorRegistry()
                .getGeneratorById(generatorId)
                .ifPresent(abstractGenerator -> {
                    Location placedLocation = Location.deserialize(getMapParameterized(generatorId + ".placedLocation"));
                    PlaceableBlockGenerator placeableAbstractGenerator = new PlaceableBlockGenerator(abstractGenerator, placedLocation);
                    generatorsToRegister.add(placeableAbstractGenerator);
                }));
        generatorsToRegister.forEach(placeableAbstractGenerator -> {
            loadGenOreList(placeableAbstractGenerator);
            GeneratorsPlugin.getInstance().getGeneratorRegistry().registerPlaceableGenerator(placeableAbstractGenerator);
            placeableAbstractGenerator.start(placeableAbstractGenerator);
        });
    }

    private void loadGenOreList(AbstractGenerator abstractGenerator) {
        FlatFileSection section = getSection(abstractGenerator.getId() + ".genOres");
        section.singleLayerKeySet().forEach(id -> {
            String genOreId = section.getString(id + ".id");
            int oreAmount = section.getInt(id + ".oreAmount");
            Location placedLocation = Location.deserialize(section.getMapParameterized(id + ".location"));

            GeneratorsPlugin.getInstance().getGenOreRegistry().getGenOreById(genOreId).map(genOre -> new PlacedGenOre(genOre, placedLocation))
                    .ifPresent(genOre -> {
                        genOre.setOreAmount(oreAmount);
                        abstractGenerator.getApplicableOreList().add(genOre);
                    });
        });
    }

    private void saveGenOreList(AbstractGenerator abstractGenerator) {
        List<PlacedGenOre> applicableOreList = abstractGenerator.getApplicableOreList();
        for (int i = 0; i < applicableOreList.size(); i++) {
            PlacedGenOre placedGenOre = applicableOreList.get(i);
            set(abstractGenerator.getId() + ".genOres" + "." + i + ".id", placedGenOre.getId());
            set(abstractGenerator.getId() + ".genOres" + "." + i + ".oreAmount", placedGenOre.getOreAmount());
            set(abstractGenerator.getId() + ".genOres" + "." + i + ".location", placedGenOre.getLocation().serialize());
        }
    }

}
