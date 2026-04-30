package board;

public class BoardFileDTO {

	/* 파일 ID */
    private int fileId;
    /* 게시글 ID */
    private int boardId;
    /* 첨부파일 원본 이름 */
    private String originName;
    /* 서버에 저장된 파일 이름 */
    private String saveName;
    /* 서버에 저장된 파일 경로 */
    private String savePath;
    /* 파일 크기 */
    private long fileSize;
    /* 파일 확장자 */
    private String fileExt;
    
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
	
	public String getSaveName() {
		return saveName;
	}
	
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	
	public String getSavePath() {
		return savePath;
	}
	
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getFileExt() {
		return fileExt;
	}
	
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
    



}