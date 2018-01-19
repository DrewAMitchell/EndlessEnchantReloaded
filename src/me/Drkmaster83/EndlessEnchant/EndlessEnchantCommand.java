package me.Drkmaster83.EndlessEnchant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EndlessEnchantCommand implements CommandExecutor, TabCompleter {
	private EndlessEnchant ee;
	
	private final String invalidLevel;
	private final String invalidEnchant;
	private final String noPermission;
	
	enum EnchantAction {ADD, IMPLICIT_ADD, REMOVE}
	
	public EndlessEnchantCommand(EndlessEnchant ee) {
		this.ee = ee;
		invalidLevel = ee.getPrefix() + "\u00A74A level \u00A76(0-32767)\u00A74 is required to enchant an item.";
		invalidEnchant = ee.getPrefix() + "\u00A74That is not a valid enchantment name.";
		noPermission = ee.getPrefix() + "\u00A74You do not have access to that command.";
	}
	
	public void performAction(Player player, EnchantAction act, String[] args) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if(args.length == 0) return;

		String message = ee.getPrefix() + "\u00A76The enchantment";
		if(act == EnchantAction.ADD || act == EnchantAction.IMPLICIT_ADD) {
			boolean endless = player.hasPermission("EndlessEnchant.Endless");
			int offset = act == EnchantAction.ADD ? 1 : 0;
			if(args.length == (1+offset)) {
				if(equalsAny(args[0+offset], "Glow", "Glowing")) {
					ee.addGlow(item);
					player.sendMessage(ee.getPrefix() + "\u00A7cGlow \u00A76has been applied to your item in hand.");
					return;
				}
				player.sendMessage(invalidLevel);
				return;
			}
			else if(args.length > 1+offset) {
				String enchName = args[0+offset];
				if(equalsAny(enchName, "Glow", "Glowing")) {
					ee.addGlow(item);
					player.sendMessage(ee.getPrefix() + "\u00A7cGlow \u00A76has been applied to your item in hand.");
					return;
				}
				if(!ee.isValidEnch(enchName)) {
					player.sendMessage(invalidEnchant);
					return;
				}
				String formal = ee.getBaseName(enchName);
				int level = ee.getNumber(args[1+offset]);
				if(EndlessEnchantment.getByName(formal) == null) {
					Kit k = ee.getKit(formal);
					if(!player.hasPermission("EndlessEnchant.ee.Kits."+k.getName()) && !player.hasPermission("EndlessEnchant.Kits.*")) {
						player.sendMessage(ee.getPrefix() + "\u00A74Sorry, but you do not have the permissions for that kit!");
						return;
					}
					if(level < 0) { //We place these separate because of the permission check above.
						player.sendMessage(ee.getPrefix() + "\u00A74Level cannot be negative, setting level to 1.");
						level = 1;
					}
					if(level > Short.MAX_VALUE) {
						player.sendMessage(ee.getPrefix() + "\u00A74Level too high, setting level to " + Short.MAX_VALUE + ".");
						level = Short.MAX_VALUE;
					}
					message += "s \u00A7c"; //Pluralize
					int t = k.getEndlessEnchantments().size();
					for(EndlessEnchantment e : k.getEndlessEnchantments()) {
						if(t == 1) message += "and " + ee.getLowerName(e.name()) + " \u00A76have been applied to your item in hand" + (!endless && level > ee.getHighest() ? ", but due to a limitation, you are only allowed up to level " + ee.getHighest() : "") + ".";
						if(t > 1) message += ee.getLowerName(e.name()) + ", ";
						ee.addEnchantment(item, Enchantment.getByName(e.name()), level, endless);
						t--;
					}
				}
				else {
					//TODO if you'd add per-enchantment permissions, it'd go here
					if(level < 0) {
						player.sendMessage(ee.getPrefix() + "\u00A74Level cannot be negative, setting level to 1.");
						level = 1;
					}
					if(level > Short.MAX_VALUE) {
						player.sendMessage(ee.getPrefix() + "\u00A74Level too high, setting level to " + Short.MAX_VALUE + ".");
						level = Short.MAX_VALUE;
					}
					message += " \u00A7c" + ee.getLowerName(formal) + " \u00A76has been applied to your item in hand" + (!endless && level > ee.getHighest() ? ", but due to a limitation, you are only allowed up to level " + ee.getHighest() : "") + ".";
					ee.addEnchantment(item, Enchantment.getByName(formal), level, endless);
				}
			}
		}
		else {
			if(args.length < 2) return;
			if(equalsAny(args[1], "Glow", "Glowing")) {
				ee.removeGlow(item);
				player.sendMessage(ee.getPrefix() + "\u00A7cGlow and all enchants \u00A76have been removed from your item in hand.");
				return;
			}
			if(equalsAny(args[1], "*", "All")) {
				if(item.getType() != Material.ENCHANTED_BOOK) ee.removeGlow(item);
				else item.setType(Material.BOOK);
				player.sendMessage(ee.getPrefix() + "\u00A7cAll enchants \u00A76have been removed from your item in hand.");
				return;
			}
			if(!ee.isValidEnch(args[1])) {
				player.sendMessage(invalidEnchant);
				return;
			}
			
			String formal = ee.getBaseName(args[1]);
			if(EndlessEnchantment.getByName(formal) == null) { //Determine whether a kit or single enchantment
				Kit k = ee.getKit(formal);
				if(!player.hasPermission("EndlessEnchant.Kits."+k.getName()) && !player.hasPermission("EndlessEnchant.Kits.*")) {
					player.sendMessage(ee.getPrefix() + "\u00A74Sorry, but you do not have the permissions for that kit!");
					return;
				}
				message += "s \u00A7c";
				int t = k.getEndlessEnchantments().size();
				for(EndlessEnchantment e : k.getEndlessEnchantments()) {
					if(t == 1) message += "and " + ee.getLowerName(e.name()) + " \u00A76have been removed from your item in hand.";
					if(t > 1) message += ee.getLowerName(e.name()) + ", ";
					ee.removeEnchantment(item, Enchantment.getByName(e.name()));
					t--;
				}
			}
			else {
				//TODO if you'd add per-enchantment permissions, it'd go here
				ee.removeEnchantment(item, Enchantment.getByName(formal));
				message += " \u00A7c" + ee.getLowerName(formal) + " \u00A76has been removed from your item.";
			}
		}
		player.sendMessage(message);
	}
	
	/** Attempts to enchant, disenchant, or add the glowing effect to an item, depending on the inputs. */
	public void parseCommand(Player player, String[] args) {
		boolean ePerm = player.hasPermission("EndlessEnchant.Enchant") || player.hasPermission("EndlessEnchant.Enchant.*");
		if(!ePerm) {
			player.sendMessage(noPermission);
			return;
		}
		if(args.length < 1) return; //Should never happen but ensures we don't throw an AIOOB
		
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item == null || item.getType() == Material.AIR) {
			player.sendMessage(ee.getPrefix() + "\u00A74You must have an item in your hand to begin enchanting.");
			return;
		}
		boolean add = equalsAny(args[0], "Add", "Enchant"); //TODO: Possibly make a permission for every existing enchant
		boolean remove = equalsAny(args[0], "Remove", "Disenchant");
		boolean implicitAdd = !(add || remove);
		
		if(implicitAdd) {
			performAction(player, EnchantAction.IMPLICIT_ADD, args);
			return;
		}
		if(args.length == 1) {
			player.sendMessage(ee.getPrefix() + "\u00A74An enchantment name " + (add ? "and a level \u00A76(0-32767)\u00A74 " : "") + "is required to " + (remove ? "dis" : "") + "enchant an item.");
			return;
		}
		else if(args.length >= 2) {
			if(remove) {
				performAction(player, EnchantAction.REMOVE, args);
			}
			else if(add) {
				performAction(player, EnchantAction.ADD, args);
			}
			else return;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("EndlessEnchant")) return false;
		if(!(sender instanceof Player)) {
			sender.getServer().getConsoleSender().sendMessage("\u00A7cYou are unable to use this command.");
			return true;
		}
		Player player = (Player) sender;
		if(args.length == 0 || (args.length >= 1 && args[0].equalsIgnoreCase("Help"))) {
			if(!player.hasPermission("EndlessEnchant.Help")) {
				player.sendMessage(noPermission);
				return true;
			}
			player.sendMessage("\u00A78==================" + ee.getPrefix().replace(" ", "") + "\u00A78==================");
			player.sendMessage("\u00A74\u00A74To see the usage of /EE, type \u00A76/EE Usage\u00A74.");
			player.sendMessage("\u00A74To see a list of Enchantments \u00A7c(Non-Aliased)\u00A74, type \u00A7b/EE Enchantments" + "\u00A74.");
			player.sendMessage("\u00A74To get the aliases of a certain enchantment, type \u00A72/EE Alias <Enchantment>\u00A74.");
			player.sendMessage("\u00A74To see a list of Enchantment Kits, type \u00A7c/EE Kits\u00A74.");
			player.sendMessage("\u00A78=====================================================");
			return true;
		}
		//args.length >= 1 and args[0] isn't help
		if(equalsAny(args[0], "Usage", "?", "/")) {
			if(!player.hasPermission("EndlessEnchant.Usage")) {
				player.sendMessage(noPermission);
				return true;
			}
			player.sendMessage(ee.getPrefix() + "\u00A74Usage:\u00A76 /EE \u00A73[Add/Remove] \u00A7a[Enchantment] \u00A72{Level} (not needed if removing)\u00A76.");
		}
		else if(equalsAny(args[0], "Enchants", "Enchantment", "Enchantments", "List")) {
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
			player.sendMessage(ee.getPrefix() + temp);
		}
		else if(equalsAny(args[0], "Alias", "Aliases")) {
			if(!player.hasPermission("EndlessEnchant.Aliases")) {
				player.sendMessage(noPermission);
				return true;
			}
			if(args.length == 1) {
				player.sendMessage(ee.getPrefix() + "\u00A74An enchantment name to get the alias of is required.");
				return true;
			}
			if(args.length > 1) {
				if(ee.isValidEnch(args[1])) {
					String baseName = ee.getBaseName(args[1]);
					player.sendMessage(ee.getPrefix() + "\u00A73" + baseName + " Aliases: " + (ee.getEnchMap().get(baseName).size() == 0 ? "[None]" : list("AND", ee.getEnchMap().get(baseName))) + ".");
				}
				else {
					player.sendMessage(ee.getPrefix() + "\u00A74That enchantment name is not valid.");
				}
			}
		}
		else if(args[0].equalsIgnoreCase("Kits")) {
			if(!player.hasPermission("EndlessEnchant.Kits") && !player.hasPermission("EndlessEnchant.Kits.List")) {
				player.sendMessage(noPermission);
				return true;
			}
			if(ee.getKits().size() == 0) {
				player.sendMessage(ee.getPrefix() + "\u00A74There are no defined enchantment kits; verify that you've configured them properly!");
				return true;
			}
			player.sendMessage("\u00A78==================" + ee.getPrefix().trim() + "\u00A78==================");
			for(Kit k : ee.getKits()) {
				List<String> enchants = new ArrayList<String>();
				for(EndlessEnchantment e : k.getEndlessEnchantments()) {
					enchants.add(e.name());
				}
				player.sendMessage(k.getFormat().replaceAll("(?i)(&([0-9A-FK-OR]))", "\u00A7$2") + ": " + k.getSuffix() + list("AND", enchants) + ".");
			}
			player.sendMessage("\u00A7c\u00A7lPlease note: These kit names are enchantment names!");
			player.sendMessage("\u00A78=====================================================");
		}
		else {
			parseCommand(player, args);
		}
		return true;
	}
	
	public boolean equalsAny(String base, String... comps) {
		for(String comp : comps) {
			if(comp.equalsIgnoreCase(base)) return true;
		}
		return false;
	}
	
	public String list(String conjunction, List<String> args) {
		String plural = "";
		if(args.size() == 0) return "";
		List<String> temp = new ArrayList<String>();
		for(String s : args) {
			if(!s.trim().equals("")) temp.add(s);
		}
		if(temp.size() == 1) {
			plural += temp.get(0);
		}
		else if(temp.size() == 2) {
			plural += temp.get(0) + " " + conjunction + " " + temp.get(1);
		}
		else if(temp.size() > 2) {
			for(int i = 0; i < temp.size(); i++) {
				if(i == temp.size() - 2) {
					plural += temp.get(i) + ", " + conjunction + " " + temp.get(i+1);
					break;
				}
				plural += temp.get(i) + ", ";
			}
		}
		return plural;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("EndlessEnchant")) return Arrays.asList("");
		if(!(sender instanceof Player)) {
			return Arrays.asList("");
		}
		if(args.length == 1) {
			ArrayList<String> firstArg = new ArrayList<String>(Arrays.asList("Add", "Alias", "Enchantments", "Help", "Kits", "Remove", "Usage"));
			for(int i = 0; i < firstArg.size(); i++) {
				if(!firstArg.get(i).toUpperCase().startsWith(args[0].toUpperCase())) {
					firstArg.remove(i);
					i--;
				}
			}
			Collections.sort(firstArg);
			return firstArg;
		}
		else if(args.length == 2) {
			if(equalsAny(args[0], "Enchants", "Enchantment", "Enchantments", "List", "Help", "Usage", "/", "?", "Kits")) return Arrays.asList("");
			ArrayList<String> tab = new ArrayList<String>();
			for(String s : ee.getEnchMap().keySet()) {
				if(s.toUpperCase().startsWith(args[1].toUpperCase())) tab.add(s);
			}
			Collections.sort(tab);
			return tab;
		}
		else if(args.length == 3) {
			if(equalsAny(args[0], "Enchants", "Enchantment", "Enchantments", "List", "Help", "Usage", "/", "?", "Kits", "Remove", "Disenchant")) return Arrays.asList("");
			return (List<String>) Arrays.asList(""+ee.getHighest());
		}
		else return (List<String>) Arrays.asList("");
	}
}