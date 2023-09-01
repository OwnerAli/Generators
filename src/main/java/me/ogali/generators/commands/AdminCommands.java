package me.ogali.generators.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.RequiredArgsConstructor;
import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.domain.impl.impl.PlaceableBlockGenerator;
import me.ogali.generators.menus.generators.GeneratorCreationMenu;
import me.ogali.generators.menus.ores.GenOreCreationMenu;
import me.ogali.generators.ore.domain.impl.PlacedGenOre;
import me.ogali.generators.range.Range;
import me.ogali.generators.utils.Chat;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("gen|generator|generators")
@CommandPermission("generators.admin")
@RequiredArgsConstructor
public class AdminCommands extends BaseCommand {

    private final GeneratorsPlugin generatorsPlugin;

    @Subcommand("create")
    @Syntax("<id> <gen speed in seconds> <generatable material> <x range> <y range> <z range> <include corners> <particle>")
    public void onGeneratorCreate(Player player, String id, long genSpeedInSeconds, Material generatableMaterial, int xRange, int yRange, int zRange,
                                  boolean includeCorners, Particle particle) {
        generatorsPlugin.getGeneratorRegistry()
                .getGeneratorById(id)
                .ifPresentOrElse(abstractGenerator -> Chat.tellFormatted(player, "&cGenerator with id: %s, already exists!", id),
                        () -> new GeneratorCreationMenu().show(player, new PlaceableBlockGenerator(id, genSpeedInSeconds,
                                generatableMaterial, new Range(xRange, yRange, zRange, includeCorners), particle)));
    }

    @Subcommand("get")
    @Syntax("<id>")
    @CommandCompletion("@generatorIdList")
    public void onGeneratorGet(Player player, String id) {
        generatorsPlugin.getGeneratorRegistry()
                .getGeneratorById(id)
                .ifPresentOrElse(abstractGenerator -> {
                            if (!(abstractGenerator instanceof PlaceableBlockGenerator placeableBlockGenerator)) return;
                            player.getInventory().addItem(placeableBlockGenerator.getItem());
                            Chat.tellFormatted(player, "&aAdded %s generator to your inventory!", id);
                        },
                        () -> Chat.tellFormatted(player, "&cInvalid generator id: %s", id));
    }

    @Subcommand("genore create")
    @Syntax("<id> <gen amount multiplier>")
    @CommandCompletion("@particleList")
    public void onGenOreCreate(Player player, String id, int genAmountMultiplier) {
        generatorsPlugin.getGenOreRegistry()
                .getGenOreById(id)
                .ifPresentOrElse(abstractGenerator -> Chat.tellFormatted(player, "&cGenOre with id: %s, already exists!", id),
                        () -> new GenOreCreationMenu().show(player, new PlacedGenOre(id, genAmountMultiplier)));
    }

    @Subcommand("genore get")
    @Syntax("<id>")
    @CommandCompletion("@genOreIdList")
    public void onGenOreGet(Player player, String id) {
        generatorsPlugin.getGenOreRegistry()
                .getGenOreById(id)
                .ifPresentOrElse(abstractGenOre -> {
                            player.getInventory().addItem(abstractGenOre.getItem());
                            Chat.tellFormatted(player, "&aAdded %s GenOre to your inventory!", id);
                        },
                        () -> Chat.tellFormatted(player, "&cInvalid GenOre id: %s", id));
    }

}
