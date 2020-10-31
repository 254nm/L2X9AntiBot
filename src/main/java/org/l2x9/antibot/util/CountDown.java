package org.l2x9.antibot.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.l2x9.antibot.AntiBot;

public class CountDown implements Runnable {
	PlayerJoinEvent event;
	AntiBot plugin;
	int taskId;
	int time;
	public CountDown(PlayerJoinEvent event, AntiBot plugin, int taskId, int time) {
		this.event = event;
		this.plugin = plugin;
		this.taskId = taskId;
		this.time = time;
	}
	@Override
	public void run() {
		if (event.getPlayer().isOnline()) {
			if (!Utils.checkIfPlayerIsVerified(event.getPlayer())) {
				time--;
				Player player = event.getPlayer();
				if (plugin.getConfig().getBoolean("Spam-the-player-to-whitelist")) {
					String message = String.join("\n", plugin.getConfig().getStringList("Unverified-Message"));
					Utils.sendMessage(player, message);
				}
				player.sendActionBar('&', plugin.getConfig().getString("ActionBar-Message").replace("%time%", "" + time));
				if (time <= 0) {
					String kick = String.join("\n", plugin.getConfig().getStringList("Kick-Message"));
					player.kickPlayer(ChatColor.translateAlternateColorCodes('&', kick));
				}
			} else {
				Bukkit.getScheduler().cancelTask(taskId);
				Utils.sendMessage(event.getPlayer(), plugin.getConfig().getString("Verify-Message"));
			}
		} else {
			Bukkit.getScheduler().cancelTask(taskId);
		}
	}
}
