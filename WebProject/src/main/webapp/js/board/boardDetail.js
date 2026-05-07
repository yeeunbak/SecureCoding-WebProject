document.addEventListener("DOMContentLoaded", function () {

    // 댓글 수정 폼 토글
    const toggleButtons = document.querySelectorAll(".comment-edit-toggle-btn");

    toggleButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            const commentId = button.dataset.commentId;

            toggleEdit(commentId);
        });
    });

    // 이동 버튼
    const moveButtons = document.querySelectorAll(".move-btn");

    moveButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            location.href = button.dataset.url;
        });
    });

    // 댓글 삭제 확인
    const commentDeleteButtons = document.querySelectorAll(".comment-delete-btn");

    commentDeleteButtons.forEach(function (button) {

        button.addEventListener("click", function (e) {

            const result = confirm("댓글을 삭제하시겠습니까?");

            if (!result) {
                e.preventDefault();
            }
        });
    });

    // 게시글 삭제 확인
    const boardDeleteButtons = document.querySelectorAll(".board-delete-btn");

    boardDeleteButtons.forEach(function (button) {

        button.addEventListener("click", function (e) {

            const result = confirm("정말 삭제하시겠습니까?");

            if (!result) {
                e.preventDefault();
            }
        });
    });
});

// 댓글 수정 폼 열기/닫기
function toggleEdit(commentId) {

    const editForm = document.getElementById("edit-form-" + commentId);

    if (editForm.classList.contains("hidden")) {
        editForm.classList.remove("hidden");

    } else {
        editForm.classList.add("hidden");
    }
}