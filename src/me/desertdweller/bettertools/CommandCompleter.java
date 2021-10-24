package me.desertdweller.bettertools;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.desertdweller.bettertools.math.BlockMath;

public class CommandCompleter implements TabCompleter{
	private static BetterTools plugin = BetterTools.getPlugin(BetterTools.class);
	
	private List<String> firstLevelArguments = new ArrayList<String>();
	private List<String> helpArguments = new ArrayList<String>();
	private List<String> boolArguments = new ArrayList<String>();
	
	public CommandCompleter() {
		firstLevelArguments.add("help");
		firstLevelArguments.add("tool");
		firstLevelArguments.add("snowtool");
		firstLevelArguments.add("settool");
		firstLevelArguments.add("radius");
		firstLevelArguments.add("mask");
		firstLevelArguments.add("blocks");
		firstLevelArguments.add("blockupdates");
		firstLevelArguments.add("through");
		firstLevelArguments.add("touching");
		firstLevelArguments.add("undo");
		firstLevelArguments.add("noise");
		firstLevelArguments.add("refreshtool");
		
		helpArguments.add("scale");
		helpArguments.add("xscew");
		helpArguments.add("yscew");
		helpArguments.add("zscew");
		helpArguments.add("min");
		helpArguments.add("max");
		helpArguments.add("frequency");
		helpArguments.add("turb");
		helpArguments.add("turbulence");
		helpArguments.add("perlin");
		helpArguments.add("noise");
		
		boolArguments.add("true");
		boolArguments.add("false");
	}
	
	public @Nullable List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd,
			@Nonnull String label, @Nonnull String[] args) {
		
		List<String> results = new ArrayList<String>();
		
		if(args.length == 1) {
			results = prunePossibilities(firstLevelArguments,args[0]);
		}else if(args.length == 2 && args[0].equals("help")) {
			results = prunePossibilities(helpArguments,args[1]);
		}else if(args.length == 2 && args[0].equals("settool")) {
			results = prunePossibilities(BlockMath.matArrayToStringList(Material.values()),args[1]);
		}else if(args.length == 2 && args[0].equals("radius")) {
			results.add("radius: 1 - " + plugin.getConfig().getString("maxRadius"));
		}else if(args.length == 2 && args[0].equals("mask")) {
			results.addAll(getPossibleBlocks(args[1]));
		}else if(args.length == 2 && args[0].equals("blocks")) {
			results.addAll(getPossibleBlocks(args[1]));
		}else if(args.length == 2 && args[0].equals("through")) {
			results.addAll(getPossibleBlocks(args[1]));
		}else if(args.length == 2 && args[0].equals("touching")) {
			results.addAll(getPossibleBlocks(args[1]));
		}else if(args.length == 2 && args[0].equals("blockupdates")) {
			results = prunePossibilities(boolArguments,args[1]);
		}else if(args[0].equals("noise")) {
			if(args.length == 2) {
				results.add("scale (any decimal number)");
				results.add("off");
			}
			if(args.length == 3)
				results.add("xscew: (any decimal number)");
			if(args.length == 4)
				results.add("yscew: (any decimal number)");
			if(args.length == 5)
				results.add("zscew: (any decimal number)");
			if(args.length == 6)
				results.add("min: -1.0 to 1.0");
			if(args.length == 7)
				results.add("max: -1.0 to 1.0");
			if(args.length == 8)
				results.add("frequency: (any decimal number)");
			if(args.length == 9) {
				results.add("none");
				results.add("turb");
				results.add("perlin");
			}
		}
		
		
		
		
		
		return results;
	}

	
	private List<String> prunePossibilities(List<String> allOptions, String starting){
		List<String> pruned = new ArrayList<String>();
		for(String option : allOptions) {
			if(option.startsWith(starting) && option != "air" && !option.equals(starting))
				pruned.add(option);
		}
		return pruned;
	}
	
	private List<String> getPossibleBlocks(String argument){
		List<String> possibleBlocks = new ArrayList<String>();
		
		String beforeBlocks = "";
		for(int i = 0; i < argument.split(",").length - 1; i++) {
			beforeBlocks = beforeBlocks + argument.split(",")[i] + ",";
		}
		for(Material mat : Material.values()) {
			if(mat.isBlock())
				possibleBlocks.add((beforeBlocks.concat(mat.toString().toLowerCase())).replaceAll("minecraft:", ""));
		}
		possibleBlocks = prunePossibilities(possibleBlocks, argument);
		//If there are no nonpruned possibilities, check that the last item is an actual item, and add a comma if so.
		if(possibleBlocks.size() == 0 && Material.getMaterial(argument.split(",")[argument.split(",").length - 1].toUpperCase()) != null) {
			if(!argument.endsWith(",")) {
				possibleBlocks.add(argument + ",");
			}else {
				for(Material mat : Material.values()) {
					if(mat.isBlock())
						possibleBlocks.add((argument.concat(mat.toString().toLowerCase())).replaceAll("minecraft:", ""));
				}
			}
		//If there is a bracket, then the user is likely trying to add special data, just resuggest what they have input so they know it is proper.
		}else if(possibleBlocks.size() == 0 && argument.split(",")[argument.split(",").length - 1].contains("[")) {
			possibleBlocks.add(argument);
		}
			
		
		return possibleBlocks;
	}
}
