<%@ page import="java.sql.Connection" %>
<%@ page import="common.DBUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DB 연결 테스트</title>
</head>
<body>
<%
    Connection conn = null;

    try {
        conn = DBUtil.getConnection();
        out.println("<h2>DB 연결 성공</h2>");
    } catch (Exception e) {
        out.println("<h2>DB 연결 실패</h2>");
        out.println("<pre>");
        e.printStackTrace(new java.io.PrintWriter(out));
        out.println("</pre>");
    } finally {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
%>
</body>
</html>