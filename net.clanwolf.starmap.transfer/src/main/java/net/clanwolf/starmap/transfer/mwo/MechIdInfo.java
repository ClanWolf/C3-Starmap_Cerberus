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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


/**
 * Diese Klasse weist der MechItemId die von der MWO Api ausgelesen wird,
 * einen Mech' zu und diese Eigenschaften des Mech's können ausgelesen werden.
 *
 * @author KERNREAKTOR
 * @version 13-08-2022
 */
public class MechIdInfo {
    //private final Map<Integer, MechIdInfo> mechID = new HashMap<>();
    //private final String msgInvalidID = "Invalid MechItemID";
    private String mechFaction;
    private String mechChassis;
    private String mechVariantType;

    private Integer mechItemId;
    private Integer mechMaxTons;
    private Double mechBaseTons;
    private String mechName;
    private Integer mechMaxJumpJets;
    private Integer mechMinEngineRating;

    public Double getMechBaseTons() {
        return mechBaseTons;
    }

    public void setMechBaseTons(Double mechBaseTons) {
        this.mechBaseTons = mechBaseTons;
    }

    public String getMechName() {
        return mechName;
    }

    public void setMechName(String mechName) {
        this.mechName = mechName;
    }

    public Integer getMechMaxJumpJets() {
        return mechMaxJumpJets;
    }

    public void setMechMaxJumpJets(Integer mechMaxJumpJets) {
        this.mechMaxJumpJets = mechMaxJumpJets;
    }

    public Integer getMechMinEngineRating() {
        return mechMinEngineRating;
    }

    public void setMechMinEngineRating(Integer mechMinEngineRating) {
        this.mechMinEngineRating = mechMinEngineRating;
    }

    public Integer getMechMaxEngineRating() {
        return mechMaxEngineRating;
    }

    public void setMechMaxEngineRating(Integer mechMaxEngineRating) {
        this.mechMaxEngineRating = mechMaxEngineRating;
    }

    private Integer mechMaxEngineRating;
    private String mechLongName;
    private String mechShortName;
    private Boolean mechValid;

    /*private MechIdInfo(EFaction faction, EChassie chassis, EVariantType variantType, String fullName, String shortName) {
        this.faction = faction;
        this.chassis = chassis;
        this.variantType = variantType;
        this.fullName = fullName;
        this.shortname = shortName;
    }*/

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new File(Objects.requireNonNull(MechIdInfo.class.getResource("/mechinfo/AllMechs.xml")).getFile()));
        doc.getDocumentElement().normalize();

        NodeList mechNodes = doc.getElementsByTagName("Mech");
        for (int i = 0; i < mechNodes.getLength(); i++) {
            Element xmlMechList = (Element) mechNodes.item(i);
            String mechChassie = xmlMechList.getAttribute("chassis");
            Integer mechItemId = Integer.valueOf(xmlMechList.getAttribute("id"));
            String mechFaction = xmlMechList.getAttribute("faction");
            String mechName = xmlMechList.getAttribute("name");
            Integer mechMaxTons = Integer.valueOf(xmlMechList.getAttribute("MaxTons"));
            Double mechBaseTons = Double.valueOf(xmlMechList.getAttribute("BaseTons"));
            Integer mechMaxJumpJets = Integer.valueOf(xmlMechList.getAttribute("MaxJumpJets"));
            Integer mechMinEngineRating = Integer.valueOf(xmlMechList.getAttribute("MinEngineRating"));
            Integer mechMaxEngineRating = Integer.valueOf(xmlMechList.getAttribute("MaxEngineRating"));
            String mechVariantType = xmlMechList.getAttribute("VariantType");
            MechIdInfo mechIdInfo = new MechIdInfo(mechItemId);
            System.out.println(mechIdInfo);
            //String[] variant = {"Standard", "Special", "Hero", "Champion"};
           /* Mech mechInfo = Mech.getMech(mechChassie, mechFaction, mechItemId, mechName);
            if (mechInfo == null) {
                System.out.println("Unknown Mech: " + mechChassie);
                continue;
            }
            if (!Arrays.asList(variant).contains(mechVariantType)) {
                System.out.println(mechVariantType);
            }*/

        }
    }
/*
    private void InitializeMechIds() {

        this.mechID.clear();
        this.mechID.put(1, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.STANDARD, "HUNCHBACK HBK-4G", "HBK-4G"));
        this.mechID.put(2, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.STANDARD, "HUNCHBACK HBK-4P", "HBK-4P"));
        this.mechID.put(3, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JENNER, EVariantType.STANDARD, "JENNER JR7-D", "JR7-D"));
        this.mechID.put(4, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JENNER, EVariantType.STANDARD, "JENNER JR7-F", "JR7-F"));
        this.mechID.put(5, new MechIdInfo(EFaction.INNERSPHERE, EChassie.COMMANDO, EVariantType.STANDARD, "COMMANDO COM-2D", "COM-2D"));
        this.mechID.put(6, new MechIdInfo(EFaction.INNERSPHERE, EChassie.COMMANDO, EVariantType.STANDARD, "COMMANDO COM-3A", "COM-3A"));
        this.mechID.put(7, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CENTURION, EVariantType.STANDARD, "CENTURION CN9-A", "CN9-A"));
        this.mechID.put(8, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CENTURION, EVariantType.SPECIAL, "CENTURION CN9-AH(L)", "CN9-AH(L)"));
        this.mechID.put(9, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.STANDARD, "HUNCHBACK HBK-4H", "HBK-4H"));
        this.mechID.put(10, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DRAGON, EVariantType.STANDARD, "DRAGON DRG-1N", "DRG-1N"));
        this.mechID.put(11, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DRAGON, EVariantType.STANDARD, "DRAGON DRG-1C", "DRG-1C"));
        this.mechID.put(12, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.STANDARD, "CATAPULT CPLT-C1", "CPLT-C1"));
        this.mechID.put(13, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.STANDARD, "CATAPULT CPLT-A1", "CPLT-A1"));
        this.mechID.put(14, new MechIdInfo(EFaction.INNERSPHERE, EChassie.AWESOME, EVariantType.STANDARD, "AWESOME AWS-8Q", "AWS-8Q"));
        this.mechID.put(15, new MechIdInfo(EFaction.INNERSPHERE, EChassie.AWESOME, EVariantType.STANDARD, "AWESOME AWS-8R", "AWS-8R"));
        this.mechID.put(16, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.STANDARD, "ATLAS AS7-D", "AS7-D"));
        this.mechID.put(17, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.STANDARD, "ATLAS AS7-D-DC", "AS7-D-DC"));
        this.mechID.put(18, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.STANDARD, "ATLAS AS7-RS", "AS7-RS"));
        this.mechID.put(19, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.STANDARD, "CATAPULT CPLT-K2", "CPLT-K2"));
        this.mechID.put(20, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JENNER, EVariantType.STANDARD, "JENNER JR7-K", "JR7-K"));
        this.mechID.put(21, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.STANDARD, "HUNCHBACK HBK-4J", "HBK-4J"));
        this.mechID.put(22, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.STANDARD, "HUNCHBACK HBK-4SP", "HBK-4SP"));
        this.mechID.put(23, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DRAGON, EVariantType.STANDARD, "DRAGON DRG-5N", "DRG-5N"));
        this.mechID.put(24, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.STANDARD, "CATAPULT CPLT-C4", "CPLT-C4"));
        this.mechID.put(25, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.STANDARD, "ATLAS AS7-K", "AS7-K"));
        this.mechID.put(26, new MechIdInfo(EFaction.INNERSPHERE, EChassie.COMMANDO, EVariantType.STANDARD, "COMMANDO COM-1B", "COM-1B"));
        this.mechID.put(27, new MechIdInfo(EFaction.INNERSPHERE, EChassie.COMMANDO, EVariantType.STANDARD, "COMMANDO COM-1D", "COM-1D"));
        this.mechID.put(28, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CENTURION, EVariantType.STANDARD, "CENTURION CN9-AL", "CN9-AL"));
        this.mechID.put(29, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CENTURION, EVariantType.STANDARD, "CENTURION CN9-D", "CN9-D"));
        this.mechID.put(30, new MechIdInfo(EFaction.INNERSPHERE, EChassie.AWESOME, EVariantType.STANDARD, "AWESOME AWS-8T", "AWS-8T"));
        this.mechID.put(31, new MechIdInfo(EFaction.INNERSPHERE, EChassie.AWESOME, EVariantType.STANDARD, "AWESOME AWS-8V", "AWS-8V"));
        this.mechID.put(32, new MechIdInfo(EFaction.INNERSPHERE, EChassie.AWESOME, EVariantType.STANDARD, "AWESOME AWS-9M", "AWS-9M"));
        this.mechID.put(33, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RAVEN, EVariantType.STANDARD, "RAVEN RVN-3L", "RVN-3L"));
        this.mechID.put(34, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RAVEN, EVariantType.STANDARD, "RAVEN RVN-2X", "RVN-2X"));
        this.mechID.put(35, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RAVEN, EVariantType.STANDARD, "RAVEN RVN-4X", "RVN-4X"));
        this.mechID.put(36, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CICADA, EVariantType.STANDARD, "CICADA CDA-3M", "CDA-3M"));
        this.mechID.put(37, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CICADA, EVariantType.STANDARD, "CICADA CDA-2A", "CDA-2A"));
        this.mechID.put(38, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CICADA, EVariantType.STANDARD, "CICADA CDA-2B", "CDA-2B"));
        this.mechID.put(39, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CICADA, EVariantType.STANDARD, "CICADA CDA-3C", "CDA-3C"));
        this.mechID.put(40, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPHRACT, EVariantType.STANDARD, "CATAPHRACT CTF-3D", "CTF-3D"));
        this.mechID.put(41, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPHRACT, EVariantType.STANDARD, "CATAPHRACT CTF-1X", "CTF-1X"));
        this.mechID.put(42, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPHRACT, EVariantType.STANDARD, "CATAPHRACT CTF-2X", "CTF-2X"));
        this.mechID.put(43, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPHRACT, EVariantType.STANDARD, "CATAPHRACT CTF-3L", "CTF-3L"));
        this.mechID.put(44, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPHRACT, EVariantType.STANDARD, "CATAPHRACT CTF-4X", "CTF-4X"));
        this.mechID.put(45, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CENTURION, EVariantType.HERO, "YEN-LO-WANG", "CN9-YLW"));
        this.mechID.put(46, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.STANDARD, "STALKER STK-5M", "STK-5M"));
        this.mechID.put(47, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.STANDARD, "STALKER STK-3F", "STK-3F"));
        this.mechID.put(48, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.STANDARD, "STALKER STK-3H", "STK-3H"));
        this.mechID.put(49, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.STANDARD, "STALKER STK-4N", "STK-4N"));
        this.mechID.put(50, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.STANDARD, "STALKER STK-5S", "STK-5S"));
        this.mechID.put(51, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SPIDER, EVariantType.STANDARD, "SPIDER SDR-5V", "SDR-5V"));
        this.mechID.put(52, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SPIDER, EVariantType.STANDARD, "SPIDER SDR-5D", "SDR-5D"));
        this.mechID.put(53, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SPIDER, EVariantType.STANDARD, "SPIDER SDR-5K", "SDR-5K"));
        this.mechID.put(54, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPHRACT, EVariantType.HERO, "ILYA MUROMETS", "CTF-IM"));
        this.mechID.put(55, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DRAGON, EVariantType.HERO, "FANG", "DRG-FANG"));
        this.mechID.put(56, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DRAGON, EVariantType.HERO, "FLAME", "DRG-FLAME"));
        this.mechID.put(57, new MechIdInfo(EFaction.INNERSPHERE, EChassie.COMMANDO, EVariantType.HERO, "THE DEATH'S KNELL", "COM-TDK"));
        this.mechID.put(58, new MechIdInfo(EFaction.INNERSPHERE, EChassie.AWESOME, EVariantType.HERO, "PRETTY BABY", "AWS-PB"));
        this.mechID.put(59, new MechIdInfo(EFaction.INNERSPHERE, EChassie.TREBUCHET, EVariantType.STANDARD, "TREBUCHET TBT-7M", "TBT-7M"));
        this.mechID.put(60, new MechIdInfo(EFaction.INNERSPHERE, EChassie.TREBUCHET, EVariantType.STANDARD, "TREBUCHET TBT-3C", "TBT-3C"));
        this.mechID.put(61, new MechIdInfo(EFaction.INNERSPHERE, EChassie.TREBUCHET, EVariantType.STANDARD, "TREBUCHET TBT-5J", "TBT-5J"));
        this.mechID.put(62, new MechIdInfo(EFaction.INNERSPHERE, EChassie.TREBUCHET, EVariantType.STANDARD, "TREBUCHET TBT-5N", "TBT-5N"));
        this.mechID.put(63, new MechIdInfo(EFaction.INNERSPHERE, EChassie.TREBUCHET, EVariantType.STANDARD, "TREBUCHET TBT-7K", "TBT-7K"));
        this.mechID.put(64, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CICADA, EVariantType.HERO, "THE X-5", "CDA-X5"));
        this.mechID.put(65, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAGERMECH, EVariantType.STANDARD, "JAGERMECH JM6-DD", "JM6-DD"));
        this.mechID.put(66, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAGERMECH, EVariantType.STANDARD, "JAGERMECH JM6-A", "JM6-A"));
        this.mechID.put(67, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAGERMECH, EVariantType.STANDARD, "JAGERMECH JM6-S", "JM6-S"));
        this.mechID.put(69, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HIGHLANDER, EVariantType.HERO, "HEAVY METAL", "HGN-HM"));
        this.mechID.put(70, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HIGHLANDER, EVariantType.STANDARD, "HIGHLANDER HGN-732", "HGN-732"));
        this.mechID.put(71, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HIGHLANDER, EVariantType.STANDARD, "HIGHLANDER HGN-733", "HGN-733"));
        this.mechID.put(72, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HIGHLANDER, EVariantType.STANDARD, "HIGHLANDER HGN-733C", "HGN-733C"));
        this.mechID.put(73, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HIGHLANDER, EVariantType.STANDARD, "HIGHLANDER HGN-733P", "HGN-733P"));
        this.mechID.put(74, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DRAGON, EVariantType.CHAMPION, "DRAGON DRG-5N(C)", "DRG-5N(C)"));
        this.mechID.put(75, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.HERO, "MISERY", "STK-M"));
        this.mechID.put(76, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JENNER, EVariantType.CHAMPION, "JENNER JR7-F(C)", "JR7-F(C)"));
        this.mechID.put(77, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKJACK, EVariantType.STANDARD, "BLACKJACK BJ-1", "BJ-1"));
        this.mechID.put(78, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKJACK, EVariantType.STANDARD, "BLACKJACK BJ-1DC", "BJ-1DC"));
        this.mechID.put(79, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKJACK, EVariantType.STANDARD, "BLACKJACK BJ-1X", "BJ-1X"));
        this.mechID.put(80, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKJACK, EVariantType.STANDARD, "BLACKJACK BJ-3", "BJ-3"));
        this.mechID.put(81, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAGERMECH, EVariantType.HERO, "FIREBRAND", "JM6-FB"));
        this.mechID.put(82, new MechIdInfo(EFaction.INNERSPHERE, EChassie.QUICKDRAW, EVariantType.STANDARD, "QUICKDRAW QKD-4G", "QKD-4G"));
        this.mechID.put(83, new MechIdInfo(EFaction.INNERSPHERE, EChassie.QUICKDRAW, EVariantType.STANDARD, "QUICKDRAW QKD-4H", "QKD-4H"));
        this.mechID.put(84, new MechIdInfo(EFaction.INNERSPHERE, EChassie.QUICKDRAW, EVariantType.STANDARD, "QUICKDRAW QKD-5K", "QKD-5K"));
        this.mechID.put(85, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.STANDARD, "HUNCHBACK HBK-4P(C)", "HBK-4P(C)"));
        this.mechID.put(86, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JENNER, EVariantType.SARAH, "JENNER JR7-D(S)", "JR7-D(S)"));
        this.mechID.put(87, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VICTOR, EVariantType.HERO, "DRAGON SLAYER", "VTR-DS"));
        this.mechID.put(88, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VICTOR, EVariantType.STANDARD, "VICTOR VTR-9K", "VTR-9K"));
        this.mechID.put(89, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VICTOR, EVariantType.STANDARD, "VICTOR VTR-9B", "VTR-9B"));
        this.mechID.put(90, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VICTOR, EVariantType.STANDARD, "VICTOR VTR-9S", "VTR-9S"));
        this.mechID.put(91, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.CHAMPION, "CATAPULT CPLT-A1(C)", "CPLT-A1(C)"));
        this.mechID.put(92, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.CHAMPION, "ATLAS AS7-RS(C)", "AS7-RS(C)"));
        this.mechID.put(93, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINTARO, EVariantType.HERO, "GOLDEN BOY", "KTO-GB"));
        this.mechID.put(94, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINTARO, EVariantType.STANDARD, "KINTARO KTO-20", "KTO-20"));
        this.mechID.put(95, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINTARO, EVariantType.STANDARD, "KINTARO KTO-18", "KTO-18"));
        this.mechID.put(96, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINTARO, EVariantType.STANDARD, "KINTARO KTO-19", "KTO-19"));
        this.mechID.put(97, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ORION, EVariantType.HERO, "PROTECTOR", "ON1-P"));
        this.mechID.put(98, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ORION, EVariantType.STANDARD, "ORION ON1-M", "ON1-M"));
        this.mechID.put(99, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ORION, EVariantType.STANDARD, "ORION ON1-K", "ON1-K"));
        this.mechID.put(100, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ORION, EVariantType.STANDARD, "ORION ON1-V", "ON1-V"));
        this.mechID.put(101, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ORION, EVariantType.STANDARD, "ORION ON1-VA", "ON1-VA"));
        this.mechID.put(102, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CENTURION, EVariantType.CHAMPION, "CENTURION CN9-A(C)", "CN9-A(C)"));
        this.mechID.put(103, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.HERO, "THE BOAR'S HEAD", "AS7-BH"));
        this.mechID.put(104, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SPIDER, EVariantType.CHAMPION, "SPIDER SDR-5K(C)", "SDR-5K(C)"));
        this.mechID.put(105, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.PHOENIX, "LOCUST LCT-1V(P)", "LCT-1V(P)"));
        this.mechID.put(106, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.STANDARD, "LOCUST LCT-1V", "LCT-1V"));
        this.mechID.put(107, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.STANDARD, "LOCUST LCT-3M", "LCT-3M"));
        this.mechID.put(108, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.STANDARD, "LOCUST LCT-3S", "LCT-3S"));
        this.mechID.put(109, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SHADOWHAWK, EVariantType.PHOENIX, "SHADOW HAWK SHD-2H(P)", "SHD-2H(P)"));
        this.mechID.put(110, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SHADOWHAWK, EVariantType.STANDARD, "SHADOW HAWK SHD-2H", "SHD-2H"));
        this.mechID.put(111, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SHADOWHAWK, EVariantType.STANDARD, "SHADOW HAWK SHD-2D2", "SHD-2D2"));
        this.mechID.put(112, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SHADOWHAWK, EVariantType.STANDARD, "SHADOW HAWK SHD-5M", "SHD-5M"));
        this.mechID.put(113, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.PHOENIX, "THUNDERBOLT TDR-5S(P)", "TDR-5S(P)"));
        this.mechID.put(114, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.STANDARD, "THUNDERBOLT TDR-5S", "TDR-5S"));
        this.mechID.put(115, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.STANDARD, "THUNDERBOLT TDR-5SS", "TDR-5SS"));
        this.mechID.put(116, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.STANDARD, "THUNDERBOLT TDR-9SE", "TDR-9SE"));
        this.mechID.put(117, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.PHOENIX, "BATTLEMASTER BLR-1G(P)", "BLR-1G(P)"));
        this.mechID.put(118, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.STANDARD, "BATTLEMASTER BLR-1G", "BLR-1G"));
        this.mechID.put(119, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.STANDARD, "BATTLEMASTER BLR-1D", "BLR-1D"));
        this.mechID.put(120, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.STANDARD, "BATTLEMASTER BLR-1S", "BLR-1S"));
        this.mechID.put(121, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.HERO, "JESTER", "CPLT-J"));
        this.mechID.put(122, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKJACK, EVariantType.CHAMPION, "BLACKJACK BJ-1(C)", "BJ-1(C)"));
        this.mechID.put(123, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JENNER, EVariantType.HERO, "OXIDE", "JR7-O"));
        this.mechID.put(124, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HIGHLANDER, EVariantType.CHAMPION, "HIGHLANDER HGN-733C(C)", "HGN-733C(C)"));
        this.mechID.put(125, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.PHOENIX, "GRIFFIN GRF-1N(P)", "GRF-1N(P)"));
        this.mechID.put(126, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.STANDARD, "GRIFFIN GRF-1N", "GRF-1N"));
        this.mechID.put(127, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.STANDARD, "GRIFFIN GRF-1S", "GRF-1S"));
        this.mechID.put(128, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.STANDARD, "GRIFFIN GRF-3M", "GRF-3M"));
        this.mechID.put(129, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLVERINE, EVariantType.PHOENIX, "WOLVERINE WVR-6R(P)", "WVR-6R(P)"));
        this.mechID.put(130, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLVERINE, EVariantType.STANDARD, "WOLVERINE WVR-6R", "WVR-6R"));
        this.mechID.put(131, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLVERINE, EVariantType.STANDARD, "WOLVERINE WVR-6K", "WVR-6K"));
        this.mechID.put(132, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLVERINE, EVariantType.STANDARD, "WOLVERINE WVR-7K", "WVR-7K"));
        this.mechID.put(133, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.HERO, "GRID IRON", "HBK-GI"));
        this.mechID.put(134, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CICADA, EVariantType.CHAMPION, "CICADA CDA-2A(C)", "CDA-2A(C)"));
        this.mechID.put(135, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.HERO, "GRID IRON LTD", "HBK-GI"));
        this.mechID.put(136, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.CHAMPION, "STALKER STK-3F(C)", "STK-3F(C)"));
        this.mechID.put(137, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FIRESTARTER, EVariantType.HERO, "EMBER", "FS9-E"));
        this.mechID.put(138, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FIRESTARTER, EVariantType.STANDARD, "FIRESTARTER FS9-S", "FS9-S"));
        this.mechID.put(139, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FIRESTARTER, EVariantType.STANDARD, "FIRESTARTER FS9-A", "FS9-A"));
        this.mechID.put(140, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FIRESTARTER, EVariantType.STANDARD, "FIRESTARTER FS9-H", "FS9-H"));
        this.mechID.put(141, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FIRESTARTER, EVariantType.STANDARD, "FIRESTARTER FS9-K", "FS9-K"));
        this.mechID.put(142, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPHRACT, EVariantType.CHAMPION, "CATAPHRACT CTF-3D(C)", "CTF-3D(C)"));
        this.mechID.put(143, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BANSHEE, EVariantType.HERO, "LA MALINCHE", "BNC-LM"));
        this.mechID.put(144, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BANSHEE, EVariantType.STANDARD, "BANSHEE BNC-3E", "BNC-3E"));
        this.mechID.put(145, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BANSHEE, EVariantType.STANDARD, "BANSHEE BNC-3M", "BNC-3M"));
        this.mechID.put(146, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BANSHEE, EVariantType.STANDARD, "BANSHEE BNC-3S", "BNC-3S"));
        this.mechID.put(147, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VICTOR, EVariantType.CHAMPION, "VICTOR VTR-9S(C)", "VTR-9S(C)"));
        this.mechID.put(148, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RAVEN, EVariantType.HERO, "HUGINN", "RVN-H"));
        this.mechID.put(149, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.STANDARD, "BATTLEMASTER BLR-3M", "BLR-3M"));
        this.mechID.put(150, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.STANDARD, "BATTLEMASTER BLR-3S", "BLR-3S"));
        this.mechID.put(151, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.STANDARD, "LOCUST LCT-1E", "LCT-1E"));
        this.mechID.put(152, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.STANDARD, "LOCUST LCT-1M", "LCT-1M"));
        this.mechID.put(153, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SHADOWHAWK, EVariantType.STANDARD, "SHADOW HAWK SHD-2D", "SHD-2D"));
        this.mechID.put(154, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SHADOWHAWK, EVariantType.STANDARD, "SHADOW HAWK SHD-2K", "SHD-2K"));
        this.mechID.put(155, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.STANDARD, "THUNDERBOLT TDR-9S", "TDR-9S"));
        this.mechID.put(156, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ORION, EVariantType.CHAMPION, "ORION ON1-K(C)", "ON1-K(C)"));
        this.mechID.put(157, new MechIdInfo(EFaction.INNERSPHERE, EChassie.TREBUCHET, EVariantType.HERO, "LOUP DE GUERRE", "TBT-LG"));
        this.mechID.put(158, new MechIdInfo(EFaction.INNERSPHERE, EChassie.QUICKDRAW, EVariantType.HERO, "IV-FOUR", "QKD-IV4"));
        this.mechID.put(159, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKJACK, EVariantType.HERO, "THE ARROW", "BJ-A"));
        this.mechID.put(160, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FIRESTARTER, EVariantType.CHAMPION, "FIRESTARTER FS9-S(C)", "FS9-S(C)"));
        this.mechID.put(161, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SHADOWHAWK, EVariantType.CHAMPION, "SHADOW HAWK SHD-2H(C)", "SHD-2H(C)"));
        this.mechID.put(162, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.SPECIAL, "TIMBER WOLF TBR-PRIME(I)", "TBR-PRIME(I)"));
        this.mechID.put(163, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.STANDARD, "TIMBER WOLF TBR-PRIME", "TBR-PRIME"));
        this.mechID.put(164, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.STANDARD, "TIMBER WOLF TBR-C", "TBR-C"));
        this.mechID.put(165, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.STANDARD, "TIMBER WOLF TBR-S", "TBR-S"));
        this.mechID.put(166, new MechIdInfo(EFaction.CLAN, EChassie.ADDER, EVariantType.STANDARD, "ADDER ADR-PRIME(I)", "ADR-PRIME(I)"));
        this.mechID.put(167, new MechIdInfo(EFaction.CLAN, EChassie.ADDER, EVariantType.STANDARD, "ADDER ADR-PRIME", "ADR-PRIME"));
        this.mechID.put(168, new MechIdInfo(EFaction.CLAN, EChassie.ADDER, EVariantType.STANDARD, "ADDER ADR-A", "ADR-A"));
        this.mechID.put(169, new MechIdInfo(EFaction.CLAN, EChassie.ADDER, EVariantType.STANDARD, "ADDER ADR-D", "ADR-D"));
        this.mechID.put(170, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.SPECIAL, "DIRE WOLF DWF-PRIME(I)", "DWF-PRIME(I)"));
        this.mechID.put(171, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.STANDARD, "DIRE WOLF DWF-PRIME", "DWF-PRIME"));
        this.mechID.put(172, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.STANDARD, "DIRE WOLF DWF-A", "DWF-A"));
        this.mechID.put(173, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.STANDARD, "DIRE WOLF DWF-B", "DWF-B"));
        this.mechID.put(174, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.SPECIAL, "KIT FOX KFX-PRIME(I)", "KFX-PRIME(I)"));
        this.mechID.put(175, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.STANDARD, "KIT FOX KFX-PRIME", "KFX-PRIME"));
        this.mechID.put(176, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.STANDARD, "KIT FOX KFX-D", "KFX-D"));
        this.mechID.put(177, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.STANDARD, "KIT FOX KFX-S", "KFX-S"));
        this.mechID.put(178, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.SPECIAL, "NOVA NVA-PRIME(I)", "NVA-PRIME(I)"));
        this.mechID.put(179, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.STANDARD, "NOVA NVA-PRIME", "NVA-PRIME"));
        this.mechID.put(180, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.STANDARD, "NOVA NVA-B", "NVA-B"));
        this.mechID.put(181, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.STANDARD, "NOVA NVA-S", "NVA-S"));
        this.mechID.put(182, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.SPECIAL, "STORMCROW SCR-PRIME(I)", "SCR-PRIME(I)"));
        this.mechID.put(183, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.STANDARD, "STORMCROW SCR-PRIME", "SCR-PRIME"));
        this.mechID.put(184, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.STANDARD, "STORMCROW SCR-C", "SCR-C"));
        this.mechID.put(185, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.STANDARD, "STORMCROW SCR-D", "SCR-D"));
        this.mechID.put(186, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.SPECIAL, "SUMMONER SMN-PRIME(I)", "SMN-PRIME(I)"));
        this.mechID.put(187, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.STANDARD, "SUMMONER SMN-PRIME", "SMN-PRIME"));
        this.mechID.put(188, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.STANDARD, "SUMMONER SMN-B", "SMN-B"));
        this.mechID.put(189, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.STANDARD, "SUMMONER SMN-D", "SMN-D"));
        this.mechID.put(190, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.SPECIAL, "WARHAWK WHK-PRIME(I)", "WHK-PRIME(I)"));
        this.mechID.put(191, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.STANDARD, "WARHAWK WHK-PRIME", "WHK-PRIME"));
        this.mechID.put(192, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.STANDARD, "WARHAWK WHK-A", "WHK-A"));
        this.mechID.put(193, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.STANDARD, "WARHAWK WHK-B", "WHK-B"));
        this.mechID.put(194, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.SPECIAL, "TIMBER WOLF TBR-PRIME(G)", "TBR-PRIME(G)"));
        this.mechID.put(195, new MechIdInfo(EFaction.CLAN, EChassie.ADDER, EVariantType.SPECIAL, "ADDER ADR-PRIME(G)", "ADR-PRIME(G)"));
        this.mechID.put(196, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.SPECIAL, "DIRE WOLF DWF-PRIME(G)", "DWF-PRIME(G)"));
        this.mechID.put(197, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.SPECIAL, "KIT FOX KFX-PRIME(G)", "KFX-PRIME(G)"));
        this.mechID.put(198, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.SPECIAL, "NOVA NVA-PRIME(G)", "NVA-PRIME(G)"));
        this.mechID.put(199, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.SPECIAL, "STORMCROW SCR-PRIME(G)", "SCR-PRIME(G)"));
        this.mechID.put(200, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.SPECIAL, "SUMMONER SMN-PRIME(G)", "SMN-PRIME(G)"));
        this.mechID.put(201, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.SPECIAL, "WARHAWK WHK-PRIME(G)", "WHK-PRIME(G)"));
        this.mechID.put(202, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINTARO, EVariantType.CHAMPION, "KINTARO KTO-18(C)", "KTO-18(C)"));
        this.mechID.put(203, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SPIDER, EVariantType.HERO, "ANANSI", "SDR-A"));
        this.mechID.put(204, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.HERO, "HELLSLINGER", "BLR-1GHE"));
        this.mechID.put(205, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VINDICATOR, EVariantType.HERO, "ST. IVES' BLUES", "VND-1SIB"));
        this.mechID.put(206, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VINDICATOR, EVariantType.STANDARD, "VINDICATOR VND-1R", "VND-1R"));
        this.mechID.put(207, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VINDICATOR, EVariantType.STANDARD, "VINDICATOR VND-1AA", "VND-1AA"));
        this.mechID.put(208, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VINDICATOR, EVariantType.STANDARD, "VINDICATOR VND-1X", "VND-1X"));
        this.mechID.put(209, new MechIdInfo(EFaction.INNERSPHERE, EChassie.QUICKDRAW, EVariantType.CHAMPION, "QUICKDRAW QKD-4G(C)", "QKD-4G(C)"));
        this.mechID.put(210, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.HERO, "SPARKY", "GRF-1E"));
        this.mechID.put(211, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.SPECIAL, "MAD DOG MDD-PRIME(I)", "MDD-PRIME(I)"));
        this.mechID.put(212, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.STANDARD, "MAD DOG MDD-PRIME", "MDD-PRIME"));
        this.mechID.put(213, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.STANDARD, "MAD DOG MDD-A", "MDD-A"));
        this.mechID.put(214, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.STANDARD, "MAD DOG MDD-B", "MDD-B"));
        this.mechID.put(215, new MechIdInfo(EFaction.INNERSPHERE, EChassie.TREBUCHET, EVariantType.CHAMPION, "TREBUCHET TBT-7M(C)", "TBT-7M(C)"));
        this.mechID.put(216, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.SPECIAL, "ATLAS AS7-S(L)", "AS7-S(L)"));
        this.mechID.put(217, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.CHAMPION, "THUNDERBOLT TDR-9SE(C)", "TDR-9SE(C)"));
        this.mechID.put(218, new MechIdInfo(EFaction.CLAN, EChassie.ICEFERRET, EVariantType.SPECIAL, "ICE FERRET IFR-PRIME(I)", "IFR-PRIME(I)"));
        this.mechID.put(219, new MechIdInfo(EFaction.CLAN, EChassie.ICEFERRET, EVariantType.STANDARD, "ICE FERRET IFR-PRIME", "IFR-PRIME"));
        this.mechID.put(220, new MechIdInfo(EFaction.CLAN, EChassie.ICEFERRET, EVariantType.STANDARD, "ICE FERRET IFR-A", "IFR-A"));
        this.mechID.put(221, new MechIdInfo(EFaction.CLAN, EChassie.ICEFERRET, EVariantType.STANDARD, "ICE FERRET IFR-C", "IFR-C"));
        this.mechID.put(222, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.HERO, "PIRATES' BANE", "LCT-PB"));
        this.mechID.put(223, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SHADOWHAWK, EVariantType.HERO, "THE GRAY DEATH", "SHD-GD"));
        this.mechID.put(224, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.SPECIAL, "MIST LYNX MLX-PRIME(I)", "MLX-PRIME(I)"));
        this.mechID.put(225, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.STANDARD, "MIST LYNX MLX-PRIME", "MLX-PRIME"));
        this.mechID.put(226, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.STANDARD, "MIST LYNX MLX-B", "MLX-B"));
        this.mechID.put(227, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.STANDARD, "MIST LYNX MLX-C", "MLX-C"));
        this.mechID.put(228, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RAVEN, EVariantType.CHAMPION, "RAVEN RVN-3L(C)", "RVN-3L(C)"));
        this.mechID.put(229, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.SPECIAL, "HELLBRINGER HBR-PRIME(I)", "HBR-PRIME(I)"));
        this.mechID.put(230, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.STANDARD, "HELLBRINGER HBR-PRIME", "HBR-PRIME"));
        this.mechID.put(231, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.STANDARD, "HELLBRINGER HBR-A", "HBR-A"));
        this.mechID.put(232, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.STANDARD, "HELLBRINGER HBR-B", "HBR-B"));
        this.mechID.put(233, new MechIdInfo(EFaction.CLAN, EChassie.GARGOYLE, EVariantType.SPECIAL, "GARGOYLE GAR-PRIME(I)", "GAR-PRIME(I)"));
        this.mechID.put(234, new MechIdInfo(EFaction.CLAN, EChassie.GARGOYLE, EVariantType.STANDARD, "GARGOYLE GAR-PRIME", "GAR-PRIME"));
        this.mechID.put(235, new MechIdInfo(EFaction.CLAN, EChassie.GARGOYLE, EVariantType.STANDARD, "GARGOYLE GAR-A", "GAR-A"));
        this.mechID.put(236, new MechIdInfo(EFaction.CLAN, EChassie.GARGOYLE, EVariantType.STANDARD, "GARGOYLE GAR-D", "GAR-D"));
        this.mechID.put(237, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.SPECIAL, "KING CRAB KGC-000(L)", "KGC-000(L)"));
        this.mechID.put(238, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.STANDARD, "KING CRAB KGC-000", "KGC-000"));
        this.mechID.put(239, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.STANDARD, "KING CRAB KGC-0000", "KGC-0000"));
        this.mechID.put(240, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.STANDARD, "KING CRAB KGC-000B", "KGC-000B"));
        this.mechID.put(241, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.STANDARD, "ATLAS AS7-S", "AS7-S"));
        this.mechID.put(242, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CENTURION, EVariantType.STANDARD, "CENTURION CN9-AH", "CN9-AH"));
        this.mechID.put(243, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PANTHER, EVariantType.SPECIAL, "PANTHER PNT-10K(R)", "PNT-10K(R)"));
        this.mechID.put(244, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PANTHER, EVariantType.STANDARD, "PANTHER PNT-10K", "PNT-10K"));
        this.mechID.put(245, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PANTHER, EVariantType.STANDARD, "PANTHER PNT-8Z", "PNT-8Z"));
        this.mechID.put(246, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PANTHER, EVariantType.STANDARD, "PANTHER PNT-9R", "PNT-9R"));
        this.mechID.put(247, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ENFORCER, EVariantType.SPECIAL, "ENFORCER ENF-5D(R)", "ENF-5D(R)"));
        this.mechID.put(248, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ENFORCER, EVariantType.STANDARD, "ENFORCER ENF-5D", "ENF-5D"));
        this.mechID.put(249, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ENFORCER, EVariantType.STANDARD, "ENFORCER ENF-4R", "ENF-4R"));
        this.mechID.put(250, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ENFORCER, EVariantType.STANDARD, "ENFORCER ENF-5P", "ENF-5P"));
        this.mechID.put(251, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BANSHEE, EVariantType.CHAMPION, "BANSHEE BNC-3M(C)", "BNC-3M(C)"));
        this.mechID.put(252, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRASSHOPPER, EVariantType.SPECIAL, "GRASSHOPPER GHR-5J(R)", "GHR-5J(R)"));
        this.mechID.put(253, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRASSHOPPER, EVariantType.STANDARD, "GRASSHOPPER GHR-5J", "GHR-5J"));
        this.mechID.put(254, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRASSHOPPER, EVariantType.STANDARD, "GRASSHOPPER GHR-5H", "GHR-5H"));
        this.mechID.put(255, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRASSHOPPER, EVariantType.STANDARD, "GRASSHOPPER GHR-5N", "GHR-5N"));
        this.mechID.put(256, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.SPECIAL, "ZEUS ZEU-6S(R)", "ZEU-6S(R)"));
        this.mechID.put(257, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.STANDARD, "ZEUS ZEU-6S", "ZEU-6S"));
        this.mechID.put(258, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.STANDARD, "ZEUS ZEU-6T", "ZEU-6T"));
        this.mechID.put(259, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.STANDARD, "ZEUS ZEU-9S", "ZEU-9S"));
        this.mechID.put(260, new MechIdInfo(EFaction.CLAN, EChassie.ADDER, EVariantType.STANDARD, "ADDER ADR-B", "ADR-B"));
        this.mechID.put(261, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.STANDARD, "DIRE WOLF DWF-S", "DWF-S"));
        this.mechID.put(262, new MechIdInfo(EFaction.CLAN, EChassie.GARGOYLE, EVariantType.STANDARD, "GARGOYLE GAR-C", "GAR-C"));
        this.mechID.put(263, new MechIdInfo(EFaction.CLAN, EChassie.ICEFERRET, EVariantType.STANDARD, "ICE FERRET IFR-D", "IFR-D"));
        this.mechID.put(264, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.STANDARD, "KIT FOX KFX-C", "KFX-C"));
        this.mechID.put(265, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.STANDARD, "MAD DOG MDD-C", "MDD-C"));
        this.mechID.put(266, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.STANDARD, "MIST LYNX MLX-A", "MLX-A"));
        this.mechID.put(267, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.STANDARD, "NOVA NVA-A", "NVA-A"));
        this.mechID.put(268, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.STANDARD, "STORMCROW SCR-A", "SCR-A"));
        this.mechID.put(269, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.STANDARD, "SUMMONER SMN-C", "SMN-C"));
        this.mechID.put(270, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.STANDARD, "TIMBER WOLF TBR-D", "TBR-D"));
        this.mechID.put(271, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.STANDARD, "WARHAWK WHK-C", "WHK-C"));
        this.mechID.put(272, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.SPECIAL, "GRIFFIN GRF-1S(C)", "GRF-1S(C)"));
        this.mechID.put(273, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLVERINE, EVariantType.SPECIAL, "WOLVERINE WVR-6K(C)", "WVR-6K(C)"));
        this.mechID.put(274, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.STANDARD, "URBANMECH UM-R63(S)", "UM-R63(S)"));
        this.mechID.put(275, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.STANDARD, "URBANMECH UM-R63", "UM-R63"));
        this.mechID.put(276, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.STANDARD, "URBANMECH UM-R60", "UM-R60"));
        this.mechID.put(277, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.STANDARD, "URBANMECH UM-R60L", "UM-R60L"));
        this.mechID.put(278, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAGERMECH, EVariantType.CHAMPION, "JAGERMECH JM6-A(C)", "JM6-A(C)"));
        this.mechID.put(279, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.STANDARD, "EBON JAGUAR EBJ-PRIME(I)", "EBJ-PRIME(I)"));
        this.mechID.put(280, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.STANDARD, "EBON JAGUAR EBJ-PRIME", "EBJ-PRIME"));
        this.mechID.put(281, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.STANDARD, "EBON JAGUAR EBJ-A", "EBJ-A"));
        this.mechID.put(282, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.STANDARD, "EBON JAGUAR EBJ-B", "EBJ-B"));
        this.mechID.put(283, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.STANDARD, "EBON JAGUAR EBJ-C", "EBJ-C"));
        this.mechID.put(284, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.SPECIAL, "EXECUTIONER EXE-PRIME(I)", "EXE-PRIME(I)"));
        this.mechID.put(285, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.STANDARD, "EXECUTIONER EXE-PRIME", "EXE-PRIME"));
        this.mechID.put(286, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.STANDARD, "EXECUTIONER EXE-A", "EXE-A"));
        this.mechID.put(287, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.STANDARD, "EXECUTIONER EXE-B", "EXE-B"));
        this.mechID.put(288, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.STANDARD, "EXECUTIONER EXE-D", "EXE-D"));
        this.mechID.put(289, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.HERO, "TOP DOG", "TDR-5S-T"));
        this.mechID.put(290, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.SPECIAL, "ARCTIC CHEETAH ACH-PRIME(I)", "ACH-PRIME(I)"));
        this.mechID.put(291, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.STANDARD, "ARCTIC CHEETAH ACH-PRIME", "ACH-PRIME"));
        this.mechID.put(292, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.STANDARD, "ARCTIC CHEETAH ACH-A", "ACH-A"));
        this.mechID.put(293, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.STANDARD, "ARCTIC CHEETAH ACH-B", "ACH-B"));
        this.mechID.put(294, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.STANDARD, "ARCTIC CHEETAH ACH-C", "ACH-C"));
        this.mechID.put(295, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.SPECIAL, "SHADOW CAT SHC-PRIME(I)", "SHC-PRIME(I)"));
        this.mechID.put(296, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.STANDARD, "SHADOW CAT SHC-PRIME", "SHC-PRIME"));
        this.mechID.put(297, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.STANDARD, "SHADOW CAT SHC-A", "SHC-A"));
        this.mechID.put(298, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.STANDARD, "SHADOW CAT SHC-B", "SHC-B"));
        this.mechID.put(299, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.STANDARD, "SHADOW CAT SHC-P", "SHC-P"));
        this.mechID.put(300, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.STANDARD, "GRIFFIN GRF-2N", "GRF-2N"));
        this.mechID.put(301, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HIGHLANDER, EVariantType.STANDARD, "HIGHLANDER HGN-732B", "HGN-732B"));
        this.mechID.put(302, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.STANDARD, "LOCUST LCT-3V", "LCT-3V"));
        this.mechID.put(303, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.STANDARD, "DIRE WOLF DWF-W", "DWF-W"));
        this.mechID.put(304, new MechIdInfo(EFaction.CLAN, EChassie.GARGOYLE, EVariantType.STANDARD, "GARGOYLE GAR-B", "GAR-B"));
        this.mechID.put(305, new MechIdInfo(EFaction.CLAN, EChassie.ICEFERRET, EVariantType.STANDARD, "ICE FERRET IFR-B", "IFR-B"));
        this.mechID.put(306, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.STANDARD, "MIST LYNX MLX-D", "MLX-D"));
        this.mechID.put(307, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.STANDARD, "NOVA NVA-C", "NVA-C"));
        this.mechID.put(308, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.STANDARD, "STORMCROW SCR-B", "SCR-B"));
        this.mechID.put(309, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.STANDARD, "TIMBER WOLF TBR-A", "TBR-A"));
        this.mechID.put(310, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.STANDARD, "BATTLEMASTER BLR-2C", "BLR-2C"));
        this.mechID.put(311, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPHRACT, EVariantType.STANDARD, "CATAPHRACT CTF-0XP", "CTF-0XP"));
        this.mechID.put(312, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PANTHER, EVariantType.STANDARD, "PANTHER PNT-10P", "PNT-10P"));
        this.mechID.put(313, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ENFORCER, EVariantType.STANDARD, "ENFORCER ENF-4P", "ENF-4P"));
        this.mechID.put(314, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRASSHOPPER, EVariantType.STANDARD, "GRASSHOPPER GHR-5P", "GHR-5P"));
        this.mechID.put(315, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.STANDARD, "ZEUS ZEU-5S", "ZEU-5S"));
        this.mechID.put(316, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLVERINE, EVariantType.HERO, "QUARANTINE", "WVR-Q"));
        this.mechID.put(317, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ENFORCER, EVariantType.CHAMPION, "ENFORCER ENF-4R(C)", "ENF-4R(C)"));
        this.mechID.put(318, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.CHAMPION, "TIMBER WOLF TBR-C(C)", "TBR-C(C)"));
        this.mechID.put(319, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKKNIGHT, EVariantType.SPECIAL, "BLACK KNIGHT BL-6-KNT(R)", "BL-6-KNT(R)"));
        this.mechID.put(320, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKKNIGHT, EVariantType.STANDARD, "BLACK KNIGHT BL-6-KNT", "BL-6-KNT"));
        this.mechID.put(321, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKKNIGHT, EVariantType.STANDARD, "BLACK KNIGHT BL-6B-KNT", "BL-6B-KNT"));
        this.mechID.put(322, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKKNIGHT, EVariantType.STANDARD, "BLACK KNIGHT BL-7-KNT", "BL-7-KNT"));
        this.mechID.put(323, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKKNIGHT, EVariantType.STANDARD, "BLACK KNIGHT BL-7-KNT-L", "BL-7-KNT-L"));
        this.mechID.put(324, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MAULER, EVariantType.SPECIAL, "MAULER MAL-1R(R)", "MAL-1R(R)"));
        this.mechID.put(325, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MAULER, EVariantType.STANDARD, "MAULER MAL-1R", "MAL-1R"));
        this.mechID.put(326, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MAULER, EVariantType.STANDARD, "MAULER MAL-MX90", "MAL-MX90"));
        this.mechID.put(327, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MAULER, EVariantType.STANDARD, "MAULER MAL-1P", "MAL-1P"));
        this.mechID.put(328, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MAULER, EVariantType.STANDARD, "MAULER MAL-2P", "MAL-2P"));
        this.mechID.put(329, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRAB, EVariantType.SPECIAL, "CRAB CRB-27(R)", "CRB-27(R)"));
        this.mechID.put(330, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRAB, EVariantType.STANDARD, "CRAB CRB-27", "CRB-27"));
        this.mechID.put(331, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRAB, EVariantType.STANDARD, "CRAB CRB-20", "CRB-20"));
        this.mechID.put(332, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRAB, EVariantType.STANDARD, "CRAB CRB-27B", "CRB-27B"));
        this.mechID.put(333, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRAB, EVariantType.STANDARD, "CRAB CRB-27SL", "CRB-27SL"));
        this.mechID.put(334, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLFHOUND, EVariantType.SPECIAL, "WOLFHOUND WLF-2(R)", "WLF-2(R)"));
        this.mechID.put(335, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLFHOUND, EVariantType.STANDARD, "WOLFHOUND WLF-2", "WLF-2"));
        this.mechID.put(336, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLFHOUND, EVariantType.STANDARD, "WOLFHOUND WLF-1", "WLF-1"));
        this.mechID.put(337, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLFHOUND, EVariantType.STANDARD, "WOLFHOUND WLF-1A", "WLF-1A"));
        this.mechID.put(338, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLFHOUND, EVariantType.STANDARD, "WOLFHOUND WLF-1B", "WLF-1B"));
        this.mechID.put(339, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CICADA, EVariantType.SPECIAL, "CICADA CDA-3F(L)", "CDA-3F(L)"));
        this.mechID.put(340, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLVERINE, EVariantType.SPECIAL, "WOLVERINE WVR-7D(L)", "WVR-7D(L)"));
        this.mechID.put(341, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.SPECIAL, "ZEUS ZEU-9S2(L)", "ZEU-9S2(L)"));
        this.mechID.put(342, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.SPECIAL, "NOVA NVA-D(L)", "NVA-D(L)"));
        this.mechID.put(343, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.SPECIAL, "EXECUTIONER EXE-C(L)", "EXE-C(L)"));
        this.mechID.put(344, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDER, EVariantType.STANDARD, "MARAUDER MAD-3R(S)", "MAD-3R(S)"));
        this.mechID.put(345, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDER, EVariantType.STANDARD, "MARAUDER MAD-3R", "MAD-3R"));
        this.mechID.put(346, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDER, EVariantType.STANDARD, "MARAUDER MAD-5D", "MAD-5D"));
        this.mechID.put(347, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDER, EVariantType.STANDARD, "MARAUDER MAD-5M", "MAD-5M"));
        this.mechID.put(348, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDER, EVariantType.HERO, "BOUNTY HUNTER II", "MAD-BH2"));
        this.mechID.put(349, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.CHAMPION, "KING CRAB KGC-000B(C)", "KGC-000B(C)"));
        this.mechID.put(350, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.SPECIAL, "KING CRAB KGC-000B(S)", "KGC-000B(S)"));
        this.mechID.put(351, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.CHAMPION, "ARCTIC CHEETAH ACH-PRIME(C)", "ACH-PRIME(C)"));
        this.mechID.put(352, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.SPECIAL, "ARCTIC CHEETAH ACH-PRIME(S)", "ACH-PRIME(S)"));
        this.mechID.put(353, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.CHAMPION, "STORMCROW SCR-PRIME(C)", "SCR-PRIME(C)"));
        this.mechID.put(354, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.SPECIAL, "STORMCROW SCR-PRIME(S)", "SCR-PRIME(S)"));
        this.mechID.put(355, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.CHAMPION, "DIRE WOLF DWF-W(C)", "DWF-W(C)"));
        this.mechID.put(356, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.SPECIAL, "DIRE WOLF DWF-W(S)", "DWF-W(S)"));
        this.mechID.put(357, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RAVEN, EVariantType.SPECIAL, "RAVEN RVN-3L(S)", "RVN-3L(S)"));
        this.mechID.put(358, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.SPECIAL, "HUNCHBACK HBK-4P(S)", "HBK-4P(S)"));
        this.mechID.put(359, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.SPECIAL, "THUNDERBOLT TDR-9SE(S)", "TDR-9SE(S)"));
        this.mechID.put(360, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.SPECIAL, "TIMBER WOLF TBR-C(S)", "TBR-C(S)"));
        this.mechID.put(361, new MechIdInfo(EFaction.CLAN, EChassie.JENNERIIC, EVariantType.SPECIAL, "JENNER IIC JR7-IIC(O)", "JR7-IIC(O)"));
        this.mechID.put(362, new MechIdInfo(EFaction.CLAN, EChassie.JENNERIIC, EVariantType.STANDARD, "JENNER IIC JR7-IIC", "JR7-IIC"));
        this.mechID.put(363, new MechIdInfo(EFaction.CLAN, EChassie.JENNERIIC, EVariantType.STANDARD, "JENNER IIC JR7-IIC-2", "JR7-IIC-2"));
        this.mechID.put(364, new MechIdInfo(EFaction.CLAN, EChassie.JENNERIIC, EVariantType.STANDARD, "JENNER IIC JR7-IIC-3", "JR7-IIC-3"));
        this.mechID.put(365, new MechIdInfo(EFaction.CLAN, EChassie.JENNERIIC, EVariantType.STANDARD, "JENNER IIC JR7-IIC-A", "JR7-IIC-A"));
        this.mechID.put(366, new MechIdInfo(EFaction.CLAN, EChassie.HUNCHBACKIIC, EVariantType.SPECIAL, "HUNCHBACK IIC HBK-IIC(O)", "HBK-IIC(O)"));
        this.mechID.put(367, new MechIdInfo(EFaction.CLAN, EChassie.HUNCHBACKIIC, EVariantType.STANDARD, "HUNCHBACK IIC HBK-IIC", "HBK-IIC"));
        this.mechID.put(368, new MechIdInfo(EFaction.CLAN, EChassie.HUNCHBACKIIC, EVariantType.STANDARD, "HUNCHBACK IIC HBK-IIC-A", "HBK-IIC-A"));
        this.mechID.put(369, new MechIdInfo(EFaction.CLAN, EChassie.HUNCHBACKIIC, EVariantType.STANDARD, "HUNCHBACK IIC HBK-IIC-B", "HBK-IIC-B"));
        this.mechID.put(370, new MechIdInfo(EFaction.CLAN, EChassie.HUNCHBACKIIC, EVariantType.STANDARD, "HUNCHBACK IIC HBK-IIC-C", "HBK-IIC-C"));
        this.mechID.put(371, new MechIdInfo(EFaction.CLAN, EChassie.ORIONIIC, EVariantType.SPECIAL, "ORION IIC ON1-IIC(O)", "ON1-IIC(O)"));
        this.mechID.put(372, new MechIdInfo(EFaction.CLAN, EChassie.ORIONIIC, EVariantType.STANDARD, "ORION IIC ON1-IIC", "ON1-IIC"));
        this.mechID.put(373, new MechIdInfo(EFaction.CLAN, EChassie.ORIONIIC, EVariantType.STANDARD, "ORION IIC ON1-IIC-A", "ON1-IIC-A"));
        this.mechID.put(374, new MechIdInfo(EFaction.CLAN, EChassie.ORIONIIC, EVariantType.STANDARD, "ORION IIC ON1-IIC-B", "ON1-IIC-B"));
        this.mechID.put(375, new MechIdInfo(EFaction.CLAN, EChassie.ORIONIIC, EVariantType.STANDARD, "ORION IIC ON1-IIC-C", "ON1-IIC-C"));
        this.mechID.put(376, new MechIdInfo(EFaction.CLAN, EChassie.HIGHLANDERIIC, EVariantType.SPECIAL, "HIGHLANDER IIC HGN-IIC(O)", "HGN-IIC(O)"));
        this.mechID.put(377, new MechIdInfo(EFaction.CLAN, EChassie.HIGHLANDERIIC, EVariantType.STANDARD, "HIGHLANDER IIC HGN-IIC", "HGN-IIC"));
        this.mechID.put(378, new MechIdInfo(EFaction.CLAN, EChassie.HIGHLANDERIIC, EVariantType.STANDARD, "HIGHLANDER IIC HGN-IIC-A", "HGN-IIC-A"));
        this.mechID.put(379, new MechIdInfo(EFaction.CLAN, EChassie.HIGHLANDERIIC, EVariantType.STANDARD, "HIGHLANDER IIC HGN-IIC-B", "HGN-IIC-B"));
        this.mechID.put(380, new MechIdInfo(EFaction.CLAN, EChassie.HIGHLANDERIIC, EVariantType.STANDARD, "HIGHLANDER IIC HGN-IIC-C", "HGN-IIC-C"));
        this.mechID.put(381, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.SPECIAL, "WARHAMMER WHM-6R(S)", "WHM-6R(S)"));
        this.mechID.put(382, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.STANDARD, "WARHAMMER WHM-6R", "WHM-6R"));
        this.mechID.put(383, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.STANDARD, "WARHAMMER WHM-6D", "WHM-6D"));
        this.mechID.put(384, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.STANDARD, "WARHAMMER WHM-7S", "WHM-7S"));
        this.mechID.put(385, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.HERO, "BLACK WIDOW", "WHM-BW"));
        this.mechID.put(386, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RIFLEMAN, EVariantType.SPECIAL, "RIFLEMAN RFL-3N(S)", "RFL-3N(S)"));
        this.mechID.put(387, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RIFLEMAN, EVariantType.STANDARD, "RIFLEMAN RFL-3N", "RFL-3N"));
        this.mechID.put(388, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RIFLEMAN, EVariantType.STANDARD, "RIFLEMAN RFL-3C", "RFL-3C"));
        this.mechID.put(389, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RIFLEMAN, EVariantType.STANDARD, "RIFLEMAN RFL-5D", "RFL-5D"));
        this.mechID.put(390, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RIFLEMAN, EVariantType.SPECIAL, "LEGEND-KILLER", "RFL-LK"));
        this.mechID.put(391, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ARCHER, EVariantType.HERO, "ARCHER ARC-2R(S)", "ARC-2R(S)"));
        this.mechID.put(392, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ARCHER, EVariantType.STANDARD, "ARCHER ARC-2R", "ARC-2R"));
        this.mechID.put(393, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ARCHER, EVariantType.STANDARD, "ARCHER ARC-5S", "ARC-5S"));
        this.mechID.put(394, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ARCHER, EVariantType.STANDARD, "ARCHER ARC-5W", "ARC-5W"));
        this.mechID.put(395, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ARCHER, EVariantType.HERO, "TEMPEST", "ARC-T"));
        this.mechID.put(396, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.CHAMPION, "LOCUST LCT-1E(C)", "LCT-1E(C)"));
        this.mechID.put(397, new MechIdInfo(EFaction.INNERSPHERE, EChassie.COMMANDO, EVariantType.CHAMPION, "COMMANDO COM-1D(C)", "COM-1D(C)"));
        this.mechID.put(398, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VINDICATOR, EVariantType.CHAMPION, "VINDICATOR VND-1AA(C)", "VND-1AA(C)"));
        this.mechID.put(399, new MechIdInfo(EFaction.INNERSPHERE, EChassie.AWESOME, EVariantType.CHAMPION, "AWESOME AWS-9M(C)", "AWS-9M(C)"));
        this.mechID.put(400, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.CHAMPION, "BATTLEMASTER BLR-2C(C)", "BLR-2C(C)"));
        this.mechID.put(401, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.CHAMPION, "MIST LYNX MLX-PRIME(C)", "MLX-PRIME(C)"));
        this.mechID.put(402, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.CHAMPION, "SHADOW CAT SHC-PRIME(C)", "SHC-PRIME(C)"));
        this.mechID.put(403, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.CHAMPION, "EBON JAGUAR EBJ-PRIME(C)", "EBJ-PRIME(C)"));
        this.mechID.put(404, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.CHAMPION, "WARHAWK WHK-C(C)", "WHK-C(C)"));
        this.mechID.put(405, new MechIdInfo(EFaction.CLAN, EChassie.KODIAK, EVariantType.SPECIAL, "KODIAK KDK-1(S)", "KDK-1(S)"));
        this.mechID.put(406, new MechIdInfo(EFaction.CLAN, EChassie.KODIAK, EVariantType.STANDARD, "KODIAK KDK-1", "KDK-1"));
        this.mechID.put(407, new MechIdInfo(EFaction.CLAN, EChassie.KODIAK, EVariantType.STANDARD, "KODIAK KDK-2", "KDK-2"));
        this.mechID.put(408, new MechIdInfo(EFaction.CLAN, EChassie.KODIAK, EVariantType.STANDARD, "KODIAK KDK-3", "KDK-3"));
        this.mechID.put(409, new MechIdInfo(EFaction.CLAN, EChassie.KODIAK, EVariantType.STANDARD, "KODIAK KDK-4", "KDK-4"));
        this.mechID.put(410, new MechIdInfo(EFaction.CLAN, EChassie.KODIAK, EVariantType.STANDARD, "KODIAK KDK-5", "KDK-5"));
        this.mechID.put(411, new MechIdInfo(EFaction.CLAN, EChassie.KODIAK, EVariantType.HERO, "SPIRIT BEAR", "KDK-SB"));
        this.mechID.put(412, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.SPECIAL, "PHOENIX HAWK PXH-1(S)", "PXH-1(S)"));
        this.mechID.put(413, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.STANDARD, "PHOENIX HAWK PXH-1", "PXH-1"));
        this.mechID.put(414, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.STANDARD, "PHOENIX HAWK PXH-1B", "PXH-1B"));
        this.mechID.put(415, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.STANDARD, "PHOENIX HAWK PXH-1K", "PXH-1K"));
        this.mechID.put(416, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.STANDARD, "PHOENIX HAWK PXH-2", "PXH-2"));
        this.mechID.put(417, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.STANDARD, "PHOENIX HAWK PXH-3S", "PXH-3S"));
        this.mechID.put(418, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.HERO, "ROC", "PXH-R"));
        this.mechID.put(419, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.HERO, "KUROI KIRI", "PXH-KK"));
        this.mechID.put(420, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.HERO, "BUTTERBEE", "CPLT-BB"));
        this.mechID.put(421, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.SPECIAL, "VIPER VPR-PRIME(S)", "VPR-PRIME(S)"));
        this.mechID.put(422, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.STANDARD, "VIPER VPR-PRIME", "VPR-PRIME"));
        this.mechID.put(423, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.STANDARD, "VIPER VPR-A", "VPR-A"));
        this.mechID.put(424, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.STANDARD, "VIPER VPR-B", "VPR-B"));
        this.mechID.put(425, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.STANDARD, "VIPER VPR-C", "VPR-C"));
        this.mechID.put(426, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.STANDARD, "VIPER VPR-D", "VPR-D"));
        this.mechID.put(427, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.HERO, "MEDUSA", "VPR-M"));
        this.mechID.put(428, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CICADA, EVariantType.STANDARD, "CICADA CDA-3F", "CDA-3F"));
        this.mechID.put(429, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLVERINE, EVariantType.STANDARD, "WOLVERINE WVR-7D", "WVR-7D"));
        this.mechID.put(430, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.STANDARD, "ZEUS ZEU-9S2", "ZEU-9S2"));
        this.mechID.put(431, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.STANDARD, "NOVA NVA-D", "NVA-D"));
        this.mechID.put(432, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.STANDARD, "EXECUTIONER EXE-C", "EXE-C"));
        this.mechID.put(433, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CYCLOPS, EVariantType.HERO, "CYCLOPS CP-11-A(S)", "CP-11-A(S)"));
        this.mechID.put(434, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CYCLOPS, EVariantType.STANDARD, "CYCLOPS CP-11-A", "CP-11-A"));
        this.mechID.put(435, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CYCLOPS, EVariantType.STANDARD, "CYCLOPS CP-10-Q", "CP-10-Q"));
        this.mechID.put(436, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CYCLOPS, EVariantType.STANDARD, "CYCLOPS CP-10-Z", "CP-10-Z"));
        this.mechID.put(437, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CYCLOPS, EVariantType.STANDARD, "CYCLOPS CP-11-A-DC", "CP-11-A-DC"));
        this.mechID.put(438, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CYCLOPS, EVariantType.STANDARD, "CYCLOPS CP-11-P", "CP-11-P"));
        this.mechID.put(439, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CYCLOPS, EVariantType.HERO, "SLEIPNIR", "CP-S"));
        this.mechID.put(440, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CENTURION, EVariantType.SPECIAL, "CENTURION CN9-A(NCIX)", "CN9-A(NCIX)"));
        this.mechID.put(441, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.SPECIAL, "NIGHT GYR NTG-PRIME(S)", "NTG-PRIME(S)"));
        this.mechID.put(442, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.STANDARD, "NIGHT GYR NTG-PRIME", "NTG-PRIME"));
        this.mechID.put(443, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.STANDARD, "NIGHT GYR NTG-A", "NTG-A"));
        this.mechID.put(444, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.STANDARD, "NIGHT GYR NTG-B", "NTG-B"));
        this.mechID.put(445, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.STANDARD, "NIGHT GYR NTG-C", "NTG-C"));
        this.mechID.put(446, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.STANDARD, "NIGHT GYR NTG-D", "NTG-D"));
        this.mechID.put(447, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.HERO, "JADE KITE", "NTG-JK"));
        this.mechID.put(448, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PANTHER, EVariantType.CHAMPION, "PANTHER PNT-10K(C)", "PNT-10K(C)"));
        this.mechID.put(449, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRAB, EVariantType.CHAMPION, "CRAB CRB-27B(C)", "CRB-27B(C)"));
        this.mechID.put(450, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRASSHOPPER, EVariantType.CHAMPION, "GRASSHOPPER GHR-5H(C)", "GHR-5H(C)"));
        this.mechID.put(451, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.CHAMPION, "ZEUS ZEU-6T(C)", "ZEU-6T(C)"));
        this.mechID.put(452, new MechIdInfo(EFaction.CLAN, EChassie.JENNERIIC, EVariantType.CHAMPION, "JENNER IIC JR7-IIC(C)", "JR7-IIC(C)"));
        this.mechID.put(453, new MechIdInfo(EFaction.CLAN, EChassie.HUNCHBACKIIC, EVariantType.CHAMPION, "HUNCHBACK IIC HBK-IIC(C)", "HBK-IIC(C)"));
        this.mechID.put(454, new MechIdInfo(EFaction.CLAN, EChassie.ORIONIIC, EVariantType.CHAMPION, "ORION IIC ON1-IIC-A(C)", "ON1-IIC-A(C)"));
        this.mechID.put(455, new MechIdInfo(EFaction.CLAN, EChassie.HIGHLANDERIIC, EVariantType.CHAMPION, "HIGHLANDER IIC HGN-IIC-C(C)", "HGN-IIC-C(C)"));
        this.mechID.put(456, new MechIdInfo(EFaction.CLAN, EChassie.HUNTSMAN, EVariantType.SPECIAL, "HUNTSMAN HMN-PRIME(S)", "HMN-PRIME(S)"));
        this.mechID.put(457, new MechIdInfo(EFaction.CLAN, EChassie.HUNTSMAN, EVariantType.STANDARD, "HUNTSMAN HMN-PRIME", "HMN-PRIME"));
        this.mechID.put(458, new MechIdInfo(EFaction.CLAN, EChassie.HUNTSMAN, EVariantType.STANDARD, "HUNTSMAN HMN-A", "HMN-A"));
        this.mechID.put(459, new MechIdInfo(EFaction.CLAN, EChassie.HUNTSMAN, EVariantType.STANDARD, "HUNTSMAN HMN-B", "HMN-B"));
        this.mechID.put(460, new MechIdInfo(EFaction.CLAN, EChassie.HUNTSMAN, EVariantType.STANDARD, "HUNTSMAN HMN-C", "HMN-C"));
        this.mechID.put(461, new MechIdInfo(EFaction.CLAN, EChassie.HUNTSMAN, EVariantType.STANDARD, "HUNTSMAN HMN-P", "HMN-P"));
        this.mechID.put(462, new MechIdInfo(EFaction.CLAN, EChassie.HUNTSMAN, EVariantType.HERO, "PAKHET", "HMN-PA"));
        this.mechID.put(463, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKJACK, EVariantType.SPECIAL, "BLACKJACK BJ-2(L)", "BJ-2(L)"));
        this.mechID.put(464, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPHRACT, EVariantType.SPECIAL, "CATAPHRACT CTF-3L(L)", "CTF-3L(L)"));
        this.mechID.put(465, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.SPECIAL, "STALKER STK-3FB(L)", "STK-3FB(L)"));
        this.mechID.put(466, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.SPECIAL, "HELLBRINGER HBR-F(L)", "HBR-F(L)"));
        this.mechID.put(467, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.SPECIAL, "SUMMONER SMN-F(L)", "SMN-F(L)"));
        this.mechID.put(468, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.SPECIAL, "SUMMONER SMN-M(L)", "SMN-M(L)"));
        this.mechID.put(469, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.SPECIAL, "LINEBACKER LBK-PRIME(S)", "LBK-PRIME(S)"));
        this.mechID.put(470, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.STANDARD, "LINEBACKER LBK-PRIME", "LBK-PRIME"));
        this.mechID.put(471, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.STANDARD, "LINEBACKER LBK-A", "LBK-A"));
        this.mechID.put(472, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.STANDARD, "LINEBACKER LBK-B", "LBK-B"));
        this.mechID.put(473, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.STANDARD, "LINEBACKER LBK-C", "LBK-C"));
        this.mechID.put(474, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.STANDARD, "LINEBACKER LBK-D", "LBK-D"));
        this.mechID.put(475, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.HERO, "REDLINE", "LBK-RL"));
        this.mechID.put(476, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.HERO, "PURIFIER", "KFX-PR"));
        this.mechID.put(477, new MechIdInfo(EFaction.CLAN, EChassie.ADDER, EVariantType.HERO, "CINDER", "ADR-CN"));
        this.mechID.put(478, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.HERO, "BREAKER", "NVA-BK"));
        this.mechID.put(479, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.HERO, "LACERATOR", "SCR-LC"));
        this.mechID.put(480, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.HERO, "PRIDE", "SMN-PD"));
        this.mechID.put(481, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.HERO, "WARRANT", "TBR-WAR"));
        this.mechID.put(482, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.HERO, "NANUQ", "WHK-NQ"));
        this.mechID.put(483, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.HERO, "ULTRAVIOLET", "DWF-UV"));
        this.mechID.put(484, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PANTHER, EVariantType.SPECIAL, "PANTHER PNT-10K(S)", "PNT-10K(S)"));
        this.mechID.put(485, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.SPECIAL, "MIST LYNX MLX-PRIME(S)", "MLX-PRIME(S)"));
        this.mechID.put(486, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRAB, EVariantType.SPECIAL, "CRAB CRB-27B(S)", "CRB-27B(S)"));
        this.mechID.put(487, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.SPECIAL, "SHADOW CAT SHC-PRIME(S)", "SHC-PRIME(S)"));
        this.mechID.put(488, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRASSHOPPER, EVariantType.SPECIAL, "GRASSHOPPER GHR-5H(S)", "GHR-5H(S)"));
        this.mechID.put(489, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.SPECIAL, "EBON JAGUAR EBJ-PRIME(S)", "EBJ-PRIME(S)"));
        this.mechID.put(490, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.SPECIAL, "ZEUS ZEU-6T(S)", "ZEU-6T(S)"));
        this.mechID.put(491, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.SPECIAL, "WARHAWK WHK-C(S)", "WHK-C(S)"));
        this.mechID.put(492, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.SPECIAL, "MARAUDER IIC MAD-IIC(S)", "MAD-IIC(S)"));
        this.mechID.put(493, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.STANDARD, "MARAUDER IIC MAD-IIC", "MAD-IIC"));
        this.mechID.put(494, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.STANDARD, "MARAUDER IIC MAD-IIC-8", "MAD-IIC-8"));
        this.mechID.put(495, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.STANDARD, "MARAUDER IIC MAD-IIC-A", "MAD-IIC-A"));
        this.mechID.put(496, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.STANDARD, "MARAUDER IIC MAD-IIC-B", "MAD-IIC-B"));
        this.mechID.put(497, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.STANDARD, "MARAUDER IIC MAD-IIC-C", "MAD-IIC-C"));
        this.mechID.put(498, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.STANDARD, "MARAUDER IIC MAD-IIC-D", "MAD-IIC-D"));
        this.mechID.put(499, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.HERO, "SCORCH", "MAD-IIC-SC"));
        this.mechID.put(500, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BUSHWACKER, EVariantType.SPECIAL, "BUSHWACKER BSW-X1(S)", "BSW-X1(S)"));
        this.mechID.put(501, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BUSHWACKER, EVariantType.STANDARD, "BUSHWACKER BSW-X1", "BSW-X1"));
        this.mechID.put(502, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BUSHWACKER, EVariantType.STANDARD, "BUSHWACKER BSW-X2", "BSW-X2"));
        this.mechID.put(503, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BUSHWACKER, EVariantType.STANDARD, "BUSHWACKER BSW-S2", "BSW-S2"));
        this.mechID.put(504, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BUSHWACKER, EVariantType.STANDARD, "BUSHWACKER BSW-P1", "BSW-P1"));
        this.mechID.put(505, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BUSHWACKER, EVariantType.STANDARD, "BUSHWACKER BSW-P2", "BSW-P2"));
        this.mechID.put(506, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BUSHWACKER, EVariantType.HERO, "HIGH ROLLER", "BSW-HR"));
        this.mechID.put(507, new MechIdInfo(EFaction.CLAN, EChassie.SUPERNOVA, EVariantType.SPECIAL, "SUPERNOVA SNV-1(S)", "SNV-1(S)"));
        this.mechID.put(508, new MechIdInfo(EFaction.CLAN, EChassie.SUPERNOVA, EVariantType.STANDARD, "SUPERNOVA SNV-1", "SNV-1"));
        this.mechID.put(509, new MechIdInfo(EFaction.CLAN, EChassie.SUPERNOVA, EVariantType.STANDARD, "SUPERNOVA SNV-3", "SNV-3"));
        this.mechID.put(510, new MechIdInfo(EFaction.CLAN, EChassie.SUPERNOVA, EVariantType.STANDARD, "SUPERNOVA SNV-A", "SNV-A"));
        this.mechID.put(511, new MechIdInfo(EFaction.CLAN, EChassie.SUPERNOVA, EVariantType.STANDARD, "SUPERNOVA SNV-B", "SNV-B"));
        this.mechID.put(512, new MechIdInfo(EFaction.CLAN, EChassie.SUPERNOVA, EVariantType.STANDARD, "SUPERNOVA SNV-C", "SNV-C"));
        this.mechID.put(513, new MechIdInfo(EFaction.CLAN, EChassie.SUPERNOVA, EVariantType.HERO, "BOILER", "SNV-BR"));
        this.mechID.put(514, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKJACK, EVariantType.STANDARD, "BLACKJACK BJ-2", "BJ-2"));
        this.mechID.put(515, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.STANDARD, "STALKER STK-3FB", "STK-3FB"));
        this.mechID.put(516, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.STANDARD, "HELLBRINGER HBR-F", "HBR-F"));
        this.mechID.put(517, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.STANDARD, "SUMMONER SMN-F", "SMN-F"));
        this.mechID.put(518, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.STANDARD, "SUMMONER SMN-M", "SMN-M"));
        this.mechID.put(519, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ASSASSIN, EVariantType.SPECIAL, "ASSASSIN ASN-21(S)", "ASN-21(S)"));
        this.mechID.put(520, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ASSASSIN, EVariantType.STANDARD, "ASSASSIN ASN-21", "ASN-21"));
        this.mechID.put(521, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ASSASSIN, EVariantType.STANDARD, "ASSASSIN ASN-23", "ASN-23"));
        this.mechID.put(522, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ASSASSIN, EVariantType.STANDARD, "ASSASSIN ASN-101", "ASN-101"));
        this.mechID.put(523, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ASSASSIN, EVariantType.STANDARD, "ASSASSIN ASN-26", "ASN-26"));
        this.mechID.put(524, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ASSASSIN, EVariantType.STANDARD, "ASSASSIN ASN-27", "ASN-27"));
        this.mechID.put(525, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ASSASSIN, EVariantType.HERO, "DARKDEATH", "ASN-DD"));
        this.mechID.put(526, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLFHOUND, EVariantType.CHAMPION, "WOLFHOUND WLF-1(C)", "WLF-1(C)"));
        this.mechID.put(527, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.CHAMPION, "PHOENIX HAWK PXH-2(C)", "PXH-2(C)"));
        this.mechID.put(528, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.CHAMPION, "WARHAMMER WHM-6D(C)", "WHM-6D(C)"));
        this.mechID.put(529, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MAULER, EVariantType.CHAMPION, "MAULER MAL-MX90(C)", "MAL-MX90(C)"));
        this.mechID.put(530, new MechIdInfo(EFaction.CLAN, EChassie.ADDER, EVariantType.CHAMPION, "ADDER ADR-PRIME(C)", "ADR-PRIME(C)"));
        this.mechID.put(531, new MechIdInfo(EFaction.CLAN, EChassie.NOVA, EVariantType.CHAMPION, "NOVA NVA-S(C)", "NVA-S(C)"));
        this.mechID.put(532, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.CHAMPION, "HELLBRINGER HBR-F(C)", "HBR-F(C)"));
        this.mechID.put(533, new MechIdInfo(EFaction.CLAN, EChassie.KODIAK, EVariantType.CHAMPION, "KODIAK KDK-3(C)", "KDK-3(C)"));
        this.mechID.put(534, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.SPECIAL, "ROUGHNECK RGH-1A(S)", "RGH-1A(S)"));
        this.mechID.put(535, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.STANDARD, "ROUGHNECK RGH-1A", "RGH-1A"));
        this.mechID.put(536, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.STANDARD, "ROUGHNECK RGH-1B", "RGH-1B"));
        this.mechID.put(537, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.STANDARD, "ROUGHNECK RGH-1C", "RGH-1C"));
        this.mechID.put(538, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.STANDARD, "ROUGHNECK RGH-2A", "RGH-2A"));
        this.mechID.put(539, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.STANDARD, "ROUGHNECK RGH-3A", "RGH-3A"));
        this.mechID.put(540, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.HERO, "REAVER", "RGH-R"));
        this.mechID.put(541, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.SPECIAL, "JAVELIN JVN-10N(S)", "JVN-10N(S)"));
        this.mechID.put(542, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.STANDARD, "JAVELIN JVN-10N", "JVN-10N"));
        this.mechID.put(543, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.STANDARD, "JAVELIN JVN-10F", "JVN-10F"));
        this.mechID.put(544, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.STANDARD, "JAVELIN JVN-10P", "JVN-10P"));
        this.mechID.put(545, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.STANDARD, "JAVELIN JVN-11A", "JVN-11A"));
        this.mechID.put(546, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.STANDARD, "JAVELIN JVN-11B", "JVN-11B"));
        this.mechID.put(547, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.HERO, "HI THERE!", "JVN-HT"));
        this.mechID.put(548, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.HERO, "K-9", "UM-K9"));
        this.mechID.put(549, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLFHOUND, EVariantType.HERO, "GRINNER", "WLF-GR"));
        this.mechID.put(550, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PANTHER, EVariantType.HERO, "KATANA KAT", "PNT-KK"));
        this.mechID.put(551, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRAB, EVariantType.HERO, "FLORENTINE", "CRB-FL"));
        this.mechID.put(552, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ENFORCER, EVariantType.HERO, "GHILLIE", "ENF-GH"));
        this.mechID.put(553, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRASSHOPPER, EVariantType.HERO, "MJÖLNIR", "GHR-MJ"));
        this.mechID.put(554, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKKNIGHT, EVariantType.HERO, "PARTISAN", "BL-P-KNT"));
        this.mechID.put(555, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ZEUS, EVariantType.HERO, "SKOKOMISH", "ZEU-SK"));
        this.mechID.put(556, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MAULER, EVariantType.HERO, "KNOCKOUT", "MAL-KO"));
        this.mechID.put(557, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.HERO, "KAIJU", "KGC-KJ"));
        this.mechID.put(558, new MechIdInfo(EFaction.INNERSPHERE, EChassie.UZIEL, EVariantType.SPECIAL, "UZIEL UZL-3S(S)", "UZL-3S(S)"));
        this.mechID.put(559, new MechIdInfo(EFaction.INNERSPHERE, EChassie.UZIEL, EVariantType.STANDARD, "UZIEL UZL-3S", "UZL-3S"));
        this.mechID.put(560, new MechIdInfo(EFaction.INNERSPHERE, EChassie.UZIEL, EVariantType.STANDARD, "UZIEL UZL-2S", "UZL-2S"));
        this.mechID.put(561, new MechIdInfo(EFaction.INNERSPHERE, EChassie.UZIEL, EVariantType.STANDARD, "UZIEL UZL-3P", "UZL-3P"));
        this.mechID.put(562, new MechIdInfo(EFaction.INNERSPHERE, EChassie.UZIEL, EVariantType.STANDARD, "UZIEL UZL-5P", "UZL-5P"));
        this.mechID.put(563, new MechIdInfo(EFaction.INNERSPHERE, EChassie.UZIEL, EVariantType.STANDARD, "UZIEL UZL-6P", "UZL-6P"));
        this.mechID.put(564, new MechIdInfo(EFaction.INNERSPHERE, EChassie.UZIEL, EVariantType.HERO, "BELIAL", "UZL-BE"));
        this.mechID.put(565, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ANNIHILATOR, EVariantType.SPECIAL, "ANNIHILATOR ANH-2A(S)", "ANH-2A(S)"));
        this.mechID.put(566, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ANNIHILATOR, EVariantType.STANDARD, "ANNIHILATOR ANH-2A", "ANH-2A"));
        this.mechID.put(567, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ANNIHILATOR, EVariantType.STANDARD, "ANNIHILATOR ANH-1A", "ANH-1A"));
        this.mechID.put(568, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ANNIHILATOR, EVariantType.STANDARD, "ANNIHILATOR ANH-1E", "ANH-1E"));
        this.mechID.put(569, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ANNIHILATOR, EVariantType.STANDARD, "ANNIHILATOR ANH-1X", "ANH-1X"));
        this.mechID.put(570, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ANNIHILATOR, EVariantType.STANDARD, "ANNIHILATOR ANH-1P", "ANH-1P"));
        this.mechID.put(571, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ANNIHILATOR, EVariantType.HERO, "MEAN BABY", "ANH-MB"));
        this.mechID.put(572, new MechIdInfo(EFaction.CLAN, EChassie.COUGAR, EVariantType.SPECIAL, "COUGAR COU-PRIME(S)", "COU-PRIME(S)"));
        this.mechID.put(573, new MechIdInfo(EFaction.CLAN, EChassie.COUGAR, EVariantType.STANDARD, "COUGAR COU-PRIME", "COU-PRIME"));
        this.mechID.put(574, new MechIdInfo(EFaction.CLAN, EChassie.COUGAR, EVariantType.STANDARD, "COUGAR COU-C", "COU-C"));
        this.mechID.put(575, new MechIdInfo(EFaction.CLAN, EChassie.COUGAR, EVariantType.STANDARD, "COUGAR COU-D", "COU-D"));
        this.mechID.put(576, new MechIdInfo(EFaction.CLAN, EChassie.COUGAR, EVariantType.STANDARD, "COUGAR COU-E", "COU-E"));
        this.mechID.put(577, new MechIdInfo(EFaction.CLAN, EChassie.COUGAR, EVariantType.STANDARD, "COUGAR COU-H", "COU-H"));
        this.mechID.put(578, new MechIdInfo(EFaction.CLAN, EChassie.COUGAR, EVariantType.HERO, "BLOOD ADDER", "COU-BA"));
        this.mechID.put(579, new MechIdInfo(EFaction.CLAN, EChassie.MADCATMKII, EVariantType.SPECIAL, "MAD CAT MK II MCII-1(S)", "MCII-1(S)"));
        this.mechID.put(580, new MechIdInfo(EFaction.CLAN, EChassie.MADCATMKII, EVariantType.STANDARD, "MAD CAT MK II MCII-1", "MCII-1"));
        this.mechID.put(581, new MechIdInfo(EFaction.CLAN, EChassie.MADCATMKII, EVariantType.STANDARD, "MAD CAT MK II MCII-2", "MCII-2"));
        this.mechID.put(582, new MechIdInfo(EFaction.CLAN, EChassie.MADCATMKII, EVariantType.STANDARD, "MAD CAT MK II MCII-4", "MCII-4"));
        this.mechID.put(583, new MechIdInfo(EFaction.CLAN, EChassie.MADCATMKII, EVariantType.STANDARD, "MAD CAT MK II MCII-A", "MCII-A"));
        this.mechID.put(584, new MechIdInfo(EFaction.CLAN, EChassie.MADCATMKII, EVariantType.STANDARD, "MAD CAT MK II MCII-B", "MCII-B"));
        this.mechID.put(585, new MechIdInfo(EFaction.CLAN, EChassie.MADCATMKII, EVariantType.HERO, "DEATHSTRIKE", "MCII-DS"));
        this.mechID.put(586, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.HERO, "EBON DRAGOON", "MLX-ED"));
        this.mechID.put(587, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.HERO, "SHARD", "ACH-SH"));
        this.mechID.put(588, new MechIdInfo(EFaction.CLAN, EChassie.ICEFERRET, EVariantType.HERO, "RAINBOW CROW", "IFR-RC"));
        this.mechID.put(589, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.HERO, "MISHIPESHU", "SHC-MI"));
        this.mechID.put(590, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.HERO, "BANDIT", "MDD-BA"));
        this.mechID.put(591, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.HERO, "ESPRIT DE CORPS", "EBJ-EC"));
        this.mechID.put(592, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.HERO, "VIRAGO", "HBR-VI"));
        this.mechID.put(593, new MechIdInfo(EFaction.CLAN, EChassie.GARGOYLE, EVariantType.HERO, "KIN WOLF", "GAR-KW"));
        this.mechID.put(594, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.HERO, "CHERBI", "EXE-CH"));
        this.mechID.put(595, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.STANDARD, "MIST LYNX MLX-G", "MLX-G"));
        this.mechID.put(596, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.STANDARD, "ARCTIC CHEETAH ACH-E", "ACH-E"));
        this.mechID.put(597, new MechIdInfo(EFaction.CLAN, EChassie.ICEFERRET, EVariantType.STANDARD, "ICE FERRET IFR-P", "IFR-P"));
        this.mechID.put(598, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.STANDARD, "SHADOW CAT SHC-H", "SHC-H"));
        this.mechID.put(599, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.STANDARD, "MAD DOG MDD-H", "MDD-H"));
        this.mechID.put(600, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.STANDARD, "EBON JAGUAR EBJ-D", "EBJ-D"));
        this.mechID.put(601, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.STANDARD, "HELLBRINGER HBR-P", "HBR-P"));
        this.mechID.put(602, new MechIdInfo(EFaction.CLAN, EChassie.GARGOYLE, EVariantType.STANDARD, "GARGOYLE GAR-E", "GAR-E"));
        this.mechID.put(603, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.STANDARD, "EXECUTIONER EXE-E", "EXE-E"));
        this.mechID.put(604, new MechIdInfo(EFaction.CLAN, EChassie.JENNERIIC, EVariantType.HERO, "FURY", "JR7-IIC-FY"));
        this.mechID.put(605, new MechIdInfo(EFaction.CLAN, EChassie.HUNCHBACKIIC, EVariantType.HERO, "DEATHWISH", "HBK-IIC-DW"));
        this.mechID.put(606, new MechIdInfo(EFaction.CLAN, EChassie.ORIONIIC, EVariantType.HERO, "SKÖLL", "ON1-IIC-SK"));
        this.mechID.put(607, new MechIdInfo(EFaction.CLAN, EChassie.HIGHLANDERIIC, EVariantType.HERO, "KEEPER", "HGN-IIC-KP"));
        this.mechID.put(608, new MechIdInfo(EFaction.INNERSPHERE, EChassie.OSIRIS, EVariantType.SPECIAL, "OSIRIS OSR-3D(S)", "OSR-3D(S)"));
        this.mechID.put(609, new MechIdInfo(EFaction.INNERSPHERE, EChassie.OSIRIS, EVariantType.STANDARD, "OSIRIS OSR-3D", "OSR-3D"));
        this.mechID.put(610, new MechIdInfo(EFaction.INNERSPHERE, EChassie.OSIRIS, EVariantType.STANDARD, "OSIRIS OSR-4D", "OSR-4D"));
        this.mechID.put(611, new MechIdInfo(EFaction.INNERSPHERE, EChassie.OSIRIS, EVariantType.STANDARD, "OSIRIS OSR-1V", "OSR-1V"));
        this.mechID.put(612, new MechIdInfo(EFaction.INNERSPHERE, EChassie.OSIRIS, EVariantType.STANDARD, "OSIRIS OSR-2V", "OSR-2V"));
        this.mechID.put(613, new MechIdInfo(EFaction.INNERSPHERE, EChassie.OSIRIS, EVariantType.STANDARD, "OSIRIS OSR-1P", "OSR-1P"));
        this.mechID.put(614, new MechIdInfo(EFaction.INNERSPHERE, EChassie.OSIRIS, EVariantType.HERO, "SEKHMET", "OSR-SE"));
        this.mechID.put(615, new MechIdInfo(EFaction.INNERSPHERE, EChassie.NIGHTSTAR, EVariantType.SPECIAL, "NIGHTSTAR NSR-9J(S)", "NSR-9J(S)"));
        this.mechID.put(616, new MechIdInfo(EFaction.INNERSPHERE, EChassie.NIGHTSTAR, EVariantType.STANDARD, "NIGHTSTAR NSR-9J", "NSR-9J"));
        this.mechID.put(617, new MechIdInfo(EFaction.INNERSPHERE, EChassie.NIGHTSTAR, EVariantType.STANDARD, "NIGHTSTAR NSR-9FC", "NSR-9FC"));
        this.mechID.put(618, new MechIdInfo(EFaction.INNERSPHERE, EChassie.NIGHTSTAR, EVariantType.STANDARD, "NIGHTSTAR NSR-9S", "NSR-9S"));
        this.mechID.put(619, new MechIdInfo(EFaction.INNERSPHERE, EChassie.NIGHTSTAR, EVariantType.STANDARD, "NIGHTSTAR NSR-9P", "NSR-9P"));
        this.mechID.put(620, new MechIdInfo(EFaction.INNERSPHERE, EChassie.NIGHTSTAR, EVariantType.STANDARD, "NIGHTSTAR NSR-10P", "NSR-10P"));
        this.mechID.put(621, new MechIdInfo(EFaction.INNERSPHERE, EChassie.NIGHTSTAR, EVariantType.HERO, "WOLF PHOENIX", "NSR-WP"));
        this.mechID.put(622, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICWOLF, EVariantType.SPECIAL, "ARCTIC WOLF ACW-PRIME(S)", "ACW-PRIME(S)"));
        this.mechID.put(623, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICWOLF, EVariantType.STANDARD, "ARCTIC WOLF ACW-PRIME", "ACW-PRIME"));
        this.mechID.put(624, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICWOLF, EVariantType.STANDARD, "ARCTIC WOLF ACW-A", "ACW-A"));
        this.mechID.put(625, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICWOLF, EVariantType.STANDARD, "ARCTIC WOLF ACW-P", "ACW-P"));
        this.mechID.put(626, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICWOLF, EVariantType.STANDARD, "ARCTIC WOLF ACW-1", "ACW-1"));
        this.mechID.put(627, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICWOLF, EVariantType.STANDARD, "ARCTIC WOLF ACW-2", "ACW-2"));
        this.mechID.put(628, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICWOLF, EVariantType.HERO, "BLOOD KIT", "ACW-BK"));
        this.mechID.put(629, new MechIdInfo(EFaction.CLAN, EChassie.NOVACAT, EVariantType.SPECIAL, "NOVA CAT NCT-PRIME(S)", "NCT-PRIME(S)"));
        this.mechID.put(630, new MechIdInfo(EFaction.CLAN, EChassie.NOVACAT, EVariantType.STANDARD, "NOVA CAT NCT-PRIME", "NCT-PRIME"));
        this.mechID.put(631, new MechIdInfo(EFaction.CLAN, EChassie.NOVACAT, EVariantType.STANDARD, "NOVA CAT NCT-A", "NCT-A"));
        this.mechID.put(632, new MechIdInfo(EFaction.CLAN, EChassie.NOVACAT, EVariantType.STANDARD, "NOVA CAT NCT-B", "NCT-B"));
        this.mechID.put(633, new MechIdInfo(EFaction.CLAN, EChassie.NOVACAT, EVariantType.STANDARD, "NOVA CAT NCT-C", "NCT-C"));
        this.mechID.put(634, new MechIdInfo(EFaction.CLAN, EChassie.NOVACAT, EVariantType.STANDARD, "NOVA CAT NCT-D", "NCT-D"));
        this.mechID.put(635, new MechIdInfo(EFaction.CLAN, EChassie.NOVACAT, EVariantType.HERO, "COBRA CAT", "NCT-CC"));
        this.mechID.put(636, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THANATOS, EVariantType.SPECIAL, "THANATOS TNS-4S(S)", "TNS-4S(S)"));
        this.mechID.put(637, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THANATOS, EVariantType.STANDARD, "THANATOS TNS-4S", "TNS-4S"));
        this.mechID.put(638, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THANATOS, EVariantType.STANDARD, "THANATOS TNS-4P", "TNS-4P"));
        this.mechID.put(639, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THANATOS, EVariantType.STANDARD, "THANATOS TNS-5P", "TNS-5P"));
        this.mechID.put(640, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THANATOS, EVariantType.STANDARD, "THANATOS TNS-5S", "TNS-5S"));
        this.mechID.put(641, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THANATOS, EVariantType.STANDARD, "THANATOS TNS-5T", "TNS-5T"));
        this.mechID.put(642, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THANATOS, EVariantType.HERO, "HANGOVER", "TNS-HA"));
        this.mechID.put(643, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.SPECIAL, "URBANMECH UM-R68(L)", "UM-R68(L)"));
        this.mechID.put(644, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.SPECIAL, "GRIFFIN GRF-5M(L)", "GRF-5M(L)"));
        this.mechID.put(645, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VICTOR, EVariantType.SPECIAL, "VICTOR VTR-9A1(L)", "VTR-9A1(L)"));
        this.mechID.put(646, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.SPECIAL, "KIT FOX KFX-G(L)", "KFX-G(L)"));
        this.mechID.put(647, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.SPECIAL, "LINEBACKER LBK-H(L)", "LBK-H(L)"));
        this.mechID.put(648, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HELLSPAWN, EVariantType.SPECIAL, "HELLSPAWN HSN-7D(S)", "HSN-7D(S)"));
        this.mechID.put(649, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HELLSPAWN, EVariantType.STANDARD, "HELLSPAWN HSN-7D", "HSN-7D"));
        this.mechID.put(650, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HELLSPAWN, EVariantType.STANDARD, "HELLSPAWN HSN-8E", "HSN-8E"));
        this.mechID.put(651, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HELLSPAWN, EVariantType.SPECIAL, "HELLSPAWN HSN-9F", "HSN-9F"));
        this.mechID.put(652, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HELLSPAWN, EVariantType.SPECIAL, "HELLSPAWN HSN-7P", "HSN-7P"));
        this.mechID.put(653, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HELLSPAWN, EVariantType.SPECIAL, "HELLSPAWN HSN-8P", "HSN-8P"));
        this.mechID.put(654, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HELLSPAWN, EVariantType.HERO, "PARALYZER", "HSN-7D2"));
        this.mechID.put(655, new MechIdInfo(EFaction.CLAN, EChassie.PIRANHA, EVariantType.SPECIAL, "PIRANHA PIR-1(S)", "PIR-1(S)"));
        this.mechID.put(656, new MechIdInfo(EFaction.CLAN, EChassie.PIRANHA, EVariantType.STANDARD, "PIRANHA PIR-1", "PIR-1"));
        this.mechID.put(657, new MechIdInfo(EFaction.CLAN, EChassie.PIRANHA, EVariantType.STANDARD, "PIRANHA PIR-2", "PIR-2"));
        this.mechID.put(658, new MechIdInfo(EFaction.CLAN, EChassie.PIRANHA, EVariantType.STANDARD, "PIRANHA PIR-3", "PIR-3"));
        this.mechID.put(659, new MechIdInfo(EFaction.CLAN, EChassie.PIRANHA, EVariantType.STANDARD, "PIRANHA PIR-A", "PIR-A"));
        this.mechID.put(660, new MechIdInfo(EFaction.CLAN, EChassie.PIRANHA, EVariantType.STANDARD, "PIRANHA PIR-B", "PIR-B"));
        this.mechID.put(661, new MechIdInfo(EFaction.CLAN, EChassie.PIRANHA, EVariantType.HERO, "CIPHER", "PIR-CI"));
        this.mechID.put(662, new MechIdInfo(EFaction.CLAN, EChassie.BLACKLANNER, EVariantType.SPECIAL, "BLACK LANNER BKL-PRIME(S)", "BKL-PRIME(S)"));
        this.mechID.put(663, new MechIdInfo(EFaction.CLAN, EChassie.BLACKLANNER, EVariantType.STANDARD, "BLACK LANNER BKL-PRIME", "BKL-PRIME"));
        this.mechID.put(664, new MechIdInfo(EFaction.CLAN, EChassie.BLACKLANNER, EVariantType.STANDARD, "BLACK LANNER BKL-A", "BKL-A"));
        this.mechID.put(665, new MechIdInfo(EFaction.CLAN, EChassie.BLACKLANNER, EVariantType.STANDARD, "BLACK LANNER BKL-C", "BKL-C"));
        this.mechID.put(666, new MechIdInfo(EFaction.CLAN, EChassie.BLACKLANNER, EVariantType.STANDARD, "BLACK LANNER BKL-D", "BKL-D"));
        this.mechID.put(667, new MechIdInfo(EFaction.CLAN, EChassie.BLACKLANNER, EVariantType.STANDARD, "BLACK LANNER BKL-E", "BKL-E"));
        this.mechID.put(668, new MechIdInfo(EFaction.CLAN, EChassie.BLACKLANNER, EVariantType.HERO, "BELLONARIUS", "BKL-BL"));
        this.mechID.put(669, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.STANDARD, "URBANMECH UM-R68", "UM-R68"));
        this.mechID.put(670, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.STANDARD, "GRIFFIN GRF-5M", "GRF-5M"));
        this.mechID.put(671, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VICTOR, EVariantType.STANDARD, "VICTOR VTR-9A1", "VTR-9A1"));
        this.mechID.put(672, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.STANDARD, "KIT FOX KFX-G", "KFX-G"));
        this.mechID.put(673, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.STANDARD, "LINEBACKER LBK-H", "LBK-H"));
        this.mechID.put(674, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.SPECIAL, "SUN SPIDER SNS-PRIME(S)", "SNS-PRIME(S)"));
        this.mechID.put(675, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.STANDARD, "SUN SPIDER SNS-PRIME", "SNS-PRIME"));
        this.mechID.put(676, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.STANDARD, "SUN SPIDER SNS-A", "SNS-A"));
        this.mechID.put(677, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.STANDARD, "SUN SPIDER SNS-B", "SNS-B"));
        this.mechID.put(678, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.STANDARD, "SUN SPIDER SNS-C", "SNS-C"));
        this.mechID.put(679, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.STANDARD, "SUN SPIDER SNS-D", "SNS-D"));
        this.mechID.put(680, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.HERO, "MANUL", "SNS-ML"));
        this.mechID.put(681, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.HERO, "VANGUARD", "SNS-VG"));
        this.mechID.put(682, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.HERO, "POWERHOUSE", "RGH-PH"));
        this.mechID.put(683, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FAFNIR, EVariantType.SPECIAL, "FAFNIR FNR-5(S)", "FNR-5(S)"));
        this.mechID.put(684, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FAFNIR, EVariantType.STANDARD, "FAFNIR FNR-5", "FNR-5"));
        this.mechID.put(685, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FAFNIR, EVariantType.STANDARD, "FAFNIR FNR-5B", "FNR-5B"));
        this.mechID.put(686, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FAFNIR, EVariantType.STANDARD, "FAFNIR FNR-6U", "FNR-6U"));
        this.mechID.put(687, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FAFNIR, EVariantType.STANDARD, "FAFNIR FNR-5E", "FNR-5E"));
        this.mechID.put(688, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FAFNIR, EVariantType.STANDARD, "FAFNIR FNR-6R", "FNR-6R"));
        this.mechID.put(689, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FAFNIR, EVariantType.HERO, "WRATH", "FNR-WR"));
        this.mechID.put(690, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.HERO, "STREET CLEANER", "UM-SC"));
        this.mechID.put(691, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FIRESTARTER, EVariantType.HERO, "FIRESTORM", "FS9-FS"));
        this.mechID.put(692, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.HERO, "ARES", "GRF-AR"));
        this.mechID.put(693, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RIFLEMAN, EVariantType.HERO, "DAO BREAKER", "RFL-DB"));
        this.mechID.put(694, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BANSHEE, EVariantType.HERO, "SIREN", "BNC-SR"));
        this.mechID.put(695, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.HERO, "KRAKEN", "AS7-KR"));
        this.mechID.put(696, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.HERO, "REVENANT", "MDD-RV"));
        this.mechID.put(697, new MechIdInfo(EFaction.INNERSPHERE, EChassie.NIGHTSTAR, EVariantType.CHAMPION, "NIGHTSTAR NSR-9P(C)", "NSR-9P(C)"));
        this.mechID.put(698, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.CHAMPION, "ROUGHNECK RGH-1A(C)", "RGH-1A(C)"));
        this.mechID.put(699, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BUSHWACKER, EVariantType.CHAMPION, "BUSHWACKER BSW-S2(C)", "BSW-S2(C)"));
        this.mechID.put(700, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ASSASSIN, EVariantType.CHAMPION, "ASSASSIN ASN-101(C)", "ASN-101(C)"));
        this.mechID.put(701, new MechIdInfo(EFaction.INNERSPHERE, EChassie.OSIRIS, EVariantType.CHAMPION, "OSIRIS OSR-1V(C)", "OSR-1V(C)"));
        this.mechID.put(702, new MechIdInfo(EFaction.INNERSPHERE, EChassie.UZIEL, EVariantType.CHAMPION, "UZIEL UZL-3P(C)", "UZL-3P(C)"));
        this.mechID.put(703, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.CHAMPION, "MAD DOG MDD-C(C)", "MDD-C(C)"));
        this.mechID.put(704, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.SPECIAL, "BLOOD ASP BAS-PRIME(S)", "BAS-PRIME(S)"));
        this.mechID.put(705, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.STANDARD, "BLOOD ASP BAS-PRIME", "BAS-PRIME"));
        this.mechID.put(706, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.STANDARD, "BLOOD ASP BAS-A", "BAS-A"));
        this.mechID.put(707, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.STANDARD, "BLOOD ASP BAS-B", "BAS-B"));
        this.mechID.put(708, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.STANDARD, "BLOOD ASP BAS-C", "BAS-C"));
        this.mechID.put(709, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.STANDARD, "BLOOD ASP BAS-D", "BAS-D"));
        this.mechID.put(710, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.HERO, "RANCOR", "BAS-RA"));
        this.mechID.put(711, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FLEA, EVariantType.SPECIAL, "FLEA FLE-17(S)", "FLE-17(S)"));
        this.mechID.put(712, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FLEA, EVariantType.STANDARD, "FLEA FLE-17", "FLE-17"));
        this.mechID.put(713, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FLEA, EVariantType.STANDARD, "FLEA FLE-15", "FLE-15"));
        this.mechID.put(714, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FLEA, EVariantType.STANDARD, "FLEA FLE-19", "FLE-19"));
        this.mechID.put(715, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FLEA, EVariantType.STANDARD, "FLEA FLE-20", "FLE-20"));
        this.mechID.put(716, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FLEA, EVariantType.STANDARD, "FLEA FLE-FA", "FLE-FA"));
        this.mechID.put(717, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FLEA, EVariantType.HERO, "ROMEO 5000", "FLE-R5K"));
        this.mechID.put(718, new MechIdInfo(EFaction.CLAN, EChassie.HELLFIRE, EVariantType.SPECIAL, "HELLFIRE HLF-1(S)", "HLF-1(S)"));
        this.mechID.put(719, new MechIdInfo(EFaction.CLAN, EChassie.HELLFIRE, EVariantType.STANDARD, "HELLFIRE HLF-1", "HLF-1"));
        this.mechID.put(720, new MechIdInfo(EFaction.CLAN, EChassie.HELLFIRE, EVariantType.STANDARD, "HELLFIRE HLF-2", "HLF-2"));
        this.mechID.put(721, new MechIdInfo(EFaction.CLAN, EChassie.HELLFIRE, EVariantType.STANDARD, "HELLFIRE HLF-A", "HLF-A"));
        this.mechID.put(722, new MechIdInfo(EFaction.CLAN, EChassie.HELLFIRE, EVariantType.STANDARD, "HELLFIRE HLF-B", "HLF-B"));
        this.mechID.put(723, new MechIdInfo(EFaction.CLAN, EChassie.HELLFIRE, EVariantType.STANDARD, "HELLFIRE HLF-C", "HLF-C"));
        this.mechID.put(724, new MechIdInfo(EFaction.CLAN, EChassie.HELLFIRE, EVariantType.HERO, "VOID", "HLF-VO"));
        this.mechID.put(725, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VULCAN, EVariantType.SPECIAL, "VULCAN VL-2T(S)", "VL-2T(S)"));
        this.mechID.put(726, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VULCAN, EVariantType.STANDARD, "VULCAN VL-2T", "VL-2T"));
        this.mechID.put(727, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VULCAN, EVariantType.STANDARD, "VULCAN VL-5T", "VL-5T"));
        this.mechID.put(728, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VULCAN, EVariantType.STANDARD, "VULCAN VT-5M", "VT-5M"));
        this.mechID.put(729, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VULCAN, EVariantType.STANDARD, "VULCAN VT-5S", "VT-5S"));
        this.mechID.put(730, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VULCAN, EVariantType.STANDARD, "VULCAN VT-6M", "VT-6M"));
        this.mechID.put(731, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VULCAN, EVariantType.HERO, "BLOODLUST", "VL-BL"));
        this.mechID.put(732, new MechIdInfo(EFaction.CLAN, EChassie.INCUBUS, EVariantType.SPECIAL, "INCUBUS INC-1(S)", "INC-1(S)"));
        this.mechID.put(733, new MechIdInfo(EFaction.CLAN, EChassie.INCUBUS, EVariantType.STANDARD, "INCUBUS INC-1", "INC-1"));
        this.mechID.put(734, new MechIdInfo(EFaction.CLAN, EChassie.INCUBUS, EVariantType.STANDARD, "INCUBUS INC-2", "INC-2"));
        this.mechID.put(735, new MechIdInfo(EFaction.CLAN, EChassie.INCUBUS, EVariantType.STANDARD, "INCUBUS INC-3", "INC-3"));
        this.mechID.put(736, new MechIdInfo(EFaction.CLAN, EChassie.INCUBUS, EVariantType.STANDARD, "INCUBUS INC-4", "INC-4"));
        this.mechID.put(737, new MechIdInfo(EFaction.CLAN, EChassie.INCUBUS, EVariantType.STANDARD, "INCUBUS INC-5", "INC-5"));
        this.mechID.put(738, new MechIdInfo(EFaction.CLAN, EChassie.INCUBUS, EVariantType.HERO, "SABRE", "INC-SA"));
        this.mechID.put(739, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHAMPION, EVariantType.SPECIAL, "CHAMPION CHP-1N(S)", "CHP-1N(S)"));
        this.mechID.put(740, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHAMPION, EVariantType.STANDARD, "CHAMPION CHP-1N", "CHP-1N"));
        this.mechID.put(741, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHAMPION, EVariantType.STANDARD, "CHAMPION CHP-2N", "CHP-2N"));
        this.mechID.put(742, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHAMPION, EVariantType.STANDARD, "CHAMPION CHP-3N", "CHP-3N"));
        this.mechID.put(743, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHAMPION, EVariantType.STANDARD, "CHAMPION CHP-1N2", "CHP-1N2"));
        this.mechID.put(744, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHAMPION, EVariantType.STANDARD, "CHAMPION CHP-1NB", "CHP-1NB"));
        this.mechID.put(745, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHAMPION, EVariantType.HERO, "INVICTUS", "CHP-INV"));
        this.mechID.put(746, new MechIdInfo(EFaction.CLAN, EChassie.VAPOREAGLE, EVariantType.SPECIAL, "VAPOR EAGLE VGL-1(S)", "VGL-1(S)"));
        this.mechID.put(747, new MechIdInfo(EFaction.CLAN, EChassie.VAPOREAGLE, EVariantType.STANDARD, "VAPOR EAGLE VGL-1", "VGL-1"));
        this.mechID.put(748, new MechIdInfo(EFaction.CLAN, EChassie.VAPOREAGLE, EVariantType.STANDARD, "VAPOR EAGLE VGL-2", "VGL-2"));
        this.mechID.put(749, new MechIdInfo(EFaction.CLAN, EChassie.VAPOREAGLE, EVariantType.STANDARD, "VAPOR EAGLE VGL-3", "VGL-3"));
        this.mechID.put(750, new MechIdInfo(EFaction.CLAN, EChassie.VAPOREAGLE, EVariantType.STANDARD, "VAPOR EAGLE VGL-4", "VGL-4"));
        this.mechID.put(751, new MechIdInfo(EFaction.CLAN, EChassie.VAPOREAGLE, EVariantType.STANDARD, "VAPOR EAGLE VGL-A", "VGL-A"));
        this.mechID.put(752, new MechIdInfo(EFaction.CLAN, EChassie.VAPOREAGLE, EVariantType.HERO, "RIVAL", "VGL-RI"));
        this.mechID.put(753, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.SPECIAL, "JAVELIN JVN-11F(L)", "JVN-11F(L)"));
        this.mechID.put(754, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RIFLEMAN, EVariantType.SPECIAL, "RIFLEMAN RFL-8D(L)", "RFL-8D(L)"));
        this.mechID.put(755, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.SPECIAL, "WARHAMMER WHM-4L(L)", "WHM-4L(L)"));
        this.mechID.put(756, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.SPECIAL, "NIGHT GYR NTG-H(L)", "NTG-H(L)"));
        this.mechID.put(757, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.SPECIAL, "MARAUDER IIC MAD-IIC-2(L)", "MAD-IIC-2(L)"));
        this.mechID.put(758, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHARGER, EVariantType.SPECIAL, "CHARGER CGR-1A1(S)", "CGR-1A1(S)"));
        this.mechID.put(759, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHARGER, EVariantType.STANDARD, "CHARGER CGR-1A1", "CGR-1A1"));
        this.mechID.put(760, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHARGER, EVariantType.STANDARD, "CHARGER CGR-1A5", "CGR-1A5"));
        this.mechID.put(761, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHARGER, EVariantType.STANDARD, "CHARGER CGR-3K", "CGR-3K"));
        this.mechID.put(762, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CHARGER, EVariantType.HERO, "NUMBER SEVEN", "CGR-N7"));
        this.mechID.put(763, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HATAMOTOCHI, EVariantType.SPECIAL, "HATAMOTO-CHI HTM-27T(S)", "HTM-27T(S)"));
        this.mechID.put(764, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HATAMOTOCHI, EVariantType.STANDARD, "HATAMOTO-CHI HTM-27T", "HTM-27T"));
        this.mechID.put(765, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HATAMOTOCHI, EVariantType.STANDARD, "HATAMOTO-KU HTM-27W", "HTM-27W"));
        this.mechID.put(766, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HATAMOTOCHI, EVariantType.STANDARD, "HATAMOTO-CHI HTM-28TR", "HTM-28TR"));
        this.mechID.put(767, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HATAMOTOCHI, EVariantType.HERO, "SHUGO", "HTM-SG"));
        this.mechID.put(768, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.SPECIAL, "WARHAMMER IIC WHM-IIC(S)", "WHM-IIC(S)"));
        this.mechID.put(769, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.STANDARD, "WARHAMMER IIC WHM-IIC", "WHM-IIC"));
        this.mechID.put(770, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.STANDARD, "WARHAMMER IIC WHM-IIC-2", "WHM-IIC-2"));
        this.mechID.put(771, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.STANDARD, "WARHAMMER IIC WHM-IIC-3", "WHM-IIC-3"));
        this.mechID.put(772, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.STANDARD, "WARHAMMER IIC WHM-IIC-4", "WHM-IIC-4"));
        this.mechID.put(773, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.STANDARD, "WARHAMMER IIC WHM-IIC-10", "WHM-IIC-10"));
        this.mechID.put(774, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.HERO, "MAUL", "WHM-IIC-ML"));
        this.mechID.put(775, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.HERO, "BLUDGEON", "WHM-IIC-BL"));
        this.mechID.put(776, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.STANDARD, "JAVELIN JVN-11F", "JVN-11F"));
        this.mechID.put(777, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RIFLEMAN, EVariantType.STANDARD, "RIFLEMAN RFL-8D", "RFL-8D"));
        this.mechID.put(778, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.STANDARD, "WARHAMMER WHM-4L", "WHM-4L"));
        this.mechID.put(779, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.STANDARD, "NIGHT GYR NTG-H", "NTG-H"));
        this.mechID.put(780, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.STANDARD, "MARAUDER IIC MAD-IIC-2", "MAD-IIC-2"));
        this.mechID.put(781, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.STANDARD, "WARHAMMER IIC WHM-IIC-A", "WHM-IIC-A"));
        this.mechID.put(782, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CORSAIR, EVariantType.SPECIAL, "CORSAIR COR-5R(S)", "COR-5R(S)"));
        this.mechID.put(783, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CORSAIR, EVariantType.STANDARD, "CORSAIR COR-5R", "COR-5R"));
        this.mechID.put(784, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CORSAIR, EVariantType.STANDARD, "CORSAIR COR-6R", "COR-6R"));
        this.mechID.put(785, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CORSAIR, EVariantType.STANDARD, "CORSAIR COR-7A", "COR-7A"));
        this.mechID.put(786, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CORSAIR, EVariantType.STANDARD, "CORSAIR COR-5T", "COR-5T"));
        this.mechID.put(787, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CORSAIR, EVariantType.STANDARD, "CORSAIR COR-7R", "COR-7R"));
        this.mechID.put(788, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CORSAIR, EVariantType.HERO, "RAVAGER", "COR-RA"));
        this.mechID.put(789, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CORSAIR, EVariantType.HERO, "BROADSIDE", "COR-BR"));
        this.mechID.put(790, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDERII, EVariantType.SPECIAL, "MARAUDER II MAD-4A(S)", "MAD-4A(S)"));
        this.mechID.put(791, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDERII, EVariantType.STANDARD, "MARAUDER II MAD-4A", "MAD-4A"));
        this.mechID.put(792, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDERII, EVariantType.STANDARD, "MARAUDER II MAD-4HP", "MAD-4HP"));
        this.mechID.put(793, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDERII, EVariantType.STANDARD, "MARAUDER II MAD-5A", "MAD-5A"));
        this.mechID.put(794, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDERII, EVariantType.STANDARD, "MARAUDER II MAD-4L", "MAD-4L"));
        this.mechID.put(795, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDERII, EVariantType.STANDARD, "MARAUDER II MAD-6S", "MAD-6S"));
        this.mechID.put(796, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDERII, EVariantType.HERO, "ALPHA", "MAD-AL"));
        this.mechID.put(797, new MechIdInfo(EFaction.CLAN, EChassie.RIFLEMANIIC, EVariantType.SPECIAL, "RIFLEMAN IIC RFL-IIC(S)", "RFL-IIC(S)"));
        this.mechID.put(798, new MechIdInfo(EFaction.CLAN, EChassie.RIFLEMANIIC, EVariantType.STANDARD, "RIFLEMAN IIC RFL-IIC", "RFL-IIC"));
        this.mechID.put(799, new MechIdInfo(EFaction.CLAN, EChassie.RIFLEMANIIC, EVariantType.STANDARD, "RIFLEMAN IIC RFL-IIC-2", "RFL-IIC-2"));
        this.mechID.put(800, new MechIdInfo(EFaction.CLAN, EChassie.RIFLEMANIIC, EVariantType.STANDARD, "RIFLEMAN IIC RFL-IIC-3", "RFL-IIC-3"));
        this.mechID.put(801, new MechIdInfo(EFaction.CLAN, EChassie.RIFLEMANIIC, EVariantType.STANDARD, "RIFLEMAN IIC RFL-IIC-4", "RFL-IIC-4"));
        this.mechID.put(802, new MechIdInfo(EFaction.CLAN, EChassie.RIFLEMANIIC, EVariantType.STANDARD, "RIFLEMAN IIC RFL-IIC-A", "RFL-IIC-A"));
        this.mechID.put(803, new MechIdInfo(EFaction.CLAN, EChassie.RIFLEMANIIC, EVariantType.HERO, "CHIRONEX", "RFL-IIC-CH"));
        this.mechID.put(804, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.SPECIAL, "CATAPULT CPLT-C2(S)", "CPLT-C2(S)"));
        this.mechID.put(805, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.SPECIAL, "WARHAMMER WHM-9D(S)", "WHM-9D(S)"));
        this.mechID.put(806, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDER, EVariantType.SPECIAL, "MARAUDER MAD-9M(S)", "MAD-9M(S)"));
        this.mechID.put(807, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.SPECIAL, "KING CRAB KGC-001(S)", "KGC-001(S)"));
        this.mechID.put(808, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DERVISH, EVariantType.SPECIAL, "DERVISH DV-6M(S)", "DV-6M(S)"));
        this.mechID.put(809, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DERVISH, EVariantType.STANDARD, "DERVISH DV-6M", "DV-6M"));
        this.mechID.put(810, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DERVISH, EVariantType.STANDARD, "DERVISH DV-7D", "DV-7D"));
        this.mechID.put(811, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DERVISH, EVariantType.STANDARD, "DERVISH DV-8D", "DV-8D"));
        this.mechID.put(812, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DERVISH, EVariantType.STANDARD, "DERVISH DV-6MR", "DV-6MR"));
        this.mechID.put(813, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DERVISH, EVariantType.STANDARD, "DERVISH DV-7P", "DV-7P"));
        this.mechID.put(814, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DERVISH, EVariantType.HERO, "FRENZY", "DV-FR"));
        this.mechID.put(815, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.STANDARD, "CATAPULT CPLT-C2", "CPLT-C2"));
        this.mechID.put(816, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.STANDARD, "WARHAMMER WHM-9D", "WHM-9D"));
        this.mechID.put(817, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDER, EVariantType.STANDARD, "MARAUDER MAD-9M", "MAD-9M"));
        this.mechID.put(818, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.STANDARD, "KING CRAB KGC-001", "KGC-001"));
        this.mechID.put(819, new MechIdInfo(EFaction.INNERSPHERE, EChassie.LOCUST, EVariantType.SPECIAL, "LOCUST LCT-1V(S)", "LCT-1V(S)"));
        this.mechID.put(820, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SHADOWHAWK, EVariantType.SPECIAL, "SHADOW HAWK SHD-2H(S)", "SHD-2H(S)"));
        this.mechID.put(821, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.SPECIAL, "THUNDERBOLT TDR-5S(S)", "TDR-5S(S)"));
        this.mechID.put(822, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BATTLEMASTER, EVariantType.SPECIAL, "BATTLEMASTER BLR-1G(S)", "BLR-1G(S)"));
        this.mechID.put(823, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRIFFIN, EVariantType.SPECIAL, "GRIFFIN GRF-1N(S)", "GRF-1N(S)"));
        this.mechID.put(824, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLVERINE, EVariantType.SPECIAL, "WOLVERINE WVR-6R(S)", "WVR-6R(S)"));
        this.mechID.put(825, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.SPECIAL, "SNOWBALL", "UM-R60L(S)"));
        this.mechID.put(826, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.SPECIAL, "FIREBALL", "PXH-1K(S)"));
        this.mechID.put(827, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.SPECIAL, "THUNDERBOLT TDR-10SE(S)", "TDR-10SE(S)"));
        this.mechID.put(828, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DRAGON, EVariantType.SPECIAL, "GRAND DRAGON DRG-1G(S)", "DRG-1G(S)"));
        this.mechID.put(829, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THUNDERBOLT, EVariantType.STANDARD, "THUNDERBOLT TDR-10SE", "TDR-10SE"));
        this.mechID.put(830, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DRAGON, EVariantType.STANDARD, "GRAND DRAGON DRG-1G", "DRG-1G"));
        this.mechID.put(831, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.SPECIAL, "TIMBER WOLF TBR-BH(S)", "TBR-BH(S)"));
        this.mechID.put(832, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.SPECIAL, "DIRE WOLF DWF-C(S)", "DWF-C(S)"));
        this.mechID.put(833, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.STANDARD, "TIMBER WOLF TBR-BH", "TBR-BH"));
        this.mechID.put(834, new MechIdInfo(EFaction.CLAN, EChassie.DIREWOLF, EVariantType.STANDARD, "DIRE WOLF DWF-C", "DWF-C"));
        this.mechID.put(835, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.STANDARD, "PHOENIX HAWK PXH-7S", "PXH-7S"));
        this.mechID.put(836, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.SPECIAL, "PHOENIX HAWK PXH-7S(S)", "PXH-7S(S)"));
        this.mechID.put(837, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.STANDARD, "STALKER STK-7D", "STK-7D"));
        this.mechID.put(838, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.SPECIAL, "STALKER STK-7D(S)", "STK-7D(S)"));
        this.mechID.put(839, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.STANDARD, "URBANMECH UM-R80", "UM-R80"));
        this.mechID.put(840, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.STANDARD, "URBANMECH UM-R80(L)", "UM-R80(L)"));
        this.mechID.put(841, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.STANDARD, "VIPER VPR-F", "VPR-F"));
        this.mechID.put(842, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.STANDARD, "VIPER VPR-F(L)", "VPR-F(L)"));
        this.mechID.put(843, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.STANDARD, "ATLAS AS7-K3", "AS7-K3"));
        this.mechID.put(844, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.STANDARD, "ATLAS AS7-K3(L)", "AS7-K3(L)"));
        this.mechID.put(845, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.STANDARD, "NIGHT GYR NTG-D(CS)", "NTG-D(CS)"));
        this.mechID.put(846, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.HERO, "BOLT", "RGH-BLT"));
        this.mechID.put(847, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.HERO, "AMBUSH", "SNS-AMB"));
        this.mechID.put(848, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MAULER, EVariantType.SPECIAL, "MAULER MAL-2P(S)", "MAL-2P(S)"));
        this.mechID.put(849, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.SPECIAL, "MARAUDER IIC MAD-IIC-A", "MAD-IIC-A(S)"));
        this.mechID.put(850, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.STANDARD, "BLOOD ASP BAS-E", "BAS-E"));
        this.mechID.put(851, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.SPECIAL, "BLOOD ASP BAS-E(S)", "BAS-E(S)"));
        this.mechID.put(852, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.STANDARD, "SHADOW CAT SHC-D", "SHC-D"));
        this.mechID.put(853, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.SPECIAL, "SHADOW CAT SHC-D(S)", "SHC-D(S)"));
        this.mechID.put(854, new MechIdInfo(EFaction.CLAN, EChassie.INCUBUS, EVariantType.CHAMPION, "INCUBUS INC-5(C)", "INC-5(C)"));
        this.mechID.put(855, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.CHAMPION, "KIT FOX KFX-D(C)", "KFX-D(C)"));
        this.mechID.put(856, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FLEA, EVariantType.CHAMPION, "FLEA FLE-17(C)", "FLE-17(C)"));
        this.mechID.put(857, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAVELIN, EVariantType.CHAMPION, "JAVELIN JVN-11A(C)", "JVN-11A(C)"));
        this.mechID.put(858, new MechIdInfo(EFaction.CLAN, EChassie.HUNTSMAN, EVariantType.CHAMPION, "HUNTSMAN HMN-PRIME(C)", "HMN-PRIME(C)"));
        this.mechID.put(859, new MechIdInfo(EFaction.CLAN, EChassie.VAPOREAGLE, EVariantType.CHAMPION, "VAPOR EAGLE VGL-2(C)", "VGL-2(C)"));
        this.mechID.put(860, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DERVISH, EVariantType.CHAMPION, "DERVISH DV-7D(C)", "DV-7D(C)"));
        this.mechID.put(861, new MechIdInfo(EFaction.INNERSPHERE, EChassie.VULCAN, EVariantType.CHAMPION, "VULCAN VL-5T(C)", "VL-5T(C)"));
        this.mechID.put(862, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.CHAMPION, "LINEBACKER LBK-H(C)", "LBK-H(C)"));
        this.mechID.put(863, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.CHAMPION, "SUMMONER SMN-D(C)", "SMN-D(C)"));
        this.mechID.put(864, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ARCHER, EVariantType.CHAMPION, "ARCHER ARC-2R(C)", "ARC-2R(C)"));
        this.mechID.put(865, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDER, EVariantType.CHAMPION, "MARAUDER MAD-9M(C)", "MAD-9M(C)"));
        this.mechID.put(866, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.CHAMPION, "BLOOD ASP BAS-A(C)", "BAS-A(C)"));
        this.mechID.put(867, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.CHAMPION, "MARAUDER IIC MAD-IIC(C)", "MAD-IIC(C)"));
        this.mechID.put(868, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ANNIHILATOR, EVariantType.CHAMPION, "ANNIHILATOR ANH-1A(C)", "ANH-1A(C)"));
        this.mechID.put(869, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CYCLOPS, EVariantType.CHAMPION, "CYCLOPS CP-10-Q(C)", "CP-10-Q(C)"));
        this.mechID.put(870, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.SPECIAL, "ATLAS AS7-D-DC(P)", "AS7-D-DC(P)"));
        this.mechID.put(871, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.SPECIAL, "CATAPULT CPLT-K2(P)", "CPLT-K2(P)"));
        this.mechID.put(872, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.SPECIAL, "HUNCHBACK HBK-4P(P)", "HBK-4P(P)"));
        this.mechID.put(873, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JENNER, EVariantType.SPECIAL, "JENNER JR7-F(P)", "JR7-F(P)"));
        this.mechID.put(874, new MechIdInfo(EFaction.CLAN, EChassie.JENNERIIC, EVariantType.SPECIAL, "JENNER IIC JR7-IIC-A(P)", "JR7-IIC-A(P)"));
        this.mechID.put(875, new MechIdInfo(EFaction.CLAN, EChassie.HUNCHBACKIIC, EVariantType.SPECIAL, "HUNCHBACK IIC HBK-IIC-B(P)", "HBK-IIC-B(P)"));
        this.mechID.put(876, new MechIdInfo(EFaction.CLAN, EChassie.ORIONIIC, EVariantType.SPECIAL, "ORION IIC ON1-IIC-A(P)", "ON1-IIC-A(P)"));
        this.mechID.put(877, new MechIdInfo(EFaction.CLAN, EChassie.HIGHLANDERIIC, EVariantType.SPECIAL, "HIGHLANDER IIC HGN-IIC-B(P)", "HGN-IIC-B(P)"));
        this.mechID.put(878, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.STANDARD, "KIT FOX KFX-P", "KFX-P"));
        this.mechID.put(879, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.SPECIAL, "KIT FOX KFX-P(S)", "KFX-P(S)"));
        this.mechID.put(880, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.SPECIAL, "SUMMONER SMN-G", "SMN-G"));
        this.mechID.put(881, new MechIdInfo(EFaction.CLAN, EChassie.SUMMONER, EVariantType.STANDARD, "SUMMONER SMN-G(S)", "SMN-G(S)"));
        this.mechID.put(885, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WOLFHOUND, EVariantType.SPECIAL, "WOLFHOUND WLF-1A(P)", "WLF-1A(P)"));
        this.mechID.put(886, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRAB, EVariantType.SPECIAL, "CRAB CRB-27SL(P)", "CRB-27SL(P)"));
        this.mechID.put(887, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKKNIGHT, EVariantType.SPECIAL, "BLACK KNIGHT BL-6B-KNT(P)", "BL-6B-KNT(P)"));
        this.mechID.put(888, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MAULER, EVariantType.SPECIAL, "MAULER MAL-MX90(P)", "MAL-MX90(P)"));
        this.mechID.put(889, new MechIdInfo(EFaction.CLAN, EChassie.COUGAR, EVariantType.SPECIAL, "COUGAR COU-H(P)", "COU-H(P)"));
        this.mechID.put(890, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICWOLF, EVariantType.SPECIAL, "ARCTIC WOLF ACW-A(P)", "ACW-A(P)"));
        this.mechID.put(891, new MechIdInfo(EFaction.CLAN, EChassie.NOVACAT, EVariantType.SPECIAL, "NOVA CAT NCT-B(P)", "NCT-B(P)"));
        this.mechID.put(892, new MechIdInfo(EFaction.CLAN, EChassie.MADCATMKII, EVariantType.SPECIAL, "MAD CAT MK II MCII-B(P)", "MCII-B(P)"));
        this.mechID.put(893, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRUSADER, EVariantType.SPECIAL, "CRUSADER CRD-3R(S)", "CRD-3R(S)"));
        this.mechID.put(894, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRUSADER, EVariantType.STANDARD, "CRUSADER CRD-3R", "CRD-3R"));
        this.mechID.put(895, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRUSADER, EVariantType.STANDARD, "CRUSADER CRD-5K", "CRD-5K"));
        this.mechID.put(896, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRUSADER, EVariantType.STANDARD, "CRUSADER CRD-5M", "CRD-5M"));
        this.mechID.put(897, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRUSADER, EVariantType.STANDARD, "CRUSADER CRD-6T", "CRD-6T"));
        this.mechID.put(898, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRUSADER, EVariantType.STANDARD, "CRUSADER CRD-7L", "CRD-7L"));
        this.mechID.put(899, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRUSADER, EVariantType.STANDARD, "CRAEL", "CRD-CR"));
        this.mechID.put(900, new MechIdInfo(EFaction.INNERSPHERE, EChassie.SPIDER, EVariantType.SPECIAL, "SPIDER SDR-5D(P)", "SDR-5D(P)"));
        this.mechID.put(901, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CICADA, EVariantType.SPECIAL, "CICADA CDA-3M(P)", "CDA-3M(P)"));
        this.mechID.put(902, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JAGERMECH, EVariantType.SPECIAL, "JAGERMECH JM6-DD(P)", "JM6-DD(P)"));
        this.mechID.put(903, new MechIdInfo(EFaction.INNERSPHERE, EChassie.KINGCRAB, EVariantType.SPECIAL, "KING CRAB KGC-000(P)", "KGC-000(P)"));
        this.mechID.put(904, new MechIdInfo(EFaction.CLAN, EChassie.INCUBUS, EVariantType.SPECIAL, "INCUBUS INC-4(P)", "INC-4(P)"));
        this.mechID.put(905, new MechIdInfo(EFaction.CLAN, EChassie.VAPOREAGLE, EVariantType.SPECIAL, "VAPOR EAGLE VGL-3(P)", "VGL-3(P)"));
        this.mechID.put(906, new MechIdInfo(EFaction.CLAN, EChassie.HELLFIRE, EVariantType.SPECIAL, "HELLFIRE HLF-A(P)", "HLF-A(P)"));
        this.mechID.put(907, new MechIdInfo(EFaction.CLAN, EChassie.KODIAK, EVariantType.SPECIAL, "KODIAK KDK-3(P)", "KDK-3(P)"));
        this.mechID.put(908, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PANTHER, EVariantType.SPECIAL, "PANTHER PNT-9R(P)", "PNT-9R(P)"));
        this.mechID.put(909, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BUSHWACKER, EVariantType.SPECIAL, "BUSHWACKER BSW-X2(P)", "BSW-X2(P)"));
        this.mechID.put(910, new MechIdInfo(EFaction.INNERSPHERE, EChassie.GRASSHOPPER, EVariantType.SPECIAL, "GRASSHOPPER GHR-5P(P)", "GHR-5P(P)"));
        this.mechID.put(911, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ANNIHILATOR, EVariantType.SPECIAL, "ANNIHILATOR ANH-1X(P)", "ANH-1X(P)"));
        this.mechID.put(912, new MechIdInfo(EFaction.CLAN, EChassie.MISTLYNX, EVariantType.SPECIAL, "MIST LYNX MLX-G(P)", "MLX-G(P)"));
        this.mechID.put(913, new MechIdInfo(EFaction.CLAN, EChassie.STORMCROW, EVariantType.SPECIAL, "STORMCROW SCR-B(P)", "SCR-B(P)"));
        this.mechID.put(914, new MechIdInfo(EFaction.CLAN, EChassie.HELLBRINGER, EVariantType.SPECIAL, "HELLBRINGER HBR-B(P)", "HBR-B(P)"));
        this.mechID.put(915, new MechIdInfo(EFaction.CLAN, EChassie.GARGOYLE, EVariantType.SPECIAL, "GARGOYLE GAR-E(P)", "GAR-E(P)"));
        this.mechID.put(916, new MechIdInfo(EFaction.INNERSPHERE, EChassie.COMMANDO, EVariantType.SPECIAL, "COMMANDO COM-1B(P)", "COM-1B(P)"));
        this.mechID.put(917, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CENTURION, EVariantType.SPECIAL, "CENTURION CN9-D(P)", "CN9-D(P)"));
        this.mechID.put(918, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ORION, EVariantType.SPECIAL, "ORION ON1-K(P)", "ON1-K(P)"));
        this.mechID.put(919, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDERII, EVariantType.SPECIAL, "MARAUDER II MAD-6S(P)", "MAD-6S(P)"));
        this.mechID.put(920, new MechIdInfo(EFaction.CLAN, EChassie.ADDER, EVariantType.SPECIAL, "ADDER ADR-A(P)", "ADR-A(P)"));
        this.mechID.put(921, new MechIdInfo(EFaction.CLAN, EChassie.SHADOWCAT, EVariantType.SPECIAL, "SHADOW CAT SHC-B(P)", "SHC-B(P)"));
        this.mechID.put(922, new MechIdInfo(EFaction.CLAN, EChassie.TIMBERWOLF, EVariantType.SPECIAL, "TIMBER WOLF TBR-S(P)", "TBR-S(P)"));
        this.mechID.put(923, new MechIdInfo(EFaction.CLAN, EChassie.SUPERNOVA, EVariantType.SPECIAL, "SUPERNOVA SNV-B(P)", "SNV-B(P)"));
        this.mechID.put(924, new MechIdInfo(EFaction.INNERSPHERE, EChassie.WARHAMMER, EVariantType.SPECIAL, "WARHAMMER WHM-7S(P)", "WHM-7S(P)"));
        this.mechID.put(925, new MechIdInfo(EFaction.INNERSPHERE, EChassie.MARAUDER, EVariantType.SPECIAL, "MARAUDER MAD-5M(P)", "MAD-5M(P)"));
        this.mechID.put(926, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RIFLEMAN, EVariantType.SPECIAL, "RIFLEMAN RFL-5D(P)", "RFL-5D(P)"));
        this.mechID.put(927, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ARCHER, EVariantType.SPECIAL, "ARCHER ARC-5W(P)", "ARC-5W(P)"));
        this.mechID.put(928, new MechIdInfo(EFaction.CLAN, EChassie.WARHAMMERIIC, EVariantType.SPECIAL, "WARHAMMER IIC WHM-IIC-A(P)", "WHM-IIC-A(P)"));
        this.mechID.put(929, new MechIdInfo(EFaction.CLAN, EChassie.MARAUDERIIC, EVariantType.SPECIAL, "MARAUDER IIC MAD-IIC-D(P)", "MAD-IIC-D(P)"));
        this.mechID.put(930, new MechIdInfo(EFaction.CLAN, EChassie.RIFLEMANIIC, EVariantType.SPECIAL, "RIFLEMAN IIC RFL-IIC-2(P)", "RFL-IIC-2(P)"));
        this.mechID.put(931, new MechIdInfo(EFaction.CLAN, EChassie.MADDOG, EVariantType.SPECIAL, "MADDOG MDD-C(P)", "MDD-C(P)"));
        this.mechID.put(932, new MechIdInfo(EFaction.INNERSPHERE, EChassie.RAVEN, EVariantType.SPECIAL, "RAVEN RVN-2X(P)", "RVN-2X(P)"));
        this.mechID.put(933, new MechIdInfo(EFaction.INNERSPHERE, EChassie.BLACKJACK, EVariantType.SPECIAL, "BLACKJACK BJ-3(P)", "BJ-3(P)"));
        this.mechID.put(934, new MechIdInfo(EFaction.INNERSPHERE, EChassie.THANATOS, EVariantType.SPECIAL, "THANATOS TNS-5S(P)", "TNS-5S(P)"));
        this.mechID.put(935, new MechIdInfo(EFaction.INNERSPHERE, EChassie.FAFNIR, EVariantType.SPECIAL, "FAFNIR FNR-5B(P)", "FNR-5B(P)"));
        this.mechID.put(936, new MechIdInfo(EFaction.CLAN, EChassie.PIRANHA, EVariantType.SPECIAL, "PIRANHA PIR-2(P)", "PIR-2(P)"));
        this.mechID.put(937, new MechIdInfo(EFaction.CLAN, EChassie.ICEFERRET, EVariantType.SPECIAL, "ICEFERRET IFR-P(P)", "IFR-P(P)"));
        this.mechID.put(938, new MechIdInfo(EFaction.CLAN, EChassie.EBONJAGUAR, EVariantType.SPECIAL, "EBONJAGUAR EBJ-A(P)", "EBJ-A(P)"));
        this.mechID.put(939, new MechIdInfo(EFaction.CLAN, EChassie.BLOODASP, EVariantType.SPECIAL, "BLOODASP BAS-B(P)", "BAS-B(P)"));
        this.mechID.put(940, new MechIdInfo(EFaction.INNERSPHERE, EChassie.TREBUCHET, EVariantType.SPECIAL, "TREBUCHET TBT-7K(P)", "TBT-7K(P)"));
        this.mechID.put(941, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ASSASSIN, EVariantType.SPECIAL, "ASSASSIN ASN-27(P)", "ASN-27(P)"));
        this.mechID.put(942, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HELLSPAWN, EVariantType.SPECIAL, "HELLSPAWN HSN-8P(P)", "HSN-8P(P)"));
        this.mechID.put(943, new MechIdInfo(EFaction.INNERSPHERE, EChassie.STALKER, EVariantType.SPECIAL, "STALKER STK-5M(P)", "STK-5M(P)"));
        this.mechID.put(944, new MechIdInfo(EFaction.CLAN, EChassie.ARCTICCHEETAH, EVariantType.SPECIAL, "ARCTICCHEETAH ACH-E(P)", "ACH-E(P)"));
        this.mechID.put(945, new MechIdInfo(EFaction.CLAN, EChassie.BLACKLANNER, EVariantType.SPECIAL, "BLACKLANNER BKL-D(P)", "BKL-D(P)"));
        this.mechID.put(946, new MechIdInfo(EFaction.CLAN, EChassie.NIGHTGYR, EVariantType.SPECIAL, "NIGHTGYR NTG-C(P)", "NTG-C(P)"));
        this.mechID.put(947, new MechIdInfo(EFaction.CLAN, EChassie.WARHAWK, EVariantType.SPECIAL, "WARHAWK WHK-B(P)", "WHK-B(P)"));
        this.mechID.put(948, new MechIdInfo(EFaction.INNERSPHERE, EChassie.URBANMECH, EVariantType.SPECIAL, "URBANMECH UM-R60(P)", "UM-R60(P)"));
        this.mechID.put(949, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ENFORCER, EVariantType.SPECIAL, "ENFORCER ENF-4P(P)", "ENF-4P(P)"));
        this.mechID.put(950, new MechIdInfo(EFaction.INNERSPHERE, EChassie.DRAGON, EVariantType.SPECIAL, "DRAGON DRG-1C(P)", "DRG-1C(P)"));
        this.mechID.put(951, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CYCLOPS, EVariantType.SPECIAL, "CYCLOPS CP-11-P(P)", "CP-11-P(P)"));
        this.mechID.put(952, new MechIdInfo(EFaction.INNERSPHERE, EChassie.PHOENIXHAWK, EVariantType.SPECIAL, "PHOENIXHAWK PXH-1B(P)", "PXH-1B(P)"));
        this.mechID.put(953, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CRUSADER, EVariantType.SPECIAL, "CRUSADER CRD-6T(P)", "CRD-6T(P)"));
        this.mechID.put(954, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ROUGHNECK, EVariantType.SPECIAL, "ROUGHNECK RGH-3A(P)", "RGH-3A(P)"));
        this.mechID.put(955, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CORSAIR, EVariantType.SPECIAL, "CORSAIR COR-7A(P)", "COR-7A(P)"));
        this.mechID.put(956, new MechIdInfo(EFaction.CLAN, EChassie.KITFOX, EVariantType.SPECIAL, "KITFOX KFX-D(P)", "KFX-D(P)"));
        this.mechID.put(957, new MechIdInfo(EFaction.CLAN, EChassie.VIPER, EVariantType.SPECIAL, "VIPER VPR-B(P)", "VPR-B(P)"));
        this.mechID.put(958, new MechIdInfo(EFaction.CLAN, EChassie.LINEBACKER, EVariantType.SPECIAL, "LINEBACKER LBK-D(P)", "LBK-D(P)"));
        this.mechID.put(959, new MechIdInfo(EFaction.CLAN, EChassie.EXECUTIONER, EVariantType.SPECIAL, "EXECUTIONER EXE-A(P)", "EXE-A(P)"));
        this.mechID.put(961, new MechIdInfo(EFaction.CLAN, EChassie.HUNTSMAN, EVariantType.SPECIAL, "HUNTSMAN HMN-P(P)", "HMN-P(P)"));
        this.mechID.put(962, new MechIdInfo(EFaction.CLAN, EChassie.SUNSPIDER, EVariantType.SPECIAL, "SUNSPIDER SNS-D(P)", "SNS-D(P)"));
        this.mechID.put(990, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.SPECIAL, "ATLAS AS7-D-DC", "AS7-D-DC"));
        this.mechID.put(996, new MechIdInfo(EFaction.INNERSPHERE, EChassie.JENNER, EVariantType.FOUNDER, "JENNER JR7-D(F)", "JR7-D(F)"));
        this.mechID.put(997, new MechIdInfo(EFaction.INNERSPHERE, EChassie.CATAPULT, EVariantType.FOUNDER, "CATAPULT CPLT-C1(F)", "CPLT-C1(F)"));
        this.mechID.put(998, new MechIdInfo(EFaction.INNERSPHERE, EChassie.HUNCHBACK, EVariantType.FOUNDER, "HUNCHBACK HBK-4G(F)", "HBK-4G(F)"));
        this.mechID.put(999, new MechIdInfo(EFaction.INNERSPHERE, EChassie.ATLAS, EVariantType.FOUNDER, "ATLAS AS7-D(F)", "AS7-D(F)"));


    }
*/

    /**
     * Mechklassen die es in MWO gibt.
     */
    public enum EMechclass {
        LIGHT, MEDIUM, HEAVY, ASSAULT, UNKNOWN
    }

    /**
     * Die Fraktionen die es in MWO gibt.
     */
    public enum EFaction {
        INNERSPHERE, CLAN, UNKNOWN
    }

    /**
     * Um welche Variante des Mach es sich handelt.
     */
    public enum EVariantType {
        STANDARD, SPECIAL, HERO, CHAMPION,
        SARAH, PHOENIX, FOUNDER

    }

    public String toString() {
        try {
            return "MechitemID: " + getMechItemId() + " " + getMechChassis() + " " + getFullName() + " is a " + getMechFaction() + " " + getMechClass() + " Mech an have " + getTonnage() + " tons";
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Alle Chassies die es in MWO gibt.
     */
    public enum EChassie {
        HUNCHBACK, JENNER, COMMANDO, CENTURION, DRAGON,
        CATAPULT, AWESOME, ATLAS, RAVEN, CICADA,
        CATAPHRACT, STALKER, SPIDER, TREBUCHET, JAGERMECH,
        HIGHLANDER, BLACKJACK, QUICKDRAW, VICTOR,
        KINTARO, ORION, LOCUST, SHADOWHAWK,
        THUNDERBOLT, BATTLEMASTER, GRIFFIN, WOLVERINE,
        FIRESTARTER, BANSHEE, TIMBERWOLF, ADDER,
        DIREWOLF, KITFOX, NOVA, STORMCROW,
        SUMMONER, WARHAWK, VINDICATOR, MADDOG,
        ICEFERRET, MISTLYNX, HELLBRINGER, GARGOYLE,
        KINGCRAB, PANTHER, ENFORCER, GRASSHOPPER,
        ZEUS, URBANMECH, EBONJAGUAR, EXECUTIONER,
        ARCTICCHEETAH, SHADOWCAT, BLACKKNIGHT, MAULER,
        CRAB, WOLFHOUND, MARAUDER, JENNERIIC,
        HUNCHBACKIIC, ORIONIIC, HIGHLANDERIIC, WARHAMMER,
        RIFLEMAN, ARCHER, KODIAK, PHOENIXHAWK,
        VIPER, CYCLOPS, NIGHTGYR, HUNTSMAN,
        LINEBACKER, MARAUDERIIC, BUSHWACKER, SUPERNOVA,
        ASSASSIN, ROUGHNECK, JAVELIN, UZIEL,
        ANNIHILATOR, COUGAR, MADCATMKII, OSIRIS,
        NIGHTSTAR, ARCTICWOLF, NOVACAT, THANATOS,
        HELLSPAWN, PIRANHA, BLACKLANNER, SUNSPIDER,
        FAFNIR, BLOODASP, FLEA, HELLFIRE,
        VULCAN, INCUBUS, CHAMPION, VAPOREAGLE,
        CHARGER, HATAMOTOCHI, WARHAMMERIIC, CORSAIR,
        MARAUDERII, RIFLEMANIIC, DERVISH, CRUSADER
    }

    /**
     * Alle Mech's die es gibt werden in einer (HashMap) initialesiert.
     *
     * @param mechItemId Die Mech ID die in der API ausgegeben wird.
     */
    public MechIdInfo(Integer mechItemId) throws ParserConfigurationException, IOException, SAXException {

        this.mechItemId = mechItemId;
        this.mechValid = false;
        // InitializeMechIds();
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new File(Objects.requireNonNull(MechIdInfo.class.getResource("/mechinfo/AllMechs.xml")).getFile()));
        doc.getDocumentElement().normalize();

        NodeList mechNodes = doc.getElementsByTagName("Mech");
        for (int i = 0; i < mechNodes.getLength(); i++) {
            Element xmlMechList = (Element) mechNodes.item(i);
            if (Objects.equals(mechItemId, Integer.valueOf(xmlMechList.getAttribute("id")))) {
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
                this.mechValid = true;
                break;
            }
        }
    }

    private Integer Base24IndexTable(char firstchar, char secondchar) {

        int IntValue = 0;

        char[] B24IT = new char[64];

        for (int i = 48; i < 112; i++) {

            B24IT[i - 48] = (char) (i);

        }

        if ((new String(B24IT).indexOf(firstchar)) >= 0 && (new String(B24IT).indexOf(secondchar)) >= 0) {

            firstchar = (char) new String(B24IT).indexOf(firstchar);
            secondchar = (char) new String(B24IT).indexOf(secondchar);

            IntValue = firstchar + (secondchar * 64);

        }
        return IntValue;
    }

    /**
     * Erstellt anhand eines MBC(Mech Build Code) die MechItemId
     *
     * @param MBC Der Code wenn man in MWO das Loadout exportiert und in die Zwischenablage kopiert.
     */
/*    public MechIdInfo(String MBC) {

        if (MBC.charAt(0) == 'A') {

            this.mechItemId = Base24IndexTable(MBC.charAt(1), MBC.charAt(2));

        } else {

            this.mechItemId = 0;

        }

        InitializeMechIds();

    }*/

    /**
     * Überprüft, ob es eine gültige MechItemId ist.
     *
     * @return Gibt den (Boolean) Wahrheitswert zurück
     */
    public Boolean IsValidId() {

        return this.mechValid;
    }

    /**
     * Die (int) Tonnage des Mech's wird zurückgegeben.
     *
     * @return Gibt die (int) Tonnage des Mech's zurück.
     */
    public Integer getTonnage() {
       /* if (getChassis() == null) {
            // MechItemId might be '0' (no Mech --> Spectator)
            return 999;
        } else {
            return switch (getChassis()) {
                case LOCUST, PIRANHA, FLEA -> 20;
                case COMMANDO, MISTLYNX -> 25;
                case SPIDER, KITFOX, URBANMECH, ARCTICCHEETAH, JAVELIN, OSIRIS, INCUBUS -> 30;
                case JENNER, RAVEN, FIRESTARTER, ADDER, PANTHER, WOLFHOUND, JENNERIIC, COUGAR -> 35;
                case CICADA, VIPER, ASSASSIN, ARCTICWOLF, VULCAN -> 40;
                case BLACKJACK, VINDICATOR, ICEFERRET, SHADOWCAT, PHOENIXHAWK, HELLSPAWN -> 45;
                case HUNCHBACK, CENTURION, TREBUCHET, NOVA, ENFORCER, CRAB, HUNCHBACKIIC, HUNTSMAN, UZIEL -> 50;
                case KINTARO, SHADOWHAWK, GRIFFIN, WOLVERINE, STORMCROW, BUSHWACKER, BLACKLANNER, VAPOREAGLE, DERVISH ->
                        55;
                case DRAGON, QUICKDRAW, MADDOG, RIFLEMAN, HELLFIRE, CHAMPION -> 60;
                case CATAPULT, JAGERMECH, THUNDERBOLT, HELLBRINGER, EBONJAGUAR, LINEBACKER, ROUGHNECK, RIFLEMANIIC, CRUSADER ->
                        65;
                case CATAPHRACT, SUMMONER, GRASSHOPPER, WARHAMMER, ARCHER, NOVACAT, SUNSPIDER -> 70;
                case ORION, TIMBERWOLF, BLACKKNIGHT, MARAUDER, ORIONIIC, NIGHTGYR, THANATOS -> 75;
                case AWESOME, VICTOR, GARGOYLE, ZEUS, CHARGER, HATAMOTOCHI, WARHAMMERIIC -> 80;
                case STALKER, BATTLEMASTER, WARHAWK, MARAUDERIIC -> 85;
                case HIGHLANDER, MAULER, HIGHLANDERIIC, CYCLOPS, SUPERNOVA, MADCATMKII, BLOODASP -> 90;
                case BANSHEE, EXECUTIONER, NIGHTSTAR, CORSAIR -> 95;
                case ATLAS, DIREWOLF, KINGCRAB, KODIAK, ANNIHILATOR, FAFNIR, MARAUDERII -> 100;
            };
        }*/
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
     * Hiermit wird eine Neue MechItemId (Integer) festgelegt.
     *
     * @param mechItemId Die Neue MechItemId.
     */
    public void setMechItemId(Integer mechItemId) {

        this.mechItemId = mechItemId;

    }

    /**
     * Die (String) Chassis des Mech's wird zurückgegeben.
     *
     * @return Gibt die (String) Chassis des Mech's zurück.
     */
    public String getMechChassis() {

        return this.mechChassis;

    }

    /**
     * Die (Enum) Mechklasse wird zurückgegeben:
     *
     * @return Gibt die (Enum) Mechklasse zurück.
     */
    public EMechclass getMechClass() throws ParserConfigurationException, IOException, SAXException {
        MechIdInfo n = new MechIdInfo(mechItemId);
        return switch (n.getTonnage()) {
            case 20, 25, 30, 35 -> EMechclass.LIGHT;
            case 40, 45, 50, 55 -> EMechclass.MEDIUM;
            case 60, 65, 70, 75 -> EMechclass.HEAVY;
            case 80, 85, 90, 95, 100 -> EMechclass.ASSAULT;
            default -> EMechclass.UNKNOWN;
        };
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

        //return IsValidId() ? this.mechID.get(this.mechItemId).fullName : this.msgInvalidID;
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

        //return IsValidId() ? this.mechID.get(this.mechItemId).shortname : this.msgInvalidID;
        return this.mechShortName;

    }

    /**
     * Die Kosten des Mechs werden anhand der Tonnage und der Variante berechnet und als (double) zurück gegeben.
     *
     * @return Kosten des Mechs in (double)
     */
    public double getMechCost() {

        //Multiplikator festlegen, wenn es sich um eine Spezial Variante handelt.
        double Multiply = switch (getMechVariantType()) {
            case "SPECIAL", "FOUNDER", "PHOENIX", "SARAH" -> 1.25;
            case "HERO" -> 1.15;
            case "CHAMPION" -> 1.2;
            default -> 1.0;

        };
        return getTonnage() * -50000 * Multiply;
    }

    public double getRepairCost(Integer HealthPercentage) {

        return (100 - HealthPercentage) * getMechCost() / 100;

    }

    /*static class Mech {
        private final int tonnage;
        private final EMechclass mechClass;
        private final EFaction mechFaction;
        private final Integer mechItemId;
        private final String mechName;
        private final String mechChassie;

        private Mech(String chassisName, int tonnage, EMechclass mechClass, String mechFaction, Integer mechItemId, String mechName) {
            this.tonnage = tonnage;
            this.mechClass = mechClass;
            this.mechFaction = EFaction.valueOf(mechFaction.toUpperCase());
            this.mechItemId = mechItemId;
            this.mechName = mechName;
            this.mechChassie = chassisName.toUpperCase();

        }

        public static Mech getMech(String chassisName, String mechFaction, Integer mechItemId, String mechName) {
            return switch (chassisName) {

                //Light Chassies
                case "piranha", "locust", "flea" ->
                        new Mech(chassisName, 20, EMechclass.LIGHT, mechFaction, mechItemId, mechName);
                case "mistlynx", "commando" ->
                        new Mech(chassisName, 25, EMechclass.LIGHT, mechFaction, mechItemId, mechName);
                case "arcticcheetah", "incubus", "kitfox", "javelin", "osiris", "spider", "urbanmech" ->
                        new Mech(chassisName, 30, EMechclass.LIGHT, mechFaction, mechItemId, mechName);
                case "adder", "cougar", "jenneriic", "jenner", "panther", "raven", "wolfhound", "firestarter" ->
                        new Mech(chassisName, 35, EMechclass.LIGHT, mechFaction, mechItemId, mechName);

                // Medium Chassies
                case "arcticwolf", "viper", "assassin", "cicada", "vulcan" ->
                        new Mech(chassisName, 40, EMechclass.MEDIUM, mechFaction, mechItemId, mechName);
                case "iceferret", "shadowcat", "blackjack", "hatchetman", "hellspawn", "phoenixhawk", "vindicator" ->
                        new Mech(chassisName, 45, EMechclass.MEDIUM, mechFaction, mechItemId, mechName);
                case "hunchbackiic", "huntsman", "nova", "centurion", "crab", "enforcer", "hunchback", "trebuchet", "uziel" ->
                        new Mech(chassisName, 50, EMechclass.MEDIUM, mechFaction, mechItemId, mechName);
                case "blacklanner", "stormcrow", "vaporeagle", "bushwacker", "dervish", "griffin", "kintaro", "shadowhawk", "wolverine" ->
                        new Mech(chassisName, 55, EMechclass.MEDIUM, mechFaction, mechItemId, mechName);

                //Heavy Chassies
                case "hellfire", "maddog", "champion", "dragon", "quickdraw", "rifleman" ->
                        new Mech(chassisName, 60, EMechclass.HEAVY, mechFaction, mechItemId, mechName);
                case "ebonjaguar", "hellbringer", "linebacker", "riflemaniic", "catapult", "crusader", "jagermech", "roughneck", "thunderbolt" ->
                        new Mech(chassisName, 65, EMechclass.HEAVY, mechFaction, mechItemId, mechName);
                case "novacat", "summoner", "sunspider", "archer", "cataphract", "grasshopper", "warhammer" ->
                        new Mech(chassisName, 70, EMechclass.HEAVY, mechFaction, mechItemId, mechName);
                case "nightgyr", "orioniic", "timberwolf", "blackknight", "marauder", "orion", "thanatos" ->
                        new Mech(chassisName, 75, EMechclass.HEAVY, mechFaction, mechItemId, mechName);

                //Assault Chassies
                case "gargoyle", "warhammeriic", "awesome", "charger", "hatamotochi", "victor", "zeus" ->
                        new Mech(chassisName, 80, EMechclass.ASSAULT, mechFaction, mechItemId, mechName);
                case "marauderiic", "warhawk", "battlemaster", "stalker" ->
                        new Mech(chassisName, 85, EMechclass.ASSAULT, mechFaction, mechItemId, mechName);
                case "bloodasp", "highlanderiic", "madcatmkii", "supernova", "cyclops", "highlander", "mauler" ->
                        new Mech(chassisName, 90, EMechclass.ASSAULT, mechFaction, mechItemId, mechName);
                case "executioner", "banshee", "corsair", "nightstar" ->
                        new Mech(chassisName, 95, EMechclass.ASSAULT, mechFaction, mechItemId, mechName);
                case "direwolf", "kodiak", "annihilator", "atlas", "fafnir", "kingcrab", "marauderii" ->
                        new Mech(chassisName, 100, EMechclass.ASSAULT, mechFaction, mechItemId, mechName);
                default -> null;
            };
        }

        public int getTonnage() {
            return tonnage;
        }

        public EMechclass getMechClass() {
            return mechClass;
        }

        public String toString() {
            return "MechitemID: " + getMechItemId() + " " + getMechChassie() + " " + getMechName() + " is a " + getMechFaction() + " " + getMechClass() + " Mech an have " + getTonnage() + " tons";
        }

        public EFaction getMechFaction() {
            return mechFaction;
        }

        public Integer getMechItemId() {
            return mechItemId;
        }

        private String addParenthesisToLastCharacter(String text) {
            if (text != null && !text.isEmpty()) {
                int lastIndex = text.length() - 1;
                char lastChar = text.charAt(lastIndex);
                return text.substring(0, lastIndex) + "(" + lastChar + ")";
            } else {
                return text;
            }
        }

        public String getMechName() {

            String newMachName = this.mechName.toUpperCase();
            if (
                    (193 < mechItemId) && (203 > mechItemId) ||
                            (348 < mechItemId) && (362 > mechItemId) ||
                            (397 < mechItemId) && (406 > mechItemId) ||
                            (447 < mechItemId) && (457 > mechItemId) ||
                            (462 < mechItemId) && (470 > mechItemId) ||
                            (483 < mechItemId) && (493 > mechItemId) ||
                            (752 < mechItemId) && (758 > mechItemId) ||
                            (818 < mechItemId) && (825 > mechItemId) ||
                            (852 < mechItemId) && (878 > mechItemId) ||
                            (881 < mechItemId) && (894 > mechItemId) ||
                            (899 < mechItemId) && (962 > mechItemId) ||
                            (967 < mechItemId) && (979 > mechItemId) ||
                            (980 < mechItemId) && (996 > mechItemId) ||
                            (3600 < mechItemId) && (3605 > mechItemId)
            ) {
                newMachName = addParenthesisToLastCharacter(this.mechName).toUpperCase();
            }
            return switch (this.mechName) {

                case "cn9-ah" -> "CN9-AH(L)";
                case "cn9-ancix" -> "CN9-A(NCIX)";
                case "as7-s" -> "AS7-S(L)";
                case "as7-d-dc-escort" -> "AS7-D-DC";
                case "jr7-d-founder" -> "JR7-D(F)";
                case "cplt-c1-founder" -> "CPLT-C1(F)";
                case "hbk-4g-founder" -> "HBK-4G(F)";
                case "as7-d-founder" -> "AS7-D(F)";
                case "drg-5nc",
                        "hbk-4pc",
                        "jr7-fc",
                        "jr7-ds",
                        "cplt-a1c",
                        "as7-rsc",
                        "cn9-ac",
                        "sdr-5kc",
                        "lct-1vp",
                        "shd-2hp",
                        "tdr-5sp",
                        "blr-1gp",
                        "bj-1c",
                        "hgn-733cc",
                        "grf-1np",
                        "wvr-6rp",
                        "cda-2ac",
                        "stk-3fc",
                        "ctf-3dc",
                        "vtr-9sc",
                        "on1-kc",
                        "fs9-sc",
                        "shd-2hc",
                        "tbr-primei",
                        "adr-primei",
                        "dwf-primei",
                        "kfx-primei",
                        "nva-primei",
                        "scr-primei",
                        "smn-primei",
                        "whk-primei",
                        "qkd-4gc",
                        "mdd-primei",
                        "tbt-7mc",
                        "tdr-9sec",
                        "ifr-primei",
                        "mlx-primei",
                        "rvn-3lc",
                        "hbr-primei",
                        "gar-primei",
                        "kgc-000l",
                        "enf-5dr",
                        "bnc-3mc",
                        "ghr-5jr",
                        "zeu-6sr",
                        "grf-1sc",
                        "wvr-6kc",
                        "um-r63s",
                        "jm6-ac",
                        "ebj-primei",
                        "exe-primei",
                        "ach-primei",
                        "shc-primei",
                        "enf-4rc",
                        "tbr-cc",
                        "bl-6-kntr",
                        "mal-1rr",
                        "crb-27r",
                        "wlf-2r",
                        "cda-3fl",
                        "wvr-7dl",
                        "zeu-9s2l",
                        "nva-dl",
                        "exe-cl",
                        "mad-3rs",
                        "hbk-iico",
                        "on1-iico",
                        "hgn-iico",
                        "whm-6rs",
                        "rfl-3ns",
                        "arc-2rs",
                        "lct-1ec",
                        "kdk-1s",
                        "pxh-1s",
                        "vpr-primes",
                        "cp-11-as",
                        "ntg-primes",
                        "bsw-x1s",
                        "snv-1s",
                        "asn-21s",
                        "jvn-10ns",
                        "uzl-3ss",
                        "anh-2as",
                        "cou-primes",
                        "mcii-1s",
                        "osr-3ds",
                        "nsr-9js",
                        "acw-primes",
                        "nct-primes",
                        "tns-4ss",
                        "um-r68l",
                        "grf-5ml",
                        "vtr-9a1l",
                        "kfx-gl",
                        "lbk-hl",
                        "hsn-7ds",
                        "pir-1s",
                        "bkl-primes",
                        "sns-primes",
                        "fnr-5s",
                        "nsr-9pc",
                        "rgh-1ac",
                        "bsw-s2c",
                        "asn-101c",
                        "osr-1vc",
                        "uzl-3pc",
                        "mdd-cc",
                        "bas-primes",
                        "fle-17s",
                        "hlf-1s",
                        "vl-2ts",
                        "inc-1s",
                        "chp-1ns",
                        "vgl-1s",
                        "htm-27ts",
                        "whm-iics",
                        "cor-5rs",
                        "mad-4as",
                        "rfl-iics",
                        "cplt-c2s",
                        "whm-9ds",
                        "mad-9ms",
                        "kgc-001s",
                        "dv-6ms",
                        "um-r60ls",
                        "pxh-1ks",
                        "tdr-10ses",
                        "drg-1gs",
                        "tbr-bhs",
                        "dwf-cs",
                        "pxh-7ss",
                        "stk-7ds",
                        "um-r80l",
                        "vpr-fl",
                        "as7-k3l",
                        "ntg-dcs",
                        "mal-2ps",
                        "mad-iic-as",
                        "bas-es",
                        "kfx-ps" -> addParenthesisToLastCharacter(this.mechName).toUpperCase();
                default -> newMachName;
            };

        }

        public String getMechChassie() {
            return mechChassie;
        }
    }*/
}
