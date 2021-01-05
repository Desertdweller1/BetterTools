package me.desertdweller.bettertools.undo;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Alteration {
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
	
	public int revert() {
		int amount = 0;
		for(Location location : blockList.keySet()) {
			location.getWorld().getBlockAt(location).setBlockData(blockList.get(location).getOriginalDat(), false);
			amount++;
		}
		return amount;
	}
}
