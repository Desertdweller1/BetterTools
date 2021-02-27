package me.desertdweller.bettertools.undo;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.desertdweller.bettertools.BetterTools;

public class Alteration {
	private static BetterTools plugin = BetterTools.getPlugin(BetterTools.class);
	private HashMap<Location, OriginalBlock> blockList = new HashMap<Location, OriginalBlock>();
	
	public Alteration() {
		
	}
	
	public Alteration(HashMap<Location, OriginalBlock> blockList) {
		this.blockList = blockList;
	}
	
	public void addBlock(Block block) {
		blockList.put(block.getLocation(), new OriginalBlock(block));
	}
	
	public HashMap<Location, OriginalBlock> getBlockList(){
		return blockList;
	}
	
	public int revert(Player p) {
		int amount = 0;
		for(Location location : blockList.keySet()) {
			plugin.getCoreProtect().logRemoval(p.getName(), location, location.getBlock().getType(), location.getBlock().getBlockData());
			location.getWorld().getBlockAt(location).setBlockData(blockList.get(location).getOriginalDat(), false);
			plugin.getCoreProtect().logPlacement(p.getName(), location, location.getBlock().getType(), location.getBlock().getBlockData());
			amount++;
		}
		return amount;
	}
}
