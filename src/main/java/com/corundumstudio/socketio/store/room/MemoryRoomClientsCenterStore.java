package com.corundumstudio.socketio.store.room;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import io.netty.util.internal.PlatformDependent;

public class MemoryRoomClientsCenterStore implements RoomClientsCenterStore {
 
	private final ConcurrentMap<String, Set<UUID>> roomClients = PlatformDependent.newConcurrentHashMap();
    private final ConcurrentMap<UUID, Set<String>> clientRooms = PlatformDependent.newConcurrentHashMap();
	
	MemoryRoomClientsCenterStore( String namespace ) {}

	@Override
	public void join(UUID sessionId, String roomId) {
		join(roomClients, roomId, sessionId);
		join(clientRooms, sessionId, roomId);
	}

	private <K, V> void join(ConcurrentMap<K, Set<V>> map, K key, V value) {
        Set<V> clients = map.get(key);
        if (clients == null) {
            clients = Collections.newSetFromMap(PlatformDependent.<V, Boolean>newConcurrentHashMap());
            Set<V> oldClients = map.putIfAbsent(key, clients);
            if (oldClients != null) {
                clients = oldClients;
            }
        }
        clients.add(value);
        // object may be changed due to other concurrent call
        if (clients != map.get(key)) {
            // re-join if queue has been replaced
            join(map, key, value);
        }
    }
	
	
	@Override
	public void leave(UUID sessionId, String roomId) {
		leave(roomClients, roomId, sessionId);
        leave(clientRooms, sessionId, roomId);
	}

	private <K, V> void leave(ConcurrentMap<K, Set<V>> map, K room, V sessionId) {
        Set<V> clients = map.get(room);
        if (clients == null) {
            return;
        }
        clients.remove(sessionId);

        if (clients.isEmpty()) {
            map.remove(room, clients);
        }
    }
	
	
	@Override
	public void onClientPing(UUID sessionId) {}

	@Override
	public void removeClient( UUID sessionId ) {
		Set<String> roomIds = this.clientRooms.remove( sessionId );
		if( roomIds != null && roomIds.size() > 0 ) {
			for( String roomId : roomIds ) {
				this.leave( this.roomClients, roomId, sessionId );
	        }
		}
	}

	@Override
	public Set<UUID> getRoomClients(String room) {
		return Collections.unmodifiableSet( this.roomClients.get( room ) );
	}

	@Override
	public Set<String> getClientRooms(UUID sessionId) {
		return Collections.unmodifiableSet( this.clientRooms.get( sessionId ) );
	}

	@Override
	public Set<String> getAllRooms() {
		return Collections.unmodifiableSet( this.roomClients.keySet() );
	}
	
}
