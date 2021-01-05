package me.desertdweller.bettertools.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import de.tr7zw.nbtapi.NBTItem;
import me.desertdweller.bettertools.math.BlockMath;
import me.desertdweller.bettertools.math.Noise;
import me.desertdweller.bettertools.undo.Alteration;
import me.desertdweller.bettertools.undo.ChangeTracker;
import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener{


	@EventHandler
	public static void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getItem() == null)
			return;
		NBTItem nbti = new NBTItem(e.getItem());
		if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools") && nbti.getString("Item").equals("Paint Tool")) {
			e.setCancelled(true);
			if(!e.getPlayer().hasPermission("bt.use")) {
				e.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to use that tool.");
				return;
			}
			Block centerBlock = e.getPlayer().getTargetBlock(dataToMaterialSet(BlockMath.stringToHashMap(nbti.getString("Through"), false).keySet()), 200);
			List<Block> blocks;
			Noise noise = new Noise(nbti.getString("Noise"));
			//long startTime = System.currentTimeMillis();
			if(nbti.hasKey("Mask") && !nbti.getString("Mask").equals("empty")) {
				blocks = BlockMath.getNearbyBlocksMasked(centerBlock.getLocation(), nbti.getInteger("Radius"),BlockMath.stringToHashMap(nbti.getString("Mask"), false), noise);
			}else {
				blocks = BlockMath.getNearbyBlocks(centerBlock.getLocation(), nbti.getInteger("Radius"), noise);
			}
			if(nbti.hasKey("Touching") && !nbti.getString("Touching").equals("")) {
				blocks = BlockMath.getBlocksTouching(blocks, BlockMath.stringToHashMap(nbti.getString("Touching"), false));
			}
			//System.out.println("[BT] It took " + (System.currentTimeMillis() - startTime)/1000d + " seconds to find appropriate blocks to change.");
	        ChangeTracker tracker = ChangeTracker.getChangesForPlayer(e.getPlayer().getUniqueId());
	        if(tracker == null)
	        	tracker = new ChangeTracker(e.getPlayer().getUniqueId());
	        Alteration change = new Alteration();
			HashMap<BlockData, Boolean> matList = BlockMath.stringToHashMap(nbti.getString("Blocks"), true);
			for(int i = 0; i < blocks.size(); i++) {
				int id = (int) (Math.random()*matList.size());
				BlockData targetData = (BlockData)  matList.keySet().toArray()[id];
				if(!blocks.get(i).getBlockData().equals(targetData)) {
					change.addBlock(blocks.get(i));
					setBlockData(blocks.get(i), targetData.clone(), nbti.getBoolean("Updates"), matList.get(targetData));
					//setBlockInNativeWorld(blocks.get(i), BlockMath.materialIds.get(targetMat), false);
				}
			}
			if(change.getBlockList().keySet().size() > 0)
				tracker.addChange(change);

			//System.out.println("[BT] It took " + (System.currentTimeMillis() - startTime)/1000d + " seconds to change the blocks.");
		}
	}
	
	private static void setBlockData(Block targetBlock, BlockData targetData, boolean updates, boolean customProps){
		if(!customProps && targetBlock.getBlockData().getClass().equals(targetData.getClass())) {
			targetData = BlockMath.applyProperties(targetData, targetBlock.getBlockData());
		}
		targetBlock.setBlockData(targetData, updates);
	}
	
	private static Set<Material> dataToMaterialSet(Set<BlockData> list){
		Set<Material> output = new HashSet<Material>();
		for(BlockData data : list) {
			output.add(data.getMaterial());
		}
		return output;
	}
	
//	@SuppressWarnings("unused")
//	private static void setBlockInNativeWorld(Block block, int blockId, boolean applyPhysics) {
//	    net.minecraft.server.v1_13_R2.World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();
//	    BlockPosition bp = new BlockPosition(block.getX(), block.getY(), block.getZ());
//	    @SuppressWarnings("deprecation")
//		IBlockData ibd = net.minecraft.server.v1_13_R2.Block.getByCombinedId(blockId + (block.getData() << 12));
//	    nmsWorld.setTypeAndData(bp, ibd, applyPhysics ? 3 : 2);
//	}
}