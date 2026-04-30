<%@ page import="java.util.List" %>
<%@ page import="board.BoardDTO" %>
<%@ page import="board.BoardFileDTO" %>
<%@ page import="board.CommentDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/loginCheck.jsp" %>

<%
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    List<BoardFileDTO> fileList = (List<BoardFileDTO>) request.getAttribute("fileList");
    List<CommentDTO> commentList = (List<CommentDTO>) request.getAttribute("commentList");

    if (board == null) {
        response.sendRedirect(request.getContextPath() + "/board/list");
        return;
    }

    int boardId = board.getBoardId();
    boolean isWriter = loginId.equals(board.getWriterId());
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/board.css">
</head>

<body>
<div class="board-container">
    <h1>게시글 상세</h1>

    <table>
        <tr>
            <th>제목</th>
            <td class="title">
                <% if ("Y".equals(board.getIsSecret())) { %>
                    <span class="secret">[비밀글]</span>
                <% } %>
                <%= board.getTitle() %>
            </td>
        </tr>

        <tr>
            <th>작성자</th>
            <td><%= board.getWriterId() %></td>
        </tr>

        <tr>
            <th>조회수</th>
            <td><%= board.getViewCount() %></td>
        </tr>

        <tr>
            <th>작성일</th>
            <td><%= board.getRegDate() %></td>
        </tr>

        <% if (board.getUpdDate() != null) { %>
        <tr>
            <th>수정일</th>
            <td><%= board.getUpdDate() %></td>
        </tr>
        <% } %>

        <tr>
            <th>내용</th>
            <td class="content">
                <%= board.getContent() == null ? "" : board.getContent().replace("\n", "<br>") %>
            </td>
        </tr>

        <tr>
            <th>첨부파일</th>
            <td class="content">
                <%
                    if (fileList == null || fileList.isEmpty()) {
                %>
                    첨부파일이 없습니다.
                <%
                    } else {
                        for (BoardFileDTO file : fileList) {
                %>
                    <div>
                        <a href="<%= request.getContextPath() %>/board/file/download?fileId=<%= file.getFileId() %>">
                            <%= file.getOriginName() %>
                        </a>
                        (<%= file.getFileSize() %> bytes)
                    </div>
                <%
                        }
                    }
                %>
            </td>
        </tr>
    </table>

    <h2>댓글</h2>

    <div class="comment-list">
        <%
            if (commentList == null || commentList.isEmpty()) {
        %>
            <div class="no-comment">등록된 댓글이 없습니다.</div>
        <%
            } else {
                for (CommentDTO comment : commentList) {
        %>
            <div class="comment-item">
                <div class="comment-header">
                    <span class="comment-writer"><%= comment.getWriterId() %></span>
                    <span class="comment-date"><%= comment.getRegDate() %></span>
                </div>

                <div class="comment-content">
                    <%= comment.getContent() == null ? "" : comment.getContent().replace("\n", "<br>") %>
                </div>

                <% if (loginId.equals(comment.getWriterId())) { %>
                    <div class="comment-btn-area">
                        <button type="button" onclick="toggleEdit(<%= comment.getCommentId() %>)">수정</button>

                        <form action="<%= request.getContextPath() %>/board/comment/delete" method="post" class="comment-delete-form">
                            <input type="hidden" name="boardId" value="<%= boardId %>">
                            <input type="hidden" name="commentId" value="<%= comment.getCommentId() %>">
                            <input type="submit" value="삭제" onclick="return confirm('댓글을 삭제하시겠습니까?');">
                        </form>
                    </div>

                    <div id="edit-form-<%= comment.getCommentId() %>" class="comment-edit-form" style="display:none;">
                        <form action="<%= request.getContextPath() %>/board/comment/update" method="post">
                            <input type="hidden" name="boardId" value="<%= boardId %>">
                            <input type="hidden" name="commentId" value="<%= comment.getCommentId() %>">

                            <textarea name="content" rows="3" required><%= comment.getContent() %></textarea>

                            <div class="btn-area">
                                <input type="submit" value="수정 완료">
                                <button type="button" onclick="toggleEdit(<%= comment.getCommentId() %>)">취소</button>
                            </div>
                        </form>
                    </div>
                <% } %>
            </div>
        <%
                }
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
            onclick="location.href='<%= request.getContextPath() %>/board/list'">

        <% if (isWriter) { %>
            <input type="button" value="수정"
                onclick="location.href='<%= request.getContextPath() %>/board/boardForm.jsp?boardId=<%= boardId %>'">

            <form action="<%= request.getContextPath() %>/board/delete" method="post" style="display:inline;">
                <input type="hidden" name="boardId" value="<%= boardId %>">
                <input type="submit" value="삭제" onclick="return confirm('정말 삭제하시겠습니까?');">
            </form>
        <% } %>
    </div>
</div>

<script>
function toggleEdit(commentId) {
    const editForm = document.getElementById("edit-form-" + commentId);

    if (editForm.style.display === "none") {
        editForm.style.display = "block";
    } else {
        editForm.style.display = "none";
    }
}
</script>
</body>
</html>