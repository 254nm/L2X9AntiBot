package org.l2x9.antibot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.l2x9.antibot.util.Utils;

public class VerifyCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("l2x9antibot.whitelist")) {
			if (args.length > 0) {
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if (!Utils.checkIfPlayerIsVerified(target)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3[L2X9AntiBot]&r&6 Verified the player &r&6" + target.getName()));
					Utils.verifyPlayer(target);
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3[L2X9AntiBot]&r&c" + target.getName() + "&r&6 Is already verified"));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error&r&cToo few arguments /verify <playerName>"));
			}
		} else {
			sender.sendMessage(ChatColor.RED + "No permission");
		}
		return true;
	}
}
