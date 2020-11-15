package me.desertdweller.bettertools.listeners;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
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
			Block centerBlock = e.getPlayer().getTargetBlock(matListToSet(BlockMath.stringToMaterialList(nbti.getString("Through"))), 200);
			List<Block> blocks;
			Noise noise = new Noise(nbti.getString("Noise"));
			//long startTime = System.currentTimeMillis();
			if(nbti.hasKey("Mask") && !nbti.getString("Mask").equals("empty")) {
				blocks = BlockMath.getNearbyBlocksMasked(centerBlock.getLocation(), nbti.getInteger("Radius"), BlockMath.stringToMaterialList(nbti.getString("Mask")), noise);
			}else {
				blocks = BlockMath.getNearbyBlocks(centerBlock.getLocation(), nbti.getInteger("Radius"), noise);
			}
			//System.out.println("[BT] It took " + (System.currentTimeMillis() - startTime)/1000d + " seconds to find appropriate blocks to change.");
	        ChangeTracker tracker = ChangeTracker.getChangesForPlayer(e.getPlayer().getUniqueId());
	        if(tracker == null)
	        	tracker = new ChangeTracker(e.getPlayer().getUniqueId());
	        Alteration change = new Alteration();
	        //startTime = System.currentTimeMillis();
			List<Material> matList = BlockMath.stringToMaterialList(nbti.getString("Blocks"));
			for(int i = 0; i < blocks.size(); i++) {
				Material targetMat = matList.get((int) (Math.random()*matList.size()));
				if(!blocks.get(i).getType().equals(targetMat)) {
					change.addBlock(blocks.get(i));	
					blocks.get(i).setType(targetMat, false);
					//setBlockInNativeWorld(blocks.get(i), BlockMath.materialIds.get(targetMat), false);
				}
			}
			if(change.getBlockList().keySet().size() > 0)
				tracker.addChange(change);

			//System.out.println("[BT] It took " + (System.currentTimeMillis() - startTime)/1000d + " seconds to change the blocks.");
		}
	}
	
	private static Set<Material> matListToSet(List<Material> list){
		Set<Material> output = new HashSet<Material>();
		for(Material mat : list) {
			output.add(mat);
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