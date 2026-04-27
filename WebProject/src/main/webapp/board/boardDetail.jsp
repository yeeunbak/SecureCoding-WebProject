<%@ page import="java.sql.*" %>
<%@ page import="common.DBUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/loginCheck.jsp" %>

<%
    request.setCharacterEncoding("UTF-8");

	// boardId 검증
    String boardIdParam = request.getParameter("boardId");

    if (boardIdParam == null || boardIdParam.trim().equals("")) {
        response.sendRedirect(request.getContextPath() + "/board/boardList.jsp");
        return;
    }

    // DB 조회 준비 (문자열 -> 숫자로 변환)
    int boardId = Integer.parseInt(boardIdParam);

    // DB 작업 준비
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    String title = "";
    String content = "";
    String writerId = "";
    String isSecret = "";
    int viewCount = 0;
    String regDate = "";
    String updDate = "";

    try {
        conn = DBUtil.getConnection();

        	// 해당 게시글 1개 조회
        String selectSql =
            "SELECT BOARD_ID, TITLE, CONTENT, WRITER_ID, IS_SECRET, VIEW_COUNT, " +
            "       TO_CHAR(REG_DATE, 'YYYY-MM-DD HH24:MI') AS REG_DATE, " +
            "       TO_CHAR(UPD_DATE, 'YYYY-MM-DD HH24:MI') AS UPD_DATE " +
            "FROM TB_BOARD " +
            "WHERE BOARD_ID = ?";

        pstmt = conn.prepareStatement(selectSql);
        pstmt.setInt(1, boardId);

        rs = pstmt.executeQuery();

        // DB -> 화면에 출력할 값으로 저장
        if (rs.next()) {
            title = rs.getString("TITLE");
            content = rs.getString("CONTENT");
            writerId = rs.getString("WRITER_ID");
            isSecret = rs.getString("IS_SECRET");
            viewCount = rs.getInt("VIEW_COUNT");
            regDate = rs.getString("REG_DATE");
            updDate = rs.getString("UPD_DATE");
        } else {
            response.sendRedirect(request.getContextPath() + "/board/boardList.jsp");
            return;
        }

        	// 비밀글 접근 제한
        if ("Y".equals(isSecret) && !loginId.equals(writerId)) {
            out.println("<script>");
            out.println("alert('비밀글은 작성자만 볼 수 있습니다.');");
            out.println("location.href='" + request.getContextPath() + "/board/boardList.jsp';");
            out.println("</script>");
            return;
        }

        rs.close();
        pstmt.close();

        // 조회수 증가
        String updateSql = "UPDATE TB_BOARD SET VIEW_COUNT = VIEW_COUNT + 1 WHERE BOARD_ID = ?"; // DB에서 증가
        pstmt = conn.prepareStatement(updateSql);
        pstmt.setInt(1, boardId);
        pstmt.executeUpdate();

        viewCount++; // 화면에서도 증가

    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect(request.getContextPath() + "/board/boardList.jsp");
        return;
    } finally {
        if (rs != null) try { rs.close(); } catch (Exception e) {}
        if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
        if (conn != null) try { conn.close(); } catch (Exception e) {}
    }

    	// 현재 로그인한 사용자 == 작성자?
    boolean isWriter = loginId.equals(writerId);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/board.css">
</head>

<body>

<h1>게시글 상세</h1>

<table>
    <tr>
        <th>제목</th>
        <td class="title">
            <% if ("Y".equals(isSecret)) { %>
                <span class="secret">[비밀글]</span>
            <% } %>
            <%= title %>
        </td>
    </tr>

    <tr>
        <th>작성자</th>
        <td><%= writerId %></td>
    </tr>

    <tr>
        <th>조회수</th>
        <td><%= viewCount %></td>
    </tr>

    <tr>
        <th>작성일</th>
        <td><%= regDate %></td>
    </tr>

    <% if (updDate != null) { %>
    <tr>
        <th>수정일</th>
        <td><%= updDate %></td>
    </tr>
    <% } %>

    <tr>
        <th>내용</th>
        <td class="content">
            <%= content.replace("\n", "<br>") %>
        </td>
    </tr>
</table>

<div class="btn-area">
    <input type="button" value="목록"
        onclick="location.href='<%= request.getContextPath() %>/board/boardList.jsp'">

	<!-- 해당 게시글의 작성자에게만 보이는 버튼 -->
    <% if (isWriter) { %>
        <input type="button" value="수정"onclick="location.href='<%= request.getContextPath() %>/board/boardForm.jsp?boardId=<%= boardId %>'">
        <form action="<%= request.getContextPath() %>/board/delete" method="post" style="display:inline;">
            <input type="hidden" name="boardId" value="<%= boardId %>">
            <input type="submit" value="삭제" onclick="return confirm('정말 삭제하시겠습니까?');">
        </form>
    <% } %>
</div>

</body>
</html>