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
package net.clanwolf.starmap.server.reporting;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;


import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.FactionDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackStatsPOJO;
import net.clanwolf.starmap.server.persistence.pojos.FactionPOJO;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.MatchDetails;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;
import net.clanwolf.starmap.transfer.mwo.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.zip.InflaterInputStream;

// https://www.tutorialspoint.com/itext/itext_adding_paragraph.htm

public class GenerateRoundReport {
    public static final String DEST = "c:\\temp\\simple_table.pdf";
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
    private final Document doc = new Document(pdfDoc, PageSize.A4.rotate());
    private Integer team1Counter = 0;
    private Integer team2Counter = 0;

    public GenerateRoundReport() throws FileNotFoundException {
    }


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();



    }

    protected Boolean istGeradeZahl(Integer value) {
        boolean bool;
        bool = value % 2 == 0;
        return bool;
    }

    protected Cell addGreyCell(String Text) throws Exception {
        Border greyBorder = new SolidBorder(new DeviceRgb(166, 166, 166), 1);
        PdfFont f = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        return new Cell()
                .setFont(f)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(Text))
                .setBackgroundColor(new DeviceRgb(242, 242, 242))
                .setBorderBottom(greyBorder)
                .setBorderLeft(greyBorder)
                .setBorderRight(greyBorder)
                .setBorderTop(greyBorder);
    }

    protected Cell addTeam1Cell(String Text) throws Exception {
        Border whiteBorder = new SolidBorder(new DeviceRgb(255, 255, 255), 1);
        PdfFont f;

        DeviceRgb red;
        DeviceRgb fontColor;

        if (team1Counter == 0) {
            red = new DeviceRgb(237, 125, 49);
            fontColor = new DeviceRgb(255, 255, 255);
            f = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } else {
            f = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            fontColor = new DeviceRgb(0, 0, 0);
            if (istGeradeZahl(team1Counter)) {
                red = new DeviceRgb(248, 203, 173);
            } else {
                red = new DeviceRgb(252, 228, 214);
            }
        }

        return new Cell()
                .setFont(f)
                .setFontSize(9)
                .setFontColor(fontColor)
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(Text))
                .setBackgroundColor(red)
                .setBorderBottom(whiteBorder)
                .setBorderLeft(whiteBorder)
                .setBorderRight(whiteBorder)
                .setBorderTop(whiteBorder);
    }

    protected Cell addTeam2Cell(String Text) throws Exception {
        Border whiteBorder = new SolidBorder(new DeviceRgb(255, 255, 255), 1);
        PdfFont f;

        DeviceRgb red;
        DeviceRgb fontColor;

        if (team2Counter == 0) {
            red = new DeviceRgb(68, 114, 196);
            fontColor = new DeviceRgb(255, 255, 255);
            f = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } else {
            f = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            fontColor = new DeviceRgb(0, 0, 0);
            if (istGeradeZahl(team2Counter)) {
                red = new DeviceRgb(217, 225, 242);
            } else {
                red = new DeviceRgb(180, 198, 231);
            }
        }

        return new Cell()
                .setFont(f)
                .setFontSize(9)
                .setFontColor(fontColor)
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(Text))
                .setBackgroundColor(red)
                .setBorderBottom(whiteBorder)
                .setBorderLeft(whiteBorder)
                .setBorderRight(whiteBorder)
                .setBorderTop(whiteBorder);
    }

    public void addGameInfo(AttackStatsPOJO attackStats, MWOMatchResult matchDetails) throws Exception {

        Table tableGameInfo = new Table(new float[5]).useAllAvailableWidth();
        Table tableHeaderInfo = new Table(new float []{2}).useAllAvailableWidth()
                .addCell(addGreyCell("Battle report created with the C3 client version: "));

        doc.add(tableHeaderInfo);
        doc.add(new Paragraph("Test").setTextAlignment(TextAlignment.CENTER));
        AreaBreak aB = new AreaBreak();
        doc.add(aB);

	    // Creating an ImageData object
	    FactionPOJO factionOfCurrentChar = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getAttackerFactionId());
	    String imageFactionFile = "/images/logos/factions/" + factionOfCurrentChar.getLogo();
	    ImageData imageFactionData = ImageDataFactory.create(imageFactionFile);
	    Image imageFaction = new Image(imageFactionData);
	    doc.add(imageFaction);

        try {

            String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX";
            DateFormat dateFormat = new SimpleDateFormat(isoDatePattern);
            Date parsedDate = dateFormat.parse(attackStats.getDropEnded());
            tableGameInfo.addCell(addGreyCell("Drop ended: " + parsedDate));

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        MatchDetails md = matchDetails.getMatchDetails();

        tableGameInfo.addCell(addGreyCell("Server Region: " + md.getRegion()));
        tableGameInfo.addCell(addGreyCell("Duration: " + md.getMatchDuration()));
        tableGameInfo.addCell(addGreyCell("View Mode: " + md.getViewMode()));
        tableGameInfo.addCell(addGreyCell("Game Mode: " + md.getGameMode()));

        tableGameInfo.addCell(addGreyCell("Map: " + attackStats.getMap()));
        tableGameInfo.addCell(addGreyCell("Map Time of Day: " + md.getTimeOfDay()));
        tableGameInfo.addCell(addGreyCell("MWO Match ID: " + attackStats.getMwoMatchId()));
        tableGameInfo.addCell(addGreyCell("Round ID: " + attackStats.getRoundId()));
        tableGameInfo.addCell(addGreyCell("Attack ID: " + attackStats.getAttackId()));

        doc.add(tableGameInfo);
        doc.add(new Paragraph("\r\n"));
        float[] columnWidths = {1, 4, 2, 1, 2, 1, 1, 1, 1, 1, 2};


        Table tableTeam1 = new Table(UnitValue.createPercentArray(columnWidths));
        tableTeam1.setHorizontalAlignment(HorizontalAlignment.CENTER);


        tableTeam1.addCell(addTeam1Cell("Unit"))
                .addCell(addTeam1Cell("Player"))
                .addCell(addTeam1Cell("Mech"))
                .addCell(addTeam1Cell("Health"))
                .addCell(addTeam1Cell("Match-score"))
                .addCell(addTeam1Cell("Damage"))
                .addCell(addTeam1Cell("Kills"))
                .addCell(addTeam1Cell("Assist"))
                .addCell(addTeam1Cell("KM DD"))
                .addCell(addTeam1Cell("CD"))
                .addCell(addTeam1Cell("Team Damage"));


        team1Counter = team1Counter + 1;


        for (UserDetail userDetail : matchDetails.getUserDetails()) {
            if ("1".equals(userDetail.getTeam())) {
                tableTeam1.addCell(addTeam1Cell(userDetail.getUnitTag()))
                        .addCell(addTeam1Cell(userDetail.getUsername()))
                        .addCell(addTeam1Cell(new MechIdInfo(userDetail.getMechItemID()).getShortname()))
                        .addCell(addTeam1Cell(userDetail.getHealthPercentage().toString()))
                        .addCell(addTeam1Cell(userDetail.getMatchScore().toString()))
                        .addCell(addTeam1Cell(userDetail.getDamage().toString()))
                        .addCell(addTeam1Cell(userDetail.getKills().toString()))
                        .addCell(addTeam1Cell(userDetail.getAssists().toString()))
                        .addCell(addTeam1Cell(userDetail.getKillsMostDamage().toString()))
                        .addCell(addTeam1Cell(userDetail.getComponentsDestroyed().toString()))
                        .addCell(addTeam1Cell(userDetail.getTeamDamage().toString()));
                team1Counter = team1Counter + 1;
            }
        }

        Integer curCount = team1Counter;

        for (int i = curCount; i < 12; i++) {
            tableTeam1.addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"))
                    .addCell(addTeam1Cell("-"));
            team1Counter = team1Counter + 1;
        }

        tableTeam1.addCell(addTeam2Cell("Unit"))
                .addCell(addTeam2Cell("Player"))
                .addCell(addTeam2Cell("Mech"))
                .addCell(addTeam2Cell("Health"))
                .addCell(addTeam2Cell("Match-score"))
                .addCell(addTeam2Cell("Damage"))
                .addCell(addTeam2Cell("Kills"))
                .addCell(addTeam2Cell("Assist"))
                .addCell(addTeam2Cell("KM DD"))
                .addCell(addTeam2Cell("CD"))
                .addCell(addTeam2Cell("Team Damage"));

        team2Counter = team2Counter + 1;
        for (UserDetail userDetail : matchDetails.getUserDetails()) {
            if ("2".equals(userDetail.getTeam())) {
                tableTeam1.addCell(addTeam2Cell(userDetail.getUnitTag()))
                        .addCell(addTeam2Cell(userDetail.getUsername()))
                        .addCell(addTeam2Cell(new MechIdInfo(userDetail.getMechItemID()).getShortname()))
                        .addCell(addTeam2Cell(userDetail.getHealthPercentage().toString()))
                        .addCell(addTeam2Cell(userDetail.getMatchScore().toString()))
                        .addCell(addTeam2Cell(userDetail.getDamage().toString()))
                        .addCell(addTeam2Cell(userDetail.getKills().toString()))
                        .addCell(addTeam2Cell(userDetail.getAssists().toString()))
                        .addCell(addTeam2Cell(userDetail.getKillsMostDamage().toString()))
                        .addCell(addTeam2Cell(userDetail.getComponentsDestroyed().toString()))
                        .addCell(addTeam2Cell(userDetail.getTeamDamage().toString()));
                team2Counter = team2Counter + 1;
            }
        }

        curCount = team2Counter;

        for (int i = curCount; i < 12; i++) {
            tableTeam1.addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"))
                    .addCell(addTeam2Cell("-"));
            team2Counter = team2Counter + 1;
        }

        doc.add(tableTeam1);
        doc.close();
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4);

        float[] columnWidths = {1, 5, 5};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));

        PdfFont f = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        Cell cell = new Cell(1, 3)
                .add(new Paragraph("This is a header"))
                .setFont(f)
                .setFontSize(13)
                .setFontColor(DeviceGray.WHITE)
                .setBackgroundColor(DeviceGray.BLACK)
                .setTextAlignment(TextAlignment.CENTER);

        table.addHeaderCell(cell);

        for (int i = 0; i < 2; i++) {
            Cell[] headerFooter = new Cell[]{
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("#")),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Key")),
                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Value"))
            };

            for (Cell hfCell : headerFooter) {
                if (i == 0) {
                    table.addHeaderCell(hfCell);
                } else {
                    table.addFooterCell(hfCell);
                }
            }
        }

        for (int counter = 0; counter < 100; counter++) {
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(counter + 1))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("key " + (counter + 1))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("value " + (counter + 1))));
        }

        doc.add(table);

        doc.close();
    }
}
