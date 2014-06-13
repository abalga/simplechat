package sk.test.simplechat.service.state.global;

import java.util.SortedSet;

import sk.test.simplechat.event.SimpleChatEvent;
import sk.test.simplechat.service.state.AbstractStateAccessor;
import sk.test.simplechat.service.state.ReceiveHandler;
import sk.test.simplechat.service.state.RequestCommand;
import sk.test.simplechat.service.state.ResponseCommand;
import sk.test.simplechat.service.state.StateSerialHandlerBuilder;

public class GlobalStateAccessor extends AbstractStateAccessor<GlobalStateAccessor.CommandType> implements GlobalStateAccess {
	private ChatGlobalState state = ChatGlobalStateImpl.getIntance();
	public GlobalStateAccessor(StateSerialHandlerBuilder shBuilder) {
		super(shBuilder);
		this.setReceiverHandler(new ReceiveHandler<CommandType>() {
			@Override
			public ResponseCommand<CommandType,?> onReceive(RequestCommand<CommandType,?> r) {
				String room, userName, message;
				String[] rd = (String[])r.getRequestData();
				
				switch (r.getCommandType()) {
					case GET_ROOMS:
						return createResponseCommand(r, state.getChatRooms());
					case GET_EVENTS:
						return createResponseCommand(r, state.getChatEvents((String)r.getRequestData()));
					case JOIN_ROOM:
						room = rd[0];
						userName = rd[1];
						state.joinRoom(room, userName);
						break;
					case LEAVE_ROOM:
						room = rd[0];
						userName = rd[1];
						state.leaveRoom(room, userName);
						break;
					case SEND_MESSAGE:
						room = rd[0];
						userName = rd[1];
						message = rd[2];
						state.sendMessage(userName, room, message);
					default:
				}

				return null;
			}
		});
	}

	protected static enum CommandType {
		GET_ROOMS,
		GET_EVENTS,
		JOIN_ROOM,
		LEAVE_ROOM,
		SEND_MESSAGE;
	}

	@Override
	public SortedSet<String> getChatRooms() {
		return this.send(CommandType.GET_ROOMS, null);
	}


	@Override
	public SortedSet<SimpleChatEvent> getChatEvents(String room) {
		return this.send(CommandType.GET_EVENTS, room);
	}

	@Override
	public void joinRoom(String room, String userName) {
		this.postMulti(CommandType.JOIN_ROOM, room, userName);
	}

	@Override
	public void leaveRoom(String room, String userName) {
		this.postMulti(CommandType.LEAVE_ROOM, room, userName);
	}

	@Override
	public void sendMessage(String userName, String room, String message) {
		this.postMulti(CommandType.SEND_MESSAGE, userName, room, message);
	}

}
