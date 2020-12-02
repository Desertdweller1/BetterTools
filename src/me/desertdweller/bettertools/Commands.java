package me.desertdweller.bettertools;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.desertdweller.bettertools.math.BlockMath;
import me.desertdweller.bettertools.math.Noise;
import me.desertdweller.bettertools.undo.ChangeTracker;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 1 || args[0].equalsIgnoreCase("help")) {
			if(args.length < 1 || (args[0].equalsIgnoreCase("help") && args.length == 1)) {
				sender.sendMessage(ChatColor.GOLD + "/bt tool");
				sender.sendMessage(ChatColor.GRAY + "This gives you the tool which how you will be able to use BetterTools. Almost all BT commands will be to adjust this tool. Everything you do to this tool will be saved, can be duplicated by copying the item, and so on.");
				sender.sendMessage(ChatColor.GOLD + "/bt radius <radius>");
				sender.sendMessage(ChatColor.GRAY + "While holding a tool, this command will set its radius.");
				sender.sendMessage(ChatColor.GOLD + "/bt blocks <block list>");
				sender.sendMessage(ChatColor.GRAY + "While holding a tool, this command will set the blocks it uses. The block list should be all the material names, without spaces, and seperated by commas. Putting the same material in the list multiple times increases its likelyhood of being used for each block replaced.");
				sender.sendMessage(ChatColor.GOLD + "/bt mask <block list>");
				sender.sendMessage(ChatColor.GRAY + "While holding a tool, this command will set the blocks it chooses to replace. The block list should be all the material names, without spaces, and seperated by commas.");
				sender.sendMessage(ChatColor.GOLD + "/bt through <block list>");
				sender.sendMessage(ChatColor.GRAY + "While holding a tool, this command will set the blocks it will ignore when choosing a landing. By default it is air, but if you are editing an ocean floor for example, you can add water to the list. The block list should be all the material names, without spaces, and seperated by commas.");
				sender.sendMessage(ChatColor.GOLD + "/bt blockupdates <true/false>");
				sender.sendMessage(ChatColor.GRAY + "While holding a tool, this command will enable or disable block updates done by it.");
				sender.sendMessage(ChatColor.GOLD + "/bt noise <scale> <xskew> <yskew> <zskew> <min> <max> <frequency> <none/turb/perlin)>");
				sender.sendMessage(ChatColor.GRAY + "While holding a tool, this command will make the tool follow a noise generation algorithm to choose blocks (after checking the tool masks). If the tool has a noise algorithm already assigned, use '/bt noise off', or set the method as none to remove it again. For more information, you can use /bt help <parameter name> to find out more about what the parameter does.");
				sender.sendMessage(ChatColor.GOLD + "/bt undo");
				sender.sendMessage(ChatColor.GRAY + "This will undo your previous action. Up to 200 times.");
				sender.sendMessage(ChatColor.GOLD + "/bt help <parameter/concept>");
				sender.sendMessage(ChatColor.GRAY + "This command followed by almost any concept or parameter mentioned, will give a brief explaination of it.");
			}else {
				if(args[1].equalsIgnoreCase("scale")) {
					sender.sendMessage(ChatColor.GRAY + "The 'scale' parameter for noise generation affects how large the noise is. This means there will be larger more continuous sections of random noise. If using Perlin noise, having it set to '1' will not work. This value is best used with decimals.");
				}else if(args[1].equalsIgnoreCase("xscew") || args[1].equalsIgnoreCase("yscew") || args[1].equalsIgnoreCase("zscew")) {
					sender.sendMessage(ChatColor.GRAY + "The 'scew' parameters affect how much the noise is stretched in that direction.");
				}else if(args[1].equalsIgnoreCase("min") || args[1].equalsIgnoreCase("max")) {
					sender.sendMessage(ChatColor.GRAY + "The noise generation generates a value for each location, ranging from -1.0 to 1.0. The min and max parameters make it so the blocks will only be placed upon the values in between their range. Larger ranges means more blocks, while the opposite is true for smaller ranges. The defaults for these parameters are Min: 0, Max: 1.");
				}else if(args[1].equalsIgnoreCase("frequency")) {
					sender.sendMessage(ChatColor.GRAY + "This parameter increases how common large continuous sections groups of blocks are, without adjusting their scale. This parameter is only applicable to the 'turbulence' method. The default is '0.5'.");
				}else if(args[1].equalsIgnoreCase("turb") || args[1].equalsIgnoreCase("turbulence")) {
					sender.sendMessage(ChatColor.GRAY + "This is a method of generating block noise. It tends to be wavy and freeform looking than Perlin Noise. ");
				}else if(args[1].equalsIgnoreCase("perlin")) {
					sender.sendMessage(ChatColor.GRAY + "Perlin noise is a much more uniform randomness. It will make more natural shapes and islands.");
				}else if(args[1].equalsIgnoreCase("noise")) {
					sender.sendMessage(ChatColor.GRAY + "Noise in the context of generation, is an algorithm which will create randomness, but not completely. It will usually generate large clumps of (in this case) blocks together. Specifically, it is most helpful for creating natural patterns on surfaces, or in 3d space.");
				}else {
					sender.sendMessage(ChatColor.WHITE + "/bt help");
					sender.sendMessage(ChatColor.WHITE + "/bt help scale");
					sender.sendMessage(ChatColor.WHITE + "/bt help [xscew/yscew/zscew]");
					sender.sendMessage(ChatColor.WHITE + "/bt help [min/max]");
					sender.sendMessage(ChatColor.WHITE + "/bt help frequency");
					sender.sendMessage(ChatColor.WHITE + "/bt help [turb/turbulence]");
					sender.sendMessage(ChatColor.WHITE + "/bt help perlin");
					sender.sendMessage(ChatColor.WHITE + "/bt help noise");
				}
			}
			return true;
		}else if(args[0].equalsIgnoreCase("tool")) {
			if(!sender.hasPermission("bt.create")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return true;
			}
			Player p = (Player) sender;
			
			NBTItem nbti = new NBTItem(new ItemStack(Material.GOLDEN_HOE));
			nbti.setString("Plugin", "BetterTools");
			nbti.setString("Item", "Paint Tool");
			if(args.length > 1) {
				nbti.setInteger("Radies", Integer.parseInt(args[1]));
			}
			nbti.setInteger("Radius", 5);
			nbti.setString("Blocks", "stone,air");
			nbti.setString("Mask", "air");
			nbti.setString("Through", "air");
			nbti.setBoolean("Updates", false);
			Noise noise = new Noise();
			noise.scale = 1;
			noise.xScew = 1;
			noise.yScew = 1;
			noise.zScew = 1;
			noise.min = 0;
			noise.max = 1;
			noise.frequency = 1;
			
			nbti.setString("Noise", noise.toString());
			if(args.length > 1)
				nbti.setInteger("Radius", Integer.parseInt(args[1]));
			
			ItemStack item = nbti.getItem();
			ItemMeta meta = item.getItemMeta();
			meta.setLore(getLore(nbti));
			item.setItemMeta(meta);
			p.getInventory().addItem(item);
			return true;
		}else if(args[0].equalsIgnoreCase("radius")) {
			if(!sender.hasPermission("bt.create")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return true;
			}
			Player p = (Player) sender;
			NBTItem nbti = new NBTItem(p.getInventory().getItemInMainHand());
			if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools")) {
				nbti.setInteger("Radius", Integer.parseInt(args[0]));
			}else {
				p.sendMessage(ChatColor.RED + "You are not holding a BT tool. Find one or use /bt tool");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("mask")) {
			if(!sender.hasPermission("bt.create")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return true;
			}
			Player p = (Player) sender;
			NBTItem nbti = new NBTItem(p.getInventory().getItemInMainHand());
			if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools")) {
				if(args.length > 1) {
					if(args[1].equalsIgnoreCase("empty") || args[1].equalsIgnoreCase("none") || args[1].equalsIgnoreCase("off")) {
						nbti.setString("Mask", "empty");
						p.sendMessage("Mask cleared");
					}else {
						String invalidName = BlockMath.checkStringList(args[1]);
						if(invalidName == null) {
							nbti.setString("Mask", args[1]);
						}else {
							p.sendMessage(ChatColor.RED + "'" + invalidName + "' is not a valid material!");
						}
					}
					ItemStack item = nbti.getItem();
					ItemMeta meta = item.getItemMeta();
					meta.setLore(getLore(nbti));
					item.setItemMeta(meta);
					p.getInventory().setItemInMainHand(item);
				}else {
					p.sendMessage(ChatColor.WHITE + "/bt mask <block list>");
					p.sendMessage(ChatColor.WHITE + "Ex: '/bt mask air,stone,white_wool', or '/bt mask empty'");
				}
			}else {
				p.sendMessage(ChatColor.RED + "You are not holding a BT tool. Find one or use /bt tool");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("refreshtool")) {
			if(!sender.hasPermission("bt.create")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return true;
			}
			Player p = (Player) sender;
			NBTItem nbti = new NBTItem(p.getInventory().getItemInMainHand());
			if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools")) {
				ItemStack item = nbti.getItem();
				ItemMeta meta = item.getItemMeta();
				meta.setLore(getLore(nbti));
				item.setItemMeta(meta);
				p.getInventory().setItemInMainHand(item);
			}else {
				p.sendMessage(ChatColor.RED + "You are not holding a BT tool. Find one or use /bt tool");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("blocks")) {
			if(!sender.hasPermission("bt.create")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return true;
			}
			Player p = (Player) sender;
			NBTItem nbti = new NBTItem(p.getInventory().getItemInMainHand());
			if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools")) {
				if(args.length > 1) {
					String invalidName = BlockMath.checkStringList(args[1]);
					if(invalidName == null) {
						nbti.setString("Blocks", args[1]);
					}else {
						p.sendMessage(ChatColor.RED + "'" + invalidName + "' is not a valid material!");
					}
					ItemStack item = nbti.getItem();
					ItemMeta meta = item.getItemMeta();
					meta.setLore(getLore(nbti));
					item.setItemMeta(meta);
					p.getInventory().setItemInMainHand(item);
				}else {
					p.sendMessage(ChatColor.WHITE + "/bt blocks <block list>");
					p.sendMessage(ChatColor.WHITE + "Ex: '/bt blocks air,stone,white_wool'");
				}
			}else {
				p.sendMessage(ChatColor.RED + "You are not holding a BT tool. Find one or use /bt tool");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("blockupdates")){
			if(!sender.hasPermission("bt.create")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return true;
			}
			Player p = (Player) sender;
			NBTItem nbti = new NBTItem(p.getInventory().getItemInMainHand());
			if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools")) {
				if(args.length == 2 && (args[1].toLowerCase().equals("true") || args[1].toLowerCase().equals("false"))) {
					nbti.setBoolean("Updates", Boolean.valueOf(args[1]));
					ItemStack item = nbti.getItem();
					ItemMeta meta = item.getItemMeta();
					meta.setLore(getLore(nbti));
					item.setItemMeta(meta);
					p.getInventory().setItemInMainHand(item);
				}else {
					p.sendMessage(ChatColor.WHITE + "/bt blockupdates <true/false>");
					p.sendMessage(ChatColor.WHITE + "Ex: '/bt blockupdates true'");
				}
			}else {
				p.sendMessage(ChatColor.RED + "You are not holding a BT tool. Find one or use /bt tool");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("undo")){
		
			if(!sender.hasPermission("bt.use")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return true;
			}
			Player p = (Player) sender;
			ChangeTracker tracker = ChangeTracker.getChangesForPlayer(p.getUniqueId());
			if(tracker == null || tracker.getUndosAvailable() == 0) {
				p.sendMessage(ChatColor.RED + "You have nothing to undo.");
				return true;
			}
			p.sendMessage(ChatColor.GRAY + "Undone " + tracker.undo() + " blocks.");
			return true;
		}else if(args[0].equalsIgnoreCase("through")){
			if(!sender.hasPermission("bt.create")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return true;
			}
			Player p = (Player) sender;
			NBTItem nbti = new NBTItem(p.getInventory().getItemInMainHand());
			if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools")) {
				if(args.length > 1) {
					String invalidName = BlockMath.checkStringList(args[1]);
					if(invalidName == null) {
						nbti.setString("Through", args[1]);
						ItemStack item = nbti.getItem();
						ItemMeta meta = item.getItemMeta();
						meta.setLore(getLore(nbti));
						item.setItemMeta(meta);
						p.getInventory().setItemInMainHand(item);
					}else {
						p.sendMessage(ChatColor.RED + "'" + invalidName + "' is not a valid material!");
					}
				}else {
					p.sendMessage(ChatColor.WHITE + "/bt through <block list>");
					p.sendMessage(ChatColor.WHITE + "Ex: '/bt through air,water'");
				}
			}else {
				p.sendMessage(ChatColor.RED + "You are not holding a BT tool. Find one or use /bt tool");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("noise")) {
			if(!sender.hasPermission("bt.create")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return true;
			}
			Player p = (Player) sender;
			NBTItem nbti = new NBTItem(p.getInventory().getItemInMainHand());
			if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools")) {
				if(args.length == 2 && args[2].equals("off")) {
					Noise noise = new Noise(nbti.getString("Noise"));
					noise.method = "off";
					nbti.setString("Noise", noise.toString());
				}else if(args.length < 9) {
					p.sendMessage(ChatColor.WHITE + "/bt noise <scale> <Xscew> <Yscew> <Zscew> <min> <max> <frequency> <none/turb/perlin>");
					p.sendMessage(ChatColor.WHITE + "For more information, you can use '/bt help noise'. Or for help understanding each input, '/bt help <input>'.");
				}else if(args.length > 8){
					Noise noise = new Noise(nbti.getString("Noise"));
					if(!args[1].equals("~"))
						noise.scale = Float.parseFloat(args[1]);
					if(!args[2].equals("~"))
						noise.xScew = Float.parseFloat(args[2]);
					if(!args[3].equals("~"))
						noise.yScew = Float.parseFloat(args[3]);
					if(!args[4].equals("~"))
						noise.zScew = Float.parseFloat(args[4]);
					if(!args[5].equals("~"))
						noise.min = Float.parseFloat(args[5]);
					if(!args[6].equals("~"))
						noise.max = Float.parseFloat(args[6]);
					if(!args[7].equals("~"))
						noise.frequency = Float.parseFloat(args[7]);
					if(!args[8].equals("none") && !args[8].equals("turb") && !args[8].equals("perlin")) {
						sender.sendMessage(ChatColor.RED + args[8] + " is not a valid choice: 'none', 'turb', 'perlin'.");
						return true;
					}
					noise.method = args[8];
					nbti.setString("Noise", noise.toString());
					if(noise.method.equals("perlin") && args[1].equals("1")) {
						p.sendMessage(ChatColor.YELLOW + "WARNING: Using a scale of exactly 1 with Perlin noise will not work properly. Use 1.001 instead.");
					}
				}
				ItemStack item = nbti.getItem();
				ItemMeta meta = item.getItemMeta();
				meta.setLore(getLore(nbti));
				item.setItemMeta(meta);
				p.getInventory().setItemInMainHand(item);
			}else {
				p.sendMessage(ChatColor.RED + "You are not holding a BT tool. Find one or use /bt tool");
			}
			return true;
		}
		return false;
	}
	
	private ArrayList<String> getLore(NBTItem item){
		ArrayList<String> lore = new ArrayList<String>();
		
		lore.add(ChatColor.GOLD + "Radius: " + ChatColor.WHITE + item.getInteger("Radius"));
		
		String loreConstructor = ChatColor.GOLD + "Painting: " + ChatColor.WHITE;
		int loopTracker = 0;
		for(int i = 0; i < item.getString("Blocks").split(",").length; i++) {
			loreConstructor = loreConstructor + item.getString("Blocks").split(",")[i];
			if(loopTracker == 2) {
				loopTracker = 0;
				lore.add(loreConstructor);
				loreConstructor = ChatColor.WHITE + "    ";
			}else {
				if(i != item.getString("Blocks").split(",").length-1)
					loreConstructor = loreConstructor + ",";
				loopTracker++;
			}
		}
		if(!loreConstructor.equals("    "))
			lore.add(loreConstructor);
		
		loreConstructor = ChatColor.GOLD + "Masks: " + ChatColor.WHITE;
		loopTracker = 0;
		for(int i = 0; i < item.getString("Mask").split(",").length; i++) {
			loreConstructor = loreConstructor + item.getString("Mask").split(",")[i];
			if(loopTracker == 2) {
				loopTracker = 0;
				lore.add(loreConstructor);
				loreConstructor = ChatColor.WHITE + "    ";
			}else {
				if(i != item.getString("Mask").split(",").length-1)
					loreConstructor = loreConstructor + ",";
				loopTracker++;
			}
		}
		if(!loreConstructor.equals("    "))
			lore.add(loreConstructor);

		loreConstructor = ChatColor.GOLD + "Through: " + ChatColor.WHITE;
		loopTracker = 0;
		for(int i = 0; i < item.getString("Through").split(",").length; i++) {
			loreConstructor = loreConstructor + item.getString("Through").split(",")[i];
			if(loopTracker == 2) {
				loopTracker = 0;
				lore.add(loreConstructor);
				loreConstructor = ChatColor.WHITE + "    ";
			}else {
				if(i != item.getString("Through").split(",").length-1)
					loreConstructor = loreConstructor + ",";
				loopTracker++;
			}
		}
		if(!loreConstructor.equals("    "))
			lore.add(loreConstructor);
		
		lore.add(ChatColor.GOLD + "Block Updates: " + ChatColor.WHITE + item.getBoolean("Updates"));
		
		Noise noise = new Noise(item.getString("Noise"));
		
		if(noise.method.equals("none")) {
			lore.add(ChatColor.GOLD + "Noise Method: " + ChatColor.WHITE + "None");
		}else if(noise.method.equals("turb")) {
			lore.add(ChatColor.GOLD + "Noise Method: " + ChatColor.WHITE + "Turbulence");
			lore.add(ChatColor.GOLD + "Noise Scale: " + ChatColor.WHITE + noise.scale);
			lore.add(ChatColor.GOLD + "Noise X Scew: " + ChatColor.WHITE + noise.xScew);
			lore.add(ChatColor.GOLD + "Noise Y Scew: " + ChatColor.WHITE + noise.yScew);
			lore.add(ChatColor.GOLD + "Noise Z Scew: " + ChatColor.WHITE + noise.zScew);
			lore.add(ChatColor.GOLD + "Noise Min Val: " + ChatColor.WHITE + noise.min);
			lore.add(ChatColor.GOLD + "Noise Max Val: " + ChatColor.WHITE + noise.max);
			lore.add(ChatColor.GOLD + "Noise Freq: " + ChatColor.WHITE + noise.frequency);
		}else if(noise.method.equals("perlin")) {
			lore.add(ChatColor.GOLD + "Noise Method: " + ChatColor.WHITE + "Perlin");
			lore.add(ChatColor.GOLD + "Noise Scale: " + ChatColor.WHITE + noise.scale);
			lore.add(ChatColor.GOLD + "Noise X Scew: " + ChatColor.WHITE + noise.xScew);
			lore.add(ChatColor.GOLD + "Noise Y Scew: " + ChatColor.WHITE + noise.yScew);
			lore.add(ChatColor.GOLD + "Noise Z Scew: " + ChatColor.WHITE + noise.zScew);
			lore.add(ChatColor.GOLD + "Noise Min Val: " + ChatColor.WHITE + noise.min);
			lore.add(ChatColor.GOLD + "Noise Max Val: " + ChatColor.WHITE + noise.max);
		}
		
		return lore;
	}

}
