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
 * @version 2.0
 */
public class randomgive extends JavaPlugin {
	
    FileConfiguration config;
    boolean debug;
    boolean type;
    List<String> category;
    Random random;
    String[] message;
    String noperm;
	
	/**
     * on Plugin enable
     */
	public void onEnable() {
		loadConfig();
    	say("Config loaded");
    	
    	random = new Random();
	}

	
	/**
     * on Plugin disable
     */
	public void onDisable() {
		saveConfig();
	}

	
	/**
     * on Command
     * @param sender - command sender
     * @param cmd - command
     * @param label
     * @return true or false
     */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			// rdisable command
			if (cmd.getName().equalsIgnoreCase("rdisable")) {
				if ((sender instanceof Player)) {
					Player p = (Player)sender;
					if(p.hasPermission("random.disable") || p.isOp()){
							this.setEnabled(false);
							p.sendMessage(ChatColor.RED + "[Randomgive] was disabled");
							say("Randomgive disabled by " + p.getName());
						return true;
					}
					else{
						p.sendMessage(noperm);
						return true;
					}
				}
				else{
					this.setEnabled(false);
					System.out.println("[From Console][Randomgive] was disabled");
					return true;
				}
			}
			
			// rreset command
			else if (cmd.getName().equalsIgnoreCase("rreset")) {
				if ((sender instanceof Player)) {
					Player p = (Player)sender;
					if(p.hasPermission("random.reset") || p.isOp()){
						    File configFile = new File(getDataFolder(), "config.yml");
						    configFile.delete();
						    saveDefaultConfig();
							p.sendMessage(ChatColor.RED + "[Randomgive] config reset");
						    reload();
							p.sendMessage(ChatColor.RED + "[Randomgive] was reloaded");
							say("Randomgive reset by " + p.getName());
						return true;
					}
					else{
						p.sendMessage(noperm);
						return true;
					}
				}
				else{
					    File configFile = new File(getDataFolder(), "config.yml");
					    configFile.delete();
					    saveDefaultConfig();
					    System.out.println("[From Console][Randomgive] config reset");
					    reload();
					    System.out.println("[From Console][Randomgive] was reloaded");
					return true;
				}
			}
			
			// rreload command
			else if (cmd.getName().equalsIgnoreCase("rreload")) {
				if ((sender instanceof Player)) {
					Player p = (Player)sender;
					if(p.hasPermission("random.reload") || p.isOp()){
							reload();
							p.sendMessage(ChatColor.RED + "[Randomgive] was reloaded");
							say("Randomgive reloaded by " + p.getName());
						return true;
					}
					else{
						p.sendMessage(noperm);
						return true;
					}
				}
				else{
						reload();
						System.out.println("[From Console][Randomgive] was reloaded");
					return true;
				}
			}
			
			// rgive command
			else if (cmd.getName().equalsIgnoreCase("rgive")) {
				if ((sender instanceof Player)) {
					Player p = (Player)sender;
					if(p.hasPermission("random.give") || p.isOp()){
						if(args.length == 1){
							return give(p, args[0]);
						}
						else{
							return false;
						}
					}
					else{
						p.sendMessage(noperm);
						return true;
					}
				}
				else{
						System.out.println("[From Console][Randomgive] command ingame only");
					return true;
				}
			}

			
			// rothers command
			else if (cmd.getName().equalsIgnoreCase("rothers")) {
				if ((sender instanceof Player)) {
					Player p = (Player)sender;
					if(p.hasPermission("random.others") || p.isOp()){
						if(args.length == 2){
							Player p2 = this.getServer().getPlayer(args[1]);
								if(p2 != null){
									return give(p2, args[0]);
								}
								else{
									return false;
								}
						}
						else{
							return false;
						}
					}
					else{
						p.sendMessage(noperm);
						return true;
					}
				}
				else{
					if(args.length == 2){
						Player p2 = this.getServer().getPlayer(args[1]);
							if(p2 != null){
								return give(p2, args[0]);
							}
							else{
								return false;
							}
					}
					else{
						return false;
					}
				}
			}
			
			// rlist command
			else if(cmd.getName().equalsIgnoreCase("rlist")) {
				if ((sender instanceof Player)) {
					Player p = (Player)sender;
					if(p.hasPermission("random.list") || p.isOp()){
							p.sendMessage(ChatColor.RED + "[Randomgive]" + category.toString());
						return true;
					}
					else{
						p.sendMessage(noperm);
						return true;
					}
				}
				else{
						System.out.println("[From Console][Randomgive]" + category.toString());
					return true;
				}
			}
		
		// nothing to do here \o/
		return false;
	}
	
	
	/*
     * give basic material to player
     */
	private boolean give(Player p, String s) {
		if(category.contains(s)){
			try{
				List<String> liste = (List<String>) config.getStringList(s);
				int index = random.nextInt(liste.size());
				String[] unchecked = liste.get(index).split(":");
					Material material = Material.matchMaterial(unchecked[0]);
					int amount = Integer.parseInt(unchecked[1]);
				ItemStack item  = new ItemStack(material, amount);
				p.getInventory().addItem(item);
				
				tell(p, material, amount);
			}catch(Exception e){
	        	if(debug){
	        		e.printStackTrace();
	        	}
	        	return false;
			}
			return true;
		}
		else{
			p.sendMessage(ChatColor.RED + s + " does not exist.");
			return false;
		}
	}


	/*
	 *  output message set in config
	 */
	private void tell(Player p, Material material, int amount){
		if(type){
			if(!message[0].startsWith("$NONE$")){
					message[3] = message[0].replace("$PlayerName$", p.getName());
					message[3] = message[3].replace("$ItemName$", material.toString());
					message[3] = message[3].replace("$World$", p.getWorld().getName());
					message[3] = message[3].replace("$ItemAmount$", "" + amount);
				p.sendMessage(message[3]);
			}
			if(!message[0].startsWith("$NONE$")){
					message[4] = message[1].replace("$PlayerName$", p.getName());
					message[4] = message[4].replace("$ItemName$", material.toString());
					message[4] = message[4].replace("$World$", p.getWorld().getName());
					message[4] = message[4].replace("$ItemAmount$", "" + amount);
				p.sendMessage(message[4]);
			}		
			if(!message[0].startsWith("$NONE$")){
					message[5] = message[2].replace("$PlayerName$", p.getName());
					message[5] = message[5].replace("$ItemName$", material.toString());
					message[5] = message[5].replace("$World$", p.getWorld().getName());
					message[5] = message[5].replace("$ItemAmount$", "" + amount);
				p.sendMessage(message[5]);
			}
		}
		else{
			if(!message[0].startsWith("$NONE$")){
					message[3] = message[0].replace("$PlayerName$", p.getName());
					message[3] = message[3].replace("$ItemName$", material.toString());
					message[3] = message[3].replace("$World$", p.getWorld().getName());
					message[3] = message[3].replace("$ItemAmount$", "" + amount);
				this.getServer().broadcastMessage(message[3]);
			}
			if(!message[0].startsWith("$NONE$")){
					message[4] = message[1].replace("$PlayerName$", p.getName());
					message[4] = message[4].replace("$ItemName$", material.toString());
					message[4] = message[4].replace("$World$", p.getWorld().getName());
					message[4] = message[4].replace("$ItemAmount$", "" + amount);
				this.getServer().broadcastMessage(message[4]);
			}		
			if(!message[0].startsWith("$NONE$")){
					message[5] = message[2].replace("$PlayerName$", p.getName());
					message[5] = message[5].replace("$ItemName$", material.toString());
					message[5] = message[5].replace("$World$", p.getWorld().getName());
					message[5] = message[5].replace("$ItemAmount$", "" + amount);
				this.getServer().broadcastMessage(message[5]);
			}
		}	
	}

	
	/**
     * load config settings
     */
	private void loadConfig() {
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();

		debug = config.getBoolean("debug");
		type = config.getBoolean("message.type");
		noperm = ChatColor.translateAlternateColorCodes('&', config.getString("message.nopermission"));
		category = (List<String>) config.getStringList("category");
		
		message = new String[6];
		message[0] = ChatColor.translateAlternateColorCodes('&', config.getString("message.line1"));
		message[1] = ChatColor.translateAlternateColorCodes('&', config.getString("message.line2"));
		message[2] = ChatColor.translateAlternateColorCodes('&', config.getString("message.line3"));
	}
	   
    
    /**
     * reload
     */
    private void reload(){
 	   	try {
 	   		// Remove unused variables and objects
			    config = null;
			    random = null;

			    category = null;
			    message = null;

			// Run java garbage collector to delete unused things
			    System.gc();
			
			// load everything again
				this.reloadConfig();
				loadConfig();
			
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
