package sk.test.simplechat.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SimpleChatServiceAsync {
    final String CHAT_DOMAIN = "chatDomain";

    void joinRoom(String userName, String roomName, AsyncCallback<Void> async);

    void leaveRoom(String userName, String roomName, AsyncCallback<Void> async);

    void sendMessage(String userName, String aMessage, AsyncCallback<Void> async);
}