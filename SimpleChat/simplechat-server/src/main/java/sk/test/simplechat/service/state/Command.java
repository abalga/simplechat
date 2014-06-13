package sk.test.simplechat.service.state;

public class Command<E> {
	private long correlationId;
	private E commandType;

	public Command() { this.correlationId = 0; }

	public Command(long correlationId, E commandType) {
		this.correlationId = correlationId;
		this.commandType = commandType;
	}

	public Command(Command<E> otherCommand) {
		this.correlationId = otherCommand.getCorrelationId();
		this.commandType = otherCommand.getCommandType();
	}

	public long getCorrelationId() {
		return this.correlationId;
	}

	public E getCommandType() {
		return this.commandType;
	}
}
