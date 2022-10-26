<?php
 /**
  * Get latest C3 users
  * PHP version 7.2.10
  *
  * @category Servercomponents
  * @package  C3
  * @author   Meldric <warwolfen@gmail.com>
  * @license  Apache License 2.0
  * @version  GIT: <git_id>
  * @link     https://www.clanwolf.net
  */

error_reporting(E_ALL);
include('config_readonly.php');
$conn_clanwolf_ro = new mysqli($db_host_C3_ro, $db_user_C3_ro, $db_pass_C3_ro);
if ($conn_clanwolf_ro->connect_error) {
    die("Connection failed: " . $conn_clanwolf_ro->connect_error);
}

$round = 0;

$sql5 = "";
$sql5 = $sql5 . "SELECT r.Round, r.Season from C3._HH_ROUND r where r.Season = 1 ";

$result5 = mysqli_query($conn_clanwolf_ro, $sql5);
if (mysqli_num_rows($result5) > 0) {
	while($row5 = mysqli_fetch_assoc($result5)) {
		$round = $row5["Round"];
	}
}

$sql12 = "";
$sql12 = $sql12 . "select ha.Season, ha.Round, ha.JumpshipID, ha.FactionID_Winner, hj.JumpshipName, hj.JumpshipFactionID, ss.Name, f.ShortName ";
$sql12 = $sql12 . "from C3._HH_ATTACK ha, C3.STARSYSTEM ss, C3._HH_STARSYSTEMDATA ssd, C3.FACTION f, C3._HH_JUMPSHIP hj ";
$sql12 = $sql12 . "where ss.ID = ha.StarSystemID ";
$sql12 = $sql12 . "and ssd.StarSystemID = ss.ID ";
$sql12 = $sql12 . "and ha.FactionID_Defender = f.ID ";
$sql12 = $sql12 . "and hj.ID = ha.JumpshipID ";
$sql12 = $sql12 . "and ha.Season = 1 ";
$sql12 = $sql12 . "and ";
$sql12 = $sql12 . "(ha.FactionID_Winner is null or ha.Round = " . $round . ") ";
$sql12 = $sql12 . "ORDER BY ha.ID ASC ";

$result12 = mysqli_query($conn_clanwolf_ro, $sql12);
if (mysqli_num_rows($result12) > 0) {
	echo "<table width=100% cellspacing=0 cellpadding=0>";
	while($row = mysqli_fetch_assoc($result12)) {
		$attackRound = $row["Round"];
		$jumpshipName = $row["JumpshipName"];
		$jumpshipFactionID = $row["JumpshipFactionID"];
		$starSystemID = $row["StarSystemID"];
		$starSystemName = $row["Name"];
		$starSystemFaction = $row["ShortName"];
		$winner = $row["FactionID_Winner"];
		$attackingFaction = "?";

		$sql14 = "select f.ShortName from C3.FACTION f where f.ID = " . $jumpshipFactionID;
		$result14 = mysqli_query($conn_clanwolf_ro, $sql14);
		if (mysqli_num_rows($result14) > 0) {
			while($row14 = mysqli_fetch_assoc($result14)) {
				$attackingFaction = $row14["ShortName"];
			}
		}

		$style = "style='font-size:10px;'";
		$icon = "<img src='https://www.clanwolf.net/images/attention_icon.png' width='8px'>";
		if ($round != $attackRound) {
			$style = "style='font-size:10px;color:#999;'";
			$icon = "<img src='https://www.clanwolf.net/images/wait_icon.png' width='8px'>";
		}

		if ($winner == null) {
			echo "<tr>";
			echo "<td align='left' width='20%' " . $style . ">&nbsp;&nbsp;&nbsp;&nbsp;R" . $attackRound . "</td>";
			echo "<td align='left' width='10%' " . $style . ">&nbsp;" . $icon . "</td>";
			echo "<td align='left' width='10%' " . $style . " title='" . $jumpshipName . "'>&nbsp;&nbsp;" . $attackingFaction . "</td>";
			echo "<td align='center' width='30%' " . $style . ">>></td>";
			echo "<td align='right' width='30%' " . $style . ">" . $starSystemName . " [" . $starSystemFaction . "]&nbsp;&nbsp;&nbsp;&nbsp;</td>";
			echo "</tr>";
		} else {
			echo "<tr>";
			echo "<td align='left' width='20%' " . $style . ">&nbsp;&nbsp;&nbsp;&nbsp;R" . $attackRound . "</td>";
			echo "<td align='left' width='10%' " . $style . ">&nbsp;<img src='https://www.clanwolf.net/images/check_icon.png' width='8px'></td>";
			echo "<td align='right' width='70%' colspan='3'" . $style . ">&nbsp;&nbsp;" . $starSystemName . "&nbsp;&nbsp;&nbsp;>>&nbsp;&nbsp;&nbsp;" . $starSystemFaction . "&nbsp;&nbsp;&nbsp;&nbsp;</td>";
			echo "</tr>";
		}
	}
	echo "</table>";
} else {
	echo "No data...";
}

?>
