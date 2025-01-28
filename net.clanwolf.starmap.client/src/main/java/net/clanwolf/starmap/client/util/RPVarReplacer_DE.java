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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.util;

import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

public class RPVarReplacer_DE {

	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	// "Der Jäger jagt den Fuchs"
	// Wer jagd den Fuchs? Der Jäger (Nominativ)
	// Wen jagt der Jäger? Den Fuchs (Akkusativ)

	// "Der Mann gibt der Frau das Buch des Bruders"
	// Wer gibt das Buch? Der Mann (Nominativ)
	// Wem gibt er das Buch? Der Frau (Dativ)
	// Wen/Was gibt der Mann der Frau? Das Buch (Akkusativ)
	// Wessen Buch ist es? Des Bruders (Genitiv)

	private static final HashMap<String, HashMap<String, String>> factionNameCases = new HashMap<>();
	private static final HashMap<String, String> dc = new HashMap<>();
	private static final HashMap<String, String> la = new HashMap<>();
	private static final HashMap<String, String> frr = new HashMap<>();
	private static final HashMap<String, String> cs = new HashMap<>();
	private static final HashMap<String, String> cw = new HashMap<>();
	private static final HashMap<String, String> cjf = new HashMap<>();
	private static final HashMap<String, String> cgb = new HashMap<>();
	private static final HashMap<String, String> cnc = new HashMap<>();
	private static final HashMap<String, String> csv = new HashMap<>();

	public static void fillLists() {
		dc.put("nom", "das Draconis Kombinat");                  // 1. Nominativ (wer)
		dc.put("dat", "dem Draconis Kombinat");                  // 2. Dativ     (wem)
		dc.put("akk", "das Draconis Kombinat");                  // 3. Akkusativ (wen/was)
		dc.put("gen", "des Draconis Kombinates");                // 4. Genitiv   (wessen)
		factionNameCases.put("DC", dc);

		la.put("nom", "die Lyranische Allianz");                 // 1. Nominativ (wer)
		la.put("dat", "der Lyranischen Allianz");                // 2. Dativ     (wem)
		la.put("akk", "die Lyranische Allianz");                 // 3. Akkusativ (wen/was)
		la.put("gen", "der Lyranischen Allianz");                // 4. Genitiv   (wessen)
		factionNameCases.put("LA", la);

		frr.put("nom", "die Freie Republik Rasalhague");         // 1. Nominativ (wer)
		frr.put("dat", "der Freien Republik Rasalhague");        // 2. Dativ     (wem)
		frr.put("akk", "die Freie Republik Rasalhague");         // 3. Akkusativ (wen/was)
		frr.put("gen", "der Freien Republik Rasalhague");        // 4. Genitiv   (wessen)
		factionNameCases.put("FRR", frr);

		cs.put("nom", "Comstar");                                // 1. Nominativ (wer)
		cs.put("dat", "Comstar");                                // 2. Dativ     (wem)
		cs.put("akk", "Comstar");                                // 3. Akkusativ (wen/was)
		cs.put("gen", "Comstars");                               // 4. Genitiv   (wessen)
		factionNameCases.put("CS", cs);

		cw.put("nom", "Clan Wolf");                              // 1. Nominativ (wer)
		cw.put("dat", "den Wölfen");                             // 2. Dativ     (wem)
		cw.put("akk", "die Wölfe");                              // 3. Akkusativ (wen/was)
		cw.put("gen", "der Wölfe");                              // 4. Genitiv   (wessen)
		factionNameCases.put("CW", cw);

		cjf.put("nom", "Clan Jadefalke");                        // 1. Nominativ (wer)
		cjf.put("dat", "den Falken");                            // 2. Dativ     (wem)
		cjf.put("akk", "die Falken");                            // 3. Akkusativ (wen/was)
		cjf.put("gen", "den Falken");                            // 4. Genitiv   (wessen)
		factionNameCases.put("CJF", cjf);

		cgb.put("nom", "Clan Geisterbär");                       // 1. Nominativ (wer)
		cgb.put("dat", "den Geisterbären");                      // 2. Dativ     (wem)
		cgb.put("akk", "die Geisterbären");                      // 3. Akkusativ (wen/was)
		cgb.put("gen", "den Geisterbären");                      // 4. Genitiv   (wessen)
		factionNameCases.put("CGB", cgb);

		cnc.put("nom", "Clan Novakatze");                        // 1. Nominativ (wer)
		cnc.put("dat", "den Novakatzen");                        // 2. Dativ     (wem)
		cnc.put("akk", "die Novakatzen");                        // 3. Akkusativ (wen/was)
		cnc.put("gen", "den Novakatzen");                        // 4. Genitiv   (wessen)
		factionNameCases.put("CNC", cnc);

		csv.put("nom", "Clan Stahlviper");                       // 1. Nominativ (wer)
		csv.put("dat", "den Stahlvipern");                       // 2. Dativ     (wem)
		csv.put("akk", "die Stahlvipern");                       // 3. Akkusativ (wen/was)
		csv.put("gen", "den Stahlvipern");                       // 4. Genitiv   (wessen)
		factionNameCases.put("CSV", csv);
	}

	public static String replaceVars(String text) {
		text = text.replace("@@PLANET@@", RPVarReplacer_DE.getValueForKey("@@PLANET@@"));
		text = text.replace("@@ATTACKER@@", RPVarReplacer_DE.getValueForKey("@@ATTACKER@@"));
		text = text.replace("@@DEFENDER@@", RPVarReplacer_DE.getValueForKey("@@DEFENDER@@"));
		text = text.replace("@@ATTACKER|nom@@", RPVarReplacer_DE.getValueForKey("@@ATTACKER|nom@@"));
		text = text.replace("@@ATTACKER|dat@@", RPVarReplacer_DE.getValueForKey("@@ATTACKER|dat@@"));
		text = text.replace("@@ATTACKER|akk@@", RPVarReplacer_DE.getValueForKey("@@ATTACKER|akk@@"));
		text = text.replace("@@ATTACKER|gen@@", RPVarReplacer_DE.getValueForKey("@@ATTACKER|gen@@"));
		text = text.replace("@@DEFENDER|nom@@", RPVarReplacer_DE.getValueForKey("@@DEFENDER|nom@@"));
		text = text.replace("@@DEFENDER|dat@@", RPVarReplacer_DE.getValueForKey("@@DEFENDER|dat@@"));
		text = text.replace("@@DEFENDER|akk@@", RPVarReplacer_DE.getValueForKey("@@DEFENDER|akk@@"));
		text = text.replace("@@DEFENDER|gen@@", RPVarReplacer_DE.getValueForKey("@@DEFENDER|gen@@"));

		if (text.contains("Beschreibung:")) {
			String part1 = text.substring(0, text.indexOf("Beschreibung:") + 14);
			String part2 = text.substring(text.indexOf("Beschreibung:") + 14);
			part2 = part2.substring(0, 1).toUpperCase() + part2.substring(1);
			text = part1 + part2;
		}

		return text;
	}

	public static String getValueForKey(String key) {
		if (factionNameCases.isEmpty()) {
			fillLists();
		}
		BOAttack attack = Nexus.getCurrentAttackOfUser();
		if (attack != null) {
			String attackerShortName = attack.getAttackerFactionShortName();
			String defenderShortName = attack.getDefenderFactionShortName();
			HashMap<String, String> attackerNameCases = factionNameCases.get(attackerShortName);
			HashMap<String, String> defenderNameCases = factionNameCases.get(defenderShortName);

			if (attackerNameCases == null || defenderNameCases == null) {
				logger.error("Faction strings have not been defined!");
				return "@@NOTFOUND@@";
			}

			return switch (key) {
				case "@@PLANET@@" -> attack.getStarSystemName();
				case "@@ATTACKER@@" -> attack.getAttackerFactionName();
				case "@@DEFENDER@@" -> attack.getDefenderFactionName();
				case "@@ATTACKER|nom@@" -> attackerNameCases.get("nom");
				case "@@ATTACKER|dat@@" -> attackerNameCases.get("dat");
				case "@@ATTACKER|akk@@" -> attackerNameCases.get("akk");
				case "@@ATTACKER|gen@@" -> attackerNameCases.get("gen");
				case "@@DEFENDER|nom@@" -> defenderNameCases.get("nom");
				case "@@DEFENDER|dat@@" -> defenderNameCases.get("dat");
				case "@@DEFENDER|akk@@" -> defenderNameCases.get("akk");
				case "@@DEFENDER|gen@@" -> defenderNameCases.get("gen");
				default -> "";
			};
		} else {
			return "ERROR: Attack is null!";
		}
	}
}
