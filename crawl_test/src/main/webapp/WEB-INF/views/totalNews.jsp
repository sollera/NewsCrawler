<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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
<div id="status" width="100%"></div>
<script>
	status();
	//setInterval(status, 5000);
</script>
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
</body>
</html>