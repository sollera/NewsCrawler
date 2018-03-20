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
function status(){
	$.ajax({
		type: "get",
		url: "/ctest/status.do",
		success: function(result){
			var chosun = "class='"; var chosunStat = "";
			var donga = "class='"; var dongaStat = "";
			var seoul = "class='"; var seoulStat = "";
			var ytn = "class='"; var ytnStat = "";
			var segye = "class='"; var segyeStat = "";
			var newdaily = "class='"; var newdailyStat = "";
			var mTime = "";
			for(var i in result){
				if(result[i].chosun == 0) {chosun += "danger'"; chosunStat="크롤러 서버가 꺼져있거나 크롤링 중에 문제가 발생했습니다.";}
				else if(result[i].chosun == 1) {chosun += "warning'"; chosunStat="크롤링을 시작했습니다.";}
				else if(result[i].chosun == 2) {chosun += "success'"; chosunStat="크롤링을 성공적으로 완료했습니다.";}
				if(result[i].donga == 0) {donga += "danger'"; dongaStat="크롤러 서버가 꺼져있거나 크롤링 중에 문제가 발생했습니다.";}
				else if(result[i].donga == 1) {donga += "warning'"; dongaStat="크롤링을 시작했습니다.";}
				else if(result[i].donga == 2) {donga += "success'"; dongaStat="크롤링을 성공적으로 완료했습니다.";}
				if(result[i].seoul == 0) {seoul += "danger'"; seoulStat="크롤러 서버가 꺼져있거나 크롤링 중에 문제가 발생했습니다.";}
				else if(result[i].seoul == 1) {seoul += "warning'"; seoulStat="크롤링을 시작했습니다.";}
				else if(result[i].seoul == 2) {seoul += "success'"; seoulStat="크롤링을 성공적으로 완료했습니다.";}
				if(result[i].ytn == 0) {ytn += "danger'"; ytnStat="크롤러 서버가 꺼져있거나 크롤링 중에 문제가 발생했습니다.";}
				else if(result[i].ytn == 1) {ytn += "warning'"; ytnStat="크롤링을 시작했습니다.";}
				else if(result[i].ytn == 2) {ytn += "success'"; ytnStat="크롤링을 성공적으로 완료했습니다.";}
				if(result[i].segye == 0) {segye += "danger'"; segyeStat="크롤러 서버가 꺼져있거나 크롤링 중에 문제가 발생했습니다.";}
				else if(result[i].segye == 1) {segye += "warning'"; segyeStat="크롤링을 시작했습니다.";}
				else if(result[i].segye == 2) {segye += "success'"; segyeStat="크롤링을 성공적으로 완료했습니다.";}
				if(result[i].newdaily == 0) {newdaily += "danger'"; newdailyStat="크롤러 서버가 꺼져있거나 크롤링 중에 문제가 발생했습니다.";}
				else if(result[i].newdaily == 1) {newdaily += "warning'"; newdailyStat="크롤링을 시작했습니다.";}
				else if(result[i].newdaily == 2) {newdaily += "success'"; newdailyStat="크롤링을 성공적으로 완료했습니다.";}
				mTime = result[i].modifyTime;
			}
			var status_tb = "<tr class='active'><td style='text-align:center;'>신문사</td><td style='text-align:center;'>상태</td></tr>";
			status_tb += "<tr "+chosun+"><td>조선일보</td><td>"+chosunStat+"</td></tr>";
			status_tb += "<tr "+chosun+"><td>동아일보</td><td>"+dongaStat+"</td></tr>";
			status_tb += "<tr "+chosun+"><td>서울신문</td><td>"+seoulStat+"</td></tr>";
			status_tb += "<tr "+chosun+"><td>YTN</td><td>"+ytnStat+"</td></tr>";
			status_tb += "<tr "+chosun+"><td>세게일보</td><td>"+segyeStat+"</td></tr>";
			status_tb += "<tr "+chosun+"><td>NewDaily</td><td>"+newdailyStat+"</td></tr>";
			status_tb += "<tr class='info'><td colspan='2' style='text-align:right;'>"+mTime+"</td></tr>";
			
			$("#tb_status").html(status_tb);
		},error: function(){
			
		}
	});
}
</script>

</head>
<body>
<div style="background:#EAEAEA;font-size:20px;text-align:center;">
뉴스 크롤러
</div>
<br>
<table style='width:500px;margin-left:auto;margin-right:auto;' class='table table-bordered'>
<colgroup>
	<col width="100px">
	<col width="400px">
</colgroup>
<tbody id="tb_status">
	<tr class="active"><td style='text-align:center;'>신문사</td><td style='text-align:center;'>상태</td></tr>
	<tr><td style='text-align:center;'>조선일보</td><td>크롤러 서버가 동작하지않습니다.</td></tr>
	<tr><td style='text-align:center;'>동아일보</td><td>크롤러 서버가 동작하지않습니다.</td></tr>
	<tr><td style='text-align:center;'>서울신문</td><td>크롤러 서버가 동작하지않습니다.</td></tr>
	<tr><td style='text-align:center;'>YTN</td><td>크롤러 서버가 동작하지않습니다.</td></tr>
	<tr><td style='text-align:center;'>세계일보</td><td>크롤러 서버가 동작하지않습니다.</td></tr>
	<tr><td style='text-align:center;'>NewDaily</td><td>크롤러 서버가 동작하지않습니다.</td></tr>
	<tr class='info'><td colspan='2' style='text-align:right;'>&nbsp;</td></tr>
</tbody>
</table>
<script>
	status();
	setInterval(status, 5000);
</script>

<!-- 크롤링 서버 온/오프 기능 추가하기 -->

</body>
</html>