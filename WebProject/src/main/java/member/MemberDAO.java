package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.DBUtil;

public class MemberDAO {

    public int insertMember(MemberDTO member) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        /* 회원가입 시 INSERT */
        String sql = "INSERT INTO TB_MEMBER "
                   + "(USER_ID, USER_PW, USER_NAME, BIRTH_DATE, USER_EMAIL, USER_ROLE) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getUserId());
            pstmt.setString(2, member.getUserPw());
            pstmt.setString(3, member.getUserName());
            pstmt.setDate(4, member.getBirthDate());
            pstmt.setString(5, member.getUserEmail());
            pstmt.setString(6, member.getUserRole());

            result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt, conn);
        }

        return result;
    }

    /* 회원가입 시, 이미 존재하는 ID 있는지 비교 */ 
    public boolean isUserIdExists(String userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        String sql = "SELECT COUNT(*) FROM TB_MEMBER WHERE USER_ID = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }

        return exists;
    }

    /* 회원가입 시, 이미 존재하는 email 있는지 비교 */ 
    public boolean isEmailExists(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        String sql = "SELECT COUNT(*) FROM TB_MEMBER WHERE USER_EMAIL = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }

        return exists;
    }
    /* 로그인 -> 단순 문자비교 */
    public MemberDTO login(String userId, String userPw) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MemberDTO member = null;

        String sql = "SELECT USER_ID, USER_NAME, USER_ROLE "
                   + "FROM TB_MEMBER "
                   + "WHERE USER_ID = ? AND USER_PW = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, userPw);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new MemberDTO();
                member.setUserId(rs.getString("USER_ID"));
                member.setUserName(rs.getString("USER_NAME"));
                member.setUserRole(rs.getString("USER_ROLE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }

        return member;
    }
    
    /* ID 가져오기 */
    public MemberDTO getMemberById(String userId) {
        Connection conn = null;				// DB 연결
        PreparedStatement pstmt = null;		// SQL 실행 객체
        ResultSet rs = null;				// SELECT 결과 저장
        MemberDTO member = null;			// 결과 담을 객체

        /* USER_ID 일치하는 한 명의 데이터 */
        String sql = "SELECT * FROM TB_MEMBER WHERE USER_ID = ?";

        try {
            conn = DBUtil.getConnection();		// DB 연결
            pstmt = conn.prepareStatement(sql);	// SQL injection 방지
            pstmt.setString(1, userId);			// userId 변경X
            rs = pstmt.executeQuery();			// SELECT문 실행 -> 결과 rs에 담음

            if (rs.next()) { // 해당 userId 존재하면
                member = new MemberDTO();						// 객체 생성
                // DB에서 꺼낸 값을 DTO에 넣기
                member.setUserId(rs.getString("USER_ID"));
                member.setUserPw(rs.getString("USER_PW"));		// ** 평문 비밀번호 저장 -> 암호화 처리 필요
                member.setUserName(rs.getString("USER_NAME"));
                member.setBirthDate(rs.getDate("BIRTH_DATE"));
                member.setUserEmail(rs.getString("USER_EMAIL"));
                member.setUserRole(rs.getString("USER_ROLE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }

        return member; // Controller로 전달
    }
    
    /* 회원정보 수정 */
    public int updateMember(MemberDTO member) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;	// 업데이트 성공 여부

        /* 해당 USER_ID의 비밀번호, 이름, 생년월일, 이메일 수정 */
        String sql = "UPDATE TB_MEMBER SET USER_PW=?, USER_NAME=?, BIRTH_DATE=?, USER_EMAIL=? WHERE USER_ID=?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getUserPw());
            pstmt.setString(2, member.getUserName());
            pstmt.setDate(3, member.getBirthDate());
            pstmt.setString(4, member.getUserEmail());
            pstmt.setString(5, member.getUserId());

            result = pstmt.executeUpdate(); // 성공 1, 실패 0

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt, conn);
        }

        return result;
    }
}