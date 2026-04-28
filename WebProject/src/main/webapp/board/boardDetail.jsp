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
        rs = null;
        
        pstmt.close();
        pstmt = null;
        
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


<h2>댓글</h2>

<div class="comment-list">
<%
	// DB 조회 준비
    PreparedStatement commentPstmt = null;
    ResultSet commentRs = null;

    try {
    		// 특정 게시글의 댓글만 가져옴
        String commentSql =
            "SELECT COMMENT_ID, CONTENT, WRITER_ID, " +
            "       TO_CHAR(REG_DATE, 'YYYY-MM-DD HH24:MI') AS REG_DATE " +
            "FROM TB_COMMENT " +
            "WHERE BOARD_ID = ? " +
            "ORDER BY COMMENT_ID ASC"; // 오래된 댓글 -> 최신 댓글 순

        commentPstmt = conn.prepareStatement(commentSql);
        commentPstmt.setInt(1, boardId); 			// 현재 보고 있는 게시글 ID 기준 댓글 조회
        commentRs = commentPstmt.executeQuery(); 	// DB에서 댓글 목록 가져오기

        boolean hasComment = false; // 댓글 존재 여부 체크

        while (commentRs.next()) {	// 댓글 반복 출력
            hasComment = true;

        		// 댓글 데이터 꺼내기
        	int commentId = commentRs.getInt("COMMENT_ID");
            String commentWriterId = commentRs.getString("WRITER_ID");
            String commentContent = commentRs.getString("CONTENT");
            String commentRegDate = commentRs.getString("REG_DATE");
%>
    <div class="comment-item">
        <div class="comment-header">
            <span class="comment-writer"><%= commentWriterId %></span>
            <span class="comment-date"><%= commentRegDate %></span>
        </div>

        <div class="comment-content">
            <%= commentContent.replace("\n", "<br>") %>
        </div>
        
		<% if (loginId.equals(commentWriterId)) { %>
        	<form action="<%= request.getContextPath() %>/board/comment/delete" method="post" class="comment-delete-form">
            	<input type="hidden" name="boardId" value="<%= boardId %>">
            	<input type="hidden" name="commentId" value="<%= commentId %>">
            	<input type="submit" value="삭제" onclick="return confirm('댓글을 삭제하시겠습니까?');">
        	</form>
    	<% } %>
    </div>
<%
        }

        if (!hasComment) {
%>
    <div class="no-comment">등록된 댓글이 없습니다.</div>
<%
        }

    } catch (Exception e) {
        e.printStackTrace();
%>
    <div class="no-comment">댓글 조회 중 오류가 발생했습니다.</div>
<%
    } finally { // DB 연결 종료
        if (commentRs != null) try { commentRs.close(); } catch (Exception e) {}
        if (commentPstmt != null) try { commentPstmt.close(); } catch (Exception e) {}
        if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
%>
</div>

<div class="comment-write">
    <form action="<%= request.getContextPath() %>/board/comment/write" method="post">
        <input type="hidden" name="boardId" value="<%= boardId %>">

        <textarea name="content" rows="4" placeholder="댓글을 입력하세요." required></textarea>

        <div class="btn-area">
            <input type="submit" value="댓글 등록">
        </div>
    </form>
</div>

<div class="btn-area">
    <input type="button" value="목록"
        onclick="location.href='<%= request.getContextPath() %>/board/boardList.jsp'">

	<!-- 해당 게시글의 작성자에게만 보이는 버튼 -->
    <% if (isWriter) { %>
        <input type="button" value="수정" onclick="location.href='<%= request.getContextPath() %>/board/boardForm.jsp?boardId=<%= boardId %>'">
        <form action="<%= request.getContextPath() %>/board/delete" method="post" style="display:inline;">
            <input type="hidden" name="boardId" value="<%= boardId %>">
            <input type="submit" value="삭제" onclick="return confirm('정말 삭제하시겠습니까?');">
        </form>
    <% } %>
</div>

</body>
</html>