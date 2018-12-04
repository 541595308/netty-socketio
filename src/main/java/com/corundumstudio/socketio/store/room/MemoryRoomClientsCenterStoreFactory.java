package com.corundumstudio.socketio.store.room;

import com.corundumstudio.socketio.scheduler.CancelableScheduler;

public class MemoryRoomClientsCenterStoreFactory extends RoomClientsCenterStoreFactory<MemoryRoomClientsCenterStore> {

	@Override
	public void init(CancelableScheduler scheduler) {}

	@Override
	MemoryRoomClientsCenterStore createRoomClientsCenterStore(String namespace, long pingTimeout) {
		return new MemoryRoomClientsCenterStore( namespace );
	}
	
}
