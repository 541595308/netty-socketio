package com.corundumstudio.socketio.store.room;

import org.redisson.api.RedissonClient;

import com.corundumstudio.socketio.scheduler.CancelableScheduler;

public class RedissonRoomClientsCenterStoreFactory extends RoomClientsCenterStoreFactory<RedissonRoomClientsCenterStore> {

	private RedissonClient redisson;
	private CancelableScheduler scheduler;
	
	public RedissonRoomClientsCenterStoreFactory( RedissonClient redisson ) {
		this.redisson = redisson;
	}
	
	@Override
	public void init( CancelableScheduler scheduler ) {
		this.scheduler = scheduler;
		if( this.scheduler == null ) {
			return;
		}
		for( RedissonRoomClientsCenterStore s : super.stores.values() ) {
			s.scheduleSessionExpireCheck( this.scheduler );
		}
	}
	
	@Override
	RedissonRoomClientsCenterStore createRoomClientsCenterStore( String namespace, long pingTimeout ) {
		RedissonRoomClientsCenterStore store = new RedissonRoomClientsCenterStore( namespace, this.redisson, pingTimeout );
		if( this.scheduler != null ) {
			store.scheduleSessionExpireCheck( this.scheduler );
		}
		return store;
	}

}
