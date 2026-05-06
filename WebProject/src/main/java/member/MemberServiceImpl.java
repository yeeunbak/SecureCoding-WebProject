package member;

import java.util.List;

public class MemberServiceImpl implements MemberService {

	/* DAO 통해 접근 */
    private MemberDAO memberDAO;

    /* DAO 호출 */
    public MemberServiceImpl() {
        memberDAO = new MemberDAO();
    }

    /* 회원가입 로직 실행 */
    @Override
    public int joinMember(MemberDTO member) {
    	
        if (memberDAO.isUserIdExists(member.getUserId())) { // ID 중복체크
            return -1;
        }
        if (memberDAO.isEmailExists(member.getUserEmail())) { // 이메일 중복체크
            return -2;
        }

        /* 사용자 OR 관리자 -> 사용자 DEFAULT */
        member.setUserRole("USER");

        /* DAO가 DB에 저장*/
        return memberDAO.insertMember(member);
    }

    @Override
    public boolean isUserIdExists(String userId) {
        return memberDAO.isUserIdExists(userId);
    }

    @Override
    public boolean isEmailExists(String email) {
        return memberDAO.isEmailExists(email);
    }
    
    @Override
    public MemberDTO login(String userId, String userPw) {
        return memberDAO.login(userId, userPw);
    }
    
    @Override
    public MemberDTO getMemberById(String userId) {
        return memberDAO.getMemberById(userId);
    }

    @Override
    public int updateMember(MemberDTO member) {
        return memberDAO.updateMember(member);
    }
    
    @Override
    public int countBoardByWriterId(String userId) {
        return memberDAO.countBoardByWriterId(userId);
    }

    @Override
    public int deleteMember(String userId) {
        return memberDAO.deleteMember(userId);
    }
    
    @Override
    public int updateMemberRole(String userId, String role) {
        return memberDAO.updateMemberRole(userId, role);
    }
    
    @Override
    public List<MemberDTO> selectMemberList() {
        return memberDAO.selectMemberList();
    }
}