package com.corundumstudio.socketio.store.room;

import java.util.Set;
import java.util.UUID;

public interface RoomClientsCenterStore {

	public void join( UUID sessionId, String room );
	
	public void leave( UUID sessionId, String room );

	public void onClientPing( UUID sessionId );
	
	public void removeClient( UUID sessionId );
	
	public Set<UUID> getRoomClients( String room );
	
	public Set<String> getClientRooms( UUID sessionId );
	
	public Set<String> getAllRooms();
	
	public boolean checkRoomExist( String room );
	
}
