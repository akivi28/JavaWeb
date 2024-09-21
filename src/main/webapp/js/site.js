console.log("Script works");

document.addEventListener("DOMContentLoaded", function () {
    var navLinks = document.querySelectorAll('#nav-mobile li a');

    var currentPath = window.location.pathname;

    navLinks.forEach(function(link) {
        if (link.getAttribute('href') === currentPath) {
            link.parentElement.classList.add('active');
        } else {
            link.parentElement.classList.remove('active');
        }
    });


    document.addEventListener("submit", e=>{
        const form = e.target;
        if(form.id === "signup-form"){
            e.preventDefault();
            const formData = new FormData(form);
            fetch(form.action,{
                method: "POST",
                body: formData
            }).then(r => r.json()).then(j =>{
                console.log(j)
            });
        }
    })
});
