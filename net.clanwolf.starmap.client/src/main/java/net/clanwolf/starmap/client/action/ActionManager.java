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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.action;

import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceMap;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class ActionManager {

	private static final ReferenceMap<ACTIONS, C3Action> sActions = new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.SOFT, AbstractReferenceMap.ReferenceStrength.SOFT);
	private static final ReferenceMap<ACTIONS, Set<WeakReference<ActionCallBackListener>>> sActionCallbackListener = new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.SOFT, AbstractReferenceMap.ReferenceStrength.HARD);
	private static final Set<WeakReference<ActionCallBackListener>> sActionCallbackListenerSet = new HashSet<>();

	private ActionManager() {
		// only to prevent inheritance
	}

	public static C3Action getAction(ACTIONS actionEnum) {
		return getAction(actionEnum, true);
	}

	public static C3Action getAction(ACTIONS actionEnum, boolean enabled) {
		C3Action action = sActions.get(actionEnum);
		if (action == null) {
			action = new C3Action(actionEnum);
			sActions.put(actionEnum, action);
		}
		action.setEnabled(enabled);
		return action;
	}

	public static boolean addActionCallbackListener(ACTIONS actions, ActionCallBackListener listener) {
		Set<WeakReference<ActionCallBackListener>> set = sActionCallbackListener.get(actions);
		if (set == null) {
			set = new HashSet<>();
			sActionCallbackListener.put(actions, set);
		}
		return set.add(new WeakReference<>(listener));
	}

	public static boolean removeActionCallbackListener(ACTIONS actions, ActionCallBackListener listener) {
		Set<WeakReference<ActionCallBackListener>> set = sActionCallbackListener.get(actions);
		if (set == null) {
			return false;
		}
		Iterator<WeakReference<ActionCallBackListener>> iterator = set.iterator();
		while (iterator.hasNext()) {
			WeakReference<ActionCallBackListener> element = iterator.next();
			if (element.get() == null || element.get() == listener) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	public static boolean addActionCallbackListener(ActionCallBackListener listener) {
		return sActionCallbackListenerSet.add(new WeakReference<>(listener));
	}

	public static boolean removeActionCallbackListener(ActionCallBackListener listener) {
		Iterator<WeakReference<ActionCallBackListener>> iterator = sActionCallbackListenerSet.iterator();
		while (iterator.hasNext()) {
			WeakReference<ActionCallBackListener> element = iterator.next();
			if (element.get() == null || element.get() == listener) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	public static ActionCallBackListener[] getActionCallbackListener() {
		return setToArray(sActionCallbackListenerSet);
	}

	public static ActionCallBackListener[] getActionCallbackListener(ACTIONS actions) {
		return setToArray(sActionCallbackListener.get(actions));
	}

	private static ActionCallBackListener[] setToArray(Set<WeakReference<ActionCallBackListener>> set) {
		if (set == null) {
			return new ActionCallBackListener[0];
		}
		Iterator<WeakReference<ActionCallBackListener>> iterator = set.iterator();
		ActionCallBackListener[] array = new ActionCallBackListener[set.size()];
		int i = 0;
		while (iterator.hasNext()) {
			ActionCallBackListener elem = iterator.next().get();
			if (elem == null) {
				iterator.remove();
			} else {
				array[i++] = elem;
			}
		}
		int size = i;
		if (array.length > size) {
			ActionCallBackListener[] newArray = new ActionCallBackListener[size];
			System.arraycopy(array, 0, newArray, 0, size);
			array = newArray;
		}
		return array;
	}

	public static void clear() {
		sActionCallbackListener.clear();
		sActionCallbackListenerSet.clear();
		sActions.clear();
	}
}
