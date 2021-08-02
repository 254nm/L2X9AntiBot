package org.l2x9.antibot.webserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.l2x9.antibot.AntiBot;
import org.l2x9.antibot.util.FileUtils;
import org.l2x9.antibot.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;

public class Pages implements HttpHandler {
    //This is the index
    private String index;

    public Pages() {
        try {
            File index = FileUtils.getFiles()[0];
            InputStream fileAsStream = new FileInputStream(index);
            this.index = Utils.toString(fileAsStream);
            fileAsStream.close();
        } catch (Throwable t) {
        }
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.writeResponse(exchange, index, 200);
    }

    //This handles verifying the player
    public static class Submit implements HttpHandler {
        private static AntiBot plugin;
        private String notFound;
        private String passed;
        private String failed;

        public Submit(AntiBot antiBot) {
            plugin = antiBot;
            try {
                File notFound = FileUtils.getFiles()[4];
                InputStream nfas = new FileInputStream(notFound);
                this.notFound = Utils.toString(nfas);
                nfas.close();
                File failed = FileUtils.getFiles()[2];
                InputStream failedStream = new FileInputStream(failed);
                this.failed = Utils.toString(failedStream);
                failedStream.close();
                File passed = FileUtils.getFiles()[1];
                InputStream passedStream = new FileInputStream(passed);
                this.passed = Utils.toString(passedStream);
                passedStream.close();

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        private boolean isCaptchaValid(String secretKey, String response) {
            try {
                String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + secretKey + "&response=" + response;
                InputStream stream = (new URL(url)).openStream();
                String raw = Utils.toString(stream);
                stream.close();
                JsonElement json = (new JsonParser()).parse(raw);
                return json.getAsJsonObject().get("success").getAsBoolean();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestURI().getRawQuery() == null) {
                Utils.writeResponse(exchange, notFound, 404);
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
                        Utils.verifyPlayer(offlinePlayer);
                        plugin.getLogger().log(Level.INFO, ChatColor.GREEN + username + " Verified Successfully");
                        response = passed;
                    } else {
                        response = failed;
                    }
                } else {
                    response = failed;
                }
                Utils.writeResponse(exchange, response, 200);
            }
        }
    }
}