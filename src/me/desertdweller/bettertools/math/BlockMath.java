package me.desertdweller.bettertools.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Attachable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Snowable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.EnderChest;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Fire;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.GlassPane;
import org.bukkit.block.data.type.Grindstone;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.Lectern;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Observer;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.block.data.type.RedstoneWallTorch;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.block.data.type.Scaffolding;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.TNT;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.block.data.type.TripwireHook;
import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.block.data.type.Wall;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import com.ags.simpleblocks.objects.SimpleBlock;

import de.tr7zw.nbtapi.NBTItem;
import me.desertdweller.bettertools.BetterTools;
import me.desertdweller.bettertools.Renderer;

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
    
    public static List<Block> getNearbyBlocksMasked(Location location, int radius, Map<BlockData, BTBMeta> mask, Noise noise) {
        List<Block> blocks = new ArrayList<Block>();
        List<Material> nonCustomMats = getNonCustomMaterials(mask);
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                	double distance = ((location.getBlockX()-x) * (location.getBlockX()-x) + ((location.getBlockZ()-z) * (location.getBlockZ()-z)) + ((location.getBlockY()-y) * (location.getBlockY()-y)));
                	if(distance < radius * radius) {
                		if(nonCustomMats.contains(location.getWorld().getBlockAt(x,y,z).getType())) {
                			if(noise.getPoint(x, y, z))
                				blocks.add(location.getWorld().getBlockAt(x, y, z));
                		}else if(mask.containsKey(location.getWorld().getBlockAt(x,y,z).getBlockData()) && noise.getPoint(x, y, z)) {
                    		blocks.add(location.getWorld().getBlockAt(x, y, z));
                		}
                	}
                }
            }
        }
        return blocks;
    }
    
    public static List<Block> getBlocksTouching(List<Block> inputBlocks, Map<BlockData, BTBMeta> touchLimits){
        List<Material> nonCustomMats = getNonCustomMaterials(touchLimits);
    	List<Block> outputBlocks = new ArrayList<Block>();
    	for(Block block : inputBlocks) {
    		if(nonCustomMats.contains(block.getRelative(1, 0, 0).getType())) {
    			outputBlocks.add(block);
    		}else if(touchLimits.containsKey(block.getRelative(1, 0, 0).getBlockData())){
    			outputBlocks.add(block);
    		}else if(nonCustomMats.contains(block.getRelative(-1, 0, 0).getType())) {
    			outputBlocks.add(block);
    		}else if(touchLimits.containsKey(block.getRelative(-1, 0, 0).getBlockData())){
    			outputBlocks.add(block);
    		}else if(nonCustomMats.contains(block.getRelative(0, 1, 0).getType())) {
    			outputBlocks.add(block);
    		}else if(touchLimits.containsKey(block.getRelative(0, 1, 0).getBlockData())){
    			outputBlocks.add(block);
    		}else if(nonCustomMats.contains(block.getRelative(0, -1, 0).getType())) {
    			outputBlocks.add(block);
    		}else if(touchLimits.containsKey(block.getRelative(0, -1, 0).getBlockData())){
    			outputBlocks.add(block);
    		}else if(nonCustomMats.contains(block.getRelative(0, 0, 1).getType())) {
    			outputBlocks.add(block);
    		}else if(touchLimits.containsKey(block.getRelative(0, 0, 1).getBlockData())){
    			outputBlocks.add(block);
    		}else if(nonCustomMats.contains(block.getRelative(0, 0, -1).getType())) {
    			outputBlocks.add(block);
    		}else if(touchLimits.containsKey(block.getRelative(0, 0, -1).getBlockData())){
    			outputBlocks.add(block);
    		}
    	}
    	return outputBlocks;
    }
    
    private static List<Material> getNonCustomMaterials(Map<BlockData, BTBMeta> map){
    	ArrayList<Material> materials = new ArrayList<Material>();
    	for(BlockData block : map.keySet()) {
    		if(!map.get(block).specified)
    			materials.add(block.getMaterial());
    	}
    	return materials;
    }
    
    public static void givePlayerNoiseMap(Player p) {
    	if(!p.getInventory().getItemInOffHand().getType().equals(Material.AIR))
    		return;
    	ItemStack map = new ItemStack(Material.FILLED_MAP);
    	MapMeta meta = (MapMeta) map.getItemMeta();
    	
    	MapView mapView = Bukkit.createMap(p.getWorld());
		mapView.setUnlimitedTracking(false);
		mapView.getRenderers().clear();
		mapView.addRenderer(new Renderer());
    	
    	meta.setMapView(mapView);
    	map.setItemMeta(meta);
    	NBTItem nbti = new NBTItem(map);
    	nbti.setString("Plugin", "BetterTools");
    	
    	p.getInventory().setItemInOffHand(nbti.getItem());
    }

    public static BlockData applyProperties(BlockData target, BlockData properties) {
    	CLAZZ z = CLAZZ.valueOf(target.getClass().getSimpleName());
    	switch(z) {
    	case CraftStairs:
    		Stairs targetStairs = (Stairs) target;
    		Stairs propertyStairs = (Stairs) properties;
    		targetStairs.setFacing(propertyStairs.getFacing());
    		targetStairs.setHalf(propertyStairs.getHalf());
    		targetStairs.setShape(propertyStairs.getShape());
    		targetStairs.setWaterlogged(propertyStairs.isWaterlogged());
    		return targetStairs;
    	case CraftAgeable:
    		Ageable targetAgeable = (Ageable) target;
    		Ageable propertyAgeable = (Ageable) properties;
    		targetAgeable.setAge(propertyAgeable.getAge());
    		return targetAgeable;
    	case CraftAnaloguePowerable:
    		AnaloguePowerable targetAnaloguePowerable = (AnaloguePowerable) target;
    		AnaloguePowerable propertyAnaloguePowerable = (AnaloguePowerable) properties;
    		targetAnaloguePowerable.setPower(propertyAnaloguePowerable.getPower());
    		return targetAnaloguePowerable;
    	case CraftAttachable:
    		Attachable targetAttachable = (Attachable) target;
    		Attachable propertyAttachable = (Attachable) properties;
    		targetAttachable.setAttached(propertyAttachable.isAttached());
    		return targetAttachable;
    	case CraftBamboo:
    		Bamboo targetBamboo = (Bamboo) target;
    		Bamboo propertyBamboo = (Bamboo) properties;
    		targetBamboo.setLeaves(propertyBamboo.getLeaves());
    		targetBamboo.setAge(propertyBamboo.getAge());
    		targetBamboo.setStage(propertyBamboo.getStage());
    		return targetBamboo;
    	case CraftBeehive:
    		Beehive targetBeehive = (Beehive) target;
    		Beehive propertyBeehive = (Beehive) properties;
    		targetBeehive.setFacing(propertyBeehive.getFacing());
    		targetBeehive.setHoneyLevel(propertyBeehive.getHoneyLevel());
    		return targetBeehive;
    	case CraftBell:
    		Bell targetBell = (Bell) target;
    		Bell propertyBell = (Bell) properties;
    		targetBell.setAttachment(propertyBell.getAttachment());
    		targetBell.setFacing(propertyBell.getFacing());
    		targetBell.setPowered(propertyBell.isPowered());
    		return targetBell;
    	case CraftBisected:
    		Bisected targetBisected = (Bisected) target;
    		Bisected propertyBisected = (Bisected) properties;
    		targetBisected.setHalf(propertyBisected.getHalf());
    		return targetBisected;
    	case CraftBlockData:
    		BlockData targetBlockData = (BlockData) target;
    		return targetBlockData;
    	case CraftBrewingStand:
    		BrewingStand targetBrewingStand = (BrewingStand) target;
    		BrewingStand propertyBrewingStand = (BrewingStand) properties;
    		for(int i : propertyBrewingStand.getBottles()) {
        		targetBrewingStand.setBottle(i, true);
    		}
    		return targetBrewingStand;
    	case CraftBubbleColumn:
    		BubbleColumn targetBubbleColumn = (BubbleColumn) target;
    		BubbleColumn propertyBubbleColumn = (BubbleColumn) properties;
    		targetBubbleColumn.setDrag(propertyBubbleColumn.isDrag());
    		return targetBubbleColumn;
    	case CraftCake:
    		Cake targetCake = (Cake) target;
    		Cake propertyCake = (Cake) properties;
    		targetCake.setBites(propertyCake.getBites());
    		return targetCake;
    	case CraftCampfire:
    		Campfire targetCampfire = (Campfire) target;
    		Campfire propertyCampfire = (Campfire) properties;
    		targetCampfire.setFacing(propertyCampfire.getFacing());
    		targetCampfire.setLit(propertyCampfire.isLit());
    		targetCampfire.setSignalFire(propertyCampfire.isSignalFire());
    		targetCampfire.setWaterlogged(propertyCampfire.isWaterlogged());
    		return targetCampfire;
    	case CraftChest:
    		Chest targetChest = (Chest) target;
    		Chest propertyChest = (Chest) properties;
    		targetChest.setFacing(propertyChest.getFacing());
    		targetChest.setType(propertyChest.getType());
    		targetChest.setWaterlogged(propertyChest.isWaterlogged());
    		return targetChest;
    	case CraftCocoa:
    		Cocoa targetCocoa = (Cocoa) target;
    		Cocoa propertyCocoa = (Cocoa) properties;
    		targetCocoa.setAge(propertyCocoa.getAge());
    		targetCocoa.setFacing(propertyCocoa.getFacing());
    		return targetCocoa;
    	case CraftCommandBlock:
    		CommandBlock targetCommandBlock = (CommandBlock) target;
    		CommandBlock propertyCommandBlock = (CommandBlock) properties;
    		targetCommandBlock.setConditional(propertyCommandBlock.isConditional());
    		targetCommandBlock.setFacing(propertyCommandBlock.getFacing());
    		return targetCommandBlock;
    	case CraftComparator:
    		Comparator targetComparator = (Comparator) target;
    		Comparator propertyComparator = (Comparator) properties;
    		targetComparator.setFacing(propertyComparator.getFacing());
    		targetComparator.setMode(propertyComparator.getMode());
    		targetComparator.setPowered(propertyComparator.isPowered());
    		return targetComparator;
    	case CraftCoralWallFan:
    		CoralWallFan targetCoralWallFan = (CoralWallFan) target;
    		CoralWallFan propertyCoralWallFan = (CoralWallFan) properties;
    		targetCoralWallFan.setFacing(propertyCoralWallFan.getFacing());
    		targetCoralWallFan.setWaterlogged(propertyCoralWallFan.isWaterlogged());
    		return targetCoralWallFan;
    	case CraftDaylightDetector:
    		DaylightDetector targetDaylightDetector = (DaylightDetector) target;
    		DaylightDetector propertyDaylightDetector = (DaylightDetector) properties;
    		targetDaylightDetector.setInverted(propertyDaylightDetector.isInverted());
    		targetDaylightDetector.setPower(propertyDaylightDetector.getPower());
    		return targetDaylightDetector;
    	case CraftDirectional:
    		Directional targetDirectional = (Directional) target;
    		Directional propertyDirectional = (Directional) properties;
    		targetDirectional.setFacing(propertyDirectional.getFacing());
    		return targetDirectional;
    	case CraftDispenser:
    		Dispenser targetDispenser = (Dispenser) target;
    		Dispenser propertyDispenser = (Dispenser) properties;
    		targetDispenser.setFacing(propertyDispenser.getFacing());
    		targetDispenser.setTriggered(propertyDispenser.isTriggered());
    		return targetDispenser;
    	case CraftDoor:
    		Door targetDoor = (Door) target;
    		Door propertyDoor = (Door) properties;
    		targetDoor.setFacing(propertyDoor.getFacing());
    		targetDoor.setHalf(propertyDoor.getHalf());
    		targetDoor.setHinge(propertyDoor.getHinge());
    		targetDoor.setOpen(propertyDoor.isOpen());
    		targetDoor.setPowered(propertyDoor.isPowered());
    		return targetDoor;
    	case CraftEnderChest:
    		EnderChest targetEnderChest = (EnderChest) target;
    		EnderChest propertyEnderChest = (EnderChest) properties;
    		targetEnderChest.setFacing(propertyEnderChest.getFacing());
    		targetEnderChest.setWaterlogged(propertyEnderChest.isWaterlogged());
    		return targetEnderChest;
    	case CraftEndPortalFrame:
    		EndPortalFrame targetEndPortalFrame = (EndPortalFrame) target;
    		EndPortalFrame propertyEndPortalFrame = (EndPortalFrame) properties;
    		targetEndPortalFrame.setEye(propertyEndPortalFrame.hasEye());
    		targetEndPortalFrame.setFacing(propertyEndPortalFrame.getFacing());
    		return targetEndPortalFrame;
    	case CraftFaceAttachable:
    		FaceAttachable targetFaceAttachable = (FaceAttachable) target;
    		FaceAttachable propertyFaceAttachable = (FaceAttachable) properties;
    		targetFaceAttachable.setAttachedFace(propertyFaceAttachable.getAttachedFace());
    		return targetFaceAttachable;
    	case CraftFarmland:
    		Farmland targetFarmland = (Farmland) target;
    		Farmland propertyFarmland = (Farmland) properties;
    		targetFarmland.setMoisture(propertyFarmland.getMoisture());
    		return targetFarmland;
    	case CraftFence:
    		Fence targetFence = (Fence) target;
    		Fence propertyFence = (Fence) properties;
    		for(BlockFace face : propertyFence.getFaces())
    			targetFence.setFace(face, true);
    		targetFence.setWaterlogged(propertyFence.isWaterlogged());
    		return targetFence;
    	case CraftFire:
    		Fire targetFire = (Fire) target;
    		Fire propertyFire = (Fire) properties;
    		targetFire.setAge(propertyFire.getAge());
    		for(BlockFace face : propertyFire.getFaces())
    			targetFire.setFace(face, true);
    		return targetFire;
    	case CraftFurnaceFurace:
    		Furnace targetFurnace = (Furnace) target;
    		Furnace propertyFurnace = (Furnace) properties;
    		targetFurnace.setFacing(propertyFurnace.getFacing());
    		targetFurnace.setLit(propertyFurnace.isLit());
    		return targetFurnace;
    	case CraftGate:
    		Gate targetGate = (Gate) target;
    		Gate propertyGate = (Gate) properties;
    		targetGate.setFacing(propertyGate.getFacing());
    		targetGate.setInWall(propertyGate.isInWall());
    		targetGate.setOpen(propertyGate.isOpen());
    		targetGate.setPowered(propertyGate.isPowered());
    		return targetGate;
    	case CraftGlassPane:
    		GlassPane targetGlassPane = (GlassPane) target;
    		GlassPane propertyGlassPane = (GlassPane) properties;
    		for(BlockFace face : propertyGlassPane.getFaces())
    			targetGlassPane.setFace(face, true);
    		targetGlassPane.setWaterlogged(propertyGlassPane.isWaterlogged());
    		return targetGlassPane;
    	case CraftGrindstone:
    		Grindstone targetGrindstone = (Grindstone) target;
    		Grindstone propertyGrindstone = (Grindstone) properties;
    		targetGrindstone.setAttachedFace(propertyGrindstone.getAttachedFace());
    		targetGrindstone.setFacing(propertyGrindstone.getFacing());
    		return targetGrindstone;
    	case CraftHopper:
    		Hopper targetHopper = (Hopper) target;
    		Hopper propertyHopper = (Hopper) properties;
    		targetHopper.setEnabled(propertyHopper.isEnabled());
    		targetHopper.setFacing(propertyHopper.getFacing());
    		return targetHopper;
    	case CraftJigsaw:
    		Jigsaw targetJigsaw = (Jigsaw) target;
    		Jigsaw propertyJigsaw = (Jigsaw) properties;
    		targetJigsaw.setOrientation(propertyJigsaw.getOrientation());
    		return targetJigsaw;
    	case CraftLadder:
    		Ladder targetLadder = (Ladder) target;
    		Ladder propertyLadder = (Ladder) properties;
    		targetLadder.setFacing(propertyLadder.getFacing());
    		targetLadder.setWaterlogged(propertyLadder.isWaterlogged());
    		return targetLadder;
    	case CraftLantern:
    		Lantern targetLantern = (Lantern) target;
    		Lantern propertyLantern = (Lantern) properties;
    		targetLantern.setHanging(propertyLantern.isHanging());
    		return targetLantern;
    	case CraftLeaves:
    		Leaves targetLeaves = (Leaves) target;
    		Leaves propertyLeaves = (Leaves) properties;
    		targetLeaves.setDistance(propertyLeaves.getDistance());
    		targetLeaves.setPersistent(propertyLeaves.isPersistent());
    		return targetLeaves;
    	case CraftLectern:
    		Lectern targetLectern = (Lectern) target;
    		Lectern propertyLectern = (Lectern) properties;
    		targetLectern.setFacing(propertyLectern.getFacing());
    		targetLectern.setPowered(propertyLectern.isPowered());
    		return targetLectern;
    	case CraftLevelled:
    		Levelled targetLevelled = (Levelled) target;
    		Levelled propertyLevelled = (Levelled) properties;
    		targetLevelled.setLevel(propertyLevelled.getLevel());
    		return targetLevelled;
    	case CraftLightable:
    		Lightable targetLightable = (Lightable) target;
    		Lightable propertyLightable = (Lightable) properties;
    		targetLightable.setLit(propertyLightable.isLit());
    		return targetLightable;
    	case CraftMultipleFacing:
    		MultipleFacing targetMultipleFacing = (MultipleFacing) target;
    		MultipleFacing propertyMultipleFacing = (MultipleFacing) properties;
    		for(BlockFace face : propertyMultipleFacing.getFaces())
    			targetMultipleFacing.setFace(face, true);
    		return targetMultipleFacing;
    	case CraftNoteBlock:
    		NoteBlock targetNoteBlock = (NoteBlock) target;
    		NoteBlock propertyNoteBlock = (NoteBlock) properties;
    		targetNoteBlock.setInstrument(propertyNoteBlock.getInstrument());
    		targetNoteBlock.setNote(propertyNoteBlock.getNote());
    		targetNoteBlock.setPowered(propertyNoteBlock.isPowered());
    		return targetNoteBlock;
    	case CraftObserver:
    		Observer targetObserver = (Observer) target;
    		Observer propertyObserver = (Observer) properties;
    		targetObserver.setFacing(propertyObserver.getFacing());
    		targetObserver.setPowered(propertyObserver.isPowered());
    		return targetObserver;
    	case CraftOpenable:
    		Openable targetOpenable = (Openable) target;
    		Openable propertyOpenable = (Openable) properties;
    		targetOpenable.setOpen(propertyOpenable.isOpen());
    		return targetOpenable;
    	case CraftOrientable:
    		Orientable targetOrientable = (Orientable) target;
    		Orientable propertyOrientable = (Orientable) properties;
    		targetOrientable.setAxis(propertyOrientable.getAxis());
    		return targetOrientable;
    	case CraftPiston:
    		Piston targetPiston = (Piston) target;
    		Piston propertyPiston = (Piston) properties;
    		targetPiston.setExtended(propertyPiston.isExtended());
    		targetPiston.setFacing(propertyPiston.getFacing());
    		return targetPiston;
    	case CraftPistonHead:
    		PistonHead targetPistonHead = (PistonHead) target;
    		PistonHead propertyPistonHead = (PistonHead) properties;
    		targetPistonHead.setFacing(propertyPistonHead.getFacing());
    		targetPistonHead.setShort(propertyPistonHead.isShort());
    		targetPistonHead.setType(propertyPistonHead.getType());
    		return targetPistonHead;
    	case CraftPowerable:
    		Powerable targetPowerable = (Powerable) target;
    		Powerable propertyPowerable = (Powerable) properties;
    		targetPowerable.setPowered(propertyPowerable.isPowered());
    		return targetPowerable;
    	case CraftRail:
    		Rail targetRail = (Rail) target;
    		Rail propertyRail = (Rail) properties;
    		targetRail.setShape(propertyRail.getShape());
    		return targetRail;
    	case CraftRedstoneRail:
    		RedstoneRail targetRedstoneRail = (RedstoneRail) target;
    		RedstoneRail propertyRedstoneRail = (RedstoneRail) properties;
    		targetRedstoneRail.setPowered(propertyRedstoneRail.isPowered());
    		targetRedstoneRail.setShape(propertyRedstoneRail.getShape());
    		return targetRedstoneRail;
    	case CraftRedstoneWallTorch:
    		RedstoneWallTorch targetRedstoneWallTorch = (RedstoneWallTorch) target;
    		RedstoneWallTorch propertyRedstoneWallTorch = (RedstoneWallTorch) properties;
    		targetRedstoneWallTorch.setFacing(propertyRedstoneWallTorch.getFacing());
    		targetRedstoneWallTorch.setLit(propertyRedstoneWallTorch.isLit());
    		return targetRedstoneWallTorch;
    	case CraftRedstoneWire:
    		RedstoneWire targetRedstoneWire = (RedstoneWire) target;
    		RedstoneWire propertyRedstoneWire = (RedstoneWire) properties;
    		targetRedstoneWire.setPower(propertyRedstoneWire.getPower());
    		for(BlockFace face : propertyRedstoneWire.getAllowedFaces())
    			targetRedstoneWire.setFace(face, propertyRedstoneWire.getFace(face));
    		return targetRedstoneWire;
    	case CraftRepeater:
    		Repeater targetRepeater = (Repeater) target;
    		Repeater propertyRepeater = (Repeater) properties;
    		targetRepeater.setDelay(propertyRepeater.getDelay());
    		targetRepeater.setFacing(propertyRepeater.getFacing());
    		targetRepeater.setLocked(propertyRepeater.isLocked());
    		targetRepeater.setPowered(propertyRepeater.isPowered());
    		return targetRepeater;
    	case CraftRespawnAnchor:
    		RespawnAnchor targetRespawnAnchor = (RespawnAnchor) target;
    		RespawnAnchor propertyRespawnAnchor = (RespawnAnchor) properties;
    		targetRespawnAnchor.setCharges(propertyRespawnAnchor.getCharges());
    		return targetRespawnAnchor;
    	case CraftRotatable:
    		Rotatable targetRotatable = (Rotatable) target;
    		Rotatable propertyRotatable = (Rotatable) properties;
    		targetRotatable.setRotation(propertyRotatable.getRotation());
    		return targetRotatable;
    	case CraftSapling:
    		Sapling targetSapling = (Sapling) target;
    		Sapling propertySapling = (Sapling) properties;
    		targetSapling.setStage(propertySapling.getStage());
    		return targetSapling;
    	case CraftScaffolding:
    		Scaffolding targetScaffolding = (Scaffolding) target;
    		Scaffolding propertyScaffolding = (Scaffolding) properties;
    		targetScaffolding.setBottom(propertyScaffolding.isBottom());
    		targetScaffolding.setDistance(propertyScaffolding.getDistance());
    		targetScaffolding.setWaterlogged(propertyScaffolding.isWaterlogged());
    		return targetScaffolding;
    	case CraftSeaPickle:
    		SeaPickle targetSeaPickle = (SeaPickle) target;
    		SeaPickle propertySeaPickle = (SeaPickle) properties;
    		targetSeaPickle.setPickles(propertySeaPickle.getPickles());
    		targetSeaPickle.setWaterlogged(propertySeaPickle.isWaterlogged());
    		return targetSeaPickle;
    	case CraftSign:
    		Sign targetSign = (Sign) target;
    		Sign propertySign = (Sign) properties;
    		targetSign.setRotation(propertySign.getRotation());
    		targetSign.setWaterlogged(propertySign.isWaterlogged());
    		return targetSign;
    	case CraftStepAbstract:
    		Slab targetSlab = (Slab) target;
    		Slab propertySlab = (Slab) properties;
    		targetSlab.setType(propertySlab.getType());
    		targetSlab.setWaterlogged(propertySlab.isWaterlogged());
    		return targetSlab;
    	case CraftSnow:
    		Snow targetSnow = (Snow) target;
    		Snow propertySnow = (Snow) properties;
    		targetSnow.setLayers(propertySnow.getLayers());
    		return targetSnow;
    	case CraftSnowable:
    		Snowable targetSnowable = (Snowable) target;
    		Snowable propertySnowable = (Snowable) properties;
    		targetSnowable.setSnowy(propertySnowable.isSnowy());
    		return targetSnowable;
    	case CraftStructureBlock:
    		StructureBlock targetStructureBlock = (StructureBlock) target;
    		StructureBlock propertyStructureBlock = (StructureBlock) properties;
    		targetStructureBlock.setMode(propertyStructureBlock.getMode());
    		return targetStructureBlock;
    	case CraftSwitch:
    		Switch targetSwitch = (Switch) target;
    		Switch propertySwitch = (Switch) properties;
    		targetSwitch.setAttachedFace(propertySwitch.getAttachedFace());
    		targetSwitch.setFacing(propertySwitch.getFacing());
    		targetSwitch.setPowered(propertySwitch.isPowered());
    		return targetSwitch;
    	case CraftTechnicalPiston:
    		TechnicalPiston targetTechnicalPiston = (TechnicalPiston) target;
    		TechnicalPiston propertyTechnicalPiston = (TechnicalPiston) properties;
    		targetTechnicalPiston.setFacing(propertyTechnicalPiston.getFacing());
    		targetTechnicalPiston.setType(propertyTechnicalPiston.getType());
    		return targetTechnicalPiston;
    	case CraftTNT:
    		TNT targetTNT = (TNT) target;
    		TNT propertyTNT = (TNT) properties;
    		targetTNT.setUnstable(propertyTNT.isUnstable());
    		return targetTNT;
    	case CraftTrapDoor:
    		TrapDoor targetTrapDoor = (TrapDoor) target;
    		TrapDoor propertyTrapDoor = (TrapDoor) properties;
    		targetTrapDoor.setFacing(propertyTrapDoor.getFacing());
    		targetTrapDoor.setHalf(propertyTrapDoor.getHalf());
    		targetTrapDoor.setOpen(propertyTrapDoor.isOpen());
    		targetTrapDoor.setPowered(propertyTrapDoor.isPowered());
    		targetTrapDoor.setWaterlogged(propertyTrapDoor.isWaterlogged());
    		return targetTrapDoor;
    	case CraftTripwire:
    		Tripwire targetTripwire = (Tripwire) target;
    		Tripwire propertyTripwire = (Tripwire) properties;
    		targetTripwire.setAttached(propertyTripwire.isAttached());
    		targetTripwire.setDisarmed(propertyTripwire.isDisarmed());
    		for(BlockFace face : propertyTripwire.getFaces())
    			targetTripwire.setFace(face, true);
    		targetTripwire.setPowered(propertyTripwire.isPowered());
    		return targetTripwire;
    	case CraftTripwireHook:
    		TripwireHook targetTripwireHook = (TripwireHook) target;
    		TripwireHook propertyTripwireHook = (TripwireHook) properties;
    		targetTripwireHook.setAttached(propertyTripwireHook.isAttached());
    		targetTripwireHook.setFacing(propertyTripwireHook.getFacing());
    		targetTripwireHook.setPowered(propertyTripwireHook.isPowered());
    		return targetTripwireHook;
    	case CraftTurtleEgg:
    		TurtleEgg targetTurtleEgg = (TurtleEgg) target;
    		TurtleEgg propertyTurtleEgg = (TurtleEgg) properties;
    		targetTurtleEgg.setEggs(propertyTurtleEgg.getEggs());
    		targetTurtleEgg.setHatch(propertyTurtleEgg.getHatch());
    		return targetTurtleEgg;
    	case CraftWall:
    		Wall targetWall = (Wall) target;
    		Wall propertyWall = (Wall) properties;
    		targetWall.setUp(propertyWall.isUp());
    		targetWall.setWaterlogged(propertyWall.isWaterlogged());
    		return targetWall;
    	case CraftWallSign:
    		WallSign targetWallSign = (WallSign) target;
    		WallSign propertyWallSign = (WallSign) properties;
    		targetWallSign.setFacing(propertyWallSign.getFacing());
    		targetWallSign.setWaterlogged(propertyWallSign.isWaterlogged());
    		return targetWallSign;
    	case CraftWaterlogged:
    		Waterlogged targetWaterlogged = (Waterlogged) target;
    		Waterlogged propertyWaterlogged = (Waterlogged) properties;
    		targetWaterlogged.setWaterlogged(propertyWaterlogged.isWaterlogged());
    		return targetWaterlogged;
    	case CraftCrops:
    		targetAgeable = (Ageable) target;
    		propertyAgeable = (Ageable) properties;
    		targetAgeable.setAge(propertyAgeable.getAge());
    		return targetAgeable;
    	default:
    		return properties;
    	}
    }
    
    //Returns the blockdata, as well as true if properties have been specifically set by the player.
    public static Map<BlockData, BTBMeta> stringToHashMap(String string, boolean ratios){
    	String[] materialNames = string.split(",");
    	HashMap<BlockData, BTBMeta> materialList = new HashMap<BlockData, BTBMeta>();
    	for(String materialString : materialNames) {
    			materialString = materialString.replace('|', ',');
    			//If no specified additional amounts.
        		if(materialString.split("%").length == 1) {
        			//Is this, or is this not, a simple block
        			if(BetterTools.getSimpleBlocks() != null && BetterTools.getSimpleBlocks().getSimpleBlock(materialString.toLowerCase()) != null) {
        				SimpleBlock sb = BetterTools.getSimpleBlocks().getSimpleBlock(materialString.toLowerCase());
        				
                		materialList.put(Bukkit.createBlockData("noteblock[instrument=" + sb.getInstrument() + ",note=" + sb.getNote() + "]"), new BTBMeta(false, 1));
        			}else {
                		materialList.put(Bukkit.createBlockData(materialString), new BTBMeta(materialString.contains("["), 1));
        			}
        			//If there is a specified amount.
        		}else if(materialString.split("%").length == 2){
        			if(BetterTools.getSimpleBlocks() != null && BetterTools.getSimpleBlocks().getSimpleBlock(materialString.split("%")[1].toLowerCase()) != null) {
        				SimpleBlock sb = BetterTools.getSimpleBlocks().getSimpleBlock(materialString.split("%")[1].toLowerCase());
        				materialList.put(Bukkit.createBlockData("noteblock[instrument=" + sb.getInstrument() + ",note=" + sb.getNote() + "]"), new BTBMeta(false, Integer.parseInt(materialString.split("%")[0])));
        			}else {
        				materialList.put(Bukkit.createBlockData(materialString.split("%")[1]), new BTBMeta(materialString.contains("["), Integer.parseInt(materialString.split("%")[0])));
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
    	//2%oak_stairs,spruce_stairs[facing=north|type=top],small_stone_bricks
    	String[] materialNames = list.split(",");
    	//2%oak_stairs spruce_stairs[facing=north|type=top] small_stone_bricks
    	for(String materialString : materialNames) {
    		materialString = materialString.replace('|', ',');
    		//2%oak_stairs  spruce_stairs[facing=north type=top]  small_stone_bricks
    		if(materialString.split("%").length == 1) {
        		//2 oak_stairs  spruce_stairs[facing=north type=top]  small_stone_bricks
    			try {
            		Bukkit.createBlockData(materialString);
        		}catch(IllegalArgumentException e) {
        			//Is it not a simpleblock?
        			if(BetterTools.getSimpleBlocks() != null && BetterTools.getSimpleBlocks().getSimpleBlock(materialString.toLowerCase()) == null)
            			return materialString;
        		}
    		}else if(materialString.split("%").length == 2){
    			try {
            		Bukkit.createBlockData(materialString.split("%")[1]);
        		}catch(IllegalArgumentException e) {
        			//Is it not a simpleblock?
        			if(BetterTools.getSimpleBlocks() != null && BetterTools.getSimpleBlocks().getSimpleBlock(materialString.toLowerCase()) == null)
            			return materialString;
        		}
    		}
    	}
    	return null;
    }
}

enum CLAZZ {
	CraftAgeable, CraftAnaloguePowerable, CraftAttachable, CraftBamboo, CraftBed, CraftBeehive, CraftBell, CraftBisected, CraftBlockData, CraftBrewingStand, CraftBubbleColumn, CraftCake, CraftCampfire, CraftChain, CraftChest, CraftCocoa, CraftCommandBlock, CraftComparator, CraftCoralWallFan, CraftDaylightDetector, CraftDirectional, CraftDispenser, CraftDoor, CraftEnderChest, CraftEndPortalFrame, CraftFaceAttachable, CraftFarmland, CraftFence, CraftFire, CraftFurnaceFurace, CraftGate, CraftGlassPane, CraftGrindstone, CraftHopper, CraftJigsaw, CraftJukebox, CraftLadder, CraftLantern, CraftLeaves, CraftLectern, CraftLevelled, CraftLightable, CraftMultipleFacing, CraftNoteBlock, CraftObserver, CraftOpenable, CraftOrientable, CraftPiston, CraftPistonHead, CraftPowerable, CraftRail, CraftRedstoneRail, CraftRedstoneWallTorch, CraftRedstoneWire, CraftRepeater, CraftRespawnAnchor, CraftRotatable, CraftSapling, CraftScaffolding, CraftSeaPickle, CraftSign, CraftStepAbstract, CraftSnow, CraftSnowable, CraftStairs, CraftStructureBlock, CraftSwitch, CraftTechnicalPiston, CraftTNT, CraftTrapDoor, CraftTripwire, CraftTripwireHook, CraftTurtleEgg, CraftWall, CraftWallSign, CraftWaterlogged, CraftCrops

}