package com.etcsoftworks.smartsneak.listeners.client;

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

public class PlayerActionListener extends PacketAdapter {

	public PlayerActionListener(Plugin plugin) {
		super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.ENTITY_ACTION);
	}
	
	public void onPacketReceiving(PacketEvent event) {
		
		getPluginLogger().info("---OnPacketReceiving---");
		
		PacketContainer packet = event.getPacket();
		if(packet.getPlayerActions().size() > 0) {

			getPluginLogger().info(String.format("Id: %d, Player: %s", packet.getIntegers().read(0), event.getPlayer().getName()));
			
			int playerId = packet.getIntegers().read(0);
			PlayerAction action = packet.getPlayerActions().read(0);
			
			getPluginLogger().info(String.format("Action: %s", action));
			
			EntityPose pose;
			switch(action) {
			case START_SPRINTING:
				pose = event.getPlayer().isSwimming() ? EntityPose.SWIMMING : EntityPose.STANDING;
				getPluginLogger().info(String.format("New Pose: %s", pose));
				break;
			default:
				pose = EntityPose.SNEAKING;
				getPluginLogger().info(String.format("New Pose: %s", pose));
				break;
			}
			
			PlayerManager.getInstance().addPlayerPose(playerId, pose);
			
		}
		
		getPluginLogger().info("\n");
//		
//		Player player = event.getPlayer();
//		getPluginLogger().info(String.format("%s -- Sneaking=%b; Sprinting=%b", player.getName(), player.isSneaking(), player.isSprinting()));
//		getPluginLogger().info(String.format("Metadata - %s", player.hasMetadata("EntityMetadata")));
//		
//		getPluginLogger().info(event.getPacketType().name());
//		
//		getPluginLogger().info("-Actions-");
//		PacketContainer packet = event.getPacket();
//		for (PlayerAction action: packet.getPlayerActions().getValues()) {
//			getPluginLogger().info(action.toString());
//		}
//		
//		getPluginLogger().info("-Packet Integers-");
//		for(Integer data: packet.getIntegers().getValues()) {
//			getPluginLogger().info(String.format("Integer: %d", data));
//		}
//		
//		getPluginLogger().info(String.format("Packet Integers: %d", packet.getIntegers().size()));
//
//		getPluginLogger().info(String.format("Packet Integer Arrays: %d", packet.getIntegerArrays().size()));
//
//		getPluginLogger().info(String.format("Packet Booleans: %d", packet.getBooleans().size()));
//
//		getPluginLogger().info(String.format("Packet ByteArrays: %d", packet.getByteArrays().size()));
//
//		getPluginLogger().info(String.format("Packet Bytes: %d", packet.getBytes().size()));
//
//		getPluginLogger().info(String.format("Packet Doubles: %d", packet.getDoubles().size()));
//
//		getPluginLogger().info(String.format("Packet UUIDs: %d", packet.getUUIDs().size()));
//
//		getPluginLogger().info(String.format("Packet Strings: %d", packet.getStrings().size()));
//		
//		getPluginLogger().info(String.format("Packet String Arrays: %d", packet.getStringArrays().size()));
		
//		PacketContainer newPacket = new PacketContainer(PacketType.Play.Client.ENTITY_ACTION);
//		newPacket.getPlayerActions().write(0, PlayerAction.START_SNEAKING);
//		event.setPacket(newPacket);
//		
//		getPluginLogger().info(String.format("New Packet Object Handle -- %d", ((PacketPlayInEntityAction) newPacket.getHandle()).d()));
//		getPluginLogger().info("------------------");
	}
	
	private Logger getPluginLogger() {
		return this.getPlugin().getLogger();
	}
	
//	public void fakePlayerCrouch(Player player, boolean fakeCrouching) {
//        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
//
//        // Create a new DataWatcher.
//        // NOTE: this will clear previous dataWatching from other clients e.g. fire animation will be removed
//        // if they were on fire.
//        DataWatcher rawDataWatcher = new DataWatcher(entityPlayer);
//
//        // First param defines the index for the DataWatcher and the second defines the data type (Byte in our case)
//        // Go here for values: https://wiki.vg/Entity_metadata#Entity_Metadata_Format
//        DataWatcherObject<Byte> basicEntityData = new DataWatcherObject<>(0, DataWatcherRegistry.a);
//
//        // Index 0 is an 8-bit bitmask. 2nd to LSB defines crouch boolean
//        byte bitMaskData = (byte) (fakeCrouching ? 0b0000_0010 : 0b0000_0000);
//
//        // Register our bitmaskdata as default so we don't have to set it again later
//        rawDataWatcher.register(basicEntityData, bitMaskData);
//
//        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
//            if (otherPlayer != player) {
//                // First param: crouching player ID
//                // Second param: the DataWatcher object
//                // Third param: whether to check if datawatcher has 'dirty' objects (objects that have updated, but
//                // have not been sent to the client)
//                PacketPlayOutEntityMetadata metadataPacket =
//                        new PacketPlayOutEntityMetadata(entityPlayer.getId(), rawDataWatcher, false);
//                ((CraftPlayer) otherPlayer).getHandle().playerConnection.sendPacket(metadataPacket);
//            }
//        }
//    }
}
