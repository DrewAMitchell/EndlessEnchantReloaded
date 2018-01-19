package me.Drkmaster83.EndlessEnchant;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class YMLConfig extends YamlConfiguration
{
	private EndlessEnchant pl;
	private File file;
	
	public YMLConfig(EndlessEnchant pl, String name) {
		this.pl = pl;
		if(!name.endsWith(".yml")) name += ".yml";
		file = new File(pl.getDataFolder(), name);
	}
	
	/** @function Reload config object in RAM to that of the file */
	public boolean reload() {
		boolean existed = file.exists();
		if(!file.exists()) {
			if(pl.getResource(file.getName()) != null) pl.saveResource(file.getName(), true); //Save the one from the JAR if possible
			else {
				try {
					file.createNewFile(); //Create a blank file
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			this.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return existed;
	}
	
	/** @function Save config object in RAM to the file */
	public void save() {
		try	{ this.save(file); }
		catch (Exception e)	{ e.printStackTrace(); }
	}
}