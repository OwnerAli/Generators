package me.ogali.generators;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.ogali.generators.commands.AdminCommands;
import me.ogali.generators.files.GenOresFile;
import me.ogali.generators.files.GeneratorsFile;
import me.ogali.generators.files.PlacedGeneratorsFile;
import me.ogali.generators.listeners.BlockBreakListener;
import me.ogali.generators.listeners.BlockPlaceListener;
import me.ogali.generators.listeners.RightClickListener;
import me.ogali.generators.registries.GenOreRegistry;
import me.ogali.generators.registries.GeneratorRegistry;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

@Getter
public final class GeneratorsPlugin extends JavaPlugin {

    @Getter
    private static GeneratorsPlugin instance;
    private GeneratorRegistry generatorRegistry;
    private GenOreRegistry genOreRegistry;
    private GeneratorsFile generatorsFile;
    private PlacedGeneratorsFile placedGeneratorsFile;
    private GenOresFile genOresFile;

    @Getter
    private Random random;

    @Override
    public void onEnable() {
        instance = this;
        random = new Random();
        initializeRegistries();
        initializeFiles();
        loadDataFromFiles();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        saveDataToFiles();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockPlaceListener(this), this);
        pluginManager.registerEvents(new RightClickListener(this), this);
        pluginManager.registerEvents(new BlockBreakListener(this), this);
    }

    private void initializeRegistries() {
        this.generatorRegistry = new GeneratorRegistry();
        this.genOreRegistry = new GenOreRegistry();
    }

    private void initializeFiles() {
        this.generatorsFile = new GeneratorsFile();
        this.placedGeneratorsFile = new PlacedGeneratorsFile();
        this.genOresFile = new GenOresFile();
    }

    private void loadDataFromFiles() {
        generatorsFile.loadFromFile();
        genOresFile.loadFromFile();
        placedGeneratorsFile.loadFromFile();
    }

    private void saveDataToFiles() {
        generatorRegistry.getRegisteredGenerators().forEach(abstractGenerator -> generatorsFile.saveToFile(abstractGenerator));
        generatorRegistry.getPlacedGenerators().forEach(placeableAbstractGenerator -> placedGeneratorsFile.saveToFile(placeableAbstractGenerator));
        genOreRegistry.getRegisteredGenOres().forEach(abstractGenOre -> genOresFile.saveToFile(abstractGenOre));
    }

    private void registerCommands() {
        PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new AdminCommands(this));
//        registerCommandContexts(paperCommandManager);
        registerCommandCompletions(paperCommandManager);
    }

    private void registerCommandCompletions(PaperCommandManager paperCommandManager) {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = paperCommandManager.getCommandCompletions();
        commandCompletions.registerCompletion("generatorIdList", handler -> generatorRegistry.getRegisteredIdList());
        commandCompletions.registerCompletion("genOreIdList", handler -> genOreRegistry.getRegisteredIdList());
    }

}
