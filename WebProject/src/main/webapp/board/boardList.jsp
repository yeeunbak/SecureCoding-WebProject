<%@ page import="java.util.List" %>
<%@ page import="board.dto.BoardDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String loginId = (String) session.getAttribute("loginId");
    String loginName = (String) session.getAttribute("loginName");
    String loginRole = (String) session.getAttribute("loginRole");

    if (loginId == null) {
        response.sendRedirect(request.getContextPath() + "/member/login.jsp");
        return;
    }

    if ("ADMIN".equals(loginRole)) {
        response.sendRedirect(request.getContextPath() + "/admin/board/list");
        return;
    }

    @SuppressWarnings("unchecked")
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

    <div class="top-menu">
        <div class="login-user">
            <strong><%= loginName %></strong>님
        </div>

        <div class="top-buttons">
            <button type="button" onclick="location.href='<%= request.getContextPath() %>/editMember'">
                회원정보 수정
            </button>
            <button type="button" onclick="location.href='<%= request.getContextPath() %>/logout'">
                로그아웃
            </button>
        </div>
    </div>

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
        <button type="button" onclick="location.href='<%= request.getContextPath() %>/board/form'">
            글쓰기
        </button>
    </div>
</div>
</body>
</html>