document.addEventListener("DOMContentLoaded", function () {
    const contextPath = document.body.dataset.contextPath;

    const joinForm = document.getElementById("joinForm");

    const userIdInput = document.getElementById("userId");
    const userEmailInput = document.getElementById("userEmail");

    const checkIdBtn = document.getElementById("checkIdBtn");
    const checkEmailBtn = document.getElementById("checkEmailBtn");

    const userIdMsg = document.getElementById("userIdMsg");
    const userEmailMsg = document.getElementById("userEmailMsg");
    const joinFormMsg = document.getElementById("joinFormMsg");

    let checkedUserId = "";
    let checkedUserEmail = "";

    function setMessage(target, message, isSuccess) {
        target.textContent = message;
        target.className = isSuccess ? "field-message success" : "field-message error";
    }

    function clearMessage(target) {
        target.textContent = "";
        target.className = "field-message";
    }

    userIdInput.addEventListener("input", function () {
        checkedUserId = "";
        clearMessage(userIdMsg);
    });

    userEmailInput.addEventListener("input", function () {
        checkedUserEmail = "";
        clearMessage(userEmailMsg);
    });

    checkIdBtn.addEventListener("click", function () {
        const userId = userIdInput.value.trim();

        if (userId === "") {
            setMessage(userIdMsg, "아이디를 입력해주세요.", false);
            return;
        }

        fetch(contextPath + "/checkDuplicate?type=userId&value=" + encodeURIComponent(userId))
            .then(response => response.text())
            .then(result => {
                const trimmed = result.trim();

                if (trimmed == "duplicate") {
                    checkedUserId = "";
                    setMessage(userIdMsg, "중복된 아이디입니다.", false);
                } else {
                    checkedUserId = userId;
                    setMessage(userIdMsg, "사용 가능한 아이디입니다.", true);
                }
            })
            .catch(() => {
                checkedUserId = "";
                setMessage(userIdMsg, "아이디 중복 확인 중 오류가 발생했습니다.", false);
            });
    });

    checkEmailBtn.addEventListener("click", function () {
        const userEmail = userEmailInput.value.trim();

        if (userEmail === "") {
            setMessage(userEmailMsg, "이메일을 입력해주세요.", false);
            return;
        }

        fetch(contextPath + "/checkDuplicate?type=userEmail&value=" + encodeURIComponent(userEmail))
            .then(response => response.text())
            .then(result => {
                const trimmed = result.trim();

                if (trimmed == "duplicate") {
                    checkedUserEmail = "";
                    setMessage(userEmailMsg, "중복된 이메일입니다.", false);
                } else {
                    checkedUserEmail = userEmail;
                    setMessage(userEmailMsg, "사용 가능한 이메일입니다.", true);
                }
            })
            .catch(() => {
                checkedUserEmail = "";
                setMessage(userEmailMsg, "이메일 중복 확인 중 오류가 발생했습니다.", false);
            });
    });

    joinForm.addEventListener("submit", function (e) {
        joinFormMsg.textContent = "";

        const currentUserId = userIdInput.value.trim();
        const currentUserEmail = userEmailInput.value.trim();

        if (checkedUserId !== currentUserId) {
            e.preventDefault();
            setMessage(userIdMsg, "아이디 중복 확인을 해주세요.", false);
            return;
        }

        if (checkedUserEmail != currentUserEmail) {
            e.preventDefault();
            setMessage(userEmailMsg, "이메일 중복 확인을 해주세요.", false);
            return;
        }
    });

    const params = new URLSearchParams(window.location.search);
    const msg = params.get("msg");

    if (msg == "duplicateId") {
        setMessage(userIdMsg, "중복된 아이디입니다.", false);
    } else if (msg == "duplicateEmail") {
        setMessage(userEmailMsg, "중복된 이메일입니다.", false);
    } else if (msg == "fail") {
        joinFormMsg.textContent = "회원가입에 실패했습니다.";
    }
});