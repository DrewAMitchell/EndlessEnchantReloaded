package me.Drkmaster83.EndlessEnchant;

public enum EndlessEnchantment
{
	PROTECTION_ENVIRONMENTAL("ARMOR", "b", 0),
	PROTECTION_FIRE("ARMOR", "b", 1),
	PROTECTION_FALL("ARMOR", "b", 2),
	PROTECTION_EXPLOSIONS("ARMOR", "b", 3),
	PROTECTION_PROJECTILE("ARMOR", "b", 4),
	OXYGEN("HELM", "b", 5),
	WATER_WORKER("HELM", "b", 6),
	THORNS("ARMOR", "b", 7),
	DEPTH_STRIDER("BOOTS", "b", 8),
	FROST_WALKER("BOOTS", "b", 9),
	BINDING_CURSE("ARMOR", "b", 10),
	DAMAGE_ALL("WEAPON", "c", 16),
	DAMAGE_UNDEAD("WEAPON", "c", 17),
	DAMAGE_ARTHROPODS("WEAPON", "c", 18),
	KNOCKBACK("WEAPON", "c", 19),
	FIRE_ASPECT("WEAPON", "c", 20),
	LOOT_BONUS_MOBS("WEAPON", "c", 21),
	SWEEPING("WEAPON", "c", 22),
	DIG_SPEED("TOOL", "d", 32),
	SILK_TOUCH("TOOL", "d", 33),
	DURABILITY("ALL", "a", 34),
	LOOT_BONUS_BLOCKS("TOOL", "d", 35),
	ARROW_DAMAGE("RANGED_WEAPON", "4", 48),
	ARROW_KNOCKBACK("RANGED_WEAPON", "4", 49),
	ARROW_FIRE("RANGED_WEAPON", "4", 50),
	ARROW_INFINITE("RANGED_WEAPON", "4", 51),
	LUCK("FISHING", "3", 61),
	LURE("FISHING", "3", 62),
	MENDING("ALL", "a", 70),
	VANISHING_CURSE("ALL", "a", 71);
	
	String category, categoryColor;
	short id;
	
	EndlessEnchantment(String category, String categoryColor, int id) {
		this.category = category;
		this.categoryColor = categoryColor;
		this.id = (short) id;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getCategoryColor() {
		return categoryColor;
	}
	
	public int getID() {
		return id;
	}
	
	public static EndlessEnchantment getByName(String formalName) {
		for(EndlessEnchantment e : values()) {
			if(e.name().equalsIgnoreCase(formalName.toUpperCase())) return e;
		}
		return null;
	}
}