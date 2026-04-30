package board;

public class CommentDTO {

	/* 댓글 ID */
    private int commentId;
    /* 게시글 ID */
    private int boardId;
    /* 내용 */
    private String content;
    /* 작성자 ID */
    private String writerId;
    /* 작성일 */
    private String regDate;
    
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriterId() {
		return writerId;
	}
	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}