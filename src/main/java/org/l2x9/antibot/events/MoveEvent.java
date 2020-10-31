package org.l2x9.antibot.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.l2x9.antibot.util.Utils;

public class MoveEvent implements Listener {
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (!Utils.checkIfPlayerIsVerified(event.getPlayer())) {
			Location to = event.getFrom();
			to.setPitch(event.getTo().getPitch());
			to.setYaw(event.getTo().getYaw());
			event.setTo(to);
		}
	}
}
