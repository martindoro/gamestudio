drawScoreTable();

function drawScoreTable() {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/rest/score/mines",
		dataType : "json"
	}).done(function(receivedData) {
		var template = $("#tmplScores").html();
		var rhtml = Mustache.render(template, receivedData);
		$("#scoreTable").html(rhtml);
	}).fail(function() {
		$("#scoreTable").html("<p>Sorry, unable to get data.</p>");
	});
}

$("#btSaveScore").click(function() {
	var score = parseInt($("#inNewScore").val().trim());
	console.log(score);

	if (isNaN(score)) {
		window.alert("Bad input!");
		return;
	}
	
	var dataToSend = {
			username:"Anonymous",
			game:"mines",
			value:score
	}
	
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/rest/score",
		contentType: "application/json",
		data: JSON.stringify(dataToSend)
	}).done(function() {
		drawScoreTable();
	}).fail(function() {
		window.alert("Unable to send data!");
	});
});