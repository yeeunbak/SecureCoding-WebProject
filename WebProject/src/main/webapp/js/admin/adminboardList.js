document.addEventListener("DOMContentLoaded", function () {
    const moveButtons = document.querySelectorAll(".move-btn");

    moveButtons.forEach(function (button) {
        button.addEventListener("click", function () {
            location.href = button.dataset.url;
        });
    });

    const deleteButtons = document.querySelectorAll(".admin-board-delete-btn");

    deleteButtons.forEach(function (button) {
        button.addEventListener("click", function (e) {
            if (!confirm("게시글을 삭제하시겠습니까?")) {
                e.preventDefault();
            }
        });
    });
});