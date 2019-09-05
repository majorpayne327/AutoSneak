package com.etcsoftworks.smartsneak.listeners.server;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.etcsoftworks.smartsneak.PlayerManager;

import net.minecraft.server.v1_14_R1.EntityPose;

public class PlayerLoginListener extends PacketAdapter {

	public static final List<PacketType> TRACKED_PACKETTYPES = Arrays.asList(
		PacketType.Login.Server.SUCCESS
	);
	
	public PlayerLoginListener(Plugin plugin) {
		super(plugin, ListenerPriority.NORMAL, TRACKED_PACKETTYPES);
	}
	
	public void onPacketSending(PacketEvent event) {

		PacketContainer packet = event.getPacket();

		getPluginLogger().info(String.format("---onPacketSending--- Packet Type: %s", packet.getType()));
		getPluginLogger().info(String.format("Packet Target: %s", event.getPlayer().getName()));
		
		PlayerManager.getInstance().addPlayerPose(event.getPlayer().getEntityId(), EntityPose.SNEAKING);
		
		getPluginLogger().info("---------------------");
	}
	
	private Logger getPluginLogger() {
		return this.getPlugin().getLogger();
	}
}

