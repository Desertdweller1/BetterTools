package me.desertdweller.bettertools.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockMath {
	public static HashMap<Material, Integer> materialIds = new HashMap<Material, Integer>();

	
    public static List<Block> getNearbyBlocks(Location location, int radius, Noise perlin) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {

                	double distance = ((location.getBlockX()-x) * (location.getBlockX()-x) + ((location.getBlockZ()-z) * (location.getBlockZ()-z)) + ((location.getBlockY()-y) * (location.getBlockY()-y)));
                	if(distance < radius * radius && perlin.getPoint(x, y, z)) {
                		blocks.add(location.getWorld().getBlockAt(x, y, z));
                	}
                }
            }
        }
        return blocks;
    }
    
    public static List<Block> getNearbyBlocksMasked(Location location, int radius, List<Material> mask, Noise perlin) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                	double distance = ((location.getBlockX()-x) * (location.getBlockX()-x) + ((location.getBlockZ()-z) * (location.getBlockZ()-z)) + ((location.getBlockY()-y) * (location.getBlockY()-y)));
                	if(distance < radius * radius && mask.contains(location.getWorld().getBlockAt(x,y,z).getType()) && perlin.getPoint(x, y, z)) {
                		blocks.add(location.getWorld().getBlockAt(x, y, z));
                	}
                }
            }
        }
        return blocks;
    }
    
    public static List<Material> stringToMaterialList(String string){
    	String[] materialNames = string.split(",");
    	List<Material> materialList = new ArrayList<Material>();
    	for(String materialString : materialNames) {
    		materialList.add(Material.getMaterial(materialString.toUpperCase()));
    	}
    	return materialList;
    }
    
    public static void initMaterialIds() {
    	int id = 0;
    	for(Material material : Material.values()) {
    		materialIds.put(material, id);
    		id++;
    	}
    }
    
    public static String checkStringList(String list) {
    	String[] materialNames = list.split(",");
    	for(String materialString : materialNames) {
    		boolean found = false;
    		for(Material mat : Material.values()) {
    			if(!found && mat.name().equals(materialString.toUpperCase()))
    				found = true;
    		}
    		if(!found)
    			return materialString;
    	}
    	return null;
    }
}
