package org.awayxd.defaultCustomWepons;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItems {
    public static ItemStack createLuminatra() {
        ItemStack item = new ItemStack(Material.SHIELD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Luminatra");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createEquilonis() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Equilonis");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createSylvora() {
        ItemStack item = new ItemStack(Material.WOODEN_HOE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Sylvora");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createAerothorn() {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Aerothorn");
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }
}
