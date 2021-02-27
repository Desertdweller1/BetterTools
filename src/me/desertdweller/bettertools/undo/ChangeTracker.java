package me.desertdweller.bettertools.undo;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

public class ChangeTracker {
	private static ArrayList<ChangeTracker> playerChanges = new ArrayList<ChangeTracker>();
	private UUID player;
	private ArrayList<Alteration> changes = new ArrayList<Alteration>();


	public ChangeTracker(UUID player) {
		this.player = player;
		playerChanges.add(this);
	}

	public void addChange(Alteration alteration) {
		changes.add(alteration);
		if(changes.size() > Bukkit.getServer().getPluginManager().getPlugin("BetterTools").getConfig().getInt("maxUndos")) {
			changes.remove(0);
		}
	}

	public int undo() {
		Alteration targetArea = changes.get(changes.size()-1);
		changes.remove(changes.size()-1);
		return targetArea.revert(Bukkit.getPlayer(player));
	}

	public UUID getPlayer() {
		return player;
	}
	
	public int getUndosAvailable() {
		return changes.size();
	}

	public static ChangeTracker getChangesForPlayer(UUID player) {
		for(ChangeTracker tracker : playerChanges) {
			if(tracker.getPlayer().equals(player)) {
				return tracker;
			}
		}
		return null;
	}
}