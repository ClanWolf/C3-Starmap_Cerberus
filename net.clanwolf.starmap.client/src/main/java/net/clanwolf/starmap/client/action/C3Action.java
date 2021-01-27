/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : https://www.clanwolf.net                           |
 * GitHub      : https://github.com/ClanWolf                        |
 * ---------------------------------------------------------------- |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * C3 includes libraries and source code by various authors.        |
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.action;

import net.clanwolf.starmap.logging.C3Logger;

import java.awt.event.ActionEvent;

/**
 * C3 Action
 *
 * @author Meldric
 */
public class C3Action extends AbstractC3Action implements ActionCallBackListener {

	private static final long serialVersionUID = 1L;
	private ACTIONS mAction;
	private ActionCallBackListener mCallbackListener;

	/**
	 * Constructor.
	 *
	 * @param action
	 */
	public C3Action(ACTIONS action) {
		this(action, null);
	}

	/**
	 * C3Action constructor
	 *
	 * @param action  ACTIONS
	 * @param enabled Boolean
	 */
	public C3Action(ACTIONS action, boolean enabled) {
		this(action, null);
		setEnabled(enabled);
	}

	/**
	 * C3Action constructor
	 *
	 * @param action   ACTIONS
	 * @param listener ActionCallBackListener
	 */
	public C3Action(ACTIONS action, ActionCallBackListener listener) {
		super(action);
		mAction = action;
		mCallbackListener = listener;
		setAccelerator(action.getAcceleratorKey());
	}

	/**
	 * Exewcute action
	 */
	public void execute() {
		execute(new ActionObject(this));
	}

	/**
	 * Execute
	 *
	 * @param o object
	 */
	public void execute(Object o) {
		execute(new ActionObject(this, o));
	}

	/**
	 * Execute
	 *
	 * @param o ActionObject
	 */
	public void execute(ActionObject o) {
		if (mAction == null) {
			return;
		}

		if (mCallbackListener != null) {
			mCallbackListener.handleAction(mAction, o);
		}
		for (ActionCallBackListener listener : ActionManager.getActionCallbackListener(mAction)) {
			if (!handleActionForListener(listener, mAction, o)) {
				break;
			}
		}
		for (ActionCallBackListener listener : ActionManager.getActionCallbackListener()) {
			if (!handleActionForListener(listener, mAction, o)) {
				break;
			}
		}
	}

	private boolean handleActionForListener(ActionCallBackListener listener, ACTIONS action, ActionObject o) {
		try {
			return listener.handleAction(action, o);
		} catch (Throwable th) {
			C3Logger.exception(null, th);
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute(e.getActionCommand());
	}
}
