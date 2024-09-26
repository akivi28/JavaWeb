console.log("Script works");

document.addEventListener("DOMContentLoaded", function () {
    M.Modal.init(
        document.querySelectorAll('.modal'), {
            opacity: 0.5,
            inDuration:	250,
            outDuration: 250,
            onOpenStart: null,
            onOpenEnd: null,
            onCloseStart: null,
            onCloseEnd:	null,
            preventScrolling: true,
            dismissible: true,
            startingTop: '4%',
            endingTop: '10%',
        });

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
        else if(form.id === "modal-auth-form") {
            e.preventDefault();
            const queryString = new URLSearchParams(new FormData(form)).toString();
            fetch(`${form.action}?${queryString}` ,{
                method: 'PATCH'
            }).then(r => r.json()).then(j =>{
                if(j.status === "Ok"){
                    window.location.reload()
                }
                else {
                    console.log(j)
                }
            });
        }
    })

    const logoutButton = document.getElementById("logout-button");
    if (logoutButton) {
        logoutButton.addEventListener("click", function () {
            Swal.fire({
                title: "Are you sure?",
                text: "Do you really want to logout?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes"
            }).then((result) => {
                if (result.isConfirmed) {
                    fetch(`?logout=true`)
                        .then(() => {
                            localStorage.setItem('logout-success-alert', "true");
                            window.location.reload();
                        })
                        .catch(error => console.error('Error logging out:', error));
                }
            });
        });
    }

    window.addEventListener('load', () => {
        if (localStorage.getItem('logout-success-alert') === 'true') {
            Swal.fire({
                title: "Logout",
                text: "You have successfully logged out of your account",
                icon: "success"
            });
            localStorage.removeItem('logout-success-alert');
        }
    });

});
