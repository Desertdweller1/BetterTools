package me.desertdweller.bettertools;

import org.bukkit.plugin.java.JavaPlugin;

import me.desertdweller.bettertools.listeners.PlayerListener;
import me.desertdweller.bettertools.math.BlockMath;
import me.desertdweller.bettertools.math.PerlinNoiseGenerator;

public class BetterTools extends JavaPlugin{
	public PerlinNoiseGenerator noiseGen;

	@Override
	public void onEnable() {
		getCommand("bt").setExecutor(new Commands());
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		noiseGen = new PerlinNoiseGenerator();
		BlockMath.initMaterialIds();
		
	}
}
