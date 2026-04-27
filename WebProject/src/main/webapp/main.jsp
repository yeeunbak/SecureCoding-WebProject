<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="/common/loginCheck.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
</head>
<body>
<div class="container">
    <h2>메인 페이지</h2>
    <p><%= loginName %>님, 환영합니다.</p>
    <div class="btn-area">
    	<input type="button" value="게시판" onclick="location.href='<%=request.getContextPath()%>/board/boardList.jsp'">
    	<input type="button" value="회원정보 수정" onclick="location.href='<%=request.getContextPath()%>/editMember'">
        <input type="button" value="로그아웃" onclick="location.href='<%=request.getContextPath()%>/logout'">
    </div>
</div>
</body>
</html>