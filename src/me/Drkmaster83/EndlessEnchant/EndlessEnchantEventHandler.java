package me.Drkmaster83.EndlessEnchant;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class EndlessEnchantEventHandler implements Listener
{
	@EventHandler
	public void onAnvil(InventoryClickEvent e) {
		if(EndlessEnchant.plugin.config.toFileConf().getBoolean("anvil") == false) return;
		if(!(e.getInventory().getType().equals(InventoryType.ANVIL))) return;
		AnvilInventory ai = (AnvilInventory) e.getInventory();
		if(ai.getItem(1) != null && ai.getItem(0) != null && ai.getItem(1).getType().equals(Material.ENCHANTED_BOOK))
		{
			if(e.getCurrentItem() != ai.getItem(0) || e.getCurrentItem() != ai.getItem(1))
			{
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getInventory().getItem(1).getItemMeta();
				e.getCurrentItem().addUnsafeEnchantments(meta.getStoredEnchants());
			}
		}
	}
}