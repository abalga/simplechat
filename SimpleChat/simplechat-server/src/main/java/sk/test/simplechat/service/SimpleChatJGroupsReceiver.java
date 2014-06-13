package sk.test.simplechat.service;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.ServletContext;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.stack.IpAddress;
import org.jgroups.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.test.simplechat.event.SimpleChatEvent;
import sk.test.simplechat.event.SimpleChatEventType;
import sk.test.simplechat.service.state.global.GlobalStateAccess;
import sk.test.simplechat.service.state.global.GlobalStateAccessor;
import sk.test.simplechat.service.state.local.LocalStateAccess;
import sk.test.simplechat.service.state.local.LocalStateAccessor;

public class SimpleChatJGroupsReceiver  extends ReceiverAdapter implements Closeable, SimpleChatReceiver {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleChatJGroupsReceiver.class);
    private static final int TIMEOUT = 5000;
    private static final String CHAT_CLUSTER = "ChatCluster";

    // TODO track the chatroom names and actual number of users in them - if number of users in a chatroom drops to 0
    // broadcast out a message on a dismissed chat room

	private JChannel channel;
	private ServletContext servletContext;

	public SimpleChatJGroupsReceiver(String jGroupsConfig, ServletContext context) {
		try {
			this.servletContext = context;
/*			String[] addressParts = joinAddress.split(":");
			if (addressParts.length != 2) {
				throw new IllegalArgumentException("Invalid address specified");
			}
			Address address = new IpAddress(addressParts[0], Integer.parseInt(addressParts[1]));
*/
			channel = new JChannel(jGroupsConfig);

			channel.setReceiver(this);
			channel.connect(CHAT_CLUSTER);
			//channel.getState(null, 10000);
		} catch (NumberFormatException e) {
			LOG.error("Invalid number format in {}", jGroupsConfig);
		} catch (UnknownHostException e) {
			LOG.error("Unknown host {}", jGroupsConfig);
		} catch (IllegalArgumentException e) {
			LOG.error("Bad host format {}", jGroupsConfig);
		} catch (Exception e) {
			LOG.error("Cannot conect to {}", jGroupsConfig);
		}
	}

	public void viewAccepted(View newView) {
		LOG.info("View accepted for {}", newView);
	}

	public void receive(Message message) {
		String line = message.getSrc() + ": " + message.getObject().toString();
		LOG.info("Received jGroups message: {} ", line); 

		if (message.getObject() instanceof SimpleChatEvent) {
			SimpleChatEvent cm = (SimpleChatEvent)message.getObject();
			synchronized (this) {
				receive(cm);
			}
		}
	}

	public void receive(SimpleChatEvent event) {
/*		SimpleChatServiceAccessor access = (SimpleChatServiceAccessor)remote;

		if (event.getEventType().equals(SimpleChatEventType.JOIN)) {
			state.joinRoom(event.getUserName(), event.getRoom());
		} else if (event.getEventType().equals(SimpleChatEventType.LEAVE)) {
			state.leaveRoom(event.getUserName(), event.getRoom());
		} else {
			String room = state.getRoomForUser(event.getUserName());
			event.setRoom(room);
		}

		remote.addEvent(access.getDomain(), event);
		*/
	}


	public void sendMessage(SimpleChatEventType messageType, String userName, String messageBody, String room) {
		try {
//			Message message = new Message(null, null, new SimpleChatEvent(messageType, userName, messageBody, room));
//			channel.send(message);
		} catch(Exception e) {
			LOG.error("Cannot send message [" + messageType.toString() + "]" + messageBody);
		}
	}

	@Override
	public void close() throws IOException {
		// TODO send out leave messages on all the local users to other jGroups receivers - not at servlet shutdown!
		//this.getServletConfig().getServletContext().setAttribute("sharedId", shared);
		//this.getServletConfig().getServletContext().getAttribute("sharedId");
		channel.close();
	}

	public GlobalStateAccess getGlobalStateAccess() {
		return (GlobalStateAccess)servletContext.getAttribute("simpleChatGlobalStateAccessor");
	}

	public LocalStateAccess getLocalStateAccess() {
		return (LocalStateAccess)servletContext.getAttribute("simpleChatLocalStateAccessor");
	}

}
