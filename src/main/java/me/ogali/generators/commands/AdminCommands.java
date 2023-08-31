package me.ogali.generators.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.RequiredArgsConstructor;
import me.ogali.generators.GeneratorsPlugin;
import me.ogali.generators.domain.impl.impl.PlaceableBlockGenerator;
import me.ogali.generators.menus.GeneratorCreationMenu;
import me.ogali.generators.range.Range;
import me.ogali.generators.utils.Chat;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("unused")
@CommandAlias("gen|generator|generators")
@CommandPermission("generators.admin")
@RequiredArgsConstructor
public class AdminCommands extends BaseCommand {

    private final GeneratorsPlugin generatorsPlugin;

    @Subcommand("create")
    @Syntax("<id> <gen speed in seconds> <x range> <y range> <z range> <particle>")
    @CommandCompletion("@particleList")
    public void onJetpackCreate(Player player, String id, long genSpeedInSeconds, int xRange, int yRange, int zRange, Particle particle) {
        generatorsPlugin.getGeneratorRegistry()
                .getGeneratorById(id)
                .ifPresentOrElse(abstractGenerator -> Chat.tellFormatted(player, "&cGenerator with id: %s, already exists!", id),
                        () -> new GeneratorCreationMenu().show(player, new PlaceableBlockGenerator(id, genSpeedInSeconds,
                                new Range(xRange, yRange, zRange), particle)));
    }

    @Subcommand("get")
    @Syntax("<id>")
    @CommandCompletion("@generatorIdList")
    public void onJetpackGet(Player player, String id) {
        generatorsPlugin.getGeneratorRegistry()
                .getGeneratorById(id)
                .ifPresentOrElse(abstractGenerator -> {
                            if (!(abstractGenerator instanceof PlaceableBlockGenerator placeableBlockGenerator)) return;
                            player.getInventory().addItem(placeableBlockGenerator.getItem());
                            Chat.tellFormatted(player, "&aAdded %s generator to your inventory!", id);
                        },
                        () -> Chat.tellFormatted(player, "&cInvalid generator id: %s", id));
    }

}
