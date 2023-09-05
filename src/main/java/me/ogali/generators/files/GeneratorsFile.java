package me.ogali.generators.files;

import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.domain.AbstractGenerator;
import me.ogali.generators.domain.impl.impl.PlaceableBlockGenerator;
import me.ogali.generators.files.domain.JsonFile;
import me.ogali.generators.range.Range;
import me.ogali.generators.utils.Chat;
import me.ogali.generators.utils.Serialization;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class GeneratorsFile extends JsonFile<AbstractGenerator> {

    public GeneratorsFile() {
        super("generators");
    }

    @Override
    public void saveToFile(AbstractGenerator abstractGenerator) {
        String id = abstractGenerator.getId();

        set(id + ".genSpeedInSeconds", abstractGenerator.getGenSpeedInSeconds());
        set(id + ".material", abstractGenerator.getGeneratableMaterial().name());
        set(id + ".rangeX", abstractGenerator.getRange().getX());
        set(id + ".rangeY", abstractGenerator.getRange().getY());
        set(id + ".rangeZ", abstractGenerator.getRange().getZ());
        set(id + ".rangeIncludeCorners", abstractGenerator.getRange().isIncludeCorners());
        set(id + ".particle", abstractGenerator.getParticle().name());
        set(id + ".customDropsId", abstractGenerator.getCustomDropsDropId());

        if (abstractGenerator instanceof PlaceableBlockGenerator placeableBlockGenerator) {
            savePlaceableBlockGenerator(placeableBlockGenerator);
        }
    }

    @Override
    public void loadFromFile() {
        singleLayerKeySet().forEach(generatorId -> {
            ItemStack generatorItem = Serialization.deserialize(getString(generatorId + ".itemstack"));
            Location placedLocation = Location.deserialize(getMapParameterized(generatorId + ".placedLocation"));
            long genSpeedInSeconds = getLong(generatorId + ".genSpeedInSeconds");
            int rangeX = getInt(generatorId + ".rangeX");
            int rangeY = getInt(generatorId + ".rangeY");
            int rangeZ = getInt(generatorId + ".rangeZ");
            boolean rangeIncludeCorners = getBoolean(generatorId + ".rangeIncludeCorners");
            String customDropsId = getString(generatorId + ".customDropsId");

            Material generatableMaterial = Material.BEETROOT_SEEDS;

            try {
                generatableMaterial = Material.valueOf(getString(generatorId + ".material"));
            } catch (IllegalArgumentException ignored) {
                Chat.log("&cInvalid generatable material named defined for generator: " + generatorId + ". Setting to default material: BEETROOT_SEEDS.");
            }

            Particle particle = Particle.VILLAGER_HAPPY;

            try {
                particle = Particle.valueOf(getString(generatorId + ".particle"));
            } catch (IllegalArgumentException ignored) {
                Chat.log("&cInvalid particle name defined for generator: " + generatorId + ". Setting to default particle: VILLAGER_HAPPY.");
            }

            Range range = new Range(rangeX, rangeY, rangeZ, rangeIncludeCorners);
            PlaceableBlockGenerator placeableBlockGenerator = new PlaceableBlockGenerator(generatorId, genSpeedInSeconds,
                    generatableMaterial, range, particle, customDropsId);
            placeableBlockGenerator.setPlacedLocation(placedLocation);
            placeableBlockGenerator.setItemStack(generatorItem);

            GeneratorsPlugin.getInstance().getGeneratorRegistry().registerGenerator(placeableBlockGenerator);
        });
    }

    private void savePlaceableBlockGenerator(PlaceableBlockGenerator placeableBlockGenerator) {
        set(placeableBlockGenerator.getId() + ".itemstack", Serialization.serialize(placeableBlockGenerator.getItemStack()));
    }

}
