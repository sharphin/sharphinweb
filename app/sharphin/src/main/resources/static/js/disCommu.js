let font = "Varela Round";
let font_color = "#000000";
let back_color = "#ffffff";
let font_size = 20;
let speed = 20;
function setFont() {
	font = document.getElementById("fontSelect").value;
}
function setColor() {
	font_color = document.getElementById("inputfontcolor").value;
}
function setBackColor() {
	back_color = document.getElementById("inputbackcolor").value;
}
function setFontSize() {
	font_size = document.getElementById("inputfontsize").value;
}
function setSpeed() {
	speed = document.getElementById("inputspeed").value;
}

function submit_text() {
	let text = document.getElementById("inputPost").value;
    let random = Math.floor( Math.random() * 600);
 
    let element = document.getElementById("center");
	insertTag = '<div id = "scroll"><span id = "scrollspan" style = "margin-top: '+random+'px;font-family: '+font+';\
	color: '+font_color+';background-color: '+back_color+';font-size: '+font_size+'px;animation : scroll01 '+speed+'s linear;">'+text+'\
	</span></div>';
	element.insertAdjacentHTML('afterbegin',insertTag);
	let el = document.getElementById("scrollspan");
	el.addEventListener('animationend', () => {
		console.log("end");
		el.remove();
	})
}
window.onload = function () {
	document.body.oncopy = function(event) {
		event.preventDefault();
	}
}
 