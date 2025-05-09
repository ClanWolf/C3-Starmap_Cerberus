package io.nadron.client.event.impl;

import io.nadron.client.app.Session;
import io.nadron.client.communication.DeliveryGuaranty.DeliveryGuarantyOptions;
import io.nadron.client.communication.MessageBuffer;
import io.nadron.client.communication.MessageSender;
import io.nadron.client.event.Event;
import io.nadron.client.event.Events;
import io.nadron.client.event.NetworkEvent;
import io.nadron.client.event.SessionEventHandler;
import io.nadron.client.util.Config;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;


/**
 * Provides default implementation for most of the events. Subclasses can
 * override the event handler method for any particular event,
 * {@link AbstractSessionEventHandler}{@link #onDataIn(Event)} to do app
 * specific logic.
 * 
 * @author Abraham Menacherry
 * 
 */
public abstract class AbstractSessionEventHandler implements SessionEventHandler {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	protected static final int eventType = Events.ANY;

	protected Session session;
	
	protected volatile boolean isReconnecting = false;

	public AbstractSessionEventHandler() {
	}
	
	public AbstractSessionEventHandler(Session session) {
		this.session = session;
	}

	@Override
	public int getEventType() {
		return eventType;
	}

	@Override
	public void onEvent(Event event) {
		doEventHandlerMethodLookup(event);
	}

	public void doEventHandlerMethodLookup(Event event) {
		int eventType = event.getType();
		switch (eventType) {
			case Events.SESSION_MESSAGE -> onDataIn(event);
			case Events.NETWORK_MESSAGE -> onNetworkMessage((NetworkEvent) event);
			case Events.LOG_IN_SUCCESS -> onLoginSuccess(event);
			case Events.LOG_IN_FAILURE -> onLoginFailure(event);
			case Events.START -> onStart(event);
			case Events.STOP -> onStart(event);
			case Events.GAME_ROOM_JOIN_SUCCESS -> onGameRoomJoin(event);
			case Events.CONNECT_FAILED -> onConnectFailed(event);
			case Events.DISCONNECT -> onDisconnect(event);
			case Events.CHANGE_ATTRIBUTE -> onChangeAttribute(event);
			case Events.EXCEPTION -> onException(event);
			case Events.REGISTRATION_FAILURE_USERNAME -> onRegistrationFailedUserName(event);
			case Events.REGISTRATION_FAILURE_USERMAIL -> onRegistrationFailedUserMail(event);

			//		case Events.LOG_OUT:
			//			onLogout(event);
			//			break;
			default -> onCustomEvent(event);
		}
	}

//	public abstract void onLogout(Event event);

	public abstract void onDataIn(Event event);

	public void onNetworkMessage(NetworkEvent networkEvent) {
		Session session = getSession();
		boolean writeable = session.isWriteable();
		MessageSender messageSender = null;
		if (networkEvent.getDeliveryGuaranty().getGuaranty() == DeliveryGuarantyOptions.FAST.getGuaranty()) {
			messageSender = session.getUdpMessageSender();
		} else {
			messageSender = session.getTcpMessageSender();
		}
		if (writeable && null != networkEvent) {
			messageSender.sendMessage(networkEvent);
		}
	}

	public void onLoginSuccess(Event event) {
	}

	public void onGameRoomJoin(Event event) {
		if (null != event.getSource() && (event.getSource() instanceof MessageBuffer)) {
			@SuppressWarnings("unchecked")
			String reconnectKey = ((MessageBuffer<ByteBuf>) event.getSource()).readString();
			if (null != reconnectKey) {
				getSession().setAttribute(Config.RECONNECT_KEY, reconnectKey);
			}
		}
	}

	public void onLoginFailure(Event event) {
	}

	public void onStart(Event event) {
		isReconnecting = false;
		getSession().setWriteable(true);
	}

	public void onStop(Event event) {
		getSession().setWriteable(false);
	}

	public void onConnectFailed(Event event) {

	}

	public void onDisconnect(Event event) {
		//onException(event);
	}

	public void onChangeAttribute(Event event) {

	}

	public void onRegistrationFailedUserName(Event event) {
		logger.debug("errorcode: UserName");
	}

	public void onRegistrationFailedUserMail(Event event) {
		logger.debug("errorcode: UserMail");
	}

	public synchronized void onException(Event event) {
		Session session = getSession();
		String reconnectKey = (String) session.getAttribute(Config.RECONNECT_KEY);
		if (null != reconnectKey) {
			if(isReconnecting) {
				return;
			} else {
				isReconnecting = true;
			}
			session.setWriteable(false);
			if (null != session.getReconnectPolicy()) {
				session.getReconnectPolicy().applyPolicy(session);
			}
			else {
				logger.error("Received exception event in session. Going to close session [001]");
				onClose(event);
			}
		}
		else {
			logger.error("Received exception event in session. Going to close session [002]");
			onClose(event);
		}
	}

	public void onClose(Event event) {
		getSession().close();
	}

	public void onCustomEvent(Event event) {

	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public void setSession(Session session) {
		this.session = session;
	}
	
}
