<?php
 /**
  * Get table information on the C3 database
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

$sql = "SELECT * FROM information_schema.columns WHERE table_schema = 'C3' ORDER BY table_name,ordinal_position";
$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) > 0) {
    echo "<table>";
	echo "<tr>";
	echo "<td>TABLE_NAME</td>";
	echo "<td>COLUMN_NAME</td>";
	echo "<td>ORDINAL_POSITION</td>";
	echo "<td>COLUMN_DEFAULT</td>";
	echo "<td>IS_NULLABLE</td>";
	echo "<td>DATA_TYPE</td>";
	echo "</tr>";
	while($row = mysqli_fetch_assoc($result)) {
		$TABLE_NAME = $row["TABLE_NAME"];
		$COLUMN_NAME = $row["COLUMN_NAME"];
		$ORDINAL_POSITION = $row["ORDINAL_POSITION"];
		$COLUMN_DEFAULT = $row["COLUMN_DEFAULT"];
		$IS_NULLABLE = $row["IS_NULLABLE"];
		$DATA_TYPE = $row["DATA_TYPE"];

		echo "<tr>";
		echo "<td>" . $TABLE_NAME . "</td>";
		echo "<td>" . $COLUMN_NAME . "</td>";
		echo "<td>" . $ORDINAL_POSITION . "</td>";
		echo "<td>" . $COLUMN_DEFAULT . "</td>";
		echo "<td>" . $IS_NULLABLE . "</td>";
		echo "<td>" . $DATA_TYPE . "</td>";
		echo "</tr>";
	}
	echo "</table>";
} else {
	echo "No data...";
}

?>
