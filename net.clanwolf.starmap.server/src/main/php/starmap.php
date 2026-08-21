<?php
	$seasonId = '';
	$animatedMapFileName = '';

	function get_string_between($string, $start, $end){
		$string = ' ' . $string;
		$ini = strpos($string, $start);
		if ($ini == 0) return '';
		$ini += strlen($start);
		$len = strpos($string, $end, $ini) - $ini;
		return substr($string, $ini, $len);
	}
?>
<!DOCTYPE HTML>
<html>

<head>
	<title>C3 Starmap / Season History</title>
	<meta http-equiv='Content-Type' content='text/html;charset=utf-8'/>

	<style type='text/css'>
		* {
			box-sizing: border-box;
		}
		.not-selectable {
		  -webkit-touch-callout: none;
		  -webkit-user-select: none;
		  -khtml-user-select: none;
		  -moz-user-select: none;
		  -ms-user-select: none;
		  user-select: none;
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
			background-color: #111;
			margin-top: 0px;
			margin-bottom: 0px;
			margin-left: 0px;
			margin-right: 0px;
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
		.map_preview_selector {
			background:#666;
			padding-left:8px;
			padding-right:8px;
		}
		.map_preview_selector:hover {
			background:#888;
			padding-left:8px;
			padding-right:8px;
		}
		.map_preview_selector:active {
			background:#bbb;
			padding-left:8px;
			padding-right:8px;
		}
		.map_preview_selector_selected {
			background:#bbb;
			padding-left:8px;
			padding-right:8px;
		}
		.map_preview_selector_switch {
			background:#444;
			padding-left:8px;
			padding-right:8px;
		}
		.map_preview_selector_switch:hover {
			background:#888;
			padding-left:8px;
			padding-right:8px;
		}
		.map_preview_selector_switch:active {
			background:#bbb;
			padding-left:8px;
			padding-right:8px;
		}
	</style>

	<script type='text/javascript' src='https://www.clanwolf.net/jscripts/ZoomifyImageViewerExpress-min.js'></script>
	<script>
		var currentButtonIndex = 0;

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
			glass.addEventListener("touchmove", moveMagnifier);
			img.addEventListener("mousemove", moveMagnifier);
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

		function replacePreviewImage(v) {
			var imgPreview = document.getElementById("map_preview");
			imgPreview.src = v
		}

		function replaceMapImage(v, id) {
			var selectors = document.getElementsByName("selector");
			var imgMap = document.getElementById("map");
			var selectedCell = document.getElementById(id);
			var glass = document.getElementById("gla");

			currentButtonIndex = id;
			console.log(currentButtonIndex);

			imgMap.src = v;
			glass.style.backgroundImage = "url('" + v + "')";

			for (let i = 0; i < selectors.length; i++) {
				if (selectedCell !== selectors[i]) {
					if (selectors[i] !== null && selectors[i] !== 'undefined') {
						selectors[i].classList.remove('map_preview_selector_selected');
						selectors[i].classList.add('map_preview_selector');
					}
				} else {
					if (selectors[i] !== null && selectors[i] !== 'undefined') {
						selectors[i].classList.remove("map_preview_selector");
						selectors[i].classList.add('map_preview_selector_selected');
					}
				}
			}
		}
		function selectorMoveForward() {
			currentButtonIndex = currentButtonIndex + 1;
			if (currentButtonIndex > maxSelectorCount) {
				currentButtonIndex = maxSelectorCount;
			}
			var elem = document.getElementById(currentButtonIndex);
			if (typeof elem.onclick == "function") {
				elem.onclick.apply(elem);
			}
		}
		function selectorMoveBack() {
			currentButtonIndex = currentButtonIndex - 1;
			if (currentButtonIndex < 0) {
				currentButtonIndex = 0;
			}
			var elem = document.getElementById(currentButtonIndex);
			if (typeof elem.onclick == "function") {
				elem.onclick.apply(elem);
			}
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
					<table width='100%' height='100%' cellspacing='0' cellpadding='0' class='not-selectable'>
						<tr height='150px'>
							<td width='5%' height='150px' align='left'><img id='map_preview' src='C3_S1_R1_map_history_preview.png' height='150px'></td>
							<td width='90%' height='150px' align='right' valign='top' rowspan='2'>
								<table width='100%'>
									<tr>
										<td colspan="5" align='center' style='padding-top:4px;padding-bottom:4px;' class='map_preview_selector_switch' onclick='selectorMoveBack();'>
											⏮
										</td>
										<td colspan="5" align='center' style='padding-top:4px;padding-bottom:4px;' class='map_preview_selector_switch' onclick='selectorMoveForward();'>
											⏭
										</td>
									</tr>
									<tr>
<?php
	$dir = dirname(__FILE__);
	$files = array_diff(scandir($dir), array('.', '..'));
	sort($files, SORT_NATURAL);
	if (count($files) > 0) {
		$co = 0;
		$co2 = 0;

		foreach ($files as $value) {
			if (str_contains($value, '_R') && str_contains($value, '_map_history_preview.png')) {
				if ($seasonId === '') {
					$seasonId = get_string_between($value, 'C3_S', '_R');
					$animatedMapFileName = 'C3_S'.$seasonId.'_map_animated.gif';
				}

				$imgMapValue = str_replace("_preview", "", $value);
				$number = str_replace("C3_S".$seasonId."_", "", $value);
				$number = str_replace("_map_history.png", "", $number);
				$number = str_replace("_map_history_preview.png", "", $number);
				echo "										<td align='center' id='".$co2."' name='selector' class='map_preview_selector' onclick='replaceMapImage(\"".$imgMapValue."\", ".$co2.");' onmouseover='replacePreviewImage(\"".$value."\");'>".$number."</td>\n";
				if ($co >= 9) {
					echo "									</tr>\n";
					echo "									<tr>\n";
					$co = -1;
				}
				$co = $co + 1;
				$co2 = $co2 + 1;
			}
		}
	}
?>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="1" align='center' style='padding-top:4px;padding-bottom:4px;' class='map_preview_selector_switch' onmouseover='startHistoryAnimation();'>
							▶
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
						<img id="map" src='C3_S1_R1_map_history.png' width='100%'>
					</div>
				</td>
			</tr>
		</table>
	</div>

	<script>
		var maxSelectorCount = <?php echo $co2 - 1; ?>;

		function initMagnifier() {
			magnify("map", 3);
			hideMagnifier();
			document.getElementById("magnifier").onmouseover=showMagnifier;
			document.getElementById("magnifier").onmouseout=hideMagnifier;
        }
		function startHistoryAnimation() {
			var imgPreview = document.getElementById("map_preview");
			imgPreview.src = '<?php echo $animatedMapFileName ?>';
		}
	    window.onload = function () {
			var firstSelector = document.getElementById("0");
			firstSelector.classList.remove("map_preview_selector");
			firstSelector.classList.add('map_preview_selector_selected');

			initMagnifier();
			Z.showImage('myContainer', 'https://c3.clanwolf.net/seasonhistory/C3_map_3059.zif', 'zSkinPath=https://www.clanwolf.net/jscripts/zoomify/Assets/Skins/Default&zNavigatorVisible=0&zInitialX=5545&zInitialY=8150&zInitialZoom=175&zMaxZoom=300&zToolbarVisible=0&zToolbarBackgroundVisible=0&zNavigatorVisible=0&zLogoVisible=0&zMinimizeVisible=0&zHelpVisible=0');
		}
		window.onresize = function () {
			window.location.reload();
		}
	</script>
</body>

</html>
