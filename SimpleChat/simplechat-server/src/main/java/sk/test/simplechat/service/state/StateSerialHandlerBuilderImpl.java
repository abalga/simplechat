package sk.test.simplechat.service.state;

public class StateSerialHandlerBuilderImpl implements StateSerialHandlerBuilder {
	private static StateSerialHandlerBuilder INSTANCE = new StateSerialHandlerBuilderImpl();

	public static StateSerialHandlerBuilder getInstance() {
		return INSTANCE;
	}

	public <E>StateSerialHandler<E> buildStateSerialHandler(ReceiveHandler<E> receiveHandler) {
	   return new StateSerialHandlerImpl<E>(receiveHandler);
	}
}
