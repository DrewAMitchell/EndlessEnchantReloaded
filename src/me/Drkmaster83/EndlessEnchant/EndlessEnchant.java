package me.Drkmaster83.EndlessEnchant;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class EndlessEnchant extends JavaPlugin implements Listener
{
	private YMLConfig enchants, config;
	private HashMap<String, List<String>> enchantNames;
	private ArrayList<Kit> kits;
	private String serverVersion;
	private final String EEPrefix = "\u00A7f[\u00A7b\u00A7lEndless\u00A7c\u00A7lEnchant\u00A7f] ";
	private int highestLevel;
	private boolean glowLore;

	@Override
	public void onEnable() {
		PluginDescriptionFile pdf = this.getDescription();
		String packageName = getServer().getClass().getPackage().getName();
		serverVersion = packageName.substring(packageName.lastIndexOf(".") + 1);
		
		//Begin adding aliases and associated enchantments
		cacheEnchantmentAndKitNames(); //Loads configuration files for us as well
		cacheKits();
		//End adding aliases and associated enchantments
		
		getServer().getPluginManager().registerEvents(new EndlessEnchantAnvilEvents(this), this);
		EndlessEnchantCommand eec = new EndlessEnchantCommand(this);
		getCommand("EndlessEnchant").setExecutor(eec);
		getCommand("EndlessEnchant").setTabCompleter(eec);
		
		getLogger().info(pdf.getName() + " v" + pdf.getVersion() + " has been enabled!");
	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pdf = this.getDescription();
		getLogger().info(pdf.getName() + " v" + pdf.getVersion() + " has been disabled!");
	}
	
	/** Must be called before verifyFiles to ensure proper saveResource call. */
	private void loadConfigurations() {
		if(enchants == null) enchants = new YMLConfig(this, "enchants.yml");
		if(config == null) config = new YMLConfig(this, "config.yml");
		if(!config.reload()) {
			getLogger().warning("Config.yml file was not found, creating one for you...");
		}
		if(!enchants.reload()) {
			getLogger().warning("Enchants.yml file was not found, creating one for you...");
		}
	}
	
	private void verifyFiles() {
		Reader enchantsJar = null, configJar = null;
		try {
			enchantsJar = new InputStreamReader(getResource("enchants.yml"), "UTF8");
			configJar = new InputStreamReader(getResource("config.yml"), "UTF8");
		}
		catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		YamlConfiguration cachedEnchants = YamlConfiguration.loadConfiguration(enchantsJar);
		YamlConfiguration cachedConfig = YamlConfiguration.loadConfiguration(configJar);
		if(enchants.getConfigurationSection("Aliases") == null) enchants.createSection("Aliases");
		for(String s : cachedEnchants.getConfigurationSection("Aliases").getKeys(false)) {
			if(!enchants.getConfigurationSection("Aliases").contains(s)) {
				enchants.set("Aliases." + s, cachedEnchants.getStringList("Aliases." + s));
			}
		}
		
		if(enchants.getConfigurationSection("Kits") == null) { //Probably erased them
			enchants.createSection("Kits");
		}
		if(enchants.getConfigurationSection("KitAliases") == null) { //Probably erased them
			enchants.createSection("KitAliases");
		}
		enchants.save();
		if(config.get("highestLevelLimit") == null) { //Needed for functionality
			config.set("highestLevelLimit", cachedConfig.get("highestLevelLimit"));
		}
		highestLevel = config.getInt("highestLevelLimit", 5);
		if(config.get("glowInLore") == null) { //Needed for functionality
			config.set("glowInLore", cachedConfig.get("glowInLore"));
		}
		glowLore = config.getBoolean("glowInLore", true);
		config.save();
	}
	
	private void cacheEnchantmentAndKitNames() {
		loadConfigurations(); //Reloads data in the files in case the user wants to reload the data from the file
		verifyFiles(); //Verifies our three sections for us
		enchantNames = new HashMap<String, List<String>>();
		for(String kitName : enchants.getConfigurationSection("Kits").getKeys(false)) { //Populate with kits' names and aliases
			kitName = kitName.replaceAll("(?i)&[0-9A-FK-OR]", "");
			List<String> aliasList = new ArrayList<String>();
			for(String kitAlias : enchants.getStringList("KitAliases." + kitName)) {
				aliasList.add(kitAlias.toUpperCase()); //Ensures that the aliases are all uppercase
			}
			enchantNames.put(kitName.toUpperCase(), aliasList);
		}
		for(String enchName : enchants.getConfigurationSection("Aliases").getKeys(false)) { //Populate with enchantments and aliases
			if(Enchantment.getByName(enchName.toUpperCase()) == null) {
				getLogger().warning("Base enchantment name from enchants.yml \"" + enchName.toUpperCase() + "\" doesn't exist in this version of Minecraft - will continue without it...");
				continue;
			}
			if(EndlessEnchantment.getByName(enchName) == null) {
				getLogger().warning("Base enchantment name from enchants.yml \"" + enchName.toUpperCase() + "\" doesn't exist in code - will continue without it...");
				continue;
			}
			List<String> aliasList = new ArrayList<String>();
			for(String alias : enchants.getStringList("Aliases." + enchName)) {
				aliasList.add(alias.toUpperCase()); //Ensures that the aliases are all uppercase
			}
			enchantNames.put(enchName.replaceAll("(?i)&[0-9A-FK-OR]", "").toUpperCase(), aliasList);
		}
	}
	
	private void cacheKits() {
		kits = new ArrayList<Kit>();
		for(String kitName : enchants.getConfigurationSection("Kits").getKeys(false)) {
			List<EndlessEnchantment> kitEnchantments = new ArrayList<EndlessEnchantment>();
			INNER: for(String enchant : enchants.getStringList("Kits." + kitName)) {
				if(enchant.equals("*")) {
					for(EndlessEnchantment e : EndlessEnchantment.values()) {
						kitEnchantments.add(e);
					}
					continue;
				}
				if(Enchantment.getByName(enchant.toUpperCase()) == null) {
					getLogger().warning("Enchantment \"" + enchant + "\" in the Kits section (Kit name: \"" + kitName + "\") of the enchants.yml is not a valid game enchantment.");
					continue INNER;
				}
				if(EndlessEnchantment.getByName(enchant) == null) {
					getLogger().warning("Enchantment \"" + enchant + "\" in the Kits section (Kit name: \"" + kitName + "\") of the enchants.yml is not a valid enchantment.");
					continue INNER;
				}
				kitEnchantments.add(EndlessEnchantment.getByName(enchant));
			}
			kits.add(new Kit(kitName.toUpperCase().replaceAll("&[0-9A-FK-OR]", ""), kitName.toUpperCase(), kitEnchantments));
		}
	}
	
	/** @return Base name associated with the presented alias - works for kits and enchantments */
	public String getBaseName(String alias) {
		if(enchantNames.keySet().contains(alias.toUpperCase())) return alias.toUpperCase(); //Not an alias
		for(Map.Entry<String, List<String>> entry : enchantNames.entrySet()) {
			if(entry.getValue().contains(alias.toUpperCase())) return entry.getKey().toUpperCase();
		}
		return null;
	}
	
	public String getLowerName(String formal) {
		return formal.toLowerCase().replace("_", " ");
	}
	
	/** @return true if the given 'possibleAlias' is associated with a Kit or EndlessEnchant */
	public boolean isValidEnch(String possibleAlias) {
		return getBaseName(possibleAlias) != null;
	}
	
	public Kit getKit(String possibleAlias) {
		for(Kit k : kits) {
			if(k.getName().equalsIgnoreCase(possibleAlias)) return k;
			if(enchantNames.get(k.getName()).contains(possibleAlias.toUpperCase())) return k;
		}
		return null;
	}
	
	enum GlowTagType { LORE, NBT; }
	
	public GlowTagType getGlowTagType(ItemStack i) {
		if(i == null || i.getType() == Material.AIR) return null;
		if(glowLore) {
			if(!i.hasItemMeta()) return null;
			ItemMeta im = i.getItemMeta();
			if(im.hasLore() && im.getLore().equals(Arrays.asList("\u00A77Glow I"))) {
				return GlowTagType.LORE;
			}
		}
		
		try {
			Object nms1Stack = getField(i, "handle");
			Object tagComp = nms1Stack.getClass().getMethod("getTag").invoke(nms1Stack);
			if(tagComp == null) {
				tagComp = Class.forName("net.minecraft.server." + serverVersion + ".NBTTagCompound").newInstance();
				nms1Stack.getClass().getMethod("setTag", tagComp.getClass()).invoke(nms1Stack, tagComp);
			}
			if(((Boolean)tagComp.getClass().getMethod("getBoolean", String.class).invoke(tagComp, "glowEffect")).booleanValue()) {
				return GlowTagType.NBT;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean hasGlowTag(ItemStack i) {
		if(getGlowTagType(i) != null) return true;
		return false;
	}
	
	public void addGlow(ItemStack item) {
		if(item == null) return;
		if(item.hasItemMeta() && item.getItemMeta().hasEnchants() && item.getItemMeta().getEnchants().size() >= 1) { //Adding glow would be redundant here
			return;
		}
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 0); //Only suitable enchantment for glow, NBT stopped functioning after 1.8
		ItemMeta i = item.getItemMeta();
		i.addItemFlags(ItemFlag.HIDE_ENCHANTS); //Don't let them see our little hack
		if(glowLore) i.setLore(Arrays.asList("\u00A77Glow I"));
		item.setItemMeta(i);
		
		if(!glowLore) {
			try {
				Object nmsStack = getField(item, "handle");
				Object tagComp = nmsStack.getClass().getMethod("getTag").invoke(nmsStack);
				if(tagComp == null) {
					tagComp = Class.forName("net.minecraft.server." + serverVersion + ".NBTTagCompound").newInstance();
				}
				tagComp.getClass().getMethod("setBoolean", String.class, boolean.class).invoke(tagComp, "glowEffect", true);
				nmsStack.getClass().getMethod("setTag", tagComp.getClass()).invoke(nmsStack, tagComp);
				item = (ItemStack) item.getClass().getMethod("asBukkitCopy", nmsStack.getClass()).invoke(null, nmsStack);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void removeGlow(ItemStack item) {
		if(hasGlowTag(item)) {
			ItemMeta im = item.getItemMeta();
			im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
			if(getGlowTagType(item) == GlowTagType.LORE) im.setLore(new ArrayList<String>());
			item.setItemMeta(im); //Do this before getField gets to it
		}
		try {
			Object nmsStack = getField(item, "handle");
			Object tagComp = nmsStack.getClass().getMethod("getTag").invoke(nmsStack);
			if(tagComp == null) {
				tagComp = Class.forName("net.minecraft.server." + serverVersion + ".NBTTagCompound").newInstance();
			}
			tagComp.getClass().getMethod("remove", String.class).invoke(tagComp, "ench");
			if(hasGlowTag(item) && getGlowTagType(item) == GlowTagType.NBT) {
				tagComp.getClass().getMethod("remove", String.class).invoke(tagComp, "glowEffect");
			}
			nmsStack.getClass().getMethod("setTag", tagComp.getClass()).invoke(nmsStack, tagComp);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private Object getField(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		}
		catch(Exception e) {
			// We don't care
			throw new RuntimeException("Unable to retrieve field content.", e);
		}
	}

	public void addEnchantment(ItemStack item, Enchantment enchantment, String level, boolean endless) {
		addEnchantment(item, enchantment, getNumber(level), endless);
	}
	
	public void addEnchantment(ItemStack item, Enchantment enchantment, int level, boolean endless) {
		if(!endless && level > highestLevel) level = highestLevel;
		if(hasGlowTag(item)) { //Ensures that our hack doesn't get found out (Durability 0 and Lore)
			removeGlow(item);
		}
		if(item.getType() != Material.BOOK && item.getType() != Material.ENCHANTED_BOOK && item.getType() != Material.BOOK_AND_QUILL) {
			item.addUnsafeEnchantment(enchantment, level);
			return;
		}
		//Cool book -> enchanted book functionality below
		if(item.getType() == Material.BOOK || item.getType() == Material.BOOK_AND_QUILL) {
			item.setType(Material.ENCHANTED_BOOK);
		}
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(enchantment, level, true);
		item.setItemMeta(meta);
	}
	
	public void removeEnchantment(ItemStack item, Enchantment enchantment) {
		if(item.getType() != Material.ENCHANTED_BOOK) {
			item.removeEnchantment(enchantment);
			return;
		}
		//Reverse enchanted book
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
		if(meta.getEnchants().size() <= 1) {
			item.setType(Material.BOOK);
		}
		else {
			meta.removeStoredEnchant(enchantment);
			item.setItemMeta(meta);
		}
	}
	
	public int getNumber(String num) {
		try {
			int i = Integer.parseInt(num);
			return i;
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public HashMap<String, List<String>> getEnchMap() {
		return enchantNames;
	}
	
	public int getHighest() {
		return highestLevel;
	}
	
	public List<Kit> getKits() {
		return kits;
	}
	
	public String getPrefix() {
		return EEPrefix;
	}
	
	@Override
	public YMLConfig getConfig() {
		return config;
	}
}

class Kit {
	private String name;
	private String format;
	private String suffix;
	private List<EndlessEnchantment> endEnchants;
	
	public Kit(String name, String format, List<EndlessEnchantment> endEnchants) {
		this.name = name;
		if(format.toCharArray()[format.length() - 2] == '&') {
			this.suffix = "\u00A7" + format.substring(format.length() - 1);
			this.format = format.substring(0, format.length() - 2);
		}
		else {
			this.format = format;
			this.suffix = "";
		}
		this.endEnchants = endEnchants;
	}
	
	public List<EndlessEnchantment> getEndlessEnchantments() {
		return endEnchants;
	}

	public String getName() {
		return name;
	}
	
	public String getFormat() {
		return format;
	}
	
	public String getSuffix() {
		return suffix;
	}
}