package me.xanium.gemseconomy.nbt;

import me.xanium.gemseconomy.GemsEconomy;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NBT {
    public static ItemStack setString(ItemStack item, String key, String value) {
        if (item == null || item.getItemMeta() == null) return item;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(GemsEconomy.getInstance(), key);
        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return item;
    }

    public static String getString(ItemStack item, String key) {
        if (item == null || item.getItemMeta() == null) return null;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(GemsEconomy.getInstance(), key);
        if (meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING)) {
            return meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
        }
        return null;
    }

    public static ItemStack setDouble(ItemStack item, String key, Double value) {
        if (item == null || item.getItemMeta() == null) return item;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(GemsEconomy.getInstance(), key);
        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.DOUBLE, value);
        item.setItemMeta(meta);
        return item;
    }

    public static Double getDouble(ItemStack item, String key) {
        if (item == null || item.getItemMeta() == null) return null;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(GemsEconomy.getInstance(), key);
        if (meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE)) {
            return meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.DOUBLE);
        }
        return null;
    }

    public static boolean hasKey(ItemStack item, String key) {
        if (item == null || item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(GemsEconomy.getInstance(), key);
        // Check for any type, as long as the key exists.
        return !meta.getPersistentDataContainer().isEmpty() && meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING) || meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE);
    }
}
