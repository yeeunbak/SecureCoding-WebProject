<%@ page import="java.sql.*" %>
<%@ page import="common.DBUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/adminCheck.jsp" %>

<%
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    String sql =
        "SELECT USER_ID, USER_NAME, USER_EMAIL, USER_ROLE, " +
        "       TO_CHAR(REG_DATE, 'YYYY-MM-DD') AS REG_DATE " +
        "FROM TB_MEMBER " +
        "ORDER BY REG_DATE DESC";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사용자 관리</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
</head>

<body>

<div class="container">
    <h2>사용자 관리</h2>

    <table>
        <tr>
            <th>아이디</th>
            <th>이름</th>
            <th>이메일</th>
            <th>권한</th>
            <th>가입일</th>
            <th>관리</th>
        </tr>

<%
    try {
        conn = DBUtil.getConnection();
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        boolean hasData = false;

        while (rs.next()) {
            hasData = true;

            String userId = rs.getString("USER_ID");
            String userName = rs.getString("USER_NAME");
            String userEmail = rs.getString("USER_EMAIL");
            String userRole = rs.getString("USER_ROLE");
            String regDate = rs.getString("REG_DATE");
%>
        <tr>
            <td><%= userId %></td>
            <td><%= userName %></td>
            <td><%= userEmail %></td>
            <td>
    			<form action="<%=request.getContextPath()%>/admin/member/roleUpdate" method="post">
        			<input type="hidden" name="userId" value="<%= userId %>">

        			<select name="role">
            			<option value="USER" <%= "USER".equals(userRole) ? "selected" : "" %>>USER</option>
            			<option value="ADMIN" <%= "ADMIN".equals(userRole) ? "selected" : "" %>>ADMIN</option>
        			</select>

        			<input type="submit" value="변경">
    			</form>
			</td>
            <td><%= regDate %></td>
            <td>
    			<form action="<%=request.getContextPath()%>/admin/member/delete" method="post" style="display:inline;">
        			<input type="hidden" name="userId" value="<%= userId %>">
        			<input type="submit" value="계정삭제" onclick="return confirm('정말 이 사용자를 삭제하겠습니까?');">
    			</form>
			</td>
        </tr>
<%
        }

        if (!hasData) {
%>
        <tr>
            <td colspan="5">등록된 사용자가 없습니다.</td>
        </tr>
<%
        }

    } catch (Exception e) {
        e.printStackTrace();
%>
        <tr>
            <td colspan="5">사용자 목록 조회 중 오류가 발생했습니다.</td>
        </tr>
<%
    } finally {
        if (rs != null) try { rs.close(); } catch (Exception e) {}
        if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
        if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
%>
    </table>

    <div class="btn-area">
        <input type="button" value="관리자 메인" onclick="location.href='<%=request.getContextPath()%>/admin/adminMain.jsp'">
    </div>
</div>

<%
    String msg = request.getParameter("msg");

    if ("hasBoard".equals(msg)) {
%>
    <script>alert("게시글이 있는 회원은 삭제할 수 없습니다.");</script>
<%
    } else if ("self".equals(msg)) {
%>
    <script>alert("본인 계정은 삭제할 수 없습니다.");</script>
<%
    } else if ("success".equals(msg)) {
%>
    <script>alert("회원이 삭제되었습니다.");</script>
<%
    } else if ("error".equals(msg)) {
%>
    <script>alert("삭제 중 오류가 발생했습니다.");</script>
<%
    }
%>
</body>
</html>