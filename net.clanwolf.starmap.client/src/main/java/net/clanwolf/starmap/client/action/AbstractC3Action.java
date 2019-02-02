/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : http://www.clanwolf.net                            |
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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.action;

import net.clanwolf.starmap.client.util.Internationalization;

import javax.swing.*;

/**
 * Abstract C3 Action
 *
 * @author Meldric
 */
public abstract class AbstractC3Action extends AbstractAction implements ActionCallBackListener {

	private static final long serialVersionUID = 1L;
	private String mNameKey;
	private ACTIONS mAction;

	/**
	 * Protected constructor.
	 */
	@SuppressWarnings("unused")
	protected AbstractC3Action() {
		boolean addActionCallbackListener;
		addActionCallbackListener = ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
	}

	/**
	 * Constructor taking Action.
	 *
	 * @param action
	 * @author kotzbrocken2
	 */
	public AbstractC3Action(ACTIONS action) {
		this();
		mAction = action;
		configureFromAction();
	}

	private void configureFromAction() {
		if (mAction != null) {
			setName(mAction.getNameKey());
			setToolTip(mAction.getToolTipKey());
			setDescription(mAction.getDescriptionKey());
			setAccelerator(mAction.getAcceleratorKey());
			setMnemonicIndex(mAction.getMnemonicIndex());
		}
	}

	/**
	 * Constructor taking String.
	 *
	 * @param nameKey The name key.
	 */
	public AbstractC3Action(String nameKey) {
		this();
		setName(nameKey);
	}

	/**
	 * Set a name.
	 *
	 * @param nameKey Key of the name.
	 */
	public void setName(String nameKey) {
		putValue(NAME, Internationalization.getString(nameKey));
		mNameKey = nameKey;
	}

	/**
	 * Set a tooltip.
	 *
	 * @param toolTipKey
	 */
	public void setToolTip(String toolTipKey) {
		putValue(SHORT_DESCRIPTION, Internationalization.getString(toolTipKey));
	}

	/**
	 * Set the description.
	 *
	 * @param descriptionKey key
	 */
	public void setDescription(String descriptionKey) {
		putValue(LONG_DESCRIPTION, Internationalization.getString(descriptionKey));
	}

	/**
	 * Set accelerator.
	 *
	 * @param acceleratorKey key
	 */
	public void setAccelerator(String acceleratorKey) {
		putValue(ACCELERATOR_KEY, acceleratorKey == null ? null : KeyStroke.getKeyStroke(Internationalization.getString(acceleratorKey)));
	}

	/**
	 * @param mnemonicIndexKey
	 */
	public void setMnemonicIndex(String mnemonicIndexKey) {
		try {
			int index = Integer.parseInt(Internationalization.getString(mnemonicIndexKey));
			super.putValue(DISPLAYED_MNEMONIC_INDEX_KEY, index);
			String name = (String) super.getValue(NAME);
			super.putValue(MNEMONIC_KEY, (int) Character.toUpperCase(name.charAt(index)));
		} catch (NumberFormatException e) {
			// do nothing
		}
	}

	@Override
	public boolean handleAction(ACTIONS action, ActionObject object) {
		setName(mNameKey);
		configureFromAction();
		return true;
	}
}
