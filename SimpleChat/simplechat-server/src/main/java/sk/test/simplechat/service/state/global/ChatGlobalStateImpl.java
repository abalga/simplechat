package sk.test.simplechat.service.state.global;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import sk.test.simplechat.event.SimpleChatEvent;
import sk.test.simplechat.event.SimpleChatEventType;

public class ChatGlobalStateImpl implements ChatGlobalState {
	private static final int MAX_EVENTS_PER_ROOM = 10;

	private static class ChatSharedStateHolder {
		public static ChatGlobalStateImpl INSTANCE = new ChatGlobalStateImpl(); 
	}

	private final Comparator<SimpleChatEvent> chatEventDateComparator = new Comparator<SimpleChatEvent>() {
		@Override
		public int compare(SimpleChatEvent o1, SimpleChatEvent o2) {
			return o1.getDate().compareTo(o2.getDate());
		}
	};

	private Map<String, Integer> chatRooms = new TreeMap<String, Integer>();
	private Map<String, SortedSet<SimpleChatEvent>> chatEventsAtRooms = new TreeMap<String, SortedSet<SimpleChatEvent>>();
	private SortedSet<SimpleChatEvent> globalChatEvents = new TreeSet<SimpleChatEvent>();

    private SortedSet<SimpleChatEvent> getGlobalChatEvents() {
    	return this.globalChatEvents;
    }

	private ChatGlobalStateImpl() {}

	public static ChatGlobalState getIntance() {
		return ChatSharedStateHolder.INSTANCE;
	}
	
    public SortedSet<String> getChatRooms() {
    	return new TreeSet<String>(this.chatRooms.keySet());
    }

    public SortedSet<SimpleChatEvent> getChatEvents(String room) {
    	SortedSet<SimpleChatEvent> globalEvents = this.getGlobalChatEvents();
    	SortedSet<SimpleChatEvent> eventsAtRooms = this.getChatEvents(room);
    	SortedSet<SimpleChatEvent> mergedEvents = new TreeSet<SimpleChatEvent>(chatEventDateComparator);

    	if (eventsAtRooms.size() == 0) {
    		return mergedEvents;
    	}

    	for (SimpleChatEvent event: globalEvents) {
    		mergedEvents.add(new SimpleChatEvent(event));
    	}

    	for (SimpleChatEvent event: eventsAtRooms) {
    		mergedEvents.add(new SimpleChatEvent(event));
    	}

    	return mergedEvents;
    }

    private void registerChatRoomEvent(SimpleChatEvent event) {
    	if (this.chatEventsAtRooms.get(event.getRoom()) == null) {
    		this.chatEventsAtRooms.put(event.getRoom(), new TreeSet<SimpleChatEvent>(chatEventDateComparator));
    	}

    	SortedSet<SimpleChatEvent> events = this.chatEventsAtRooms.get(event.getRoom());
    	if (events.size() == MAX_EVENTS_PER_ROOM) {
    		events.remove(events.first());
    	}

    	events.add(event);
    }

    private void registerChatGlobalEvent(SimpleChatEvent event) {
    	if (this.globalChatEvents.size() == MAX_EVENTS_PER_ROOM) {
    		this.globalChatEvents.remove(this.globalChatEvents.first());
    	}

    	this.globalChatEvents.add(event);
    }

    public boolean leaveRoom(String room, String userName) {
		Integer visitorCount = this.chatRooms.get(room);
		if (visitorCount != null) {
			if (visitorCount.intValue() > 1) {
			  this.chatRooms.put(room, Integer.valueOf(visitorCount.intValue() - 1));
			} else {
			  this.chatRooms.remove(room);
			  return true;
			}
		}

		this.registerChatGlobalEvent(new SimpleChatEvent(SimpleChatEventType.JOIN, userName, null, room, new Date()));
    	return false;
    }

    public void joinRoom(String room, String userName) {
		Integer visitorCount = this.chatRooms.get(room);
		if (visitorCount != null) {
			this.chatRooms.put(room, Integer.valueOf(visitorCount.intValue() + 1));
		} else {
			this.chatRooms.put(room, Integer.valueOf(1));
		}

		this.registerChatGlobalEvent(new SimpleChatEvent(SimpleChatEventType.JOIN, userName, null, room, new Date()));
    }

    public Comparator<SimpleChatEvent> getChatEventDateComparator() {
    	return chatEventDateComparator;
    }

	@Override
	public void sendMessage(String userName, String room, String message) {
		this.registerChatRoomEvent(new SimpleChatEvent(SimpleChatEventType.MESSAGE, userName, message, room, new Date()));
	}
}
