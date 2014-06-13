package sk.test.simplechat.service.state;

import sk.test.simplechat.service.state.RequestCommand;
import sk.test.simplechat.service.state.ResponseCommand;

public interface ReceiveHandler<H> {
	ResponseCommand<H,?> onReceive(RequestCommand<H,?> request);
}
