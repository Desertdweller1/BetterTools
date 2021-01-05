package me.desertdweller.bettertools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

public class PlayerJoinListener implements Listener{

	@EventHandler
	public static void onPlayerJoinEvent(PlayerJoinEvent e) {
		System.out.println(e.getPlayer().hasPermission("bt.create"));
		System.out.println(e.getPlayer().isOp());
		System.out.println(Bukkit.getPluginManager().getPlugin("BetterTools").getConfig().getBoolean("joinMessages"));
		if((e.getPlayer().hasPermission("bt.create") || e.getPlayer().isOp()) && Bukkit.getPluginManager().getPlugin("BetterTools").getConfig().getBoolean("joinMessages")) {
			e.getPlayer().sendMessage(ChatColor.GREEN + "BetterTools V" + Bukkit.getPluginManager().getPlugin("BetterTools").getDescription().getVersion() + " is enabled, "
					+ "use '/bt tool' to get started, or '/bt help' for more information.");
		}
	}
	
}
