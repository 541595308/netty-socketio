/**
 * Copyright 2012 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.corundumstudio.socketio;

import java.util.Collection;

import com.corundumstudio.socketio.protocol.Packet;

/**
 * broadcast interface
 *
 */
public interface BroadcastOperations extends ClientOperations {

	public Collection<SocketIOClient> getClients();
	
	public <T> void send(Packet packet, BroadcastAckCallback<T> ackCallback);

	public void sendEvent(String name, SocketIOClient excludedClient, Object... data);
	
	public <T> void sendEvent(String name, Object data, BroadcastAckCallback<T> ackCallback);
	
	public <T> void sendEvent(String name, Object data, SocketIOClient excludedClient, BroadcastAckCallback<T> ackCallback);
	
}
