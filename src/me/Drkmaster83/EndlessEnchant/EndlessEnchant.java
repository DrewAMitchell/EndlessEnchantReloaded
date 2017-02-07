package me.Drkmaster83.EndlessEnchant;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class EndlessEnchant extends JavaPlugin implements Listener
{
	public PluginDescriptionFile pdf;
	public YMLConfig enchants, config;
	public static HashMap<String, List<String>> enchantNames;
	public static LinkedHashMap<Kit, List<EndlessEnchantment>> kits;
	public static EndlessEnchant plugin;
	private EndlessEnchantEventHandler eventHandler;
	public static String serverVersion;
	public static final String EEPrefix = "\u00A7f[\u00A7b\u00A7lEndless\u00A7c\u00A7lEnchant\u00A7f] ";
	private static String noPermission = EEPrefix + "\u00A74You do not have access to that command.";
	public static int highestLevel;
	public static boolean glowLore;
	
	@Override
	public void onEnable() {
		plugin = this;
		eventHandler = new EndlessEnchantEventHandler();
		pdf = this.getDescription();
		serverVersion = getServer().getClass().getPackage().getName().substring(getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		getServer().getPluginManager().registerEvents(eventHandler, this);
		
		enchants = new YMLConfig(this, "enchants.yml");
		config = new YMLConfig(this, "config.yml");
		if(config.create() == false) {
			getLogger().warning("Default config.yml file was not found, creating one for you...");
			this.saveResource("config.yml", true);
		}
		if(enchants.create() == false) {
			getLogger().warning("Enchants.yml file was not found, creating one for you...");
			this.saveResource("enchants.yml", true);
		}
		
		//Begin adding aliases and associated enchantments
		loadAliases();
		initEnchants();
		//End adding aliases and associated enchantments
		
		getLogger().info(pdf.getName() + " v" + pdf.getVersion() + " has been enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info(pdf.getName() + " v" + pdf.getVersion() + " has been disabled!");
	}
	
	private void loadAliases() {
		verifyFiles(); //Verifies our three sections for us
		enchantNames = new HashMap<String, List<String>>();
		for(String s : enchants.toFileConf().getConfigurationSection("Kits").getKeys(false)) { //Populate with kits' names and aliases
			s = s.replaceAll("(&([a-fk-or0-9A-FK-OR]))", "");
			List<String> aliasList = enchants.toFileConf().getStringList("KitAliases." + s) == null ? new ArrayList<String>() : new ArrayList<String>(enchants.toFileConf().getStringList("KitAliases." + s)); //Remove necessity for duplicate entry in KitAliases
			for(int i = 0; i < aliasList.size(); i++) {
				aliasList.set(i, aliasList.get(i).toUpperCase());
			}
			enchantNames.put(s.toUpperCase(), aliasList);
		}
		for(String s : enchants.toFileConf().getConfigurationSection("Aliases").getKeys(false)) { //Populate with enchantments and aliases
			if(EndlessEnchantment.getByName(s.toUpperCase()) == null) {
				getLogger().warning("Base enchantment name \"" + s.toUpperCase() + "\" doesn't exist (either in code or in-game) - will continue without it...");
				continue;
			}
			List<String> aliasList = new ArrayList<String>(enchants.toFileConf().getStringList("Aliases." + s));
			for(int i = 0; i < aliasList.size(); i++) {
				aliasList.set(i, aliasList.get(i).toUpperCase());
			}
			enchantNames.put(s.replaceAll("(&([a-fk-or0-9A-FK-OR]))", "").toUpperCase(), aliasList);
		}
	}
	
	private void verifyFiles() {
		Reader enchantsJar = null, configJar = null;
		try {
			enchantsJar = new InputStreamReader(getResource("enchants.yml"), "UTF8");
			configJar = new InputStreamReader(getResource("config.yml"), "UTF8");
		}
		catch(UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		
		YamlConfiguration cachedEnchants = YamlConfiguration.loadConfiguration(enchantsJar);
		YamlConfiguration cachedConfig = YamlConfiguration.loadConfiguration(configJar);
		if(enchants.toFileConf().getConfigurationSection("Aliases") == null) enchants.toFileConf().createSection("Aliases");
		for(String s : cachedEnchants.getConfigurationSection("Aliases").getKeys(false)) {
			if(!enchants.toFileConf().getConfigurationSection("Aliases").contains(s)) {
				enchants.toFileConf().set("Aliases." + s, cachedEnchants.getStringList("Aliases." + s));
			}
		}
		enchants.save();
		enchants.reload(); //Cleanup
		
		if(enchants.toFileConf().getConfigurationSection("Kits") == null) { //Probably erased them
			enchants.toFileConf().createSection("Kits");
			enchants.save();
		}
		if(enchants.toFileConf().getConfigurationSection("KitAliases") == null) { //Probably erased them
			enchants.toFileConf().createSection("KitAliases");
			enchants.save();
		}
		
		if(config.toFileConf().get("highestLevelLimit") == null) { //Needed for functionality
			config.set("highestLevelLimit", cachedConfig.get("highestLevelLimit"));
		}
		highestLevel = config.toFileConf().getInt("highestLevelLimit");
		if(config.toFileConf().get("glowInLore") == null) { //Needed for functionality
			config.set("glowInLore", cachedConfig.get("glowInLore"));
		}
		glowLore = config.toFileConf().getBoolean("glowInLore");
	}
	
	private void initEnchants() {
		kits = new LinkedHashMap<Kit, List<EndlessEnchantment>>();
		for(String s : enchants.toFileConf().getConfigurationSection("Kits").getKeys(false)) {
			List<EndlessEnchantment> toPut = new ArrayList<EndlessEnchantment>();
			INNER: for(String enchant : enchants.toFileConf().getStringList("Kits." + s)) {
				if(enchant.equals("*")) {
					for(EndlessEnchantment e : EndlessEnchantment.values()) {
						toPut.add(e);
					}
					continue;
				}
				else if(EndlessEnchantment.getByName(enchant.toUpperCase()) == null) {
					getLogger().warning("Enchantment \"" + enchant + "\" in the Kits section (Kit name: \"" + s + "\") of the enchants.yml is not a valid enchantment.");
					continue INNER;
				}
				toPut.add(EndlessEnchantment.getByName(getFormalName(enchant.toUpperCase())));
			}
			kits.put(new Kit(s.toUpperCase().replaceAll("(&([0-9A-FK-OR]))", ""), s.toUpperCase()), toPut);
		}
	}
	
	public static String getFormalName(String alias) {
		if(enchantNames.keySet().contains(alias.toUpperCase())) return alias.toUpperCase(); //Not an alias
		for(List<String> aliasList : enchantNames.values()) {
			if(aliasList.contains(alias.toUpperCase())) return MapUtils.getKeyByValue(enchantNames, aliasList).toUpperCase();
		}
		return null;
	}
	
	public static String getDisplayName(String formal) {
		return formal.toLowerCase().replace("_", " ");
	}
	
	public static boolean isValid(String s) {
		return (getFormalName(s) != null);
	}
	
	enum GlowTagType { LORE, NBT; }
	
	public static GlowTagType getGlowTagType(ItemStack i) {
		if(glowLore) {
			if(!i.hasItemMeta()) return null;
			ItemMeta im = i.getItemMeta();
			if(im.hasLore() && im.getLore().equals(Arrays.asList("\u00A77Glow I"))) {
				return GlowTagType.LORE;
			}
			return null;
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
	
	public static boolean hasGlowTag(ItemStack i) {
		if(getGlowTagType(i) != null) return true;
		return false;
	}
	
	private static void addGlow(ItemStack item) {
		if(item == null) return;
		if(item.hasItemMeta() && item.getItemMeta().hasEnchants() && item.getItemMeta().getEnchants().size() >= 1) {
			return;
		}
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
		ItemMeta i = item.getItemMeta();
		i.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		if(glowLore) i.setLore(Arrays.asList("\u00A77Glow I"));
		item.setItemMeta(i);
		
		if(!glowLore) {
			try {
				Object nms1Stack = getField(item, "handle");
				Object tagComp = nms1Stack.getClass().getMethod("getTag").invoke(nms1Stack);
				if(tagComp == null) {
					tagComp = Class.forName("net.minecraft.server." + serverVersion + ".NBTTagCompound").newInstance();
					nms1Stack.getClass().getMethod("setTag", tagComp.getClass()).invoke(nms1Stack, tagComp);
				}
				tagComp.getClass().getMethod("setBoolean", String.class, boolean.class).invoke(tagComp, "glowEffect", true);
				nms1Stack.getClass().getMethod("setTag", tagComp.getClass()).invoke(nms1Stack, tagComp);
				item = (ItemStack) item.getClass().getMethod("asBukkitCopy", nms1Stack.getClass()).invoke(null, nms1Stack);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void removeGlow(ItemStack item) {
		if(hasGlowTag(item)) {
			ItemMeta im = item.getItemMeta();
			im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
			if(getGlowTagType(item).equals(GlowTagType.LORE)) im.setLore(new ArrayList<String>());
			item.setItemMeta(im); //Do this before getField gets to it
		}
		try {
			Object nms1Stack = getField(item, "handle");
			Object tagComp = nms1Stack.getClass().getMethod("getTag").invoke(nms1Stack);
			if(tagComp == null) {
				tagComp = Class.forName("net.minecraft.server." + serverVersion + ".NBTTagCompound").newInstance();
				nms1Stack.getClass().getMethod("setTag", tagComp.getClass()).invoke(nms1Stack, tagComp);
			}
			tagComp.getClass().getMethod("remove", String.class).invoke(tagComp, "ench");
			if(hasGlowTag(item) && getGlowTagType(item).equals(GlowTagType.NBT)) {
				tagComp.getClass().getMethod("remove", String.class).invoke(tagComp, "glowEffect");
			}
			nms1Stack.getClass().getMethod("setTag", tagComp.getClass()).invoke(nms1Stack, tagComp);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static Object getField(Object obj, String name) {
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

	public static boolean addEnchantment(ItemStack item, Enchantment enchantment, String level, boolean endless) {
		return addEnchantment(item, enchantment, getNumber(level), endless);
	}
	
	public static boolean addEnchantment(ItemStack item, Enchantment enchantment, int level, boolean endless) {
		if(!endless && level > highestLevel) level = highestLevel;
		if(hasGlowTag(item)) {
			removeGlow(item);
		}
		if(item.getType() != Material.BOOK && item.getType() != Material.ENCHANTED_BOOK && item.getType() != Material.BOOK_AND_QUILL) {
			item.addUnsafeEnchantment(enchantment, level);
		}
		else {
			if(item.getType() == Material.BOOK) {
				item.setType(Material.ENCHANTED_BOOK);
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(enchantment, level, true);
				item.setItemMeta(meta);
			}
			else if(item.getType() == Material.BOOK_AND_QUILL) {
				item.setType(Material.ENCHANTED_BOOK);
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(enchantment, level, true);
				item.setItemMeta(meta);
			}
			else {
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
				meta.addStoredEnchant(enchantment, level, true);
				item.setItemMeta(meta);
			}
		}
		return true;
	}
	
	public static boolean removeEnchantment(ItemStack item, Enchantment enchantment) {
		if(item.getType() != Material.ENCHANTED_BOOK) {
			item.removeEnchantment(enchantment);
		}
		else {
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
			if(meta.getEnchants().size() == 0) {
				meta.removeStoredEnchant(enchantment);
				item.setType(Material.BOOK);
				meta.setDisplayName("Book");
				item.setItemMeta(meta);
			}
			else {
				meta.removeStoredEnchant(enchantment);
				item.setItemMeta(meta);
			}
		}
		return true;
	}
	
	/** Attempts to enchant, disenchant, or add the glowing effect to an item, depending on the inputs. */
	public static boolean attemptAction(Player player, String[] args, ItemStack i) {
		boolean ePerm = player.hasPermission("EndlessEnchant.Enchant") || player.hasPermission("EndlessEnchant.Enchant.*");
		if(!ePerm) {
			player.sendMessage(noPermission);
			return true;
		}
		ItemStack item = player.getInventory().getItemInMainHand();
		boolean endless = player.hasPermission("EndlessEnchant.Endless");
		String invalidLevel = EEPrefix + "\u00A74A level \u00A76(0-32767)\u00A74 is required to enchant an item.";
		String invalidEnchant = EEPrefix + "\u00A74That is not a valid enchantment name.";
		String message = EEPrefix + "\u00A76The enchantment";
		if(args.length >= 1) {
			boolean add = args[0].equalsIgnoreCase("Add") || args[0].equalsIgnoreCase("Enchant"); //TODO: Possibly make a permission for every existing enchant
			boolean remove = args[0].equalsIgnoreCase("Remove") || args[0].equalsIgnoreCase("Disenchant");
			if(item == null || item.getType().equals(Material.AIR)) {
				player.sendMessage(EEPrefix + "\u00A74You must have an item in your hand to begin enchanting.");
				return true;
			}
			if(args[0].equalsIgnoreCase("Glow")) {
				addGlow(item);
				player.sendMessage(EEPrefix + "\u00A7cGlow \u00A76has been applied to your item in hand.");
				return true;
			}
			if(add || remove) {
				if(args.length == 1) {
					player.sendMessage(EEPrefix + "\u00A74An enchantment name " + (add ? "and a level (0-32767) " : "") + "is required to " + (remove ? "dis" : "") + "enchant an item.");
					return true;
				}
				else if(args.length >= 2) {
					if(remove) {
						if(args[1].equalsIgnoreCase("Glow")) {
							removeGlow(item);
							player.sendMessage(EEPrefix + "\u00A7cGlow and all enchants \u00A76have been removed from your item in hand.");
							return true;
						}
						if(args[1].equalsIgnoreCase("All")) {
							removeGlow(item);
							player.sendMessage(EEPrefix + "\u00A7cAll enchants \u00A76have been removed from your item in hand.");
							return true;
						}
						else if(isValid(args[1])) {
							String formal = getFormalName(args[1]);
							if(EndlessEnchantment.getByName(formal) == null) { //Determine whether a kit or single enchantment
								if(getKitByName(formal) != null) {
									Kit k = getKitByName(formal);
									if(!player.hasPermission("EndlessEnchant.Kits."+k.getName()) && !player.hasPermission("EndlessEnchant.Kits.*")) {
										player.sendMessage(EEPrefix + "\u00A74Sorry, but you do not have the permissions for that kit!");
										return true;
									}
									message += "s \u00A7c";
									int t = kits.get(k).size();
									for(EndlessEnchantment e : kits.get(k)) {
										if(t == 1) message += "and " + getDisplayName(e.name()) + " \u00A76have been removed from your item in hand.";
										if(t > 1) message += getDisplayName(e.name()) + ", ";
										removeEnchantment(item, Enchantment.getByName(e.name()));
										t--;
									}
								}
								else {
									player.sendMessage(invalidEnchant);
									return true;
								}
							}
							else {
								message += " \u00A7c" + getDisplayName(formal) + " \u00A76has been removed from your item.";
								removeEnchantment(item, Enchantment.getByName(formal));
							}
						}
						else {
							player.sendMessage(invalidEnchant);
							return true;
						}
					}
					else if(add) {
						if(args.length == 2) {
							if(args[1].equalsIgnoreCase("Glow")) {
								addGlow(item);
								player.sendMessage(EEPrefix + "\u00A7cGlow \u00A76has been applied to your item in hand.");
								return true;
							}
							player.sendMessage(invalidLevel);
							return true;
						}
						else if(args.length > 2) {
							if(args[1].equalsIgnoreCase("Glow")) {
								addGlow(item);
								player.sendMessage(EEPrefix + "\u00A7cGlow \u00A76has been applied to your item in hand.");
								return true;
							}
							if(isValid(args[1])) {
								String formal = getFormalName(args[1]);
								int level = getNumber(args[2]);
								if(EndlessEnchantment.getByName(formal) == null) {
									if(getKitByName(formal) != null) {
										Kit k = getKitByName(formal);
										if(!player.hasPermission("EndlessEnchant.Kits."+k.getName()) && !player.hasPermission("EndlessEnchant.Kits.*")) {
											player.sendMessage(EEPrefix + "\u00A74Sorry, but you do not have the permissions for that kit!");
											return true;
										}
										if(level < 0) {
											player.sendMessage(EEPrefix + "\u00A74Level cannot be negative, setting level to 1.");
											level = 1;
										}
										if(level > Short.MAX_VALUE) {
											player.sendMessage(EEPrefix + "\u00A74Level too high, setting level to " + Short.MAX_VALUE + ".");
											level = Short.MAX_VALUE;
										}
										message += "s \u00A7c";
										int t = kits.get(k).size();
										for(EndlessEnchantment e : kits.get(k)) {
											if(t == 1) message += "and " + getDisplayName(e.name()) + " \u00A76have been applied to your item in hand" + (!endless && getNumber(args[2]) > highestLevel ? ", but due to a limitation, you are only allowed up to level " + highestLevel : "") + ".";
											if(t > 1) message += getDisplayName(e.name()) + ", ";
											addEnchantment(item, Enchantment.getByName(e.name()), level, endless);
											t--;
										}
									}
									else {
										player.sendMessage(invalidEnchant);
										return true;
									}
								}
								else {
									if(level < 0) {
										player.sendMessage(EEPrefix + "\u00A74Level cannot be negative, setting level to 1.");
										level = 1;
									}
									if(level > Short.MAX_VALUE) {
										player.sendMessage(EEPrefix + "\u00A74Level too high, setting level to " + Short.MAX_VALUE + ".");
										level = Short.MAX_VALUE;
									}
									message += " \u00A7c" + getDisplayName(formal) + " \u00A76has been applied to your item in hand" + (!endless && getNumber(args[2]) > highestLevel ? ", but due to a limitation, you are only allowed up to level " + highestLevel : "") + ".";
									addEnchantment(item, Enchantment.getByName(formal), level, endless);
								}
							}
							else {
								
								player.sendMessage(invalidEnchant);
								return true;
							}
						}
					}
				}
			}
			else { //Assume adding
				if(args.length == 1) {
					if(isValid(args[0])) {
						player.sendMessage(invalidLevel);
						return true;
					}
					else {
						player.sendMessage(invalidEnchant);
						return true;
					}
				}
				else {
					if(isValid(args[0])) {
						int level = getNumber(args[1]);
						String formal = getFormalName(args[0]);
						if(EndlessEnchantment.getByName(formal) == null) {
							if(getKitByName(formal) != null) {
								Kit k = getKitByName(formal);
								if(!player.hasPermission("EndlessEnchant.Kits."+k.getName()) && !player.hasPermission("EndlessEnchant.Kits.*")) {
									player.sendMessage(EEPrefix + "\u00A74Sorry, but you do not have the permissions for that kit!");
									return true;
								}
								if(level < 0) {
									player.sendMessage(EEPrefix + "\u00A74Level cannot be negative, setting level to 1.");
									level = 1;
								}
								if(level > Short.MAX_VALUE) {
									player.sendMessage(EEPrefix + "\u00A74Level too high, setting level to " + Short.MAX_VALUE + ".");
									level = Short.MAX_VALUE;
								}
								message += "s \u00A7c";
								int t = kits.get(k).size();
								for(EndlessEnchantment e : kits.get(k)) {
									if(t == 1) message += "and " + getDisplayName(e.name()) + " \u00A76have been applied to your item in hand" + (!endless && getNumber(args[1]) > highestLevel ? ", but due to a limitation, you are only allowed up to level " + highestLevel : "") + ".";
									if(t > 1) message += getDisplayName(e.name()) + ", ";
									addEnchantment(item, Enchantment.getByName(e.name()), args[1], endless);
									t--;
								}
							}
							else {
								player.sendMessage(invalidEnchant);
								return true;
							}
						}
						else {
							if(level < 0) {
								player.sendMessage(EEPrefix + "\u00A74Level cannot be negative, setting level to 1.");
								level = 1;
							}
							if(level > Short.MAX_VALUE) {
								player.sendMessage(EEPrefix + "\u00A74Level too high, setting level to " + Short.MAX_VALUE + ".");
								level = Short.MAX_VALUE;
							}
							message += " \u00A7c" + getDisplayName(formal) + " \u00A76has been applied to your item in hand" + (!endless && getNumber(args[1]) > highestLevel ? ", but due to a limitation, you are only allowed up to level " + highestLevel : "") + ".";
							addEnchantment(item, Enchantment.getByName(formal), args[1], endless);
						}
					}
					else {
						player.sendMessage(invalidEnchant);
						return true;
					}
				}
			}
		}
		player.sendMessage(message);
		return true;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.getServer().getConsoleSender().sendMessage("\u00A7cYou are unable to use this command.");
			return true;
		}
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("EndlessEnchant")) {
			if(args.length == 0 || (args.length >= 1 && args[0].equalsIgnoreCase("Help"))) {
				if(!player.hasPermission("EndlessEnchant.Help")) {
					player.sendMessage(noPermission);
					return true;
				}
				player.sendMessage("\u00A78==================" + EEPrefix.replace(" ", "") + "\u00A78==================");
				player.sendMessage("\u00A74\u00A74To see the usage of /EE, type \u00A76/EE Usage\u00A74.");
				player.sendMessage("\u00A74To see a list of Enchantments \u00A7c(Non-Aliased)\u00A74, type \u00A7b/EE Enchantments" + "\u00A74.");
				player.sendMessage("\u00A74To get the aliases of a certain enchantment, type \u00A72/EE Alias <Enchantment>\u00A74.");
				player.sendMessage("\u00A74To see a list of Enchantment Kits, type \u00A7c/EE Kits\u00A74.");
				player.sendMessage("\u00A78=====================================================");
				return true;
			}
			else if(args.length >= 1) {
				if(args[0].equalsIgnoreCase("Usage") || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("/")) {
					if(!player.hasPermission("EndlessEnchant.Usage")) {
						player.sendMessage(noPermission);
						return true;
					}
					player.sendMessage(EEPrefix + "\u00A74Usage:\u00A76 /EE \u00A73[Add/Remove] \u00A7a[Enchantment] \u00A72{Level} (not needed if removing)\u00A76.");
				}
				else if(args[0].equalsIgnoreCase("Enchants") || args[0].equalsIgnoreCase("Enchantment") || args[0].equalsIgnoreCase("Enchantments") || args[0].equalsIgnoreCase("List")) {
					if(!player.hasPermission("EndlessEnchant.Enchantments")) {
						player.sendMessage(noPermission);
						return true;
					}
					String temp = "", lastColor = "";
					int index = EndlessEnchantment.values().length;
					for(EndlessEnchantment e : EndlessEnchantment.values()) {
						if(!e.getCategoryColor().equals(lastColor)) {
							lastColor = e.getCategoryColor();
							temp += "\u00A7" + lastColor;
						}
						if(index == 1) {
							temp += e.name() + ".";
							break;
						}
						temp += e.name() + ", ";
						if(index == 2) temp += "AND ";
						index--;
					}
					player.sendMessage(EEPrefix + temp);
				}
				else if(args[0].equalsIgnoreCase("Alias") || args[0].equalsIgnoreCase("Aliases")) {
					if(!player.hasPermission("EndlessEnchant.Aliases")) {
						player.sendMessage(noPermission);
						return true;
					}
					if(args.length == 1) {
						player.sendMessage(EEPrefix + "\u00A74An enchantment name to get the alias of is required.");
						return true;
					}
					if(args.length > 1) {
						if(isValid(args[1])) {
							player.sendMessage(EEPrefix + "\u00A73" + getFormalName(args[1]) + " Aliases: " + list("AND", enchantNames.get(getFormalName(args[1]))) + ".");
						}
						else {
							player.sendMessage(EEPrefix + "\u00A7cThat enchantment name is not valid.");
						}
					}
				}
				else if(args[0].equalsIgnoreCase("Kits")) {
					if(!player.hasPermission("EndlessEnchant.Kits") && !player.hasPermission("EndlessEnchant.Kits.List")) {
						player.sendMessage(noPermission);
						return true;
					}
					player.sendMessage("\u00A78==================" + EEPrefix.replaceAll(" ", "") + "\u00A78==================");
					for(Kit k : kits.keySet()) {
						List<String> enchants = new ArrayList<String>();
						for(EndlessEnchantment e : kits.get(k)) {
							enchants.add(e.name());
						}
						player.sendMessage(k.getFormat().replaceAll("(&([a-fk-orA-FK-OR0-9]))", "\u00A7$2") + ": " + k.getSuffix() + list("AND", enchants) + ".");
					}
					player.sendMessage("\u00A7c\u00A7lPlease note: These kit names are enchantment names!");
					player.sendMessage("\u00A78=====================================================");
				}
				else {
					return attemptAction(player, args, player.getInventory().getItemInMainHand());
				}
			}
		}
		return true;
	}
	
	public static int getNumber(String num) {
		try {
			int i = Integer.parseInt(num);
			return i;
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public static Kit getKitByName(String formal) {
		for(Kit k : kits.keySet()) {
			if(k.getName().equalsIgnoreCase(formal)) return k;
		}
		return null;
	}
	
	public String list(String conjunction, List<String> args) {
		String plural = "";
		if(args.size() == 0) return "";
		List<String> temp = new ArrayList<String>();
		for(String s : args) {
			if(!s.equals("")) temp.add(s);
		}
		if(temp.size() == 1) {
			plural += temp.get(0);
			return plural;
		}
		if(temp.size() == 2) {
			plural += temp.get(0) + " " + conjunction + " " + temp.get(1);
			return plural;
		}
		if(temp.size() > 2) {
			while(temp.size() > 0) {
				if(temp.size() == 2) {
					plural += temp.get(0) + ", " + conjunction + " " + temp.get(1);
					break;
				}
				else if(temp.size() > 2) {
					plural += temp.get(0) + ", ";
					temp.remove(temp.get(0));
				}
			}
		}
		return plural;
	}
}

class Kit {
	private String name;
	private String format;
	private String suffix;
	
	public Kit(String name, String format) {
		this.name = name;
		this.format = format;
		if(format.toCharArray()[format.length() - 2] == '&') {
			suffix = "\u00A7" + format.substring(format.length() - 1);
			this.format = format.substring(0, format.length() - 2);
		}
		else suffix = "";
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