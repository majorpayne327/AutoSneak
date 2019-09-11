package com.etcsoftworks.smartsneak.listeners.server;

import java.util.logging.Logger;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.etcsoftworks.smartsneak.PlayerManager;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;

import net.minecraft.server.v1_14_R1.EntityPose;

public class EntityMetadataListener extends PacketAdapter {

	public EntityMetadataListener(Plugin plugin) {
		super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_METADATA);
	}
	
	public void onPacketSending(PacketEvent event) {

		PacketContainer packet = event.getPacket();

		getLogger().fine(String.format("---onPacketSending--- Packet Type: %s", packet.getType()));
		Entity entity = packet.getEntityModifier(event.getPlayer().getWorld()).read(0);
		getLogger().fine(String.format("Packet Source: %s", entity != null ? entity.getName() : ""));
		getLogger().fine(String.format("Packet Target: %s", event.getPlayer().getName()));
		
		if(containsMetadata(packet)) {
			
			getLogger().fine(String.format("Watchable Collection Modifiers: %d", packet.getWatchableCollectionModifier().size()));
			getLogger().fine(String.format("Modifiers: %s", packet.getWatchableCollectionModifier().read(0).toString()));
			
			WrappedDataWatcher dataWatcher = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
			
			getLogger().fine(String.format("DataWatcherIndicies: %s", dataWatcher.getIndexes().toString()));

			if(isRunning(dataWatcher)) {
				getLogger().fine(String.format("Setting Player to %s", EntityPose.STANDING));
				dataWatcher.setObject(6, Registry.get(EntityPose.class), EntityPose.STANDING);
			} else if(isStanding(dataWatcher)) {
				getLogger().fine(String.format("Setting Player to %s", EntityPose.SNEAKING));
				dataWatcher.setObject(0, Registry.get(Byte.class), (byte) 0x02);
				dataWatcher.setObject(6, Registry.get(EntityPose.class), EntityPose.SNEAKING);
			} else if(isFlying(dataWatcher)) {
				dataWatcher.setObject(6, Registry.get(EntityPose.class), EntityPose.SNEAKING);
//			} else if(isSwimming(dataWatcher)) {
//				dataWatcher.setObject(6, Registry.get(EntityPose.class), EntityPose.SNEAKING);
			} else if(isSleeping(dataWatcher)) {
				dataWatcher.setObject(0, Registry.get(Byte.class), (byte) 0x02);
			}
			
			getLogger().fine(String.format("Modified DataWatcherIndicies: %s", dataWatcher.getIndexes().toString()));
			
			getLogger().fine(String.format("New Modifiers: %s", dataWatcher.getWatchableObjects().toString()));
			packet.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

			
		} else {
			getLogger().fine(String.format("PlayerPoses: %s", PlayerManager.getInstance()));
			getLogger().fine(String.format("--IGNORED--"));
		}
		
		getLogger().fine("---------------------");
	}
	
	private boolean containsMetadata(PacketContainer packet) {
		return packet.getWatchableCollectionModifier().size() == 1;
	}
	
	private boolean playerIsRegisteredToPlugin() {
		return false;
	}
	
	private boolean isRunning(WrappedDataWatcher dataWatcher) {
		return dataWatcher.hasIndex(0) && dataWatcher.getByte(0) == 0x08;
	}
	
	private boolean isStanding(WrappedDataWatcher dataWatcher) {
		return (dataWatcher.hasIndex(0) && dataWatcher.getByte(0) == 0) || (dataWatcher.hasIndex(6) && (EntityPose) dataWatcher.getObject(6) == EntityPose.STANDING);
	}
	
	private boolean isFlying(WrappedDataWatcher dataWatcher) {
		return dataWatcher.hasIndex(6) && (EntityPose) dataWatcher.getObject(6) == EntityPose.FALL_FLYING;
	}
	
	private boolean isSwimming(WrappedDataWatcher dataWatcher) {
		return (dataWatcher.hasIndex(0) && dataWatcher.getByte(0) == 0x10) || (dataWatcher.hasIndex(6) && (EntityPose) dataWatcher.getObject(6) == EntityPose.SWIMMING);
	}	
	
	private boolean isSleeping(WrappedDataWatcher dataWatcher) {
		return dataWatcher.hasIndex(6) && (EntityPose) dataWatcher.getObject(6) == EntityPose.SLEEPING;
	}
	
	private Logger getLogger() {
		return this.getPlugin().getLogger();
	}
}

