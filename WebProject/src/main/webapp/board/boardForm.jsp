<%@ page import="java.util.List" %>
<%@ page import="board.dto.BoardDTO" %>
<%@ page import="board.dto.BoardFileDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/loginCheck.jsp" %>

<%
    BoardDTO board = (BoardDTO) request.getAttribute("board");
	
	@SuppressWarnings("unchecked")
	List<BoardFileDTO> fileList = (List<BoardFileDTO>) request.getAttribute("fileList");

    boolean isEdit = board != null;

    int boardId = isEdit ? board.getBoardId() : 0;
    String title = isEdit ? board.getTitle() : "";
    String content = isEdit ? board.getContent() : "";
    String isSecret = isEdit ? board.getIsSecret() : "N";
    
    String returnUrl = request.getParameter("returnUrl");
    if (returnUrl == null){
    	returnUrl = "";
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

<form action="<%= request.getContextPath() %><%= isEdit ? "/board/update" : "/board/write" %>"
      method="post"
      enctype="multipart/form-data">

	<input type="hidden" name="returnUrl" value="<%= returnUrl %>">
	
    <% if (isEdit) { %>
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
                		<strong>기존 첨부파일</strong>

                		<%
                    		if (fileList == null || fileList.isEmpty()) {
                		%>
                    		<div>첨부된 파일이 없습니다.</div>
                		<%
                    	} else {
                        	for (BoardFileDTO file : fileList) {
                		%>
                    		<div class="file-item">
                        		<span>
                            		<a href="<%= request.getContextPath() %>/board/file/download?fileId=<%= file.getFileId() %>">
                                		<%= file.getOriginName() %>
                            		</a>
                            		(<%= file.getFileSize() %> bytes)
                        		</span>

                        		<button type="button" onclick="deleteFile(<%= file.getFileId() %>)">삭제</button>
                    		</div>
                		<%
                        		}
                    		}
                		%>
            		</div>
        		<% } %>

        		<div style="margin-top:10px;">
            		<input type="file" name="uploadFile" multiple>
        		</div>
            </td>
        </tr>
    </table>

    <div class="btn-area">
        <input type="submit" value="<%= isEdit ? "수정하기" : "작성하기" %>">

        <% if (isEdit) { %>
            <input type="button" value="취소"
                   onclick="location.href='<%= request.getContextPath() %>/board/detail?boardId=<%= boardId %>'">
        <% } else { %>
            <input type="button" value="취소"
                   onclick="location.href='<%= request.getContextPath() %>/board/list'">
        <% } %>
    </div>
</form>
<script>
function deleteFile(fileId) {
    if (!confirm("첨부파일을 삭제하시겠습니까?")) {
        return;
    }

    const form = document.createElement("form");
    form.method = "post";
    form.action = "<%= request.getContextPath() %>/board/file/delete";

    const boardInput = document.createElement("input");
    boardInput.type = "hidden";
    boardInput.name = "boardId";
    boardInput.value = "<%= boardId %>";

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