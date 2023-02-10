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
$currentRoundStartDateTimeStamp = 0;
$tsNow = strtotime("now");

$sql5 = "";
// $sql5 = $sql5 . "SELECT r.Round, r.Season, r.CurrentRoundStartDate, r.Updated from C3._HH_ROUND r where r.Season = 1 ";
$sql5 = $sql5 . "SELECT r.Round, r.Season, r.CurrentRoundStartDate, r.CurrentRoundStartDateRealTime from C3._HH_ROUND r where r.Season = 1 ";
$result5 = mysqli_query($conn_clanwolf_ro, $sql5);
if (mysqli_num_rows($result5) > 0) {
	while($row5 = mysqli_fetch_assoc($result5)) {
		$round = $row5["Round"];
		$currentRoundStartDateTimeStamp = $row5["CurrentRoundStartDateRealTime"];
	}
}

//$sql6 = "";
//$sql6 = $sql6 . "SELECT s.StartDateRealYear from C3._HH_SEASON s where s.ID = 1 ";
//$result6 = mysqli_query($conn_clanwolf_ro, $sql6);
//if (mysqli_num_rows($result6) > 0) {
//	while($row6 = mysqli_fetch_assoc($result6)) {
//		$seasonStartDateRealYear = $row6["StartDateRealYear"];
//	}
//}

$hoursLeft = 83 - floor(($tsNow - strtotime($currentRoundStartDateTimeStamp)) / (60 * 60));
echo "<center><div style='font-size:10px;'>S1 / R" . $round . " [~".$hoursLeft."h]</div></center><br>";

$sql11 = "";
$sql11 = $sql11 . "SELECT U.ID, U.UserName, US.IP, US.UserId, US.LoginTime, US.ClientVersion ";
$sql11 = $sql11 . "FROM C3.USER U, C3.USER_SESSION US WHERE U.ID = US.UserId ";
$sql11 = $sql11 . "ORDER BY LoginTime DESC ";
// $sql11 = $sql11 . "LIMIT 5 ";

$minimumRowCount = 0;
$result11 = mysqli_query($conn_clanwolf_ro, $sql11);
if (mysqli_num_rows($result11) > 0) {
	echo "<table width=100% cellspacing=0 cellpadding=0>";
	while($row = mysqli_fetch_assoc($result11)) {
		$minimumRowCount = $minimumRowCount + 1;
		$timestamp = floor(($tsNow - strtotime($row["LoginTime"])) / 60);
		$version = $row["ClientVersion"];
		$IP = $row["IP"];
		$show = 1;
		if ($timestamp < 5) { $timestamp = "<span style='color:#00ff00;'>online</span>"; }
		if ($timestamp >= 5 && $timestamp <= 60) { $timestamp = "<span style='color:#ffff00;'>" . $timestamp . 'm</span>'; }
		if ($timestamp > 60 && $timestamp <= (12 * 60)) { $timestamp = "<span style='color:#777;'>> 60m</span>"; }
		if ($timestamp > (12 * 60) && $timestamp < (48 * 60)) { $timestamp = "<span style='color:#777;'>> 12h</span>"; }
		if ($timestamp > (48 * 60)) {
			$timestamp = "<span style='color:#777;'>> 2d</span>";
			if ($minimumRowCount > 2) {
				$show = 0;
			}
		}

		if ($show == 1) {
			$name = $row["UserName"];
			echo "<tr><td align='left' style='font-size:10px;' title='" . $IP . "'>&nbsp;&nbsp;&nbsp;&nbsp;- " . $name . "</td><td align='right' style='font-size:10px;'>" . $version . "</td><td align='right' style='font-size:10px;'>" . $timestamp . "&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
		}
	}
	echo "</table>";
} else {
	echo "No data...";
}

?>
