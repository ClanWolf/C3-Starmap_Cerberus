<?php
 /**
  * DB connection
  * PHP version 7.2.10
  *
  * @category Servercomponents
  * @package  C3
  * @author   Meldric <warwolfen@gmail.com>
  * @license  Apache License 2.0
  * @version  GIT: <git_id>
  * @link     https://www.clanwolf.net
  */

require_once('./config_clanwolf.php');

// Create connection
$conn_clanwolf = new mysqli($db_host, $db_user, $db_pass);

// Check connection
if ($conn_clanwolf->connect_error) {
    die("Connection failed: " . $conn_clanwolf->connect_error);
}

?>
