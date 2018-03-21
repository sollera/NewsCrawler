<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>News</title>

<!-- 페이징을 위한 부트스트랩 css 호출 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<script>
//크롤링 상태 ajax
function status(){
	$.ajax({
	   type: "get",
	   //contentType: "application/json", ==> 생략가능(RestController이기때문에 가능)
	   url: "/ctest/status.do",
	   success: function(result){
	       var imgsrc1 = "<img src='https://github.com/sollera/NewsCrawler/blob/master/image/";
	       var imgsrc2 = ".png?raw=true' style='width:17px;height:17px;'>";
	       for(var i in result){
		       var output = "<table border='0' style='margin-left: auto; margin-right: auto;'><tr><td style='padding:5px;'>";
		       output += "조선일보&nbsp;" + imgsrc1;
		       if(result[i].chosun == 0) output += "red";
		       else if(result[i].chosun == 1) output += "yellow";
		       else if(result[i].chosun == 2) output += "green";
		       output += imgsrc2 + "</td><td style='padding:5px;'>";
		       output += "동아일보&nbsp;" + imgsrc1;
		       if(result[i].donga == 0) output += "red";
		       else if(result[i].donga == 1) output += "yellow";
		       else if(result[i].donga == 2) output += "green";
		       output += imgsrc2 + "</td><td style='padding:5px;'>";
		       output += "서울신문&nbsp;" + imgsrc1;
		       if(result[i].seoul == 0) output += "red";
		       else if(result[i].seoul == 1) output += "yellow";
		       else if(result[i].seoul == 2) output += "green";
		       output += imgsrc2 + "</td><td style='padding:5px;'>";
		       output += "YTN&nbsp;" + imgsrc1;
		       if(result[i].ytn == 0) output += "red";
		       else if(result[i].ytn == 1) output += "yellow";
		       else if(result[i].ytn == 2) output += "green";
		       output += imgsrc2 + "</td><td style='padding:5px;'>";
		       output += "세계일보&nbsp;" + imgsrc1;
		       if(result[i].segye == 0) output += "red";
		       else if(result[i].segye == 1) output += "yellow";
		       else if(result[i].segye == 2) output += "green";
		       output += imgsrc2 + "</td><td style='padding:5px;'>";
		       output += "NewDaily&nbsp;" + imgsrc1;
		       if(result[i].newdaily == 0) output += "red";
		       else if(result[i].newdaily == 1) output += "yellow";
		       else if(result[i].newdaily == 2) output += "green";
		       output += imgsrc2 + "</td></tr></table>";
	       }
	       
	       $("#status").html(output);	//준비되지 않았으면 빨간색, 갱신중이면 노란색, 갱신 완료상태면 녹색 동그라미로 표시
	   }, error: function() {}
	});
}

//새로운 뉴스가 있는지 확인
var newsCntOri = 0;
function newsCnt(){
	$.ajax({
		type: "get",
		//contentType: "application/json", ==> 생략가능(RestController이기때문에 가능)
		url: "/ctest/newsCnt.do",
		success: function(result){
		    if(result > newsCntOri){
				newsCntOri = result;
				$("#newsCnt").html("<a href='http://localhost:8082/ctest/news/1' style='color: red'>new</a>");
		    }else if(result == newsCntOri){
				$("#newsCnt").html("&nbsp;&nbsp;&nbsp;");
		    }
		}, error: function() {}
	});
}

//기사 ajax
//function news(firstnews){
//	$.ajax({
//	   type: "get",
//	   //contentType: "application/json", ==> 생략가능(RestController이기때문에 가능)
//	   url: "/ctest/news.do/"+firstnews,
//	   success: function(result){
//		   var outputN = "";
//	       for(var i in result){
//		       outputN += "<tr>";
//		       outputN += "<td style='text-align:center;'>"+result[i].site+"</td>";
//		       outputN += "<td><a href='"+result[i].newsURL+"'>"+result[i].title+"</td>";
//		       outputN += "<td style='text-align:center;'>"+result[i].enrollDT+"</td>";
//		       outputN += "</tr>";
//	       }
//	       $("#tb_newsList").html(outputN);
//	   }, error: function() {}
//	});
//}

</script>

</head>
<body>
<div style="background:#EAEAEA;font-size:20px;text-align:center;">
신문 모아보기
</div>
<!-- 크롤링 서버 상태 -->
<table border="0" style="margin-left: auto; margin-right: auto;"><tr><td>
<div id="status" style="float:left;margin-right:10px;"></div><div id="newsCnt" style="float:left;"></div>
</td></tr></table>
<script>
	status();
	setInterval(status, 5000);
	newsCnt();
	setInterval(newsCnt, 1000);
</script>

<!-- 뉴스 목록 -->
<div class="container">
	<table class="table table-hover">
		<colgroup>
			<col width="15%">
			<col width="70%">
			<col width="15%">
		</colgroup>
		<thead>
			<tr>
				<th style="text-align:center;">site</th>
				<th style="text-align:center;">title</th>
				<th style="text-align:center;">date</th>
			</tr>
		</thead>
		<tbody id="tb_newsList">
			<c:forEach items="${newsList}" var="list">
				<tr>
					<td style="text-align:center;">${list.site}</td>
					<td><a href="${list.newsURL}">${list.title}</a></td>
					<td style="text-align:center;">${list.enrollDT}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<!-- 페이징 -->
<div align="center">
	<ul class="pagination">
		<!-- 이전 페이지 블록으로 이동 버튼 -->
		<c:choose>
			<c:when test="${previous eq 'yes'}">
				<li class="page-item"><a class="page-link" href="/ctest/news/${firstPage-10}">&laquo;</a></li>
			</c:when>
			<c:otherwise>
				<li class="page-item disabled"><a class="page-link">&laquo;</a></li>
			</c:otherwise>
		</c:choose>
		<!-- 페이지 번호(다음 페이지 번호 유/무) -->
		<c:choose>
			<c:when test="${next eq 'yes'}">
				<c:forEach begin="${firstPage}" end="${firstPage+9}" var="pgN">
					<c:choose>
						<c:when test="${pgN eq curPage}"><li class="page-item active"><a class="page-link">${pgN}<span class="sr-only">(current)</span></a></li></c:when>
						<c:otherwise><li class="page-item"><a class="page-link" href="/ctest/news/${pgN}">${pgN}</a></li></c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<c:forEach begin="${firstPage}" end="${lastPage}" var="pgN">
					<c:choose>
						<c:when test="${pgN eq curPage}"><li class="page-item active"><a class="page-link">${pgN}<span class="sr-only">(current)</span></a></li></c:when>
						<c:otherwise><li class="page-item"><a class="page-link" href="/ctest/news/${pgN}">${pgN}</a></li></c:otherwise>
					</c:choose>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		<!-- 다음 페이지 블록으로 이동 버튼 -->
		<c:choose>
			<c:when test="${next eq 'yes'}">
				<li class="page-item"><a class="page-link" href="/ctest/news/${firstPage+10}">&raquo;</a></li>
			</c:when>
			<c:otherwise>
				<li class="page-item disabled"><a class="page-link">&raquo;</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>

</body>
</html>