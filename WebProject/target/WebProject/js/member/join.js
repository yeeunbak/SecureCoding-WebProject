// 아이디, 이메일 중복체크 AJAX로 처리 & 검증

document.addEventListener("DOMContentLoaded", function () { // HTML 다 로드된 후 실행
	
	// JSP에서 넣어준 contextPath 사용
    const contextPath = document.body.dataset.contextPath;

	// 가입 Form 가져오기
    const joinForm = document.getElementById("joinForm");

	// 입력값 가져오기
    const userIdInput = document.getElementById("userId");
    const userEmailInput = document.getElementById("userEmail");

	// 중복확인 버튼
    const checkIdBtn = document.getElementById("checkIdBtn");
    const checkEmailBtn = document.getElementById("checkEmailBtn");

	// 메세지 출력 영역
    const userIdMsg = document.getElementById("userIdMsg");
    const userEmailMsg = document.getElementById("userEmailMsg");
    const joinFormMsg = document.getElementById("joinFormMsg");

	// 검증 완료된 값 저장 (중복 아닌 값) -> 중복확인 후, 값 변경 하고 바로 가입 시도 방지
    let checkedUserId = "";
    let checkedUserEmail = "";

	// 메시지 출력, 색상 변경
    function setMessage(target, message, isSuccess) {
        target.textContent = message;
        target.className = isSuccess ? "field-message success" : "field-message error"; // css로 색 제어
    }

	// 메세지 초기화
    function clearMessage(target) {
        target.textContent = "";
        target.className = "field-message";
    }

	// 입력 변경 감지 -> 값 바뀌면 기존 검증 무효화, 메세지 삭제
    userIdInput.addEventListener("input", function () {
        checkedUserId = "";
        clearMessage(userIdMsg);
    });

    userEmailInput.addEventListener("input", function () {
        checkedUserEmail = "";
        clearMessage(userEmailMsg);
    });

	// 아이디 중복 체크 
    checkIdBtn.addEventListener("click", function () {
        const userId = userIdInput.value.trim();

		// 서버 요청 전, 빈 값 체크
        if (userId === "") {
            setMessage(userIdMsg, "아이디를 입력해주세요.", false);
            return;
        }

		// 서버 요청
        fetch(contextPath + "/checkDuplicate?type=userId&value=" + encodeURIComponent(userId))
            .then(response => response.text()) // 응답 처리
            .then(result => {
                const trimmed = result.trim();

				
                if (trimmed == "duplicate") { 							// 중복일 때
                    checkedUserId = ""; 								// 초기화
                    setMessage(userIdMsg, "중복된 아이디입니다.", false);		// 빨간 메세지
                } else {
                    checkedUserId = userId;								// 검증 완료된 값 저장
                    setMessage(userIdMsg, "사용 가능한 아이디입니다.", true);
                }
            })
            .catch(() => {
                checkedUserId = "";
                setMessage(userIdMsg, "아이디 중복 확인 중 오류가 발생했습니다.", false);
            });
    });

	// 이메일 중복 체크 
    checkEmailBtn.addEventListener("click", function () {
        const userEmail = userEmailInput.value.trim();

		// 서버 요청 전, 빈 값 체크
        if (userEmail === "") {
            setMessage(userEmailMsg, "이메일을 입력해주세요.", false);
            return;
        }

		// 서버 요청
        fetch(contextPath + "/checkDuplicate?type=userEmail&value=" + encodeURIComponent(userEmail))
            .then(response => response.text()) // 응답 처리
            .then(result => {
                const trimmed = result.trim();

                if (trimmed == "duplicate") {							// 중복일 때
                    checkedUserEmail = "";								// 초기화
                    setMessage(userEmailMsg, "중복된 이메일입니다.", false);	// 빨간 메세지
                } else {
                    checkedUserEmail = userEmail;						// 검증 완료된 값 저장
                    setMessage(userEmailMsg, "사용 가능한 이메일입니다.", true);
                }
            })
            .catch(() => {
                checkedUserEmail = "";
                setMessage(userEmailMsg, "이메일 중복 확인 중 오류가 발생했습니다.", false);
            });
    });

	// 폼 제출 검증
    joinForm.addEventListener("submit", function (e) {
        joinFormMsg.textContent = "";

		// 현재 값 가져오기
        const currentUserId = userIdInput.value.trim();
        const currentUserEmail = userEmailInput.value.trim();

		// 아이디 & 이메일 검증 확인
        if (checkedUserId !== currentUserId) {
            e.preventDefault(); // form 전송 차단
            setMessage(userIdMsg, "아이디 중복 확인을 해주세요.", false);
            return;
        }
        if (checkedUserEmail != currentUserEmail) {
            e.preventDefault(); // form 전송 차단
            setMessage(userEmailMsg, "이메일 중복 확인을 해주세요.", false);
            return;
        }
    });

	// URL 파라미터 처리
    const params = new URLSearchParams(window.location.search); // 서버에서 화면으로 데이터 전달, ?msg=xxx 가져오기
    const msg = params.get("msg");

    if (msg == "duplicateId") {
        setMessage(userIdMsg, "중복된 아이디입니다.", false);
    } else if (msg == "duplicateEmail") {
        setMessage(userEmailMsg, "중복된 이메일입니다.", false);
    } else if (msg == "fail") {
        joinFormMsg.textContent = "회원가입에 실패했습니다.";
    }
});