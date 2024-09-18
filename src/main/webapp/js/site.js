console.log("Script works");

document.addEventListener("DOMContentLoaded", function () {
    var navLinks = document.querySelectorAll('#nav-mobile li a');

    var currentPath = window.location.pathname;

    // Проверяем каждый элемент меню и делаем активным, если путь совпадает
    navLinks.forEach(function(link) {
        if (link.getAttribute('href') === currentPath) {
            link.parentElement.classList.add('active');
        } else {
            link.parentElement.classList.remove('active');
        }
    });
});
