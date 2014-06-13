/*
package sk.test.simplechat.service;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.test.simplechat.event.SimpleChatEventType;
import sk.test.simplechat.service.eventfilter.SimpleChatEventFilter;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

public class SimpleChatServiceImpl extends RemoteEventServiceServlet implements SimpleChatService, SimpleChatServiceAccessor {
	private static final long serialVersionUID = 5539797794038891690L;

	private static final Logger LOG = LoggerFactory.getLogger(SimpleChatServiceImpl.class);
    private static final Domain chatDomain = DomainFactory.getDomain(SimpleChatService.CHAT_DOMAIN);
    private static final String RECEIVER_ID = "simpleChatJGroupsReceiver";
    private SimpleChatJGroupsReceiver receiver;

	@Override
	public void joinRoom(String userName, String roomName) {
		LOG.debug("User {} joined room {}", userName, roomName);
		setEventFilter(chatDomain, new SimpleChatEventFilter(roomName));
		getReceiver().sendMessage(SimpleChatEventType.JOIN, userName, null, roomName);
	}

	@Override
	public void leaveRoom(String userName, String roomName) {
		LOG.debug("User {} left room {}", userName, roomName);
		//setEventFilter(chatDomain, new ReluctantEventFilter());
		getReceiver().sendMessage(SimpleChatEventType.LEAVE, userName, null, roomName);
	}

	@Override
	public void sendMessage(String userName, String message) {
		LOG.debug("User {} sent: {}", userName, message);
		getReceiver().sendMessage(SimpleChatEventType.MESSAGE, userName, message, null);
	}

	@Override
	public void init() throws ServletException {
		final ServletContext context = this.getServletConfig().getServletContext();
		synchronized (context) {
			if (context.getAttribute(RECEIVER_ID) == null) {
				final String coordinatorAddress = System.getProperty("stateProviderAddress");
				final SimpleChatJGroupsReceiver receiver = new SimpleChatJGroupsReceiver(coordinatorAddress, this);
				context.setAttribute(RECEIVER_ID, receiver);
			}
		}
	}

	@Override
	public void destroy() {
		try {
			receiver.close();
		} catch(IOException e) {}
	}
	
	public Domain getDomain() {
		return chatDomain;
	}

	@Override
	public void pong(String userName) {
		receiver.keepUserAlive(userName);
	}

	public SimpleChatJGroupsReceiver getReceiver() {
		return (SimpleChatJGroupsReceiver)this.getServletConfig().getServletContext().getAttribute(RECEIVER_ID);
	}
}
*/