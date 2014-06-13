package sk.test.simplechat.service.state;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StateSerialHandlerImpl<E> implements StateSerialHandler<E> {
	private BlockingQueue<RequestCommand<E,?>> inbound = new LinkedBlockingQueue<RequestCommand<E,?>>();
	private BlockingQueue<ResponseCommand<E,?>> outbound = new LinkedBlockingQueue<ResponseCommand<E,?>>();
	private ReceiveHandler<E> receiveHandler;
	private volatile long correlationId = 0L;
    private static final int RETRY_PERIOD = 100;

	public StateSerialHandlerImpl(ReceiveHandler<E> handler) {
		this.receiveHandler = handler;

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						RequestCommand<E,?> command = inbound.take();
						if (receiveHandler != null) {
							ResponseCommand<E,?> result = receiveHandler.onReceive(command);
							if (result == null) {
								// for the case the thread is ready, but the receiverHandler is not attached
								inbound.add(command);
								Thread.sleep(RETRY_PERIOD);
							} else {
								outbound.add(result);
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(r).start();
	}

	@SuppressWarnings("unchecked")
	public <F,G>ResponseCommand<E,G> send(RequestCommand<E,F> request) {
		this.inbound.add(request);

		try {
			ResponseCommand<E,?> responseCandidate = (ResponseCommand<E,?>)this.outbound.take();
			Queue<ResponseCommand<E,?>> temporary = new LinkedList<ResponseCommand<E,?>>(); 

			while (responseCandidate.getCorrelationId() != request.getCorrelationId()) {
				temporary.add(responseCandidate);
				responseCandidate = outbound.take();
			}

			this.outbound.addAll(temporary);
			return (ResponseCommand<E,G>)responseCandidate;
		} catch (InterruptedException e) {
			return new ResponseCommand<E,G>() {};
		}
	}

	public <F>void post(RequestCommand<E,F> command) {
		inbound.add(command);
	}

	public synchronized long newCorrelationId() {
		return ++correlationId;
	}
}
