package com.corundumstudio.socketio.store.room;

import java.util.HashMap;
import java.util.Map;

import com.corundumstudio.socketio.scheduler.CancelableScheduler;

public abstract class RoomClientsCenterStoreFactory<T extends RoomClientsCenterStore> {

	protected Map<String, T> stores = new HashMap<>();
	
	public abstract void init( CancelableScheduler scheduler );
	
	public T getRoomClientsCenterStore( String namespace, long pingTimeout ) {
		T store = this.stores.get( namespace );
		if( store != null ) {
			return store;
		}
		store = this.createRoomClientsCenterStore( namespace, pingTimeout );
		this.stores.put( namespace, store );
		return store;
	}
	
	abstract T createRoomClientsCenterStore( String namespace, long pingTimeout );
	
}
