drawScoreTable();

function drawScoreTable() {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/rest/score/sotb1",
		dataType : "json"
	}).done(function(receivedData) {
		var template = $("#tmplScores").html();
		var rhtml = Mustache.render(template, receivedData);
		$("#scoreTableSOTB1").html(rhtml);
	}).fail(function() {
		$("#scoreTableSOTB1").html("<p>Sorry, unable to get data.</p>");
	});
	
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/rest/score/sotb2",
		dataType : "json"
	}).done(function(receivedData) {
		var template = $("#tmplScores").html();
		var rhtml = Mustache.render(template, receivedData);
		$("#scoreTableSOTB2").html(rhtml);
	}).fail(function() {
		$("#scoreTableSOTB2").html("<p>Sorry, unable to get data.</p>");
	});
	
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/rest/score/sotb3",
		dataType : "json"
	}).done(function(receivedData) {
		var template = $("#tmplScores").html();
		var rhtml = Mustache.render(template, receivedData);
		$("#scoreTableSOTB3").html(rhtml);
	}).fail(function() {
		$("#scoreTableSOTB3").html("<p>Sorry, unable to get data.</p>");
	});
}

$("#btSaveScore").click(function() {
	var score = parseInt($("#inNewScore").val().trim());
	var username = $("#inUsername").val().trim();
	var g = document.getElementById("sel1");
	var game = g.options[g.selectedIndex].value;

	if (isNaN(score)) {
		window.alert("Missing or wrong score value!");
		return;
	}

	if (username=="") {
		window.alert("Missing username!");
		return;
	}
	var dataToSend = {
			username:username,
			game:game,
			value:score
	}
	
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/rest/score",
		contentType: "application/json",
		data: JSON.stringify(dataToSend)
	}).done(function() {
		drawScoreTable();
		$("#inUsername").val("");
		$("#inNewScore").val("");
	}).fail(function() {
		window.alert("Unable to send data!");
	});
});