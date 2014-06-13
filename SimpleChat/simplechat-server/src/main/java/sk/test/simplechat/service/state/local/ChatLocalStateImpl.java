package sk.test.simplechat.service.state.local;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ChatLocalStateImpl implements ChatLocalState {
	private static class ChatLocalStateHolder {
		public static ChatLocalStateImpl INSTANCE = new ChatLocalStateImpl(); 
	}

	private Map<String, String> chatRoomForUUID = new TreeMap<String, String>();
	private Map<String, Set<String>> uuidsForChatRoom = new TreeMap<String, Set<String>>(); 
	private Map<String, String> userForUUID = new TreeMap<String, String>();
	
	private ChatLocalStateImpl() {}

	@Override
	public String getChatRoomFor(String uuid) {
		return this.chatRoomForUUID.get(uuid);
	}

	@Override
	public String getFirstUUIDsForRoom(String room) {
		Set<String> uuidSet = this.uuidsForChatRoom.get(room);
		return (uuidSet.size() > 0) ? this.uuidsForChatRoom.get(room).toArray(new String[0])[0] : null;
	}

	@Override
	public String getUserForUUID(String uuid) {
		return this.userForUUID.get(uuid);
	}

	@Override
	public void joinRoom(String uuid, String userName, String chatRoom) {
		this.chatRoomForUUID.put(uuid, chatRoom);
		Set<String> existingUUIDs = this.uuidsForChatRoom.get(chatRoom);
		if (existingUUIDs == null) {
			existingUUIDs = new TreeSet<String>();
		}
		existingUUIDs.add(uuid);
		this.userForUUID.put(uuid, userName);
	}

	@Override
	public void leaveRoom(String uuid) {
		String chatRoom = this.getChatRoomFor(uuid);
		this.uuidsForChatRoom.remove(chatRoom);
		this.userForUUID.remove(uuid);
		this.chatRoomForUUID.remove(uuid);
	}

	public static ChatLocalState getIntance() {
		return ChatLocalStateHolder.INSTANCE;
	}
}
