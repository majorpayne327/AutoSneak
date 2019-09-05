package com.etcsoftworks.smartsneak.listeners.server;

import java.util.ArrayList;
import java.util.List;
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
import com.etcsoftworks.smartsneak.PlayerManager;
import com.etcsoftworks.smartsneak.SmartSneak;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import net.minecraft.server.v1_14_R1.EntityPose;

public class EntityMetadataListener extends PacketAdapter {

	public EntityMetadataListener(Plugin plugin) {
		super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_METADATA);
	}
	
	public void onPacketSending(PacketEvent event) {

		PacketContainer packet = event.getPacket();

		getPluginLogger().info(String.format("---onPacketSending--- Packet Type: %s", packet.getType()));
		int entityId = packet.getIntegers().size() != 0 ? packet.getIntegers().read(0) : -1;
		Entity entity = getEntity(entityId);
		getPluginLogger().info(String.format("Packet Source: %s", entity != null ? entity.getName() : ""));
		getPluginLogger().info(String.format("Packet Target: %s", event.getPlayer().getName()));
		
		if(!PlayerManager.getInstance().containsId(packet.getIntegers().read(0))) {

			getPluginLogger().info(String.format("PlayerPoses: %s", PlayerManager.getInstance()));
			getPluginLogger().info(String.format("--IGNORED--"));
			return;
		}
		
		getPluginLogger().info(String.format("isSneaking: %s; isSwimming: %s", event.getPlayer().isSneaking(), event.getPlayer().isSwimming()));
		
		getPluginLogger().info(String.format("Watchable Collection Modifiers: %d", packet.getWatchableCollectionModifier().size()));
		if(packet.getWatchableCollectionModifier().size() != 0) {
			
			getPluginLogger().info(String.format("Modifiers: %s", packet.getWatchableCollectionModifier().read(0).toString()));
			
			List<WrappedWatchableObject> newData = new ArrayList<WrappedWatchableObject>();
			for(WrappedWatchableObject modifier: packet.getWatchableCollectionModifier().read(0)) {
				getPluginLogger().info(String.format("Modifier: %s", modifier.toString()));
				
				if(isRunning(modifier)) {
					// Need to send new packet for standing?
					getPluginLogger().info(String.format("Setting Player to %s", EntityPose.STANDING));
					break;
				}
				
				if(isStanding(modifier)) {
					int playerId = packet.getIntegers().read(0);
					EntityPose pose = PlayerManager.getInstance().getPlayerPose(playerId);
					getPluginLogger().info(String.format("Setting Player to %s", pose));
					modifier.setValue(pose);
					break;
				}
				
				if(isSwimming(modifier)) {
//					int playerId = packet.getIntegers().read(0);
//					EntityPose pose = PlayerManager.getInstance().getPlayerPose(playerId);
//					getPluginLogger().info(String.format("Setting Player to %s", pose));
					modifier.setValue((byte) 0x02);					
					break;
				}
			}
			
			if(newData.size() > 0) {
				packet.getWatchableCollectionModifier().read(0).addAll(newData);
			}
			getPluginLogger().info(String.format("New Modifiers: %s", packet.getWatchableCollectionModifier().read(0).toString()));
		}
		
		getPluginLogger().info("---------------------");
	}
	
	private boolean isRunning(WrappedWatchableObject watchable) {
		return watchable.getIndex() == 0 && (byte) watchable.getValue() == 0x08;
	}
	
	private boolean isStanding(WrappedWatchableObject watchable) {
		return watchable.getIndex() == 6 && ((EntityPose) watchable.getValue()) == EntityPose.STANDING;
	}
	
	private boolean isSwimming(WrappedWatchableObject watchable) {
		return watchable.getIndex() == 0 && (byte) watchable.getValue() == 0x08;
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

