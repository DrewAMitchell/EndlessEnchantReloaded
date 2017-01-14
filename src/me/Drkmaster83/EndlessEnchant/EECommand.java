package me.Drkmaster83.EndlessEnchant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.v1_7_R3.ItemStack;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class EECommand implements CommandExecutor
{
	public HashMap<ArrayList<String>, Enchantment> enchants = new HashMap<ArrayList<String>, Enchantment>();
	public HashMap<ArrayList<String>, List<Enchantment>> collectiveEnchants = new HashMap<ArrayList<String>, List<Enchantment>>();
	String EEPrefix = "\u00A7f[\u00A7b\u00A7lEndless\u00A7c\u00A7lEnchant\u00A7f] ";

	ArrayList<String> all = new ArrayList<String> (Arrays.asList("ALL"));
	ArrayList<String> armor = new ArrayList<String> (Arrays.asList("ARMOR", "ARMOUR"));
	ArrayList<String> sword = new ArrayList<String> (Arrays.asList("SWORD", "SWORDS"));
	ArrayList<String> tools = new ArrayList<String> (Arrays.asList("TOOL", "TOOLS"));
	ArrayList<String> bows = new ArrayList<String> (Arrays.asList("BOW", "BOWS"));
	ArrayList<String> fishingRods = new ArrayList<String> (Arrays.asList("FISHING", "ROD", "FISHINGROD"));
	ArrayList<String> prot0 = new ArrayList<String> (Arrays.asList("PROTECTION", "PROT", "PROTECTIONALL", "PROTECTION_ALL", "PROT_ALL", "ProtAll", "PROTECTION_ENVIRONMENTAL", "PROT_ENVIRONMENTAL", "0"));
	ArrayList<String> prot1 = new ArrayList<String> (Arrays.asList("FIREPROTECTION", "FIRE_PROTECTION", "FIREPROTECTION", "FIREPROT", "FIRE_PROT", "PROTECTION_FIRE", "PROT_FIRE", "FLAME_PROTECTION", "FLAME_PROT", "FLAMEPROT", "FLAMEPROTECTION", "1"));
	ArrayList<String> prot2 = new ArrayList<String> (Arrays.asList("FEATHERFALLING", "FEATHER_FALLING", "PROTECTION_FALL", "PROT_FALL", "FEATHER", "FEATHERS", "NOFALL", "NO_FALL", "2"));
	ArrayList<String> prot3 = new ArrayList<String> (Arrays.asList("BLASTPROTECTION", "BLAST_PROTECTION", "BLASTPROT", "PROTECTION_EXPLOSIONS", "PROT_EXPLOSIONS", "BLAST_PROT", "EXPLOSIONSPROT", "EXPLOSIONS_PROT", "EXPLOSIONSPROTECTION", "EXPLOSIONS_PROTECTION", "BOOMPROT", "BOOM_PROT", "BOOMPROTECTION", "BOOM_PROTECTION", "3"));
	ArrayList<String> prot4 = new ArrayList<String> (Arrays.asList("PROJECTILEPROTECTION", "PROJECTILE_PROTECTION", "PROJPROT", "PROJ_PROT", "PROJECTILEPROT", "PROJECTILE_PROT", "ARROWPROT", "ARROW_PROT", "ARROWPROTECTION", "ARROW_PROTECTION", "PROTECTION_PROJECTILE", "PROT_PROJECTILE", "PROTPROJECTILE", "PROT_PROJ", "PROTPROJ", "4"));
	ArrayList<String> prot5 = new ArrayList<String> (Arrays.asList("RESPIRATION", "RESPIRATION", "OXYGEN", "WATERBREATHE", "WATER_BREATHE", "WATERBREATHER", "WATER_BREATHER", "5"));
	ArrayList<String> prot6 = new ArrayList<String> (Arrays.asList("AQUAAFFINITY", "AQUA_AFFINITY", "WATERWORKER", "WATER_WORKER", "6"));
	ArrayList<String> prot7 = new ArrayList<String> (Arrays.asList("THORNS", "THORN", "PLANT", "RETALIATION", "7"));
	ArrayList<String> dmg16 = new ArrayList<String> (Arrays.asList("DAMAGE_ALL", "DAMAGEALL", "DAMAGE", "SHARPNESS", "SHARP", "16"));
	ArrayList<String> dmg17 = new ArrayList<String> (Arrays.asList("UNDEAD_DAMAGE", "UNDEADDAMAGE", "DAMAGE_UNDEAD", "DAMAGEUNDEAD", "SMITE", "17"));
	ArrayList<String> dmg18 = new ArrayList<String> (Arrays.asList("BANEOFARTHROPODS", "BANEOFARTHROPOD", "BANE_OF_ARTHROPOD", "ARTHROPODDAMAGE", "ARTHROPOD_DAMAGE", "BANE", "ARTHROPODS", "BANE_OF_ARTHROPODS", "ARTHROPODSDAMAGE", "ARTHROPODS_DAMAGE", "DAMAGE_ARTHROPODS", "DAMAGE_ARTHRPOD", "DAMAGEARTHROPODS", "DAMAGEARTHROPOD", "18"));
	ArrayList<String> dmg19 = new ArrayList<String> (Arrays.asList("KNOCKBACK", "KNOCK_BACK", "PUSH", "SLAP", "SLAM", "SMACK", "19"));
	ArrayList<String> dmg20 = new ArrayList<String> (Arrays.asList("FIREASPECT", "FIRE_ASPECT", "FIRE", "20"));
	ArrayList<String> dmg21 = new ArrayList<String> (Arrays.asList("LOOTING", "LOOT_BONUS_MOBS", "LOOTBONUSMOBS", "LOOTMOBS", "LOOT_MOBS", "21"));
	ArrayList<String> tool32 = new ArrayList<String> (Arrays.asList("EFFICIENCY", "DIGSPEED", "DIG_SPEED", "FASTBREAK", "FAST_BREAK", "32"));
	ArrayList<String> tool33 = new ArrayList<String> (Arrays.asList("SILKTOUCH", "SILK_TOUCH", "SILK", "SILKY", "33"));
	ArrayList<String> tool34 = new ArrayList<String> (Arrays.asList("DURABILITY", "UNBREAK", "UNBREAKING", "UNBREAKABLE", "34"));
	ArrayList<String> tool35 = new ArrayList<String> (Arrays.asList("FORTUNE", "LOOT_BONUS_BLOCKS", "LOOTBONUSBLOCKS", "LOOTBLOCKS", "LOOT_BLOCKS", "35"));
	ArrayList<String> bow48 = new ArrayList<String> (Arrays.asList("POWER", "ARROW_DAMAGE", "ARROWDAMAGE", "POWER_ARROW", "POWERARROW", "DAMAGE_ARROW", "DAMAGEARROW", "48"));
	ArrayList<String> bow49 = new ArrayList<String> (Arrays.asList("PUNCH", "ARROW_KNOCKBACK", "ARROWKNOCKBACK", "KNOCKBACK_ARROW", "KNOCKBACKARROW", "49"));
	ArrayList<String> bow50 = new ArrayList<String> (Arrays.asList("ARROW_FIRE", "ARROWFIRE", "FLAME", "FIREARROW", "FIRE_ARROW", "FLAME_ARROW", "FLAMEARROW", "50"));
	ArrayList<String> bow51 = new ArrayList<String> (Arrays.asList("INFINITY", "INFINITE", "ARROW_INFINITY", "ARROWINFINITY", "ARROW_INFINITE", "ARROWINFINITE", "51"));
	ArrayList<String> fishingRod61 = new ArrayList<String> (Arrays.asList("LUCK", "LUCKOFSEA", "SEALUCK", "LUCK_OF_SEA", "SEA_LUCK", "LUCKY", "61"));
	ArrayList<String> fishingRod62 = new ArrayList<String> (Arrays.asList("LURE", "BITING", "FISHLURE", "LUREFISH", "FISH_LURE", "LURE_FISH", "62"));

	public void initEnchants() {
		//Begin adding aliases and associated enchantments
		collectiveEnchants.put(all, allEnchantments());
		collectiveEnchants.put(armor, armorEnchantments());
		collectiveEnchants.put(sword, swordEnchantments());
		collectiveEnchants.put(tools, toolEnchantments());
		collectiveEnchants.put(bows, bowEnchantments());
		collectiveEnchants.put(fishingRods, fishingRodEnchantments());

		enchants.put(prot0, Enchantment.PROTECTION_ENVIRONMENTAL);
		enchants.put(prot1, Enchantment.PROTECTION_FIRE);
		enchants.put(prot2, Enchantment.PROTECTION_FALL);
		enchants.put(prot3, Enchantment.PROTECTION_EXPLOSIONS);
		enchants.put(prot4, Enchantment.PROTECTION_PROJECTILE);
		enchants.put(prot5, Enchantment.OXYGEN);
		enchants.put(prot6, Enchantment.WATER_WORKER);
		enchants.put(prot7, Enchantment.THORNS);

		enchants.put(dmg16, Enchantment.DAMAGE_ALL);
		enchants.put(dmg17, Enchantment.DAMAGE_UNDEAD);
		enchants.put(dmg18, Enchantment.DAMAGE_ARTHROPODS);
		enchants.put(dmg19, Enchantment.KNOCKBACK);
		enchants.put(dmg20, Enchantment.FIRE_ASPECT);
		enchants.put(dmg21, Enchantment.LOOT_BONUS_MOBS);

		enchants.put(tool32, Enchantment.DIG_SPEED);
		enchants.put(tool33, Enchantment.SILK_TOUCH);
		enchants.put(tool34, Enchantment.DURABILITY);
		enchants.put(tool35, Enchantment.LOOT_BONUS_BLOCKS);

		enchants.put(bow48, Enchantment.ARROW_DAMAGE);
		enchants.put(bow49, Enchantment.ARROW_KNOCKBACK);
		enchants.put(bow50, Enchantment.ARROW_FIRE);
		enchants.put(bow51, Enchantment.ARROW_INFINITE);

		enchants.put(fishingRod61, Enchantment.LUCK);
		enchants.put(fishingRod62, Enchantment.LURE);
		//End adding aliases and associated enchantments
	}

	private List<Enchantment> allEnchantments() {
		List<Enchantment> list = new ArrayList<Enchantment>();
		for(Enchantment e : Enchantment.values()) {
			list.add(e);
		}
		return list;
	}

	private List<Enchantment> armorEnchantments() {
		List<Enchantment> list = new ArrayList<Enchantment>();
		list.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		list.add(Enchantment.PROTECTION_FIRE);
		list.add(Enchantment.PROTECTION_FALL);
		list.add(Enchantment.PROTECTION_EXPLOSIONS);
		list.add(Enchantment.PROTECTION_PROJECTILE);
		list.add(Enchantment.OXYGEN);
		list.add(Enchantment.WATER_WORKER);
		list.add(Enchantment.THORNS);
		return list;
	}

	private List<Enchantment> swordEnchantments() {
		List<Enchantment> list = new ArrayList<Enchantment>();
		list.add(Enchantment.DAMAGE_ALL);
		list.add(Enchantment.DAMAGE_UNDEAD);
		list.add(Enchantment.DAMAGE_ARTHROPODS);
		list.add(Enchantment.FIRE_ASPECT);
		list.add(Enchantment.KNOCKBACK);
		list.add(Enchantment.LOOT_BONUS_MOBS);
		return list;
	}

	private List<Enchantment> toolEnchantments() {
		List<Enchantment> list = new ArrayList<Enchantment>();
		list.add(Enchantment.DIG_SPEED);
		list.add(Enchantment.SILK_TOUCH);
		list.add(Enchantment.DURABILITY);
		list.add(Enchantment.LOOT_BONUS_BLOCKS);
		return list;
	}

	private List<Enchantment> bowEnchantments() {
		List<Enchantment> list = new ArrayList<Enchantment>();
		list.add(Enchantment.ARROW_DAMAGE);
		list.add(Enchantment.ARROW_INFINITE);
		list.add(Enchantment.ARROW_FIRE);
		list.add(Enchantment.ARROW_KNOCKBACK);
		return list;
	}

	private List<Enchantment> fishingRodEnchantments() {
		List<Enchantment> list = new ArrayList<Enchantment>();
		list.add(Enchantment.LUCK);
		list.add(Enchantment.LURE);
		return list;
	}

	private Object getField(Object obj, String name) {
		try 
		{
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} 
		catch (Exception e) 
		{
			// We don't care
			throw new RuntimeException("Unable to retrieve field content.", e);
		}
	}

	private void addGlow(org.bukkit.inventory.ItemStack stack, Player player) 
	{
		if(!(player.hasPermission("EndlessEnchant.Enchant"))) player.sendMessage(EEPrefix + "\u00A74You do not have access to that command.");
		if(stack == null || stack.getType() == Material.AIR) player.sendMessage(EEPrefix + "\u00A74You cannot add glow to a nonexistant object!");
		else
		{
			player.sendMessage(EEPrefix + "\u00A7cGlow \u00A76has been applied to your item in hand.");
			ItemStack nmsStack = (ItemStack) getField(stack, "handle");
			NBTTagCompound compound = nmsStack.tag;

			// Initialize the compound if we need to
			if (compound == null) 
			{
				compound = new NBTTagCompound();
				nmsStack.tag = compound;
			}

			// Empty enchanting compound
			compound.set("ench", new NBTTagList());
			nmsStack.tag = compound;
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.getServer().getConsoleSender().sendMessage("\u00A7cYou are unable to use this command.");
			return true;
		}
		Player player = (Player) sender;
		if(commandLabel.equalsIgnoreCase("EndlessEnchant") || commandLabel.equalsIgnoreCase("EE") || commandLabel.equalsIgnoreCase("Enchant")) {
			String PartA = EEPrefix + "\u00A76The enchantment\u00A7c";
			String PartB = "\u00A76has been applied to your item in hand.";
			String PartAP = EEPrefix + "\u00A76The enchantments\u00A7c";
			String PartBP = "\u00A76have been applied to your item in hand.";
			String PartR = "\u00A76has been removed from your item in hand.";
			String PartRP = "\u00A76have been removed from your item in hand.";
			org.bukkit.inventory.ItemStack H = player.getInventory().getItemInHand();
			String InvalidLevel = EEPrefix + "\u00A74A level \u00A76(0-32767)\u00A74 is required to enchant an item.";
			String NoPermission = EEPrefix + "\u00A74You do not have access to that command.";
			String TooManyArguments = EEPrefix + "\u00A74Too many arguments.";
			String InvalidEnchant = EEPrefix + "\u00A74That is not a valid enchantment name.";
			String InvalidCommand = EEPrefix + "\u00A74That is not a valid command.";

			if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("Help"))) {
				if(player.hasPermission("EndlessEnchant.Help"))
				{
					player.sendMessage("\u00A78==================" + EEPrefix.replaceAll(" ", "") + "\u00A78==================");
					player.sendMessage("\u00A74To see the usage of /EE, type \u00A76/EE Usage\u00A74.");
					player.sendMessage("\u00A74To see a list of Enchantments \u00A7c(Non-Aliased)\u00A74, type \u00A7b/EE Enchantments\u00A74.");
					player.sendMessage("\u00A74To get the aliases of a certain enchantment, type \u00A72/EE Alias <Enchantment>\u00A74.");
					player.sendMessage("\u00A74To see a list of Enchantment Aliases, type \u00A7a/EE Aliases\u00A74.");
					player.sendMessage("\u00A74To see a list of Enchantment Kits, type \u00A7c/EE Kits\u00A74.");
					player.sendMessage("\u00A78=====================================================");
				}
				else player.sendMessage(NoPermission);
			}
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("Add")) {
					if(player.hasPermission("EndlessEnchant.Enchant")) player.sendMessage(EEPrefix + "\u00A74An enchantment name and a level is required to enchant an item.");
					else player.sendMessage(NoPermission);
				}
				else if(args[0].equalsIgnoreCase("Usage") || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("/"))
				{
					if(player.hasPermission("EndlessEnchant.Usage")) player.sendMessage(EEPrefix + "\u00A74Usage: \u00A76/EE \u00A73[Add/Remove] \u00A7a[Enchantment] \u00A72{Level} (not needed if removing)\u00A76.");
					else player.sendMessage(NoPermission);
				}
				else if(args[0].equalsIgnoreCase("Glow"))
				{
					CraftItemStack H2 = CraftItemStack.asCraftCopy(H);
					addGlow(H2, player);
					player.setItemInHand(H2);
				}
				else if(args[0].equalsIgnoreCase("Enchants") || args[0].equalsIgnoreCase("Enchant") || args[0].equalsIgnoreCase("Enchantment") || args[0].equalsIgnoreCase("Enchantments") || args[0].equalsIgnoreCase("List"))
				{
					if(player.hasPermission("EndlessEnchant.Enchantments"))
					{
						player.sendMessage(EEPrefix + "\u00A7bPROTECTION_ENVIRONMENTAL, PROTECTION_FIRE, PROTECTION_FALL, PROTECTION_EXPLOSIONS, PROTECTION_PROJECTILE, OXYGEN, WATER_WORKER, THORNS, " +
								"\u00A7cDAMAGE_ALL, DAMAGE_UNDEAD, DAMAGE_ARTHROPODS, KNOCKBACK, FIRE_ASPECT, LOOT_BONUS_MOBS, " +
								"\u00A75DIG_SPEED, SILK_TOUCH, DURABILITY, LOOT_BONUS_BLOCKS, " +
								"\u00A77ARROW_DAMAGE, ARROW_KNOCKBACK, ARROW_FIRE, ARROW_INFINITE," + 
								"\u00A79LUCK_OF_SEA, LURE" + 
								"\u00A7b.");
					}
					else player.sendMessage(NoPermission);
				}
				else if(args[0].equalsIgnoreCase("Aliases"))
				{
					if(player.hasPermission("EndlessEnchant.Aliases"))
					{
						player.sendMessage(EEPrefix + ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "Enchantment Aliases" + ChatColor.DARK_RED + ":");
						player.sendMessage(ChatColor.AQUA + "PROTECTION_ENVIRONMENTAL: Protection, Prot, ProtectionAll, Protection_All, ProtAll, Prot_All, Prot_Environmental, 0.");
						player.sendMessage(ChatColor.GREEN + "PROTECTION_FIRE: FireProtection, Fire_Protection, FireProt, Fire_Prot, Prot_Fire, FlameProtection, Flame_Protection, FlameProt, Flame_Prot, 1.");
						player.sendMessage(ChatColor.AQUA + "PROTECTION_FALL: FeatherFalling, Feather_Falling, Prot_Fall, Feather, Feathers, NoFall, No_Fall, 2.");
						player.sendMessage(ChatColor.GREEN + "PROTECTION_EXPLOSIONS: BlastProtection, Blast_Protection, BlastProt, Blast_Prot, Prot_Explosions, ExplosionsProtection, Explosions_Protection, ExplosionsProt, Explosions_Prot, BoomProtection, Boom_Protection, BoomProt, Boom_Prot, 3.");
						player.sendMessage(ChatColor.AQUA + "PROTECTION_PROJECTILE: ProjectileProtection, Projectile_Protection, ProjectileProt, Projectile_Prot, ProjProt, Proj_Prot, ArrowProtection, Arrow_Protection, ArrowProt, Arrow_Prot, ProtectionProj, Protection_Proj, ProtProjectile, Prot_Projectile, ProtProj, Prot_Proj, 4.");
						player.sendMessage(ChatColor.GREEN + "OXYGEN: Respiration, WaterBreathe, Water_Breathe, WaterBreather, Water_Breather, 5.");
						player.sendMessage(ChatColor.AQUA + "WATER_WORKER: AquaAffinity, Aqua_Affinity, WaterWorker, 6.");
						player.sendMessage(ChatColor.GREEN + "THORNS: Thorn, Plant, Retaliation, 7.");
						player.sendMessage(ChatColor.RED + "DAMAGE_ALL: Damage, DamageAll, Sharpness, Sharp, 16.");
						player.sendMessage(ChatColor.GOLD + "DAMAGE_UNDEAD: Smite, UndeadDamage, Undead_Damage, DamageUndead, 17.");
						player.sendMessage(ChatColor.RED + "DAMAGE_ARTHROPODS: BaneOfArthropods, Bane_Of_Arthropods, BaneOfArthropod, Bane_Of_Arthropod, ArthropodsDamage, Arthropods_Damage, ArthropodDamage, Arthropod_Damage, Bane, Arthropods, DamageArthropods, DamageArthropod, Damage_Arthropod, 18.");
						player.sendMessage(ChatColor.GOLD + "KNOCKBACK: Knockback, Knock_Back, Push, Slap, Slam, Smack, 19.");
						player.sendMessage(ChatColor.RED + "FIRE_ASPECT: FireAspect, Fire_Aspect, Fire, 20.");
						player.sendMessage(ChatColor.GOLD + "LOOT_BONUS_MOBS: Looting, LootBonusMobs, LootMobs, Loot_Mobs, 21.");
						player.sendMessage(ChatColor.DARK_PURPLE + "DIG_SPEED: Efficiency, DigSpeed, FastBreak, Fast_Break, 32.");
						player.sendMessage(ChatColor.LIGHT_PURPLE + "SILK_TOUCH: SilkTouch, Silk, Silky, 33.");
						player.sendMessage(ChatColor.DARK_PURPLE + "DURABILITY: Durability, Unbreak, Unbreaking, Unbreakable, 34.");
						player.sendMessage(ChatColor.LIGHT_PURPLE + "LOOT_BONUS_BLOCKS: Fortune, LootBonusBlocks, LootBlocks, Loot_Blocks, 35.");
						player.sendMessage(ChatColor.GRAY + "ARROW_DAMAGE: Power, PowerArrow, Power_Arrow, ArrowDamage, DamageArrow, Damage_Arrow, 48.");
						player.sendMessage(ChatColor.DARK_GRAY + "ARROW_KNOCKBACK: Punch, ArrowKnockback, KnockbackArrow, Knockback_Arrow, 49.");
						player.sendMessage(ChatColor.GRAY + "ARROW_FIRE: Flame, FireArrow, Fire_Arrow, FlameArrow, Flame_Arrow, 50.");
						player.sendMessage(ChatColor.DARK_GRAY + "ARROW_INFINITY: Infinity, Infinite, Arrow_Infinity, ArrowInfinity, 51.");
						player.sendMessage(ChatColor.BLUE + "LUCK_OF_THE_SEA: Luck, LuckOfSea, SeaLuck, Luck_Of_Sea, Sea_Luck, Lucky, 61.");
						player.sendMessage(ChatColor.DARK_BLUE + "LURE: Lure, Biting, FishLure, LureFish, Fish_Lure, Lure_Fish, 62.");
					}
					else player.sendMessage(NoPermission);
				}
				else if(isValidEnchantmentName(args[0])) {
					if(player.hasPermission("EndlessEnchant.Enchant")) {
						if(player.hasPermission("EndlessEnchant.Endless")) player.sendMessage(InvalidLevel);
						else player.sendMessage(InvalidLevel.replace("0-32767", "0-5"));
					}
					else player.sendMessage(NoPermission);
				}
			}
			else if(args.length == 2)
			{

				return true;
			}
			else if(args.length == 3)
			{

				return true;
			}
			else if(args.length > 3)
			{
				player.sendMessage(TooManyArguments);
				return true;
			}
		}
		return false;
	}

	public boolean onasCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(commandLabel.equalsIgnoreCase("EndlessEnchant") || commandLabel.equalsIgnoreCase("EE") || commandLabel.equalsIgnoreCase("Enchant"))
			{
				if(args.length == 0)
				{

				}
				else if(args.length == 1)
				{
					//Enchantment Shortcuts
					
					else if(args[0].equalsIgnoreCase("Kits"))
					{
						if(player.hasPermission("EndlessEnchant.Kits.List") || player.hasPermission("EndlessEnchant.Kits.*") || StarPerm)
						{
							player.sendMessage(ChatColor.DARK_GRAY + "==================" + EEPrefix.replaceAll(" ", "") + ChatColor.DARK_GRAY + "==================");
							player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "All" + ChatColor.GOLD + " - All of the Kits and enchantments.");
							player.sendMessage(ChatColor.AQUA + "Armor - Protection_Environmental, Protection_Fire, Protection_Fall, Protection_Explosions, Protection_Projectile, Oxygen, Water_Worker, Thorns.");
							player.sendMessage(ChatColor.RED + "Swords - Damage_All, Damage_Undead, Damage_Arthropods, Knockback, Fire_Aspect, Loot_Bonus_Mobs.");
							player.sendMessage(ChatColor.DARK_PURPLE + "Tools - Dig_Speed, Silk_Touch, Durability, Loot_Bonus_Blocks.");
							player.sendMessage(ChatColor.GRAY + "Bows - Arrow_Damage, Arrow_Knockback, Arrow_Fire, Arrow_Infinity.");
							player.sendMessage(ChatColor.DARK_RED + "Keep in mind that these kits are enchantment names!");
							player.sendMessage(ChatColor.DARK_GRAY + "=====================================================");
						}
						else player.sendMessage(NoPermission);
					}
					else if(args[0].equalsIgnoreCase("Alias"))
					{
						if(player.hasPermission("EndlessEnchant.Aliases")) player.sendMessage(EEPrefix + "\u00A74An enchantment name to get the alias of is required.");
						else player.sendMessage(NoPermission);
					}
					else if(args[0].equalsIgnoreCase("Remove"))
					{
						if(player.hasPermission("EndlessEnchant.Disenchant") || player.hasPermission("EndlessEnchant.Enchant.*") || StarPerm)
						{
							player.sendMessage(EEPrefix + ChatColor.DARK_RED + "An enchantment name to remove is required as the second argument.");
						}
						else player.sendMessage(NoPermission);
					}
					else {
						player.sendMessage(InvalidCommand);
						return false;
					}
				}
				else if(args.length == 2)
				{
					if(args[0].equalsIgnoreCase("Alias"))
					{
						if(player.hasPermission("EndlessEnchant.Aliases") || StarPerm)
						{
							args[1] = args[1].toUpperCase();
							if(getAliases(ChatColor.AQUA, args[1]) != null) {
								player.sendMessage(getAliases(ChatColor.AQUA, args[1]));
								return true;
							}
							player.sendMessage(EEPrefix + ChatColor.RED + "That enchantment name is not valid.");
							return false;
						}
						else player.sendMessage(NoPermission);
					}
					else if(args[0].equalsIgnoreCase("Add"))
					{
						args[1] = args[1].toUpperCase();
						if(isValidEnchantmentName(args[1]))
						{
							if(player.hasPermission("EndlessEnchant.Endless")) player.sendMessage(InvalidLevel);
							else if(player.hasPermission("EndlessEnchant.Enchant") && !(player.hasPermission("EndlessEnchant.Endless")))
							{
								player.sendMessage(InvalidLevel.replaceAll("0-32767", "0-5"));
							}
							else player.sendMessage(NoPermission);
						}
						else if(args[1].equalsIgnoreCase("Glow"))
						{
							addGlow(H, player);
							return true;
						}
						else player.sendMessage(InvalidEnchant);
						return true;
					}
					else if(args[0].equalsIgnoreCase("Remove"))
					{
						if(player.hasPermission("EndlessEnchant.Disenchant") || player.hasPermission("EndlessEnchant.Enchant.*") || StarPerm)
						{
							if(args[1].equalsIgnoreCase("Glow"))
							{
								if(H == null || H.getType() == Material.AIR)
								{
									player.sendMessage(EEPrefix + ChatColor.DARK_RED + "You cannot remove glow from a nonexistant object!");
								}
								else
								{
									ItemStack nmsStack = (ItemStack) getField(H, "handle");
									NBTTagCompound compound = nmsStack.tag;

									// Initialize the compound if we need to
									if (compound == null) 
									{
										compound = new NBTTagCompound();
										nmsStack.tag = compound;
									}

									// Empty enchanting compound
									compound.remove("ench");

									player.sendMessage(EEPrefix + ChatColor.RED + "Glow " + ChatColor.GOLD + "has been removed from your item in hand.");
									return true;
								}
							}
							else
							{
								if(H.getType() == Material.AIR || H == null)
								{
									player.sendMessage(EEPrefix + ChatColor.DARK_RED + "You cannot remove enchantments from a nonexistant object!");
									return true;
								}
								else
								{
									if(args[1].equalsIgnoreCase("All"))
									{
										for(Enchantment e : enchants.values()) {
											removeAnEnchantment(H, e);
										}
										/*removeAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL);
										removeAnEnchantment(H, Enchantment.PROTECTION_FIRE);
										removeAnEnchantment(H, Enchantment.PROTECTION_FALL);
										removeAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS);
										removeAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE);
										removeAnEnchantment(H, Enchantment.OXYGEN);
										removeAnEnchantment(H, Enchantment.WATER_WORKER);
										removeAnEnchantment(H, Enchantment.THORNS);
										removeAnEnchantment(H, Enchantment.DAMAGE_ALL);
										removeAnEnchantment(H, Enchantment.DAMAGE_UNDEAD);
										removeAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS);
										removeAnEnchantment(H, Enchantment.KNOCKBACK);
										removeAnEnchantment(H, Enchantment.FIRE_ASPECT);
										removeAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS);
										removeAnEnchantment(H, Enchantment.DIG_SPEED);
										removeAnEnchantment(H, Enchantment.SILK_TOUCH);
										removeAnEnchantment(H, Enchantment.DURABILITY);
										removeAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS);
										removeAnEnchantment(H, Enchantment.ARROW_DAMAGE);
										removeAnEnchantment(H, Enchantment.ARROW_KNOCKBACK);
										removeAnEnchantment(H, Enchantment.ARROW_FIRE);
										removeAnEnchantment(H, Enchantment.ARROW_INFINITE);*/
										String msg = PartAP + " ";
										Iterator<ArrayList<String>> it = enchants.keySet().iterator();
										while(it.hasNext()) {
											msg += list("", it.next());
											if(!it.hasNext()) {
												msg += list("and", it.next());
												break;
											}
										}
										msg += PartRP;
										player.sendMessage(msg);
										//player.sendMessage(PartAP + " protection environment, protection fire, protection fall, protection explosions, protection projectile, oxygen, water worker, thorns, damage all, damage undead, damage arthropods, knockback, fire aspect, loot bonus mobs, dig speed, silk touch, durability, loot bonus blocks, arrow damage, arrow knockback, arrow fire, and arrow infinite " + PartRP);
									}
									else if(args[1].equalsIgnoreCase("Armor") || args[1].equalsIgnoreCase("Armour"))
									{
										removeAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL);
										removeAnEnchantment(H, Enchantment.PROTECTION_FIRE);
										removeAnEnchantment(H, Enchantment.PROTECTION_FALL);
										removeAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS);
										removeAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE);
										removeAnEnchantment(H, Enchantment.OXYGEN);
										removeAnEnchantment(H, Enchantment.WATER_WORKER);
										removeAnEnchantment(H, Enchantment.THORNS);
										player.sendMessage(PartAP + " protection environmental, protection fire, protection fall, protection explosions, protection projectile, oxygen, water worker, and thorns " + PartRP);
									}
									else if(args[1].equalsIgnoreCase("Tools") || args[1].equalsIgnoreCase("Tool"))
									{
										removeAnEnchantment(H, Enchantment.DIG_SPEED);
										removeAnEnchantment(H, Enchantment.SILK_TOUCH);
										removeAnEnchantment(H, Enchantment.DURABILITY);
										removeAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS);
										player.sendMessage(PartAP + " dig speed, silk touch, durability, and loots bonus blocks " + PartRP);
									}
									else if(args[1].equalsIgnoreCase("Swords") || args[1].equalsIgnoreCase("Sword"))
									{
										removeAnEnchantment(H, Enchantment.DAMAGE_ALL);
										removeAnEnchantment(H, Enchantment.DAMAGE_UNDEAD);
										removeAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS);
										removeAnEnchantment(H, Enchantment.KNOCKBACK);
										removeAnEnchantment(H, Enchantment.FIRE_ASPECT);
										removeAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS);
										player.sendMessage(PartAP + " damage all, damage undead, damage arthropods, knockback, fire aspect, and loot bonus mobs " + PartRP);
									}
									else if(args[1].equalsIgnoreCase("Bows") || args[1].equalsIgnoreCase("Bow"))
									{
										removeAnEnchantment(H, Enchantment.ARROW_DAMAGE);
										removeAnEnchantment(H, Enchantment.ARROW_KNOCKBACK);
										removeAnEnchantment(H, Enchantment.ARROW_FIRE);
										removeAnEnchantment(H, Enchantment.ARROW_INFINITE);
										player.sendMessage(PartAP + " arrow damage, arrow knockback, arrow fire, and arrow infinite " + PartRP);
									}
									else if(prot0.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL);
										player.sendMessage(PartA + " protection environmental " + PartR);
									}
									else if(prot1.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.PROTECTION_FIRE);
										player.sendMessage(PartA + " protection fire " + PartR);
									}
									else if(prot2.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.PROTECTION_FALL);
										player.sendMessage(PartA + " protection fall " + PartR);
									}
									else if(prot3.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS);
										player.sendMessage(PartA + " protection explosions " + PartR);
									}
									else if(prot4.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE);
										player.sendMessage(PartA + " protection projectile " + PartR);
									}
									else if(prot5.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.OXYGEN);
										player.sendMessage(PartA + " oxygen " + PartR);
									}
									else if(prot6.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.WATER_WORKER);
										player.sendMessage(PartA + " water worker " + PartR);
									}
									else if(prot7.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.THORNS);
										player.sendMessage(PartA + " thorns " + PartR);
									}
									else if(dmg16.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.DAMAGE_ALL);
										player.sendMessage(PartA + " damage all " + PartR);
									}
									else if(dmg17.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.DAMAGE_UNDEAD);
										player.sendMessage(PartA + " damage undead " + PartR);
									}
									else if(dmg18.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS);
										player.sendMessage(PartA + " damage arthropods " + PartR);
									}
									else if(dmg19.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.KNOCKBACK);
										player.sendMessage(PartA + " knockback " + PartR);
									}
									else if(dmg20.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.FIRE_ASPECT);
										player.sendMessage(PartA + " fire aspect " + PartR);
									}
									else if(dmg21.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS);
										player.sendMessage(PartA + " loot bonus mobs " + PartR);
									}
									else if(tool32.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.DIG_SPEED);
										player.sendMessage(PartA + " dig speed " + PartR);
									}
									else if(tool33.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.SILK_TOUCH);
										player.sendMessage(PartA + " silk touch " + PartR);
									}
									else if(tool34.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.DURABILITY);
										player.sendMessage(PartA + " durability " + PartR);
									}
									else if(tool35.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS);
										player.sendMessage(PartA + " loot bonus blocks " + PartR);
									}
									else if(bow48.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.ARROW_DAMAGE);
										player.sendMessage(PartA + " arrow damage " + PartR);
									}
									else if(bow49.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.ARROW_KNOCKBACK);
										player.sendMessage(PartA + " arrow knockback " + PartR);
									}
									else if(bow50.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.ARROW_FIRE);
										player.sendMessage(PartA + " arrow fire " + PartR);
									}
									else if(bow51.contains(args[1]))
									{
										removeAnEnchantment(H, Enchantment.ARROW_INFINITE);
										player.sendMessage(PartA + " arrow infinite " + PartR);
									}
									else
									{
										player.sendMessage(InvalidEnchant);
									}
								}
							}
						}
						else
						{
							player.sendMessage(NoPermission);
						}
						return true;
					}
					int level;
					try
					{
						level = Integer.parseInt(args[1]);
					}
					catch (NumberFormatException e)
					{
						player.sendMessage(EEPrefix + ChatColor.DARK_RED + "A valid level with which to enchant the item is required!");
						return false;
					}
					if(level < 0)
					{
						if(player.isOp() || EPerm || StarPerm)
						{
							player.sendMessage(EEPrefix + ChatColor.DARK_RED + "The enchantment level must be 0 or more.");
						}
					}
					else if(level > 5 && level <= 32767)
					{
						if(EPerm && !(player.hasPermission("EndlessEnchant.Endless")) || !(StarPerm))
						{
							player.sendMessage(EEPrefix + ChatColor.DARK_RED + "You do not have access to Unsafe Enchanting (enchantments over level 5). Setting the enchantment level to 5.");
							level = 5;
						}
					}
					else if(level >= 32768)
					{
						if(player.isOp() || EPerm && player.hasPermission("EndlessEnchant.Endless") || StarPerm)
						{
							player.sendMessage(EEPrefix + ChatColor.DARK_RED + "32767 is the maximum enchantment level! Setting the enchantment level to 32767.");
							level = 32767;
						}
					}
					if(EPerm || StarPerm)
					{
						if(H.getType() == Material.AIR || H == null)
						{
							player.sendMessage(EEPrefix + ChatColor.DARK_RED + "You cannot add enchantments to a nonexistant object!");
						}
						else
						{
							args[0] = args[0].toUpperCase();
							if(args[0].equalsIgnoreCase("All"))
							{
								addAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL, level, player);
								addAnEnchantment(H, Enchantment.PROTECTION_FIRE, level, player);
								addAnEnchantment(H, Enchantment.PROTECTION_FALL, level, player);
								addAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS, level, player);
								addAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE, level, player);
								addAnEnchantment(H, Enchantment.OXYGEN, level, player);
								addAnEnchantment(H, Enchantment.WATER_WORKER, level, player);
								addAnEnchantment(H, Enchantment.THORNS, level, player);
								addAnEnchantment(H, Enchantment.DAMAGE_ALL, level, player);
								addAnEnchantment(H, Enchantment.DAMAGE_UNDEAD, level, player);
								addAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS, level, player);
								addAnEnchantment(H, Enchantment.KNOCKBACK, level, player);
								addAnEnchantment(H, Enchantment.FIRE_ASPECT, level, player);
								addAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS, level, player);
								addAnEnchantment(H, Enchantment.DIG_SPEED, level, player);
								addAnEnchantment(H, Enchantment.SILK_TOUCH, level, player);
								addAnEnchantment(H, Enchantment.DURABILITY, level, player);
								addAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS, level, player);
								addAnEnchantment(H, Enchantment.ARROW_DAMAGE, level, player);
								addAnEnchantment(H, Enchantment.ARROW_KNOCKBACK, level, player);
								addAnEnchantment(H, Enchantment.ARROW_FIRE, level, player);
								addAnEnchantment(H, Enchantment.ARROW_INFINITE, level, player);
								player.sendMessage(PartAP + " protection environment, protection fire, protection fall, protection explosions, protection projectile, oxygen, water worker, thorns, damage all, damage undead, damage arthropods, knockback, fire aspect, loot bonus mobs, dig speed, silk touch, durability, loot bonus blocks, arrow damage, arrow knockback, arrow fire, and arrow infinite " + PartBP);
							}
							else if(args[0].equalsIgnoreCase("Armor") || args[0].equalsIgnoreCase("Armour"))
							{
								addAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL, level, player);
								addAnEnchantment(H, Enchantment.PROTECTION_FIRE, level, player);
								addAnEnchantment(H, Enchantment.PROTECTION_FALL, level, player);
								addAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS, level, player);
								addAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE, level, player);
								addAnEnchantment(H, Enchantment.OXYGEN, level, player);
								addAnEnchantment(H, Enchantment.WATER_WORKER, level, player);
								addAnEnchantment(H, Enchantment.THORNS, level, player);
								player.sendMessage(PartAP + " protection environmental, protection fire, protection fall, protection explosions, protection projectile, oxygen, water worker, and thorns " + PartBP);
							}
							else if(args[0].equalsIgnoreCase("Tools") || args[0].equalsIgnoreCase("Tool"))
							{
								addAnEnchantment(H, Enchantment.DIG_SPEED, level, player);
								addAnEnchantment(H, Enchantment.SILK_TOUCH, level, player);
								addAnEnchantment(H, Enchantment.DURABILITY, level, player);
								addAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS, level, player);
								player.sendMessage(PartAP + " dig speed, silk touch, durability, and loots bonus blocks " + PartBP);
							}
							else if(args[0].equalsIgnoreCase("Swords") || args[0].equalsIgnoreCase("Sword"))
							{
								addAnEnchantment(H, Enchantment.DAMAGE_ALL, level, player);
								addAnEnchantment(H, Enchantment.DAMAGE_UNDEAD, level, player);
								addAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS, level, player);
								addAnEnchantment(H, Enchantment.KNOCKBACK, level, player);
								addAnEnchantment(H, Enchantment.FIRE_ASPECT, level, player);
								addAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS, level, player);
								player.sendMessage(PartAP + " damage all, damage undead, damage arthropods, knockback, fire aspect, and loot bonus mobs " + PartBP);
							}
							else if(args[0].equalsIgnoreCase("Bows") || args[0].equalsIgnoreCase("Bow"))
							{
								addAnEnchantment(H, Enchantment.ARROW_DAMAGE, level, player);
								addAnEnchantment(H, Enchantment.ARROW_KNOCKBACK, level, player);
								addAnEnchantment(H, Enchantment.ARROW_FIRE, level, player);
								addAnEnchantment(H, Enchantment.ARROW_INFINITE, level, player);
								player.sendMessage(PartAP + " arrow damage, arrow knockback, arrow fire, and arrow infinite " + PartBP);
							}
							else if(prot0.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL, level, player);
								player.sendMessage(PartA + " protection environmental " + PartB);
							}
							else if(prot1.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.PROTECTION_FIRE, level, player);
								player.sendMessage(PartA + " protection fire " + PartB);
							}
							else if(prot2.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.PROTECTION_FALL, level, player);
								player.sendMessage(PartA + " protection fall " + PartB);
							}
							else if(prot3.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS, level, player);
								player.sendMessage(PartA + " protection explosions " + PartB);
							}
							else if(prot4.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE, level, player);
								player.sendMessage(PartA + " protection projectile " + PartB);
							}
							else if(prot5.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.OXYGEN, level, player);
								player.sendMessage(PartA + " oxygen " + PartB);
							}
							else if(prot6.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.WATER_WORKER, level, player);
								player.sendMessage(PartA + " water worker " + PartB);
							}
							else if(prot7.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.THORNS, level, player);
								player.sendMessage(PartA + " thorns " + PartB);
							}
							else if(dmg16.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.DAMAGE_ALL, level, player);
								player.sendMessage(PartA + " damage all " + PartB);
							}
							else if(dmg17.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.DAMAGE_UNDEAD, level, player);
								player.sendMessage(PartA + " damage undead " + PartB);
							}
							else if(dmg18.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS, level, player);
								player.sendMessage(PartA + " damage arthropods " + PartB);
							}
							else if(dmg19.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.KNOCKBACK, level, player);
								player.sendMessage(PartA + " knockback " + PartB);
							}
							else if(dmg20.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.FIRE_ASPECT, level, player);
								player.sendMessage(PartA + " fire aspect " + PartB);
							}
							else if(dmg21.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS, level, player);
								player.sendMessage(PartA + " loot bonus mobs " + PartB);
							}
							else if(tool32.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.DIG_SPEED, level, player);
								player.sendMessage(PartA + " dig speed " + PartB);
							}
							else if(tool33.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.SILK_TOUCH, level, player);
								player.sendMessage(PartA + " silk touch " + PartB);
							}
							else if(tool34.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.DURABILITY, level, player);
								player.sendMessage(PartA + " durability " + PartB);
							}
							else if(tool35.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS, level, player);
								player.sendMessage(PartA + " loot bonus blocks " + PartB);
							}
							else if(bow48.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.ARROW_DAMAGE, level, player);
								player.sendMessage(PartA + " arrow damage " + PartB);
							}
							else if(bow49.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.ARROW_KNOCKBACK, level, player);
								player.sendMessage(PartA + " arrow knockback " + PartB);
							}
							else if(bow50.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.ARROW_FIRE, level, player);
								player.sendMessage(PartA + " arrow fire " + PartB);
							}
							else if(bow51.contains(args[0]))
							{
								addAnEnchantment(H, Enchantment.ARROW_INFINITE, level, player);
								player.sendMessage(PartA + " arrow infinite " + PartB);
							}
							else
							{
								player.sendMessage(InvalidEnchant);
							}
							return true;
						}
					}
					else if(player.hasPermission("EndlessEnchant.Kits.All") 
							|| player.hasPermission("EndlessEnchant.Kits.Armor")
							|| player.hasPermission("EndlessEnchant.Kits.Tools")
							|| player.hasPermission("EndlessEnchant.Kits.Swords")
							|| player.hasPermission("EndlessEnchant.Kits.Bows")
							|| player.hasPermission("EndlessEnchant.Kits.*"))
					{
						if(H.getType() == Material.AIR || H == null)
						{
							player.sendMessage(EEPrefix + ChatColor.DARK_RED + "You cannot add enchantments to a nonexistant object!");
						}
						else
						{
							if(args[0].equalsIgnoreCase("All"))
							{
								args[0] = args[0].toUpperCase();
								if(player.hasPermission("EndlessEnchant.Kits.All") || player.hasPermission("EndlessEnchant.Kits.*"))
								{
									addAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL, level, player);
									addAnEnchantment(H, Enchantment.PROTECTION_FIRE, level, player);
									addAnEnchantment(H, Enchantment.PROTECTION_FALL, level, player);
									addAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS, level, player);
									addAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE, level, player);
									addAnEnchantment(H, Enchantment.OXYGEN, level, player);
									addAnEnchantment(H, Enchantment.WATER_WORKER, level, player);
									addAnEnchantment(H, Enchantment.THORNS, level, player);
									addAnEnchantment(H, Enchantment.DAMAGE_ALL, level, player);
									addAnEnchantment(H, Enchantment.DAMAGE_UNDEAD, level, player);
									addAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS, level, player);
									addAnEnchantment(H, Enchantment.KNOCKBACK, level, player);
									addAnEnchantment(H, Enchantment.FIRE_ASPECT, level, player);
									addAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS, level, player);
									addAnEnchantment(H, Enchantment.DIG_SPEED, level, player);
									addAnEnchantment(H, Enchantment.SILK_TOUCH, level, player);
									addAnEnchantment(H, Enchantment.DURABILITY, level, player);
									addAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS, level, player);
									addAnEnchantment(H, Enchantment.ARROW_DAMAGE, level, player);
									addAnEnchantment(H, Enchantment.ARROW_KNOCKBACK, level, player);
									addAnEnchantment(H, Enchantment.ARROW_FIRE, level, player);
									addAnEnchantment(H, Enchantment.ARROW_INFINITE, level, player);
									player.sendMessage(PartAP + " protection environment, protection fire, protection fall, protection explosions, protection projectile, oxygen, water worker, thorns, damage all, damage undead, damage arthropods, knockback, fire aspect, loot bonus mobs, dig speed, silk touch, durability, loot bonus blocks, arrow damage, arrow knockback, arrow fire, and arrow infinite " + PartBP);
								}
							}
							else if(args[0].equalsIgnoreCase("Armor") || args[0].equalsIgnoreCase("Armour"))
							{
								if(player.hasPermission("EndlessEnchant.Kits.Armor") || player.hasPermission("EndlessEnchant.Kits.*"))
								{
									addAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL, level, player);
									addAnEnchantment(H, Enchantment.PROTECTION_FIRE, level, player);
									addAnEnchantment(H, Enchantment.PROTECTION_FALL, level, player);
									addAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS, level, player);
									addAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE, level, player);
									addAnEnchantment(H, Enchantment.OXYGEN, level, player);
									addAnEnchantment(H, Enchantment.WATER_WORKER, level, player);
									addAnEnchantment(H, Enchantment.THORNS, level, player);
									player.sendMessage(PartAP + " protection environmental, protection fire, protection fall, protection explosions, protection projectile, oxygen, water worker, and thorns " + PartBP);
								}
							}
							else if(args[0].equalsIgnoreCase("Tools") || args[0].equalsIgnoreCase("Tool"))
							{
								if(player.hasPermission("EndlessEnchant.Kits.Tools") || player.hasPermission("EndlessEnchant.Kits.*"))
								{
									addAnEnchantment(H, Enchantment.DIG_SPEED, level, player);
									addAnEnchantment(H, Enchantment.SILK_TOUCH, level, player);
									addAnEnchantment(H, Enchantment.DURABILITY, level, player);
									addAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS, level, player);
									player.sendMessage(PartAP + " dig speed, silk touch, durability, and loots bonus blocks " + PartBP);
								}
							}
							else if(args[0].equalsIgnoreCase("Swords") || args[0].equalsIgnoreCase("Sword"))
							{
								if(player.hasPermission("EndlessEnchant.Kits.Swords") || player.hasPermission("EndlessEnchant.Kits.*"))
								{
									addAnEnchantment(H, Enchantment.DAMAGE_ALL, level, player);
									addAnEnchantment(H, Enchantment.DAMAGE_UNDEAD, level, player);
									addAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS, level, player);
									addAnEnchantment(H, Enchantment.KNOCKBACK, level, player);
									addAnEnchantment(H, Enchantment.FIRE_ASPECT, level, player);
									addAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS, level, player);
									player.sendMessage(PartAP + " damage all, damage undead, damage arthropods, knockback, fire aspect, and loot bonus mobs " + PartBP);
								}
							}
							else if(args[0].equalsIgnoreCase("Bows") || args[0].equalsIgnoreCase("Bow"))
							{
								if(player.hasPermission("EndlessEnchant.Kits.Bows") || player.hasPermission("EndlessEnchant.Kits.*"))
								{
									addAnEnchantment(H, Enchantment.ARROW_DAMAGE, level, player);
									addAnEnchantment(H, Enchantment.ARROW_KNOCKBACK, level, player);
									addAnEnchantment(H, Enchantment.ARROW_FIRE, level, player);
									addAnEnchantment(H, Enchantment.ARROW_INFINITE, level, player);
									player.sendMessage(PartAP + " arrow damage, arrow knockback, arrow fire, and arrow infinite " + PartBP);
								}
							}
							else if(!(prot0.contains(args[0]) || prot1.contains(args[0]) || prot2.contains(args[0]) || prot3.contains(args[0]) || prot4.contains(args[0]) || prot5.contains(args[0]) || prot6.contains(args[0]) || prot7.contains(args[0]) 
									|| dmg16.contains(args[0]) || dmg17.contains(args[0]) || dmg18.contains(args[0]) || dmg19.contains(args[0]) || dmg20.contains(args[0]) || dmg21.contains(args[0]) 
									|| tool32.contains(args[0]) || tool33.contains(args[0]) || tool34.contains(args[0]) || tool35.contains(args[0]) 
									|| bow48.contains(args[0]) || bow49.contains(args[0]) || bow50.contains(args[0]) || bow51.contains(args[0])))
							{
								player.sendMessage(InvalidEnchant);
							}
						}
					}
					else
					{
						player.sendMessage(NoPermission);
					}
				}
				else if(args.length == 3)
				{
					if(args[0].equalsIgnoreCase("Remove"))
					{
						if(player.hasPermission("EndlessEnchant.Disenchant") || player.hasPermission("EndlessEnchant.Enchant.*") || StarPerm)
						{
							player.sendMessage(TooManyArguments);
							return true;
						}
						else
						{
							player.sendMessage(NoPermission);
						}
						return true;
					}
					else if(args[0].equalsIgnoreCase("Glow"))
					{
						player.sendMessage(TooManyArguments);
						return true;
					}
					else if(!args[0].equalsIgnoreCase("Add") && !args[0].equalsIgnoreCase("Remove") && !args[0].equalsIgnoreCase("Glow"))
					{
						if(player.hasPermission("EndlessEnchant.Aliases") || EPerm || StarPerm || player.hasPermission("EndlessEnchant.Kits.*"))
						{
							player.sendMessage(TooManyArguments);
							return true;
						}
						else
						{
							player.sendMessage(NoPermission);
						}
						return true;
					}
					else
					{
						int level;
						try
						{
							level = Integer.parseInt(args[2]);
						}
						catch(NumberFormatException ex)
						{
							player.sendMessage(EEPrefix + ChatColor.DARK_RED + "A valid level with which to enchant the item is required!");
							return false;
						}
						if(level < 0)
						{
							if(player.isOp() || EPerm || StarPerm)
							{
								player.sendMessage(EEPrefix + ChatColor.DARK_RED + "The enchantment level must be 0 or more.");
							}
						}
						else if(level > 5 && level <= 32767)
						{
							if(EPerm && !(player.hasPermission("EndlessEnchant.Endless")) || !(StarPerm))
							{
								player.sendMessage(EEPrefix + ChatColor.DARK_RED + "You do not have access to Unsafe Enchanting (enchantments over level 5). Setting the enchantment level to 5.");
								level = 5;
							}
						}
						else if(level >= 32768)
						{
							if(player.isOp() || EPerm && player.hasPermission("EndlessEnchant.Endless") || StarPerm)
							{
								player.sendMessage(EEPrefix + ChatColor.DARK_RED + "32767 is the maximum enchantment level! Setting the enchantment level to 32767.");
								level = 32767;
							}
						}
						if(args[0].equalsIgnoreCase("Add"))
						{
							if(H.getType() == Material.AIR || H == null)
							{
								player.sendMessage(EEPrefix + ChatColor.DARK_RED + "You cannot add enchantments to a nonexistant object!");
							}
							else
							{
								if(args[1].equalsIgnoreCase("All"))
								{
									args[0] = args[0].toUpperCase();
									if(player.hasPermission("EndlessEnchant.Enchant.*") || player.hasPermission("EndlessEnchant.Kits.*") || StarPerm)
									{
										addAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL, level, player);
										addAnEnchantment(H, Enchantment.PROTECTION_FIRE, level, player);
										addAnEnchantment(H, Enchantment.PROTECTION_FALL, level, player);
										addAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS, level, player);
										addAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE, level, player);
										addAnEnchantment(H, Enchantment.OXYGEN, level, player);
										addAnEnchantment(H, Enchantment.WATER_WORKER, level, player);
										addAnEnchantment(H, Enchantment.THORNS, level, player);
										addAnEnchantment(H, Enchantment.DAMAGE_ALL, level, player);
										addAnEnchantment(H, Enchantment.DAMAGE_UNDEAD, level, player);
										addAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS, level, player);
										addAnEnchantment(H, Enchantment.KNOCKBACK, level, player);
										addAnEnchantment(H, Enchantment.FIRE_ASPECT, level, player);
										addAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS, level, player);
										addAnEnchantment(H, Enchantment.DIG_SPEED, level, player);
										addAnEnchantment(H, Enchantment.SILK_TOUCH, level, player);
										addAnEnchantment(H, Enchantment.DURABILITY, level, player);
										addAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS, level, player);
										addAnEnchantment(H, Enchantment.ARROW_DAMAGE, level, player);
										addAnEnchantment(H, Enchantment.ARROW_KNOCKBACK, level, player);
										addAnEnchantment(H, Enchantment.ARROW_FIRE, level, player);
										addAnEnchantment(H, Enchantment.ARROW_INFINITE, level, player);
										player.sendMessage(PartAP + " protection environment, protection fire, protection fall, protection explosions, protection projectile, oxygen, water worker, thorns, damage all, damage undead, damage arthropods, knockback, fire aspect, loot bonus mobs, dig speed, silk touch, durability, loot bonus blocks, arrow damage, arrow knockback, arrow fire, and arrow infinite " + PartBP);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(args[1].equalsIgnoreCase("Armor") || args[1].equalsIgnoreCase("Armour"))
								{
									if(player.hasPermission("EndlessEnchant.Kits.Armor") || player.hasPermission("EndlessEnchant.Kits.*") || player.hasPermission("EndlessEnchant.Enchant.*") || StarPerm)
									{
										addAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL, level, player);
										addAnEnchantment(H, Enchantment.PROTECTION_FIRE, level, player);
										addAnEnchantment(H, Enchantment.PROTECTION_FALL, level, player);
										addAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS, level, player);
										addAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE, level, player);
										addAnEnchantment(H, Enchantment.OXYGEN, level, player);
										addAnEnchantment(H, Enchantment.WATER_WORKER, level, player);
										addAnEnchantment(H, Enchantment.THORNS, level, player);
										player.sendMessage(PartAP + " protection environmental, protection fire, protection fall, protection explosions, protection projectile, oxygen, water worker, and thorns " + PartBP);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(args[1].equalsIgnoreCase("Tools") || args[1].equalsIgnoreCase("Tool"))
								{
									if(player.hasPermission("EndlessEnchant.Kits.Tools") || player.hasPermission("EndlessEnchant.Kits.*") || player.hasPermission("EndlessEnchant.Enchant.*") || StarPerm)
									{
										addAnEnchantment(H, Enchantment.DIG_SPEED, level, player);
										addAnEnchantment(H, Enchantment.SILK_TOUCH, level, player);
										addAnEnchantment(H, Enchantment.DURABILITY, level, player);
										addAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS, level, player);
										player.sendMessage(PartAP + " dig speed, silk touch, durability, and loots bonus blocks " + PartBP);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(args[1].equalsIgnoreCase("Swords") || args[1].equalsIgnoreCase("Sword"))
								{
									if(player.hasPermission("EndlessEnchant.Kits.Swords") || player.hasPermission("EndlessEnchant.Kits.*") || player.hasPermission("EndlessEnchant.Enchant.*") || StarPerm)
									{
										addAnEnchantment(H, Enchantment.DAMAGE_ALL, level, player);
										addAnEnchantment(H, Enchantment.DAMAGE_UNDEAD, level, player);
										addAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS, level, player);
										addAnEnchantment(H, Enchantment.KNOCKBACK, level, player);
										addAnEnchantment(H, Enchantment.FIRE_ASPECT, level, player);
										addAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS, level, player);
										player.sendMessage(PartAP + " damage all, damage undead, damage arthropods, knockback, fire aspect, and loot bonus mobs " + PartBP);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(args[1].equalsIgnoreCase("Bows") || args[1].equalsIgnoreCase("Bow"))
								{
									if(player.hasPermission("EndlessEnchant.Kits.Bows") || player.hasPermission("EndlessEnchant.Kits.*") || player.hasPermission("EndlessEnchant.Enchant.*") || StarPerm)
									{
										addAnEnchantment(H, Enchantment.ARROW_DAMAGE, level, player);
										addAnEnchantment(H, Enchantment.ARROW_KNOCKBACK, level, player);
										addAnEnchantment(H, Enchantment.ARROW_FIRE, level, player);
										addAnEnchantment(H, Enchantment.ARROW_INFINITE, level, player);
										player.sendMessage(PartAP + " arrow damage, arrow knockback, arrow fire, and arrow infinite " + PartBP);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(prot0.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.PROTECTION_ENVIRONMENTAL, level, player);
										player.sendMessage(PartA + " protection environmental " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(prot1.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.PROTECTION_FIRE, level, player);
										player.sendMessage(PartA + " protection fire " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(prot2.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.PROTECTION_FALL, level, player);
										player.sendMessage(PartA + " protection fall " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(prot3.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.PROTECTION_EXPLOSIONS, level, player);
										player.sendMessage(PartA + " protection explosions " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(prot4.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.PROTECTION_PROJECTILE, level, player);
										player.sendMessage(PartA + " protection projectile " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(prot5.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.OXYGEN, level, player);
										player.sendMessage(PartA + " oxygen " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(prot6.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.WATER_WORKER, level, player);
										player.sendMessage(PartA + " water worker " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(prot7.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.THORNS, level, player);
										player.sendMessage(PartA + " thorns " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(dmg16.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.DAMAGE_ALL, level, player);
										player.sendMessage(PartA + " damage all " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(dmg17.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.DAMAGE_UNDEAD, level, player);
										player.sendMessage(PartA + " damage undead " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(dmg18.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.DAMAGE_ARTHROPODS, level, player);
										player.sendMessage(PartA + " damage arthropods " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(dmg19.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.KNOCKBACK, level, player);
										player.sendMessage(PartA + " knockback " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(dmg20.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.FIRE_ASPECT, level, player);
										player.sendMessage(PartA + " fire aspect " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(dmg21.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.LOOT_BONUS_MOBS, level, player);
										player.sendMessage(PartA + " loot bonus mobs " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(tool32.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.DIG_SPEED, level, player);
										player.sendMessage(PartA + " dig speed " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(tool33.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.SILK_TOUCH, level, player);
										player.sendMessage(PartA + " silk touch " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(tool34.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.DURABILITY, level, player);
										player.sendMessage(PartA + " durability " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(tool35.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.LOOT_BONUS_BLOCKS, level, player);
										player.sendMessage(PartA + " loot bonus blocks " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(bow48.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.ARROW_DAMAGE, level, player);
										player.sendMessage(PartA + " arrow damage " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(bow49.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.ARROW_KNOCKBACK, level, player);
										player.sendMessage(PartA + " arrow knockback " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(bow50.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.ARROW_FIRE, level, player);
										player.sendMessage(PartA + " arrow fire " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else if(bow51.contains(args[1]))
								{
									if(EPerm || StarPerm)
									{
										addAnEnchantment(H, Enchantment.ARROW_INFINITE, level, player);
										player.sendMessage(PartA + " arrow infinite " + PartB);
									}
									else
									{
										player.sendMessage(NoPermission);
									}
								}
								else
								{
									player.sendMessage(EEPrefix + ChatColor.DARK_RED + "That is not a valid enchantment.");
								}
								return true;
							}
							return true;
						}
					}
					return true;
				}
				else if(args.length > 3)
				{
					player.sendMessage(TooManyArguments);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidEnchantmentName(String s) {
		for(ArrayList<String> list : collectiveEnchants.keySet()) {
			if(list.contains(s.toUpperCase())) return true;
		}
		if(getAliases(ChatColor.BLACK, s) != null) return true;
		return false;
	}
	
	public void enchant(String enchantment, ItemStack i) {
		
	}

	public String getAliases(ChatColor color, String s) {
		for(ArrayList<String> list : enchants.keySet()) {
			if(!list.contains(s)) continue;
			if(list.contains(s)) {
				String msg = EEPrefix + color + enchants.get(list).getName() + " Aliases: ";
				msg += list("and ", list) + ".";
				return msg;
			}
		}
		return null;
	}
}
