package me.Drkmaster83.EndlessEnchant;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YMLConfig
{
	private Plugin p;
	private String fileName;
	private File dataFile;
	private YamlConfiguration data;
	
	public YMLConfig(Plugin p, String fileName) {
		this.p = p;
		this.fileName = fileName;
		if(!(fileName.endsWith(".yml"))) this.fileName = this.fileName + ".yml";
		initialize();
	}
	
	public void initialize() {
		dataFile = new File(p.getDataFolder(), fileName);
	}
	
	/**
	 * @return False if it was nonexistant
	 * @function Creates file with initialized data. 
	 */
	public boolean create() {
		if(dataFile == null) dataFile = new File(p.getDataFolder(), fileName);
		try {
			if(!p.getDataFolder().exists()) p.getDataFolder().mkdir();
			if(!dataFile.exists()) {
				dataFile.createNewFile();
				data = YamlConfiguration.loadConfiguration(dataFile);
				save();
				return false;
			}
			data = YamlConfiguration.loadConfiguration(dataFile);
			return true;
		}
		catch (Exception e) { 
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return The data.yml file's configuration object.
	 */
	public FileConfiguration toFileConf() {
		return data;
	}
	
	/**
	 * @function Sets data and saves that data to the file
	 */
	public void set(String path, Object value) {
		if(data.contains(path)) {
			if(value.equals(data.get(path))) return;
		}
		data.set(path, value);
		save();
	}
	
	/**
	 * @function Reload config object in RAM to that of the file
	 */
	public void reload() {
		data = YamlConfiguration.loadConfiguration(dataFile);
		save();
	}
	
	/**
	 * @function Save config object in RAM to the file
	 */
	public void save() {
		try	{ data.save(dataFile); }
		catch (Exception e)	{ e.printStackTrace(); }
	}
}
