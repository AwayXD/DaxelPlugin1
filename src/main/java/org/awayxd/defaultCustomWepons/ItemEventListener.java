package org.awayxd.defaultCustomWepons;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemEventListener implements Listener {
    private final JavaPlugin plugin;
    private final Map<UUID, Long> equilonisCooldowns = new HashMap<>();
    private final int fireRadius = 10; // Define the radius of the fire here

    public ItemEventListener(JavaPlugin plugin) {
        this.plugin = plugin;
        startActionBarUpdater();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        String itemName = item.getItemMeta().getDisplayName();

        if (itemName.equals("Luminatra")) {
            // Implement Luminatra functionality
            event.setCancelled(true); // Cancel the shield block to trigger custom behavior
            player.getNearbyEntities(3, 3, 3).forEach(entity -> {
                if (entity instanceof LivingEntity) {
                    entity.setVelocity(entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5));
                }
            });
            item.setDurability((short) (item.getDurability() + 1)); // Custom durability handling
        } else if (itemName.equals("Equilonis")) {
            // Implement Equilonis functionality
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                UUID playerId = player.getUniqueId();
                long currentTime = System.currentTimeMillis();
                long lastUsed = equilonisCooldowns.getOrDefault(playerId, 0L);

                if (currentTime - lastUsed >= 120000) { // 2 minutes cooldown
                    equilonisCooldowns.put(playerId, currentTime);

                    // Show red particle ring
                    for (int i = 0; i < 360; i += 10) {
                        double angle = i * Math.PI / 180;
                        double x = fireRadius * Math.cos(angle);
                        double z = fireRadius * Math.sin(angle);
                        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(x, 0, z), 1, new Particle.DustOptions(org.bukkit.Color.RED, 1));
                    }

                    player.getNearbyEntities(10, 10, 10).forEach(entity -> {
                        if (entity instanceof LivingEntity) {
                            ((LivingEntity) entity).setFireTicks(200); // Set on fire for 10 seconds
                        }
                    });
                    item.setDurability((short) (item.getDurability() + 1)); // Custom durability handling
                }
            }
        } else if (itemName.equals("Sylvora")) {
            // Implement Sylvora functionality
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.getClickedBlock().setType(Material.DIRT); // Reverts tilled land to dirt
                // Logic to speed up crop growth (simplified example)
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    event.getClickedBlock().setType(Material.DIRT); // Revert after 10 minutes
                }, 12000L); // 10 minutes in ticks
            }
        } else if (itemName.equals("Aerothorn")) {
            // Aerothorn doesn't require specific interaction handling as it's a passive effect
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String itemName = item.getItemMeta().getDisplayName();
            if (itemName.equals("Luminatra") || itemName.equals("Equilonis") || itemName.equals("Sylvora") || itemName.equals("Aerothorn")) {
                event.setCancelled(true); // Prevent default item damage
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String itemName = item.getItemMeta().getDisplayName();
                if (itemName.equals("Aerothorn")) {
                    // Implement Aerothorn functionality
                    if (Math.random() < 0.05) { // 5% chance
                        double damage = event.getDamage() * 2;
                        event.setDamage(damage); // Double damage
                        player.sendMessage("Aerothorn dealt double damage: " + damage);
                    } else {
                        player.sendMessage("Aerothorn dealt normal damage: " + event.getDamage());
                    }
                }
            }
        }
    }

    private void startActionBarUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActionBars();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Update every second
    }

    private void updateActionBars() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String itemName = item.getItemMeta().getDisplayName();
                if (itemName.equals("Equilonis")) {
                    UUID playerId = player.getUniqueId();
                    long currentTime = System.currentTimeMillis();
                    long lastUsed = equilonisCooldowns.getOrDefault(playerId, 0L);
                    long timeLeft = (120000 - (currentTime - lastUsed)) / 1000; // Time left in seconds

                    if (timeLeft > 0) {
                        player.sendActionBar(ChatColor.RED + "Cooldown: " + timeLeft + "s | Fire Radius: " + fireRadius + " blocks");
                    } else {
                        player.sendActionBar(ChatColor.GREEN + "Ready! | Fire Radius: " + fireRadius + " blocks");
                    }
                }
            }
        }
    }
}
