google.charts.load('current', {'packages':['sankey','corechart', 'bar', 'calendar']});
google.charts.setOnLoadCallback(drawCharts);

function drawCharts() {
	checkfieldDate("from");
	checkfieldDate("to");
	
	var params="";
	params+="?from="+escape( getFieldDate("from") );
	params+="&to="+escape( getFieldDate("to") );
	params+="&video="+escape( document.getElementById( "video" ).value );
	
	fetch('/api'+params).then(function(response) {
		response.json().then(function(value) { 
			drawChartP1( value.subsdia );
			drawChartP2( value.subssem );
			});
		});
	}

function drawChartP1( values ) {
	var head=[ ['Fecha', 'Suscriptores'] ];
	
	var data = google.visualization.arrayToDataTable(
		head.concat( values )
		);

  	var chart = new google.visualization.LineChart(document.getElementById('p1Chart'));
  	chart.draw(data, { });
	}

function drawChartP2( values ) {
	var head=[ ['Dia', 'Suscriptores'] ];
	
	var data = google.visualization.arrayToDataTable(
		head.concat( values )
		);
	
  	var chart = new google.charts.Bar(document.getElementById('p2Chart'));
  	chart.draw(data, { });
	}

function checkfieldDate( idElement ) {
	var el=document.getElementById( idElement );
	
	if ( el.value=='' ) return;
	if ( el.value.split('/').length!=3 ) { return el.value=''; }
	
	}

function getFieldDate( idElement ) {
	var el=document.getElementById( idElement );
	
	if ( el.value=='' ) return '';
	var d=el.value.split('/');
	
	return d[2]+'-'+d[1]+'-'+d[0];	
	}
