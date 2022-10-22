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

$sql12 = "SELECT A.Round, A.StarSystemID FROM C3._HH_ATTACK A WHERE A.FactionID_Winner is null ORDER BY ID DESC ";
$result12 = mysqli_query($conn_clanwolf_ro, $sql12);
if (mysqli_num_rows($result12) > 0) {
	echo "<table width=100% cellspacing=0 cellpadding=0>";
	while($row = mysqli_fetch_assoc($result12)) {
		$round = $row["Round"];
		$starSystem = $row["StarSystemID"];
		echo "<tr><td align='left' style='font-size:8px;'>&nbsp;&nbsp;&nbsp;&nbsp;- R" . $round . "</td><td align='right' style='font-size:8px;'>" . $starSystem . "&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
	}
	echo "</table>";
} else {
	echo "No data...";
}

?>
