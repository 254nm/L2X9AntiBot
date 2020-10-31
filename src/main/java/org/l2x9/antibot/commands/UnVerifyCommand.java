package org.l2x9.antibot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.l2x9.antibot.util.Utils;

public class UnVerifyCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("l2x9antibot.unwhitelist")) {
			if (args.length > 0) {
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if (Utils.checkIfPlayerIsVerified(target)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3[L2X9AntiBot]&r&6 Unverified the player &r&6" + target.getName()));
					Utils.unVerifyPlayer(target);
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3[L2X9AntiBot]&r&c" + target.getName() + "&r&6 Is not already verified"));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error&r&cToo few arguments /unverify <playerName>"));
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No permission");
		}
		return true;
	}
}
