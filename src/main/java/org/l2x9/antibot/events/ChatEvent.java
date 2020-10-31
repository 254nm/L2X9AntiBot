package org.l2x9.antibot.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.l2x9.antibot.AntiBot;
import org.l2x9.antibot.util.Utils;

public class ChatEvent implements Listener {
	AntiBot plugin;
	public ChatEvent(AntiBot antiBot) {
		plugin = antiBot;
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (!Utils.checkIfPlayerIsVerified(event.getPlayer())) {
			event.setCancelled(true);
			Player player = event.getPlayer();
			String kick = String.join("\n", plugin.getConfig().getStringList("Kick-Message"));
			Bukkit.getScheduler().runTask(plugin, () -> player.kickPlayer(ChatColor.translateAlternateColorCodes('&', kick)));
		}
	}
}