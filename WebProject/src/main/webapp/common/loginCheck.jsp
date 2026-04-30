<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String loginId = (String) session.getAttribute("loginId"); // 로그인 여부 확인용
    String loginName = (String) session.getAttribute("loginName");  // 화면에 이름 출력용
    String loginRole = (String) session.getAttribute("loginRole");  // 사용자 OR 관리자

    if (loginId == null) {
        response.sendRedirect(request.getContextPath() + "/member/login.jsp");
        return;
    }
%>