package sk.test.simplechat.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("chatservice")
public interface SimpleChatService extends RemoteService {
    final String CHAT_DOMAIN = "chatDomain";
    final String EVENT_CHANNEL = "eventChannel";

    void joinRoom(String userName, String roomName);
    void leaveRoom(String userName, String roomName);
    void sendMessage(String userName, String aMessage);
    void pong(String userName);
}