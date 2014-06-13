package sk.test.simplechat.service.state;

public class ResponseCommand<E,F> extends Command<E> {
	private F responseData;

	public ResponseCommand() { super(); }

	public ResponseCommand(RequestCommand<E,?> requestCommand, F responseData) {
		super(requestCommand);
		this.responseData = responseData;
	}

	public F getResponseData() {
		return this.responseData;
	}
}
