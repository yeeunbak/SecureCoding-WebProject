<%@ page import="java.sql.*, common.DBUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/loginCheck.jsp" %>

<%
    request.setCharacterEncoding("UTF-8");

	// boardId X -> 작성
	// boardId O -> 수정
    String boardIdParam = request.getParameter("boardId");

    boolean isEdit = false;
    int boardId = 0;

    String title = "";
    String content = "";
    String isSecret = "N";

    // 수정 모드
    if (boardIdParam != null && !boardIdParam.trim().equals("")) {
        // boardId O -> DB에서 기존 게시글 조회
    	isEdit = true;
        boardId = Integer.parseInt(boardIdParam);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            // 조회된 값 화면에 미리 넣기
            String sql = "SELECT * FROM TB_BOARD WHERE BOARD_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
            		// 수정화면 -> 기존 제목, 내용, 비밀글 여부
                title = rs.getString("TITLE");
                content = rs.getString("CONTENT");
                isSecret = rs.getString("IS_SECRET");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn != null) try { conn.close(); } catch (Exception e) {}
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= isEdit ? "게시글 수정" : "게시글 작성" %></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css">
</head>

<body>

<h2><%= isEdit ? "게시글 수정" : "게시글 작성" %></h2>

<form action="<%= request.getContextPath() %><%= isEdit ? "/board/update" : "/board/write" %>" method="post">

    <% if (isEdit) { %>
        <input type="hidden" name="boardId" value="<%= boardId %>">
    <% } %>

    <table>
        <tr>
            <td>제목</td>
            <td>
                <input type="text" name="title" value="<%= title %>" required>
            </td>
        </tr>

        <tr>
            <td>내용</td>
            <td>
                <textarea name="content" rows="10" cols="50" required><%= content %></textarea>
            </td>
        </tr>

        <tr>
        		<!-- 기존 글이 비밀글이면, 체크된 상태로 보여짐 -->
        		<!-- 체크 isSecret=Y 전송 / 체크X 값이 안넘어가서 컨트롤러에서 N으로 처리 -->
            <td>비밀글</td>
            <td>
                <input type="checkbox" name="isSecret" value="Y" <%= "Y".equals(isSecret) ? "checked" : "" %> >
            </td>
        </tr>
    </table>

    <div class="btn-area">
        <input type="submit" value="<%= isEdit ? "수정하기" : "작성하기" %>">
        <input type="button" value="취소" onclick="location.href='<%=request.getContextPath()%>/board/boardList.jsp'">
    </div>

</form>

</body>
</html>