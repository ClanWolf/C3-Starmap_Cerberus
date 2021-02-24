<?php
 /**
  * DB connection
  * PHP version 7.2.10
  *
  * @category  Servercomponents
  * @package   C3
  * @author    warwolfen@gmail.com
  * @license   Apache License 2.0
  * @version   1.0
  * @link      https://www.clanwolf.net
  */

require_once './config.php' ;

// Create connection
$conn = new mysqli($db_host, $db_user, $db_pass);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

?>
