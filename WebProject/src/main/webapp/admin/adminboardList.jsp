<%@ page import="java.sql.*" %>
<%@ page import="common.DBUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/adminCheck.jsp" %>

<%
    request.setCharacterEncoding("UTF-8");

    String searchType = request.getParameter("searchType");
    String keyword = request.getParameter("keyword");

    if (searchType == null) {
        searchType = "title";
    }

    if (keyword == null) {
        keyword = "";
    }

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    String sql =
        "SELECT BOARD_ID, TITLE, WRITER_ID, IS_SECRET, VIEW_COUNT, " +
        "       TO_CHAR(REG_DATE, 'YYYY-MM-DD') AS REG_DATE " +
        "FROM TB_BOARD ";

    if (!keyword.trim().equals("")) {
        if ("content".equals(searchType)) {
            sql += "WHERE CONTENT LIKE ? ";
        } else if ("writer".equals(searchType)) {
            sql += "WHERE WRITER_ID LIKE ? ";
        } else {
            sql += "WHERE TITLE LIKE ? ";
        }
    }

    sql += "ORDER BY BOARD_ID DESC";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>등록된 게시글 관리</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/board.css">
</head>

<body>

<h1>등록된 게시글 관리</h1>

<div class="search-box">
    <form method="get" action="adminboardList.jsp">
        <select name="searchType">
            <option value="title" <%= "title".equals(searchType) ? "selected" : "" %>>제목</option>
            <option value="content" <%= "content".equals(searchType) ? "selected" : "" %>>내용</option>
            <option value="writer" <%= "writer".equals(searchType) ? "selected" : "" %>>작성자</option>
        </select>

        <input type="text" name="keyword" value="<%= keyword %>" placeholder="검색어 입력">
        <input type="submit" value="검색">
    </form>
</div>

<table>
    <tr>
        <th>번호</th>
        <th>제목</th>
        <th>작성자</th>
        <th>비밀글</th>
        <th>조회수</th>
        <th>작성일</th>
        <th>관리</th>
    </tr>

<%
    try {
        conn = DBUtil.getConnection();
        pstmt = conn.prepareStatement(sql);

        if (!keyword.trim().equals("")) {
            pstmt.setString(1, "%" + keyword + "%");
        }

        rs = pstmt.executeQuery();

        boolean hasData = false;

        while (rs.next()) {
            hasData = true;

            int boardId = rs.getInt("BOARD_ID");
            String title = rs.getString("TITLE");
            String writerId = rs.getString("WRITER_ID");
            String isSecret = rs.getString("IS_SECRET");
            int viewCount = rs.getInt("VIEW_COUNT");
            String regDate = rs.getString("REG_DATE");
%>

    <tr>
        <td><%= boardId %></td>
        <td class="title">
            <% if ("Y".equals(isSecret)) { %>
                <span class="secret">[비밀글]</span>
            <% } %>

            <a href="<%= request.getContextPath() %>/admin/adminboardDetail.jsp?boardId=<%= boardId %>">
                <%= title %>
            </a>
        </td>
        <td><%= writerId %></td>
        <td><%= "Y".equals(isSecret) ? "Y" : "N" %></td>
        <td><%= viewCount %></td>
        <td><%= regDate %></td>
        <td>
            <form action="<%= request.getContextPath() %>/admin/board/delete" method="post" style="display:inline;">
                <input type="hidden" name="boardId" value="<%= boardId %>">
                <input type="submit" value="삭제" onclick="return confirm('게시글을 삭제하시겠습니까?');">
            </form>
        </td>
    </tr>

<%
        }

        if (!hasData) {
%>
    <tr>
        <td colspan="7">등록된 게시글이 없습니다.</td>
    </tr>
<%
        }

    } catch (Exception e) {
        e.printStackTrace();
%>
    <tr>
        <td colspan="7">게시글 목록 조회 중 오류가 발생했습니다.</td>
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
	<button onclick="location.href='<%=request.getContextPath()%>/board/boardForm.jsp?returnUrl=admin'">글쓰기</button>
    <input type="button" value="관리자 메인" onclick="location.href='<%= request.getContextPath() %>/admin/adminMain.jsp'">
    <input type="button" value="메인" onclick="location.href='<%= request.getContextPath() %>/main.jsp'">
</div>

</body>
</html>