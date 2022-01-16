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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.util;


/**
 * Diese Klasse erstellt einen Benutzerdefinierten Text-Fortschrittsbalken.
 *
 * Die leeren und die gefüllten Text-Fortschrittsbalken können frei definiert werden.
 *
 * @author KERNREAKTOR
 */
public class TxtProgressBar {

    private int maxvalue;
    private String emptybar;
    private String filledbar;
    private String currentpercentvalue;

    /**
     * Ruft den (int) Wert ab der 100 % entspricht, der bei {@link #setMaxvalue(int)} gesetz wurde oder bei
     * {@link #TxtProgressBar(int, String, String)} Instanziiert wurde.
     *
     * @return Gibt den (int) Wert zurück, der 100 % entspricht.
     */
    public int getMaxvalue() {
        return maxvalue;
    }

    /**
     * Setz einen neuen (int) Wert der 100 % entsprechen soll.
     *
     * Dieser neue Wert kann bei {@link #getMaxvalue()} abgerufen werden.
     *
     * @param maxvalue Der neue (int) Wert der 100 % entsprechen soll.
     */
    public void setMaxvalue(int maxvalue) {
        this.maxvalue = maxvalue;
    }

    /**
     * Ruft das Zeichen für den leeren Fortschrittsbalken als (String) ab,
     * der bei {@link #setEmptybar(String)} gesetz wurde.
     *
     * @return (String) Zeichen der leeren Fortschrittsbalken.
     */
    public String getEmptybar() {
        return emptybar;
    }

    /**
     * Setz ein neues (String) Zeichen für den leeren Fortschrittsbalken,
     * der bei {@link #getEmptybar()} abgerufen werden kann.
     *
     * @param emptybar Das neue (String) Zeichen für den leeren Balken.
     */
    public void setEmptybar(String emptybar) {
        this.emptybar = emptybar;
    }

    /**
     *Ruft das Zeichen für den vollen Fortschrittsbalken asl (String) ab,
     * der bei {@link #setFilledbar(String)} gesetz wurde.
     *
     * @return (String) Zeichen der gefüllten Fortschrittsbalken.
     */
    public String getFilledbar() {
        return filledbar;
    }

    /**
     * Setz ein neues (String) Zeichen für den vollen Fortschrittsbalken,
     * der bei {@link #getFilledbar()} abgerufen werden kann.
     *
     * @param filledbar Das neue (String) Zeichen für den vollen Balken.
     */
    public void setFilledbar(String filledbar) {
        this.filledbar = filledbar;
    }

    /**
     *
     * @param maxvalue Ein (int) Wert der 100 % entsprechen soll.
     * @param emptybar Das (String) Zeichen was für den leeren Balken dargestellt werden soll.
     * @param filledbar Das (String) Zeichen was für den vollen Balken dargestellt werden soll.
     */
    public TxtProgressBar(int maxvalue, String emptybar, String filledbar) {

        this.emptybar=emptybar;
        this.filledbar = filledbar;
        this.maxvalue = maxvalue;

    }

    /**
     * Ruft den aktuellen Wert in % ab der bei {@link #getcurprogress(int)} gesetz wurde.
     *
     * @return Gibt den aktuellen Wert in (String) Prozent mit dem %-Zeichen zurück.
     */
    public String getCurrentpercentvalue() {
        return this.currentpercentvalue;
    }

    /**
     * Erstellt einen (String) Text-Fortschrittsbalken vom aktuellen Wert.
     *
     * @param currentvalue Aktueller (int) Wert.
     * @return Gibt den (String) Text-Fortschrittsbalken zurück.
     */
    public String getcurprogress(int currentvalue) {
        int curpercent = currentvalue * 100 / this.maxvalue;
        StringBuilder txtbar = new StringBuilder();

        if(curpercent <10) {

            txtbar.append(String.valueOf(this.emptybar).repeat(10));

        } else {

            txtbar.append(String.valueOf(this.filledbar).repeat( curpercent / 10));
            txtbar.append(String.valueOf(this.emptybar).repeat(10 - (curpercent / 10)));

        }

        this.currentpercentvalue = curpercent  + " %";

        return txtbar.toString();
    }
}
