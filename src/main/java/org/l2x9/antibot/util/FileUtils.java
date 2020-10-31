package org.l2x9.antibot.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.l2x9.antibot.AntiBot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileUtils {
	private static AntiBot plugin;
	private static File index;
	private static File passed;
	private static File failed;
	private static File verified;
	private static File notFound;
	private static FileConfiguration verifiedConfig;
	public static void loadHTML() {
		try {
			File htmlFolder = new File(plugin.getDataFolder(), "HTMLFiles");
			index = new File(htmlFolder, "index.html");
			passed = new File(htmlFolder, "passed.html");
			failed = new File(htmlFolder, "failed.html");
			notFound = new File(htmlFolder, "notfound.html");
			verified = new File(plugin.getDataFolder(), "Verified.yml");
			verifiedConfig = new YamlConfiguration();
			if (!htmlFolder.exists()) {
				htmlFolder.mkdir();
			}
			if (!index.exists()) {
				InputStream indexStream = plugin.getResource("index.html");
				Files.copy(indexStream, index.toPath());
			}
			if (!passed.exists()) {
				InputStream passedStream = plugin.getResource("passed.html");
				Files.copy(passedStream, passed.toPath());
			}
			if (!failed.exists()) {
				InputStream failedStream = plugin.getResource("failed.html");
				Files.copy(failedStream, failed.toPath());
			}
			if (!verified.exists()) {
				InputStream verifiedStream = plugin.getResource("verified.yml");
				Files.copy(verifiedStream, verified.toPath());
			}
			verifiedConfig.load(verified);
			if (!notFound.exists()) {
				InputStream notFoundStream = plugin.getResource("notfound.html");
				Files.copy(notFoundStream, notFound.toPath());
			}
		} catch (IOException | InvalidConfigurationException ioException) {
			ioException.printStackTrace();
		}
	}

	public static FileConfiguration getVerifiedConfig() {
		return verifiedConfig;
	}

	public static void setPlugin(AntiBot plugin) {
		FileUtils.plugin = plugin;
	}
	public static File[] getFiles() {
		return new File[] { index, passed, failed, verified, notFound };
	}
}
