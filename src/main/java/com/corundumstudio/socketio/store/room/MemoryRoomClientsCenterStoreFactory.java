package com.corundumstudio.socketio.store.room;

public class MemoryRoomClientsCenterStoreFactory implements RoomClientsCenterStoreFactory {

	@Override
	public RoomClientsCenterStore getRoomClientsCenterStore( String namespace, long pingTimeout ) {
		return new MemoryRoomClientsCenterStore( namespace );
	}
	
}
