package sk.test.simplechat.service.state.local;

public interface LocalStateAccess {
	String getRoomFor(String uuid);
	String getUUIDForRoom(String room);
	String getUserForUUID(String uuid);
	void joinRoom(String uuid, String userName, String chatRoom);
	void leaveRoom(String uuid);
	final String LOCAL_STATE_ACCESS = "simpleChatLocalStateAccess";
}
