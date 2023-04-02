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
package net.clanwolf.starmap.transfer.mwo;

import net.clanwolf.starmap.exceptions.MechItemIdNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * Diese Klasse weist der MechItemId die von der MWO Api ausgelesen wird,
 * einen Mech' zu und diese Eigenschaften des Mech's können ausgelesen werden.
 *
 * @author KERNREAKTOR
 * @version 31-03-2023
 */
public class MechIdInfo {
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Integer mechItemId;
    private String mechFaction;
    private String mechChassis;
    private String mechVariantType;
    private Integer mechMaxTons;
    private Double mechBaseTons;
    private String mechName;
    private Integer mechMaxJumpJets;
    private Integer mechMinEngineRating;
    private Integer mechMaxEngineRating;
    private String mechLongName;
    private String mechShortName;
    private Integer HP;

    /**
     * Erzeugt ein neues Objekt, anhand der MechItemId
     *
     * @param mechItemId Die Mech ID die in der API ausgegeben wird.
     */
    public MechIdInfo(Integer mechItemId) throws ParserConfigurationException, IOException, SAXException, MechItemIdNotFoundException {

        this.mechItemId = mechItemId;

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Objects.requireNonNull(MechIdInfo.class.getResourceAsStream("/mechinfo/AllMechs.xml")));
        doc.getDocumentElement().normalize();

        boolean mechfound = false;
        NodeList mechNodes = doc.getElementsByTagName("Mech");
        for (int i = 0; i < mechNodes.getLength(); i++) {
            Element xmlMechList = (Element) mechNodes.item(i);
            if (Objects.equals(mechItemId, Integer.valueOf(xmlMechList.getAttribute("id")))) {
                mechfound = true;
                this.mechChassis = xmlMechList.getAttribute("chassis");
                this.mechFaction = xmlMechList.getAttribute("faction");
                this.mechName = xmlMechList.getAttribute("name");
                this.mechBaseTons = Double.valueOf(xmlMechList.getAttribute("BaseTons"));
                this.mechMaxTons = Integer.valueOf(xmlMechList.getAttribute("MaxTons"));
                this.mechMaxJumpJets = Integer.valueOf(xmlMechList.getAttribute("MaxJumpJets"));
                this.mechMaxEngineRating = Integer.valueOf(xmlMechList.getAttribute("MinEngineRating"));
                this.mechMinEngineRating = Integer.valueOf(xmlMechList.getAttribute("MaxEngineRating"));
                this.mechVariantType = xmlMechList.getAttribute("VariantType").toUpperCase();
                this.mechLongName = xmlMechList.getAttribute("longname");
                this.mechShortName = xmlMechList.getAttribute("shortname");
                this.HP = Integer.valueOf(xmlMechList.getAttribute("HP"));
                break;
            }
        }
        if (!mechfound) {
            this.mechChassis = "Unknown";
            this.mechFaction = "Unknown";
            this.mechName = "Unknown";
            this.mechBaseTons = -1.0;
            this.mechMaxTons = -1;
            this.mechMaxJumpJets = -1;
            this.mechMaxEngineRating = -1;
            this.mechMinEngineRating = -1;
            this.mechVariantType = "Unknown";
            this.mechLongName = "Unknown";
            this.mechShortName = "Unknown";
            this.HP = -1;

            throw new MechItemIdNotFoundException("Mech ID: " + mechItemId + " not found in the xml file.");
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, MechItemIdNotFoundException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Objects.requireNonNull(MechIdInfo.class.getResourceAsStream("/mechinfo/AllMechs.xml")));
        doc.getDocumentElement().normalize();
        NodeList mechNodes = doc.getElementsByTagName("Mech");

        for (int i = 0; i < mechNodes.getLength(); i++) {
            Element xmlMechList = (Element) mechNodes.item(i);
            Integer mechItemId = Integer.valueOf(xmlMechList.getAttribute("id"));
//            String mechChassie = xmlMechList.getAttribute("chassis");
//            String mechFaction = xmlMechList.getAttribute("faction");
//            String mechName = xmlMechList.getAttribute("name");
//            Integer mechMaxTons = Integer.valueOf(xmlMechList.getAttribute("MaxTons"));
//            Double mechBaseTons = Double.valueOf(xmlMechList.getAttribute("BaseTons"));
//            Integer mechMaxJumpJets = Integer.valueOf(xmlMechList.getAttribute("MaxJumpJets"));
//            Integer mechMinEngineRating = Integer.valueOf(xmlMechList.getAttribute("MinEngineRating"));
//            Integer mechMaxEngineRating = Integer.valueOf(xmlMechList.getAttribute("MaxEngineRating"));
//            String mechVariantType = xmlMechList.getAttribute("VariantType");

            try {
                MechIdInfo mechIdInfo;
                mechIdInfo = new MechIdInfo(mechItemId);
                System.out.println(mechIdInfo.getFullName() + "|" + mechIdInfo.getShortname() + "|" + mechIdInfo.getMechVariantType() + "|" + mechIdInfo.getTonnage());

                //String[] variant = {"Standard", "Special", "Hero", "Champion"};

	           /* Mech mechInfo = Mech.getMech(mechChassie, mechFaction, mechItemId, mechName);
	            if (mechInfo == null) {
	                System.out.println("Unknown Mech: " + mechChassie);
	                continue;
	            }
	            if (!Arrays.asList(variant).contains(mechVariantType)) {
	                System.out.println(mechVariantType);
	            }*/
            } catch (MechItemIdNotFoundException e) {
                logger.error("MechItemIdNotFoundException", e);
            }
        }
    }

    public Integer getHP() {
        return HP;
    }

    public Double getMechBaseTons() {
        return mechBaseTons;
    }

    public Integer getMechMaxJumpJets() {
        return mechMaxJumpJets;
    }

    public Integer getMechMinEngineRating() {
        return mechMinEngineRating;
    }

    public Integer getMechMaxEngineRating() {
        return mechMaxEngineRating;
    }

    public String toString() {
        try {
            return "MechitemID: " + getMechItemId() + " " + getMechChassis() + " " + getFullName() + " is a " + getMechFaction() + " " + getMechClass() + " Mech an have " + getTonnage() + " tons";
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Die (int) Tonnage des Mech's wird zurückgegeben.
     *
     * @return Gibt die (int) Tonnage des Mech's zurück.
     */
    public Integer getTonnage() {
        return this.mechMaxTons;
    }

    /**
     * Die (Enum) Fraktion wird zurückgegeben.
     *
     * @return Gibt die (Enum) Fraktion des Mech's zurück.
     */
    public String getMechFaction() {
        return this.mechFaction;
    }

    /**
     * Gibt die MechItemId (Integer) zurück, die bei {@link MechIdInfo} angegeben wurde.
     *
     * @return Gibt die MechItemId zurück.
     */
    public Integer getMechItemId() {
        return mechItemId;
    }

    /**
     * Die (String) Chassis des Mech's wird zurückgegeben.
     *
     * @return Gibt die (String) Chassis des Mech's zurück.
     */
    public String getMechChassis() throws ParserConfigurationException, IOException, SAXException {
        String mechChassi = null;
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Objects.requireNonNull(MechIdInfo.class.getResourceAsStream("/mechinfo/AllMechsChassis.xml")));
        doc.getDocumentElement().normalize();

        NodeList mechNodes = doc.getElementsByTagName("Mech");

        for (int i = 0; i < mechNodes.getLength(); i++) {
            Element xmlMechList = (Element) mechNodes.item(i);
            if (Objects.equals(this.mechChassis, xmlMechList.getAttribute("chassis"))) {
                mechChassi = xmlMechList.getAttribute("ingamechassis");
                break;
            }
        }
        return mechChassi;
    }

    /**
     * Die (Enum) Mechklasse wird zurückgegeben:
     *
     * @return Gibt die (Enum) Mechklasse zurück.
     */
    public EMechclass getMechClass() throws ParserConfigurationException, IOException, SAXException {
        EMechclass mechclass = null;
        switch (mechMaxTons) {
            case 20, 25, 30, 35 -> mechclass = EMechclass.LIGHT;
            case 40, 45, 50, 55 -> mechclass = EMechclass.MEDIUM;
            case 60, 65, 70, 75 -> mechclass = EMechclass.HEAVY;
            case 80, 85, 90, 95, 100 -> mechclass = EMechclass.ASSAULT;
            case -1 -> {
                mechclass = EMechclass.UNKNOWN;
                logger.error("Unknown mech class");
            }
        }
        return mechclass;
    }

    /**
     * Die (String) Variante des Mech's wird zurückgegeben.
     *
     * <p>Die (String) Werte die er zurückgeben kann ist: CHAMPION, HERO, SPECIAL und STANDARD</p>
     *
     * @return Gibt die (String) Variante zurück.
     */
    public String getMechVariantType() {
        return this.mechVariantType;
    }

    /**
     * Der Vollständige (String) Mechname wird zurückgegeben.
     *
     * <p>Ein Beispielname wäre: GARGOYLE GAR-PRIME(I)</p>
     *
     * @return Gib den (String) vollständigen Mechnamen zurück.
     */
    public String getFullName() {
        return this.mechLongName;
    }

    /**
     * Die Kurzform des (String) Mech'snamens wird zurückgeben.
     *
     * <p>Ein Beispiel wie es aussehen würde: GAR-PRIME(I)</p>
     *
     * @return Gibt die Kurzform des (String) Mechnamen zurück.
     */
    public String getShortname() {
        return this.mechShortName;
    }

    public String getMechName() {
        return mechName;
    }

    /**
     * Mechklassen die es in MWO gibt.
     */
    public enum EMechclass {
        LIGHT, MEDIUM, HEAVY, ASSAULT, UNKNOWN
    }
}
