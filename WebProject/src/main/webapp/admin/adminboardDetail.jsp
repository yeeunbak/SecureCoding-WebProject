<%@ page import="java.sql.*" %>
<%@ page import="common.DBUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/adminCheck.jsp" %>

<%
    request.setCharacterEncoding("UTF-8");

    // boardId 검증
    String boardIdParam = request.getParameter("boardId");

    if (boardIdParam == null || boardIdParam.trim().equals("")) {
        response.sendRedirect(request.getContextPath() + "/admin/adminboardList.jsp");
        return;
    }

    // DB 조회 준비 (문자열 -> 숫자로 변환)
    int boardId = Integer.parseInt(boardIdParam);

    // DB 작업 준비
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    // 게시글 정보를 화면에 출력하기 위한 변수
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
            response.sendRedirect(request.getContextPath() + "/admin/adminboardList.jsp");
            return;
        }

        rs.close();
        rs = null;
        
        pstmt.close();
        pstmt = null;
        
        // 조회수 증가
        // 비밀글 권한 확인 후 조회수를 올리기 때문에 권한 없는 접근은 조회수에 반영되지 않음
        String updateSql = "UPDATE TB_BOARD SET VIEW_COUNT = VIEW_COUNT + 1 WHERE BOARD_ID = ?";
        pstmt = conn.prepareStatement(updateSql);
        pstmt.setInt(1, boardId);
        pstmt.executeUpdate();

        viewCount++; // 화면에서도 증가된 조회수로 표시

    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect(request.getContextPath() + "/admin/adminboardList.jsp");
        return;
    } finally {
        if (rs != null) try { rs.close(); } catch (Exception e) {}
        if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
    }

    // 현재 로그인한 사용자 == 작성자?
    // 작성자인 경우에만 게시글 수정/삭제 버튼을 보여주기 위해 사용
    boolean isWriter = loginId.equals(writerId);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 관리 상세</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/board.css">
</head>

<body>
<div class="board-container">
	<h1>게시글 관리 상세</h1>

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
    
    <tr>
        <th>첨부파일</th>
        <td class="content">
<%
    // 첨부파일 조회 준비
    PreparedStatement filePstmt = null;
    ResultSet fileRs = null;

    try {
        // 현재 게시글에 등록된 첨부파일 목록 조회
        String fileSql =
            "SELECT FILE_ID, ORIGIN_NAME, FILE_SIZE " +
            "FROM TB_BOARD_FILE " +
            "WHERE BOARD_ID = ? " +
            "ORDER BY FILE_ID ASC";

        filePstmt = conn.prepareStatement(fileSql);
        filePstmt.setInt(1, boardId);
        fileRs = filePstmt.executeQuery();

        boolean hasFile = false;

        while (fileRs.next()) {
            hasFile = true;

            int fileId = fileRs.getInt("FILE_ID");
            String originName = fileRs.getString("ORIGIN_NAME");
            long fileSize = fileRs.getLong("FILE_SIZE");
%>
            <div>
                <!-- 파일명을 클릭하면 다운로드 컨트롤러로 이동 -->
                <a href="<%= request.getContextPath() %>/board/file/download?fileId=<%= fileId %>">
                    <%= originName %>
                </a>
                (<%= fileSize %> bytes)
            </div>
<%
        }

        if (!hasFile) {
%>
            첨부파일이 없습니다.
<%
        }

    } catch (Exception e) {
        e.printStackTrace();
%>
            첨부파일 조회 중 오류가 발생했습니다.
<%
    } finally {
        if (fileRs != null) try { fileRs.close(); } catch (Exception e) {}
        if (filePstmt != null) try { filePstmt.close(); } catch (Exception e) {}
    }
%>
        </td>
    </tr>
</table>

<h2>댓글</h2>

<div class="comment-list">
<%
    // 댓글 조회 준비
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
        commentPstmt.setInt(1, boardId);           // 현재 보고 있는 게시글 ID 기준 댓글 조회
        commentRs = commentPstmt.executeQuery();   // DB에서 댓글 목록 가져오기

        boolean hasComment = false; // 댓글 존재 여부 체크

        while (commentRs.next()) {  // 댓글 반복 출력
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
            <!-- 본인 댓글에만 수정/삭제 버튼 표시 -->
            <div class="comment-btn-area">
                <button type="button" onclick="toggleEdit(<%= commentId %>)">수정</button>

                <form action="<%= request.getContextPath() %>/board/comment/delete" method="post" class="comment-delete-form">
                    <input type="hidden" name="boardId" value="<%= boardId %>">
                    <input type="hidden" name="commentId" value="<%= commentId %>">
                    <input type="submit" value="삭제" onclick="return confirm('댓글을 삭제하시겠습니까?');">
                </form>
            </div>

            <!-- 댓글 수정 폼: 처음에는 숨겨두고 수정 버튼 클릭 시 표시 -->
            <div id="edit-form-<%= commentId %>" class="comment-edit-form" style="display:none;">
                <form action="<%= request.getContextPath() %>/board/comment/update" method="post">
                    <input type="hidden" name="boardId" value="<%= boardId %>">
                    <input type="hidden" name="commentId" value="<%= commentId %>">

                    <textarea name="content" rows="3" required><%= commentContent %></textarea>

                    <div class="btn-area">
                        <input type="submit" value="수정 완료">
                        <button type="button" onclick="toggleEdit(<%= commentId %>)">취소</button>
                    </div>
                </form>
            </div>
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
    <!-- 댓글 작성 form -->
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
        onclick="location.href='<%= request.getContextPath() %>/admin/adminboardList.jsp'">

    <!-- 관리자 본인이 작성한 게시글일 때만 수정 버튼 표시 -->
    <% if (isWriter) { %>
        <input type="button" value="수정" onclick="location.href='<%= request.getContextPath() %>/board/boardForm.jsp?boardId=<%= boardId %>'">
	<% } %>
	
    <form action="<%= request.getContextPath() %>/admin/board/delete" method="post" style="display:inline;">
        <input type="hidden" name="boardId" value="<%= boardId %>">
        <input type="submit" value="삭제" onclick="return confirm('게시글을 삭제하시겠습니까?');">
    </form>

</div>

<script>
// 댓글 수정 폼 열기/닫기
function toggleEdit(commentId) {
    const editForm = document.getElementById("edit-form-" + commentId);

    if (editForm.style.display == "none") {
        editForm.style.display = "block";
    } else {
        editForm.style.display = "none";
    }
}
</script>
</div>
</body>
</html>