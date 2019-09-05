package com.etcsoftworks.smartsneak.listeners.client;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.etcsoftworks.smartsneak.PlayerManager;
import com.etcsoftworks.smartsneak.SmartSneak;

import net.minecraft.server.v1_14_R1.DataWatcher;
import net.minecraft.server.v1_14_R1.EntityPose;
import net.minecraft.server.v1_14_R1.PacketPlayInEntityAction;

public class ClientLoginListener extends PacketAdapter {
	
	public static final List<PacketType> TRACKED_PACKETTYPES = Arrays.asList(
			PacketType.Login.Client.START,
			PacketType.Login.Client.CUSTOM_PAYLOAD
		);
	
	public ClientLoginListener(Plugin plugin) {
		super(plugin, ListenerPriority.NORMAL, TRACKED_PACKETTYPES);
	}
	
	public void onPacketReceiving(PacketEvent event) {
		
		PacketContainer packet = event.getPacket();

		getPluginLogger().info(String.format("---onPacketReceiving--- Packet Type: %s", packet.getType()));
		getPluginLogger().info(String.format("Client Packet Source: %d", packet.getIntegers().size() != 0 ? packet.getIntegers().read(0) : -1));
		getPluginLogger().info(String.format("Packet Target: %s", event.getPlayer().getName()));
		
		getPluginLogger().info("\n");
	}
	
	private Logger getPluginLogger() {
		return this.getPlugin().getLogger();
	}
}
