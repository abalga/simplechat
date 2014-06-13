package sk.test.simplechat.service.state.local;

import sk.test.simplechat.service.state.AbstractStateAccessor;
import sk.test.simplechat.service.state.ReceiveHandler;
import sk.test.simplechat.service.state.RequestCommand;
import sk.test.simplechat.service.state.ResponseCommand;
import sk.test.simplechat.service.state.StateSerialHandlerBuilder;


public class LocalStateAccessor extends AbstractStateAccessor<LocalStateAccessor.CommandType> implements LocalStateAccess {
	private ChatLocalState state = ChatLocalStateImpl.getIntance();
	public LocalStateAccessor(StateSerialHandlerBuilder shBuilder) {
		super(shBuilder);
		this.setReceiverHandler(new ReceiveHandler<CommandType>() {
			@Override
			public ResponseCommand<CommandType,?> onReceive(RequestCommand<CommandType,?> r) {
				String[] rd;
				String uuid, userName, chatRoom;

				switch (r.getCommandType()) {
					case GET_ROOM_FOR:
						return createResponseCommand(r, state.getChatRoomFor((String)r.getRequestData()));
					case UUID_FOR_ROOM:
						return createResponseCommand(r, state.getFirstUUIDsForRoom((String)r.getRequestData()));
					case GET_USER_FOR_UUID:
						return createResponseCommand(r, state.getUserForUUID((String)r.getRequestData()));
					case JOIN:
						rd = (String[])r.getRequestData();
						uuid = rd[0];
						userName = rd[1];
						chatRoom = rd[2];
						state.joinRoom(uuid, userName, chatRoom);
						return null;
					case LEAVE:
						state.leaveRoom((String)r.getRequestData());
					default:
						return null;
				}
			}
		});
	}

	protected static enum CommandType {
		GET_ROOM_FOR,
		UUID_FOR_ROOM,
		GET_USER_FOR_UUID,
		JOIN,
		LEAVE;
	}

	@Override
	public String getRoomFor(String uuid) {
		return this.send(CommandType.GET_ROOM_FOR, uuid);
	}

	@Override
	public String getUUIDForRoom(String room) {
		return this.send(CommandType.UUID_FOR_ROOM, room);
	}

	@Override
	public String getUserForUUID(String uuid) {
		return this.send(CommandType.GET_USER_FOR_UUID, uuid);
	}

	@Override
	public void joinRoom(String uuid, String userName, String chatRoom) {
		this.postMulti(CommandType.JOIN, uuid, userName, chatRoom);
	}

	@Override
	public void leaveRoom(String uuid) {
		this.postMulti(CommandType.LEAVE, uuid);
	}
}
