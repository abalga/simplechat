package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

public class SimpleChat extends ReceiverAdapter {
	private JChannel channel;
	//private String user_name = System.getProperty("user.name", "n/a");
	private final List<String> state = new LinkedList<String>();
	private String s = "0";
	private String configFile = "udp.xml";

	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	public void viewAccepted(View new_view) {
		System.out.println(format.format(new Date()) + " ** view: " + new_view);
		System.out.flush();
	}

	public void receive(Message msg) {
//		System.out.println(format.format(new Date()) + " at receive: " + msg.getObject().toString() + " with s:" + s);
//		System.out.flush();

		if (!msg.getObject().toString().endsWith(":1") && !msg.getObject().toString().endsWith(":2")) {
			if ("1".equals(s)) {
				Message msg1 = new Message(null, null, msg.getObject().toString() + ":1");
				try {
					channel.send(msg1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if ("2".equals(s)) {
				Message msg2 = new Message(null, null, msg.getObject().toString() + ":2");
				try {
					channel.send(msg2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
		} else {
			if ("0".equals(s)) {
				String line = msg.getSrc() + ": " + msg.getObject();
				System.out.println(format.format(new Date()) + " Received message: " + line);
				System.out.flush();
			}
		}

//		synchronized (state) {
//			state.add(line);
//		}
	}

	public void getState(OutputStream output) throws Exception {
		System.out.println(format.format(new Date()) + " Called getState.");
		System.out.flush();
	    synchronized(state) {
	    	System.out.println(format.format(new Date()) + " GetState in monitor block.");
			System.out.flush();
	        Util.objectToStream(state, new DataOutputStream(output));
	    }
	}


	public void setState(InputStream input) throws Exception {
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>)Util.objectFromStream(new DataInputStream(input));

	    synchronized(state) {
	        state.clear();
	        state.addAll(list);
	    }

		System.out.println(format.format(new Date()) + " Received state (" + list.size()
				+ " messages in chat history):");
		System.out.flush();

	    for(String str: list) {
	        System.out.println(str);
	    }
	}

	private void start() throws Exception {
		channel = new JChannel(configFile);
		channel.setReceiver(this);
		channel.connect("ChatCluster");
		channel.getState(/*new IpAddress("localhost", 7800)*/null, 10000);
		eventLoop();
		channel.close();
	}

	private void eventLoop() {
		try {
			int n = 0;
			while (true) {
			    n++;
				if ("0".equals(s)) {
					Thread.sleep(10);
					Message msg0 = new Message(null, null, "0" + String.valueOf(n));
					channel.send(msg0);
					System.out.println(format.format(new Date()) + " Sent message " + msg0.getObject());
					System.out.flush();
				} else {
					while(true) {
						Thread.sleep(1000);
					}
				}
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SimpleChat(String s, String configFile) {
		this.s = s;
		this.configFile = configFile;
	}

	public static void main(String[] args) throws Exception {
		new SimpleChat(args[0], args[1]).start();
	}
}