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

// echo "host: " . $db_host_C3 . "<br>";
// echo "user: " . $db_user_C3 . "<br>";
// echo "pass: " . $db_pass_C3 . "<br>";

$conn = new mysqli($db_host_C3, $db_user_C3, $db_pass_C3);
if ($conn->connect_error) {
	die("Connection failed: " . $conn->connect_error);
}

$sql = "UPDATE C3.SYSCONFIG SET VALUE='7.2.107' WHERE SYSCONFIG.KEY='VERSION CLIENT'";

if ($conn->query($sql) === TRUE) {
  echo "Record updated successfully to 7.2.107.";
} else {
  echo "Error updating record: " . $conn->error;
}

?>
