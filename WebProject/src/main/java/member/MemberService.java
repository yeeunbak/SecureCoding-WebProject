package member;

public interface MemberService {

	/* 필요한 메서드 -> 구현 MemberServiceImpl */
    int joinMember(MemberDTO member);

    boolean isUserIdExists(String userId);
    boolean isEmailExists(String email);
    
    MemberDTO login(String userId, String userPw);
    
    MemberDTO getMemberById(String userId);
    
    int updateMember(MemberDTO member);
}