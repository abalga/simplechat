package sk.test.simplechat.service.state;

public class RequestCommand<E,F> extends Command<E> {
	private F requestData;

	public RequestCommand(long correlationId, E type, F requestData) {
		super(correlationId, type);
		this.requestData = requestData;
	}

	public F getRequestData() {
		return this.requestData;
	}
}
