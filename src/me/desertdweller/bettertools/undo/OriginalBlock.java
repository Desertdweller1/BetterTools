package me.desertdweller.bettertools.undo;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class OriginalBlock {
	private BlockData originalDat;
	
	public OriginalBlock(Block block) {
		originalDat = block.getBlockData().clone();
	}
	
	public BlockData getOriginalDat() {
		return originalDat;
	}
}
