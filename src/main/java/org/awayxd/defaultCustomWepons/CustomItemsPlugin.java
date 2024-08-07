package org.awayxd.defaultCustomWepons;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItemsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ItemEventListener(this), this);
        getLogger().info("CustomItemsPlugin has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomItemsPlugin has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("givecustomitem")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /givecustomitem <item> <player>");
                return false;
            }

            String itemName = args[0];
            Player targetPlayer = getServer().getPlayer(args[1]);

            if (targetPlayer == null) {
                sender.sendMessage("Player not found.");
                return false;
            }

            ItemStack item = null;

            switch (itemName.toLowerCase()) {
                case "luminatra":
                    item = new ItemStack(Material.SHIELD);
                    break;
                case "equilonis":
                    item = new ItemStack(Material.EMERALD);
                    break;
                case "sylvora":
                    item = new ItemStack(Material.WOODEN_HOE);
                    break;
                case "aerothorn":
                    item = new ItemStack(Material.WOODEN_SWORD);
                    break;
                default:
                    sender.sendMessage("Invalid item name.");
                    return false;
            }

            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(itemName.substring(0, 1).toUpperCase() + itemName.substring(1));
                item.setItemMeta(meta);
                targetPlayer.getInventory().addItem(item);
                sender.sendMessage("Gave " + itemName + " to " + targetPlayer.getName());
            }
            return true;
        }
        return false;
    }
}
