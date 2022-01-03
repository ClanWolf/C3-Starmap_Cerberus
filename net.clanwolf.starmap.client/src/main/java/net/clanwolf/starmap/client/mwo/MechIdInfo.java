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
package net.clanwolf.starmap.client.mwo;

/**
 * Author: KERNREAKTOR
 * Version: Datenstand vom 14-12-2021
 */
public class MechIdInfo {
	
	private EFaction faction;	
	private EChassis chassis;	
	private String shortname;	
	private byte maxtons;	
	private EMechclass mechclass;	
	private String fullname;	
	private String varianttype;
	private Boolean isvalidid;
	
	public MechIdInfo(int mechitemid)
	{

		switch(mechitemid)
		{

        case 1:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-4G";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK HBK-4G";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 2:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-4P";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK HBK-4P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 3:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JENNER;
            this.shortname = "JR7-D";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER JR7-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 4:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JENNER;
            this.shortname = "JR7-F";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER JR7-F";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 5:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.COMMANDO;
            this.shortname = "COM-2D";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COMMANDO COM-2D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 6:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.COMMANDO;
            this.shortname = "COM-3A";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COMMANDO COM-3A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 7:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CENTURION;
            this.shortname = "CN9-A";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CENTURION CN9-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 8:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CENTURION;
            this.shortname = "CN9-AH(L)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CENTURION CN9-AH(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 9:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-4H";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK HBK-4H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 10:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DRAGON;
            this.shortname = "DRG-1N";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "DRAGON DRG-1N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 11:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DRAGON;
            this.shortname = "DRG-1C";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "DRAGON DRG-1C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 12:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-C1";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPULT CPLT-C1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 13:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-A1";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPULT CPLT-A1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 14:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.AWESOME;
            this.shortname = "AWS-8Q";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "AWESOME AWS-8Q";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 15:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.AWESOME;
            this.shortname = "AWS-8R";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "AWESOME AWS-8R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 16:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-D";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 17:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-D-DC";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-D-DC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 18:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-RS";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-RS";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 19:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-K2";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPULT CPLT-K2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 20:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JENNER;
            this.shortname = "JR7-K";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER JR7-K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 21:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-4J";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK HBK-4J";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 22:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-4SP";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK HBK-4SP";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 23:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DRAGON;
            this.shortname = "DRG-5N";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "DRAGON DRG-5N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 24:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-C4";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPULT CPLT-C4";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 25:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-K";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 26:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.COMMANDO;
            this.shortname = "COM-1B";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COMMANDO COM-1B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 27:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.COMMANDO;
            this.shortname = "COM-1D";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COMMANDO COM-1D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 28:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CENTURION;
            this.shortname = "CN9-AL";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CENTURION CN9-AL";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 29:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CENTURION;
            this.shortname = "CN9-D";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CENTURION CN9-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 30:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.AWESOME;
            this.shortname = "AWS-8T";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "AWESOME AWS-8T";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 31:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.AWESOME;
            this.shortname = "AWS-8V";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "AWESOME AWS-8V";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 32:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.AWESOME;
            this.shortname = "AWS-9M";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "AWESOME AWS-9M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 33:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RAVEN;
            this.shortname = "RVN-3L";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "RAVEN RVN-3L";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 34:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RAVEN;
            this.shortname = "RVN-2X";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "RAVEN RVN-2X";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 35:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RAVEN;
            this.shortname = "RVN-4X";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "RAVEN RVN-4X";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 36:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CICADA;
            this.shortname = "CDA-3M";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CICADA CDA-3M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 37:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CICADA;
            this.shortname = "CDA-2A";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CICADA CDA-2A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 38:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CICADA;
            this.shortname = "CDA-2B";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CICADA CDA-2B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 39:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CICADA;
            this.shortname = "CDA-3C";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CICADA CDA-3C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 40:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPHRACT;
            this.shortname = "CTF-3D";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPHRACT CTF-3D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 41:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPHRACT;
            this.shortname = "CTF-1X";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPHRACT CTF-1X";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 42:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPHRACT;
            this.shortname = "CTF-2X";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPHRACT CTF-2X";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 43:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPHRACT;
            this.shortname = "CTF-3L";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPHRACT CTF-3L";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 44:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPHRACT;
            this.shortname = "CTF-4X";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPHRACT CTF-4X";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 45:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CENTURION;
            this.shortname = "CN9-YLW";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "YEN-LO-WANG";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 46:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-5M";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-5M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 47:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-3F";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-3F";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 48:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-3H";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-3H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 49:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-4N";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-4N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 50:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-5S";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-5S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 51:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SPIDER;
            this.shortname = "SDR-5V";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "SPIDER SDR-5V";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 52:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SPIDER;
            this.shortname = "SDR-5D";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "SPIDER SDR-5D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 53:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SPIDER;
            this.shortname = "SDR-5K";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "SPIDER SDR-5K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 54:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPHRACT;
            this.shortname = "CTF-IM";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ILYA MUROMETS";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 55:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DRAGON;
            this.shortname = "DRG-FANG";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "FANG";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 56:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DRAGON;
            this.shortname = "DRG-FLAME";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "FLAME";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 57:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.COMMANDO;
            this.shortname = "COM-TDK";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "THE DEATH'S KNELL";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 58:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.AWESOME;
            this.shortname = "AWS-PB";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "PRETTY BABY";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 59:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.TREBUCHET;
            this.shortname = "TBT-7M";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "TREBUCHET TBT-7M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 60:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.TREBUCHET;
            this.shortname = "TBT-3C";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "TREBUCHET TBT-3C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 61:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.TREBUCHET;
            this.shortname = "TBT-5J";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "TREBUCHET TBT-5J";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 62:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.TREBUCHET;
            this.shortname = "TBT-5N";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "TREBUCHET TBT-5N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 63:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.TREBUCHET;
            this.shortname = "TBT-7K";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "TREBUCHET TBT-7K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 64:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CICADA;
            this.shortname = "CDA-X5";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "THE X-5";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 65:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAGERMECH;
            this.shortname = "JM6-DD";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "JAGERMECH JM6-DD";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 66:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAGERMECH;
            this.shortname = "JM6-A";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "JAGERMECH JM6-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 67:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAGERMECH;
            this.shortname = "JM6-S";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "JAGERMECH JM6-S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 69:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HIGHLANDER;
            this.shortname = "HGN-HM";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HEAVY METAL";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 70:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HIGHLANDER;
            this.shortname = "HGN-732";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER HGN-732";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 71:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HIGHLANDER;
            this.shortname = "HGN-733";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER HGN-733";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 72:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HIGHLANDER;
            this.shortname = "HGN-733C";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER HGN-733C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 73:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HIGHLANDER;
            this.shortname = "HGN-733P";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER HGN-733P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 74:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DRAGON;
            this.shortname = "DRG-5N(C)";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "DRAGON DRG-5N(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 75:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-M";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MISERY";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 76:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JENNER;
            this.shortname = "JR7-F(C)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER JR7-F(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 77:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKJACK;
            this.shortname = "BJ-1";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACKJACK BJ-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 78:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKJACK;
            this.shortname = "BJ-1DC";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACKJACK BJ-1DC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 79:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKJACK;
            this.shortname = "BJ-1X";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACKJACK BJ-1X";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 80:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKJACK;
            this.shortname = "BJ-3";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACKJACK BJ-3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 81:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAGERMECH;
            this.shortname = "JM6-FB";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "FIREBRAND";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 82:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.QUICKDRAW;
            this.shortname = "QKD-4G";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "QUICKDRAW QKD-4G";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 83:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.QUICKDRAW;
            this.shortname = "QKD-4H";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "QUICKDRAW QKD-4H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 84:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.QUICKDRAW;
            this.shortname = "QKD-5K";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "QUICKDRAW QKD-5K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 85:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-4P(C)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK HBK-4P(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 86:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JENNER;
            this.shortname = "JR7-D(S)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER JR7-D(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 87:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VICTOR;
            this.shortname = "VTR-DS";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DRAGON SLAYER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 88:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VICTOR;
            this.shortname = "VTR-9K";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "VICTOR VTR-9K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 89:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VICTOR;
            this.shortname = "VTR-9B";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "VICTOR VTR-9B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 90:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VICTOR;
            this.shortname = "VTR-9S";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "VICTOR VTR-9S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 91:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-A1(C)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPULT CPLT-A1(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 92:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-RS(C)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-RS(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 93:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINTARO;
            this.shortname = "KTO-GB";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GOLDEN BOY";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 94:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINTARO;
            this.shortname = "KTO-20";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "KINTARO KTO-20";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 95:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINTARO;
            this.shortname = "KTO-18";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "KINTARO KTO-18";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 96:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINTARO;
            this.shortname = "KTO-19";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "KINTARO KTO-19";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 97:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ORION;
            this.shortname = "ON1-P";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "PROTECTOR";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 98:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ORION;
            this.shortname = "ON1-M";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION ON1-M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 99:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ORION;
            this.shortname = "ON1-K";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION ON1-K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 100:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ORION;
            this.shortname = "ON1-V";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION ON1-V";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 101:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ORION;
            this.shortname = "ON1-VA";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION ON1-VA";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 102:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CENTURION;
            this.shortname = "CN9-A(C)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CENTURION CN9-A(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 103:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-BH";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "THE BOAR'S HEAD";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 104:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SPIDER;
            this.shortname = "SDR-5K(C)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "SPIDER SDR-5K(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 105:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-1V(P)";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "LOCUST LCT-1V(P)";
            this.varianttype = "PHOENIX";
            this.isvalidid = true;
            break;

        case 106:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-1V";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "LOCUST LCT-1V";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 107:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-3M";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "LOCUST LCT-3M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 108:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-3S";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "LOCUST LCT-3S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 109:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SHADOWHAWK;
            this.shortname = "SHD-2H(P)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW HAWK SHD-2H(P)";
            this.varianttype = "PHOENIX";
            this.isvalidid = true;
            break;

        case 110:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SHADOWHAWK;
            this.shortname = "SHD-2H";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW HAWK SHD-2H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 111:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SHADOWHAWK;
            this.shortname = "SHD-2D2";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW HAWK SHD-2D2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 112:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SHADOWHAWK;
            this.shortname = "SHD-5M";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW HAWK SHD-5M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 113:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-5S(P)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-5S(P)";
            this.varianttype = "PHOENIX";
            this.isvalidid = true;
            break;

        case 114:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-5S";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-5S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 115:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-5SS";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-5SS";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 116:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-9SE";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-9SE";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 117:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-1G(P)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BATTLEMASTER BLR-1G(P)";
            this.varianttype = "PHOENIX";
            this.isvalidid = true;
            break;

        case 118:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-1G";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BATTLEMASTER BLR-1G";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 119:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-1D";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BATTLEMASTER BLR-1D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 120:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-1S";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BATTLEMASTER BLR-1S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 121:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-J";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "JESTER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 122:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKJACK;
            this.shortname = "BJ-1(C)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACKJACK BJ-1(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 123:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JENNER;
            this.shortname = "JR7-O";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "OXIDE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 124:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HIGHLANDER;
            this.shortname = "HGN-733C(C)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER HGN-733C(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 125:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-1N(P)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRIFFIN GRF-1N(P)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 126:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-1N";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRIFFIN GRF-1N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 127:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-1S";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRIFFIN GRF-1S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 128:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-3M";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRIFFIN GRF-3M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 129:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLVERINE;
            this.shortname = "WVR-6R(P)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "WOLVERINE WVR-6R(P)";
            this.varianttype = "PHOENIX";
            this.isvalidid = true;
            break;

        case 130:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLVERINE;
            this.shortname = "WVR-6R";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "WOLVERINE WVR-6R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 131:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLVERINE;
            this.shortname = "WVR-6K";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "WOLVERINE WVR-6K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 132:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLVERINE;
            this.shortname = "WVR-7K";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "WOLVERINE WVR-7K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 133:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-GI";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRID IRON";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 134:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CICADA;
            this.shortname = "CDA-2A(C)";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CICADA CDA-2A(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 135:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-GI";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRID IRON LTD";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 136:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-3F(C)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-3F(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 137:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FIRESTARTER;
            this.shortname = "FS9-E";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "EMBER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 138:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FIRESTARTER;
            this.shortname = "FS9-S";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FIRESTARTER FS9-S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 139:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FIRESTARTER;
            this.shortname = "FS9-A";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FIRESTARTER FS9-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 140:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FIRESTARTER;
            this.shortname = "FS9-H";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FIRESTARTER FS9-H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 141:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FIRESTARTER;
            this.shortname = "FS9-K";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FIRESTARTER FS9-K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 142:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPHRACT;
            this.shortname = "CTF-3D(C)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPHRACT CTF-3D(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 143:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BANSHEE;
            this.shortname = "BNC-LM";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "LA MALINCHE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 144:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BANSHEE;
            this.shortname = "BNC-3E";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BANSHEE BNC-3E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 145:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BANSHEE;
            this.shortname = "BNC-3M";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BANSHEE BNC-3M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 146:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BANSHEE;
            this.shortname = "BNC-3S";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BANSHEE BNC-3S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 147:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VICTOR;
            this.shortname = "VTR-9S(C)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "VICTOR VTR-9S(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 148:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RAVEN;
            this.shortname = "RVN-H";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "HUGINN";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 149:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-3M";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BATTLEMASTER BLR-3M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 150:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-3S";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BATTLEMASTER BLR-3S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 151:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-1E";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "LOCUST LCT-1E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 152:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-1M";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "LOCUST LCT-1M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 153:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SHADOWHAWK;
            this.shortname = "SHD-2D";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW HAWK SHD-2D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 154:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SHADOWHAWK;
            this.shortname = "SHD-2K";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW HAWK SHD-2K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 155:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-9S";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-9S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 156:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ORION;
            this.shortname = "ON1-K(C)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION ON1-K(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 157:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.TREBUCHET;
            this.shortname = "TBT-LG";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "LOUP DE GUERRE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 158:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.QUICKDRAW;
            this.shortname = "QKD-IV4";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "IV-FOUR";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 159:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKJACK;
            this.shortname = "BJ-A";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "THE ARROW";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 160:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FIRESTARTER;
            this.shortname = "FS9-S(C)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FIRESTARTER FS9-S(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 161:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SHADOWHAWK;
            this.shortname = "SHD-2H(C)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW HAWK SHD-2H(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 162:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-PRIME(I)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 163:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-PRIME";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 164:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-C";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 165:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-S";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 166:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ADDER;
            this.shortname = "ADR-PRIME(I)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ADDER ADR-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 167:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ADDER;
            this.shortname = "ADR-PRIME";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ADDER ADR-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 168:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ADDER;
            this.shortname = "ADR-A";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ADDER ADR-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 169:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ADDER;
            this.shortname = "ADR-D";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ADDER ADR-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 170:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-PRIME(I)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 171:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-PRIME";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 172:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-A";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 173:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-B";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 174:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KITFOX;
            this.shortname = "KFX-PRIME(I)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "KIT FOX KFX-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 175:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KITFOX;
            this.shortname = "KFX-PRIME";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "KIT FOX KFX-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 176:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KITFOX;
            this.shortname = "KFX-D";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "KIT FOX KFX-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 177:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KITFOX;
            this.shortname = "KFX-S";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "KIT FOX KFX-S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 178:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-PRIME(I)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 179:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-PRIME";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 180:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-B";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 181:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-S";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 182:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-PRIME(I)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "STORMCROW SCR-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 183:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-PRIME";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "STORMCROW SCR-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 184:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-C";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "STORMCROW SCR-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 185:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-D";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "STORMCROW SCR-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 186:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-PRIME(I)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 187:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-PRIME";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 188:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-B";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 189:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-D";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 190:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAWK;
            this.shortname = "WHK-PRIME(I)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAWK WHK-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 191:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAWK;
            this.shortname = "WHK-PRIME";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAWK WHK-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 192:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAWK;
            this.shortname = "WHK-A";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAWK WHK-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 193:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAWK;
            this.shortname = "WHK-B";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAWK WHK-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 194:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-PRIME(G)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-PRIME(G)";
            this.varianttype = "GOLD";
            this.isvalidid = true;
            break;

        case 195:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ADDER;
            this.shortname = "ADR-PRIME(G)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ADDER ADR-PRIME(G)";
            this.varianttype = "GOLD";
            this.isvalidid = true;
            break;

        case 196:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-PRIME(G)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-PRIME(G)";
            this.varianttype = "GOLD";
            this.isvalidid = true;
            break;

        case 197:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KITFOX;
            this.shortname = "KFX-PRIME(G)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "KIT FOX KFX-PRIME(G)";
            this.varianttype = "GOLD";
            this.isvalidid = true;
            break;

        case 198:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-PRIME(G)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-PRIME(G)";
            this.varianttype = "GOLD";
            this.isvalidid = true;
            break;

        case 199:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-PRIME(G)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "STORMCROW SCR-PRIME(G)";
            this.varianttype = "GOLD";
            this.isvalidid = true;
            break;

        case 200:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-PRIME(G)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-PRIME(G)";
            this.varianttype = "GOLD";
            this.isvalidid = true;
            break;

        case 201:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAWK;
            this.shortname = "WHK-PRIME(G)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAWK WHK-PRIME(G)";
            this.varianttype = "GOLD";
            this.isvalidid = true;
            break;

        case 202:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINTARO;
            this.shortname = "KTO-18(C)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "KINTARO KTO-18(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 203:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SPIDER;
            this.shortname = "SDR-A";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ANANSI";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 204:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-1GHE";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HELLSLINGER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 205:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VINDICATOR;
            this.shortname = "VND-1SIB";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ST. IVES' BLUES";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 206:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VINDICATOR;
            this.shortname = "VND-1R";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VINDICATOR VND-1R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 207:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VINDICATOR;
            this.shortname = "VND-1AA";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VINDICATOR VND-1AA";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 208:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VINDICATOR;
            this.shortname = "VND-1X";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VINDICATOR VND-1X";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 209:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.QUICKDRAW;
            this.shortname = "QKD-4G(C)";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "QUICKDRAW QKD-4G(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 210:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-1E";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SPARKY";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 211:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADDOG;
            this.shortname = "MDD-PRIME(I)";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MAD DOG MDD-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 212:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADDOG;
            this.shortname = "MDD-PRIME";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MAD DOG MDD-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 213:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADDOG;
            this.shortname = "MDD-A";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MAD DOG MDD-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 214:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADDOG;
            this.shortname = "MDD-B";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MAD DOG MDD-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 215:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.TREBUCHET;
            this.shortname = "TBT-7M(C)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "TREBUCHET TBT-7M(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 216:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-S(L)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-S(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 217:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-9SE(C)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-9SE(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 218:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ICEFERRET;
            this.shortname = "IFR-PRIME(I)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ICE FERRET IFR-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 219:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ICEFERRET;
            this.shortname = "IFR-PRIME";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ICE FERRET IFR-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 220:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ICEFERRET;
            this.shortname = "IFR-A";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ICE FERRET IFR-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 221:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ICEFERRET;
            this.shortname = "IFR-C";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ICE FERRET IFR-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 222:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-PB";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PIRATES' BANE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 223:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SHADOWHAWK;
            this.shortname = "SHD-GD";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "THE GRAY DEATH";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 224:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-PRIME(I)";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "MIST LYNX MLX-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 225:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-PRIME";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "MIST LYNX MLX-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 226:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-B";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "MIST LYNX MLX-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 227:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-C";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "MIST LYNX MLX-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 228:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RAVEN;
            this.shortname = "RVN-3L(C)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "RAVEN RVN-3L(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 229:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLBRINGER;
            this.shortname = "HBR-PRIME(I)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLBRINGER HBR-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 230:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLBRINGER;
            this.shortname = "HBR-PRIME";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLBRINGER HBR-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 231:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLBRINGER;
            this.shortname = "HBR-A";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLBRINGER HBR-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 232:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLBRINGER;
            this.shortname = "HBR-B";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLBRINGER HBR-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 233:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.GARGOYLE;
            this.shortname = "GAR-PRIME(I)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "GARGOYLE GAR-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 234:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.GARGOYLE;
            this.shortname = "GAR-PRIME";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "GARGOYLE GAR-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 235:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.GARGOYLE;
            this.shortname = "GAR-A";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "GARGOYLE GAR-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 236:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.GARGOYLE;
            this.shortname = "GAR-D";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "GARGOYLE GAR-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 237:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINGCRAB;
            this.shortname = "KGC-000(L)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KING CRAB KGC-000(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 238:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINGCRAB;
            this.shortname = "KGC-000";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KING CRAB KGC-000";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 239:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINGCRAB;
            this.shortname = "KGC-0000";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KING CRAB KGC-0000";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 240:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINGCRAB;
            this.shortname = "KGC-000B";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KING CRAB KGC-000B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 241:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-S";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 242:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CENTURION;
            this.shortname = "CN9-AH";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CENTURION CN9-AH";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 243:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PANTHER;
            this.shortname = "PNT-10K(R)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PANTHER PNT-10K(R)";
            this.varianttype = "RESISTANCE";
            this.isvalidid = true;
            break;

        case 244:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PANTHER;
            this.shortname = "PNT-10K";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PANTHER PNT-10K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 245:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PANTHER;
            this.shortname = "PNT-8Z";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PANTHER PNT-8Z";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 246:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PANTHER;
            this.shortname = "PNT-9R";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PANTHER PNT-9R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 247:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ENFORCER;
            this.shortname = "ENF-5D(R)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ENFORCER ENF-5D(R)";
            this.varianttype = "RESISTANCE";
            this.isvalidid = true;
            break;

        case 248:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ENFORCER;
            this.shortname = "ENF-5D";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ENFORCER ENF-5D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 249:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ENFORCER;
            this.shortname = "ENF-4R";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ENFORCER ENF-4R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 250:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ENFORCER;
            this.shortname = "ENF-5P";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ENFORCER ENF-5P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 251:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BANSHEE;
            this.shortname = "BNC-3M(C)";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BANSHEE BNC-3M(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 252:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRASSHOPPER;
            this.shortname = "GHR-5J(R)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "GRASSHOPPER GHR-5J(R)";
            this.varianttype = "RESISTANCE";
            this.isvalidid = true;
            break;

        case 253:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRASSHOPPER;
            this.shortname = "GHR-5J";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "GRASSHOPPER GHR-5J";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 254:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRASSHOPPER;
            this.shortname = "GHR-5H";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "GRASSHOPPER GHR-5H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 255:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRASSHOPPER;
            this.shortname = "GHR-5N";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "GRASSHOPPER GHR-5N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 256:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-6S(R)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ZEUS ZEU-6S(R)";
            this.varianttype = "RESISTANCE";
            this.isvalidid = true;
            break;

        case 257:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-6S";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ZEUS ZEU-6S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 258:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-6T";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ZEUS ZEU-6T";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 259:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-9S";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ZEUS ZEU-9S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 260:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ADDER;
            this.shortname = "ADR-B";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ADDER ADR-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 261:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-S";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 262:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.GARGOYLE;
            this.shortname = "GAR-C";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "GARGOYLE GAR-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 263:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ICEFERRET;
            this.shortname = "IFR-D";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ICE FERRET IFR-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 264:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KITFOX;
            this.shortname = "KFX-C";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "KIT FOX KFX-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 265:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADDOG;
            this.shortname = "MDD-C";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MAD DOG MDD-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 266:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-A";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "MIST LYNX MLX-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 267:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-A";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 268:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-A";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "STORMCROW SCR-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 269:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-C";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 270:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-D";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 271:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAWK;
            this.shortname = "WHK-C";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAWK WHK-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 272:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-1S(C)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRIFFIN GRF-1S(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 273:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLVERINE;
            this.shortname = "WVR-6K(C)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "WOLVERINE WVR-6K(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 274:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-R63(S)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "URBANMECH UM-R63(S)";
            this.varianttype = "WHO YOU COLLING SLOW?";
            this.isvalidid = true;
            break;

        case 275:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-R63";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "URBANMECH UM-R63";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 276:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-R60";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "URBANMECH UM-R60";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 277:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-R60L";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "URBANMECH UM-R60L";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 278:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAGERMECH;
            this.shortname = "JM6-A(C)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "JAGERMECH JM6-A(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 279:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EBONJAGUAR;
            this.shortname = "EBJ-PRIME(I)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "EBON JAGUAR EBJ-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 280:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EBONJAGUAR;
            this.shortname = "EBJ-PRIME";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "EBON JAGUAR EBJ-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 281:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EBONJAGUAR;
            this.shortname = "EBJ-A";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "EBON JAGUAR EBJ-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 282:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EBONJAGUAR;
            this.shortname = "EBJ-B";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "EBON JAGUAR EBJ-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 283:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EBONJAGUAR;
            this.shortname = "EBJ-C";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "EBON JAGUAR EBJ-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 284:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EXECUTIONER;
            this.shortname = "EXE-PRIME(I)";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "EXECUTIONER EXE-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 285:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EXECUTIONER;
            this.shortname = "EXE-PRIME";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "EXECUTIONER EXE-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 286:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EXECUTIONER;
            this.shortname = "EXE-A";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "EXECUTIONER EXE-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 287:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EXECUTIONER;
            this.shortname = "EXE-B";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "EXECUTIONER EXE-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 288:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EXECUTIONER;
            this.shortname = "EXE-D";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "EXECUTIONER EXE-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 289:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-5S-T";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TOP DOG";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 290:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICCHEETAH;
            this.shortname = "ACH-PRIME(I)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ARCTIC CHEETAH ACH-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 291:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICCHEETAH;
            this.shortname = "ACH-PRIME";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ARCTIC CHEETAH ACH-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 292:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICCHEETAH;
            this.shortname = "ACH-A";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ARCTIC CHEETAH ACH-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 293:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICCHEETAH;
            this.shortname = "ACH-B";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ARCTIC CHEETAH ACH-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 294:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICCHEETAH;
            this.shortname = "ACH-C";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ARCTIC CHEETAH ACH-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 295:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SHADOWCAT;
            this.shortname = "SHC-PRIME(I)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW CAT SHC-PRIME(I)";
            this.varianttype = "INVASION";
            this.isvalidid = true;
            break;

        case 296:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SHADOWCAT;
            this.shortname = "SHC-PRIME";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW CAT SHC-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 297:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SHADOWCAT;
            this.shortname = "SHC-A";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW CAT SHC-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 298:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SHADOWCAT;
            this.shortname = "SHC-B";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW CAT SHC-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 299:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SHADOWCAT;
            this.shortname = "SHC-P";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW CAT SHC-P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 300:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-2N";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRIFFIN GRF-2N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 301:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HIGHLANDER;
            this.shortname = "HGN-732B";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER HGN-732B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 302:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-3V";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "LOCUST LCT-3V";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 303:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-W";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-W";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 304:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.GARGOYLE;
            this.shortname = "GAR-B";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "GARGOYLE GAR-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 305:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ICEFERRET;
            this.shortname = "IFR-B";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ICE FERRET IFR-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 306:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-D";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "MIST LYNX MLX-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 307:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-C";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 308:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-B";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "STORMCROW SCR-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 309:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-A";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 310:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-2C";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BATTLEMASTER BLR-2C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 311:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPHRACT;
            this.shortname = "CTF-0XP";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPHRACT CTF-0XP";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 312:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PANTHER;
            this.shortname = "PNT-10P";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PANTHER PNT-10P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 313:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ENFORCER;
            this.shortname = "ENF-4P";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ENFORCER ENF-4P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 314:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRASSHOPPER;
            this.shortname = "GHR-5P";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "GRASSHOPPER GHR-5P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 315:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-5S";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ZEUS ZEU-5S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 316:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLVERINE;
            this.shortname = "WVR-Q";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "QUARANTINE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 317:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ENFORCER;
            this.shortname = "ENF-4R(C)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ENFORCER ENF-4R(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 318:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-C(C)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-C(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 319:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKKNIGHT;
            this.shortname = "BL-6-KNT(R)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BLACK KNIGHT BL-6-KNT(R)";
            this.varianttype = "RESISTANCE II";
            this.isvalidid = true;
            break;

        case 320:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKKNIGHT;
            this.shortname = "BL-6-KNT";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BLACK KNIGHT BL-6-KNT";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 321:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKKNIGHT;
            this.shortname = "BL-6B-KNT";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BLACK KNIGHT BL-6B-KNT";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 322:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKKNIGHT;
            this.shortname = "BL-7-KNT";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BLACK KNIGHT BL-7-KNT";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 323:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKKNIGHT;
            this.shortname = "BL-7-KNT-L";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BLACK KNIGHT BL-7-KNT-L";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 324:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MAULER;
            this.shortname = "MAL-1R(R)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAULER MAL-1R(R)";
            this.varianttype = "RESISTANCE II";
            this.isvalidid = true;
            break;

        case 325:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MAULER;
            this.shortname = "MAL-1R";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAULER MAL-1R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 326:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MAULER;
            this.shortname = "MAL-MX90";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAULER MAL-MX90";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 327:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MAULER;
            this.shortname = "MAL-1P";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAULER MAL-1P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 328:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MAULER;
            this.shortname = "MAL-2P";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAULER MAL-2P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 329:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CRAB;
            this.shortname = "CRB-27(R)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CRAB CRB-27(R)";
            this.varianttype = "RESISTANCE II";
            this.isvalidid = true;
            break;

        case 330:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CRAB;
            this.shortname = "CRB-27";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CRAB CRB-27";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 331:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CRAB;
            this.shortname = "CRB-20";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CRAB CRB-20";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 332:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CRAB;
            this.shortname = "CRB-27B";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CRAB CRB-27B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 333:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CRAB;
            this.shortname = "CRB-27SL";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CRAB CRB-27SL";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 334:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLFHOUND;
            this.shortname = "WLF-2(R)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "WOLFHOUND WLF-2(R)";
            this.varianttype = "RESISTANCE II";
            this.isvalidid = true;
            break;

        case 335:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLFHOUND;
            this.shortname = "WLF-2";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "WOLFHOUND WLF-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 336:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLFHOUND;
            this.shortname = "WLF-1";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "WOLFHOUND WLF-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 337:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLFHOUND;
            this.shortname = "WLF-1A";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "WOLFHOUND WLF-1A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 338:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLFHOUND;
            this.shortname = "WLF-1B";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "WOLFHOUND WLF-1B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 339:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CICADA;
            this.shortname = "CDA-3F(L)";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CICADA CDA-3F(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 340:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLVERINE;
            this.shortname = "WVR-7D(L)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "WOLVERINE WVR-7D(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 341:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-9S2(L)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ZEUS ZEU-9S2(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 342:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-D(L)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-D(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 343:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EXECUTIONER;
            this.shortname = "EXE-C(L)";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "EXECUTIONER EXE-C(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 344:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDER;
            this.shortname = "MAD-3R(S)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MARAUDER MAD-3R(S)";
            this.varianttype = "RETURN OF AN ICON";
            this.isvalidid = true;
            break;

        case 345:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDER;
            this.shortname = "MAD-3R";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MARAUDER MAD-3R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 346:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDER;
            this.shortname = "MAD-5D";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MARAUDER MAD-5D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 347:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDER;
            this.shortname = "MAD-5M";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MARAUDER MAD-5M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 348:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDER;
            this.shortname = "MAD-BH2";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BOUNTY HUNTER II";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 349:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINGCRAB;
            this.shortname = "KGC-000B(C)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KING CRAB KGC-000B(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 350:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINGCRAB;
            this.shortname = "KGC-000B(S)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KING CRAB KGC-000B(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 351:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICCHEETAH;
            this.shortname = "ACH-PRIME(C)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ARCTIC CHEETAH ACH-PRIME(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 352:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICCHEETAH;
            this.shortname = "ACH-PRIME(S)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ARCTIC CHEETAH ACH-PRIME(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 353:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-PRIME(C)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "STORMCROW SCR-PRIME(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 354:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-PRIME(S)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "STORMCROW SCR-PRIME(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 355:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-W(C)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-W(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 356:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-W(S)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-W(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 357:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RAVEN;
            this.shortname = "RVN-3L(S)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "RAVEN RVN-3L(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 358:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-4P(S)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK HBK-4P(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 359:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-9SE(S)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-9SE(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 360:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-C(S)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-C(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 361:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.JENNERIIC;
            this.shortname = "JR7-IIC(O)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER IIC JR7-IIC(O)";
            this.varianttype = "ORIGINS IIC";
            this.isvalidid = true;
            break;

        case 362:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.JENNERIIC;
            this.shortname = "JR7-IIC";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER IIC JR7-IIC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 363:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.JENNERIIC;
            this.shortname = "JR7-IIC-2";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER IIC JR7-IIC-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 364:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.JENNERIIC;
            this.shortname = "JR7-IIC-3";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER IIC JR7-IIC-3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 365:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.JENNERIIC;
            this.shortname = "JR7-IIC-A";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER IIC JR7-IIC-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 366:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNCHBACKIIC;
            this.shortname = "HBK-IIC(O)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK IIC HBK-IIC(O)";
            this.varianttype = "ORIGINS IIC";
            this.isvalidid = true;
            break;

        case 367:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNCHBACKIIC;
            this.shortname = "HBK-IIC";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK IIC HBK-IIC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 368:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNCHBACKIIC;
            this.shortname = "HBK-IIC-A";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK IIC HBK-IIC-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 369:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNCHBACKIIC;
            this.shortname = "HBK-IIC-B";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK IIC HBK-IIC-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 370:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNCHBACKIIC;
            this.shortname = "HBK-IIC-C";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK IIC HBK-IIC-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 371:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ORIONIIC;
            this.shortname = "ON1-IIC(O)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION IIC ON1-IIC(O)";
            this.varianttype = "ORIGINS IIC";
            this.isvalidid = true;
            break;

        case 372:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ORIONIIC;
            this.shortname = "ON1-IIC";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION IIC ON1-IIC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 373:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ORIONIIC;
            this.shortname = "ON1-IIC-A";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION IIC ON1-IIC-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 374:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ORIONIIC;
            this.shortname = "ON1-IIC-B";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION IIC ON1-IIC-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 375:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ORIONIIC;
            this.shortname = "ON1-IIC-C";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION IIC ON1-IIC-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 376:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HIGHLANDERIIC;
            this.shortname = "HGN-IIC(O)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER IIC HGN-IIC(O)";
            this.varianttype = "ORIGINS IIC";
            this.isvalidid = true;
            break;

        case 377:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HIGHLANDERIIC;
            this.shortname = "HGN-IIC";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER IIC HGN-IIC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 378:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HIGHLANDERIIC;
            this.shortname = "HGN-IIC-A";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER IIC HGN-IIC-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 379:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HIGHLANDERIIC;
            this.shortname = "HGN-IIC-B";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER IIC HGN-IIC-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 380:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HIGHLANDERIIC;
            this.shortname = "HGN-IIC-C";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER IIC HGN-IIC-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 381:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-6R(S)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARHAMMER WHM-6R(S)";
            this.varianttype = "RETURN OF AN ICON";
            this.isvalidid = true;
            break;

        case 382:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-6R";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARHAMMER WHM-6R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 383:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-6D";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARHAMMER WHM-6D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 384:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-7S";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARHAMMER WHM-7S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 385:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-BW";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BLACK WIDOW";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 386:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RIFLEMAN;
            this.shortname = "RFL-3N(S)";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN RFL-3N(S)";
            this.varianttype = "RETURN OF AN ICON";
            this.isvalidid = true;
            break;

        case 387:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RIFLEMAN;
            this.shortname = "RFL-3N";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN RFL-3N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 388:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RIFLEMAN;
            this.shortname = "RFL-3C";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN RFL-3C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 389:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RIFLEMAN;
            this.shortname = "RFL-5D";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN RFL-5D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 390:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RIFLEMAN;
            this.shortname = "RFL-LK";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "LEGEND-KILLER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 391:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ARCHER;
            this.shortname = "ARC-2R(S)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ARCHER ARC-2R(S)";
            this.varianttype = "RETURN OF AN ICON";
            this.isvalidid = true;
            break;

        case 392:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ARCHER;
            this.shortname = "ARC-2R";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ARCHER ARC-2R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 393:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ARCHER;
            this.shortname = "ARC-5S";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ARCHER ARC-5S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 394:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ARCHER;
            this.shortname = "ARC-5W";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ARCHER ARC-5W";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 395:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ARCHER;
            this.shortname = "ARC-T";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TEMPEST";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 396:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-1E(C)";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "LOCUST LCT-1E(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 397:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.COMMANDO;
            this.shortname = "COM-1D(C)";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COMMANDO COM-1D(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 398:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VINDICATOR;
            this.shortname = "VND-1AA(C)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VINDICATOR VND-1AA(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 399:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.AWESOME;
            this.shortname = "AWS-9M(C)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "AWESOME AWS-9M(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 400:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-2C(C)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BATTLEMASTER BLR-2C(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 401:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-PRIME(C)";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "MIST LYNX MLX-PRIME(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 402:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SHADOWCAT;
            this.shortname = "SHC-PRIME(C)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW CAT SHC-PRIME(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 403:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EBONJAGUAR;
            this.shortname = "EBJ-PRIME(C)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "EBON JAGUAR EBJ-PRIME(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 404:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAWK;
            this.shortname = "WHK-C(C)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAWK WHK-C(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 405:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KODIAK;
            this.shortname = "KDK-1(S)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KODIAK KDK-1(S)";
            this.varianttype = "TOTEM 'MECH";
            this.isvalidid = true;
            break;

        case 406:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KODIAK;
            this.shortname = "KDK-1";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KODIAK KDK-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 407:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KODIAK;
            this.shortname = "KDK-2";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KODIAK KDK-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 408:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KODIAK;
            this.shortname = "KDK-3";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KODIAK KDK-3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 409:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KODIAK;
            this.shortname = "KDK-4";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KODIAK KDK-4";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 410:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KODIAK;
            this.shortname = "KDK-5";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KODIAK KDK-5";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 411:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KODIAK;
            this.shortname = "KDK-SB";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SPIRIT BEAR";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 412:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-1(S)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PHOENIX HAWK PXH-1(S)";
            this.varianttype = "RETURN OF AN ICON";
            this.isvalidid = true;
            break;

        case 413:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-1";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PHOENIX HAWK PXH-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 414:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-1B";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PHOENIX HAWK PXH-1B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 415:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-1K";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PHOENIX HAWK PXH-1K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 416:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-2";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PHOENIX HAWK PXH-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 417:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-3S";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PHOENIX HAWK PXH-3S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 418:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-R";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ROC";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 419:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-KK";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "KUROI KIRI";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 420:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-BB";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BUTTERBEE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 421:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VIPER;
            this.shortname = "VPR-PRIME(S)";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VIPER VPR-PRIME(S)";
            this.varianttype = "STRIKE FEAR";
            this.isvalidid = true;
            break;

        case 422:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VIPER;
            this.shortname = "VPR-PRIME";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VIPER VPR-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 423:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VIPER;
            this.shortname = "VPR-A";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VIPER VPR-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 424:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VIPER;
            this.shortname = "VPR-B";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VIPER VPR-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 425:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VIPER;
            this.shortname = "VPR-C";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VIPER VPR-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 426:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VIPER;
            this.shortname = "VPR-D";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VIPER VPR-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 427:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VIPER;
            this.shortname = "VPR-M";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "MEDUSA";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 428:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CICADA;
            this.shortname = "CDA-3F";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CICADA CDA-3F";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 429:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLVERINE;
            this.shortname = "WVR-7D";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "WOLVERINE WVR-7D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 430:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-9S2";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ZEUS ZEU-9S2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 431:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-D";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 432:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EXECUTIONER;
            this.shortname = "EXE-C";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "EXECUTIONER EXE-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 433:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CYCLOPS;
            this.shortname = "CP-11-A(S)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CYCLOPS CP-11-A(S)";
            this.varianttype = "TAKE COMMAND";
            this.isvalidid = true;
            break;

        case 434:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CYCLOPS;
            this.shortname = "CP-11-A";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CYCLOPS CP-11-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 435:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CYCLOPS;
            this.shortname = "CP-10-Q";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CYCLOPS CP-10-Q";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 436:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CYCLOPS;
            this.shortname = "CP-10-Z";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CYCLOPS CP-10-Z";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 437:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CYCLOPS;
            this.shortname = "CP-11-A-DC";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CYCLOPS CP-11-A-DC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 438:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CYCLOPS;
            this.shortname = "CP-11-P";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CYCLOPS CP-11-P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 439:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CYCLOPS;
            this.shortname = "CP-S";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SLEIPNIR";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 440:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CENTURION;
            this.shortname = "CN9-A(NCIX)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CENTURION CN9-A(NCIX)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 441:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-PRIME(S)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NIGHT GYR NTG-PRIME(S)";
            this.varianttype = "PACKS A PUNCH";
            this.isvalidid = true;
            break;

        case 442:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-PRIME";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NIGHT GYR NTG-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 443:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-A";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NIGHT GYR NTG-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 444:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-B";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NIGHT GYR NTG-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 445:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-C";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NIGHT GYR NTG-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 446:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-D";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NIGHT GYR NTG-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 447:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-JK";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "JADE KITE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 448:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PANTHER;
            this.shortname = "PNT-10K(C)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PANTHER PNT-10K(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 449:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CRAB;
            this.shortname = "CRB-27B(C)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CRAB CRB-27B(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 450:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRASSHOPPER;
            this.shortname = "GHR-5H(C)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "GRASSHOPPER GHR-5H(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 451:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-6T(C)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ZEUS ZEU-6T(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 452:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.JENNERIIC;
            this.shortname = "JR7-IIC(C)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER IIC JR7-IIC(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 453:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNCHBACKIIC;
            this.shortname = "HBK-IIC(C)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK IIC HBK-IIC(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 454:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ORIONIIC;
            this.shortname = "ON1-IIC-A(C)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ORION IIC ON1-IIC-A(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 455:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HIGHLANDERIIC;
            this.shortname = "HGN-IIC-C(C)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HIGHLANDER IIC HGN-IIC-C(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 456:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNTSMAN;
            this.shortname = "HMN-PRIME(S)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNTSMAN HMN-PRIME(S)";
            this.varianttype = "IT'S HUNTING SEASON";
            this.isvalidid = true;
            break;

        case 457:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNTSMAN;
            this.shortname = "HMN-PRIME";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNTSMAN HMN-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 458:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNTSMAN;
            this.shortname = "HMN-A";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNTSMAN HMN-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 459:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNTSMAN;
            this.shortname = "HMN-B";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNTSMAN HMN-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 460:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNTSMAN;
            this.shortname = "HMN-C";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNTSMAN HMN-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 461:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNTSMAN;
            this.shortname = "HMN-P";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNTSMAN HMN-P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 462:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNTSMAN;
            this.shortname = "HMN-PA";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PAKHET";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 463:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKJACK;
            this.shortname = "BJ-2(L)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACKJACK BJ-2(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 464:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPHRACT;
            this.shortname = "CTF-3L(L)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPHRACT CTF-3L(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 465:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-3FB(L)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-3FB(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 466:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLBRINGER;
            this.shortname = "HBR-F(L)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLBRINGER HBR-F(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 467:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-F(L)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-F(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 468:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-M(L)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-M(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 469:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.LINEBACKER;
            this.shortname = "LBK-PRIME(S)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "LINEBACKER LBK-PRIME(S)";
            this.varianttype = "LEAD THE CHARGE";
            this.isvalidid = true;
            break;

        case 470:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.LINEBACKER;
            this.shortname = "LBK-PRIME";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "LINEBACKER LBK-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 471:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.LINEBACKER;
            this.shortname = "LBK-A";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "LINEBACKER LBK-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 472:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.LINEBACKER;
            this.shortname = "LBK-B";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "LINEBACKER LBK-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 473:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.LINEBACKER;
            this.shortname = "LBK-C";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "LINEBACKER LBK-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 474:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.LINEBACKER;
            this.shortname = "LBK-D";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "LINEBACKER LBK-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 475:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.LINEBACKER;
            this.shortname = "LBK-RL";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "REDLINE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 476:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KITFOX;
            this.shortname = "KFX-PR";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PURIFIER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 477:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ADDER;
            this.shortname = "ADR-CN";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "CINDER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 478:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-BK";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BREAKER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 479:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.STORMCROW;
            this.shortname = "SCR-LC";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "LACERATOR";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 480:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-PD";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "PRIDE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 481:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-WAR";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARRANT";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 482:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAWK;
            this.shortname = "WHK-NQ";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "NANUQ";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 483:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-UV";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ULTRAVIOLET";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 484:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PANTHER;
            this.shortname = "PNT-10K(S)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PANTHER PNT-10K(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 485:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-PRIME(S)";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "MIST LYNX MLX-PRIME(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 486:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CRAB;
            this.shortname = "CRB-27B(S)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "CRAB CRB-27B(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 487:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SHADOWCAT;
            this.shortname = "SHC-PRIME(S)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW CAT SHC-PRIME(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 488:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRASSHOPPER;
            this.shortname = "GHR-5H(S)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "GRASSHOPPER GHR-5H(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 489:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EBONJAGUAR;
            this.shortname = "EBJ-PRIME(S)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "EBON JAGUAR EBJ-PRIME(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 490:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-6T(S)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ZEUS ZEU-6T(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 491:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAWK;
            this.shortname = "WHK-C(S)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAWK WHK-C(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 492:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC(S)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC(S)";
            this.varianttype = "EVOLUTION OF AN ICON";
            this.isvalidid = true;
            break;

        case 493:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 494:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC-8";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC-8";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 495:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC-A";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 496:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC-B";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 497:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC-C";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 498:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC-D";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 499:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC-SC";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SCORCH";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 500:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BUSHWACKER;
            this.shortname = "BSW-X1(S)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BUSHWACKER BSW-X1(S)";
            this.varianttype = "BLAZE A TRAIL";
            this.isvalidid = true;
            break;

        case 501:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BUSHWACKER;
            this.shortname = "BSW-X1";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BUSHWACKER BSW-X1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 502:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BUSHWACKER;
            this.shortname = "BSW-X2";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BUSHWACKER BSW-X2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 503:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BUSHWACKER;
            this.shortname = "BSW-S2";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BUSHWACKER BSW-S2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 504:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BUSHWACKER;
            this.shortname = "BSW-P1";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BUSHWACKER BSW-P1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 505:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BUSHWACKER;
            this.shortname = "BSW-P2";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BUSHWACKER BSW-P2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 506:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BUSHWACKER;
            this.shortname = "BSW-HR";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HIGH ROLLER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 507:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUPERNOVA;
            this.shortname = "SNV-1(S)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SUPERNOVA SNV-1(S)";
            this.varianttype = "DESTROYER OF WORLS";
            this.isvalidid = true;
            break;

        case 508:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUPERNOVA;
            this.shortname = "SNV-1";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SUPERNOVA SNV-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 509:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUPERNOVA;
            this.shortname = "SNV-3";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SUPERNOVA SNV-3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 510:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUPERNOVA;
            this.shortname = "SNV-A";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SUPERNOVA SNV-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 511:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUPERNOVA;
            this.shortname = "SNV-B";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SUPERNOVA SNV-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 512:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUPERNOVA;
            this.shortname = "SNV-C";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SUPERNOVA SNV-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 513:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUPERNOVA;
            this.shortname = "SNV-BR";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BOILER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 514:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKJACK;
            this.shortname = "BJ-2";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACKJACK BJ-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 515:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-3FB";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-3FB";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 516:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLBRINGER;
            this.shortname = "HBR-F";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLBRINGER HBR-F";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 517:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-F";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-F";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 518:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUMMONER;
            this.shortname = "SMN-M";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUMMONER SMN-M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 519:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ASSASSIN;
            this.shortname = "ASN-21(S)";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ASSASSIN ASN-21(S)";
            this.varianttype = "THE PROFESSIONAL";
            this.isvalidid = true;
            break;

        case 520:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ASSASSIN;
            this.shortname = "ASN-21";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ASSASSIN ASN-21";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 521:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ASSASSIN;
            this.shortname = "ASN-23";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ASSASSIN ASN-23";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 522:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ASSASSIN;
            this.shortname = "ASN-101";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ASSASSIN ASN-101";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 523:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ASSASSIN;
            this.shortname = "ASN-26";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ASSASSIN ASN-26";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 524:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ASSASSIN;
            this.shortname = "ASN-27";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ASSASSIN ASN-27";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 525:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ASSASSIN;
            this.shortname = "ASN-DD";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "DARKDEATH";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 526:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLFHOUND;
            this.shortname = "WLF-1(C)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "WOLFHOUND WLF-1(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 527:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-2(C)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PHOENIX HAWK PXH-2(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 528:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-6D(C)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARHAMMER WHM-6D(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 529:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MAULER;
            this.shortname = "MAL-MX90(C)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAULER MAL-MX90(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 530:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ADDER;
            this.shortname = "ADR-PRIME(C)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ADDER ADR-PRIME(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 531:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVA;
            this.shortname = "NVA-S(C)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "NOVA NVA-S(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 532:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLBRINGER;
            this.shortname = "HBR-F(C)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLBRINGER HBR-F(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 533:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KODIAK;
            this.shortname = "KDK-3(C)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KODIAK KDK-3(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 534:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-1A(S)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ROUGHNECK RGH-1A(S)";
            this.varianttype = "ALL WORK AN TONS OF PLAY";
            this.isvalidid = true;
            break;

        case 535:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-1A";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ROUGHNECK RGH-1A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 536:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-1B";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ROUGHNECK RGH-1B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 537:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-1C";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ROUGHNECK RGH-1C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 538:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-2A";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ROUGHNECK RGH-2A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 539:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-3A";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ROUGHNECK RGH-3A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 540:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-R";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "REAVER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 541:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAVELIN;
            this.shortname = "JVN-10N(S)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JAVELIN JVN-10N(S)";
            this.varianttype = "TIP OF THE SPEAR";
            this.isvalidid = true;
            break;

        case 542:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAVELIN;
            this.shortname = "JVN-10N";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JAVELIN JVN-10N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 543:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAVELIN;
            this.shortname = "JVN-10F";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JAVELIN JVN-10F";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 544:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAVELIN;
            this.shortname = "JVN-10P";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JAVELIN JVN-10P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 545:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAVELIN;
            this.shortname = "JVN-11A";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JAVELIN JVN-11A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 546:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAVELIN;
            this.shortname = "JVN-11B";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JAVELIN JVN-11B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 547:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAVELIN;
            this.shortname = "JVN-HT";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "HI THERE!";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 548:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-K9";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "K-9";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 549:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLFHOUND;
            this.shortname = "WLF-GR";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "GRINNER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 550:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PANTHER;
            this.shortname = "PNT-KK";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "KATANA KAT";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 551:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CRAB;
            this.shortname = "CRB-FL";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "FLORENTINE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 552:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ENFORCER;
            this.shortname = "ENF-GH";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GHILLIE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 553:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRASSHOPPER;
            this.shortname = "GHR-MJ";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MJ�LNIR";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 554:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BLACKKNIGHT;
            this.shortname = "BL-P-KNT";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "PARTISAN";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 555:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ZEUS;
            this.shortname = "ZEU-SK";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SKOKOMISH";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 556:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MAULER;
            this.shortname = "MAL-KO";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KNOCKOUT";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 557:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINGCRAB;
            this.shortname = "KGC-KJ";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KAIJU";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 558:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.UZIEL;
            this.shortname = "UZL-3S(S)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "UZIEL UZL-3S(S)";
            this.varianttype = "CIVIL WAR";
            this.isvalidid = true;
            break;

        case 559:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.UZIEL;
            this.shortname = "UZL-3S";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "UZIEL UZL-3S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 560:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.UZIEL;
            this.shortname = "UZL-2S";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "UZIEL UZL-2S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 561:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.UZIEL;
            this.shortname = "UZL-3P";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "UZIEL UZL-3P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 562:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.UZIEL;
            this.shortname = "UZL-5P";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "UZIEL UZL-5P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 563:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.UZIEL;
            this.shortname = "UZL-6P";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "UZIEL UZL-6P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 564:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.UZIEL;
            this.shortname = "UZL-BE";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BELIAL";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 565:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ANNIHILATOR;
            this.shortname = "ANH-2A(S)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ANNIHILATOR ANH-2A(S)";
            this.varianttype = "CIVIL WAR";
            this.isvalidid = true;
            break;

        case 566:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ANNIHILATOR;
            this.shortname = "ANH-2A";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ANNIHILATOR ANH-2A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 567:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ANNIHILATOR;
            this.shortname = "ANH-1A";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ANNIHILATOR ANH-1A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 568:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ANNIHILATOR;
            this.shortname = "ANH-1E";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ANNIHILATOR ANH-1E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 569:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ANNIHILATOR;
            this.shortname = "ANH-1X";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ANNIHILATOR ANH-1X";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 570:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ANNIHILATOR;
            this.shortname = "ANH-1P";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ANNIHILATOR ANH-1P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 571:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ANNIHILATOR;
            this.shortname = "ANH-MB";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MEAN BABY";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 572:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.COUGAR;
            this.shortname = "COU-PRIME(S)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COUGAR COU-PRIME(S)";
            this.varianttype = "CIVIL WAR";
            this.isvalidid = true;
            break;

        case 573:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.COUGAR;
            this.shortname = "COU-PRIME";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COUGAR COU-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 574:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.COUGAR;
            this.shortname = "COU-C";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COUGAR COU-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 575:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.COUGAR;
            this.shortname = "COU-D";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COUGAR COU-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 576:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.COUGAR;
            this.shortname = "COU-E";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COUGAR COU-E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 577:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.COUGAR;
            this.shortname = "COU-H";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "COUGAR COU-H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 578:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.COUGAR;
            this.shortname = "COU-BA";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "BLOOD ADDER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 579:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADCATMKII;
            this.shortname = "MCII-1(S)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAD CAT MK II MCII-1(S)";
            this.varianttype = "CIVIL WAR";
            this.isvalidid = true;
            break;

        case 580:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADCATMKII;
            this.shortname = "MCII-1";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAD CAT MK II MCII-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 581:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADCATMKII;
            this.shortname = "MCII-2";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAD CAT MK II MCII-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 582:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADCATMKII;
            this.shortname = "MCII-4";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAD CAT MK II MCII-4";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 583:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADCATMKII;
            this.shortname = "MCII-A";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAD CAT MK II MCII-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 584:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADCATMKII;
            this.shortname = "MCII-B";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAD CAT MK II MCII-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 585:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADCATMKII;
            this.shortname = "MCII-DS";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DEATHSTRIKE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 586:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-ED";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "EBON DRAGOON";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 587:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICCHEETAH;
            this.shortname = "ACH-SH";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "SHARD";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 588:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ICEFERRET;
            this.shortname = "IFR-RC";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "RAINBOW CROW";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 589:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SHADOWCAT;
            this.shortname = "SHC-MI";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "MISHIPESHU";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 590:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADDOG;
            this.shortname = "MDD-BA";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BANDIT";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 591:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EBONJAGUAR;
            this.shortname = "EBJ-EC";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ESPRIT DE CORPS";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 592:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLBRINGER;
            this.shortname = "HBR-VI";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "VIRAGO";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 593:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.GARGOYLE;
            this.shortname = "GAR-KW";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KIN WOLF";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 594:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EXECUTIONER;
            this.shortname = "EXE-CH";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CHERBI";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 595:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MISTLYNX;
            this.shortname = "MLX-G";
            this.maxtons = 25;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "MIST LYNX MLX-G";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 596:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICCHEETAH;
            this.shortname = "ACH-E";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ARCTIC CHEETAH ACH-E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 597:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ICEFERRET;
            this.shortname = "IFR-P";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ICE FERRET IFR-P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 598:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SHADOWCAT;
            this.shortname = "SHC-H";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW CAT SHC-H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 599:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADDOG;
            this.shortname = "MDD-H";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MAD DOG MDD-H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 600:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EBONJAGUAR;
            this.shortname = "EBJ-D";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "EBON JAGUAR EBJ-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 601:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLBRINGER;
            this.shortname = "HBR-P";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLBRINGER HBR-P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 602:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.GARGOYLE;
            this.shortname = "GAR-E";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "GARGOYLE GAR-E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 603:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.EXECUTIONER;
            this.shortname = "EXE-E";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "EXECUTIONER EXE-E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 604:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.JENNERIIC;
            this.shortname = "JR7-IIC-FY";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FURY";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 605:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HUNCHBACKIIC;
            this.shortname = "HBK-IIC-DW";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "DEATHWISH";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 606:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ORIONIIC;
            this.shortname = "ON1-IIC-SK";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SK�LL";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 607:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HIGHLANDERIIC;
            this.shortname = "HGN-IIC-KP";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KEEPER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 608:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.OSIRIS;
            this.shortname = "OSR-3D(S)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "OSIRIS OSR-3D(S)";
            this.varianttype = "CIVIL WAR ESCALATION";
            this.isvalidid = true;
            break;

        case 609:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.OSIRIS;
            this.shortname = "OSR-3D";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "OSIRIS OSR-3D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 610:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.OSIRIS;
            this.shortname = "OSR-4D";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "OSIRIS OSR-4D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 611:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.OSIRIS;
            this.shortname = "OSR-1V";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "OSIRIS OSR-1V";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 612:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.OSIRIS;
            this.shortname = "OSR-2V";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "OSIRIS OSR-2V";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 613:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.OSIRIS;
            this.shortname = "OSR-1P";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "OSIRIS OSR-1P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 614:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.OSIRIS;
            this.shortname = "OSR-SE";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "SEKHMET";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 615:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.NIGHTSTAR;
            this.shortname = "NSR-9J(S)";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "NIGHTSTAR NSR-9J(S)";
            this.varianttype = "CIVIL WAR ESCALATION";
            this.isvalidid = true;
            break;

        case 616:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.NIGHTSTAR;
            this.shortname = "NSR-9J";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "NIGHTSTAR NSR-9J";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 617:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.NIGHTSTAR;
            this.shortname = "NSR-9FC";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "NIGHTSTAR NSR-9FC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 618:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.NIGHTSTAR;
            this.shortname = "NSR-9S";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "NIGHTSTAR NSR-9S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 619:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.NIGHTSTAR;
            this.shortname = "NSR-9P";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "NIGHTSTAR NSR-9P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 620:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.NIGHTSTAR;
            this.shortname = "NSR-10P";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "NIGHTSTAR NSR-10P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 621:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.NIGHTSTAR;
            this.shortname = "NSR-WP";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WOLF PHOENIX";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 622:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICWOLF;
            this.shortname = "ACW-PRIME(S)";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ARCTIC WOLF ACW-PRIME(S)";
            this.varianttype = "CIVIL WAR ESCALATION";
            this.isvalidid = true;
            break;

        case 623:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICWOLF;
            this.shortname = "ACW-PRIME";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ARCTIC WOLF ACW-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 624:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICWOLF;
            this.shortname = "ACW-A";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ARCTIC WOLF ACW-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 625:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICWOLF;
            this.shortname = "ACW-P";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ARCTIC WOLF ACW-P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 626:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICWOLF;
            this.shortname = "ACW-1";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ARCTIC WOLF ACW-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 627:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICWOLF;
            this.shortname = "ACW-2";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ARCTIC WOLF ACW-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 628:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.ARCTICWOLF;
            this.shortname = "ACW-BK";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLOOD KIT";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 629:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVACAT;
            this.shortname = "NCT-PRIME(S)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NOVA CAT NCT-PRIME(S)";
            this.varianttype = "CIVIL WAR ESCALATION";
            this.isvalidid = true;
            break;

        case 630:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVACAT;
            this.shortname = "NCT-PRIME";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NOVA CAT NCT-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 631:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVACAT;
            this.shortname = "NCT-A";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NOVA CAT NCT-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 632:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVACAT;
            this.shortname = "NCT-B";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NOVA CAT NCT-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 633:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVACAT;
            this.shortname = "NCT-C";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NOVA CAT NCT-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 634:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVACAT;
            this.shortname = "NCT-D";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NOVA CAT NCT-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 635:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NOVACAT;
            this.shortname = "NCT-CC";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "COBRA CAT";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 636:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THANATOS;
            this.shortname = "TNS-4S(S)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THANATOS TNS-4S(S)";
            this.varianttype = "DEATH'S CALLING CARD";
            this.isvalidid = true;
            break;

        case 637:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THANATOS;
            this.shortname = "TNS-4S";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THANATOS TNS-4S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 638:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THANATOS;
            this.shortname = "TNS-4P";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THANATOS TNS-4P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 639:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THANATOS;
            this.shortname = "TNS-5P";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THANATOS TNS-5P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 640:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THANATOS;
            this.shortname = "TNS-5S";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THANATOS TNS-5S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 641:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THANATOS;
            this.shortname = "TNS-5T";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THANATOS TNS-5T";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 642:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THANATOS;
            this.shortname = "TNS-HA";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HANGOVER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 643:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-R68(L)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "URBANMECH UM-R68(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 644:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-5M(L)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRIFFIN GRF-5M(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 645:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VICTOR;
            this.shortname = "VTR-9A1(L)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "VICTOR VTR-9A1(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 646:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KITFOX;
            this.shortname = "KFX-G(L)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "KIT FOX KFX-G(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 647:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.LINEBACKER;
            this.shortname = "LBK-H(L)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "LINEBACKER LBK-H(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 648:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HELLSPAWN;
            this.shortname = "HSN-7D(S)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HELLSPAWN HSN-7D(S)";
            this.varianttype = "HELL HATH NO FURY";
            this.isvalidid = true;
            break;

        case 649:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HELLSPAWN;
            this.shortname = "HSN-7D";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HELLSPAWN HSN-7D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 650:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HELLSPAWN;
            this.shortname = "HSN-8E";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HELLSPAWN HSN-8E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 651:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HELLSPAWN;
            this.shortname = "HSN-9F";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HELLSPAWN HSN-9F";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 652:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HELLSPAWN;
            this.shortname = "HSN-7P";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HELLSPAWN HSN-7P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 653:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HELLSPAWN;
            this.shortname = "HSN-8P";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HELLSPAWN HSN-8P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 654:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HELLSPAWN;
            this.shortname = "HSN-7D2";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PARALYZER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 655:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.PIRANHA;
            this.shortname = "PIR-1(S)";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PIRANHA PIR-1(S)";
            this.varianttype = "PREPARE TO BE SHOOLED";
            this.isvalidid = true;
            break;

        case 656:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.PIRANHA;
            this.shortname = "PIR-1";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PIRANHA PIR-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 657:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.PIRANHA;
            this.shortname = "PIR-2";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PIRANHA PIR-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 658:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.PIRANHA;
            this.shortname = "PIR-3";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PIRANHA PIR-3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 659:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.PIRANHA;
            this.shortname = "PIR-A";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PIRANHA PIR-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 660:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.PIRANHA;
            this.shortname = "PIR-B";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "PIRANHA PIR-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 661:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.PIRANHA;
            this.shortname = "PIR-CI";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "CIPHER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 662:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLACKLANNER;
            this.shortname = "BKL-PRIME(S)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACK LANNER BKL-PRIME(S)";
            this.varianttype = "ATTACK IN BLACK";
            this.isvalidid = true;
            break;

        case 663:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLACKLANNER;
            this.shortname = "BKL-PRIME";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACK LANNER BKL-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 664:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLACKLANNER;
            this.shortname = "BKL-A";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACK LANNER BKL-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 665:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLACKLANNER;
            this.shortname = "BKL-C";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACK LANNER BKL-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 666:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLACKLANNER;
            this.shortname = "BKL-D";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACK LANNER BKL-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 667:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLACKLANNER;
            this.shortname = "BKL-E";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLACK LANNER BKL-E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 668:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLACKLANNER;
            this.shortname = "BKL-BL";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BELLONARIUS";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 669:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-R68";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "URBANMECH UM-R68";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 670:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-5M";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRIFFIN GRF-5M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 671:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VICTOR;
            this.shortname = "VTR-9A1";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "VICTOR VTR-9A1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 672:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.KITFOX;
            this.shortname = "KFX-G";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "KIT FOX KFX-G";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 673:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.LINEBACKER;
            this.shortname = "LBK-H";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "LINEBACKER LBK-H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 674:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUNSPIDER;
            this.shortname = "SNS-PRIME(S)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUN SPIDER SNS-PRIME(S)";
            this.varianttype = "DESERT STRIKE";
            this.isvalidid = true;
            break;

        case 675:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUNSPIDER;
            this.shortname = "SNS-PRIME";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUN SPIDER SNS-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 676:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUNSPIDER;
            this.shortname = "SNS-A";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUN SPIDER SNS-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 677:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUNSPIDER;
            this.shortname = "SNS-B";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUN SPIDER SNS-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 678:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUNSPIDER;
            this.shortname = "SNS-C";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUN SPIDER SNS-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 679:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUNSPIDER;
            this.shortname = "SNS-D";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "SUN SPIDER SNS-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 680:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUNSPIDER;
            this.shortname = "SNS-ML";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MANUL";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 681:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUNSPIDER;
            this.shortname = "SNS-VG";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "VANGUARD";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 682:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-PH";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "POWERHOUSE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 683:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FAFNIR;
            this.shortname = "FNR-5(S)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "FAFNIR FNR-5(S)";
            this.varianttype = "FEAR NOTHING";
            this.isvalidid = true;
            break;

        case 684:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FAFNIR;
            this.shortname = "FNR-5";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "FAFNIR FNR-5";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 685:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FAFNIR;
            this.shortname = "FNR-5B";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "FAFNIR FNR-5B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 686:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FAFNIR;
            this.shortname = "FNR-6U";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "FAFNIR FNR-6U";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 687:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FAFNIR;
            this.shortname = "FNR-5E";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "FAFNIR FNR-5E";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 688:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FAFNIR;
            this.shortname = "FNR-6R";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "FAFNIR FNR-6R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 689:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FAFNIR;
            this.shortname = "FNR-WR";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WRATH";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 690:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-SC";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "STREET CLEANER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 691:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FIRESTARTER;
            this.shortname = "FS9-FS";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FIRESTORM";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 692:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-AR";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ARES";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 693:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RIFLEMAN;
            this.shortname = "RFL-DB";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "DAO BREAKER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 694:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BANSHEE;
            this.shortname = "BNC-SR";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SIREN";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 695:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-KR";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KRAKEN";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 696:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADDOG;
            this.shortname = "MDD-RV";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "REVENANT";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 697:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.NIGHTSTAR;
            this.shortname = "NSR-9P(C)";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "NIGHTSTAR NSR-9P(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 698:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-1A(C)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "ROUGHNECK RGH-1A(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 699:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BUSHWACKER;
            this.shortname = "BSW-S2(C)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BUSHWACKER BSW-S2(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 700:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ASSASSIN;
            this.shortname = "ASN-101(C)";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "ASSASSIN ASN-101(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 701:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.OSIRIS;
            this.shortname = "OSR-1V(C)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "OSIRIS OSR-1V(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 702:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.UZIEL;
            this.shortname = "UZL-3P(C)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "UZIEL UZL-3P(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 703:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MADDOG;
            this.shortname = "MDD-C(C)";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MAD DOG MDD-C(C)";
            this.varianttype = "CHAMPION";
            this.isvalidid = true;
            break;

        case 704:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLOODASP;
            this.shortname = "BAS-PRIME(S)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BLOOD ASP BAS-PRIME(S)";
            this.varianttype = "COLD BLOODED";
            this.isvalidid = true;
            break;

        case 705:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLOODASP;
            this.shortname = "BAS-PRIME";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BLOOD ASP BAS-PRIME";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 706:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLOODASP;
            this.shortname = "BAS-A";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BLOOD ASP BAS-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 707:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLOODASP;
            this.shortname = "BAS-B";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BLOOD ASP BAS-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 708:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLOODASP;
            this.shortname = "BAS-C";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BLOOD ASP BAS-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 709:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLOODASP;
            this.shortname = "BAS-D";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BLOOD ASP BAS-D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 710:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.BLOODASP;
            this.shortname = "BAS-RA";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "RANCOR";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 711:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FLEA;
            this.shortname = "FLE-17(S)";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FLEA FLE-17(S)";
            this.varianttype = "SCRATCH THAT ITCH";
            this.isvalidid = true;
            break;

        case 712:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FLEA;
            this.shortname = "FLE-17";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FLEA FLE-17";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 713:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FLEA;
            this.shortname = "FLE-15";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FLEA FLE-15";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 714:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FLEA;
            this.shortname = "FLE-19";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FLEA FLE-19";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 715:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FLEA;
            this.shortname = "FLE-20";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FLEA FLE-20";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 716:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FLEA;
            this.shortname = "FLE-FA";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "FLEA FLE-FA";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 717:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.FLEA;
            this.shortname = "FLE-R5K";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "ROMEO 5000";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 718:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLFIRE;
            this.shortname = "HLF-1(S)";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLFIRE HLF-1(S)";
            this.varianttype = "UNLEASHED";
            this.isvalidid = true;
            break;

        case 719:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLFIRE;
            this.shortname = "HLF-1";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLFIRE HLF-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 720:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLFIRE;
            this.shortname = "HLF-2";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLFIRE HLF-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 721:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLFIRE;
            this.shortname = "HLF-A";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLFIRE HLF-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 722:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLFIRE;
            this.shortname = "HLF-B";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLFIRE HLF-B";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 723:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLFIRE;
            this.shortname = "HLF-C";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "HELLFIRE HLF-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 724:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.HELLFIRE;
            this.shortname = "HLF-VO";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "VOID";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 725:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VULCAN;
            this.shortname = "VL-2T(S)";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VULCAN VL-2T(S)";
            this.varianttype = "FORGED IN FIRE";
            this.isvalidid = true;
            break;

        case 726:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VULCAN;
            this.shortname = "VL-2T";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VULCAN VL-2T";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 727:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VULCAN;
            this.shortname = "VL-5T";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VULCAN VL-5T";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 728:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VULCAN;
            this.shortname = "VT-5M";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VULCAN VT-5M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 729:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VULCAN;
            this.shortname = "VT-5S";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VULCAN VT-5S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 730:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VULCAN;
            this.shortname = "VT-6M";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VULCAN VT-6M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 731:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.VULCAN;
            this.shortname = "VL-BL";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "BLOODLUST";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 732:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.INCUBUS;
            this.shortname = "INC-1(S)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "INCUBUS INC-1(S)";
            this.varianttype = "FROM THE SHADOWS";
            this.isvalidid = true;
            break;

        case 733:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.INCUBUS;
            this.shortname = "INC-1";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "INCUBUS INC-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 734:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.INCUBUS;
            this.shortname = "INC-2";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "INCUBUS INC-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 735:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.INCUBUS;
            this.shortname = "INC-3";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "INCUBUS INC-3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 736:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.INCUBUS;
            this.shortname = "INC-4";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "INCUBUS INC-4";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 737:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.INCUBUS;
            this.shortname = "INC-5";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "INCUBUS INC-5";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 738:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.INCUBUS;
            this.shortname = "INC-SA";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "SABRE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 739:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHAMPION;
            this.shortname = "CHP-1N(S)";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CHAMPION CHP-1N(S)";
            this.varianttype = "THE UNDISPUTED";
            this.isvalidid = true;
            break;

        case 740:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHAMPION;
            this.shortname = "CHP-1N";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CHAMPION CHP-1N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 741:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHAMPION;
            this.shortname = "CHP-2N";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CHAMPION CHP-2N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 742:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHAMPION;
            this.shortname = "CHP-3N";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CHAMPION CHP-3N";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 743:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHAMPION;
            this.shortname = "CHP-1N2";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CHAMPION CHP-1N2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 744:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHAMPION;
            this.shortname = "CHP-1NB";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CHAMPION CHP-1NB";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 745:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHAMPION;
            this.shortname = "CHP-INV";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "INVICTUS";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 746:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VAPOREAGLE;
            this.shortname = "VGL-1(S)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VAPOR EAGLE VGL-1(S)";
            this.varianttype = "TURN AND BURN";
            this.isvalidid = true;
            break;

        case 747:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VAPOREAGLE;
            this.shortname = "VGL-1";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VAPOR EAGLE VGL-1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 748:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VAPOREAGLE;
            this.shortname = "VGL-2";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VAPOR EAGLE VGL-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 749:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VAPOREAGLE;
            this.shortname = "VGL-3";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VAPOR EAGLE VGL-3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 750:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VAPOREAGLE;
            this.shortname = "VGL-4";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VAPOR EAGLE VGL-4";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 751:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VAPOREAGLE;
            this.shortname = "VGL-A";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VAPOR EAGLE VGL-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 752:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VAPOREAGLE;
            this.shortname = "VGL-RI";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "RIVAL";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 753:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAVELIN;
            this.shortname = "JVN-11F(L)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JAVELIN JVN-11F(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 754:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RIFLEMAN;
            this.shortname = "RFL-8D(L)";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN RFL-8D(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 755:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-4L(L)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARHAMMER WHM-4L(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 756:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-H(L)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NIGHT GYR NTG-H(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 757:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC-2(L)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC-2(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 758:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHARGER;
            this.shortname = "CGR-1A1(S)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CHARGER CGR-1A1(S)";
            this.varianttype = "EVOLUTION";
            this.isvalidid = true;
            break;

        case 759:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHARGER;
            this.shortname = "CGR-1A1";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CHARGER CGR-1A1";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 760:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHARGER;
            this.shortname = "CGR-1A5";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CHARGER CGR-1A5";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 761:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHARGER;
            this.shortname = "CGR-3K";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CHARGER CGR-3K";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 762:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CHARGER;
            this.shortname = "CGR-N7";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "NUMBER SEVEN";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 763:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HATAMOTOCHI;
            this.shortname = "HTM-27T(S)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HATAMOTO-CHI HTM-27T(S)";
            this.varianttype = "EVOLUTION";
            this.isvalidid = true;
            break;

        case 764:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HATAMOTOCHI;
            this.shortname = "HTM-27T";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HATAMOTO-CHI HTM-27T";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 765:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HATAMOTOCHI;
            this.shortname = "HTM-27W";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HATAMOTO-KU HTM-27W";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 766:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HATAMOTOCHI;
            this.shortname = "HTM-28TR";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "HATAMOTO-CHI HTM-28TR";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 767:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HATAMOTOCHI;
            this.shortname = "HTM-SG";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "SHUGO";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 768:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAMMERIIC;
            this.shortname = "WHM-IIC(S)";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAMMER IIC WHM-IIC(S)";
            this.varianttype = "SUPERIOR FIREPOWER";
            this.isvalidid = true;
            break;

        case 769:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAMMERIIC;
            this.shortname = "WHM-IIC";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAMMER IIC WHM-IIC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 770:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAMMERIIC;
            this.shortname = "WHM-IIC-2";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAMMER IIC WHM-IIC-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 771:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAMMERIIC;
            this.shortname = "WHM-IIC-3";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAMMER IIC WHM-IIC-3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 772:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAMMERIIC;
            this.shortname = "WHM-IIC-4";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAMMER IIC WHM-IIC-4";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 773:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAMMERIIC;
            this.shortname = "WHM-IIC-10";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAMMER IIC WHM-IIC-10";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 774:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAMMERIIC;
            this.shortname = "WHM-IIC-ML";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAUL";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 775:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAMMERIIC;
            this.shortname = "WHM-IIC-BL";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BLUDGEON";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 776:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JAVELIN;
            this.shortname = "JVN-11F";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JAVELIN JVN-11F";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 777:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.RIFLEMAN;
            this.shortname = "RFL-8D";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN RFL-8D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 778:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-4L";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARHAMMER WHM-4L";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 779:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-H";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NIGHT GYR NTG-H";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 780:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC-2";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 781:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.WARHAMMERIIC;
            this.shortname = "WHM-IIC-A";
            this.maxtons = 80;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "WARHAMMER IIC WHM-IIC-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 782:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CORSAIR;
            this.shortname = "COR-5R(S)";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CORSAIR COR-5R(S)";
            this.varianttype = "GIVE NO QUARTER";
            this.isvalidid = true;
            break;

        case 783:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CORSAIR;
            this.shortname = "COR-5R";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CORSAIR COR-5R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 784:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CORSAIR;
            this.shortname = "COR-6R";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CORSAIR COR-6R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 785:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CORSAIR;
            this.shortname = "COR-7A";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CORSAIR COR-7A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 786:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CORSAIR;
            this.shortname = "COR-5T";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CORSAIR COR-5T";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 787:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CORSAIR;
            this.shortname = "COR-7R";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "CORSAIR COR-7R";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 788:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CORSAIR;
            this.shortname = "COR-RA";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "RAVAGER";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 789:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CORSAIR;
            this.shortname = "COR-BR";
            this.maxtons = 95;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BROADSIDE";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 790:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDERII;
            this.shortname = "MAD-4A(S)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER II MAD-4A(S)";
            this.varianttype = "THE ULTIMATE WEAPON";
            this.isvalidid = true;
            break;

        case 791:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDERII;
            this.shortname = "MAD-4A";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER II MAD-4A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 792:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDERII;
            this.shortname = "MAD-4HP";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER II MAD-4HP";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 793:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDERII;
            this.shortname = "MAD-5A";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER II MAD-5A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 794:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDERII;
            this.shortname = "MAD-4L";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER II MAD-4L";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 795:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDERII;
            this.shortname = "MAD-6S";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER II MAD-6S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 796:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDERII;
            this.shortname = "MAD-AL";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ALPHA";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 797:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.RIFLEMANIIC;
            this.shortname = "RFL-IIC(S)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN IIC RFL-IIC(S)";
            this.varianttype = "DEADLY ACCURATE";
            this.isvalidid = true;
            break;

        case 798:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.RIFLEMANIIC;
            this.shortname = "RFL-IIC";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN IIC RFL-IIC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 799:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.RIFLEMANIIC;
            this.shortname = "RFL-IIC-2";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN IIC RFL-IIC-2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 800:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.RIFLEMANIIC;
            this.shortname = "RFL-IIC-3";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN IIC RFL-IIC-3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 801:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.RIFLEMANIIC;
            this.shortname = "RFL-IIC-4";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN IIC RFL-IIC-4";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 802:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.RIFLEMANIIC;
            this.shortname = "RFL-IIC-A";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "RIFLEMAN IIC RFL-IIC-A";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 803:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.RIFLEMANIIC;
            this.shortname = "RFL-IIC-CH";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CHIRONEX";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 804:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-C2(S)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPULT CPLT-C2(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 805:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-9D(S)";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARHAMMER WHM-9D(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 806:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDER;
            this.shortname = "MAD-9M(S)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MARAUDER MAD-9M(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 807:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINGCRAB;
            this.shortname = "KGC-001(S)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KING CRAB KGC-001(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 808:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DERVISH;
            this.shortname = "DV-6M(S)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "DERVISH DV-6M(S)";
            this.varianttype = "WARRIOR SPIRIT";
            this.isvalidid = true;
            break;

        case 809:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DERVISH;
            this.shortname = "DV-6M";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "DERVISH DV-6M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 810:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DERVISH;
            this.shortname = "DV-7D";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "DERVISH DV-7D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 811:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DERVISH;
            this.shortname = "DV-8D";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "DERVISH DV-8D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 812:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DERVISH;
            this.shortname = "DV-6MR";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "DERVISH DV-6MR";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 813:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DERVISH;
            this.shortname = "DV-7P";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "DERVISH DV-7P";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 814:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DERVISH;
            this.shortname = "DV-FR";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "FRENZY";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 815:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-C2";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPULT CPLT-C2";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 816:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WARHAMMER;
            this.shortname = "WHM-9D";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "WARHAMMER WHM-9D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 817:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MARAUDER;
            this.shortname = "MAD-9M";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "MARAUDER MAD-9M";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 818:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.KINGCRAB;
            this.shortname = "KGC-001";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "KING CRAB KGC-001";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 819:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.LOCUST;
            this.shortname = "LCT-1V(S)";
            this.maxtons = 20;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "LOCUST LCT-1V(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 820:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.SHADOWHAWK;
            this.shortname = "SHD-2H(S)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "SHADOW HAWK SHD-2H(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 821:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-5S(S)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-5S(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 822:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.BATTLEMASTER;
            this.shortname = "BLR-1G(S)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "BATTLEMASTER BLR-1G(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 823:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.GRIFFIN;
            this.shortname = "GRF-1N(S)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "GRIFFIN GRF-1N(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 824:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.WOLVERINE;
            this.shortname = "WVR-6R(S)";
            this.maxtons = 55;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "WOLVERINE WVR-6R(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 825:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-R60L(S)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "SNOWBALL";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 826:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-1K(S)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "FIREBALL";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 827:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-10SE(S)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-10SE(S)";
            this.varianttype = "STRYKER";
            this.isvalidid = true;
            break;

        case 828:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DRAGON;
            this.shortname = "DRG-1G(S)";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "GRAND DRAGON DRG-1G(S)";
            this.varianttype = "STRYKER";
            this.isvalidid = true;
            break;

        case 829:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.THUNDERBOLT;
            this.shortname = "TDR-10SE";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "THUNDERBOLT TDR-10SE";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 830:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.DRAGON;
            this.shortname = "DRG-1G";
            this.maxtons = 60;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "GRAND DRAGON DRG-1G";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 831:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-BH(S)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-BH(S)";
            this.varianttype = "WARDEN";
            this.isvalidid = true;
            break;

        case 832:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-C(S)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-C(S)";
            this.varianttype = "WARDEN";
            this.isvalidid = true;
            break;

        case 833:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.TIMBERWOLF;
            this.shortname = "TBR-BH";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "TIMBER WOLF TBR-BH";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 834:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.DIREWOLF;
            this.shortname = "DWF-C";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "DIRE WOLF DWF-C";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 835:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-7S";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PHOENIX HAWK PXH-7S";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 836:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.PHOENIXHAWK;
            this.shortname = "PXH-7S(S)";
            this.maxtons = 45;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "PHOENIX HAWK PXH-7S(S)";
            this.varianttype = "HUNTER";
            this.isvalidid = true;
            break;

        case 837:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-7D";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-7D";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 838:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.STALKER;
            this.shortname = "STK-7D(S)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "STALKER STK-7D(S)";
            this.varianttype = "HUNTER";
            this.isvalidid = true;
            break;

        case 839:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-R80";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "URBANMECH UM-R80";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 840:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.URBANMECH;
            this.shortname = "UM-R80(L)";
            this.maxtons = 30;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "URBANMECH UM-R80(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 841:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VIPER;
            this.shortname = "VPR-F";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VIPER VPR-F";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 842:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.VIPER;
            this.shortname = "VPR-F(L)";
            this.maxtons = 40;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "VIPER VPR-F(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 843:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-K3";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-K3";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 844:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-K3(L)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-K3(L)";
            this.varianttype = "LOYALTY";
            this.isvalidid = true;
            break;

        case 845:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.NIGHTGYR;
            this.shortname = "NTG-D(CS)";
            this.maxtons = 75;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "NIGHT GYR NTG-D(CS)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 846:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ROUGHNECK;
            this.shortname = "RGH-BLT";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "BOLT";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 847:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.SUNSPIDER;
            this.shortname = "SNS-AMB";
            this.maxtons = 70;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "AMBUSH";
            this.varianttype = "HERO";
            this.isvalidid = true;
            break;

        case 848:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.MAULER;
            this.shortname = "MAL-2P(S)";
            this.maxtons = 90;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MAULER MAL-2P(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 849:

            this.faction = EFaction.CLAN;
            this.chassis = EChassis.MARAUDERIIC;
            this.shortname = "MAD-IIC-A(S)";
            this.maxtons = 85;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "MARAUDER IIC MAD-IIC-A(S)";
            this.varianttype = "SPECIAL";
            this.isvalidid = true;
            break;

        case 990:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-D-DC";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-D-DC";
            this.varianttype = "STANDARD";
            this.isvalidid = true;
            break;

        case 996:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.JENNER;
            this.shortname = "JR7-D(F)";
            this.maxtons = 35;
            this.mechclass = EMechclass.LIGHT;
            this.fullname = "JENNER JR7-D(F)";
            this.varianttype = "FOUNDER";
            this.isvalidid = true;
            break;

        case 997:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.CATAPULT;
            this.shortname = "CPLT-C1(F)";
            this.maxtons = 65;
            this.mechclass = EMechclass.HEAVY;
            this.fullname = "CATAPULT CPLT-C1(F)";
            this.varianttype = "FOUNDER";
            this.isvalidid = true;
            break;

        case 998:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.HUNCHBACK;
            this.shortname = "HBK-4G(F)";
            this.maxtons = 50;
            this.mechclass = EMechclass.MEDIUM;
            this.fullname = "HUNCHBACK HBK-4G(F)";
            this.varianttype = "FOUNDER";
            this.isvalidid = true;
            break;

        case 999:

            this.faction = EFaction.INNERSPHERE;
            this.chassis = EChassis.ATLAS;
            this.shortname = "AS7-D(F)";
            this.maxtons = 100;
            this.mechclass = EMechclass.ASSAULT;
            this.fullname = "ATLAS AS7-D(F)";
            this.varianttype = "FOUNDER";
            this.isvalidid = true;
            break;


        default: this.isvalidid = false;

        	break;
		}
	}
	
	public String getFaction()
	{
		return faction.toString();
	}
	
	public String getChassis()
	{
		return chassis.toString();
	}

	public String getShortname()
	{
		return shortname;
	}
	
	public byte getMaxtons()
	{
		return maxtons;
	}

	public String getMechclass()
	{
		return mechclass.toString();
	}

	public String getFullname()
	{
		return fullname;
	}

	public String getVarianttype()
	{
		return varianttype;
	}

	public Boolean getIsvalidid()
	{
		return isvalidid;
	}

	public enum EChassis
	{
		ADDER,
		ANNIHILATOR,
		ARCHER,
		ARCTICCHEETAH,
		ARCTICWOLF,
		ASSASSIN,
		ATLAS,
		AWESOME,
		BANSHEE,
		BATTLEMASTER,
		BLACKJACK,
		BLACKKNIGHT,
		BLACKLANNER,
		BLOODASP,
		BUSHWACKER,
		CATAPHRACT,
		CATAPULT,
		CENTURION,
		CHAMPION,
		CHARGER,
		CICADA,
		COMMANDO,
		CORSAIR,
		COUGAR,
		CRAB,
		CYCLOPS,
		DERVISH,
		DIREWOLF,
		DRAGON,
		EBONJAGUAR,
		ENFORCER,
		EXECUTIONER,
		FAFNIR,
		FIRESTARTER,
		FLEA,
		GARGOYLE,
		GRASSHOPPER,
		GRIFFIN,
		HATAMOTOCHI,
		HELLBRINGER,
		HELLFIRE,
		HELLSPAWN,
		HIGHLANDER,
		HIGHLANDERIIC,
		HUNCHBACK,
		HUNCHBACKIIC,
		HUNTSMAN,
		ICEFERRET,
		INCUBUS,
		JAGERMECH,
		JAVELIN,
		JENNER,
		JENNERIIC,
		KINGCRAB,
		KINTARO,
		KITFOX,
		KODIAK,
		LINEBACKER,
		LOCUST,
		MADCATMKII,
		MADDOG,
		MARAUDER,
		MARAUDERII,
		MARAUDERIIC,
		MAULER,
		MISTLYNX,
		NIGHTGYR,
		NIGHTSTAR,
		NOVA,
		NOVACAT,
		ORION,
		ORIONIIC,
		OSIRIS,
		PANTHER,
		PHOENIXHAWK,
		PIRANHA,
		QUICKDRAW,
		RAVEN,
		RIFLEMAN,
		RIFLEMANIIC,
		ROUGHNECK,
		SHADOWCAT,
		SHADOWHAWK,
		SPIDER,
		STALKER,
		STORMCROW,
		SUMMONER,
		SUNSPIDER,
		SUPERNOVA,
		THANATOS,
		THUNDERBOLT,
		TIMBERWOLF,
		TREBUCHET,
		URBANMECH,
		UZIEL,
		VAPOREAGLE,
		VICTOR,
		VINDICATOR,
		VIPER,
		VULCAN,
		WARHAMMER,
		WARHAMMERIIC,
		WARHAWK,
		WOLFHOUND,
		WOLVERINE,
		ZEUS
	}

	public enum EMechclass
	{
		LIGHT, MEDIUM,	HEAVY,	ASSAULT
	}

	public enum EFaction
	{
		INNERSPHERE, CLAN
	}
	
}
