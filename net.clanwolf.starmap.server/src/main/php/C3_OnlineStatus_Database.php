<?php

require_once './db.php';

$sql = "SELECT * FROM C3.SYSCONFIG";
$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $key = $row["Key"];
        $value = $row["Value"];

        if ($key === "VERSION DB") {
            echo "online";
        }
    }
} else {
    echo "offline";
}

?>
