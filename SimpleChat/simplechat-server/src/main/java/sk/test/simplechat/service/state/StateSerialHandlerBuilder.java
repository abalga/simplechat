package sk.test.simplechat.service.state;

public interface StateSerialHandlerBuilder {
	<E>StateSerialHandler<E> buildStateSerialHandler(ReceiveHandler<E> receiveHandler);
}
