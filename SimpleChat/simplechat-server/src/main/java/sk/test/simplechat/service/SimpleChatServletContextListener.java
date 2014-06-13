package sk.test.simplechat.service;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import sk.test.simplechat.service.state.StateSerialHandlerBuilder;
import sk.test.simplechat.service.state.StateSerialHandlerBuilderImpl;
import sk.test.simplechat.service.state.global.GlobalStateAccess;
import sk.test.simplechat.service.state.global.GlobalStateAccessor;
import sk.test.simplechat.service.state.local.LocalStateAccess;
import sk.test.simplechat.service.state.local.LocalStateAccessor;

public class SimpleChatServletContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		final ServletContext context = event.getServletContext();
		
/*		if (context.getAttribute(RECEIVER_ID) == null) {
			final String coordinatorAddress = System.getProperty("stateProviderAddress");
			final SimpleChatJGroupsReceiver receiver = new SimpleChatJGroupsReceiver(coordinatorAddress, this);
			context.setAttribute(RECEIVER_ID, receiver);
		}
*/
		StateSerialHandlerBuilder shBuilder = StateSerialHandlerBuilderImpl.getInstance();

		GlobalStateAccess globalStateAccess = new GlobalStateAccessor(shBuilder);
		context.setAttribute(GlobalStateAccess.GLOBAL_STATE_ACCESS, globalStateAccess);

		LocalStateAccess localStateAccess = new LocalStateAccessor(shBuilder);
		context.setAttribute(LocalStateAccess.LOCAL_STATE_ACCESS, localStateAccess);

		final String config = "jGroupsConfig";
		String jGroupsConfig = context.getInitParameter(config);
		context.setAttribute(SimpleChatReceiver.CHAT_RECEIVER, new SimpleChatJGroupsReceiver(jGroupsConfig, context));
	}
}
