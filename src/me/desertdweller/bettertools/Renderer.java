package me.desertdweller.bettertools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import de.tr7zw.nbtapi.NBTItem;
import me.desertdweller.bettertools.math.Noise;

public class Renderer extends MapRenderer {

	@SuppressWarnings("deprecation")
	@Override
	public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
		ItemStack tool = player.getInventory().getItemInMainHand();
		if(tool.getType().equals(Material.AIR))
			return;
		NBTItem nbti = new NBTItem(tool);
		if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("BetterTools") && !nbti.getString("Noise").equals("none")) {
			Noise noise = new Noise(nbti.getString("Noise"));
			
			for(int x = 0; x < 128; x++) {
				for(int y = 0; y < 128; y++) {
					if(noise.getPoint(x, 0, y)) {
						mapCanvas.setPixel(x, y, MapPalette.DARK_GRAY);
					}else {
						mapCanvas.setPixel(x, y, MapPalette.TRANSPARENT);
					}
				}
			}
		}
		
	}

	
}
