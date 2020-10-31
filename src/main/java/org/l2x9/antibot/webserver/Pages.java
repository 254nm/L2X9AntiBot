package org.l2x9.antibot.webserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.l2x9.antibot.AntiBot;
import org.l2x9.antibot.util.FileUtils;
import org.l2x9.antibot.util.Utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class Pages implements HttpHandler {
	//This is the index
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		File index = FileUtils.getFiles()[0];
		InputStream fileAsStream = new FileInputStream(index);
		String response = IOUtils.toString(fileAsStream, StandardCharsets.UTF_8);
		Utils.writeResponse(exchange, response, 200);
		fileAsStream.close();
	}
	//This handles verifying the player
	public static class Submit implements HttpHandler {
		private static AntiBot plugin;
		public Submit(AntiBot antiBot) {
			plugin = antiBot;
		}
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			if (exchange.getRequestURI().getRawQuery() == null) {
				File notFound = FileUtils.getFiles()[4];
				InputStream fileAsStream = new FileInputStream(notFound);
				String response = IOUtils.toString(fileAsStream, StandardCharsets.UTF_8);
				Utils.writeResponse(exchange, response, 404);
			} else {
				String response;
				String[] rawReq = exchange.getRequestURI().getRawQuery().split("&");
				String username = rawReq[0].replace("username=", "");
				String key = plugin.getConfig().getString("Secret-Key");
				String gCaptchaResponse = rawReq[1].replace("g-recaptcha-response=", "");
				boolean valid = isCaptchaValid(key, gCaptchaResponse);
				if (valid) {
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
					if (!Utils.checkIfPlayerIsVerified(offlinePlayer)) {
						//If the captcha the user entered is valid
						File passed = FileUtils.getFiles()[1];
						InputStream passedStream = new FileInputStream(passed);
						Utils.verifyPlayer(offlinePlayer);
						plugin.getLogger().log(Level.INFO, ChatColor.GREEN + username + " Verified Successfully");
						response = IOUtils.toString(passedStream);
						passedStream.close();
					} else {
						File failed = FileUtils.getFiles()[2];
						InputStream failedStream = new FileInputStream(failed);
						response = IOUtils.toString(failedStream);
						failedStream.close();
					}
				} else {
					File failed = FileUtils.getFiles()[2];
					InputStream failedStream = new FileInputStream(failed);
					response = IOUtils.toString(failedStream);
					failedStream.close();
				}
				Utils.writeResponse(exchange, response, 200);
			}
		}
		public static boolean isCaptchaValid(String secretKey, String response) {
			try {
				String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + secretKey + "&response=" + response;
				InputStream stream = (new URL(url)).openStream();
				String raw = IOUtils.toString(stream);
				stream.close();
				JsonElement json = (new JsonParser()).parse(raw);
				return json.getAsJsonObject().get("success").getAsBoolean();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}