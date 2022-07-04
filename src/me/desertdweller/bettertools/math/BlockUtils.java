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
import org.bukkit.block.data.type.AmethystCluster;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.BigDripleaf;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Candle;
import org.bukkit.block.data.type.CaveVines;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Dripleaf;
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
import org.bukkit.block.data.type.SmallDripleaf;
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

import com.google.common.base.Enums;

import de.tr7zw.nbtapi.NBTItem;
import me.desertdweller.bettertools.Renderer;

public class BlockUtils {
	public static HashMap<Material, Integer> materialIds = new HashMap<Material, Integer>();

	public static List<Block> getNearbyBlocks(Location location, int radius, Noise perlin) {
		List<Block> blocks = new ArrayList<Block>();
		for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
			for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
				for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {

					double distance = ((location.getBlockX() - x) * (location.getBlockX() - x)
							+ ((location.getBlockZ() - z) * (location.getBlockZ() - z))
							+ ((location.getBlockY() - y) * (location.getBlockY() - y)));
					if (distance < radius * radius && perlin.getPoint(x, y, z)) {
						blocks.add(location.getWorld().getBlockAt(x, y, z));
					}
				}
			}
		}
		return blocks;
	}

	public static List<Block> getNearbyBlocksMasked(Location location, int radius, Map<BlockData, BTBMeta> mask, Noise noise, boolean snowBrush) {
		List<Block> blocks = new ArrayList<Block>();
		List<Material> nonCustomMats = getNonCustomMaterials(mask);
		for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
			for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
				for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
					double distance = ((location.getBlockX() - x) * (location.getBlockX() - x)
							+ ((location.getBlockZ() - z) * (location.getBlockZ() - z))
							+ ((location.getBlockY() - y) * (location.getBlockY() - y)));
					if (distance < radius * radius) {
						if (nonCustomMats.contains(location.getWorld().getBlockAt(x, y, z).getType())) {
							if (noise.getPoint(x, y, z))
								blocks.add(location.getWorld().getBlockAt(x, y, z));
						} else if (mask.containsKey(location.getWorld().getBlockAt(x, y, z).getBlockData())
								&& noise.getPoint(x, y, z)) {
							blocks.add(location.getWorld().getBlockAt(x, y, z));
						} else if (snowBrush && location.getWorld().getBlockAt(x, y-1, z).getType() != Material.AIR
								&& location.getWorld().getBlockAt(x, y, z).getType() == Material.AIR
								&& noise.getPoint(x, y, z)) {
							blocks.add(location.getWorld().getBlockAt(x, y, z));
						}
					}
				}
			}
		}
		return blocks;
	}

	public static List<Block> getBlocksTouching(List<Block> inputBlocks, Map<BlockData, BTBMeta> touchLimits) {
		Map<Material, BTBMeta> nonCustomMats = getNonCustomMaterialsWithMeta(touchLimits);
		List<Block> outputBlocks = new ArrayList<Block>();
		for (Block block : inputBlocks) {
			if (nonCustomMats.containsKey(block.getRelative(1, 0, 0).getType()) && nonCustomMats.get(block.getRelative(1, 0, 0).getType()).containsParam("west", true)) {
				outputBlocks.add(block);
			} else if (touchLimits.containsKey(block.getRelative(1, 0, 0).getBlockData()) && touchLimits.get(block.getRelative(1, 0, 0).getBlockData()).containsParam("west", true)) {
				outputBlocks.add(block);
			} else if (nonCustomMats.containsKey(block.getRelative(-1, 0, 0).getType()) && nonCustomMats.get(block.getRelative(-1, 0, 0).getType()).containsParam("east", true)) {
				outputBlocks.add(block);
			} else if (touchLimits.containsKey(block.getRelative(-1, 0, 0).getBlockData()) && touchLimits.get(block.getRelative(-1, 0, 0).getBlockData()).containsParam("east", true)) {
				outputBlocks.add(block);
			} else if (nonCustomMats.containsKey(block.getRelative(0, 1, 0).getType()) && nonCustomMats.get(block.getRelative(0, 1, 0).getType()).containsParam("down", true)) {
				outputBlocks.add(block);
			} else if (touchLimits.containsKey(block.getRelative(0, 1, 0).getBlockData()) && touchLimits.get(block.getRelative(0, 1, 0).getBlockData()).containsParam("down", true)) {
				outputBlocks.add(block);
			} else if (nonCustomMats.containsKey(block.getRelative(0, -1, 0).getType()) && nonCustomMats.get(block.getRelative(0, -1, 0).getType()).containsParam("up", true)) {
				outputBlocks.add(block);
			} else if (touchLimits.containsKey(block.getRelative(0, -1, 0).getBlockData()) && touchLimits.get(block.getRelative(0, -1, 0).getBlockData()).containsParam("up", true)) {
				outputBlocks.add(block);
			} else if (nonCustomMats.containsKey(block.getRelative(0, 0, 1).getType()) && nonCustomMats.get(block.getRelative(0, 0, 1).getType()).containsParam("south", true)) {
				outputBlocks.add(block);
			} else if (touchLimits.containsKey(block.getRelative(0, 0, 1).getBlockData()) && touchLimits.get(block.getRelative(0, 0, 1).getBlockData()).containsParam("south", true)) {
				outputBlocks.add(block);
			} else if (nonCustomMats.containsKey(block.getRelative(0, 0, -1).getType()) && nonCustomMats.get(block.getRelative(0, 0, -1).getType()).containsParam("north", true)) {
				outputBlocks.add(block);
			} else if (touchLimits.containsKey(block.getRelative(0, 0, -1).getBlockData()) && touchLimits.get(block.getRelative(0, 0, -1).getBlockData()).containsParam("north", true)) {
				outputBlocks.add(block);
			}
		}
		return outputBlocks;
	}

	//This is for all of the blocks given with no defined blockdata. It is intended to be used as a general case instead of being limited to the default blockstates when comparing.
	private static List<Material> getNonCustomMaterials(Map<BlockData, BTBMeta> map) {
		ArrayList<Material> materials = new ArrayList<Material>();
		for (BlockData block : map.keySet()) {
			if (!map.get(block).specified)
				materials.add(block.getMaterial());
		}
		return materials;
	}
	
	private static Map<Material, BTBMeta> getNonCustomMaterialsWithMeta(Map<BlockData, BTBMeta> map) {
		Map<Material, BTBMeta> materials = new HashMap<Material, BTBMeta>();
		for (BlockData block : map.keySet()) {
			if (!map.get(block).specified)
				materials.put(block.getMaterial(), map.get(block));
		}
		return materials;
	}

	public static void givePlayerNoiseMap(Player p) {
		if (!p.getInventory().getItemInOffHand().getType().equals(Material.AIR))
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
		CLAZZ z;
		z = Enums.getIfPresent(CLAZZ.class, target.getClass().getSimpleName()).orNull();
		if (z == null) {
			return null;
		}
		switch (z) {
		case CraftAgeable:
			Ageable targetAgeable = (Ageable) target;
			Ageable propertyAgeable = (Ageable) properties;
			targetAgeable.setAge(propertyAgeable.getAge());
			return targetAgeable;
		case CraftAmethystCluster:
			AmethystCluster targetAmethyst = (AmethystCluster) target;
			AmethystCluster propertyAmethyst = (AmethystCluster) properties;
			targetAmethyst.setWaterlogged(propertyAmethyst.isWaterlogged());
			targetAmethyst.setFacing(propertyAmethyst.getFacing());
			return targetAmethyst;
		case CraftAnvil:
			Directional targetAnvil = (Directional) target;
			Directional propertyAnvil = (Directional) properties;
			targetAnvil.setFacing(propertyAnvil.getFacing());
			return targetAnvil;
		case CraftBamboo:
			Bamboo targetBamboo = (Bamboo) target;
			Bamboo propertyBamboo = (Bamboo) properties;
			targetBamboo.setLeaves(propertyBamboo.getLeaves());
			targetBamboo.setAge(propertyBamboo.getAge());
			targetBamboo.setStage(propertyBamboo.getStage());
			return targetBamboo;
		case CraftBanner:
			Rotatable targetBanner = (Rotatable) target;
			Rotatable propertyBanner = (Rotatable) properties;
			targetBanner.setRotation(propertyBanner.getRotation());
			return targetBanner;
		case CraftBannerWall:
			Directional targetBannerWall = (Directional) target;
			Directional propertyBannerWall = (Directional) properties;
			targetBannerWall.setFacing(propertyBannerWall.getFacing());
			return targetBannerWall;
		case CraftBarrel:
			Directional targetBarrel = (Directional) target;
			Directional propertyBarrel = (Directional) properties;
			targetBarrel.setFacing(propertyBarrel.getFacing());
			return targetBarrel;
		case CraftBed:
			Bed targetBed = (Bed) target;
			Bed propertyBed = (Bed) properties;
			targetBed.setFacing(propertyBed.getFacing());
			targetBed.setPart(propertyBed.getPart());
			return targetBed;
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
		case CraftBeetroot:
			Ageable targetBeetroot = (Ageable) target;
			Ageable propertyBeetroot = (Ageable) properties;
			targetBeetroot.setAge(propertyBeetroot.getAge());
			return targetBeetroot;
		case CraftBigDripleaf:
			BigDripleaf targetBigDripleaf = (BigDripleaf) target;
			BigDripleaf propertyBigDripleaf = (BigDripleaf) properties;
			targetBigDripleaf.setFacing(propertyBigDripleaf.getFacing());
			targetBigDripleaf.setWaterlogged(propertyBigDripleaf.isWaterlogged());
			targetBigDripleaf.setTilt(propertyBigDripleaf.getTilt());
			return targetBigDripleaf;
		case CraftBigDripleafStem:
			Dripleaf targetBigDripleafStem = (Dripleaf) target;
			Dripleaf propertyBigDripleafStem = (Dripleaf) properties;
			targetBigDripleafStem.setFacing(propertyBigDripleafStem.getFacing());
			targetBigDripleafStem.setWaterlogged(propertyBigDripleafStem.isWaterlogged());
			return targetBigDripleafStem;
		case CraftBrewingStand:
			BrewingStand targetBrewingStand = (BrewingStand) target;
			BrewingStand propertyBrewingStand = (BrewingStand) properties;
			for (int i : propertyBrewingStand.getBottles()) {
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
		case CraftCandle:
			Candle targetCandle = (Candle) target;
			Candle propertyCandle = (Candle) properties;
			targetCandle.setCandles(propertyCandle.getCandles());
			targetCandle.setLit(propertyCandle.isLit());
			targetCandle.setWaterlogged(propertyCandle.isWaterlogged());
			return targetCandle;
		case CraftCandleCake:
			Lightable targetCandleCake = (Lightable) target;
			Lightable propertyCandleCake = (Lightable) properties;
			targetCandleCake.setLit(propertyCandleCake.isLit());
			return targetCandleCake;
		case CraftCampfire:
			Campfire targetCampfire = (Campfire) target;
			Campfire propertyCampfire = (Campfire) properties;
			targetCampfire.setFacing(propertyCampfire.getFacing());
			targetCampfire.setLit(propertyCampfire.isLit());
			targetCampfire.setSignalFire(propertyCampfire.isSignalFire());
			targetCampfire.setWaterlogged(propertyCampfire.isWaterlogged());
			return targetCampfire;
		case CraftCaveVines:
			CaveVines targetCaveVines = (CaveVines) target;
			CaveVines propertyCaveVines = (CaveVines) properties;
			targetCaveVines.setAge(propertyCaveVines.getAge());
			targetCaveVines.setBerries(propertyCaveVines.isBerries());
			return targetCaveVines;
		case CraftCaveVinesPlant:
			CaveVinesPlant targetCaveVinesPlant = (CaveVinesPlant) target;
			CaveVinesPlant propertyCaveVinesPlant = (CaveVinesPlant) properties;
			targetCaveVinesPlant.setBerries(propertyCaveVinesPlant.isBerries());
			return targetCaveVinesPlant;
		case CraftCarrots:
			Ageable targetCarrots = (Ageable) target;
			Ageable propertyCarrots = (Ageable) properties;
			targetCarrots.setAge(propertyCarrots.getAge());
			return targetCarrots;
		case CraftChest:
			Chest targetChest = (Chest) target;
			Chest propertyChest = (Chest) properties;
			targetChest.setFacing(propertyChest.getFacing());
			targetChest.setType(propertyChest.getType());
			targetChest.setWaterlogged(propertyChest.isWaterlogged());
			return targetChest;
		case CraftCobbleWall:
			Wall targetWall = (Wall) target;
			Wall propertyWall = (Wall) properties;
			targetWall.setUp(propertyWall.isUp());
			targetWall.setWaterlogged(propertyWall.isWaterlogged());
			targetWall.setHeight(BlockFace.EAST, propertyWall.getHeight(BlockFace.EAST));
			targetWall.setHeight(BlockFace.WEST, propertyWall.getHeight(BlockFace.WEST));
			targetWall.setHeight(BlockFace.SOUTH, propertyWall.getHeight(BlockFace.SOUTH));
			targetWall.setHeight(BlockFace.NORTH, propertyWall.getHeight(BlockFace.NORTH));
			return targetWall;
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
		case CraftCoralDead:
			Waterlogged targetCoralDead = (Waterlogged) target;
			Waterlogged propertyCoralDead = (Waterlogged) properties;
			targetCoralDead.setWaterlogged(propertyCoralDead.isWaterlogged());
			return targetCoralDead;
		case CraftCoralFan:
			Waterlogged targetCoralFan = (Waterlogged) target;
			Waterlogged propertyCoralFan = (Waterlogged) properties;
			targetCoralFan.setWaterlogged(propertyCoralFan.isWaterlogged());
			return targetCoralFan;
		case CraftCoralFanAbstract:
			Waterlogged targetCoralFanAbstract = (Waterlogged) target;
			Waterlogged propertyCoralFanAbstract = (Waterlogged) properties;
			targetCoralFanAbstract.setWaterlogged(propertyCoralFanAbstract.isWaterlogged());
			return targetCoralFanAbstract;
		case CraftCoralFanWall:
			CoralWallFan targetCoralWallFan = (CoralWallFan) target;
			CoralWallFan propertyCoralWallFan = (CoralWallFan) properties;
			targetCoralWallFan.setFacing(propertyCoralWallFan.getFacing());
			targetCoralWallFan.setWaterlogged(propertyCoralWallFan.isWaterlogged());
			return targetCoralWallFan;
		case CraftCoralFanWallAbstract:
			CoralWallFan targetCoralWallFanAbstract = (CoralWallFan) target;
			CoralWallFan propertyCoralWallFanAbstract = (CoralWallFan) properties;
			targetCoralWallFanAbstract.setFacing(propertyCoralWallFanAbstract.getFacing());
			targetCoralWallFanAbstract.setWaterlogged(propertyCoralWallFanAbstract.isWaterlogged());
			return targetCoralWallFanAbstract;
		case CraftCoralPlant:
			Waterlogged targetCoralPlant = (Waterlogged) target;
			Waterlogged propertyCoralPlant = (Waterlogged) properties;
			targetCoralPlant.setWaterlogged(propertyCoralPlant.isWaterlogged());
			return targetCoralPlant;
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
		case CraftDirtSnow:
			Snowable targetPodzol = (Snowable) target;
			Snowable propertyPodzol = (Snowable) properties;
			targetPodzol.setSnowy(propertyPodzol.isSnowy());
			return targetPodzol;
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
			for (BlockFace face : propertyFence.getFaces())
				targetFence.setFace(face, true);
			targetFence.setWaterlogged(propertyFence.isWaterlogged());
			return targetFence;
		case CraftFire:
			Fire targetFire = (Fire) target;
			Fire propertyFire = (Fire) properties;
			targetFire.setAge(propertyFire.getAge());
			for (BlockFace face : propertyFire.getFaces())
				targetFire.setFace(face, true);
			return targetFire;
		case CraftFluids:
			return properties;
		case CraftFurnaceFurace:
			Furnace targetFurnace = (Furnace) target;
			Furnace propertyFurnace = (Furnace) properties;
			targetFurnace.setFacing(propertyFurnace.getFacing());
			targetFurnace.setLit(propertyFurnace.isLit());
			return targetFurnace;
		case CraftFenceGate:
			Gate targetGate = (Gate) target;
			Gate propertyGate = (Gate) properties;
			targetGate.setFacing(propertyGate.getFacing());
			targetGate.setInWall(propertyGate.isInWall());
			targetGate.setOpen(propertyGate.isOpen());
			targetGate.setPowered(propertyGate.isPowered());
			return targetGate;
		case CraftFloorSign:
			Sign targetFloorSign = (Sign) target;
			Sign propertyFloorSign = (Sign) properties;
			targetFloorSign.setRotation(propertyFloorSign.getRotation());
			targetFloorSign.setWaterlogged(propertyFloorSign.isWaterlogged());
			return targetFloorSign;
		case CraftGlassPane:
			GlassPane targetGlassPane = (GlassPane) target;
			GlassPane propertyGlassPane = (GlassPane) properties;
			for (BlockFace face : propertyGlassPane.getFaces())
				targetGlassPane.setFace(face, true);
			targetGlassPane.setWaterlogged(propertyGlassPane.isWaterlogged());
			return targetGlassPane;
		case CraftGlazedTerracotta:
			Directional targetGlazedTerracotta = (Directional) target;
			Directional propertyGlazedTerracotta = (Directional) properties;
			targetGlazedTerracotta.setFacing(propertyGlazedTerracotta.getFacing());
			return targetGlazedTerracotta;
		case CraftGrass:
			Snowable targetGrass = (Snowable) target;
			Snowable propertyGrass = (Snowable) properties;
			targetGrass.setSnowy(propertyGrass.isSnowy());
			return propertyGrass;
		case CraftGrindstone:
			Grindstone targetGrindstone = (Grindstone) target;
			Grindstone propertyGrindstone = (Grindstone) properties;
			targetGrindstone.setAttachedFace(propertyGrindstone.getAttachedFace());
			targetGrindstone.setFacing(propertyGrindstone.getFacing());
			return targetGrindstone;
		case CraftHangingRoots:
			Waterlogged targetHangingRoots = (Waterlogged) target;
			Waterlogged propertyHangingRoots = (Waterlogged) properties;
			targetHangingRoots.setWaterlogged(propertyHangingRoots.isWaterlogged());
			return targetHangingRoots;
		case CraftHopper:
			Hopper targetHopper = (Hopper) target;
			Hopper propertyHopper = (Hopper) properties;
			targetHopper.setEnabled(propertyHopper.isEnabled());
			targetHopper.setFacing(propertyHopper.getFacing());
			return targetHopper;
		case CraftHugeMushroom:
			MultipleFacing targetHugeMushroom = (MultipleFacing) target;
			MultipleFacing propertyHugeMushroom = (MultipleFacing) properties;
			for(BlockFace face : propertyHugeMushroom.getFaces())
				targetHugeMushroom.setFace(face, propertyHugeMushroom.hasFace(face));
			return targetHugeMushroom;
		case CraftIceFrost:
			Ageable targetIceFrost = (Ageable) target;
			Ageable propertyIceFrost = (Ageable) properties;
			targetIceFrost.setAge(propertyIceFrost.getAge());
			return targetIceFrost;
		case CraftJigsaw:
			Jigsaw targetJigsaw = (Jigsaw) target;
			Jigsaw propertyJigsaw = (Jigsaw) properties;
			targetJigsaw.setOrientation(propertyJigsaw.getOrientation());
			return targetJigsaw;
		case CraftKelp:
			Ageable targetKelp = (Ageable) target;
			Ageable propertyKelp = (Ageable) properties;
			targetKelp.setAge(propertyKelp.getAge());
			return targetKelp;
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
			for (BlockFace face : propertyMultipleFacing.getFaces())
				targetMultipleFacing.setFace(face, true);
			return targetMultipleFacing;
		case CraftNote:
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
		case CraftPotatoes:
			Ageable targetPotatoes = (Ageable) target;
			Ageable propertyPotatoes = (Ageable) properties;
			targetPotatoes.setAge(propertyPotatoes.getAge());
			return targetPotatoes;
		case CraftPowerable:
			Powerable targetPowerable = (Powerable) target;
			Powerable propertyPowerable = (Powerable) properties;
			targetPowerable.setPowered(propertyPowerable.isPowered());
			return targetPowerable;
		case CraftPressurePlateBinary:
			Powerable targetPressurePlateBinary = (Powerable) target;
			Powerable propertyPressurePlateBinary = (Powerable) properties;
			targetPressurePlateBinary.setPowered(propertyPressurePlateBinary.isPowered());
			return targetPressurePlateBinary;
		case CraftRail:
			Rail targetRail = (Rail) target;
			Rail propertyRail = (Rail) properties;
			targetRail.setShape(propertyRail.getShape());
			return targetRail;
		case CraftRedstoneOre:
			Lightable targetRedstoneOre = (Lightable) target;
			Lightable propertyRedstoneOre = (Lightable) properties;
			targetRedstoneOre.setLit(propertyRedstoneOre.isLit());
			return targetRedstoneOre;
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
			for (BlockFace face : propertyRedstoneWire.getAllowedFaces())
				targetRedstoneWire.setFace(face, propertyRedstoneWire.getFace(face));
			return targetRedstoneWire;
		case CraftReed:
			Ageable targetReed = (Ageable) target;
			Ageable propertyReed = (Ageable) properties;
			targetReed.setAge(propertyReed.getAge());
			return targetReed;
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
			Orientable targetRotatable = (Orientable) target;
			Orientable propertyRotatable = (Orientable) properties;
			targetRotatable.setAxis(propertyRotatable.getAxis());
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
		case CraftShulkerBox:
			Directional targetShulkerBox = (Directional) target;
			Directional propertyShulkerBox = (Directional) properties;
			targetShulkerBox.setFacing(propertyShulkerBox.getFacing());
			return targetShulkerBox;
		case CraftSign:
			Sign targetSign = (Sign) target;
			Sign propertySign = (Sign) properties;
			targetSign.setRotation(propertySign.getRotation());
			targetSign.setWaterlogged(propertySign.isWaterlogged());
			return targetSign;
		case CraftStainedGlassPane:
			GlassPane targetPane = (GlassPane) target;
			GlassPane propertyPane = (GlassPane) properties;
			for(BlockFace face : propertyPane.getFaces())
				targetPane.setFace(face, propertyPane.hasFace(face));
			targetPane.setWaterlogged(propertyPane.isWaterlogged());
			return targetPane;
		case CraftStairs:
			Stairs targetStairs = (Stairs) target;
			Stairs propertyStairs = (Stairs) properties;
			targetStairs.setFacing(propertyStairs.getFacing());
			targetStairs.setHalf(propertyStairs.getHalf());
			targetStairs.setShape(propertyStairs.getShape());
			targetStairs.setWaterlogged(propertyStairs.isWaterlogged());
			return targetStairs;
		case CraftSmallDripleaf:
			SmallDripleaf targetSmallDripleaf = (SmallDripleaf) target;
			SmallDripleaf propertySmallDripleaf = (SmallDripleaf) properties;
			targetSmallDripleaf.setFacing(propertySmallDripleaf.getFacing());
			targetSmallDripleaf.setHalf(propertySmallDripleaf.getHalf());
			targetSmallDripleaf.setWaterlogged(propertySmallDripleaf.isWaterlogged());
			return targetSmallDripleaf;
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
		case CraftStepAbstract:
			Slab targetSlab = (Slab) target;
			Slab propertySlab = (Slab) properties;
			targetSlab.setType(propertySlab.getType());
			targetSlab.setWaterlogged(propertySlab.isWaterlogged());
			return targetSlab;
		case CraftStructureBlock:
			StructureBlock targetStructureBlock = (StructureBlock) target;
			StructureBlock propertyStructureBlock = (StructureBlock) properties;
			targetStructureBlock.setMode(propertyStructureBlock.getMode());
			return targetStructureBlock;
		case CraftSweetBerryBush:
			Ageable targetSweetBerryBush = (Ageable) target;
			Ageable propertySweetBerryBush = (Ageable) properties;
			targetSweetBerryBush.setAge(propertySweetBerryBush.getAge());
			return targetSweetBerryBush;
		case CraftSwitch:
			Switch targetSwitch = (Switch) target;
			Switch propertySwitch = (Switch) properties;
			targetSwitch.setAttachedFace(propertySwitch.getAttachedFace());
			targetSwitch.setFacing(propertySwitch.getFacing());
			targetSwitch.setPowered(propertySwitch.isPowered());
			return targetSwitch;
		case CraftTallPlantFlower:
			Bisected targetTallPlantFlower = (Bisected) target;
			Bisected propertyTallPlantFlower = (Bisected) properties;
			targetTallPlantFlower.setHalf(propertyTallPlantFlower.getHalf());
			return targetTallPlantFlower;
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
		case CraftTrapdoor:
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
			for (BlockFace face : propertyTripwire.getFaces())
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
		case CraftTwistingVines:
			Ageable targetTwistingVines = (Ageable) target;
			Ageable propertyTwistingVines = (Ageable) properties;
			targetTwistingVines.setAge(propertyTwistingVines.getAge());
			return targetTwistingVines;
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
		case CraftWeatheringCopperSlab:
			Slab targetWeatheringSlab = (Slab) target;
			Slab propertyWeatheringSlab = (Slab) properties;
			targetWeatheringSlab.setWaterlogged(propertyWeatheringSlab.isWaterlogged());
			targetWeatheringSlab.setType(propertyWeatheringSlab.getType());
			return targetWeatheringSlab;
		case CraftWeatheringCopperStair:
			Stairs targetWeatheringStair = (Stairs) target;
			Stairs propertyWeatheringStair = (Stairs) properties;
			targetWeatheringStair.setFacing(propertyWeatheringStair.getFacing());
			targetWeatheringStair.setHalf(propertyWeatheringStair.getHalf());
			targetWeatheringStair.setShape(propertyWeatheringStair.getShape());
			targetWeatheringStair.setWaterlogged(propertyWeatheringStair.isWaterlogged());
			return targetWeatheringStair;
		case CraftWeepingVines:
			Ageable targetWeepingVine = (Ageable) target;
			Ageable propertyWeepingVine = (Ageable) properties;
			targetWeepingVine.setAge(propertyWeepingVine.getAge());
			return targetWeepingVine;
		case CraftWoodButton:
			Switch targetWoodButton = (Switch) target;
			Switch propertyWoodButton = (Switch) properties;
			targetWoodButton.setAttachedFace(propertyWoodButton.getAttachedFace());
			targetWoodButton.setFacing(propertyWoodButton.getFacing());
			targetWoodButton.setPowered(propertyWoodButton.isPowered());
			return targetWoodButton;
		case CraftStoneButton:
			Switch targetStoneButton = (Switch) target;
			Switch propertyStoneButton = (Switch) properties;
			targetStoneButton.setAttachedFace(propertyStoneButton.getAttachedFace());
			targetStoneButton.setFacing(propertyStoneButton.getFacing());
			targetStoneButton.setPowered(propertyStoneButton.isPowered());
			return targetStoneButton;
		case CraftCrops:
			targetAgeable = (Ageable) target;
			propertyAgeable = (Ageable) properties;
			targetAgeable.setAge(propertyAgeable.getAge());
			return targetAgeable;
		default:
			return target;
		}
	}

	public static void initMaterialIds() {
		int id = 0;
		for (Material material : Material.values()) {
			materialIds.put(material, id);
			id++;
		}
	}

	public static Block smoothSnowBlock(Block target) {
		if (target.getRelative(BlockFace.UP).getType() == Material.SNOW
				|| target.getRelative(BlockFace.UP).getType() == Material.SNOW_BLOCK) {
			target.setType(Material.SNOW, false);
			Snow blockData = ((Snow) target.getBlockData());
			blockData.setLayers(8);
			target.setBlockData(blockData, false);
			return target;
		}
		if (target.getRelative(BlockFace.DOWN).getType() == Material.SNOW
				&& ((Snow) target.getRelative(BlockFace.DOWN).getBlockData()).getLayers() != 8
				|| target.getRelative(BlockFace.DOWN).isPassable() || target.getRelative(BlockFace.DOWN).isLiquid()) {
			target.setType(Material.AIR, false);
			return target;
		}

		int averageHeight = getAverageSurroundingHeight(target);
		if (averageHeight <= 8) {
			target.setType(Material.AIR, false);
			return target;
		} else if (averageHeight > 15) {
			target.setType(Material.SNOW, false);
			Snow blockData = ((Snow) target.getBlockData());
			blockData.setLayers(8);
			target.setBlockData(blockData, false);
		} else {
			target.setType(Material.SNOW, false);
			Snow blockData = ((Snow) target.getBlockData());
			blockData.setLayers(averageHeight - 8);
			target.setBlockData(blockData, false);
		}
		return target;
	}

	private static int getAverageSurroundingHeight(Block target) {
		float totalSurroundingHeight = 0;

		totalSurroundingHeight += getDirectionTotalHeight(target, BlockFace.NORTH);
		totalSurroundingHeight += getDirectionTotalHeight(target, BlockFace.NORTH_NORTH_EAST);
		totalSurroundingHeight += getDirectionTotalHeight(target, BlockFace.EAST);
		totalSurroundingHeight += getDirectionTotalHeight(target, BlockFace.SOUTH_EAST);
		totalSurroundingHeight += getDirectionTotalHeight(target, BlockFace.SOUTH);
		totalSurroundingHeight += getDirectionTotalHeight(target, BlockFace.SOUTH_WEST);
		totalSurroundingHeight += getDirectionTotalHeight(target, BlockFace.WEST);
		totalSurroundingHeight += getDirectionTotalHeight(target, BlockFace.NORTH_WEST);

		return Math.round(totalSurroundingHeight / 24);
	}

	private static int getDirectionTotalHeight(Block origin, BlockFace direction) {
		int totalHeight = 0;
		totalHeight += getHeightOfBlock(origin.getRelative(direction).getRelative(BlockFace.DOWN));
		totalHeight += getHeightOfBlock(origin.getRelative(direction));
		totalHeight += getHeightOfBlock(origin.getRelative(direction).getRelative(BlockFace.UP));
		totalHeight += getHeightOfBlock(origin.getRelative(direction).getRelative(direction).getRelative(BlockFace.DOWN));
		totalHeight += getHeightOfBlock(origin.getRelative(direction).getRelative(direction));
		totalHeight += getHeightOfBlock(origin.getRelative(direction).getRelative(direction).getRelative(BlockFace.UP));
		totalHeight += getHeightOfBlock(origin.getRelative(direction).getRelative(direction).getRelative(direction).getRelative(BlockFace.DOWN));
		totalHeight += getHeightOfBlock(origin.getRelative(direction).getRelative(direction).getRelative(direction));
		totalHeight += getHeightOfBlock(origin.getRelative(direction).getRelative(direction).getRelative(direction).getRelative(BlockFace.UP));
		return totalHeight;
	}

	private static int getHeightOfBlock(Block block) {
		if (block.isPassable() || block.isLiquid())
			return 0;
		switch (block.getType()) {
		case SNOW:
			Snow snow = (Snow) block.getBlockData();
			return snow.getLayers();
		case AIR:
			return 0;
		default:
			return 8;
		}
	}
}

enum CLAZZ {
	CraftAmethystCluster, CraftAnvil, CraftAgeable, CraftBanner, CraftBannerWall, CraftBamboo, CraftBarrel, CraftBed, CraftBeehive, CraftBell, CraftBeetroot, CraftBigDripleaf, CraftBigDripleafStem, 
	CraftBisected, CraftBlockData, CraftBrewingStand, CraftBubbleColumn, CraftCake, CraftCandle, CraftCandleCake, CraftCampfire, CraftCaveVines, CraftCaveVinesPlant, CraftCarrots, CraftChain,
	CraftChest, CraftCobbleWall, CraftCocoa, CraftCommandBlock, CraftComparator, CraftCoralDead, CraftCoralFan, CraftCoralFanAbstract, CraftCoralFanWall, CraftCoralFanWallAbstract, 
	CraftDaylightDetector, CraftDirectional, CraftDirtSnow, CraftDispenser, CraftDoor, CraftEnderChest, CraftEndPortalFrame,
	CraftCoralPlant, CraftFaceAttachable, CraftFarmland, CraftFence, CraftFenceGate, CraftFloorSign, CraftFire, CraftFluids, CraftFurnaceFurace,
	CraftGlassPane, CraftGlazedTerracotta, CraftGrass, CraftGrindstone, CraftHangingRoots, CraftHopper, CraftHugeMushroom, CraftIceFrost, CraftJigsaw, CraftKelp, CraftJukebox, CraftLadder, CraftLantern, CraftLeaves,
	CraftLectern, CraftLevelled, CraftLightable, CraftMultipleFacing, CraftNote, CraftObserver, CraftOpenable,
	CraftOrientable, CraftPiston, CraftPistonHead, CraftPotatoes, CraftPowerable, CraftPressurePlateBinary, CraftRail, CraftRedstoneOre, CraftRedstoneRail, CraftRedstoneWallTorch,
	CraftRedstoneWire, CraftReed, CraftRepeater, CraftRespawnAnchor, CraftRotatable, CraftSapling, CraftScaffolding,
	CraftSeaPickle, CraftShulkerBox, CraftSign, CraftStainedGlassPane, CraftStairs, CraftSmallDripleaf, CraftSnow, CraftSnowable, CraftStepAbstract, CraftStructureBlock, CraftSweetBerryBush,
	CraftSwitch, CraftTallPlantFlower, CraftTechnicalPiston, CraftTNT, CraftTrapdoor, CraftTripwire, CraftTripwireHook, CraftTurtleEgg, CraftTwistingVines,
	CraftWallSign, CraftWaterlogged, CraftWeatheringCopperSlab, CraftWeatheringCopperStair, CraftWeepingVines, CraftWoodButton, CraftStoneButton, CraftCrops

}