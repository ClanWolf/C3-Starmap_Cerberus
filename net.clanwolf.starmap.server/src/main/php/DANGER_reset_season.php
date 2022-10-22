<?php
 /**
  * Reset season
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
ini_set("display_errors", 1);

include('config.php');
$conn = new mysqli($db_host_C3, $db_user_C3, $db_pass_C3);
if ($conn->connect_error) {
	die("Connection failed: " . $conn->connect_error);
}
?>

<html>

<header>
</header>

<body>

<h1>DANGERZONE!</h1>

<p>RESET SEASON.<br>Type "reset the season" to run the script.</p>
<form method="post" action="<?php echo $_SERVER['PHP_SELF']; ?>">
<label for="idKey">Confirm:</label>
<input type="text" id="idKey" name="key">
<input type="submit" value="Execute" id="idExecute">

<?php
  if(empty($_POST["key"])) {
    echo "<p>Confirm execution!</p>";
  } else {
    echo "<p>Execution " . $_POST["key"] . "</p>";

    if ($_POST["key"] == "reset season") {
      echo "<p>Running...</p>";
      // $sql = "UPDATE C3.SYSCONFIG SET VALUE='5.7.14' WHERE SYSCONFIG.KEY='VERSION CLIENT'";

      // if ($conn->query($sql) === TRUE) {
      //   echo "Updated successfully.";
      // } else {
      //   echo "Error: " . $conn->error;
      // }

      echo "<p>Finished.</p>";
    } else {
      echo "<p>Wrong confirmation. Exiting.</p>";
    }
  }
?>

</form>

</body>
</html>
