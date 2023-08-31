package me.ogali.generators;

import co.aikar.commands.*;
import lombok.Getter;
import me.ogali.generators.commands.AdminCommands;
import me.ogali.generators.listeners.BlockBreakListener;
import me.ogali.generators.listeners.BlockPlaceListener;
import me.ogali.generators.listeners.RightClickListener;
import me.ogali.generators.registries.GeneratorRegistry;
import org.bukkit.Particle;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class GeneratorsPlugin extends JavaPlugin {

    @Getter
    private static GeneratorsPlugin instance;

    @Getter
    private GeneratorRegistry generatorRegistry;

    @Override
    public void onEnable() {
        instance = this;
        initializeRegistries();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockPlaceListener(generatorRegistry), this);
        pluginManager.registerEvents(new RightClickListener(this), this);
        pluginManager.registerEvents(new BlockBreakListener(this), this);
    }

    private void initializeRegistries() {
        this.generatorRegistry = new GeneratorRegistry();
    }

    private void registerCommands() {
        PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new AdminCommands(this));
        registerCommandContexts(paperCommandManager);
        registerCommandCompletions(paperCommandManager);
    }

    private void registerCommandCompletions(PaperCommandManager paperCommandManager) {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = paperCommandManager.getCommandCompletions();
        List<String> allParticleNames = Arrays.stream(Particle.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        // Add colored REDSTONE particle values
        allParticleNames.add("REDSTONE_RED");
        allParticleNames.add("REDSTONE_GREEN");
        allParticleNames.add("REDSTONE_BLUE");
        allParticleNames.add("REDSTONE_WHITE");
        allParticleNames.add("REDSTONE_YELLOW");
        allParticleNames.add("REDSTONE_PURPLE");
        allParticleNames.add("REDSTONE_ORANGE");
        allParticleNames.add("REDSTONE_GRAY");
        allParticleNames.add("REDSTONE_BLACK");

        commandCompletions.registerCompletion("particleList", handler -> allParticleNames);
        commandCompletions.registerCompletion("generatorIdList", handler -> generatorRegistry.getGeneratorIdList());
    }

    private void registerCommandContexts(PaperCommandManager paperCommandManager) {
        CommandContexts<BukkitCommandExecutionContext> commandContexts = paperCommandManager.getCommandContexts();
        commandContexts.registerContext(Particle.class, context -> {
            String particleEnumName = context.popFirstArg();

            if (particleEnumName.isEmpty() || particleEnumName.isBlank()) {
                return Particle.CAMPFIRE_COSY_SMOKE;
            }

            try {
                return Particle.valueOf(particleEnumName);
            } catch (IllegalArgumentException ex) {
                throw new InvalidCommandArgument("Invalid particle value: " + particleEnumName);
            }
        });
    }

}
