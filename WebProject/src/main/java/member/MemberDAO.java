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
}