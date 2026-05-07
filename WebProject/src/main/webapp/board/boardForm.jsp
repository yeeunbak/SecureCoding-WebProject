<%@ page import="java.util.List" %>
<%@ page import="board.dto.BoardDTO" %>
<%@ page import="board.dto.BoardFileDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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
    if (returnUrl == null) {
        returnUrl = "";
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= isEdit ? "게시글 수정" : "게시글 작성" %></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css">
<script defer src="<%= request.getContextPath() %>/js/board/boardForm.js"></script>
</head>

<body data-context-path="<%= request.getContextPath() %>" data-board-id="<%= boardId %>">
<div class="board-container">
<h2><%= isEdit ? "게시글 수정" : "게시글 작성" %></h2>
<form action="<%= request.getContextPath() %><%= isEdit ? "/board/update" : "/board/write" %>" method="post" enctype="multipart/form-data">
    <input type="hidden" name="returnUrl" value="<%= returnUrl %>">

    <% if (isEdit) { %>
        <input type="hidden" name="boardId" value="<%= boardId %>">
    <% } %>

    <table class="board-form-table">

        <tr>
            <th>제목</th>
            <td>
                <input type="text" name="title" value="<%= title %>" maxlength="200" required>
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
                                <button type="button" class="delete-file-btn" data-file-id="<%= file.getFileId() %>"> 삭제 </button>
                            </div>

                        <%
                                }
                            }
                        %>

                    </div>

                <% } %>

                <div class="file-upload-area">
                    <input type="file" name="uploadFile" multiple>
                </div>
            </td>
        </tr>
    </table>

    <div class="btn-area">

        <input type="submit" value="<%= isEdit ? "수정하기" : "작성하기" %>">
        <input type="button" value="취소" class="cancel-btn" data-url="<%= request.getContextPath() %><%= isEdit ? "/board/detail?boardId=" + boardId : "/board/list" %>">
    </div>
</form>
</div>
</body>
</html>