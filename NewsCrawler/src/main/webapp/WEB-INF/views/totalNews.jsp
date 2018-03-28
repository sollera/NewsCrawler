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
					topSite += "<tr><td>"+num+". "+data[i].col+"</td><td align='right'>&nbsp;"+data[i].cnt+"</td></tr>";
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
					topSite += "<tr><td>"+num+". "+data[i].col+"</td><td align='right'>&nbsp;"+data[i].cnt+"</td></tr>";
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
},50000);

function filterOpen(){
	if($("#tb_filter").css("display") == "none") $("#tb_filter").css("display","");
	else {
		$("#tb_filter").css("display","none");
		CheckSite(true);
		CheckType(true);
	}
}

function filterApply(){
	var types = "";
	var types1 = "";
	var types2 = "";
	
	var chkS = document.getElementsByName("chk_site[]");
	var chkT = document.getElementsByName("chk_type[]");
	
	var go1 = false;
	var go2 = false;
	
	if(document.getElementById("chk_siteAll").checked != true) {
		for(var i = 0; i < chkS.length; i++){
			if(chkS[i].checked == true) {
				types1 += "-"+chkS[i].value;
				go1 = true;
			}
		}
		types1 = types1.substring(1).trim();
		types += "&s="+types1;
	} else go1 = true;
	if(document.getElementById("chk_typeAll").checked != true){
		for(var j = 0; j < chkT.length; j++){
			if(chkT[j].checked == true) {
				types2 += "-"+chkT[j].value;
				go2 = true;
			}
		}
		types2 = types2.substring(1).trim();
		types += "&c="+types2;
	} else go2 = true;
	
	
	if(go1 == true && go2 == true) location.href="/crawler/news?p=1"+types;
}

function CheckSite(check){
	var chkS = document.getElementsByName("chk_site[]");
	
	if(check == false){
		for(var i=0; i<chkS.length;i++){ 
			chkS[i].checked = true;     //모두 체크
		}
	}else if(check == true){
		for(var i=0; i<chkS.length;i++){   
			chkS[i].checked = false;     //모두 해제
		}
	}else{
		alert("그딴게 어딨어!");
	}
}

function CheckType(check){
	var chkT = document.getElementsByName("chk_type[]");
	
	if(check == false){
		check = true;
		for(var i=0; i<chkT.length;i++){ 
			chkT[i].checked = true;     //모두 체크
		}
	}else if(check == true){
		check = false;
		for(var i=0; i<chkT.length;i++){   
			chkT[i].checked = false;     //모두 해제
		}
	}else{
		alert("그런거 없다니까?");
	}
}

function CheckAllSite(obj){
	if(obj == true){
		var chkS = document.getElementsByName("chk_site[]");
		for(var i = 0; i < chkS.length; i++){
			chkS[i].checked = false;
		}
	}else if(obj == false){
		document.getElementById("chk_siteAll").checked = false;
	}else{
		alert("???");
	}
}
function CheckAllType(obj){
	if(obj == true){
		var chkT = document.getElementsByName("chk_type[]");
		for(var i = 0; i < chkT.length; i++){
			chkT[i].checked = false;
		}
	}else if(obj == false){
		document.getElementById("chk_typeAll").checked = false;
	}else{
		alert("???");
	}
}

</script>

</head>
<body>
<div style="background:#EAEAEA;font-size:20px;text-align:center;">
신문 모아보기
</div>

<div style="width:15%;float:left;">&nbsp;</div>

<div style="float:left;width:70%;min-width:840px;">

<div style="width:100%;background:#B3AEFF;padding-bottom:3px;">

<table border='0' style='color:white;width:100%;height:30px;'><tr>
<td style='valign:middle;align:center;width:100px;padding-left:10px;'>필터&nbsp;&nbsp;&nbsp;&gt;&gt;</td>
<td><button type='button' style='color:white;background:#B3AEFF;font-size:5px;border-style:dotted;' onclick='filterOpen()'>추가</button></td>
</tr></table>

<div id="tb_filter" style="width:100%;background:#D7D2FF;display:none;">

<table border='0' align="center" style="width:90%;">
<tr><td style="width:100px;">신문사</td>
<td id="td_siteFilter">
<input type="checkbox" id="chk_siteAll" onclick="CheckAllSite(true)" value="site전체" checked="checked" />전체
<input type="checkbox" id="chk_chosun" name="chk_site[]" style="margin-left:5px;" onclick="CheckAllSite(false)" value="조선일보" />조선일보
<input type="checkbox" id="chk_donga" name="chk_site[]" style="margin-left:5px;" onclick="CheckAllSite(false)" value="동아일보" />동아일보
<input type="checkbox" id="chk_seoul" name="chk_site[]" style="margin-left:5px;" onclick="CheckAllSite(false)" value="서울신문" />서울신문
<input type="checkbox" id="chk_ytn" name="chk_site[]" style="margin-left:5px;" onclick="CheckAllSite(false)" value="YTN" />YTN
<input type="checkbox" id="chk_segye" name="chk_site[]" style="margin-left:5px;" onclick="CheckAllSite(false)" value="세계일보" />세계일보
<input type="checkbox" id="chk_hangyeorye" name="chk_site[]" style="margin-left:5px;" onclick="CheckAllSite(false)" value="한겨례" />한겨례
</td></tr>
<tr><td>카테고리</td>
<td id="td_typeFilter">
<input type="checkbox" id="chk_typeAll" onclick="CheckAllType(true)" value="type전체" checked="checked" />전체
<input type="checkbox" id="chk_politics" name="chk_type[]" style="margin-left:5px;" onclick="CheckAllType(false)" value="정치" />정치
<input type="checkbox" id="chk_economy" name="chk_type[]" style="margin-left:5px;" onclick="CheckAllType(false)" value="경제" />경제
<input type="checkbox" id="chk_society" name="chk_type[]" style="margin-left:5px;" onclick="CheckAllType(false)" value="사회" />사회
<input type="checkbox" id="chk_culture" name="chk_type[]" style="margin-left:5px;" onclick="CheckAllType(false)" value="문화" />문화
<input type="checkbox" id="chk_opinion" name="chk_type[]" style="margin-left:5px;" onclick="CheckAllType(false)" value="사설" />사설
<input type="checkbox" id="chk_sportEntertainment" name="chk_type[]" style="margin-left:5px;" onclick="CheckAllType(false)" value="스포츠','연예','스포츠ㆍ연예" />스포츠·연예
<input type="checkbox" id="chk_etc" name="chk_type[]" style="margin-left:5px;" onclick="CheckAllType(false)" value="기타" />기타
</td></tr>
<tr><td colspan="2" align="center">
<input type="button" style="height:23px;text-size:15px;padding-top:0px;padding-bottom:0px;background:#4742DB;color:#C5C0FF;border-style:none;" onclick="filterApply()" value="적용">&nbsp;
<button type="button" style="height:23px;text-size:15px;padding-top:0px;padding-bottom:0px;background:#4742DB;color:#C5C0FF;border-style:none;" onclick="filterOpen()">취소</button>
</td></tr>
</table>

</div>

</div>
<br />

<!-- 뉴스 목록 -->
<div class="container" style="width:100%;">
	<table class="table table-hover">
		<colgroup>
			<col width="14%">
			<col width="70%">
			<col width="16%">
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
					<td style="min-width:567px;max-width:610px;text-overflow:ellipsis;white-space:nowrap;word-wrap:normal;overflow:hidden;">
						<span style="background:#FF6C6C;color:white;border-radius:4px;padding:1px;">${list.type}</span>&nbsp;<a href="${list.newsURL}">${list.title}</a>
					</td>
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
				<li class="page-item"><a class="page-link" href="/crawler/news?p=${firstPage-10}" name="paging[]">&laquo;</a></li>
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
						<c:otherwise><li class="page-item"><a class="page-link" href="/crawler/news?p=${pgN}" name="paging[]">${pgN}</a></li></c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<c:forEach begin="${firstPage}" end="${lastPage}" var="pgN">
					<c:choose>
						<c:when test="${pgN eq curPage}"><li class="page-item active"><a class="page-link">${pgN}<span class="sr-only">(current)</span></a></li></c:when>
						<c:otherwise><li class="page-item"><a class="page-link" href="/crawler/news?p=${pgN}" name="paging[]">${pgN}</a></li></c:otherwise>
					</c:choose>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		<!-- 다음 페이지 블록으로 이동 버튼 -->
		<c:choose>
			<c:when test="${next eq 'yes'}">
				<li class="page-item"><a class="page-link" href="/crawler/news?p=${firstPage+10}" name="paging[]">&raquo;</a></li>
			</c:when>
			<c:otherwise>
				<li class="page-item disabled"><a class="page-link">&raquo;</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>
<script>
function changPagingURL(){
	var pagingLink = document.getElementsByName("paging[]");
	var url = document.URL;
	url = url.substring(url.indexOf("news?")+4);
	if(url.indexOf("&") != -1){
		url = url.substring(url.indexOf("&"));
		for(var i = 0; i < pagingLink.length; i++){
			var oriUrl = pagingLink[i].getAttribute("href");
			pagingLink[i].setAttribute("href",oriUrl+url);
		}
	}
}
changPagingURL();
</script>

</div>

<div style="width:15%;float:left;">
<br /><br />
<div id="hot_site" style="background:#D9E5FF;margin-left:auto;margin-right:auto;margin-top:50px;width:140px;height:130px;padding-top:10px;border-radius:5px;"></div>
<div id="hot_type" style="background:#D9E5FF;margin-left:auto;margin-right:auto;margin-top:50px;width:140px;height:130px;padding-top:10px;border-radius:5px;"></div>

</div>

</body>
</html>