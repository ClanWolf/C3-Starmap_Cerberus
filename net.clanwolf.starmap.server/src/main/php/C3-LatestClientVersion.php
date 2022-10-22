<?php
 /**
  * Get latest client version
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
include('config.php');
$conn = new mysqli($db_host_C3, $db_user_C3, $db_pass_C3);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT * FROM C3.SYSCONFIG";
$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $key = $row["Key"];
        $value = $row["Value"];

        if ($key === "VERSION CLIENT") {
            echo $value;
        }
    }
} else {
	echo "No data...";
}

?>
