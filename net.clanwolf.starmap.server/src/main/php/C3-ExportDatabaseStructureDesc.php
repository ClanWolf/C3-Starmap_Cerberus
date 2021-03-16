<?php
	require_once('./db.php');

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
		echo "ERROR: DB offline";
	}
?>
