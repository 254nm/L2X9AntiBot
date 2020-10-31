package org.l2x9.antibot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.l2x9.antibot.AntiBot;

public class ReloadConfig implements CommandExecutor {
	AntiBot plugin;

	public ReloadConfig(AntiBot plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("l2x9antibot.reload")) {
			sender.sendMessage(ChatColor.GREEN + "[L2X9AntiBot] Config reloaded");
			plugin.reloadConfig();
		} else {
			sender.sendMessage(ChatColor.RED + "No permission");
		}
		return true;
	}
}
