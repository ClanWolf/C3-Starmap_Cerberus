package net.clanwolf.starmap.client.util;/*
 *  ---------------------------------------------------------------- |
 *     ____ _____                                                    |
 *    / ___|___ /                   Communicate - Command - Control  |
 *   | |     |_ \                   MK V "Cerberus"                  |
 *   | |___ ___) |                                                   |
 *    \____|____/                                                    |
 *                                                                   |
 *  ---------------------------------------------------------------- |
 *  Info        : https://www.clanwolf.net                           |
 *  GitHub      : https://github.com/ClanWolf                        |
 *  ---------------------------------------------------------------- |
 *  Licensed under the Apache License, Version 2.0 (the "License");  |
 *  you may not use this file except in compliance with the License. |
 *  You may obtain a copy of the License at                          |
 *  http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                   |
 *  Unless required by applicable law or agreed to in writing,       |
 *  software distributed under the License is distributed on an "AS  |
 *  IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 *  express or implied. See the License for the specific language    |
 *  governing permissions and limitations under the License.         |
 *                                                                   |
 *  C3 includes libraries and source code by various authors.        |
 *  Copyright (c) 2001-2022, ClanWolf.net                            |
 *  ---------------------------------------------------------------- |
 */

/*
  Author: KERNREAKTOR
  Version: 1.0.0
 */

public class TxtProgressBar {

    int maxvalue;
    String emptybar;
    String filledbar;


    public TxtProgressBar(int maxvalue, String emptybar, String filledbar) {

        this.emptybar=emptybar;
        this.filledbar = filledbar;
        this.maxvalue = maxvalue;

    }

    public String getcurprogress(int currentvalue) {
        int curpercent = currentvalue *100 / this.maxvalue;
        StringBuilder txtbar = new StringBuilder();

        if(curpercent <10) {

            txtbar.append(String.valueOf(this.emptybar).repeat(10));

        } else {

            txtbar.append(String.valueOf(this.filledbar).repeat( curpercent / 10));
            txtbar.append(String.valueOf(this.emptybar).repeat(10 - (curpercent / 10)));

        }

        return txtbar.toString();
    }
}
