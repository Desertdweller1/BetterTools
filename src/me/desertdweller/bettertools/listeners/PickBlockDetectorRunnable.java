package me.desertdweller.bettertools.listeners;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PickBlockDetectorRunnable extends BukkitRunnable implements Listener{
	private static JavaPlugin plugin;
	private static HashMap<Player, Inventory> previousCreativeInvs = new HashMap<Player, Inventory>();
	
	public PickBlockDetectorRunnable(JavaPlugin plugin) {
		PickBlockDetectorRunnable.plugin = plugin;
	}

	@Override
	public void run() {
		for(Player player : plugin.getServer().getOnlinePlayers()) {
			if(player.getGameMode() == GameMode.CREATIVE) {
				
				
			//If player is not in creative, clear them from the list.
			}else if(previousCreativeInvs.containsKey(player)) {
				previousCreativeInvs.remove(player);
			}
		}
	}
}
