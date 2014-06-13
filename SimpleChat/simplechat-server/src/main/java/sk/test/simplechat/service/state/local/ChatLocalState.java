package sk.test.simplechat.service.state.local;

public interface ChatLocalState {
	/**
	 * Retrieves the chat room name for the user with a give atmosphere UUID. Returns null if no chatroom for the given UUID is found.
	 * @param uuid Athmosphere resource UUID for the user for which the chat room is to be found
	 * @return the retrieved chatroom name
	 */
	public String getChatRoomFor(String uuid);
	/**
	 * Get UUID of one of the users found locally for the given chatroom name. Used by remote calls through jGroups.
	 * Returns null in case there is no user in the given chat room locally. 
	 * @param room the room name
	 * @return uuid of one of the users
	 */
	public String getFirstUUIDsForRoom(String room);
	
	/**
	 * Method for retrieval of the user name for a given UUID.
	 * @return user name mapped to the given UUID
	 */
	public String getUserForUUID(String uuid);

	//AthmosphereResource AthmosphereResourceFactory.getDefault().find(uuid);
	/**
	 * Joins the chat room for the user. 
	 * @param uuid a uuid
	 * @param userName user name for which the uuid applies
	 * @param chatRoom the chat room to join
	 */
	public void joinRoom(String uuid, String userName, String chatRoom);
	
	/**
	 * Called when the user leaves the chat room.
	 * @param uuid id of the user leaving his/her chat room
	 */
	public void leaveRoom(String uuid);
}
