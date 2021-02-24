<?php
    /*
    * Parameter
    * rpid            -> RolePlayID ist gleich der Name des Subdirectory unter /resources
    * filename        -> Name des Bildes, das angezeigt werden soll
    * audio           -> Name der MP3, die abgespielt werden soll
    * video           -> Name der MP4, die angezeigt werden soll
    * TODO: storyname -> Name der Geschichte/des Kapitel
    * Es soll noch so sein, das man den Name der Story als Parameter übergeben kann,
    * dann soll dieser Zentriert im Bild angezeigt werden, aber nur, wenn keiner der anderen Parameter übergeben wurde
    
    * Wird kein Parameter angegeben, so wird lediglich das Default-Bild angezeigt
    * Wird einer der Parameter filename, audio, video angegeben ist der Parameter ripd zwingend notwendig
    * da sonst die Resourcen nicht gefunden werden

    * Beispiel URL's
    * http://localhost/test/generichtml.php?rpid=1&filename=test.png
    * http://localhost/test/generichtml.php?rpid=1&filename=test.png&audio=Decoherence.mp3
    * http://localhost/test/generichtml.php?rpid=1&video=01.mp4
    * 
    * http://www.clanwolf.net/apps/C3/rpg/genericHtml.php
    */

    $ressourcePath = './resources/';

    // Variable for header informations
    $header = '<head></head>';

    // Variable for body informations
    $html = '';
    $bodyImg = '';
    $bodyAudio = '';
    $bodyVideo = '';
    $resourceSubPath = '';
    $storyname = '';

    if (isset ($_GET["storyname"])) {
        $storyname = $_GET["storyname"];
    }

    if (isset($_GET["rpid"])) {
        $resourceSubPath = $ressourcePath.$_GET["rpid"].'/';

        if (isset($_GET["video"])) {
            $bodyVideo = '<video src="'.$resourceSubPath.$_GET["video"].'" autoplay></video>';

        } else {
            // Set default image
            $image = './default.png';

            // Check other image file
            if (isset($_GET["filename"])) {
                $image = $resourceSubPath.$_GET["filename"];
            }
            // Image file is always set    
            $bodyImg = '<img src="'.$image.'" width="720" height="420" alt="No image found"></img>';

            // Check audiofile
            if (isset($_GET["audio"])) {
                $bodyAudio = '<audio src="'.$resourceSubPath.$_GET["audio"].'" type="audio/mp3" autoplay volume="0.3"></audio>';
            }
        }    
        $body = '<body> '.$bodyImg. $bodyAudio. $bodyVideo.'</body>';    
        $html = '<html> ' .$header . $body .'</html>';        
    } else {
        $html = '<html>
        <head>
           <title>Intro</title>
           <<link href="https://fonts.googleapis.com/css?family=Aldrich" rel="stylesheet"> 

           <style>
                html{
                border:0px;
                }

                 body {
                  overflow:hidden;
                 }

                .box1 {
                   background-color: black;
                   position: relative;
                   height: 875px;
                   top: 0px;
                 }

                #CenterLabel {
                    background-image: url(./default.png);
                    background-size:cover;
                    position: absolute;                    
                    top: 10px;
                    width: 690px;
                    height: 350px;
                }

                .zentriert {
                    text-align: center;
                    color: #FFFFFF;

                    position: relative;
                    top: 85px;
                    width: 690px;

                    font-family: \'Aldrich\', sans-serif;
                    /* text-shadow: 1.5px 1.5px #FFFF00; */

                    transform: translateY(-50%);
                    transform: scale(0);

                    animation-name: scaleText;
                    animation-delay: 0.1s;
                    animation-duration: 8s;
                    animation-fill-mode: forwards;
                    animation-play-state: running;             
                }

                @keyframes scaleText {
                    0%  { transform: scale(0);
                          font-size: 10px;}
                    50% { transform: scale(3.0);)
                        }                  
                    60% { transform: scale(1.6);                                
                        }
                    75% { transform: scale(1.6);                
                        }        
                    78% { transform: scale(1.6);
                          transform: rotate(-3deg);
                        }   
                    85% { transform: scale(1.6);
                          transform: rotate(3deg);
                        }                        
                    90% { transform: scale(1.6);
                          transform: rotate(-3deg);
                        }
                    95% { transform: scale(1.6);
                          transform: rotate(3deg);
                        }      
                    100% { transform: scale(1.6);
                           transform: rotate(0deg);
                           font-size: 20px;
                        }
                }
           </style>
        </head>

        <body>
            <div class="box1">
                <div id="CenterLabel">
                    <p class="zentriert">'.$storyname.'</p>
                </div>   
            </div>

            <audio src="./defaultsound.mp3" type="audio/mp3" autoplay volume="0.3" ></audio>
        </body>
        </html>';
    }

    // Create html code    
    echo $html;
?>
