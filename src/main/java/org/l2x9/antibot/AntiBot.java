package org.l2x9.antibot;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.l2x9.antibot.events.ChatEvent;
import org.l2x9.antibot.events.CommandEvent;
import org.l2x9.antibot.events.MoveEvent;
import org.l2x9.antibot.events.PlayerJoin;
import org.l2x9.antibot.util.FileUtils;
import org.l2x9.antibot.webserver.Pages;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class AntiBot extends JavaPlugin {
	HttpServer webServer;
	PluginManager pluginManager = getServer().getPluginManager();
	@Override
	public void onEnable() {
		saveDefaultConfig();
		FileUtils.setPlugin(this);
		FileUtils.loadHTML();
		try {
			int port = getConfig().getInt("Port");
			webServer = HttpServer.create(new InetSocketAddress(port), 0);
			webServer.createContext("/", new Pages());
			webServer.createContext("/submit", new Pages.Submit(this));
			webServer.start();
			getLogger().info(ChatColor.translateAlternateColorCodes('&', "&cWebServer&a Started on port " + webServer.getAddress().getPort()));
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		pluginManager.registerEvents(new ChatEvent(this), this);
		pluginManager.registerEvents(new CommandEvent(this), this);
		pluginManager.registerEvents(new MoveEvent(), this);
		pluginManager.registerEvents(new PlayerJoin(this), this);
	}
	@Override
	public void onDisable() {
		webServer.stop(0);
		getLogger().info(ChatColor.translateAlternateColorCodes('&', "&cWebServer&a Stopped"));
	}
}
