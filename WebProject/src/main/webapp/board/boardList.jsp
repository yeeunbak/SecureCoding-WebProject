<%@ page import="java.sql.*" %>
<%@ page import="common.DBUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/loginCheck.jsp" %>

<%
    request.setCharacterEncoding("UTF-8");

    String searchType = request.getParameter("searchType");	// 검색 조건
    String keyword = request.getParameter("keyword");		// 검색어

    // 기본 검색 기준 -> 제목
    if (searchType == null) {
        searchType = "title";
    }

    if (keyword == null) {
        keyword = "";
    }

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    // 검색
    String sql =
        "SELECT BOARD_ID, TITLE, WRITER_ID, IS_SECRET, VIEW_COUNT, " +
        "       TO_CHAR(REG_DATE, 'YYYY-MM-DD') AS REG_DATE " +
        "FROM TB_BOARD ";

    // 검색 조건 추가
    if (!keyword.trim().equals("")) {
        if ("content".equals(searchType)) {
            sql += "WHERE CONTENT LIKE ? ";				// 내용 검색
        } else if ("writer".equals(searchType)) {
            sql += "WHERE WRITER_ID LIKE ? ";			// 작성자 검색
        } else {
            sql += "WHERE TITLE LIKE ? ";				// 제목 검색
        }
    }

    sql += "ORDER BY BOARD_ID DESC";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>

<link rel="stylesheet" href="<%= request.getContextPath() %>/css/board.css">

</head>

<body>
<div class="board-container">
	<h1>게시글 목록</h1>

<div class="search-box">
    <form method="get" action="boardList.jsp">
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
    </tr>

<%
    try {
        conn = DBUtil.getConnection();
        pstmt = conn.prepareStatement(sql);

        if (!keyword.trim().equals("")) {
            pstmt.setString(1, "%" + keyword + "%");	// LIKE '%keyword%'
        }

        rs = pstmt.executeQuery();

        boolean hasData = false;

        // 목록 출력
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

			<!-- 상세 페이지 -->
            <a href="boardDetail.jsp?boardId=<%= boardId %>">
                <%= title %>
            </a>
        </td>
        <td><%= writerId %></td>
        <td><%= "Y".equals(isSecret) ? "Y" : "N" %></td>
        <td><%= viewCount %></td>
        <td><%= regDate %></td>
    </tr>

<%
        }
        if (!hasData) {  // 게시글 없을 때
%>
    <tr>
        <td colspan="6">등록된 게시글이 없습니다.</td>
    </tr>
<%
        }

    } catch (Exception e) {
        e.printStackTrace();
%>
    <tr>
        <td colspan="6">게시글 목록 조회 중 오류가 발생했습니다.</td>
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
    <button onclick="location.href='<%=request.getContextPath()%>/board/boardForm.jsp'">글쓰기</button>
    <input type="button" value="메인" onclick="location.href='<%= request.getContextPath() %>/main.jsp'">
</div>
</div>
</body>
</html>