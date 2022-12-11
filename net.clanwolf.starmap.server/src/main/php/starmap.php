<!DOCTYPE HTML>

<html>

<head>
	<title>C3 Starmap / Season History</title>
	<meta http-equiv='Content-Type' content='text/html;charset=utf-8'/>

	<style type='text/css'>
		* {
			box-sizing: border-box;
		}
		html, body {
			font-size: 12px;
			font-family: verdana;
			color: #000;
			margin: 0;
			padding: 0;
		}
		img {
			border: 0px;
		}
		#content {
			/* background-image: url(https://www.clanwolf.net/themes/ClanWolf/images/body-bg.png); */
			background-color: coral;
			margin-top: 10px;
			margin-bottom: 10px;
			margin-left: 10px;
			margin-right: 10px;
		}
		#myContainer {
			margin:0px;
			padding:0px;
			border:0px;
			width:100%;
			height:100%;
			background-color:white;
		}
		.img-magnifier-container {
			position:relative;
		}
		.img-magnifier-glass {
			position: absolute;
			border: 3px solid #000;
			border-radius: 0%;
			cursor: none;
			width: 500px;
			height: 300px;
			pointer-events: none;
		}
	</style>

	<script type='text/javascript' src='https://www.clanwolf.net/jscripts/ZoomifyImageViewerExpress-min.js'></script>
	<script type='text/javascript'>
		Z.showImage('myContainer', 'https://www.clanwolf.net/apps/C3/seasonhistory/C3_map_3059.zif', 'zSkinPath=https://www.clanwolf.net/jscripts/zoomify/Assets/Skins/Default&zNavigatorVisible=0&zInitialX=5545&zInitialY=8150&zInitialZoom=175&zMaxZoom=300&zToolbarVisible=0&zToolbarBackgroundVisible=0&zNavigatorVisible=0&zLogoVisible=0&zMinimizeVisible=0&zHelpVisible=0');
	</script>
	<script>
		function magnify(imgID, zoom) {
			var img, glass, w, h, bw;
			img = document.getElementById(imgID);
			glass = document.createElement("DIV");
			glass.setAttribute("class", "img-magnifier-glass");
			glass.setAttribute("id", "gla");
			img.parentElement.insertBefore(glass, img);
			glass.style.backgroundImage = "url('" + img.src + "')";
			glass.style.backgroundRepeat = "no-repeat";
			glass.style.backgroundSize = (img.width * zoom) + "px " + (img.height * zoom) + "px";
			bw = 3;
			w = glass.offsetWidth / 2;
			h = glass.offsetHeight / 2;
			glass.addEventListener("mousemove", moveMagnifier);
			img.addEventListener("mousemove", moveMagnifier);
			glass.addEventListener("touchmove", moveMagnifier);
			img.addEventListener("touchmove", moveMagnifier);

			function moveMagnifier(e) {
				var pos, x, y;
				e.preventDefault();
				pos = getCursorPos(e);
				x = pos.x;
				y = pos.y;
				if (x > img.width - (w / zoom)) {x = img.width - (w / zoom);}
				if (x < w / zoom) {x = w / zoom;}
				if (y > img.height - (h / zoom)) {y = img.height - (h / zoom);}
				if (y < h / zoom) {y = h / zoom;}
				glass.style.left = (x - w) + "px";
				glass.style.top = (y - h) + "px";
				glass.style.backgroundPosition = "-" + ((x * zoom) - w + bw) + "px -" + ((y * zoom) - h + bw) + "px";
			}

			function getCursorPos(e) {
				var a, x = 0, y = 0;
				e = e || window.event;
				a = img.getBoundingClientRect();
				x = e.pageX - a.left;
				y = e.pageY - a.top;
				x = x - window.pageXOffset;
				y = y - window.pageYOffset;
				return {x : x, y : y};
			}
		}

		function showMagnifier() {
			document.getElementById('gla').style.visibility = "visible";
		}

		function hideMagnifier() {
			document.getElementById('gla').style.visibility = "hidden";
		}
	</script>
</head>

<body>
	<div id="content">
		<table width='100%' height='100%' cellspacing='0' cellpadding='0'>
			<!--
			<tr>
				<td width='70%' height='1%' align='center'><h1>C3</h1></td>
				<td width='30%' height='1%' align='center'><h1>Offizielle Karten</h1></td>
			</tr>
			<tr>
				<td width='70%' height='1%' align='center'></td>
				<td width='30%' height='1%' align='center'><p>3059</p></td>
			</tr>
			-->
			<tr>
				<td width='70%' height='150px'>
					<table width='100%' height='100%' cellspacing='0' cellpadding='0'>
						<tr height='150px'>
							<td width='10%' height='150px' align='left'><img src='https://www.clanwolf.net/apps/C3/seasonhistory/S1/C3_S1_R1_map_history_preview.png' height='150px'></td>
							<td width='80%' height='150px' align='right'>&nbsp;&nbsp;&nbsp;Runde:&nbsp;&nbsp;&nbsp;</td>
							<td width='10%' height='150px' align='right' nowrap>
								<p>
									01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>
									21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>
									41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</p>
							</td>
						</tr>
					</table>
				</td>
				<td width='30%'rowspan='2' align='center'>
					<div id='myContainer'></div>
				</td>
			</tr>
			<tr>
				<td width='70%' align='center' valign='top'>
					<div id="magnifier" class="img-magnifier-container">
						<img id="myimage" src='https://www.clanwolf.net/apps/C3/seasonhistory/S1/C3_S1_R1_map_history.png' width='100%'>
					</div>
				</td>
			</tr>
		</table>
	</div>

	<script>
		magnify("myimage", 3);
		hideMagnifier();
		document.getElementById("magnifier").onmouseover=showMagnifier;
		document.getElementById("magnifier").onmouseout=hideMagnifier;
	</script>
</body>

</html>
