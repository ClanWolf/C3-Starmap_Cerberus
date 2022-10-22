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

error_reporting(E_ALL);

echo "<b>Synchronizing users to C3</b><br>";
echo "--------------------------------<br>";

include('config.php');
$conn = new mysqli($db_host_C3, $db_user_C3, $db_pass_C3);
if ($conn->connect_error) {
	die("Connection failed: " . $conn->connect_error);
}
echo "Connected to source db.<br>";

include('config_clanwolf.php');
$conn_clanwolf = new mysqli($db_host_CW, $db_user_CW, $db_pass_CW);
if ($conn_clanwolf->connect_error) {
    die("Connection failed: " . $conn_clanwolf->connect_error);
}
echo "Connected to target db.<br>";

// Sync

echo "--------------------------------<br>";
echo "Getting all users from the C3-Client user group:<br>";
echo "<br>";
$sql = "SELECT * FROM clanwolf.cwfusion_users WHERE user_groups like '%.30'";
// echo "select: " . $sql;
$result = mysqli_query($conn_clanwolf, $sql);
if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
        $user_id = $row["user_id"];
        $user_name = $row["user_name"];
        $user_password = $row["user_password"];
        $user_email = $row["user_email"];
        $user_birthdate = $row["user_birthdate"];
        $user_locale = $row["user_locale"];
        $user_avatar = $row["user_avatar"];
        $user_joined = $row["user_joined"];
        $user_lastvisit = $row["user_lastvisit"];
        $user_web = $row["user_web"];

        echo "Found SOURCE user " . $user_name  . "<br>";

        if (strtotime($user_birthdate) < 0) {
            $date = new DateTime('1975-01-01');
            $user_birthdate = $date->format('Y-m-d');
            echo "Found 0000-Birthdate date for user: " . user_name . ". Setting to: " . $user_birthdate . "<br>";
        }
//        if (strtotime($user_joined) < 0) {
//            echo "Found 0000-Joinede date for user: " . user_name . "<br>";
//        }
//        if (strtotime($user_lastvisit) < 0) {
//            echo "Found 0000-Lastvisit date for user: " . user_name . "<br>";
//        }

        $sql2 = "SELECT * FROM C3.USER WHERE UserName = '" . $user_name . "'";
        // echo "select: " . $sql2;
        $result2 = mysqli_query($conn, $sql2);
        if (mysqli_num_rows($result2) > 0) {
            while($row2 = mysqli_fetch_assoc($result2)) {
                $active = $row2["active"];

                echo "Found TARGET user: " . $user_name . "<br>";
                if ($active == '1') {
                    echo "... synchronizing (user is ACTIVE)";

                    // -- ID
                    // -- UserName
                    // -- UserPassword
                    // + UserPasswordWebsite
                    // + UserEMail
                    // + UserAvatar
                    // -- FirstName
                    // -- LastName
                    // -- Location
                    // -- Zipcode
                    // -- Website
                    // -- Privileges
                    // + BirthDate
                    // + JoinDate
                    // -- LastLogin
                    // -- BannedUntil
                    // -- BannReason
                    // -- Created
                    // -- CurrentCharacterID
                    // -- LastModified
                    // -- LastModifiedByUserId
                    // + active

                    $dt = new DateTime('@' . $user_joined);
                    $dts = $dt->format('Y-m-d');

                    $sql2_update = "UPDATE C3.USER SET ";
                    $sql2_update = $sql2_update . "UserPasswordWebsite='" . $user_password . "', ";
                    $sql2_update = $sql2_update . "UserEMail='" . $user_email . "', ";
                    if ($user_avatar != "") {
                        $sql2_update = $sql2_update . "UserAvatar='https://www.clanwolf.net/images/avatars/" . $user_avatar . "', ";
                    } else {
                        $sql2_update = $sql2_update . "UserAvatar='https://www.clanwolf.net/images/noavatar.jpg', ";
                    }
                    $sql2_update = $sql2_update . "BirthDate='" . $user_birthdate . "', ";
                    $sql2_update = $sql2_update . "JoinDate='" . $dts . "', ";
                    $sql2_update = $sql2_update . "Website='" . $user_web . "', ";
                    $sql2_update = $sql2_update . "active=1 ";
                    $sql2_update = $sql2_update . "WHERE UserName = '" . $user_name . "'";

                    // echo " DEBUG: " . $sql2_update;

                    if(mysqli_query($conn, $sql2_update)) {
                        echo "... processed!<br>";
                    } else {
                        echo "... not processed! ERROR: Could not execute " . $sql2_update . ". Error: " . mysqli_error($conn) . "<br>";
                    }
                } else {
                    echo "... ignoring (user is NOT ACTIVE).<br>";
                }
            }
        } else {
            echo "no TARGET user";
            echo "... Creating user: " . $user_name;

            // ID
            // UserName
            // UserPassword
            // UserEMail
            // UserAvatar
            // FirstName
            // LastName
            // Location
            // Zipcode
            // Website
            // Privileges
            // BirthDate
            // JoinDate
            // LastLogin
            // BannedUntil
            // BannReason
            // Created
            // CurrentCharacterID
            // LastModified
            // LastModifiedByUserId
            // active

            $dt = new DateTime('@' . $user_joined);
            $dts = $dt->format('Y-m-d');

            $dt_now = date("Y-m-d H:i:s");

            if ($user_avatar != "") {
                $avatar = "'http://www.clanwolf.net/images/avatars/" . $user_avatar . "'";
            } else {
                $avatar = "'https://www.clanwolf.net/images/noavatar.jpg'";
            }

            // TODO: Fill in the correct data to prevent Server crashes on wrong data (0-value dates,...)
            // TODO: Dates may never be 0000.00.00:00...
            // Insert "FirstName"
            // Insert "LastName"
            // Insert "Location"
            // Insert "Zipcode"
            // Insert "Created" here with the current date
            // Insert "LastModified" here with the current date
            // Insert "LastModifiedByUserId" --> 26

            $sql2_insert = "INSERT INTO C3.USER (";
            $sql2_insert = $sql2_insert . "UserName, ";
            $sql2_insert = $sql2_insert . "UserPasswordWebsite, ";
            $sql2_insert = $sql2_insert . "UserEMail, ";
            $sql2_insert = $sql2_insert . "UserAvatar, ";
            $sql2_insert = $sql2_insert . "BirthDate, ";
            $sql2_insert = $sql2_insert . "JoinDate, ";
            $sql2_insert = $sql2_insert . "FirstName, ";
            $sql2_insert = $sql2_insert . "LastName, ";
            $sql2_insert = $sql2_insert . "Location, ";
            $sql2_insert = $sql2_insert . "Zipcode, ";
            $sql2_insert = $sql2_insert . "Website, ";
            $sql2_insert = $sql2_insert . "Created, ";
            $sql2_insert = $sql2_insert . "LastModified, ";
            $sql2_insert = $sql2_insert . "LastModifiedByUserId, ";
            $sql2_insert = $sql2_insert . "active ";
            $sql2_insert = $sql2_insert . ") ";
            $sql2_insert = $sql2_insert . "VALUES (";
            $sql2_insert = $sql2_insert . "'" . $user_name . "', ";         // UserName
            $sql2_insert = $sql2_insert . "'" . $user_password . "', ";     // UserPasswordWebsite
            $sql2_insert = $sql2_insert . "'" . $user_email . "', ";        // UserEMail
            $sql2_insert = $sql2_insert . $avatar . ", ";                   // UserAvatar
            $sql2_insert = $sql2_insert . "'" . $user_birthdate . "', ";    // BirthDate
            $sql2_insert = $sql2_insert . "'" . $dts . "', ";               // JoinDate
            $sql2_insert = $sql2_insert . "'No data', ";                    // FirstName
            $sql2_insert = $sql2_insert . "'No data', ";                    // LastName
            $sql2_insert = $sql2_insert . "'No data', ";                    // Location
            $sql2_insert = $sql2_insert . "11111, ";                        // Zipcode
            $sql2_insert = $sql2_insert . "'" . $user_web . "', ";          // Website
            $sql2_insert = $sql2_insert . "'" . $dt_now . "', ";            // Created
            $sql2_insert = $sql2_insert . "'" . $dt_now . "', ";            // LastModified
            $sql2_insert = $sql2_insert . "26, ";                           // LastModifiedByUserId
            $sql2_insert = $sql2_insert . "1";                              // active
            $sql2_insert = $sql2_insert . ")";

            // echo " DEBUG: " . $sql2_insert;

            if(mysqli_query($conn, $sql2_insert)) {
                $last_user_id = mysqli_insert_id($conn);
                echo "... processed! Got id: " . $last_user_id . "<br>";

                echo "Inserting character for user " . $user_name . ".";

                $sql7_insert = "INSERT INTO C3.ROLEPLAY_CHARACTER (";
                $sql7_insert = $sql7_insert . "CharName, ";
                $sql7_insert = $sql7_insert . "Callsign, ";
                $sql7_insert = $sql7_insert . "LastName, ";
                $sql7_insert = $sql7_insert . "Birthdate, ";
                $sql7_insert = $sql7_insert . "Gender, ";
                //$sql7_insert = $sql7_insert . "History, ";
                //$sql7_insert = $sql7_insert . "Image, ";
                $sql7_insert = $sql7_insert . "Health, ";
                $sql7_insert = $sql7_insert . "Strength, ";
                $sql7_insert = $sql7_insert . "Body, ";
                $sql7_insert = $sql7_insert . "Dexterity, ";
                $sql7_insert = $sql7_insert . "Reflexes, ";
                $sql7_insert = $sql7_insert . "Intelligence, ";
                $sql7_insert = $sql7_insert . "Will, ";
                $sql7_insert = $sql7_insert . "Charisma, ";
                $sql7_insert = $sql7_insert . "Edge, ";
                $sql7_insert = $sql7_insert . "UserID, ";
                $sql7_insert = $sql7_insert . "StarsystemID, ";
                $sql7_insert = $sql7_insert . "JumpshipID, ";
                $sql7_insert = $sql7_insert . "FactionID, ";
                $sql7_insert = $sql7_insert . "FactionTypeID";
                //$sql7_insert = $sql7_insert . "StoryID ";
                $sql7_insert = $sql7_insert . ") ";
                $sql7_insert = $sql7_insert . "VALUES (";
                $sql7_insert = $sql7_insert . "'" . $user_name . "', ";         // UserName
                $sql7_insert = $sql7_insert . "'" . $user_name . "', ";         // Callsign
                $sql7_insert = $sql7_insert . "'Müller', ";                     // LastName
                $sql7_insert = $sql7_insert . "'" . $user_birthdate . "', ";    // Birthdate
                $sql7_insert = $sql7_insert . "'male', ";                       // Gender
                //$sql7_insert = $sql7_insert . "'...', ";                        // History
                //$sql7_insert = $sql7_insert . "'NULL', ";                       // Image
                $sql7_insert = $sql7_insert . "0, ";                            // Health
                $sql7_insert = $sql7_insert . "0, ";                            // Strength
                $sql7_insert = $sql7_insert . "0, ";                            // Body
                $sql7_insert = $sql7_insert . "0, ";                            // Dexterity
                $sql7_insert = $sql7_insert . "0, ";                            // Reflexes
                $sql7_insert = $sql7_insert . "0, ";                            // Intelligence
                $sql7_insert = $sql7_insert . "0, ";                            // Will
                $sql7_insert = $sql7_insert . "0, ";                            // Charisma
                $sql7_insert = $sql7_insert . "0, ";                            // Edge
                $sql7_insert = $sql7_insert . $last_user_id . ", ";             // UserID
                $sql7_insert = $sql7_insert . "1886, ";                         // StarSystemID (Terra = 1886)
                $sql7_insert = $sql7_insert . "0, ";                            // JumpShipID
                $sql7_insert = $sql7_insert . "32, ";                           // FactionID
                $sql7_insert = $sql7_insert . "2";                              // FactionTypeID
                //$sql7_insert = $sql7_insert . "1";                              // StoryID
                $sql7_insert = $sql7_insert . ")";

                // echo " DEBUG: " . $sql7_insert;

                if(mysqli_query($conn, $sql7_insert)) {
                   $last_char_id = mysqli_insert_id($conn);
                   echo "... processed! Got char id: " . $last_char_id . "<br>";

                   echo "Updating char id in the created user.";

                   $sql8_update = "UPDATE C3.USER SET ";
                   $sql8_update = $sql8_update . "CurrentCharacterID = " . $last_char_id . " ";
                   $sql8_update = $sql8_update . "WHERE ID = " . $last_user_id;

                   if(mysqli_query($conn, $sql8_update)) {
                       echo "... processed!<br>";
                   } else {
                       echo "... not processed! ERROR: Could not execute " . $sql8_update . ". Error: " . mysqli_error($conn) . "<br>";
                   }
                } else {
                    echo "... not processed! ERROR: Could not execute " . $sql7_insert . ". Error: " . mysqli_error($conn) . "<br>";
                }
            } else {
                echo "... not processed! ERROR: Could not execute " . $sql2_insert . ". Error: " . mysqli_error($conn) . "<br>";
            }
        }
    }
} else {
    echo "No SOURCE users fetched.<br>";
}
?>
