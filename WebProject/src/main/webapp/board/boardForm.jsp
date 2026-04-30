<%@ page import="java.sql.*, common.DBUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/loginCheck.jsp" %>

<%
    request.setCharacterEncoding("UTF-8");

	// boardId X -> 작성 모드
	// boardId O -> 수정 모드
    String boardIdParam = request.getParameter("boardId");

    boolean isEdit = false; // 작성/수정 구분
    int boardId = 0;

    String title = "";
    String content = "";
    String isSecret = "N";

    // DB 연결 객체
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        conn = DBUtil.getConnection();

        	// 수정 모드일 경우 기존 게시글 조회
        if (boardIdParam != null && !boardIdParam.trim().equals("")) {

        	isEdit = true;
            boardId = Integer.parseInt(boardIdParam);

            	// 게시글 정보 조회
            String sql = "SELECT * FROM TB_BOARD WHERE BOARD_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, boardId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // 기존 데이터 화면에 미리 세팅
                title = rs.getString("TITLE");
                content = rs.getString("CONTENT");
                isSecret = rs.getString("IS_SECRET");
            }

            // 리소스 정리
            rs.close();
            pstmt.close();
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
<div class="board-container">

<h2><%= isEdit ? "게시글 수정" : "게시글 작성" %></h2>

<%
    String returnUrl = request.getParameter("returnUrl");

    if (returnUrl == null) {
        returnUrl = "";
    }
%>

<!-- 게시글 작성/수정 form -->
<form action="<%= request.getContextPath() %><%= isEdit ? "/board/update" : "/board/write" %>" method="post" enctype="multipart/form-data"> <!-- 파일 업로드 필수 -->

    <% if (isEdit) { %>
        <!-- 수정 시 게시글 식별용 -->
        <input type="hidden" name="boardId" value="<%= boardId %>">
    <% } %>

    <table class="board-form-table">
        <tr>
            <th>제목</th>
            <td>
                <input type="text" name="title" value="<%= title %>" required>
            </td>
        </tr>

        <tr>
            <th>내용</th>
            <td>
                <textarea name="content" required><%= content %></textarea>
            </td>
        </tr>

        <tr>
            <!-- 비밀글 체크 -->
            <th>비밀글</th>
            <td class="secret-check">
            	<label>
            		<input type="checkbox" name="isSecret" value="Y" <%= "Y".equals(isSecret) ? "checked" : "" %>> 비밀글로 설정
            	</label>
            </td>
        </tr>
        
        <tr>
    		<th>첨부파일</th>
    		<td>

        		<% if (isEdit) { %>
            		<div class="file-list">
		<%
            try {
        			// 기존 첨부파일 목록 조회
        		String fileSql =
            		"SELECT FILE_ID, ORIGIN_NAME, FILE_SIZE " +
            		"FROM TB_BOARD_FILE " +
            		"WHERE BOARD_ID = ? " +
            		"ORDER BY FILE_ID ASC";

        		pstmt = conn.prepareStatement(fileSql);
        		pstmt.setInt(1, boardId);
        		rs = pstmt.executeQuery();

        		boolean hasFile = false;

        		while (rs.next()) {
            		hasFile = true;

            		int fileId = rs.getInt("FILE_ID");
            		String originName = rs.getString("ORIGIN_NAME");
            		long fileSize = rs.getLong("FILE_SIZE");
		%>
                		<div class="file-item">
                    		<!-- 파일명 + 용량 표시 -->
                    		<span><%= originName %> (<%= fileSize %> bytes)</span>

                    		<!-- 파일 삭제 버튼 -->
                    		<button type="button" onclick="deleteFile(<%= fileId %>)">삭제</button>
                		</div>
		<%
        		}

        		if (!hasFile) {
		%>
                		<div>첨부된 파일이 없습니다.</div>
		<%
        		}

    		} catch (Exception e) {
        		e.printStackTrace();
		%>
                		<div>첨부파일 조회 중 오류가 발생했습니다.</div>
		<%
    		} finally {
        		if (rs != null) rs.close();
        		if (pstmt != null) pstmt.close();
    		}
		%>
            		</div>
        		<% } %>

        			<!-- 여러 파일 업로드 -->
        		<input type="file" name="uploadFile" multiple>

    		</td>
		</tr>
    </table>

    <div class="btn-area">
        <input type="submit" value="<%= isEdit ? "수정하기" : "작성하기" %>">
        <input type="button" value="취소" onclick="location.href='<%=request.getContextPath()%><%= "admin".equals(returnUrl) ? "/admin/adminboardList.jsp" : "/board/boardList.jsp" %>'">
    </div>
    
</form>

<!-- 파일 삭제용 POST 요청 -->
<script>
function deleteFile(fileId) {

    if (!confirm("첨부파일을 삭제하시겠습니까?")) {
        return;
    }

    // form을 동적으로 생성해서 POST 요청
    const form = document.createElement("form");
    form.method = "post";
    form.action = "<%= request.getContextPath() %>/board/file/delete";

    // 게시글 ID 전달
    const boardInput = document.createElement("input");
    boardInput.type = "hidden";
    boardInput.name = "boardId";
    boardInput.value = "<%= boardId %>";

    // 파일 ID 전달
    const fileInput = document.createElement("input");
    fileInput.type = "hidden";
    fileInput.name = "fileId";
    fileInput.value = fileId;

    form.appendChild(boardInput);
    form.appendChild(fileInput);

    document.body.appendChild(form);
    form.submit();
}
</script>
</div>
</body>
</html>

<%
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // DB 자원 정리
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (conn != null) conn.close();
    }
%>