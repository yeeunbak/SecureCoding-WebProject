<%@ page import="java.sql.*, common.DBUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/loginCheck.jsp" %>

<%
    request.setCharacterEncoding("UTF-8");

    String boardIdParam = request.getParameter("boardId");

    boolean isEdit = false;
    int boardId = 0;

    String title = "";
    String content = "";
    String isSecret = "N";

    if (boardIdParam != null && !boardIdParam.trim().equals("")) {
        isEdit = true;
        boardId = Integer.parseInt(boardIdParam);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            String sql = "SELECT * FROM TB_BOARD WHERE BOARD_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
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
            <td>비밀글</td>
            <td>
                <input type="checkbox" name="isSecret" value="Y"
                    <%= "Y".equals(isSecret) ? "checked" : "" %> >
            </td>
        </tr>
    </table>

    <div class="btn-area">
        <input type="submit" value="<%= isEdit ? "수정하기" : "작성하기" %>">

        <input type="button" value="취소"
            onclick="location.href='<%=request.getContextPath()%>/board/boardList.jsp'">
    </div>

</form>

</body>
</html>