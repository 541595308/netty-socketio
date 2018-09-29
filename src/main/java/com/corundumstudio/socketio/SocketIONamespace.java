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
import java.util.UUID;

import com.corundumstudio.socketio.listener.ClientListeners;
import com.corundumstudio.socketio.protocol.Packet;

/**
 * Fully thread-safe.
 *
 */
public interface SocketIONamespace extends ClientListeners {

    String getName();

    BroadcastOperations getBroadcastOperations();

    BroadcastOperations getRoomOperations(String room);

    /**
     * Get all clients connected to namespace
     *
     * @return collection of clients
     */
    Collection<SocketIOClient> getAllClients();

    /**
     * Get client by uuid connected to namespace
     *
     * @param uuid - id of client
     * @return client
     */
    SocketIOClient getClient(UUID uuid);
    
    /**
     * 加入房间并广播到其他节点
     * @param room
     * @param sessionId
     */
    public void joinRoom(String room, UUID sessionId);
    /**
     * 加入房间，不广播.
     * 用于处理其他节点广播来的加入请求
     * @param room
     * @param sessionId
     */
    public void join(String room, UUID sessionId);
    
    /**
     * 离开房间并广播到其他节点
     * @param room
     * @param sessionId
     */
    public void leaveRoom(String room, UUID sessionId);
    /**
     * 离开房间，不广播
     * 用于处理其他节点广播来的离开请求
     * @param room
     * @param sessionId
     */
    public void leave(String room, UUID sessionId);
    
    public Iterable<SocketIOClient> getRoomClients(String room);
    /**
     * 处理其他节点广播来的房间数据发送
     * @param room
     * @param packet
     */
    public void dispatch(String room, Packet packet);
    
}
