<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>History Page</title>

<!-- chart.js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.bundle.min.js"></script>

<!-- JQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

</head>
<body>
<div style="width:1000px;margin-left:auto;margin-right:auto;margin-top:50px;">
	<font size="5">새로 수집한 데이터</font>
	<canvas id="lineChart" width="1000px" height="300px"></canvas>
</div>
<div style="width:1035px;margin-left:auto;margin-right:auto;margin-top:50px;">
	<font size="5">에러 발생 빈도</font>
	<div style="width:1035px;margin-top:10px;">
		<div style="width:170px;float:left;margin-right:3px;">
			<canvas id="pieChart_chosun" style="width:170px;height:220px;"></canvas>
		</div>
		<div style="width:170px;float:left;margin-right:3px;">
			<canvas id="pieChart_donga" style="width:170px;height:220px;"></canvas>
		</div>
		<div style="width:170px;float:left;margin-right:3px;">
			<canvas id="pieChart_seoul" style="width:170px;height:220px;"></canvas>
		</div>
		<div style="width:170px;float:left;margin-right:3px;">
			<canvas id="pieChart_ytn" style="width:170px;height:220px;"></canvas>
		</div>
		<div style="width:170px;float:left;margin-right:3px;">
			<canvas id="pieChart_segye" style="width:170px;height:220px;"></canvas>
		</div>
		<div style="width:170px;float:left;">
			<canvas id="pieChart_hangyeorye" style="width:170px;height:220px;"></canvas>
		</div>
	</div>
</div>

<script>
var time = 10000;

//라인 차트
var ctxLine = document.getElementById("lineChart").getContext("2d");
var lineChartStartSet = {
    type: 'line',
    data: {
        labels: [0],
        datasets: [{
            label: '조선일보',
            fill: false,
            data: [0],
            borderColor: 'rgba(255,99,132,1)',
            borderWidth: 1
        },
        {
            label: '동아일보',
            fill: false,
            data: [0],
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
        },
        {
            label: '서울신문',
            fill: false,
            data: [0],
            borderColor: 'rgba(255, 206, 86, 1)',
            borderWidth: 1
        },
        {
            label: 'YTN',
            fill: false,
            data: [0],
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
        },
        {
            label: '세계일보',
            fill: false,
            data: [0],
            borderColor: 'rgba(153, 102, 255, 1)',
            borderWidth: 1
        },
        {
            label: '한겨례',
            fill: false,
            data: [0],
            borderColor: 'rgba(255, 159, 64, 1)',
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        },
        responsive: false,
        animation: false
    }
};
var lineDataLen = 0;
var myLineChart = new Chart(ctxLine,lineChartStartSet);


//파이 차트 - 조선일보
var ctxPieChosun = document.getElementById("pieChart_chosun").getContext("2d");
var myPieChartChosun = new Chart(ctxPieChosun, {
    type: 'pie',
    data: {
		datasets: [{
			data: [0, 0],
			backgroundColor: [
				'rgba(255,99,132,1)',
				'rgba(54, 162, 235, 1)',
			]
		}],
		labels: [
			'#성공',
			'#실패'
		]
	},
    options: {
    	title: {
            display: true,
            text: '조선일보'
        },
    	legend: {
            display: true,
            position: 'bottom'
        },
    	responsive : false
    }
});
//파이 차트 - 동아일보
var ctxPieDonga = document.getElementById("pieChart_donga").getContext("2d");
var myPieChartDonga = new Chart(ctxPieDonga, {
    type: 'pie',
    data: {
		datasets: [{
			data: [0, 0],
			backgroundColor: [
				'rgba(255,99,132,1)',
				'rgba(54, 162, 235, 1)',
			]
		}],
		labels: [
			'#성공',
			'#실패'
		]
	},
    options: {
    	title: {
            display: true,
            text: '동아일보'
        },
    	legend: {
            display: true,
            position: 'bottom'
        },
    	responsive : false
    }
});
//파이 차트 - 서울신문
var ctxPieSeoul = document.getElementById("pieChart_seoul").getContext("2d");
var myPieChartSeoul = new Chart(ctxPieSeoul, {
    type: 'pie',
    data: {
		datasets: [{
			data: [0, 0],
			backgroundColor: [
				'rgba(255,99,132,1)',
				'rgba(54, 162, 235, 1)',
			]
		}],
		labels: [
			'#성공',
			'#실패'
		]
	},
    options: {
    	title: {
            display: true,
            text: '서울신문'
        },
    	legend: {
            display: true,
            position: 'bottom'
        },
    	responsive : false
    }
});
//파이 차트 - YTN
var ctxPieYtn = document.getElementById("pieChart_ytn").getContext("2d");
var myPieChartYtn = new Chart(ctxPieYtn, {
    type: 'pie',
    data: {
		datasets: [{
			data: [0, 0],
			backgroundColor: [
				'rgba(255,99,132,1)',
				'rgba(54, 162, 235, 1)',
			]
		}],
		labels: [
			'#성공',
			'#실패'
		]
	},
    options: {
    	title: {
            display: true,
            text: 'YTN'
        },
    	legend: {
            display: true,
            position: 'bottom'
        },
    	responsive : false
    }
});
//파이 차트 - 세계일보
var ctxPieSegye = document.getElementById("pieChart_segye").getContext("2d");
var myPieChartSegye = new Chart(ctxPieSegye, {
    type: 'pie',
    data: {
		datasets: [{
			data: [0, 0],
			backgroundColor: [
				'rgba(255,99,132,1)',
				'rgba(54, 162, 235, 1)',
			]
		}],
		labels: [
			'#성공',
			'#실패'
		]
	},
    options: {
    	title: {
            display: true,
            text: '세계일보'
        },
    	legend: {
            display: true,
            position: 'bottom'
        },
    	responsive : false
    }
});
//파이 차트 - 한겨례
var ctxPieHangyeorye = document.getElementById("pieChart_hangyeorye").getContext("2d");
var myPieChartHangyeorye = new Chart(ctxPieHangyeorye, {
    type: 'pie',
    data: {
		datasets: [{
			data: [0, 0],
			backgroundColor: [
				'rgba(255,99,132,1)',
				'rgba(54, 162, 235, 1)',
			]
		}],
		labels: [
			'#성공',
			'#실패'
		]
	},
    options: {
    	title: {
            display: true,
            text: '한겨례'
        },
    	legend: {
            display: true,
            position: 'bottom'
        },
    	responsive : false
    }
});

//line chart 값 변경
setInterval(function status(){
	$.ajax({
		type: "get",
		url: "/crawler/collectHistory.do",
		success: function(result){
			var site;
			var hour = 0;
			var cnt;
			
			var hourArray = [0];
			var chosun = [0];
			var donga = [0];
			var seoul = [0];
			var ytn = [0];
			var segye = [0];
			var hangyeorye = [0];
			
			for(var i in result){
				site = result[i].site;
				hour = result[i].hour;
				cnt = result[i].newDataSum;
				
				var chk = false;
				for(var i = 0; i < hourArray.length; i++){
					if(hour == hourArray[i]) chk = true;
				}
				if(chk == false) hourArray.push(hour);
				
				if(site == "조선일보"){
					chosun.push(cnt);
				}else if(site == "동아일보"){
					donga.push(cnt);
				}else if(site == "서울신문"){
					seoul.push(cnt);
				}else if(site == "YTN"){
					ytn.push(cnt);
				}else if(site == "세계일보"){
					segye.push(cnt);
				}else if(site == "한겨례"){
					hangyeorye.push(cnt);
				}
			}
			for(var i = 0; i < hourArray.length; i++){
				myLineChart.data.labels[i] = hourArray[i];
				myLineChart.data.datasets[0].data[i] = chosun[i];
				myLineChart.data.datasets[1].data[i] = donga[i];
				myLineChart.data.datasets[2].data[i] = seoul[i];
				myLineChart.data.datasets[3].data[i] = ytn[i];
				myLineChart.data.datasets[4].data[i] = segye[i];
				myLineChart.data.datasets[5].data[i] = hangyeorye[i];
			}
			myLineChart.update();
			
		}
	});
},time);

//pie 차트 값 변경
setInterval(function status(){
	$.ajax({
		type: "get",
		url: "/crawler/errorHistory.do",
		success: function(result){
			var site;
			var success;
			var error;
			
			for(var i in result){
				site = result[i].site;
				success = result[i].success;
				error = result[i].error;
				if(site == "조선일보"){
					myPieChartChosun.data.datasets[0].data[0] = success;
					myPieChartChosun.data.datasets[0].data[1] = error;
				}else if(site == "동아일보"){
					myPieChartDonga.data.datasets[0].data[0] = success;
					myPieChartDonga.data.datasets[0].data[1] = error;
				}else if(site == "서울신문"){
					myPieChartSeoul.data.datasets[0].data[0] = success;
					myPieChartSeoul.data.datasets[0].data[1] = error;
				}else if(site == "YTN"){
					myPieChartYtn.data.datasets[0].data[0] = success;
					myPieChartYtn.data.datasets[0].data[1] = error;
				}else if(site == "세계일보"){
					myPieChartSegye.data.datasets[0].data[0] = success;
					myPieChartSegye.data.datasets[0].data[1] = error;
				}else if(site == "한겨례"){
					myPieChartHangyeorye.data.datasets[0].data[0] = success;
					myPieChartHangyeorye.data.datasets[0].data[1] = error;
				}
			}
			
			myPieChartChosun.update();
			myPieChartDonga.update();
			myPieChartSeoul.update();
			myPieChartYtn.update();
			myPieChartSegye.update();
			myPieChartHangyeorye.update();
		}
	});
},time);

</script>

</body>
</html>