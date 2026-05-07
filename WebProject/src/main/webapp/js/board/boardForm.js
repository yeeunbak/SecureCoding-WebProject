document.addEventListener("DOMContentLoaded", function () {

    const contextPath = document.body.dataset.contextPath;
    const boardId = document.body.dataset.boardId;

    // 첨부파일 삭제 버튼
    const deleteButtons = document.querySelectorAll(".delete-file-btn");

    deleteButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            const fileId = button.dataset.fileId;

            deleteFile(contextPath, boardId, fileId);
        });
    });

    // 취소 버튼
    const cancelBtn = document.querySelector(".cancel-btn");

    if (cancelBtn) {

        cancelBtn.addEventListener("click", function () {

            location.href = cancelBtn.dataset.url;
        });
    }
});

// 첨부파일 삭제
function deleteFile(contextPath, boardId, fileId) {

    if (!confirm("첨부파일을 삭제하시겠습니까?")) {
        return;
    }

    const form = document.createElement("form");

    form.method = "post";
    form.action = contextPath + "/board/file/delete";

    const boardInput = document.createElement("input");

    boardInput.type = "hidden";
    boardInput.name = "boardId";
    boardInput.value = boardId;

    const fileInput = document.createElement("input");

    fileInput.type = "hidden";
    fileInput.name = "fileId";
    fileInput.value = fileId;

    form.appendChild(boardInput);
    form.appendChild(fileInput);

    document.body.appendChild(form);

    form.submit();
}