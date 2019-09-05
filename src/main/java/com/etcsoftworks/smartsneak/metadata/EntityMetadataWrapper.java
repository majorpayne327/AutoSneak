package com.etcsoftworks.smartsneak.metadata;

import com.comphenix.protocol.wrappers.WrappedWatchableObject;

public class EntityMetadataWrapper {
	
	private final int EFFECT_IDX = 0;
	private final int POSE_IDX = 6;
	
	private WrappedWatchableObject wrappedObject;
	
	public EntityMetadataWrapper(WrappedWatchableObject wrappedObject) {
		this.wrappedObject = wrappedObject;
	}
}
