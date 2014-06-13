package sk.test.simplechat.event;

public enum SimpleChatEventType {
	JOIN("Join"),
	LEAVE("Leave"),
	MESSAGE("Message")
	;

	private String description;

	SimpleChatEventType(String description) {
		this.description = description;
	}

	public String toString() {
		return this.description;
	}

	public static SimpleChatEventType fromString(String type) {
		if ("Join".equals(type)) {
			return JOIN;
		} else if ("Leave".equals(type)) {
			return LEAVE;
		} else {
			return MESSAGE;
		}
	}
}