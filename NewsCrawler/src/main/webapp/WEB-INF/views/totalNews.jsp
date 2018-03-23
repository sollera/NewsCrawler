<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="viewport" content="width=device-width, user-scalable=no">

<title>News</title>

<!-- 페이징을 위한 부트스트랩 css 호출 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<script>
setInterval(function(){
	$.ajax({
		type: "get",
		url: "/crawler/topSite.do",
		success: function(data){
			var num = 1;
			var topSite = "<center><strong>열일하는 신문사 랭킹</strong></center><br />";
			topSite += "<table border='0' align='center'>";
			for(var i in data){
				if(data[i].cnt != 0){
					topSite += "<tr><td>"+num+". "+data[i].col+"</td><td align='right'>"+data[i].cnt+"</td></tr>";
					if(i == 2) break;
				}else{
					topSite += "<tr><td colspan='2'>기사 없음</td></tr>";
				}
				num++;
			}
			topSite += "</table>";
			$("#hot_site").html(topSite);
		},error: function(){
			$("#hot_site").html("조회 실패");
		}
	});
	$.ajax({
		type: "get",
		url: "/crawler/topType.do",
		success: function(data){
			var num = 1;
			var topSite = "<center><strong>오늘의 핫 카테고리</strong></center><br />";
			topSite += "<table border='0' align='center'>";
			for(var i in data){
				if(data[i].cnt != 0){
					topSite += "<tr><td>"+num+". "+data[i].col+"</td><td align='right'>"+data[i].cnt+"</td></tr>";
					if(i == 2) break;
				}else{
					topSite += "<tr><td colspan='2'>기사 없음</td></tr>";
				}
				num++;
			}
			topSite += "</table>";
			$("#hot_type").html(topSite);
		},error: function(){
			$("#hot_type").html("조회 실패");
		}
	});
},5000);

</script>

</head>
<body>
<div style="background:#EAEAEA;font-size:20px;text-align:center;">
신문 모아보기
</div>
<br />

<div style="width:15%;float:left;">&nbsp;</div>

<div style="float:left;width:70%;">

<!-- 뉴스 목록 -->
<div class="container" style="width:100%;">
	<table class="table table-hover">
		<colgroup>
			<col width="15%">
			<col width="70%">
			<col width="15%">
		</colgroup>
		<thead>
			<tr>
				<th style="text-align:center;min-width:72px;">site</th>
				<th style="text-align:center;">title</th>
				<th style="text-align:center;min-width:127px;">date</th>
			</tr>
		</thead>
		<tbody id="tb_newsList">
			<c:forEach items="${newsList}" var="list">
				<tr>
					<td style="text-align:center;">${list.site}</td>
					<td><span style="background:#FF6C6C;color:white;border-radius:4px;padding:1px;">${list.type}</span>&nbsp;<a href="${list.newsURL}">${list.title}</a></td>
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
				<li class="page-item"><a class="page-link" href="/crawler/news/${firstPage-10}">&laquo;</a></li>
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
						<c:otherwise><li class="page-item"><a class="page-link" href="/crawler/news/${pgN}">${pgN}</a></li></c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<c:forEach begin="${firstPage}" end="${lastPage}" var="pgN">
					<c:choose>
						<c:when test="${pgN eq curPage}"><li class="page-item active"><a class="page-link">${pgN}<span class="sr-only">(current)</span></a></li></c:when>
						<c:otherwise><li class="page-item"><a class="page-link" href="/crawler/news/${pgN}">${pgN}</a></li></c:otherwise>
					</c:choose>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		<!-- 다음 페이지 블록으로 이동 버튼 -->
		<c:choose>
			<c:when test="${next eq 'yes'}">
				<li class="page-item"><a class="page-link" href="/crawler/news/${firstPage+10}">&raquo;</a></li>
			</c:when>
			<c:otherwise>
				<li class="page-item disabled"><a class="page-link">&raquo;</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>

</div>

<div style="width:15%;float:left;">

<div id="hot_site" style="background:#D9E5FF;margin-left:auto;margin-right:auto;margin-top:50px;width:140px;height:130px;padding-top:10px;border-radius:5px;"></div>
<div id="hot_type" style="background:#D9E5FF;margin-left:auto;margin-right:auto;margin-top:50px;width:140px;height:130px;padding-top:10px;border-radius:5px;"></div>

</div>

</body>
</html>