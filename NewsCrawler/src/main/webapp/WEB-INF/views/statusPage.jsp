<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Crawler Status</title>

<!-- 페이징을 위한 부트스트랩 css 호출 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

<script>
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
			
		},error: function(){
			alert("db 확인 ajax 에러");
		}
	});
},1000);

//크롤러 켜져있는지 체크해서 버튼 값 반영
var crawlerOn = "<button type='button' onclick='crawlerControll(&quot;on&quot;)'>Crawler On</button>&nbsp; &nbsp;<button type='button' disabled>Crawler Off</button>";
var crawlerOff = "<button type='button' disabled>Crawler On</button>&nbsp; &nbsp;<button type='button' onclick='crawlerControll(&quot;off&quot;)'>Crawler Off</button>";
function onOff(){
	$.ajax({
		type: "get",
		url: "/crawler/onOff.do",
		success: function(onOff){
			
			if(onOff == -1) $("#crawlerStartButton").html(crawlerOn);
			else $("#crawlerStartButton").html(crawlerOff);
			
		},error: function(){
			alert("크롤러 on/off 체크 실패");
		}
	});
}

//크롤러 시작/종료 버튼 컨트롤
function crawlerControll(power){
	if(power == "on"){
		$.ajax({
			type: "get",
			url: "/crawler/bot/start",
			success: function(){
				alert("크롤러 동작 시작");
				$("#crawlerStartButton").html(crawlerOff);
			},error: function(){
				alert("크롤러 컨트롤 실패");
			}
		});
	}else{
		$.ajax({
			type: "get",
			url: "/crawler/bot/end",
			success: function(){
				alert("크롤러 동작 종료");
				//쓰레드 종료 안되는 문제 해결 좀.....
				$("#crawlerStartButton").html(crawlerOn);
			},error: function(){
				alert("크롤러 컨트롤 실패");
			}
		});
	}
}

setInterval(function errCnt(){
	$.ajax({
		type: "get",
		url: "/crawler/errCnt.do",
		success: function(errData){
			for(var i in errData){
				if(errData[i].site == "조선일보") {
					$("#td_chosunSucc").html(errData[i].successAcc+"개 성공");
					$("#td_chosunErr").html(errData[i].errorAcc+"개 실패");
				}
				else if(errData[i].site == "동아일보") {
					$("#td_dongaSucc").html(errData[i].successAcc+"개 성공");
					$("#td_dongaErr").html(errData[i].errorAcc+"개 실패");
				}
				else if(errData[i].site == "서울신문") {
					$("#td_seoulSucc").html(errData[i].successAcc+"개 성공");
					$("#td_seoulErr").html(errData[i].errorAcc+"개 실패");
				}
				else if(errData[i].site == "YTN") {
					$("#td_ytnSucc").html(errData[i].successAcc+"개 성공");
					$("#td_ytnErr").html(errData[i].errorAcc+"개 실패");
				}
				else if(errData[i].site == "세계일보") {
					$("#td_segyeSucc").html(errData[i].successAcc+"개 성공");
					$("#td_segyeErr").html(errData[i].errorAcc+"개 실패");
				}
				else if(errData[i].site == "한겨례") {
					$("#td_hangyeoryeSucc").html(errData[i].successAcc+"개 성공");
					$("#td_hangyeoryeErr").html(errData[i].errorAcc+"개 실패");
				}
				else alert("그런게 있을리가 없어");
			}
			$("#td_errCheckTime").html("최근 수집 시간 : "+errData[0].dt);
		},error: function(){
			alert("크롤러 on/off 체크 실패");
		}
	});
},5000);


</script>

</head>
<body>
<div style="background:#EAEAEA;font-size:20px;text-align:center;">
뉴스 크롤러
</div>
<br />

<div id="crawlerStartButton"></div>
<script>onOff();</script>
<br />

<div style="margin-left:auto;margin-right:auto;width:960px;height:320px;">

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
		<tr><td colspan='2' style='border-top:1px solid;text-align:right' id="td_dbCheckTime"></td></tr>
	</tbody>
	</table>
	
	</div>
	
	<div style='float:left;margin-left:20px;'>
	
	<table style='width:350px;' class='table'>
	<colgroup>
		<col width="150px">
		<col width="100px">
		<col width="100px">
	</colgroup>
	<tbody>
		<tr class='active'><td colspan='3' style='text-align:center;'>크롤링 에러 확인</td></tr>
		<tr><td style='padding-left:20px;'>조선일보</td><td id="td_chosunSucc" style="text-align:right;padding-right:5px;"></td><td id="td_chosunErr" style="text-align:right;padding-right:5px;"></td></tr>
		<tr><td style='padding-left:20px;'>동아일보</td><td id="td_dongaSucc" style="text-align:right;padding-right:5px;"></td><td id="td_dongaErr" style="text-align:right;padding-right:5px;"></td></tr>
		<tr><td style='padding-left:20px;'>서울신문</td><td id="td_seoulSucc" style="text-align:right;padding-right:5px;"></td><td id="td_seoulErr" style="text-align:right;padding-right:5px;"></td></tr>
		<tr><td style='padding-left:20px;'>YTN</td><td id="td_ytnSucc" style="text-align:right;padding-right:5px;"></td><td id="td_ytnErr" style="text-align:right;padding-right:5px;"></td></tr>
		<tr><td style='padding-left:20px;'>세계일보</td><td id="td_segyeSucc" style="text-align:right;padding-right:5px;"></td><td id="td_segyeErr" style="text-align:right;padding-right:5px;"></td></tr>
		<tr><td style='padding-left:20px;'>한겨례</td><td id="td_hangyeoryeSucc" style="text-align:right;padding-right:5px;"></td><td id="td_hangyeoryeErr" style="text-align:right;padding-right:5px;"></td></tr>
		<tr><td colspan='3' style='border-top:1px solid;text-align:right' id="td_errCheckTime"></td></tr>
	</tbody>
	</table>
	
	</div>
	
	<div style='float:left;margin-left:20px;'>
	
	<table style='width:250px;' class='table'>
	<colgroup>
		<col width="200px">
		<col width="50px">
	</colgroup>
	<tbody>
		<tr class='active'><td colspan='2' style='text-align:center;'>새로 등록된 기사</td></tr>
		<tr><td style='padding-left:20px;'>조선일보</td><td>+1</td></tr>
		<tr><td style='padding-left:20px;'>동아일보</td><td>+20</td></tr>
		<tr><td style='padding-left:20px;'>서울신문</td><td>+3</td></tr>
		<tr><td style='padding-left:20px;'>YTN</td><td>+4</td></tr>
		<tr><td style='padding-left:20px;'>세계일보</td><td>+5</td></tr>
		<tr><td style='padding-left:20px;'>한겨례</td><td>+1</td></tr>
		<tr><td colspan='2' style='border-top:1px solid;text-align:right' id="td_"></td></tr>
	</tbody>
	</table>
	
	</div>

</div>

<div style="width:960px;margin-left:auto;margin-right:auto;background:#D5D5D5;">

에러 로그 ==> ajax 반영하기

</div>

</body>
</html>