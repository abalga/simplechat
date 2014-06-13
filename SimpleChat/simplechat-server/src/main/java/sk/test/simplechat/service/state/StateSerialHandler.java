package sk.test.simplechat.service.state;

import sk.test.simplechat.service.state.RequestCommand;
import sk.test.simplechat.service.state.ResponseCommand;

public interface StateSerialHandler<E> {
	<F,G>ResponseCommand<E,G> send(RequestCommand<E,F> request);
	<F>void post(RequestCommand<E,F> command);
	public long newCorrelationId();
}
