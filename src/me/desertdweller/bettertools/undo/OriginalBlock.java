package me.desertdweller.bettertools.undo;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class OriginalBlock {
	private Material originalMat;
	
	public OriginalBlock(Block block) {
		originalMat = block.getType();
	}
	
	public Material getOriginalMat() {
		return originalMat;
	}
}
