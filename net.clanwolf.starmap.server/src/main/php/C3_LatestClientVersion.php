<?php
 /**
  * Get latest client version
  * PHP version 7.2.10
  *
  * @category  Servercomponents
  * @package   C3
  * @author    warwolfen@gmail.com
  * @license   Apache License 2.0
  * @version   1.0
  * @link      https://www.clanwolf.net
  */

require_once './db.php';

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
    echo "ERROR: DB offline";
}

?>
