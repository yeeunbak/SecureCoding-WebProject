<%@ page import="java.util.List" %>
<%@ page import="board.dto.BoardDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/adminCheck.jsp" %>

<%
    @SuppressWarnings("unchecked")
    List<BoardDTO> boardList = (List<BoardDTO>) request.getAttribute("boardList");

    String searchType = (String) request.getAttribute("searchType");
    String keyword = (String) request.getAttribute("keyword");

    if (searchType == null) {
        searchType = "title";
    }

    if (keyword == null) {
        keyword = "";
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>등록된 게시글 관리</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/board.css">
</head>

<body>
<div class="board-container">
    <h1>등록된 게시글 관리</h1>

    <div class="search-box">
        <form method="get" action="<%= request.getContextPath() %>/admin/board/list">
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
    if (boardList == null || boardList.isEmpty()) {
%>
        <tr>
            <td colspan="7">등록된 게시글이 없습니다.</td>
        </tr>
<%
    } else {
        for (BoardDTO board : boardList) {
%>
        <tr>
            <td><%= board.getBoardId() %></td>
            <td class="title">
                <% if ("Y".equals(board.getIsSecret())) { %>
                    <span class="secret">[비밀글]</span>
                <% } %>

                <a href="<%= request.getContextPath() %>/admin/board/detail?boardId=<%= board.getBoardId() %>">
                    <%= board.getTitle() %>
                </a>
            </td>
            <td><%= board.getWriterId() %></td>
            <td><%= "Y".equals(board.getIsSecret()) ? "Y" : "N" %></td>
            <td><%= board.getViewCount() %></td>
            <td><%= board.getRegDate() %></td>
            <td>
                <form action="<%= request.getContextPath() %>/admin/board/delete" method="post" style="display:inline;">
                    <input type="hidden" name="boardId" value="<%= board.getBoardId() %>">
                    <input type="submit" value="삭제" onclick="return confirm('게시글을 삭제하시겠습니까?');">
                </form>
            </td>
        </tr>
<%
        }
    }
%>
    </table>

    <div class="btn-area">
        <button onclick="location.href='<%= request.getContextPath() %>/board/form?returnUrl=admin'">글쓰기</button>
        <input type="button" value="관리자 메인" onclick="location.href='<%= request.getContextPath() %>/admin/adminMain.jsp'">
    </div>
</div>
</body>
</html>