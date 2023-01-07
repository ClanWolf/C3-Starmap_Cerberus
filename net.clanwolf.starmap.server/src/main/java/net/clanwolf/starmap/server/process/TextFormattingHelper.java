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
 * Copyright (c) 2001-2023, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.process;

import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.FactionDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackStatsPOJO;
import net.clanwolf.starmap.server.persistence.pojos.FactionPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.clanwolf.starmap.constants.Constants.*;

/**
 * Text-Formatierungshelfer, um Berichte zu erstellen.
 * @author KERNREAKTOR
 * @version 1.0.0
 */
public class TextFormattingHelper {

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Ruft den kompletten String builder ab, der mithilfe der Methoden erzeugt wurde.
     * @return Ruft den gesamten Bericht ab.
     * @throws RuntimeException ruft die Fehlerbehandlung ab.
     */
    public StringBuilder getMailMessage() throws RuntimeException {
        return mailMessage;
    }

    private final StringBuilder mailMessage = new StringBuilder();

    /**
     * Kalkuliert, wie viel eine Zahl in einer Zahl reinpasst.
     * @param value Die gesamte Zahl.
     * @param range Die Konstante, deren Wert RANGE enthält.
     * @return gibt den Wert zurück, wie viel die Zahl reinpasst.
     */
    public Long CalcRange(Long value, Long range) {

        Long rest = value % range;

        return (value - rest) / range;
    }

    /**
     * Fügt einen Text inklusiv einer neuen Zeile in den Bericht ein.
     * @param text Der Text, der eingefügt werden soll. Ohne eine neue Zeile hinzuzufügen, weil die neue Zeile automatisch eingefügt wird.
     */
    public void addAppendText(String text) {
        mailMessage.append(text).append("\r\n");
    }

    /**
     * Fügt eine Kopfzeile ein, für den XP-Bericht.
     * @throws RuntimeException ruft die Fehlerbehandlung ab.
     */
    public void startXPReport() throws RuntimeException {
        mailMessage.append("--- Begin from the report of XP distribution ---").append("\r\n\r\n");
    }

    /**
     * Fügt die zerstörten Komponenten in den XP-Bericht ein.
     * @param countComponentDestroyed anzahl der zerstörten Komponenten.
     * @throws RuntimeException ruft die Fehlerbehandlung ab.
     */
    public void addXPComponentDestroyed(Integer countComponentDestroyed) throws RuntimeException {
        addTwoColumnsText("Component destroyed: ", XP_REWARD_COMPONENT_DESTROYED * countComponentDestroyed +
                " XP (" + XP_REWARD_COMPONENT_DESTROYED + " XP * " + countComponentDestroyed +
                " Component destroyed)");
    }

    /**
     * Fügt den Match-score in den XP-Bericht ein.
     * @param matchScore der Match score, der erreicht wurde.
     * @throws RuntimeException ruft die Fehlerbehandlung ab.
     */
    public void addXPMatchScore(Integer matchScore) throws RuntimeException {
        addTwoColumnsText("Match score: ", CalcRange(matchScore.longValue(), XP_REWARD_EACH_MATCH_SCORE_RANGE) * XP_REWARD_EACH_MATCH_SCORE +
                " XP (" + XP_REWARD_EACH_MATCH_SCORE + " XP * " +
                CalcRange(matchScore.longValue(), XP_REWARD_EACH_MATCH_SCORE_RANGE)+
                " per reached " + XP_REWARD_EACH_MATCH_SCORE_RANGE + " Match score [User match score: " +
                matchScore + "])");
    }

    /**
     * Fügt den Schaden in den XP-Bericht ein.
     * @param damage der Damage, der erreicht wurde.
     * @throws RuntimeException ruft die Fehlerbehandlung ab.
     */
    public void addXPDamage(Integer damage) throws RuntimeException {
        addTwoColumnsText("Damage: ",CalcRange(damage.longValue(), XP_REWARD_EACH_DAMAGE_RANGE) * XP_REWARD_EACH_DAMAGE +
                " XP (" + XP_REWARD_EACH_DAMAGE + " XP * " +
                CalcRange(damage.longValue(), XP_REWARD_EACH_DAMAGE_RANGE) + " per reached " +
                XP_REWARD_EACH_DAMAGE_RANGE + " Damage [User damage: " + damage + "])");
    }

    /**
     * Fügt alle relevanten Informationen in den Bericht ein.
     * @param attackStats Die AttackStatsPOJO
     * @throws RuntimeException ruft die Fehlerbehandlung ab.
     */
    public void addGameInfo(AttackStatsPOJO attackStats) throws RuntimeException {
        FactionPOJO factionAttacker = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getAttackerFactionId());
        FactionPOJO factionDefender = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getDefenderFactionId());
        FactionPOJO factionWinner = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getWinnerFactionId());

        addTwoColumnsText("Attacker: ", factionAttacker.getName_en());
        addTwoColumnsText("Defender: ", factionDefender.getName_en());
        addTwoColumnsText("Round ID: ", attackStats.getRoundId().toString());
        addTwoColumnsText("Attack ID: ", attackStats.getAttackId().toString());
        addTwoColumnsText("ID: ", attackStats.getId().toString());
        addTwoColumnsText("MWO Match ID: ", attackStats.getMwoMatchId());
        addTwoColumnsText("Map: ", attackStats.getMap());
        addTwoColumnsText("Game mode: ", attackStats.getMode());
		if (factionWinner != null) {
			addTwoColumnsText("Winner: ", factionWinner.getName_en());
		} else {
			addTwoColumnsText("Winner: ", "None / Undecided");
		}

        try {

            String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX";
            DateFormat dateFormat = new SimpleDateFormat(isoDatePattern);
            Date parsedDate = dateFormat.parse(attackStats.getDropEnded());

            addTwoColumnsText("Drop ended: ", parsedDate.toString());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Fügt einen zweispaltigen Text in den Bericht ein.
     * @param firstColumn der Text in der ersten Spalte.
     * @param secondColumn der Text in der zweiten Spalte.
     */
    public void addTwoColumnsText(String firstColumn, String secondColumn) {
        String columnWidthDefault = "%-25.25s";
        mailMessage.append(String.format(columnWidthDefault, firstColumn)).append(secondColumn).append("\r\n");
    }
}
