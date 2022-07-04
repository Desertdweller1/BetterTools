package me.desertdweller.bettertools.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class StringUtils {

	// Returns the blockdata, as well as true if properties have been specifically
		// set by the player.
		public static Map<BlockData, BTBMeta> stringToHashMap(String string, boolean ratios) {
			string = setDefaultBlockData(string);
			String[] materialNames = string.split(",");
			HashMap<BlockData, BTBMeta> materialList = new HashMap<BlockData, BTBMeta>();
			for (String materialString : materialNames) {
				String customParams = null;
				if(materialString.contains("<")) {
					customParams = materialString.split("<")[1].replace("|", ",");
					materialString = materialString.split("<")[0];
					customParams = customParams.replace(">", "");
				}
				materialString = materialString.replace('|', ',');
				// If no specified additional amounts.
				if (materialString.split("%").length == 1) {
					materialList.put(Bukkit.createBlockData(materialString), new BTBMeta(materialString.contains("["), 1, customParams));
				}else if (materialString.split("%").length == 2) {
					materialList.put(Bukkit.createBlockData(materialString.split("%")[1]),
							new BTBMeta(materialString.contains("["), Integer.parseInt(materialString.split("%")[0]),
							customParams));

				}
			}
			return materialList;
		}
		
		//Replaces any properties in the list, such as '#blocks', with the list from blocks.
		public static String replaceSpecialStrings(String targetList, String blocksList) {
			return targetList.toLowerCase().replace("#blocks", blocksList);
		}
		
		private static String setDefaultBlockData(String inputString) {
			String outputString = "";

			for (String blockString : inputString.split(",")) {
				if (blockString.split("%").length == 1) {
					if (blockString.equals("oak_leaves") || blockString.equals("dark_oak_leaves")
							|| blockString.equals("spruce_leaves") || blockString.equals("jungle_leaves")
							|| blockString.equals("birch_leaves") || blockString.equals("acacia_leaves"))
						blockString = blockString + "[persistent=true]";
				} else {
					String blockdata = blockString.split("%")[1];
					if (blockdata.equals("oak_leaves") || blockdata.equals("dark_oak_leaves")
							|| blockdata.equals("spruce_leaves") || blockdata.equals("jungle_leaves")
							|| blockdata.equals("birch_leaves") || blockdata.equals("acacia_leaves"))
						blockString = blockString + "[persistent=true]";
				}
				if (outputString.equals("")) {
					outputString = blockString;
				} else {
					outputString = outputString + "," + blockString;
				}
			}
			return outputString;
		}

		public static List<String> matArrayToStringList(Material[] materials) {
			List<String> strings = new ArrayList<String>();

			for (Material mat : materials) {
				strings.add(mat.toString().toLowerCase());
			}
			return strings;
		}

		public static String checkStringList(String list, String methodName) {
			// 2%oak_stairs,spruce_stairs[facing=north|type=top],small_stone_bricks
			String[] materialNames = list.split(",");
			// 2%oak_stairs spruce_stairs[facing=north|type=top] small_stone_bricks
			for (String materialString : materialNames) {
				String originalString = materialString;
				// 2%oak_stairs spruce_stairs[facing=north,type=top] small_stone_bricks
				//Checks to make sure the custom parameters are valid, and removes them from the check.
				if(materialString.equalsIgnoreCase("#blocks"))
					continue;
				if(materialString.contains("<")) {
					if(checkCustomParams(materialString.split("<")[1], methodName)) {
						return originalString;
					}
					materialString = materialString.split("<")[0];
				}
				materialString = materialString.replace('|', ',');
				if (materialString.split("%").length == 1) {
					// 2 oak_stairs spruce_stairs[facing=north,type=top] small_stone_bricks
					try {
						Bukkit.createBlockData(materialString);
					} catch (IllegalArgumentException e) {
						return originalString;
					}
				} else if (materialString.split("%").length == 2) {
					try {
						Bukkit.createBlockData(materialString.split("%")[1]);
					} catch (IllegalArgumentException e) {
						return originalString;
					}
				}
			}
			return null;
		}
		
		private static boolean checkCustomParams(String params, String methodName) {
			switch(methodName){
			case "touching":
				params = params.toLowerCase();
				params = params.replace(">", "");
				for(String param : params.split("|")) {
					if(!param.equals("up") && !param.equals("down") && !param.equals("north") && !param.equals("south") && !param.equals("west") && !param.equals("east")) {
						return false;
					}
				}
			}
			return true;
		}
}
