package member;

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
        if (memberDAO.isUserIdExists(member.getUserId())) { /* ID 중복체크 */
            return -1;
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
    public MemberDTO login(String userId, String userPw) {
        return memberDAO.login(userId, userPw);
    }
}