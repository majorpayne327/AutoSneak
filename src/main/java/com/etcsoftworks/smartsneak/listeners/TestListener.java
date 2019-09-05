package com.etcsoftworks.smartsneak.listeners;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class TestListener extends PacketAdapter {
	
	public TestListener(Plugin plugin) {
		super(
				plugin, 
				ListenerPriority.LOW, 
				PacketType.Play.Server.ENTITY
				);
	}
	
	public void onPacketSending(PacketEvent event) {
		
		
		PacketContainer packet = event.getPacket();

		getPluginLogger().info(String.format("---onPacketSending--- Packet Type: %s", packet.getType()));
		int entityId = packet.getIntegers().size() != 0 ? packet.getIntegers().read(0) : -1;
		Entity entity = getEntity(entityId);
		getPluginLogger().info(String.format("Client Packet Source -- ID: %d Name: %s", entityId, entity != null ? entity.getName(): ""));
		getPluginLogger().info(String.format("Packet Target: %s", event.getPlayer().getName()));
	}
		
	
	public void onPacketReceiving(PacketEvent event) {
				
		PacketContainer packet = event.getPacket();

		getPluginLogger().info(String.format("---OnPacketReceiving--- Packet Type: %s", packet.getType()));
		getPluginLogger().info(String.format("Client Packet Source: %d", packet.getIntegers().size() != 0 ? packet.getIntegers().read(0) : -1));
		getPluginLogger().info(String.format("Packet Target: %s", event.getPlayer().getName()));
		
		getPluginLogger().info("\n");
	}
	
	private Entity getEntity(int entityId) {
		for (World world: Bukkit.getWorlds()) {
			for (Entity entity: world.getEntities()) {
				if (entity.getEntityId() == entityId) {
					return entity;
				}
			}
		}
		
		return null;
	}
	
	private Logger getPluginLogger() {
		return this.getPlugin().getLogger();
	}
}
