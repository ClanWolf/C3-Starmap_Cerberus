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

import java.util.HashMap;
import java.util.Map;

/*
  Author: KERNREAKTOR
  Version: 14-12-2021
 */
public class MapInfo {

    final Map<String, String> map = new HashMap<>();


    public MapInfo(){

        map.put("AlpinePeaks","Alpine Peaks");
        map.put("BorealReach","Boreal Reach");
        map.put("BorealVault","Boreal Vault");
        map.put("CanyonNetwork","Canyon Network");
        map.put("CapitolCity","Solaris City");
        map.put("CausticValley","Caustic Valley");
        map.put("Crater2","Rubellite Oasis");
        map.put("CrimsonStrait","Crimson Strait");
        map.put("EmeraldTaiga","Emerald Taiga");
        map.put("ForestColony","Forest Colony");
        map.put("ForestColonyClassic","Forest Colony (Classic)");
        map.put("ForestColonySnow","Forest Colony Snow (Classic)");
        map.put("FrozenCity","Frozen City");
        map.put("FrozenCityClassic","Frozen City (Classic)");
        map.put("FrozenCityNight","Frozen City Night (Classic)");
        map.put("GrimPlexus","Grim Plexus");
        map.put("GrimPortico","Grim Portico");
        map.put("HelleboreSprings","Hellebore Springs");
        map.put("HelleboreSpringsQP","Hellebore Outpost");
        map.put("HibernalRift","HIBERNAL RIFT");
        map.put("HPGManifold","HPG Manifold");
        map.put("IceLavaNetwork","Hibernal Rift");
        map.put("IshiyamaCaves","Ishiyama Caves");
        map.put("LiaoJungle","Liao Jungle");
        map.put("MechFactory","Mech Factory");
        map.put("MiningCollective","Mining Collective");
        map.put("PolarHighlands","Polar Highlands");
        map.put("PrivateTest1v1","1v1 Steiner Coliseum");
        map.put("PrivateTest2v2","2v2 Test");
        map.put("PrivateTest4v4","4v4 Test A");
        map.put("PrivateTest8ffa","4v4 Test B");
        map.put("RiverCity","River City");
        map.put("RubelliteOasis","Rubellite Oasis");
        map.put("SteinerColiseum","Steiner Coliseum");
        map.put("SulfurousRift","Sulfurous Rift");
        map.put("TerraTherma","Terra Therma");
        map.put("TourmalineDesert","Tourmaline Desert");
        map.put("ViridianBog","Viridian Bog");
        map.put("VitricForge","Vitric Forge");

    }

    public String GetMapName(String apimapname){

        return  this.map.getOrDefault(apimapname, apimapname);
    }

}
