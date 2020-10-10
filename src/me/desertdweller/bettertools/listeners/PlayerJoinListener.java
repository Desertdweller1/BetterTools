package me.desertdweller.bettertools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

public class PlayerJoinListener implements Listener{

	public static void onPlayerJoinEvent(PlayerJoinEvent e) {
		if(e.getPlayer().hasPermission("bt.create")) {
			e.getPlayer().sendMessage(ChatColor.YELLOW + "Better Tools " + Bukkit.getPluginManager().getPlugin("BetterTools").getDescription().getVersion() + " is currently running. Use /bt help to get started.");
		}
	}
	
}
