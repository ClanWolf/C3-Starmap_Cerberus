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

require_once('./db.php');

$sql = "UPDATE C3.SYSCONFIG SET VALUE='5.7.8' WHERE SYSCONFIG.KEY='VERSION CLIENT'";

if ($conn->query($sql) === TRUE) {
  echo "Record updated successfully to 5.7.8.";
} else {
  echo "Error updating record: " . $conn->error;
}

?>