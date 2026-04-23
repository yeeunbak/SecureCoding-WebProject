<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String loginId = (String) session.getAttribute("loginId");      // 로그인 여부 확인용
    String loginName = (String) session.getAttribute("loginName");  // 화면에 이름 출력용

    /* 로그아웃 상태 -> login.jsp로 이동 */
    if (loginId == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
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
        <input type="button" value="로그아웃" onclick="location.href='<%=request.getContextPath()%>/logout'">
    </div>
</div>
</body>
</html>