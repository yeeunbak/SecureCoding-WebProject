package member;

import java.sql.Date;

public class MemberDTO {

	/* ID */
	private String userId;
	/* 비밀번호 */
	private String userPw;
	/* 이름 */
	private String userName;
	/* 생년월일 */
	private Date birthDate;
	/* 이메일 */
	private String userEmail;
	/* 사용자 OR 관리자 */
	private String userRole;
	/* 가입일시 */
	private Date regDate;

	public MemberDTO() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
}