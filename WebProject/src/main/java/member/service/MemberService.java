package member.service;

import java.util.List;

import member.dto.MemberDTO;

public interface MemberService {

	// 회원 가입
    int joinMember(MemberDTO member);
    
    // 아이디 중복 검사
    boolean isUserIdExists(String userId);
    
    // 이메일 중복 검사
    boolean isEmailExists(String email);
    
    // 로그인
    MemberDTO login(String userId, String userPw);
    
    // 사용자 아이디로 회원 정보 조회
    MemberDTO getMemberById(String userId);
    
    // 회원 정보 수정
    int updateMember(MemberDTO member);

    // 회원 삭제
    int deleteMember(String userId);
    
    // 회원 권한 수정
    int updateMemberRole(String userId, String role);
    
    // 전체 회원 목록 조회
    List<MemberDTO> selectMemberList();
}
