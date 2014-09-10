package randomgive;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * @author DieFriiks / CustomCraftDev / undeaD_D
 * @category randomgive plugin
 * @version 1.0
 */
public class randomgive extends JavaPlugin {
	
    FileConfiguration config;
    Boolean debug;
    List<String> items;
    Random random;
    String message1;
    String message2;
    String message3;
    ChatColor[] color;
    
	
	/**
     * on Plugin enable
     */
	public void onEnable() {
		loadConfig();
    		if(debug){
    			say("Config loaded");
    		}
	}

	
	/**
     * on Plugin disable
     */
	public void onDisable() {
		try {
			saveConfig();
		} catch (Exception e) {
        		if(debug){
        			e.printStackTrace();
        		}
		}
	}

	
	/**
     * on Command
     * @param sender - command sender
     * @param cmd - command
     * @param label
     * @return boolean
     */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((sender instanceof Player)) {
			Player p = (Player)sender;
			
			// rdisable command
			if (cmd.getName().equalsIgnoreCase("rdisable") && p.isOp()) {
					this.setEnabled(false);
					p.sendMessage("disabled randomgive ...");
					if(debug){
						say("Randomgive disabled by " + p.getName());
					}
				return true;
			}
			
			// rreset command
			if (cmd.getName().equalsIgnoreCase("rreset") && p.isOp()) {
				    File configFile = new File(getDataFolder(), "config.yml");
				    configFile.delete();
				    saveDefaultConfig();
				    reload();
					p.sendMessage("randomgive was reset ...");
					if(debug){
						say("Randomgive reset by " + p.getName());
					}
				return true;
			}
			
			// rgive command
			if (cmd.getName().equalsIgnoreCase("rgive")) {
					give(p, random.nextInt(items.size()));
				return true;
			}
			// rreload command
			if (cmd.getName().equalsIgnoreCase("rreload") && p.isOp()) {
					reload();
					p.sendMessage("reloaded randomgive ...");
					if(debug){
						say("Randomgive reloaded by " + p.getName());
					}
				return true;
			}
		}
		
		// commands from console
		else{
			say("Command ingame only ...");
			return true;
		}
		
		// nothing to do here \o/
		return false;
	}
	
	
	/**
     * add material to player inventory
     */
	private void give(Player p, Integer index) {
		String[] unchecked = items.get(index).split(":");
		Material material = Material.matchMaterial(unchecked[0]);
		int amount = Integer.parseInt(unchecked[1]);
		
		ItemStack item  = new ItemStack(material, amount);
		p.getInventory().addItem(item);
		
		message1 = message1.replace("$PlayerName$", p.getName());
		message2 = message2.replace("$PlayerName$", p.getName());
		message3 = message3.replace("$PlayerName$", p.getName());
		
		message1 = message1.replace("$ItemName$", material.toString());
		message2 = message2.replace("$ItemName$", material.toString());
		message3 = message3.replace("$ItemName$", material.toString());
		
		message1 = message1.replace("$ItemAmount$", "" + amount);
		message2 = message2.replace("$ItemAmount$", "" + amount);
		message3 = message3.replace("$ItemAmount$", "" + amount);
		
		if(!message1.startsWith("$NONE$")){
			this.getServer().broadcastMessage(color[0] + message1);
		}
		if(!message2.startsWith("$NONE$")){
			this.getServer().broadcastMessage(color[1] + message2);
		}		
		if(!message3.startsWith("$NONE$")){
			this.getServer().broadcastMessage(color[2] + message3);
		}
	}


	/**
     * load config settings
     */
	private void loadConfig() {
		random = new Random();
		
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();

		debug = config.getBoolean("debug");
		items = (List<String>) config.getStringList("items");
		
		message1 = config.getString("message.line1");
		message2 = config.getString("message.line2");
		message3 = config.getString("message.line3");
		
		color = new ChatColor[3];
		color[0] = ChatColor.valueOf(config.getString("message.color1"));
		color[1] = ChatColor.valueOf(config.getString("message.color2"));
		color[2] = ChatColor.valueOf(config.getString("message.color3"));
	}
	   
    
    /**
     * reload
     */
    private void reload(){
 	   try {
 	   	// Remove unused objects
		    config = null;
		    random = null;
			    
		    debug = null;
		    items = null;
		    color = null;
		    
		    message1 = null;
		    message2 = null;
		    message3 = null;
		    

		    
		// Run java garbage collector to delete unused things
		    System.gc();
		
		// load everything again -.-
			this.reloadConfig();
			loadConfig();
		if(debug){
			say("reload successfull");
		}
		
	   	} catch (Exception e) {
        	    	if(debug){
        			e.printStackTrace();
        		}
        	}
    }
    
    
    /**
     * print to console
     * @param message to print
     */
	public void say(String out) {
		if(debug){
			System.out.println("[Randomgive] " + out);
		}
	}
}
