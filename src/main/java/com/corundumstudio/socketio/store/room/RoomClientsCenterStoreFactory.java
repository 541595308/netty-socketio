package com.corundumstudio.socketio.store.room;

public interface RoomClientsCenterStoreFactory {

	public RoomClientsCenterStore getRoomClientsCenterStore( String namespace, long pingTimeout );
	
}
