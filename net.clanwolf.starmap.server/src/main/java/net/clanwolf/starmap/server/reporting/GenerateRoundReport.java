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
package net.clanwolf.starmap.server.reporting;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
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
import net.clanwolf.starmap.exceptions.MechItemIdNotFoundException;
import net.clanwolf.starmap.mail.MailManager;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.server.process.BalanceUserInfo;
import net.clanwolf.starmap.server.process.CalcXP;
import net.clanwolf.starmap.server.process.FactionInfo;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import net.clanwolf.starmap.server.util.OSCheck;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.MatchDetails;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;
import net.clanwolf.starmap.transfer.mwo.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

// https://www.tutorialspoint.com/itext/itext_adding_paragraph.htm

public class GenerateRoundReport {


    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static String DEST;
    private final Document doc;
    private final Long starSystemID;
    private final DecimalFormat nf = new DecimalFormat();
    private final float[] columnWidthsXP = {2, 1, 2, 1, 1, 1, 1, 1, 1};
    private final Border whiteBorder = new SolidBorder(new DeviceRgb(255, 255, 255), 0);
    private final Border blackBorder = new SolidBorder(new DeviceRgb(0, 0, 0), 1);
    private final AttackPOJO attackPOJO;
    public FactionPOJO factionAttacker;
    public FactionPOJO factionDefender;
    private Integer attackerCounter = 0;
    private Integer defenderCounter = 0;
    private boolean factionTableAdded = false;
    private Table tableXPAttacker;
    private Table tableXPDefender;
    private List xpWarning;
    private List costWarning;
    private Table tableXPAttackerHeader;
    private Table tableXPDefenderHeader;
    private int dropCounter = 1;
    private String MWOMatchID;

    public GenerateRoundReport(AttackPOJO attackPOJO) throws Exception {
        String pdfFileName;
        starSystemID = attackPOJO.getStarSystemID();
        this.attackPOJO = attackPOJO;

        OSCheck.OSType osType = OSCheck.getOperatingSystemType();
        switch (osType) {
            case Linux -> DEST = "/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/seasonhistory/S1/Reports/";
            case Windows -> DEST = "c:\\temp\\";
        }

        StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, this.attackPOJO.getStarSystemID());
        pdfFileName = "C3-InvasionReport_S" + attackPOJO.getSeason() + "_R" + attackPOJO.getRound() + "_" + ssPojo.getName() + ".pdf";

        logger.info("--- Genrate PDF report for attackId: " + attackPOJO.getId());
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST + pdfFileName));

        tableXPAttacker = new Table(UnitValue.createPercentArray(columnWidthsXP))
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        tableXPDefender = new Table(UnitValue.createPercentArray(columnWidthsXP))
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        doc = new Document(pdfDoc, PageSize.A4);
    }

    static boolean urlExists(java.lang.String URL) {
        java.net.URL url;

        try {
            url = (new URI(URL)).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
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

    private static String getMechName(UserDetail userDetail) throws ParserConfigurationException, IOException, SAXException {
        String mechName = "<UNKNOWN MECH>";
        try {
            mechName = new MechIdInfo(userDetail.getMechItemID()).getShortname();
        } catch (MechItemIdNotFoundException e) {
            logger.error("The 'Mech's name could not be determined.", e);
        }
        return mechName;
    }

    public String getDefenderTeam() {
        ArrayList<RolePlayCharacterStatsPOJO> rolePlayCharacterStatsPOJO = RolePlayCharacterStatsDAO.getInstance().findByMatchId(MWOMatchID);
        String defTeam = null;

        for (RolePlayCharacterStatsPOJO rpChar : rolePlayCharacterStatsPOJO) {
            if (rpChar.getMwoTeam() != null) {
                if (Objects.equals(factionDefender.getId(), rpChar.getRoleplayCharacterFactionId())) {
                    defTeam = String.valueOf(rpChar.getMwoTeam());
                    break;
                }
            }
        }
        return defTeam;
    }

    public String getAttackerTeam() {
        ArrayList<RolePlayCharacterStatsPOJO> rolePlayCharacterStatsPOJO = RolePlayCharacterStatsDAO.getInstance().findByMatchId(MWOMatchID);
        String attTeam = null;

        for (RolePlayCharacterStatsPOJO rpChar : rolePlayCharacterStatsPOJO) {
            if (rpChar.getMwoTeam() != null) {
                if (Objects.equals(factionAttacker.getId(), rpChar.getRoleplayCharacterFactionId())) {
                    attTeam = String.valueOf(rpChar.getMwoTeam());
                    break;
                }
            }
        }
        return attTeam;
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
        FactionTypePOJO factionTypePOJO = FactionTypeDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, factionType);
        return factionTypePOJO.getName_en() + " (" + factionTypePOJO.getShortName() + ")";
    }

    public void saveReport() {
        doc.close();
        logger.info("✅ PDF report created");
        dropCounter = 1;
        String[] receivers = {"keshik@googlegroups.com"};
        boolean sent;

        if (!GameServer.isDevelopmentPC) {
            StarSystemPOJO planet = StarSystemDAO.getInstance().findById(ServerNexus.DUMMY_USERID, starSystemID);

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

        Integer curCount = attackerCounter;
        for (int i = curCount; i < 13; i++) {

            for (int j = 0; j < 9; j++) {
                tableXPAttacker.addCell(addAttackerCell("-"));
            }
            attackerCounter = attackerCounter + 1;
        }
        curCount = defenderCounter;
        for (int i = curCount; i < 13; i++) {

            for (int j = 0; j < 9; j++) {
                tableXPDefender.addCell(addDefenderCell("-"));
            }
            defenderCounter = defenderCounter + 1;
        }

        attackerCounter = 0;
        defenderCounter = 0;

        doc.add(tableXPAttackerHeader)
                .add(tableXPAttacker)
                .add(new Paragraph())
                .add(tableXPDefenderHeader)
                .add(tableXPDefender);

        tableXPAttacker = new Table(UnitValue.createPercentArray(columnWidthsXP))
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        tableXPDefender = new Table(UnitValue.createPercentArray(columnWidthsXP))
                .useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
    }

    private void createCalcInfoXP() {
        List calcInfo = new List()
                .setFontSize(8)
                .add("Victory = " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_VICTORY").getValue() +
                        " XP | Loss = " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_LOSS").getValue() + " XP | Tie = " +
                        C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_TIE").getValue() + " XP")
                .add("Components destroyed: " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_COMPONENT_DESTROYED").getValue() + " XP per destroyed component")
                .add("Match-score: " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_MATCH_SCORE").getValue() + " XP for each reach " +
                        C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_MATCH_SCORE_RANGE").getValue() + " match score")
                .add("Damage: " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_DAMAGE").getValue() + " XP for ech reach " +
                        C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_DAMAGE_RANGE").getValue() + " damage")
                .add("Invasion involvement: " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_INVASION_INVOLVEMENT").getValue() + " XP")
                .add("Mech Controlling: Light= " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_MECH_CONTROLLING_LIGHT").getValue() + " XP | " +
                        "Medium = " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_MECH_CONTROLLING_MEDIUM").getValue() + " XP | " +
                        "Heavy = " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_MECH_CONTROLLING_HEAVY").getValue() + " XP | " +
                        "Assault = " + C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_XP_REWARD_MECH_CONTROLLING_ASSAULT").getValue() + " XP");

        doc.add(calcInfo)
                .add(new Paragraph());
    }

    public void addXPForTeam(UserDetail userDetail, MWOMatchResult matchResult, RolePlayCharacterPOJO currentCharacter) throws Exception {
        CalcXP calcXP = new CalcXP(userDetail, matchResult, currentCharacter);


        if (Objects.equals(userDetail.getTeam(), getAttackerTeam())) {
            if (attackerCounter == 0) {

                tableXPAttacker
                        .addCell(addAttackerCell("Username"))
                        .addCell(addAttackerCell("Loss/Victory"))
                        .addCell(addAttackerCell("Components destroyed"))
                        .addCell(addAttackerCell("Match-score"))
                        .addCell(addAttackerCell("Damage"))
                        .addCell(addAttackerCell("Invasion involvement"))
                        .addCell(addAttackerCell("Mech controlling"))
                        .addCell(addAttackerCell("Earned XP"))
                        .addCell(addAttackerCell("XP Total"));

                attackerCounter = 1;
            }

            tableXPAttacker
                    .addCell(addAttackerCell(calcXP.getUserName()))
                    .addCell(addAttackerCell(calcXP.getUserXPVictoryLoss().toString() + " XP"))
                    .addCell(addAttackerCell(calcXP.getDescriptionComponentDestroyed()))
                    .addCell(addAttackerCell(calcXP.getDescriptionMatchScore()))
                    .addCell(addAttackerCell(calcXP.getDescriptionDamage()))
                    .addCell(addAttackerCell(calcXP.getDescriptionInvasionInvolvement()))
                    .addCell(addAttackerCell(calcXP.getDescriptionMechControl()))
                    .addCell(addAttackerCell(calcXP.getUserXPCurrent().toString() + " XP"))
                    .addCell(addAttackerCell(calcXP.getUserXPTotal() + " XP"));
            attackerCounter = attackerCounter + 1;
        }
        if (Objects.equals(userDetail.getTeam(), getDefenderTeam())) {
            if (defenderCounter == 0) {

                tableXPDefender
                        .addCell(addDefenderCell("Username"))
                        .addCell(addDefenderCell("Loss/Victory"))
                        .addCell(addDefenderCell("Components destroyed"))
                        .addCell(addDefenderCell("Match-score"))
                        .addCell(addDefenderCell("Damage"))
                        .addCell(addDefenderCell("Invasion involvement"))
                        .addCell(addDefenderCell("Mech controlling"))
                        .addCell(addDefenderCell("Earned XP"))
                        .addCell(addDefenderCell("XP Total"));

                defenderCounter = 1;

            }
            tableXPDefender
                    .addCell(addDefenderCell(calcXP.getUserName()))
                    .addCell(addDefenderCell(calcXP.getUserXPVictoryLoss().toString() + " XP"))
                    .addCell(addDefenderCell(calcXP.getDescriptionComponentDestroyed()))
                    .addCell(addDefenderCell(calcXP.getDescriptionMatchScore()))
                    .addCell(addDefenderCell(calcXP.getDescriptionDamage()))
                    .addCell(addDefenderCell(calcXP.getDescriptionInvasionInvolvement()))
                    .addCell(addDefenderCell(calcXP.getDescriptionMechControl()))
                    .addCell(addDefenderCell(calcXP.getUserXPCurrent().toString() + " XP"))
                    .addCell(addDefenderCell(calcXP.getUserXPTotal() + " XP"));
            defenderCounter = defenderCounter + 1;
        }
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

    protected Cell addDefenderCell(String Text) throws Exception {
        PdfFont f;
        DeviceRgb red;
        DeviceRgb fontColor;

        if (defenderCounter == 0) {
            red = new DeviceRgb(237, 125, 49);
            fontColor = new DeviceRgb(255, 255, 255);
            f = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } else {
            f = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            fontColor = new DeviceRgb(0, 0, 0);
            if (istGeradeZahl(defenderCounter)) {
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

    protected Cell addAttackerCell(String Text) throws Exception {
        PdfFont f;
        DeviceRgb blue;
        DeviceRgb fontColor;

        if (attackerCounter == 0) {
            blue = new DeviceRgb(68, 114, 196);
            fontColor = new DeviceRgb(255, 255, 255);
            f = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } else {
            f = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            fontColor = new DeviceRgb(0, 0, 0);
            if (istGeradeZahl(attackerCounter)) {
                blue = new DeviceRgb(180, 198, 231);
            } else {
                blue = new DeviceRgb(217, 225, 242);
            }
        }

        return new Cell()
                .setFont(f)
                .setFontSize(5)
                .setFontColor(fontColor)
                .setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(Text))
                .setBackgroundColor(blue)
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
        factionAttacker = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, attackStats.getAttackerFactionId());
        factionDefender = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, attackStats.getDefenderFactionId());

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
                .addCell(addAttackerCell(factionAttacker.getName_en()).setFontSize(20))
                .addCell(addDefenderCell(factionDefender.getName_en()).setFontSize(20));

        doc.add(tableResult);
        tableResult = new Table(UnitValue.createPercentArray(new float[]{25, 25, 25, 25})).useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        FactionInfo factionAttackerInfo = new FactionInfo(factionAttacker.getId());
        FactionInfo factionDefenderInfo = new FactionInfo(factionDefender.getId());


        // Es kann ein Unentschieden geben im Drop, d.h. "getWinningTeam()" könnte null liefern!
        if (matchDetails.getMatchDetails().getWinningTeam() != null) {
            if (matchDetails.getMatchDetails().getWinningTeam().equals(factionAttackerInfo.getTeam(MWOMatchID))) {
                tableResult.addCell(addAttackerCell("VICTORY").setFontSize(20));
            } else {
                tableResult.addCell(addAttackerCell("DEFEAT").setFontSize(20));
            }
        } else {
            tableResult.addCell(addAttackerCell("TIE").setFontSize(20));
        }

        if (Objects.equals(factionAttackerInfo.getTeam(MWOMatchID), "1")) {
            tableResult.addCell(addAttackerCell(matchDetails.getMatchDetails().getTeam1Score().toString()).setFontSize(20))
                    .addCell(addDefenderCell(matchDetails.getMatchDetails().getTeam2Score().toString()).setFontSize(20));
        } else {
            tableResult.addCell(addAttackerCell(matchDetails.getMatchDetails().getTeam2Score().toString()).setFontSize(20))
                    .addCell(addDefenderCell(matchDetails.getMatchDetails().getTeam1Score().toString()).setFontSize(20));
        }
//        tableResult.addCell(addAttackerCell(matchDetails.getMatchDetails().getTeam2Score().toString()).setFontSize(20))
//                .addCell(addAttackerCell(matchDetails.getMatchDetails().getTeam1Score().toString()).setFontSize(20));

        // Es kann ein Unentschieden geben im Drop, d.h. "getWinningTeam()" könnte null liefern!
        if (matchDetails.getMatchDetails().getWinningTeam() != null) {
            if (matchDetails.getMatchDetails().getWinningTeam().equals(factionDefenderInfo.getTeam(MWOMatchID))) {
                tableResult.addCell(addDefenderCell("VICTORY").setFontSize(20));
            } else {
                tableResult.addCell(addDefenderCell("DEFEAT").setFontSize(20));
            }
        } else {
            tableResult.addCell(addDefenderCell("TIE").setFontSize(20));
        }

        doc.add(tableResult);
        doc.add(new Paragraph(""));
    }

    private void createTableMatchResult(MWOMatchResult matchDetails, FactionPOJO factionAttacker, FactionPOJO factionDefender) throws Exception {
        tableXPAttackerHeader = new Table(new float[1]).useAllAvailableWidth().addHeaderCell(addAttackerCell(factionAttacker.getName_en()));
        tableXPDefenderHeader = new Table(new float[1]).useAllAvailableWidth().addHeaderCell(addDefenderCell(factionDefender.getName_en()));

        doc.add(new Paragraph(""));
        float[] columnWidthsFaction = {1, 4, 2, 1, 2, 1, 1, 1, 1, 1, 2};

        Table tableTeam1 = new Table(UnitValue.createPercentArray(columnWidthsFaction)).useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        attackerCounter = 0;
        tableTeam1.addCell(addAttackerCell("Unit"))
                .addCell(addAttackerCell("Player"))
                .addCell(addAttackerCell("Mech"))
                .addCell(addAttackerCell("Health"))
                .addCell(addAttackerCell("Match-score"))
                .addCell(addAttackerCell("Damage"))
                .addCell(addAttackerCell("Kills"))
                .addCell(addAttackerCell("Assist"))
                .addCell(addAttackerCell("KM DD"))
                .addCell(addAttackerCell("CD"))
                .addCell(addAttackerCell("Team Damage"));

        attackerCounter = attackerCounter + 1;

        for (UserDetail userDetail : matchDetails.getUserDetails()) {
            if ("1".equals(userDetail.getTeam())) {
                String mechName = getMechName(userDetail);
                tableTeam1.addCell(addAttackerCell(userDetail.getUnitTag()))
                        .addCell(addAttackerCell(userDetail.getUsername()))
                        .addCell(addAttackerCell(mechName))
                        .addCell(addAttackerCell(userDetail.getHealthPercentage().toString()))
                        .addCell(addAttackerCell(userDetail.getMatchScore().toString()))
                        .addCell(addAttackerCell(userDetail.getDamage().toString()))
                        .addCell(addAttackerCell(userDetail.getKills().toString()))
                        .addCell(addAttackerCell(userDetail.getAssists().toString()))
                        .addCell(addAttackerCell(userDetail.getKillsMostDamage().toString()))
                        .addCell(addAttackerCell(userDetail.getComponentsDestroyed().toString()))
                        .addCell(addAttackerCell(userDetail.getTeamDamage().toString()));
                attackerCounter = attackerCounter + 1;
            }
        }

        Integer curCount = attackerCounter;

        for (int i = curCount; i < 13; i++) {
            tableTeam1.addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"))
                    .addCell(addAttackerCell("-"));
            attackerCounter = attackerCounter + 1;
        }


        Table tableTeam2 = new Table(UnitValue.createPercentArray(columnWidthsFaction)).useAllAvailableWidth()
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        defenderCounter = 0;

        tableTeam2.addCell(addDefenderCell("Unit"))
                .addCell(addDefenderCell("Player"))
                .addCell(addDefenderCell("Mech"))
                .addCell(addDefenderCell("Health"))
                .addCell(addDefenderCell("Match-score"))
                .addCell(addDefenderCell("Damage"))
                .addCell(addDefenderCell("Kills"))
                .addCell(addDefenderCell("Assist"))
                .addCell(addDefenderCell("KM DD"))
                .addCell(addDefenderCell("CD"))
                .addCell(addDefenderCell("Team Damage"));

        defenderCounter = defenderCounter + 1;
        for (UserDetail userDetail : matchDetails.getUserDetails()) {
            if ("2".equals(userDetail.getTeam())) {
                String mechName = getMechName(userDetail);
                tableTeam2.addCell(addDefenderCell(userDetail.getUnitTag()))
                        .addCell(addDefenderCell(userDetail.getUsername()))
                        .addCell(addDefenderCell(mechName))
                        .addCell(addDefenderCell(userDetail.getHealthPercentage().toString()))
                        .addCell(addDefenderCell(userDetail.getMatchScore().toString()))
                        .addCell(addDefenderCell(userDetail.getDamage().toString()))
                        .addCell(addDefenderCell(userDetail.getKills().toString()))
                        .addCell(addDefenderCell(userDetail.getAssists().toString()))
                        .addCell(addDefenderCell(userDetail.getKillsMostDamage().toString()))
                        .addCell(addDefenderCell(userDetail.getComponentsDestroyed().toString()))
                        .addCell(addDefenderCell(userDetail.getTeamDamage().toString()));
                defenderCounter = defenderCounter + 1;
            }
        }

        curCount = defenderCounter;

        for (int i = curCount; i < 13; i++) {
            tableTeam2.addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"))
                    .addCell(addDefenderCell("-"));
            defenderCounter = defenderCounter + 1;
        }
        attackerCounter = 0;
        defenderCounter = 0;

        doc.add(tableXPAttackerHeader);
        doc.add(tableTeam1);
        doc.add(new Paragraph(""));
        doc.add(tableXPDefenderHeader);
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
			URI uri = new URI(imageFactionFile + factionAttacker.getLogo());
            ImageData imageFactionData = ImageDataFactory.create(uri.toURL());
            Image imageFactionAttacker = new Image(imageFactionData);
            imageFactionAttacker.scaleAbsolute(100, 100);
            tableFactionInfo.addCell(addWhiteCell(imageFactionAttacker)).setVerticalAlignment(VerticalAlignment.MIDDLE);
        } else {
            tableFactionInfo.addCell(addWhiteCell(factionAttacker.getShortName()));
        }

        tableFactionInfo.addCell(addWhiteCell("VS").setFontSize(45).setWidth(150));

        if (urlExists(imageFactionFile + factionDefender.getLogo())) {
	        URI uri = new URI(imageFactionFile + factionDefender.getLogo());
            ImageData imageFactionData = ImageDataFactory.create(uri.toURL());
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

    private void createSeasonHistoryMap(Long seasonId, Long roundId) throws MalformedURLException, URISyntaxException {
        String seasonHistoryURL = "https://www.clanwolf.net/apps/C3/seasonhistory/S" + seasonId +
                "/C3_S" + seasonId + "_R" + roundId + "_map_history.png";

        if (urlExists(seasonHistoryURL)) {
            logger.info("Download season history map: " + seasonHistoryURL);
			URI uri = new URI(seasonHistoryURL);
            ImageData imgSeasonData = ImageDataFactory.create(uri.toURL());
            Image imgSeason = new Image(imgSeasonData);
            imgSeason.setAutoScale(true);

            doc.add(imgSeason);
            doc.add(createLink("Link to season history map", seasonHistoryURL));
        } else {
            logger.error("Season history map not found on: " + seasonHistoryURL);
           /* ImageData imgSeasonData = ImageDataFactory.create("C:\\Users\\Stefan\\Downloads\\Bilder\\Wallpaper\\C3_S1_R86_map_history.png");
            Image imSeason = new Image(imgSeasonData);
            imSeason.setAutoScale(true);
            doc.add(imSeason);
            doc.add(createLink("Link to season history map", seasonHistoryURL));*/
        }
    }

    private String getPlanetImg(StarSystemPOJO starSystemPOJO) {
        String linkImgPlanet = "https://www.clanwolf.net/apps/C3/static/planets/";

        String formatted = String.format("%03d", Integer.valueOf(starSystemPOJO.getSystemImageName()));
        return linkImgPlanet + formatted + ".png";

    }

    private void createPlanetInfo() throws Exception {
        doc.add(new Paragraph("Information about the planet being attacked:").setFontSize(8).setBold());
        StarSystemPOJO planet = StarSystemDAO.getInstance().findById(ServerNexus.DUMMY_USERID, starSystemID);

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
			URI uri = new URI(LinkImgPlanet);
            ImageData imgPlanetData = ImageDataFactory.create(uri.toURL());
            Image imgPlanet = new Image(imgPlanetData);

            imgPlanet.scaleAbsolute(100, 100)
                    .setHorizontalAlignment(HorizontalAlignment.LEFT);

            Table tablePlanetInfo = new Table(new float[]{1, 5});

            tablePlanetInfo.addCell(addWhiteCell(imgPlanet))
                    .addCell(addWhiteCell(planetInfo));

            doc.add(tablePlanetInfo)
                    .add(createLink("Link to Sarna.net to get more information about the planet " + planet.getName() + ".", planet.getSarnaLinkSystem()))
                    .add(new Paragraph(""));
        } else {

            logger.error("Planet image not found on: " + LinkImgPlanet);
            doc.add(addWhiteCell(planetInfo))
                    .add(createLink("Link to Sarna.net to get more information about the planet " + planet.getName() + ".", planet.getSarnaLinkSystem()))
                    .add(new Paragraph(""));

        }
    }

    private void createC3Header(String title) throws Exception {
        String c3Logo = "https://www.clanwolf.net/static/images/logos/c3_logo.png";
        SysConfigPOJO clientVersion = SysConfigDAO.getInstance().findById(ServerNexus.DUMMY_USERID, 2L);

        Table tableC3Header = new Table(new float[]{1, 5, 15});

        if (urlExists(c3Logo)) {
			URI uri = new URI(c3Logo);
            ImageData imgC3LogoData = ImageDataFactory.create(uri.toURL());
            Image imgC3Logo = new Image(imgC3LogoData);

            imgC3Logo.scaleAbsolute(30, 30);
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
                .addCell(addWhiteCell(title)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(12)
                        .setBorderLeft(blackBorder));

        doc.add(tableC3Header)
                .add(new Paragraph(""));

        createLine(new DeviceRgb(0, 0, 0));

        doc.add(new Paragraph(""));
    }

    public void createCalcReport(java.util.List<BalanceUserInfo> attacker, java.util.List<BalanceUserInfo> defender) throws Exception {

        long defCostTotal = 0L;
        long attCostTotal = 0L;

        Table defenderTable = new Table(new float[]{1}).useAllAvailableWidth()
                .setBorder(Border.NO_BORDER)
                .addCell(addDefenderCell(factionDefender.getName_en()));

        Table defenderCostTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 2, 2, 2, 2, 2, 2})).useAllAvailableWidth()
                .addCell(addDefenderCell("Username"))
                .addCell(addDefenderCell("Mech repair"))
                .addCell(addDefenderCell("Damage"))
                .addCell(addDefenderCell("Comp. destroyed"))
                .addCell(addDefenderCell("Kills"))
                .addCell(addDefenderCell("Assist"))
                .addCell(addDefenderCell("Match-score"))
                .addCell(addDefenderCell("Team Damage"))
                .addCell(addDefenderCell("Loss / Victory"))
                .addCell(addDefenderCell("Subtotal"))
                .setBorder(Border.NO_BORDER);

        defenderCounter = 1;

        for (BalanceUserInfo def : defender) {
            defenderCostTable.addCell(addDefenderCell(def.userName))
                    .addCell(addDefenderCell((100 - def.playerMechHealth) + "% to repair\r\n" + nf.format(def.mechRepairCost) + " C-Bills"))
                    .addCell(addDefenderCell(def.playerDamage + " Damage gone\r\n" + nf.format(def.rewardDamage) + " C-Bills"))
                    .addCell(addDefenderCell(def.playerComponentDestroyed + " destroyed\r\n" + nf.format(def.rewardComponentsDestroyed) + " C-Bills"))
                    .addCell(addDefenderCell(def.playerKills + " kills\r\n" + nf.format(def.rewardKill) + " C-Bills"))
                    .addCell(addDefenderCell(def.playerAssist + " assists\r\n" + nf.format(def.rewardAssist) + " C-Bills"))
                    .addCell(addDefenderCell(def.playerMatchScore + " Match-score\r\n" + nf.format(def.rewardMatchScore) + " C-Bills"))
                    .addCell(addDefenderCell(def.playerTeamDamage + " Team damage\r\n" + nf.format(def.rewardTeamDamage) + " C-Bills"))
                    .addCell(addDefenderCell(nf.format(def.rewardLossVictory) + " C-Bills"))
                    .addCell(addDefenderCell(nf.format(def.subTotal) + " C-Bills"));

//            addCostDefender("Costs and rewards for the pilot " + def.userName, 0L);
//            addCostDefender(def.playerMechName + " repair costs (" + (100 - def.playerMechHealth) + "% to repair)", def.mechRepairCost);
//            addCostDefender("Reward damage (" + def.playerDamage + " Damage gone)", def.rewardDamage);
//            addCostDefender("Reward component destroyed (" + def.playerComponentDestroyed + " destroyed)", def.rewardComponentsDestroyed);
//            addCostDefender("Reward kills (" + def.playerKills + " kills)", def.rewardKill);
//            addCostDefender("Reward assist (" + def.playerAssist + " assists)", def.rewardAssist);
//            addCostDefender("Reward Match-score (" + def.playerMatchScore + " Match-score)", def.rewardMatchScore);
//            addCostDefender("Team damage (" + def.playerTeamDamage + " Team damage)", def.rewardTeamDamage);
//            addCostDefender(def.rewardLossVictoryDescription, def.rewardLossVictory);
//            defenderCounter = -1;
//            addCostDefender("Subtotal", def.subTotal);
            defCostTotal = defCostTotal + def.subTotal;
            defenderCounter = defenderCounter + 1;
        }
        //Restliche Spalten mit - auffüllen
        for (int i = attacker.size(); i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                defenderCostTable.addCell(addDefenderCell("-\r\n-"));
            }
            defenderCounter = defenderCounter + 1;
        }
        defenderCounter = 0;
        for (int j = 0; j < 8; j++) {
            defenderCostTable.addCell(addDefenderCell(""));
        }

        defenderCostTable.addCell(addDefenderCell("Total:"))
                .addCell(addDefenderCell(nf.format(defCostTotal) + " C-Bills"));

        defenderCounter = -1;
        //addCostDefender("Total", defCostTotal);

        Table attackerTable = new Table(new float[]{1}).useAllAvailableWidth()
                .setBorder(Border.NO_BORDER)
                .addCell(addAttackerCell(factionAttacker.getName_en()));

        Table attackercostTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 2, 2, 2, 2, 2, 2})).useAllAvailableWidth()
                .addCell(addAttackerCell("Username"))
                .addCell(addAttackerCell("Mech repair"))
                .addCell(addAttackerCell("Damage"))
                .addCell(addAttackerCell("Comp. destroyed"))
                .addCell(addAttackerCell("Kills"))
                .addCell(addAttackerCell("Assist"))
                .addCell(addAttackerCell("Match-score"))
                .addCell(addAttackerCell("Team Damage"))
                .addCell(addAttackerCell("Loss / Victory"))
                .addCell(addAttackerCell("Subtotal"))
                .setBorder(Border.NO_BORDER);

        attackerCounter = 1;

        for (BalanceUserInfo att : attacker) {
            attackercostTable.addCell(addAttackerCell(att.userName))
                    .addCell(addAttackerCell((100 - att.playerMechHealth) + "% to repair\r\n" + nf.format(att.mechRepairCost) + " C-Bills"))
                    .addCell(addAttackerCell(att.playerDamage + " Damage gone\r\n" + nf.format(att.rewardDamage) + " C-Bills"))
                    .addCell(addAttackerCell(att.playerComponentDestroyed + " destroyed\r\n" + nf.format(att.rewardComponentsDestroyed) + " C-Bills"))
                    .addCell(addAttackerCell(att.playerKills + " kills\r\n" + nf.format(att.rewardKill) + " C-Bills"))
                    .addCell(addAttackerCell(att.playerAssist + " assists\r\n" + nf.format(att.rewardAssist) + " C-Bills"))
                    .addCell(addAttackerCell(att.playerMatchScore + " Match-score\r\n" + nf.format(att.rewardMatchScore) + " C-Bills"))
                    .addCell(addAttackerCell(att.playerTeamDamage + " Team damage\r\n" + nf.format(att.rewardTeamDamage) + " C-Bills"))
                    .addCell(addAttackerCell(nf.format(att.rewardLossVictory) + " C-Bills"))
                    .addCell(addAttackerCell(nf.format(att.subTotal) + " C-Bills"));

//            addCostAttacker("Costs and rewards for the pilot " + att.userName, 0L);
//            addCostAttacker(att.playerMechName + " repair costs (" + (100 - att.playerMechHealth) + "% to repair)", att.mechRepairCost);
//            addCostAttacker("Reward Damage (" + att.playerDamage + " Damage gone)", att.rewardDamage);
//            addCostAttacker("Reward component destroyed (" + att.playerComponentDestroyed + " destroyed)", att.rewardComponentsDestroyed);
//            addCostAttacker("Reward kills (" + att.playerKills + " kills)", att.rewardKill);
//            addCostAttacker("Reward assist (" + att.playerAssist + " assists)", att.rewardAssist);
//            addCostAttacker("Reward Match-score (" + att.playerMatchScore + " Match-score)", att.rewardMatchScore);
//            addCostAttacker("Team damage (" + att.playerTeamDamage + " Team damage)", att.rewardTeamDamage);
//            addCostAttacker(att.rewardLossVictoryDescription, att.rewardLossVictory);
//            attackerCounter = attackerCounter + 1;
//            addCostAttacker("Subtotal", att.subTotal);
            attCostTotal = attCostTotal + att.subTotal;
            attackerCounter = attackerCounter + 1;
        }
        //Restliche Spalten mit - auffüllen
        for (int i = attacker.size(); i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                attackercostTable.addCell(addAttackerCell("-\r\n-"));
            }
            attackerCounter = attackerCounter + 1;
        }
        attackerCounter = 0;
        for (int j = 0; j < 8; j++) {
            attackercostTable.addCell(addAttackerCell(""));
        }

        attackercostTable.addCell(addAttackerCell("Total:"))
                .addCell(addAttackerCell(nf.format(attCostTotal) + " C-Bills"));
        //addCostAttacker("Total", attCostTotal);

        // Table t = new Table(new float[]{1, 1}).useAllAvailableWidth()
        //         .setBorder(Border.NO_BORDER);

        defenderCounter = 0;
        attackerCounter = 0;

//        t.addCell(addDefenderCell(factionDefender.getName_en()))
//                .addCell(addAttackerCell(factionAttacker.getName_en()))
//                .addCell(addTable(tableCostDefender))
//                .addCell(addTable(tableCostAttacker));

        doc.add(new AreaBreak());
        createC3Header("Costs and rewards for drop " + dropCounter);
        createCalcInfoCost();
        doc.add(new Paragraph(""))
                .add(attackerTable)
                .add(attackercostTable)
                .add(new Paragraph(""))
                .add(defenderTable)
                .add(defenderCostTable)
                .add(new Paragraph(""));
        // .add(t);

        attackerCounter = 0;
        defenderCounter = 0;
        dropCounter = dropCounter + 1;
    }

    private void createCalcInfoCost() {
        List calcInf = new List()
                .setFontSize(8)
                .add("Victory = " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_VICTORY").getValue()) +
                        " C-Bills | Loss = " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_LOSS").getValue()) +
                        " C-Bills | Tie = " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_TIE").getValue()) + " C-Bills")
                .add("Damage: " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_DAMAGE").getValue()) + " C-Bills each damage done.")
                .add("Components destroyed: " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_COMPONENT_DESTROYED").getValue()) + " C-Bills each component destroyed.")
                .add("Kills: " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_KILL").getValue()) + " C-Bills each kill.")
                .add("Match-score: " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_MACHT_SCORE").getValue()) + " C-Bills each match-score.")
                .add("Team damage: " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_TEAM_DAMAGE").getValue()) + " C-Bills each team damage.")
                .add("Assist: " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_ASSIST").getValue()) + " C-Bills each assist.")
                .add("No team damage: " + nf.format(C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_NO_TEAM_DAMAGE").getValue()) + " C-Bills if the player does not cause team damage.");

        doc.add(new Paragraph("Cost calculation:")
                        .setFontSize(8)
                        .setBold())
                .add(calcInf);
    }
}
