package sk.test.simplechat.service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.gwt.server.AtmosphereGwtHandler;
import org.atmosphere.gwt.server.GwtAtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;

import sk.test.simplechat.service.state.global.GlobalStateAccess;
import sk.test.simplechat.service.state.local.LocalStateAccess;


public class SimpleChatAtmosphereHandler extends AtmosphereGwtHandler  {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        Logger.getLogger("").setLevel(Level.INFO);
        Logger.getLogger("org.atmosphere.gwt").setLevel(Level.ALL);
        Logger.getLogger("org.atmosphere.samples").setLevel(Level.ALL);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.ALL);
        logger.trace("Updated logging levels");
    }

    @Override
    public int doComet(GwtAtmosphereResource resource) throws ServletException, IOException {
    	// TODO store in the pathinfo the room and user id as well (i.e. user x joins room y)
    	// store the user id and uuid pair in the application context
    	// TODO in addition to the room broadcasters create a global broadcaster for the room join/leave events
    	// this broadcaster will exist since the init method is called till the application is shutdown

    	// store in the receiver also the last n messages of each chatroom
    	// - these will be sent at connecting to chatroom to the user
        String room = resource.getRequest().getPathInfo();
        Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(room, true);
        resource.getAtmosphereResource().setBroadcaster(broadcaster);
        return 300000;
    }

    @Override
    public void cometTerminated(GwtAtmosphereResource cometResponse, boolean serverInitiated) {
        super.cometTerminated(cometResponse, serverInitiated);
//cometResponse.getAtmosphereResource().uuid()
//        res = this.lookupResource(connectionUUID)
//        res.post(message);
        logger.info("Comet disconnected");
    }

    @Override
    public void doPost(HttpServletRequest postRequest, HttpServletResponse postResponse,
            List<?> messages, GwtAtmosphereResource cometResource) {
        //broadcast(messages, cometResource);
    }

	public GlobalStateAccess getGlobalStateAccess() {
		return (GlobalStateAccess)this.getServletContext().getAttribute(GlobalStateAccess.GLOBAL_STATE_ACCESS);
	}

	public LocalStateAccess getLocalStateAccess() {
		return (LocalStateAccess)this.getServletContext().getAttribute(LocalStateAccess.LOCAL_STATE_ACCESS);
	}
}
