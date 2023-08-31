package me.ogali.generators.listeners;

import me.ogali.generators.GeneratorsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class RightClickListener implements Listener {

    private final GeneratorsPlugin generatorsPlugin;

    public RightClickListener(GeneratorsPlugin generatorsPlugin) {
        this.generatorsPlugin = generatorsPlugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!Action.RIGHT_CLICK_BLOCK.equals(event.getAction())) return;
        if (event.getClickedBlock() == null) return;

        generatorsPlugin.getGeneratorRegistry().getNearestGenerator(event.getPlayer().getLocation()).flatMap(generator -> generator.getApplicableOreList().stream()
                .filter(placedGenOre -> placedGenOre.getLocation().equals(event.getClickedBlock().getLocation()))
                .findFirst()).ifPresent(placedGenOre -> event.getPlayer().sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD +
                "Generated Ore: " + placedGenOre.getOreAmount()));
    }


}
