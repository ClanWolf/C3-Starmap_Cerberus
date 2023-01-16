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
package net.clanwolf.starmap.server.reporting;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import net.clanwolf.client.mail.MailManager;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.FactionDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StarSystemDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.SysConfigDAO;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.server.process.BalanceUserInfo;
import net.clanwolf.starmap.server.process.CalcXP;
import net.clanwolf.starmap.server.process.FactionInfo;
import net.clanwolf.starmap.server.util.OSCheck;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.MatchDetails;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;
import net.clanwolf.starmap.transfer.mwo.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static net.clanwolf.starmap.constants.Constants.*;

// https://www.tutorialspoint.com/itext/itext_adding_paragraph.htm

public class GenerateRoundReport {


    public static String DEST;
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Document doc;
    private Integer team1Counter = 0;
    private Integer team2Counter = 0;
    private boolean factionTableAdded = false;
    private Table tableXPTeam1;
    private Table tableXPTeam2;
    private List xpWarning;
    private List costWarning;
    private final Long starSystemID;
    private final DecimalFormat nf = new DecimalFormat();
    private Table tableXPTeam1Header;
    private Table tableXPTeam2Header;
    private final float[] columnWidthsXP = {2, 1, 2, 2, 1, 1,};
    private final float[] columnWidthCost = {3, 1};
    private Table tableCostDefender;
    private Table tableCostAttacker;
    private final Border whiteBorder = new SolidBorder(new DeviceRgb(255, 255, 255), 0);
    private final Border blackBorder = new SolidBorder(new DeviceRgb(0, 0, 0), 1);
    public FactionPOJO factionAttacker;
    public FactionPOJO factionDefender;
    private final AttackPOJO attackPOJO;
    private int dropCounter = 1;
    private String MWOMatchID;


    public void addCostDefender(String description, Long amount) throws Exception {

        if (tableCostDefender == null) {
            team1Counter = 0;
            tableCostDefender = new Table(UnitValue.createPercentArray(columnWidthCost))
                    .setBorderBottom(whiteBorder)
                    .setBorderLeft(whiteBorder)
                    .setBorderRight(whiteBorder)
                    .setBorderTop(whiteBorder)
                    .setBorder(Border.NO_BORDER)
                    .useAllAvailableWidth()
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .addCell(addTeam1Cell("Description"))
                    .addCell(addTeam1Cell("Amount"));
            team1Counter = 1;
        }
        switch (team1Counter) {
            case -1 -> {
                team1Counter = 0;
                tableCostDefender.addCell(addTeam1Cell(description).setTextAlignment(TextAlignment.LEFT))
                        .addCell(addTeam1Cell(nf.format(amount) + " C-Bills").setTextAlignment(TextAlignment.RIGHT));
                team1Counter = team1Counter + 1;
            }
            case 1 -> {
                team1Counter = 0;
                tableCostDefender.addCell(addTeam1Cell(description))
                        .addCell(addTeam1Cell(" "));
                team1Counter = 2;
            }
            default -> {
                tableCostDefender.addCell(addTeam1Cell(description).setTextAlignment(TextAlignment.LEFT))
                        .addCell(addTeam1Cell(nf.format(amount) + " C-Bills").setTextAlignment(TextAlignment.RIGHT));
                team1Counter = team1Counter + 1;
            }
        }
    }

    public void addCostAttacker(String description, Long amount) throws Exception {

        if (tableCostAttacker == null) {
            team2Counter = 0;
            tableCostAttacker = new Table(UnitValue.createPercentArray(columnWidthCost))

                    .useAllAvailableWidth()
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .addCell(addTeam2Cell("Description"))
                    .addCell(addTeam2Cell("Amount"));
            team2Counter = 1;
        }
        switch (team2Counter) {
            case -1 -> {
                team2Counter = 0;
                tableCostAttacker.addCell(addTeam2Cell(description).setTextAlignment(TextAlignment.LEFT))
                        .addCell(addTeam2Cell(nf.format(amount) + " C-Bills").setTextAlignment(TextAlignment.RIGHT));
                team2Counter = team2Counter + 1;
            }
            case 1 -> {
                team2Counter = 0;
                tableCostAttacker.addCell(addTeam2Cell(description))
                        .addCell(addTeam2Cell(" "));
                team2Counter = 2;
            }
            default -> {
                tableCostAttacker.addCell(addTeam2Cell(description).setTextAlignment(TextAlignment.LEFT))
                        .addCell(addTeam2Cell(nf.format(amount) + " C-Bills").setTextAlignment(TextAlignment.RIGHT));
                team2Counter = team2Counter + 1;
            }
        }
    }

    public void addXPWarning(String text) {
        if (xpWarning == null) {
            xpWarning = new List();
        }
        xpWarning.add(text).setFontSize(8).setFontColor(new DeviceRgb(255, 51, 0));
    }

    public void addCostWarning(String text) {
        if (costWarning == null) {
            costWarning = new List();
        }

        costWarning.add(text).setFontSize(8).setFontColor(new DeviceRgb(255, 51, 0));
    }

    public String getFactionType(Long factionType) {
        String strType = null;
        if (factionType == 1) {
            strType = "Inner Sphere";
        } else if (factionType == 2) {
            strType = "Clan";
        } else if (factionType == 3) {
            strType = "Mercenary";
        } else if (factionType == 4) {
            strType = "Periphery";
        } else if (factionType == 5) {
            strType = "Pirate";
        } else if (factionType == 6) {
            strType = "Chaos March";
        } else if (factionType == 7) {
            strType = "ComStar";
        } else if (factionType == 8) {
            strType = "Republic of the Sphere";
        } else if (factionType == 9) {
            strType = "Neutral";
        } else if (factionType == 10) {
            strType = "Rebel";
        } else if (factionType == 11) {
            strType = "Trader";
        }

        return strType;
    }

    public GenerateRoundReport(AttackPOJO ap) throws Exception {
        String pdfFileName;
        starSystemID = ap.getStarSystemID();
        attackPOJO = ap;

        OSCheck.OSType osType = OSCheck.getOperatingSystemType();
        switch (osType) {
            case Linux -> DEST = "/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/seasonhistory/S1/Reports/";
            case Windows -> DEST = "c:\\temp\\";
        }
        StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(Nexus.END_ROUND_USERID, attackPOJO.getStarSystemID());
        pdfFileName = "C3-InvasionReport_S" + ap.getSeason() + "_R" + ap.getRound() + "_" + ssPojo.getName() + ".pdf";

        logger.info("--- Genrate PDF report for attackId: " + ap.getId());
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST + pdfFileName));

        tableXPTeam1 = new Table(UnitValue.createPercentArray(columnWidthsXP))
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        tableXPTeam2 = new Table(UnitValue.createPercentArray(columnWidthsXP))
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        tableCostDefender = new Table(UnitValue.createPercentArray(columnWidthCost))
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        doc = new Document(pdfDoc, PageSize.A4);
    }

    static boolean urlExists(java.lang.String URL) {
        java.net.URL url;

        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
            return false;
        }
        try {
            url.openStream().close();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public void saveReport() {

        doc.close();
        logger.info("✅ PDF report created");
        dropCounter = 1;
        String[] receivers = {"keshik@googlegroups.com"};
        boolean sent;

        if (!GameServer.isDevelopmentPC) {
            StarSystemPOJO planet = StarSystemDAO.getInstance().findById(Nexus.DUMMY_USERID, starSystemID);

            String subject = "PDF report created for season:" +
                    attackPOJO.getSeason() +
                    " round:" +
                    attackPOJO.getRound() +
                    " planet:" +
                    planet.getName();

            String message = "PDF report successfully created." +
                    "\r\n" +
                    "Season:" +
                    attackPOJO.getSeason() +
                    "\r\n" +
                    "Round:" +
                    attackPOJO.getRound() +
                    "\r\n" +
                    "Planet:" +
                    planet.getName() +
                    "\r\n" +
                    "File path to the report:" +
                    DEST;

            sent = MailManager.sendMail("c3@clanwolf.net", receivers, subject, message, false);

            if (sent) {
                // sent
                logger.info("Mail sent.");
            } else {
                // error during email sending
                logger.info("Error during mail dispatch.");
            }
        } else {
            logger.info("Mail was not sent out because this is a dev computer.");
        }
    }

    public void finishXPReport() throws Exception {

        createC3Header("XP award for drop " + dropCounter);
        if (!(xpWarning == null)) {
            doc.add(new Paragraph("The following players do not get XP:").setFontSize(8).setBold())
                    .add(xpWarning)
                    .add(new Paragraph());
            xpWarning = null;
        }
        doc.add(new Paragraph("Calculation for XP distribution:").setFontSize(8).setBold());

        createCalcInfoXP();

        Integer curCount = team1Counter;
        for (int i = curCount; i < 13; i++) {

            for (int j = 0; j < 6; j++) {
                tableXPTeam1.addCell(addTeam1Cell("-"));
            }
            team1Counter = team1Counter + 1;
        }
        curCount = team2Counter;
        for (int i = curCount; i < 13; i++) {

            for (int j = 0; j < 6; j++) {
                tableXPTeam2.addCell(addTeam2Cell("-"));
            }
            team2Counter = team2Counter + 1;
        }

        team1Counter = 0;
        team2Counter = 0;


        doc.add(tableXPTeam1Header)
                .add(tableXPTeam1)
                .add(new Paragraph())
                .add(tableXPTeam2Header)
                .add(tableXPTeam2);

        tableXPTeam1 = new Table(UnitValue.createPercentArray(columnWidthsXP))
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        tableXPTeam2 = new Table(UnitValue.createPercentArray(columnWidthsXP))
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
    }

    private void createCalcInfoXP() {
        List calcInfo = new List()
                .setFontSize(8)
                .add("Victory = " + XP_REWARD_VICTORY + " XP / Loss = " + XP_REWARD_LOSS + " XP loss")
                .add("Components destroyed: " + XP_REWARD_COMPONENT_DESTROYED + " XP per destroyed component")
                .add("Match-score: " + XP_REWARD_EACH_MATCH_SCORE + " XP for each reach " + XP_REWARD_EACH_MATCH_SCORE_RANGE + " match score")
                .add("Damage: " + XP_REWARD_EACH_DAMAGE + " XP for ech reach " + XP_REWARD_EACH_DAMAGE_RANGE + " damage");

        doc.add(calcInfo)
                .add(new Paragraph());
    }

    public void addXPForTeam(UserDetail userDetail, MWOMatchResult matchResult, RolePlayCharacterPOJO currentCharacter) throws Exception {
        CalcXP calcXP = new CalcXP(userDetail, matchResult, currentCharacter);


        if (Objects.equals(userDetail.getTeam(), "1")) {
            if (team1Counter == 0) {

                tableXPTeam1
                        .addCell(addTeam1Cell("Username"))
                        .addCell(addTeam1Cell("Loss/Victory"))
                        .addCell(addTeam1Cell("Components destroyed"))
                        .addCell(addTeam1Cell("Match-score"))
                        .addCell(addTeam1Cell("Damage"))
                        .addCell(addTeam1Cell("Earned XP"));

                team1Counter = 1;
            }

            tableXPTeam1
                    .addCell(addTeam1Cell(calcXP.getUserName()))
                    .addCell(addTeam1Cell(calcXP.getUserXPVictoryLoss().toString() + " XP"))
                    .addCell(addTeam1Cell(calcXP.getDescriptionComponentDestroyed()))
                    .addCell(addTeam1Cell(calcXP.getDescriptionMatchScore()))
                    .addCell(addTeam1Cell(calcXP.getDescriptionDamage()))
                    .addCell(addTeam1Cell(calcXP.getUserXPCurrent().toString() + " XP"));
            team1Counter = team1Counter + 1;
        }
        if (Objects.equals(userDetail.getTeam(), "2")) {
            if (team2Counter == 0) {

                tableXPTeam2
                        .addCell(addTeam2Cell("Username"))
                        .addCell(addTeam2Cell("Loss/Victory"))
                        .addCell(addTeam2Cell("Components destroyed"))
                        .addCell(addTeam2Cell("Match-score"))
                        .addCell(addTeam2Cell("Damage"))
                        .addCell(addTeam2Cell("Earned XP"));

                team2Counter = 1;

            }
            tableXPTeam2
                    .addCell(addTeam2Cell(calcXP.getUserName()))
                    .addCell(addTeam2Cell(calcXP.getUserXPVictoryLoss().toString() + " XP"))
                    .addCell(addTeam2Cell(calcXP.getDescriptionComponentDestroyed()))
                    .addCell(addTeam2Cell(calcXP.getDescriptionMatchScore()))
                    .addCell(addTeam2Cell(calcXP.getDescriptionDamage()))
                    .addCell(addTeam2Cell(calcXP.getUserXPCurrent().toString() + " XP"));
            team2Counter = team2Counter + 1;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:/itextExamples/textAnnotation.pdf");
        file.getParentFile().mkdirs();
        // Creating a PdfWriter
        String dest = "C:/itextExamples/textAnnotation.pdf";
        PdfWriter writer = new PdfWriter(dest);

        // Creating a PdfDocument
        PdfDocument pdf = new PdfDocument(writer);

        // Creating a Document
        Document document = new Document(pdf);


        Table t = new Table(new float[]{1, 1}).useAllAvailableWidth();
        Table tableDef = new Table(new float[]{1, 1}).useAllAvailableWidth();
        Table tableAtt = new Table(new float[]{1, 1}).useAllAvailableWidth();
        //t.addCell(cell1);

        // Cell attCell = new Cell();
        //Cell defCell = new Cell();


        Cell test = new Cell()
                .add(tableAtt);

        t.addCell(test);
        test = new Cell()
                .add(tableDef);
        t.addCell(test);
        document.add(t);
        test = new Cell(2, 2)
                .add(new Paragraph("1"))
                .add(new Paragraph("2"))
                .add(new Paragraph("3"));
        t.addCell(test);
        document.add(t);

        // Closing the document
        document.close();

        System.out.println("Annotation added successfully");


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
                .setFontSize(5)
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(Text))
                .setBackgroundColor(new DeviceRgb(242, 242, 242))
                .setBorderBottom(greyBorder)
                .setBorderLeft(greyBorder)
                .setBorderRight(greyBorder)
                .setBorderTop(greyBorder);
    }

    protected Cell addTable(Table table) {

        return new Cell()

                .add(table)
                .setBorder(Border.NO_BORDER);
    }
    protected Cell addTeam1Cell(String Text) throws Exception {

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
                .setFontSize(5)
                .setFontColor(fontColor)
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(Text))
                .setBackgroundColor(red)
                .setBorderBottom(whiteBorder)
                .setBorderLeft(whiteBorder)
                .setBorderRight(whiteBorder)
                .setBorderTop(whiteBorder);
    }

    protected Cell addWhiteCell(List list) throws Exception {

        PdfFont f;

        f = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        return new Cell()
                .setFont(f)
                .setFontSize(8)
                .add(list)
                .setBorderBottom(whiteBorder)
                .setBorderLeft(whiteBorder)
                .setBorderRight(whiteBorder)
                .setBorderTop(whiteBorder);
    }

    protected Cell addWhiteCell(String Text) throws Exception {

        PdfFont f;

        f = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        return new Cell()
                .setFont(f)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .add(new Paragraph(Text))
                .setBorderBottom(whiteBorder)
                .setBorderLeft(whiteBorder)
                .setBorderRight(whiteBorder)
                .setBorderTop(whiteBorder);
    }

    protected Cell addWhiteCell(Image image) {


        return new Cell()
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .add(image)
                .setBorderBottom(whiteBorder)
                .setBorderLeft(whiteBorder)
                .setBorderRight(whiteBorder)
                .setBorderTop(whiteBorder);
    }

    protected Cell addTeam2Cell(String Text) throws Exception {

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
                red = new DeviceRgb(180, 198, 231);
            } else {
                red = new DeviceRgb(217, 225, 242);
            }
        }

        return new Cell()
                .setFont(f)
                .setFontSize(5)
                .setFontColor(fontColor)
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(Text))
                .setBackgroundColor(red)
                .setBorderBottom(whiteBorder)
                .setBorderLeft(whiteBorder)
                .setBorderRight(whiteBorder)
                .setBorderTop(whiteBorder);
    }

    protected void createLine(DeviceRgb color) {
        Table table = new Table(new float[1]).useAllAvailableWidth();
        table.setBorder(whiteBorder)
                .setBorderTop(new SolidBorder(color, 1));
        doc.add(table);

    }

    public Paragraph createLink(String text, String url) {
        // Creating a PdfLinkAnnotation object
        Rectangle rect = new Rectangle(0, 0);
        PdfLinkAnnotation annotation = new PdfLinkAnnotation(rect);

        // Setting action of the annotation
        PdfAction action = PdfAction.createURI(url);
        annotation.setAction(action);

        // Creating a link
        Link link = new Link(text, annotation);

        // Creating a paragraph
        Paragraph paragraph = new Paragraph("");

        // Adding link to paragraph
        paragraph.add(link.setUnderline());
        return paragraph;
    }

    public void addGameInfo(AttackStatsPOJO attackStats, MWOMatchResult matchDetails) throws Exception {

        factionAttacker = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getAttackerFactionId());
        factionDefender = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getDefenderFactionId());

        if (!factionTableAdded) {

            createC3Header("Planet information");
            createPlanetInfo();
            createSeasonHistoryMap(attackStats.getSeasonId(), attackStats.getRoundId());

            doc.add(new AreaBreak());

        } else {
            doc.add(new AreaBreak());
        }

        createC3Header("Match details for drop " + dropCounter);
        createMatchInfo(matchDetails.getMatchDetails(), attackStats);
        createFactionInfo(factionAttacker, factionDefender);
        createVictoryDefeatTable(matchDetails, factionAttacker, factionDefender);

        createTableMatchResult(matchDetails, factionAttacker, factionDefender);

        doc.add(new AreaBreak());
    }


    private void createVictoryDefeatTable(MWOMatchResult matchDetails, FactionPOJO factionAttacker, FactionPOJO factionDefender) throws Exception {
        Table tableResult = new Table(UnitValue.createPercentArray(new float[]{50, 50})).useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .addCell(addTeam2Cell(factionAttacker.getName_en()).setFontSize(20))
                .addCell(addTeam1Cell(factionDefender.getName_en()).setFontSize(20));

        doc.add(tableResult);
        tableResult = new Table(UnitValue.createPercentArray(new float[]{25, 25, 25, 25})).useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        FactionInfo factionAttackerInfo = new FactionInfo(factionAttacker.getId());
        FactionInfo factionDefenderInfo = new FactionInfo(factionDefender.getId());



        if (matchDetails.getMatchDetails().getWinningTeam().equals(factionAttackerInfo.getTeam(MWOMatchID))) {
            tableResult.addCell(addTeam2Cell("VICTORY").setFontSize(20));
        } else {

            tableResult.addCell(addTeam2Cell("DEFEAT").setFontSize(20));
        }

        tableResult.addCell(addTeam2Cell(matchDetails.getMatchDetails().getTeam2Score().toString()).setFontSize(20))
                .addCell(addTeam1Cell(matchDetails.getMatchDetails().getTeam1Score().toString()).setFontSize(20));

        if (matchDetails.getMatchDetails().getWinningTeam().equals(factionDefenderInfo.getTeam(MWOMatchID))) {
            tableResult.addCell(addTeam1Cell("VICTORY").setFontSize(20));
        } else {

            tableResult.addCell(addTeam1Cell("DEFEAT").setFontSize(20));
        }

        doc.add(tableResult);
        doc.add(new Paragraph(""));
    }

    private void createTableMatchResult(MWOMatchResult matchDetails, FactionPOJO factionAttacker, FactionPOJO factionDefender) throws Exception {
        tableXPTeam1Header = new Table(new float[1]).useAllAvailableWidth().addHeaderCell(addTeam1Cell(factionDefender.getName_en()));
        tableXPTeam2Header = new Table(new float[1]).useAllAvailableWidth().addHeaderCell(addTeam2Cell(factionAttacker.getName_en()));

        doc.add(new Paragraph(""));
        float[] columnWidthsFaction = {1, 4, 2, 1, 2, 1, 1, 1, 1, 1, 2};

        Table tableTeam1 = new Table(UnitValue.createPercentArray(columnWidthsFaction)).useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        team1Counter = 0;
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

        for (int i = curCount; i < 13; i++) {
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


        Table tableTeam2 = new Table(UnitValue.createPercentArray(columnWidthsFaction)).useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        team2Counter = 0;

        tableTeam2.addCell(addTeam2Cell("Unit"))
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
                tableTeam2.addCell(addTeam2Cell(userDetail.getUnitTag()))
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

        for (int i = curCount; i < 13; i++) {
            tableTeam2.addCell(addTeam2Cell("-"))
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
        team1Counter = 0;
        team2Counter = 0;

        doc.add(tableXPTeam1Header);
        doc.add(tableTeam1);
        doc.add(new Paragraph(""));
        doc.add(tableXPTeam2Header);
        doc.add(tableTeam2);
    }

    private void createMatchInfo(MatchDetails matchDetails, AttackStatsPOJO attackStats) throws Exception {
        float[] columnWidth = {1, 1, 1, 1};

        MWOMatchID = attackStats.getMwoMatchId();

        Table tableGameInfo = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth()
                .setVerticalAlignment(VerticalAlignment.BOTTOM)
                .addCell(addGreyCell("Server Region: " + matchDetails.getRegion()))
                .addCell(addGreyCell("Duration: " + matchDetails.getMatchDuration()))
                .addCell(addGreyCell("View Mode: " + matchDetails.getViewMode()))
                .addCell(addGreyCell("Game Mode: " + matchDetails.getGameMode()))
                .addCell(addGreyCell("Map Time of Day: " + matchDetails.getTimeOfDay()))

                .addCell(addGreyCell("Match time in minutes: " + matchDetails.getMatchTimeMinutes()))
                .addCell(addGreyCell("Map: " + attackStats.getMap()))
                .addCell(addGreyCell("MWO Match ID: " + attackStats.getMwoMatchId()))
                .addCell(addGreyCell("Round: " + attackStats.getRoundId()))
                .addCell(addGreyCell("Attack ID: " + attackStats.getAttackId()))
                .addCell(addGreyCell("C3 ID:" + attackStats.id));

        try {

            String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX";
            DateFormat dateFormat = new SimpleDateFormat(isoDatePattern);
            Date parsedDate = dateFormat.parse(attackStats.getDropEnded());
            tableGameInfo.addCell(addGreyCell("Drop ended: " + parsedDate));

        } catch (Exception e) {
            tableGameInfo.addCell(addGreyCell("Drop ended: " + attackStats.getDropEnded()));
            logger.error(e.getMessage());
        }

        doc.add(new Paragraph("Lobby setting:").setFontSize(8).setBold());
        doc.add(tableGameInfo);

    }

    private void createFactionInfo(FactionPOJO factionAttacker, FactionPOJO factionDefender) throws Exception {

        doc.add(new Paragraph("Information about the attacker and defender:").setFontSize(8).setBold());
        Table tableFactionInfo = new Table(new float[3])
                .addCell(addWhiteCell("Attacker").setFontSize(10))
                .addCell(addWhiteCell(""))
                .addCell(addWhiteCell("Defender").setFontSize(10));

        tableFactionInfo.setHorizontalAlignment(HorizontalAlignment.CENTER);

        String imageFactionFile = "https://www.clanwolf.net/static/images/logos/factions/";

        if (urlExists(imageFactionFile + factionAttacker.getLogo())) {
            ImageData imageFactionData = ImageDataFactory.create(new URL(imageFactionFile + factionAttacker.getLogo()));
            Image imageFactionAttacker = new Image(imageFactionData);
            imageFactionAttacker.scaleAbsolute(100, 100);
            tableFactionInfo.addCell(addWhiteCell(imageFactionAttacker)).setVerticalAlignment(VerticalAlignment.MIDDLE);
        } else {
            tableFactionInfo.addCell(addWhiteCell(factionAttacker.getShortName()));
        }

        tableFactionInfo.addCell(addWhiteCell("VS").setFontSize(45).setWidth(150));

        if (urlExists(imageFactionFile + factionDefender.getLogo())) {
            ImageData imageFactionData = ImageDataFactory.create(new URL(imageFactionFile + factionDefender.getLogo()));
            Image imageFactionDefender = new Image(imageFactionData);
            imageFactionDefender.scaleAbsolute(100, 100);
            tableFactionInfo.addCell(addWhiteCell(imageFactionDefender)).setVerticalAlignment(VerticalAlignment.MIDDLE);
        } else {
            tableFactionInfo.addCell(addWhiteCell(factionDefender.getShortName()));
        }

        List attackerInfo = new List()
                .add("Name: " + factionAttacker.getName_en())
                .add("Main system :" + factionAttacker.getMainSystem())
                .add("Faction type: " + getFactionType(factionAttacker.getFactionTypeID()));

        List defenderInfo = new List()
                .add("Name: " + factionDefender.getName_en())
                .add("Main system :" + factionDefender.getMainSystem())
                .add("Faction type: " + getFactionType(factionDefender.getFactionTypeID()));

        tableFactionInfo

                .addCell(addWhiteCell(attackerInfo))
                .addCell(addWhiteCell(""))
                .addCell(addWhiteCell(defenderInfo));
        factionTableAdded = true;

        doc.add(tableFactionInfo)
                .add(new Paragraph(""));


        //logger.info("---Faction info created---");

    }

    private void createSeasonHistoryMap(Long seasonId, Long roundId) throws MalformedURLException {
        String seasonHistoryURL = "https://www.clanwolf.net/apps/C3/seasonhistory/S" + seasonId +
                "/C3_S" + seasonId + "_R" + roundId + "_map_history.png";

        if (urlExists(seasonHistoryURL)) {
            logger.info("Download season history map: " + seasonHistoryURL);
            ImageData imgSeasonData = ImageDataFactory.create(new URL(seasonHistoryURL));
            Image imgSeason = new Image(imgSeasonData);
            imgSeason.setAutoScale(true);

            doc.add(imgSeason);
            doc.add(createLink("Link to season history map", seasonHistoryURL));
        } else {

            logger.error("Season history map not found on: " + seasonHistoryURL);
            ImageData imgSeasonData = ImageDataFactory.create("C:\\Users\\Stefan\\Downloads\\Bilder\\Wallpaper\\C3_S1_R86_map_history.png");
            Image imSeason = new Image(imgSeasonData);
            imSeason.setAutoScale(true);
            doc.add(imSeason);
            doc.add(createLink("Link to season history map", seasonHistoryURL));
        }
    }

    private String getPlanetImg(StarSystemPOJO starSystemPOJO){
        String linkImgPlanet = "https://www.clanwolf.net/apps/C3/static/planets/";

        String formatted = String.format("%03d", Integer.valueOf(starSystemPOJO.getSystemImageName()));
        return linkImgPlanet + formatted + ".png";

    }
    private void createPlanetInfo() throws Exception {
        doc.add(new Paragraph("Information about the planet being attacked:").setFontSize(8).setBold());
        StarSystemPOJO planet = StarSystemDAO.getInstance().findById(Nexus.DUMMY_USERID, starSystemID);

        List planetInfo = new List();

        planetInfo
                .setFontSize(8)
                .add("Planet name: " + planet.getName())
                .add("X and Y coordinates: X: " + planet.getX() + ", Y: " + planet.getY())
                .add("Star type: " + planet.getStarType1())
                .add("Planet ID:" + planet.getId())
                .add("Population: " + nf.format(planet.getPopulation()))
                .add("Resources: " + nf.format(planet.getResources()));

        String LinkImgPlanet = getPlanetImg(planet);

        if (urlExists(LinkImgPlanet)) {

            logger.info("Download planet image: " + LinkImgPlanet);
            ImageData imgPlanetData = ImageDataFactory.create(new URL(LinkImgPlanet));
            Image imgPlanet = new Image(imgPlanetData);

            imgPlanet.scaleAbsolute(100, 100)
                    .setHorizontalAlignment(HorizontalAlignment.LEFT);

            Table tablePlanetInfo = new Table(new float[]{1, 5});

            tablePlanetInfo.addCell(addWhiteCell(imgPlanet))
                    .addCell(addWhiteCell(planetInfo));

            doc.add(tablePlanetInfo)
                    .add(createLink("Link to Sarna.net to get more information about the planet " + planet.getName() + ".", planet.getSarnaLinkSystem()).setFontSize(8))
                    .add(new Paragraph(""));
        }else {

            logger.error("Planet image not found on: " + LinkImgPlanet);
            doc.add(addWhiteCell(planetInfo))
                    .add(createLink("Link to Sarna.net to get more information about the planet " + planet.getName() + ".", planet.getSarnaLinkSystem()).setFontSize(8))
                    .add(new Paragraph(""));

        }
    }

    private void createC3Header(String title) throws Exception {
        String c3Logo = "https://www.clanwolf.net/static/images/logos/c3_logo.png";
        SysConfigPOJO clientVersion = SysConfigDAO.getInstance().findById(Nexus.DUMMY_USERID, 2L);

        Table tableC3Header = new Table(new float[]{1, 5, 15});

        if (urlExists(c3Logo)) {
            ImageData imgC3LogoData = ImageDataFactory.create(new URL(c3Logo));
            Image imgC3Logo = new Image(imgC3LogoData);

            imgC3Logo.scaleAbsolute(35, 35);
            tableC3Header.addCell(addWhiteCell(imgC3Logo));
        }
        Cell cell = new Cell();
        cell
                .setBorder(whiteBorder)
                .add(addWhiteCell("C3 invasion report")
                        .setBold()
                        .setTextAlignment(TextAlignment.LEFT))
                .add(addWhiteCell("Created with version: " + clientVersion.getValue())
                        .setTextAlignment(TextAlignment.LEFT));
        tableC3Header
                .addCell(cell)
                .addCell(addWhiteCell("-" + title)
                        .setFontSize(10)
                        .setBorderLeft(blackBorder));

        doc.add(tableC3Header)
                .add(new Paragraph(""));

        createLine(new DeviceRgb(0, 0, 0));

        doc.add(new Paragraph(""));
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

    public void createCalcReport(java.util.List<BalanceUserInfo> attacker, java.util.List<BalanceUserInfo> defender) throws Exception {

        long defCostTotal = 0L;
        long attCostTotal = 0L;
        tableCostDefender = null;
        tableCostAttacker = null;
        for (BalanceUserInfo def : defender) {
            team1Counter = 1;
            addCostDefender("Costs and rewards for the pilot " + def.userName, 0L);
            addCostDefender(def.playerMechName.getFullName() + " repair costs (" + (100 - def.playerMechHealth) + "% to repair)", def.mechRepairCost);
            addCostDefender("Reward damage (" + def.playerDamage + " Damage gone)", def.rewardDamage);
            addCostDefender("Reward component destroyed (" + def.playerComponentDestroyed + " destroyed)", def.rewardComponentsDestroyed);
            addCostDefender("Reward kills (" + def.playerKills + " kills)", def.rewardKill);
            addCostDefender("Reward assist (" + def.playerAssist + " assists)", def.rewardAssist);
            addCostDefender("Reward Match-score (" + def.playerMatchScore + " Match-score)", def.rewardMatchScore);
            addCostDefender("Team damage (" + def.playerTeamDamage + " Team damage)", def.rewardTeamDamage);
            addCostDefender(def.rewardLossVictoryDescription, def.rewardLossVictory);
            team1Counter = -1;
            addCostDefender("Subtotal", def.subTotal);
            defCostTotal = defCostTotal + def.subTotal;
        }

        team1Counter = -1;
        addCostDefender("Total", defCostTotal);

        for (BalanceUserInfo att : attacker) {
            team2Counter = 1;
            addCostAttacker("Costs and rewards for the pilot " + att.userName, 0L);
            addCostAttacker(att.playerMechName.getFullName() + " repair costs (" + (100 - att.playerMechHealth) + "% to repair)", att.mechRepairCost);
            addCostAttacker("Reward Damage (" + att.playerDamage + " Damage gone)", att.rewardDamage);
            addCostAttacker("Reward component destroyed (" + att.playerComponentDestroyed + " destroyed)", att.rewardComponentsDestroyed);
            addCostAttacker("Reward kills (" + att.playerKills + " kills)", att.rewardKill);
            addCostAttacker("Reward assist (" + att.playerAssist + " assists)", att.rewardAssist);
            addCostAttacker("Reward Match-score (" + att.playerMatchScore + " Match-score)", att.rewardMatchScore);
            addCostAttacker("Team damage (" + att.playerTeamDamage + " Team damage)", att.rewardTeamDamage);
            addCostAttacker(att.rewardLossVictoryDescription, att.rewardLossVictory);
            team2Counter = -1;
            addCostAttacker("Subtotal", att.subTotal);
            attCostTotal = attCostTotal + att.subTotal;
        }
        team2Counter = -1;
        addCostAttacker("Total", attCostTotal);

        Table t = new Table(new float[]{1, 1}).useAllAvailableWidth()
                .setBorder(Border.NO_BORDER);

        team1Counter = 0;
        team2Counter = 0;

        t.addCell(addTeam1Cell(factionDefender.getName_en()))
                .addCell(addTeam2Cell(factionAttacker.getName_en()))
                .addCell(addTable(tableCostDefender))
                .addCell(addTable(tableCostAttacker));

        doc.add(new AreaBreak());
        createC3Header("Costs and rewards for drop " + dropCounter);
        createCalcInfoCost();
        doc.add(new Paragraph(""))
                .add(t);

        tableCostDefender = null;
        tableCostAttacker = null;
        team1Counter = 0;
        team2Counter = 0;
        //logger.info("--- Balance report finished ---");
        dropCounter = dropCounter + 1;
    }

    private void createCalcInfoCost() {
        List calcInf = new List()
                .setFontSize(8)
                .add("Victory = " + nf.format(REWARD_VICTORY) + " C-Bills / Loss = " + nf.format(REWARD_LOSS) + " C-Bills")
                .add("Damage: " + nf.format(REWARD_EACH_DAMAGE) + " C-Bills each damage done.")
                .add("Components destroyed: " + nf.format(REWARD_EACH_COMPONENT_DESTROYED) + " C-Bills each component destroyed.")
                .add("Kills: " + nf.format(REWARD_EACH_KILL) + " C-Bills each kill.")
                .add("Match-score: " + nf.format(REWARD_EACH_MACHT_SCORE) + " C-Bills each match-score.")
                .add("Team damage: " + nf.format(REWARD_EACH_TEAM_DAMAGE) + " C-Bills each team damage.")
                .add("Assist: " + nf.format(REWARD_ASSIST) + " C-Bills each assist.")
                .add("No team damage: " + nf.format(REWARD_NO_TEAM_DAMAGE) + " C-Bills if the player does not cause team damage.");

        doc.add(new Paragraph("Cost calculation:")
                        .setFontSize(8)
                        .setBold())
                .add(calcInf);
    }
}
