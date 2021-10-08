package me.desertdweller.bettertools.listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

			Map<BlockData, BTBMeta> matList = BlockMath.stringToHashMap(nbti.getString("Blocks"), true);
			
			setBlocksInArea(blocks, getBlockList(matList), e.getPlayer(), matList, nbti.getBoolean("Updates"));
		}
	}
	
	private static List<BlockData> getBlockList(Map<BlockData, BTBMeta> matList){
		List<BlockData> blockList = new ArrayList<BlockData>();
		for(BlockData block : matList.keySet()) {
			for(int i = 0; i < matList.get(block).amount; i++)
				blockList.add(block);
		}
		return blockList;
	}
	
	private static void setBlocksInArea(List<Block> area, List<BlockData> blockList, Player p, Map<BlockData, BTBMeta> matList, boolean blockUpdates) {
        Alteration change = new Alteration();
		for(int i = 0; i < area.size(); i++) {
			int id = (int) (Math.random()*blockList.size());
			BlockData targetData = (BlockData)  blockList.get(id);
			if(!area.get(i).getBlockData().equals(targetData)) {
				change.addBlock(area.get(i));
				plugin.getCoreProtect().logRemoval(p.getName(), area.get(i).getLocation(), area.get(i).getType(), area.get(i).getBlockData());
				if(!setBlockData(area.get(i), targetData.clone(), blockUpdates, matList.get(targetData).specified)) {
					p.sendMessage(ChatColor.RED + "The block type " + area.get(i).getBlockData().getClass().getSimpleName() + " was not able to correctly transfer data. This is an error. Please report it with this message and it will be fixed ASAP.");
				}
				plugin.getCoreProtect().logPlacement(p.getName(), area.get(i).getLocation(), area.get(i).getType(), area.get(i).getBlockData());
			}
		}
		
		ChangeTracker tracker = ChangeTracker.getChangesForPlayer(p.getUniqueId());
        if(tracker == null)
        	tracker = new ChangeTracker(p.getUniqueId());
		
		if(change.getBlockList().keySet().size() > 0)
			tracker.addChange(change);
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
	
	@EventHandler
	public static void onPlayerItemHoldEvent(PlayerItemHeldEvent e){
		ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
		if(item == null || item.getType() == Material.AIR) {
			if(e.getPlayer().getInventory().getItemInOffHand().getType() == Material.AIR)
				return;
			NBTItem offhand = new NBTItem(e.getPlayer().getInventory().getItemInOffHand());
			if(offhand.getItem().getType() == Material.FILLED_MAP && offhand.hasKey("Plugin") && offhand.getString("Plugin").equals("BetterTools")) {
				e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
			}
			return;
		}
		NBTItem nbti = new NBTItem(item);
		if(!nbti.hasKey("Plugin") || !nbti.getString("Plugin").equals("BetterTools") || new Noise(nbti.getString("Noise")).method.equals("none")) {
			if(e.getPlayer().getInventory().getItemInOffHand().getType() == Material.AIR)
				return;
			NBTItem offhand = new NBTItem(e.getPlayer().getInventory().getItemInOffHand());
			if(offhand.getItem().getType() == Material.FILLED_MAP && offhand.hasKey("Plugin") && offhand.getString("Plugin").equals("BetterTools")) {
				e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
			}
			return;
		}
		if(item.getType() == Material.FILLED_MAP) {
			e.getPlayer().getInventory().setItem(e.getNewSlot(), null);
		}else if(!new Noise(nbti.getString("Noise")).method.equals("none")) {
			BlockMath.givePlayerNoiseMap(e.getPlayer());
		}
	}

	@EventHandler
	public static void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		if(e.getPlayer().getInventory().getItemInOffHand().getType() == Material.AIR)
			return;
		NBTItem offhand = new NBTItem(e.getPlayer().getInventory().getItemInOffHand());
		if(offhand.getItem().getType() == Material.FILLED_MAP && offhand.hasKey("Plugin") && offhand.getString("Plugin").equals("BetterTools")) {
			if(e.getPlayer().getInventory().getItemInMainHand().equals(null))
				e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public static void onInventoryClickEvent(InventoryClickEvent e) {
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
			return;
		NBTItem nbti = new NBTItem(e.getCurrentItem());
		if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools") && e.getCurrentItem().getType() == Material.FILLED_MAP) {
			e.setCancelled(true);
			e.getClickedInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
			e.setCursor(new ItemStack(Material.AIR));
		}
	}
	
	@EventHandler
	public static void onPlayerSwapHandsEvent(PlayerSwapHandItemsEvent e) {
		if(e.getOffHandItem().getType() == Material.AIR)
			return;
		NBTItem nbti = new NBTItem(e.getOffHandItem());
		if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools") && e.getOffHandItem().getType() == Material.FILLED_MAP) {
			e.setOffHandItem(new ItemStack(Material.AIR));
		}
	}
	
	@EventHandler
	public static void onFarmlandTrampleEvent(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.PHYSICAL) && e.hasBlock() && e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && e.getClickedBlock().getType() == Material.FARMLAND) {
			e.setCancelled(true);
		}
	}
	
	private static Set<Material> dataToMaterialSet(Set<BlockData> list){
		Set<Material> output = new HashSet<Material>();
		for(BlockData data : list) {
			output.add(data.getMaterial());
		}
		return output;
	}
}