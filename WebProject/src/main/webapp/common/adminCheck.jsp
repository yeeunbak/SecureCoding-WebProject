<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String loginId = (String) session.getAttribute("loginId");
    String loginName = (String) session.getAttribute("loginName");
    String loginRole = (String) session.getAttribute("loginRole");

    if (loginId == null) {
        response.sendRedirect(request.getContextPath() + "/member/login.jsp");
        return;
    }

    if (!"ADMIN".equals(loginRole)) {
        out.println("<script>");
        out.println("alert('관리자만 접근할 수 있습니다.');");
        out.println("location.href='" + request.getContextPath() + "/main.jsp';");
        out.println("</script>");
        return;
    }
%>