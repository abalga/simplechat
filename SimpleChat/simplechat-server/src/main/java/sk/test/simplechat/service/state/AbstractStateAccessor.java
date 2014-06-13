package sk.test.simplechat.service.state;

import sk.test.simplechat.service.state.RequestCommand;
import sk.test.simplechat.service.state.ResponseCommand;

public abstract class AbstractStateAccessor<H> {
	private StateSerialHandler<H> serialHandler;
	private ReceiveHandler<H> stateAccessorHandler;

	public AbstractStateAccessor(StateSerialHandlerBuilder shBuilder) {
		this.serialHandler = shBuilder.buildStateSerialHandler(new ReceiveHandler<H>() {
			@Override
			public ResponseCommand<H,?> onReceive(RequestCommand<H,?> r) {
				if (stateAccessorHandler != null) {
					return stateAccessorHandler.onReceive(r);
				} else {
					return null;
				}
			}
		});
	}

	public void setReceiverHandler(ReceiveHandler<H> stateAccessorHandler) {
		this.stateAccessorHandler = stateAccessorHandler;
	}

	protected <E> ResponseCommand<H,E> createResponseCommand(RequestCommand<H,?> request, E responseData) {
		return new ResponseCommand<H,E>((RequestCommand<H, ?>) request, responseData);		
	}

	protected <F> RequestCommand<H,F> createRequestCommand(H command, F requestData) {
		return new RequestCommand<H, F>(id(), command, requestData);
	}

	protected <G> G sendMulti(H t, Object... r) {
		return this.send(t, r);
	}

	protected void postMulti(H t, Object... r) {
		this.post(t, r);
	}

	protected <F,G> G send(H t, F r) {
		RequestCommand<H, F> c = this.createRequestCommand(t, r);
		return this.serialHandler.<F,G>send(c).getResponseData();
	}

	protected <G> void post(H t, G r) {
		RequestCommand<H, G> c = this.createRequestCommand(t, r);
		this.serialHandler.<G>post(c);
	}

	protected long id() {
		return this.serialHandler.newCorrelationId();
	}

}
