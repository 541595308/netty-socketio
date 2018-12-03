package com.corundumstudio.socketio.store.room;

import java.util.concurrent.ScheduledExecutorService;

import org.redisson.api.RedissonClient;

public class RedissonRoomClientsCenterStoreFactory implements RoomClientsCenterStoreFactory {

	private RedissonClient redisson;
	private ScheduledExecutorService scheduled;
	
	public RedissonRoomClientsCenterStoreFactory( RedissonClient redisson, ScheduledExecutorService scheduled ) {
		this.redisson = redisson;
		this.scheduled = scheduled;
	}
	
	@Override
	public RoomClientsCenterStore getRoomClientsCenterStore( String namespace, long pingTimeout ) {
		return new RedissonRoomClientsCenterStore( namespace, this.redisson, this.scheduled, pingTimeout );
	}
	
}
