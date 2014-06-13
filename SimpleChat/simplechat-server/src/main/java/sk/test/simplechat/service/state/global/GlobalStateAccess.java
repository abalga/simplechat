package sk.test.simplechat.service.state.global;

import java.util.SortedSet;

import sk.test.simplechat.event.SimpleChatEvent;

public interface GlobalStateAccess {
	SortedSet<String> getChatRooms();
	SortedSet<SimpleChatEvent> getChatEvents(String room);
	final String GLOBAL_STATE_ACCESS = "simpleChatGlobalStateAccess";
	void joinRoom(String room, String userName);
	void leaveRoom(String room, String userName);
	void sendMessage(String userName, String room, String message);
}
