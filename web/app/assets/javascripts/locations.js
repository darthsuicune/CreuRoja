// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.

function loadScript() {
	var script = document.createElement("script");
	script.type = "text/javascript";
	script.src = "https://maps.google.com/maps/api/js?key=AIzaSyBfi8KVys-Vo9uea-i_IKNMRgfB6EXI5dk&v=3.13&sensor=false&libraries=geometry&callback=initialize";
	document.body.appendChild(script);
}