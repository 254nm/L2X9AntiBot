package org.l2x9.antibot.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;
import org.l2x9.antibot.AntiBot;
import org.l2x9.antibot.util.CountDown;
import org.l2x9.antibot.util.Utils;

import java.util.List;

public class PlayerJoin implements Listener {
	AntiBot plugin;
	int taskId;
	public PlayerJoin(AntiBot antiBot) {
		plugin = antiBot;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		int time = plugin.getConfig().getInt("Verify-Timeout");
		if (!Utils.checkIfPlayerIsVerified(player)) {
				List<String> rawMessage = plugin.getConfig().getStringList("Unverified-Message");
				String message = String.join("\n", rawMessage);
				Utils.sendMessage(player, message);
			BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new CountDown(event, plugin, taskId, time), 0, 20);
			taskId = task.getTaskId();
		}
	}
}