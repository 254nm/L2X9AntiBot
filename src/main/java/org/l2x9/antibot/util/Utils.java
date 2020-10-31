package org.l2x9.antibot.util;

import com.sun.net.httpserver.HttpExchange;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Utils {
	private static final List<String> verifiedPlayers = FileUtils.getVerifiedConfig().getStringList("Verified-Players");
	public static void verifyPlayer(OfflinePlayer player) {
		String name = player.getName();
		if (!verifiedPlayers.contains(name)) {
			verifiedPlayers.add(name);
			FileUtils.getVerifiedConfig().set("Verified-Players", verifiedPlayers);
			try {
				FileUtils.getVerifiedConfig().save(FileUtils.getFiles()[3]);
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	public static void unVerifyPlayer(OfflinePlayer player) {
		String name = player.getName();
		if (verifiedPlayers.contains(name)) {
			verifiedPlayers.remove(name);
			FileUtils.getVerifiedConfig().set("Verified-Players", verifiedPlayers);
			try {
				FileUtils.getVerifiedConfig().save(FileUtils.getFiles()[3]);
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	public static boolean checkIfPlayerIsVerified(OfflinePlayer player) {
		return verifiedPlayers.contains(player.getName());
	}
	public static void writeResponse(HttpExchange exchange, String response, int code) {
		try {
			exchange.sendResponseHeaders(code, response.length());
			OutputStream stream = exchange.getResponseBody();
			stream.write(response.getBytes());
			stream.flush();
			stream.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	public static void sendMessage(Player player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
}
