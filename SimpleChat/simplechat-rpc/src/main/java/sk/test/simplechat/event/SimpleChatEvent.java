package sk.test.simplechat.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleChatEvent implements Serializable {
	private static final long serialVersionUID = 7503586797066864913L;

	private SimpleChatEventType eventType;
	private String userName;
	private String message;
	private String room;
	private Date date;
	private static final SimpleDateFormat format = new SimpleDateFormat();

	public SimpleChatEvent() {}

	public SimpleChatEvent(SimpleChatEvent e) {
		this.eventType = e.eventType;
		this.userName = e.userName;
		this.message = e.message;
		this.room = e.room;
		this.date = new Date(date.getTime());
	}

	public SimpleChatEvent(SimpleChatEventType messageType, String userName, String message, String room, Date date) {
		this.eventType = messageType;
		this.userName = userName;
		this.message = message;
		this.room = room;
		this.date = date;
	}

	public Date getDate() {
		return this.date;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getMessage() {
		return this.message;
	}

	public SimpleChatEventType getEventType() {
		return this.eventType;
	}

	public boolean isGlobalEventType() {
		return SimpleChatEventType.JOIN.equals(this.eventType) || SimpleChatEventType.LEAVE.equals(this.eventType); 
	}

	public String getRoom() {
		return this.room;
	}

	public String toString() {
		switch (this.eventType) {
		  case JOIN:
			  return format.format(date) + ": user " + this.getUserName() + " is joining " + this.getRoom();
		  case LEAVE:
			  return format.format(date) + ": user " + this.getUserName() + " is leaving: " + this.getRoom();
		  case MESSAGE:
		  default:
			  return format.format(date) + ": user " + this.getUserName() + " is sending message '" + this.getMessage() + "'";
		}
	}
}