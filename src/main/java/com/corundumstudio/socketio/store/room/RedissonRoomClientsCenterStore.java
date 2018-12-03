package com.corundumstudio.socketio.store.room;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSetMultimap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedissonRoomClientsCenterStore implements RoomClientsCenterStore {
	
	private Logger logger = LoggerFactory.getLogger( RedissonRoomClientsCenterStore.class );
	
	private RScoredSortedSet<String> sessionIds;
	private RSetMultimap<String, String> clientRooms;
	private RSetMultimap<String, String> roomClients;
	private long pingTimeout;
	
	RedissonRoomClientsCenterStore( String namespace, RedissonClient redisson, ScheduledExecutorService scheduled, long pingTimeout ) {
		this.sessionIds = redisson.getScoredSortedSet( String.format( "socketio:%s:client-id", namespace ) );
		this.clientRooms = redisson.getSetMultimap( String.format( "socketio:%s:client-rooms", namespace ) );
		this.roomClients = redisson.getSetMultimap( String.format( "socketio:%s:room-clients", namespace ) );
		this.pingTimeout = pingTimeout;
		
		scheduled.schedule( () -> {
			try {
				this.dealExpireSession();
			} catch ( Exception e ) {
				logger.error( "定时处理过期的sessionId 出现异常", e );
			}
		}, pingTimeout, TimeUnit.MILLISECONDS );
	}
	
	/**
	 * 处理过期session
	 */
	private void dealExpireSession() {
		this.sessionIds.removeRangeByScore( 0, true, System.currentTimeMillis(), true );
	}
	

	@Override
	public void join(UUID sessionId, String room) {
		String sessionIdStr = sessionId.toString();
		this.clientRooms.put( sessionIdStr, room );
		this.roomClients.put( room, sessionIdStr );
	}

	@Override
	public void leave(UUID sessionId, String room) {
		String sessionIdStr = sessionId.toString();
		this.clientRooms.remove( sessionIdStr, room );
		this.roomClients.remove( room, sessionIdStr );
	}

	@Override
	public void onClientPing(UUID sessionId) {
		this.sessionIds.add( System.currentTimeMillis() + this.pingTimeout * 3, sessionId.toString() );
	}

	@Override
	public void removeClient( UUID sessionId ) {
		String sessionIdStr = sessionId.toString();
		this.sessionIds.remove( sessionIdStr );
		Set<String> rooms = this.clientRooms.removeAll( sessionIdStr );
		if( rooms != null && rooms.size() > 0 ) {
			for( String room : rooms ) {
				this.roomClients.remove( room, sessionIdStr );
			}
		}
	}

	@Override
	public Set<UUID> getRoomClients(String room) {
		Set<String> sessionIdSet = this.roomClients.get( room );
		Set<UUID> set = new HashSet<UUID>();
		if( set == null || set.size() == 0 ) {
			return set;
		}
		for( String id : sessionIdSet ) {
			set.add( UUID.fromString( id ) );
		}
		return set;
	}

	@Override
	public Set<String> getClientRooms(UUID sessionId) {
		return Collections.unmodifiableSet( this.clientRooms.get( sessionId.toString() ) );
	}

	@Override
	public Set<String> getAllRooms() {
		return Collections.unmodifiableSet( this.roomClients.keySet() );
	}
	
}
