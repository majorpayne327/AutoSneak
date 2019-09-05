package com.etcsoftworks.smartsneak;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.server.v1_14_R1.EntityPose;

public class PlayerManager {
	
	private static Logger logger = Logger.getLogger("PlayerManager");
	private static PlayerManager instance;
	
	public static PlayerManager getInstance() {
		if (instance == null) {
			logger.info("Creating new PlayerManager");
			instance = new PlayerManager();
		}
		return instance; 
	}
	
	private Map<Integer, EntityPose> playerPoses;
	
	private PlayerManager() {
		playerPoses = new HashMap<Integer, EntityPose>();
	}
	
	public EntityPose getPlayerPose(int entityId) {
		return playerPoses.getOrDefault(entityId, EntityPose.SNEAKING);
	}
	
	public boolean containsId(int entityId) {
		return playerPoses.containsKey(entityId);
	}
	
	public EntityPose addPlayerPose(int entityId, EntityPose pose){
		return playerPoses.put(entityId, pose);
	}
	
	public void removePlayer(int entityId){
		playerPoses.remove(entityId);
	}
	
	public String toString() {
		return playerPoses.toString();
	}
}
