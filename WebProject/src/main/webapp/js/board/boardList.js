document.addEventListener("DOMContentLoaded", function () {
    const moveButtons = document.querySelectorAll(".move-btn");

    moveButtons.forEach(function (button) {
        button.addEventListener("click", function () {
            location.href = button.dataset.url;
        });
    });
});