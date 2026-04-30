package board;

public class BoardFileDTO {

	/* 파일 ID */
    private int fileId;
    /* 게시글 ID */
    private int boardId;
    /* 원본 파일명 */
    private String originName;
    /* 파일 크기 */
    private long fileSize;

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}


}