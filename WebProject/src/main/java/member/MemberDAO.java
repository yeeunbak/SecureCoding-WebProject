package member;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import common.MyBatisUtil;

public class MemberDAO {

    private static final String NAMESPACE = "member.MemberMapper.";

    // 회원가입
    public int insertMember(MemberDTO member) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.insert(NAMESPACE + "insertMember", member);
            session.commit();
            return result;
        }
    }

    // 아이디 중복 체크
    public boolean isUserIdExists(String userId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            int count = session.selectOne(NAMESPACE + "countUserId", userId);
            return count > 0;
        }
    }

    // 이메일 중복 체크
    public boolean isEmailExists(String email) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            int count = session.selectOne(NAMESPACE + "countEmail", email);
            return count > 0;
        }
    }

    // 로그인
    public MemberDTO login(String userId, String userPw) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("userPw", userPw);

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "login", paramMap);
        }
    }

    // 회원 조회
    public MemberDTO getMemberById(String userId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "selectMemberById", userId);
        }
    }

    // 회원정보 수정
    public int updateMember(MemberDTO member) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.update(NAMESPACE + "updateMember", member);
            session.commit();
            return result;
        }
    }

    // 회원이 작성한 게시글 수 조회
    public int countBoardByWriterId(String userId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectOne(NAMESPACE + "countBoardByWriterId", userId);
        }
    }

    // 회원 삭제
    public int deleteMember(String userId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.delete(NAMESPACE + "deleteMember", userId);
            session.commit();
            return result;
        }
    }

    // 회원 권한 수정
    public int updateMemberRole(String userId, String role) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("role", role);

        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession(false)) {
            int result = session.update(NAMESPACE + "updateMemberRole", paramMap);
            session.commit();
            return result;
        }
    }
    
    // 회원 목록 조회
    public List<MemberDTO> selectMemberList() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            return session.selectList(NAMESPACE + "selectMemberList");
        }
    }
}