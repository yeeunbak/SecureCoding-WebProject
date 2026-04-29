<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/adminCheck.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
</head>
<body>

<div class="container">
    <h2>관리자 페이지</h2>

    <div class="btn-area">
        <input type="button" value="사용자 관리"
            onclick="location.href='<%=request.getContextPath()%>/admin/memberList.jsp'">

        <input type="button" value="등록된 게시글 관리"
            onclick="location.href='<%=request.getContextPath()%>/admin/boardList.jsp'">

        <input type="button" value="메인"
            onclick="location.href='<%=request.getContextPath()%>/main.jsp'">
    </div>
</div>

</body>
</html>