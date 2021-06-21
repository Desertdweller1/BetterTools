package me.desertdweller.bettertools.listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.desertdweller.bettertools.BetterTools;
import me.desertdweller.bettertools.math.BTBMeta;
import me.desertdweller.bettertools.math.BlockMath;
import me.desertdweller.bettertools.math.Noise;
import me.desertdweller.bettertools.undo.Alteration;
import me.desertdweller.bettertools.undo.ChangeTracker;
import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener{
	private static BetterTools plugin = BetterTools.getPlugin(BetterTools.class);

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
			Map<BlockData, BTBMeta> matList = BlockMath.stringToHashMap(nbti.getString("Blocks"), true);
			List<BlockData> blockList = new ArrayList<BlockData>();
			for(BlockData block : matList.keySet()) {
				for(int i = 0; i < matList.get(block).amount; i++)
					blockList.add(block);
			}
			for(int i = 0; i < blocks.size(); i++) {
				int id = (int) (Math.random()*blockList.size());
				BlockData targetData = (BlockData)  blockList.get(id);
				if(!blocks.get(i).getBlockData().equals(targetData)) {
					change.addBlock(blocks.get(i));
					plugin.getCoreProtect().logRemoval(e.getPlayer().getName(), blocks.get(i).getLocation(), blocks.get(i).getType(), blocks.get(i).getBlockData());
					if(!setBlockData(blocks.get(i), targetData.clone(), nbti.getBoolean("Updates"), matList.get(targetData).specified)) {
						e.getPlayer().sendMessage(ChatColor.RED + "The block type" + blocks.get(i).getBlockData().getClass().getSimpleName() + " was not able to correctly transfer data. This is an error. Please report it with this message and it will be fixed ASAP.");
					}
					plugin.getCoreProtect().logPlacement(e.getPlayer().getName(), blocks.get(i).getLocation(), blocks.get(i).getType(), blocks.get(i).getBlockData());
					//setBlockInNativeWorld(blocks.get(i), BlockMath.materialIds.get(targetMat), false);
				}
			}
			if(change.getBlockList().keySet().size() > 0)
				tracker.addChange(change);
			//System.out.println("[BT] It took " + (System.currentTimeMillis() - startTime)/1000d + " seconds to change the blocks.");
		}
	}
	
	@EventHandler
	public static void onPlayerItemHoldEvent(PlayerItemHeldEvent e){
		ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
		if(item == null || item.getType().equals(Material.AIR)) {
			if(e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.AIR))
				return;
			NBTItem offhand = new NBTItem(e.getPlayer().getInventory().getItemInOffHand());
			if(offhand.getItem().getType().equals(Material.FILLED_MAP) && offhand.hasKey("Plugin") && offhand.getString("Plugin").equals("BetterTools")) {
				e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
			}
			return;
		}
		NBTItem nbti = new NBTItem(item);
		if(!nbti.hasKey("Plugin") || !nbti.getString("Plugin").equals("BetterTools") || new Noise(nbti.getString("Noise")).method.equals("none")) {
			if(e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.AIR))
				return;
			NBTItem offhand = new NBTItem(e.getPlayer().getInventory().getItemInOffHand());
			if(offhand.getItem().getType().equals(Material.FILLED_MAP) && offhand.hasKey("Plugin") && offhand.getString("Plugin").equals("BetterTools")) {
				e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
			}
			return;
		}
		if(item.getType().equals(Material.FILLED_MAP)) {
			e.getPlayer().getInventory().setItem(e.getNewSlot(), null);
		}else if(!new Noise(nbti.getString("Noise")).method.equals("none")) {
			BlockMath.givePlayerNoiseMap(e.getPlayer());
		}
	}

	@EventHandler
	public static void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if(e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.AIR))
			return;
		NBTItem offhand = new NBTItem(e.getPlayer().getInventory().getItemInOffHand());
		if(offhand.getItem().getType().equals(Material.FILLED_MAP) && offhand.hasKey("Plugin") && offhand.getString("Plugin").equals("BetterTools")) {
			if(e.getPlayer().getInventory().getItemInMainHand().equals(null))
				e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public static void onInventoryClickEvent(InventoryClickEvent e) {
		if(e.getCurrentItem().getType().equals(Material.AIR))
			return;
		NBTItem nbti = new NBTItem(e.getCurrentItem());
		if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools") && e.getCurrentItem().getType().equals(Material.FILLED_MAP)) {
			System.out.println(e.getSlot());
			e.setCancelled(true);
			e.getClickedInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
			e.setCursor(new ItemStack(Material.AIR));
		}
	}
	
	public static void onPlayerSwapHandsEvent(PlayerSwapHandItemsEvent e) {
		if(e.getOffHandItem().getType().equals(Material.AIR))
			return;
		NBTItem nbti = new NBTItem(e.getOffHandItem());
		if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools") && e.getOffHandItem().getType().equals(Material.FILLED_MAP)) {
			e.setOffHandItem(new ItemStack(Material.AIR));
		}
	}
	
	private static boolean setBlockData(Block targetBlock, BlockData targetData, boolean updates, boolean customProps){
		//If the blocks are of the same type, (ie both walls), then transfer data from the previous block to the other.
		if(!customProps && targetBlock.getBlockData().getClass().equals(targetData.getClass())) {
			targetData = BlockMath.applyProperties(targetData, targetBlock.getBlockData());
		}
		//This would be null if there was an error in trying to transfer data from one block to another.
		if(targetData == null)
			return false;
		targetBlock.setBlockData(targetData, updates);
		return true;
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