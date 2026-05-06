package member;

import java.util.List;

public interface MemberService {

    int joinMember(MemberDTO member);

    boolean isUserIdExists(String userId);
    boolean isEmailExists(String email);
    
    MemberDTO login(String userId, String userPw);
    
    MemberDTO getMemberById(String userId);
    
    int updateMember(MemberDTO member);
    
    int countBoardByWriterId(String userId);

    int deleteMember(String userId);
    
    int updateMemberRole(String userId, String role);
    
    List<MemberDTO> selectMemberList();
}