<%@ page import="java.util.List" %>
<%@ page import="board.dto.BoardDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/loginCheck.jsp" %>

<%
    List<BoardDTO> boardList = (List<BoardDTO>) request.getAttribute("boardList");
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
        <form method="get" action="<%= request.getContextPath() %>/board/list">
            <select name="searchType">
                <option value="title" <%= "title".equals(request.getAttribute("searchType")) ? "selected" : "" %>>제목</option>
            	<option value="content" <%= "content".equals(request.getAttribute("searchType")) ? "selected" : "" %>>내용</option>
            	<option value="writer" <%= "writer".equals(request.getAttribute("searchType")) ? "selected" : "" %>>작성자</option>
            </select>

            <input type="text" name="keyword" value="<%= request.getAttribute("keyword") == null ? "" : request.getAttribute("keyword") %>" placeholder="검색어 입력">
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
    if (boardList == null || boardList.isEmpty()) {
%>
        <tr>
            <td colspan="6">등록된 게시글이 없습니다.</td>
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

                <a href="<%= request.getContextPath() %>/board/detail?boardId=<%= board.getBoardId() %>">
                    <%= board.getTitle() %>
                </a>
            </td>
            <td><%= board.getWriterId() %></td>
            <td><%= "Y".equals(board.getIsSecret()) ? "Y" : "N" %></td>
            <td><%= board.getViewCount() %></td>
            <td><%= board.getRegDate() %></td>
        </tr>
<%
        }
    }
%>
    </table>

    <div class="btn-area">
        <button onclick="location.href='<%= request.getContextPath() %>/board/form'">글쓰기</button>
        <input type="button" value="메인" onclick="location.href='<%= request.getContextPath() %>/main.jsp'">
    </div>
</div>
</body>
</html>