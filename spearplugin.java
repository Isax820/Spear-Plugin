package org.spearplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class spearplugin extends JavaPlugin implements Listener {

    public static final String SPEAR_NAME = "§6Spear";
    public static final int SPEAR_MODEL_DATA = 1001;

    private double spearDamage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        registerRecipe();

        getLogger().info("SpearPlugin enabled (1.19 → 1.21.10)");
    }

    private void loadConfig() {
        spearDamage = getConfig().getDouble("spear.damage", 10.0);
    }

    private void registerRecipe() {
        ItemStack spear = createSpear();

        NamespacedKey key = new NamespacedKey(this, "spear");
        ShapedRecipe recipe = new ShapedRecipe(key, spear);

        recipe.shape(
                " D ",
                " D ",
                " S "
        );

        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.STICK);

        Bukkit.addRecipe(recipe);
    }

    private ItemStack createSpear() {
        ItemStack item = new ItemStack(Material.TRIDENT);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.setDisplayName(SPEAR_NAME);
        meta.setLore(List.of(
                "§7Une lance équilibrée",
                "§7Dégâts: §c" + spearDamage
        ));

        meta.setCustomModelData(SPEAR_MODEL_DATA);
        item.setItemMeta(meta);

        return item;
    }

    private boolean isSpear(ItemStack item) {
        if (item == null || item.getType() != Material.TRIDENT) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null
                && meta.hasCustomModelData()
                && meta.getCustomModelData() == SPEAR_MODEL_DATA;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (!isSpear(event.getItem())) return;
        // Comportement custom possible ici
    }

    @EventHandler
    public void onSpearHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident trident)) return;
        if (!(trident.getShooter() instanceof org.bukkit.entity.Player)) return;

        ItemStack item = trident.getItem();
        if (!isSpear(item)) return;

        trident.setDamage(spearDamage);
    }

    // ===== Commande /info =====
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("info")) {
            sender.sendMessage("§6=== SpearPlugin ===");
            sender.sendMessage("§eAuteur: §fIsax");
            sender.sendMessage("§eVersion: §f" + getDescription().getVersion());
            sender.sendMessage("§eCompatible: §f1.19 → 1.21.10");
            return true;
        }
        return false;
    }
}
