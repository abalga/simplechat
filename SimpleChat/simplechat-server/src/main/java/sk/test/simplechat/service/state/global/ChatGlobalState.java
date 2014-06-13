package sk.test.simplechat.service.state.global;

import java.util.Comparator;
import java.util.SortedSet;

import sk.test.simplechat.event.SimpleChatEvent;

public interface ChatGlobalState {
	public Comparator<SimpleChatEvent> getChatEventDateComparator();
	
	/**
	 * Getter for the chat rooms list for any user in the cluster. 
	 * @return the list of chatrooms
	 */
	public SortedSet<String> getChatRooms();
	
	/**
	 * Getter for the list of chat events for a given chat room name.
	 * @param room name of the chatroom
	 * @return list of the chat events
	 */
	public SortedSet<SimpleChatEvent> getChatEvents(String room);
	
	/**
	 * Method to be called when the given chat room is leaved. De-registers the chat room from the list of globally shared chat rooms.
	 * @param room name of the chatroom
	 * @param userName user leaving the room
	 * @return true, if the current user left the room as the last user
	 */
	public boolean leaveRoom(String room, String userName);
	
	/**
	 * Method called when the given chat room is joined. Registers the chat room to the list of globally shared chat rooms.
	 * @param room name of the chat room to join
	 * @param userName user joining the room
	 */
	public void joinRoom(String room, String userName);

	/**
	 * Method for registration of the currently sent message.
	 * @param userName user sending a message
	 * @param room chat room
	 * @param message the message content
	 */
	public void sendMessage(String userName, String room, String message);
}
