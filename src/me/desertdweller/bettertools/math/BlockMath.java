package me.desertdweller.bettertools.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

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
    
    public static List<Block> getNearbyBlocksMasked(Location location, int radius, List<BlockData> mask, Noise perlin) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                	double distance = ((location.getBlockX()-x) * (location.getBlockX()-x) + ((location.getBlockZ()-z) * (location.getBlockZ()-z)) + ((location.getBlockY()-y) * (location.getBlockY()-y)));
                	if(distance < radius * radius && mask.contains(location.getWorld().getBlockAt(x,y,z).getBlockData()) && perlin.getPoint(x, y, z)) {
                		blocks.add(location.getWorld().getBlockAt(x, y, z));
                	}
                }
            }
        }
        return blocks;
    }

    public static List<BlockData> stringToDataList(String string, boolean ratios){
    	String[] materialNames = string.split(",");
    	List<BlockData> materialList = new ArrayList<BlockData>();
    	for(String materialString : materialNames) {
    		if(materialString.split("%").length == 1) {
        		materialList.add(Bukkit.createBlockData(materialString));
    		}else if(materialString.split("%").length == 2){
    			for(int i = 0; i < Integer.parseInt(materialString.split("%")[0]); i++) {
    				materialList.add(Bukkit.createBlockData(materialString.split("%")[1]));
    			}
    		}
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
    		if(materialString.split("%").length == 1) {
    			try {
            		Bukkit.createBlockData(materialString);
        		}catch(IllegalArgumentException e) {
        			return materialString;
        		}
    		}else if(materialString.split("%").length == 2){
    			try {
            		Bukkit.createBlockData(materialString.split("%")[1]);
        		}catch(IllegalArgumentException e) {
        			return materialString;
        		}
    		}
    	}
    	return null;
    }
}
