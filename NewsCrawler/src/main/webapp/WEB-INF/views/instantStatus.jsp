<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Instant Data Page</title>

<!-- 부트스트랩 css 호출 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<!-- JQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

<script>
//interval time
var time = 3000;

//크롤러 온/오프 버튼 초기값
function powerChk(power){
	if(power == 0) $("#btn_crawlerPower").html("START");
	else $("#btn_crawlerPower").html("STOP");
}
//크롤러 컨트롤 버튼 작동
function operateCrawler(){
	if($("#btn_crawlerPower").text() == "START") {
		$.ajax({
			type: "get",
			url: "/crawler/bot/start",
			success: function(){
				$("#btn_crawlerPower").html("STOP");
			}
		});
	} else {
		$.ajax({
			type: "get",
			url: "/crawler/bot/end",
			success: function(){
				$("#btn_crawlerPower").html("START");
			}
		});
	}
}

//서버상태 ajax
setInterval(function status(){
	$.ajax({
		type: "get",
		url: "/crawler/dbCheck.do",
		success: function(result){
			
			var red = "<img src='https://github.com/sollera/NewsCrawler/blob/master/image/red.png?raw=true' style='width:20px;height:20px;'>";
			var yellow = "<img src='https://github.com/sollera/NewsCrawler/blob/master/image/yellow.png?raw=true' style='width:20px;height:20px;'>";
			var green = "<img src='https://github.com/sollera/NewsCrawler/blob/master/image/green.png?raw=true' style='width:20px;height:20px;'>";
			var grey = "<img src='https://github.com/sollera/NewsCrawler/blob/master/image/grey.png?raw=true' style='width:20px;height:20px;'>";
			
			var chosun = "";
			var donga = "";
			var seoul = "";
			var ytn = "";
			var segye = "";
			var hangyeorye = "";
			var mTime = "";
			
			if(result.chosun == 0) chosun = red
			else if(result.chosun == 1) chosun = yellow;
			else if(result.chosun == 2) chosun = green;
			else if(result.chosun == -1) chosun = grey;
			
			if(result.donga == 0) donga = red
			else if(result.donga == 1) donga = yellow;
			else if(result.donga == 2) donga = green;
			else if(result.donga == -1) donga = grey;
			
			if(result.seoul == 0) seoul = red
			else if(result.seoul == 1) seoul = yellow;
			else if(result.seoul == 2) seoul = green;
			else if(result.seoul == -1) seoul = grey;
			
			if(result.ytn == 0) ytn = red
			else if(result.ytn == 1) ytn = yellow;
			else if(result.ytn == 2) ytn = green;
			else if(result.ytn == -1) ytn = grey;
			
			if(result.segye == 0) segye = red
			else if(result.segye == 1) segye = yellow;
			else if(result.segye == 2) segye = green;
			else if(result.segye == -1) segye = grey;
			
			if(result.hangyeorye == 0) hangyeorye = red
			else if(result.hangyeorye == 1) hangyeorye = yellow;
			else if(result.hangyeorye == 2) hangyeorye = green;
			else if(result.hangyeorye == -1) hangyeorye = grey;
			
			mTime = result.modifyTime;
			
			$("#td_chosun").html(chosun);
			$("#td_donga").html(donga);
			$("#td_seoul").html(seoul);
			$("#td_ytn").html(ytn);
			$("#td_segye").html(segye);
			$("#td_hangyeorye").html(hangyeorye);
			$("#td_dbCheckTime").html("최근 수집 시간 : "+mTime);

		}
	});
},time);

//걸린 시간
setInterval(function status(){
	$.ajax({
		type: "get",
		url: "/crawler/operatingTime.do",
		success: function(result){
			var site;
			var startTime;
			var endTime;
			var spendTime;
			var sumTime;
			var cnt;
			
			for(var i in result){
				site = result[i].site;
				startTime = result[i].startTime;
				endTime = result[i].endTime;
				spendTime = result[i].spendTime + " 초 &nbsp;";
				sumTime = result[i].sumTime;
				cnt = result[i].cnt;
				var avgTime = 0;
				if(cnt !== 0) {
					avgTime = sumTime / cnt;
					avgTime = avgTime.toFixed(2);
				}
				avgTime = avgTime + " 초 &nbsp;";
				
				if(startTime !== "" && startTime !== null) startTime = startTime.substring(11);
				if(endTime !== "" && endTime !== null) endTime = endTime.substring(11);
				
				if(site == "조선일보"){
					$("#td_chosun_start").html(startTime);
					$("#td_chosun_end").html(endTime);
					$("#td_chosun_taken").html(spendTime);
					$("#td_chosun_avg").html(avgTime);
				}else if(site == "동아일보"){
					$("#td_donga_start").html(startTime);
					$("#td_donga_end").html(endTime);
					$("#td_donga_taken").html(spendTime);
					$("#td_donga_avg").html(avgTime);
				}else if(site == "서울신문"){
					$("#td_seoul_start").html(startTime);
					$("#td_seoul_end").html(endTime);
					$("#td_seoul_taken").html(spendTime);
					$("#td_seoul_avg").html(avgTime);
				}else if(site == "YTN"){
					$("#td_ytn_start").html(startTime);
					$("#td_ytn_end").html(endTime);
					$("#td_ytn_taken").html(spendTime);
					$("#td_ytn_avg").html(avgTime);
				}else if(site == "세계일보"){
					$("#td_segye_start").html(startTime);
					$("#td_segye_end").html(endTime);
					$("#td_segye_taken").html(spendTime);
					$("#td_segye_avg").html(avgTime);
				}else if(site == "한겨례"){
					$("#td_hangyeorye_start").html(startTime);
					$("#td_hangyeorye_end").html(endTime);
					$("#td_hangyeorye_taken").html(spendTime);
					$("#td_hangyeorye_avg").html(avgTime);
				}
			}
		}
	});
},time);

//수집 기사 수
setInterval(function errLog(){
	$.ajax({
		type: "get",
		url: "/crawler/searchNews.do",
		success: function(result){
			var site;
			var target;
			var newData;
			var allData;
			
			for(var i in result){
				site = result[i].site;
				target = result[i].target + " 개 &nbsp;";
				newData = "+ " + result[i].newData + "&nbsp; &nbsp;";
				allData = result[i].allData + " 개 &nbsp;";
				
				if(site == "조선일보"){
					$("#td_chosun_target").html(target);
					$("#td_chosun_new").html(newData);
					$("#td_chosun_all").html(allData);
				}else if(site == "동아일보"){
					$("#td_donga_target").html(target);
					$("#td_donga_new").html(newData);
					$("#td_donga_all").html(allData);
				}else if(site == "서울신문"){
					$("#td_seoul_target").html(target);
					$("#td_seoul_new").html(newData);
					$("#td_seoul_all").html(allData);
				}else if(site == "YTN"){
					$("#td_ytn_target").html(target);
					$("#td_ytn_new").html(newData);
					$("#td_ytn_all").html(allData);
				}else if(site == "세계일보"){
					$("#td_segye_target").html(target);
					$("#td_segye_new").html(newData);
					$("#td_segye_all").html(allData);
				}else if(site == "한겨례"){
					$("#td_hangyeorye_target").html(target);
					$("#td_hangyeorye_new").html(newData);
					$("#td_hangyeorye_all").html(allData);
				}
			}
		}
	});
},time);

//에러로그
setInterval(function errLog(){
	$.ajax({
		type: "get",
		url: "/crawler/errLog.do",
		success: function(errlog){
			var log = "&nbsp; ** 뉴스 크롤러 동작 에러 로그("+today()+") **<br /><br />";
			for(var i in errlog){
				log += errlog[i].errTime+" "+errlog[i].site+" 크롤러 봇 기동 중 "+errlog[i].error+" 에러 발생<br />";
			}
			if(errlog == null) log += "에러 발생 기록 없음<br />";
			$("#errLog").html(log);
		},error: function(){
			
		}
	});
},time);
function today(){
	var today = new Date();
	var yyyy = today.getFullYear();
	var MM = today.getMonth()+1; //January is 0!
	var dd = today.getDate();
	
	if(dd<10) {
	    dd='0'+dd
	} 
	if(MM<10) {
	    MM='0'+MM
	} 
	
	return yyyy+"/"+MM+"/"+dd;
}

</script>

<style>
.td_center {
	text-align : center;
}
.td_right {
	text-align : right;
}
</style>

</head>
<body>

	<!-- 크롤러 파워 온/오프 버튼 -->
	<div style="width:190px;margin-left:auto;margin-right:auto;margin-top:50px;">
		<button type="button" id="btn_crawlerPower" style="width:190px;height:80px;font-size:50px;border-radius:10px;" onclick="operateCrawler()"></button>
	</div>
	<script>powerChk("${power}");</script>
  
 	<!-- 첫 줄 -->
  	<div style="margin-top:50px;width:1000px;margin-left:auto;margin-right:auto;">
	 	<!-- 크롤러 온/오프 상태표 -->
		<div style='float:left;margin-left:20px;'>
			<table style='width:250px;' class='table'>
			<colgroup>
				<col width="200px">
				<col width="50px">
			</colgroup>
			<tbody>
				<tr class='active'><td colspan='2' style='text-align:center;'>신문사 별 크롤러 상태</td></tr>
				<tr><td style='padding-left:20px;'>조선일보</td><td id="td_chosun"></td></tr>
				<tr><td style='padding-left:20px;'>동아일보</td><td id="td_donga"></td></tr>
				<tr><td style='padding-left:20px;'>서울신문</td><td id="td_seoul"></td></tr>
				<tr><td style='padding-left:20px;'>YTN</td><td id="td_ytn"></td></tr>
				<tr><td style='padding-left:20px;'>세계일보</td><td id="td_segye"></td></tr>
				<tr><td style='padding-left:20px;'>한겨례</td><td id="td_hangyeorye"></td></tr>
				<tr><td colspan='2' style='border-top:1px solid;text-align:right'></td></tr>
			</tbody>
			</table>
		</div>
		
		<!-- 크롤링 시작/종료/걸린시간/평균시간 -->
		<div style='float:left;margin-left:10px;'>
			<table style='width:400px;' class='table'>
			<colgroup>
				<col width="100px">
				<col width="100px">
				<col width="100px">
				<col width="100px">
			</colgroup>
			<tbody>
				<tr class='active'>
					<td style='text-align:center;'>시작 시간</td> <td style='text-align:center;'>종료 시간</td>
					<td style='text-align:center;'>걸린 시간</td> <td style='text-align:center;'>평균 시간</td>
				</tr>
				<tr>
					<td id="td_chosun_start" class="td_center">&nbsp;</td><td id="td_chosun_end" class="td_center"></td>
					<td id="td_chosun_taken" class="td_right"></td><td id="td_chosun_avg" class="td_right"></td>
				</tr>
				<tr>
					<td id="td_donga_start" class="td_center">&nbsp;</td><td id="td_donga_end" class="td_center"></td>
					<td id="td_donga_taken" class="td_right"></td><td id="td_donga_avg" class="td_right"></td></tr>
				<tr>
					<td id="td_seoul_start" class="td_center">&nbsp;</td><td id="td_seoul_end" class="td_center"></td>
					<td id="td_seoul_taken" class="td_right"></td><td id="td_seoul_avg" class="td_right"></td></tr>
				<tr>
					<td id="td_ytn_start" class="td_center">&nbsp;</td><td id="td_ytn_end" class="td_center"></td>
					<td id="td_ytn_taken" class="td_right"></td><td id="td_ytn_avg" class="td_right"></td></tr>
				<tr>
					<td id="td_segye_start" class="td_center">&nbsp;</td><td id="td_segye_end" class="td_center"></td>
					<td id="td_segye_taken" class="td_right"></td><td id="td_segye_avg" class="td_right"></td></tr>
				<tr>
					<td id="td_hangyeorye_start" class="td_center">&nbsp;</td><td id="td_hangyeorye_end" class="td_center"></td>
					<td id="td_hangyeorye_taken" class="td_right"></td><td id="td_hangyeorye_avg" class="td_right"></td></tr>
				<tr>
					<td colspan='4' style='border-top:1px solid;text-align:right'></td></tr>
			</tbody>
			</table>
		</div>
		
		<!-- 크롤링 수집 개수 / 새로운 기사 수 -->
		<div style='float:left;margin-left:10px;margin-right:20px;'>
			<table style='width:290px;' class='table'>
			<colgroup>
				<col width="100px">
				<col width="90px">
				<col width="100px">
			</colgroup>
			<tbody>
				<tr class='active' >
					<td style='text-align:center;'>수집 대상</td> <td style='text-align:center;'>새 기사</td> <td style='text-align:center;'>전체 기사</td>
				</tr>
				<tr><td id="td_chosun_target" class="td_right">&nbsp;</td><td id="td_chosun_new" class="td_right"></td><td id="td_chosun_all" class="td_right"></td></tr>
				<tr><td id="td_donga_target" class="td_right">&nbsp;</td><td id="td_donga_new" class="td_right"></td><td id="td_donga_all" class="td_right"></td></tr>
				<tr><td id="td_seoul_target" class="td_right">&nbsp;</td><td id="td_seoul_new" class="td_right"></td><td id="td_seoul_all" class="td_right"></td></tr>
				<tr><td id="td_ytn_target" class="td_right">&nbsp;</td><td id="td_ytn_new" class="td_right"></td><td id="td_ytn_all" class="td_right"></td></tr>
				<tr><td id="td_segye_target" class="td_right">&nbsp;</td><td id="td_segye_new" class="td_right"></td><td id="td_segye_all" class="td_right"></td></tr>
				<tr><td id="td_hangyeorye_target" class="td_right">&nbsp;</td><td id="td_hangyeorye_new" class="td_right"></td><td id="td_hangyeorye_all" class="td_right"></td></tr>
				<tr><td colspan='3' style='border-top:1px solid;text-align:right'></td></tr>
			</tbody>
			</table>
		</div>
	</div>
	
	<!-- 둘째 줄 -->
	<div id="errLog" style='width:1000px;min-height:300px;margin-left:auto;margin-right:auto;margin-top:400px;background:#E4E4E4;'></div>

</body>
</html>