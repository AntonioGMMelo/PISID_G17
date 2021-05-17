let mail = document.getElementById("email");
let pass = document.getElementById("senha");
let shadowMail = document.getElementById("shadowMail");
let shadowPass = document.getElementById("shadowPass");

mail.addEventListener('focusin', ()=>{

    shadowMail.classList.add("show");

});

mail.addEventListener('focusout', ()=>{

    shadowMail.classList.remove("show");

});

pass.addEventListener('focusin', ()=>{

    shadowPass.classList.add("show");

});

pass.addEventListener('focusout', ()=>{

    shadowPass.classList.remove("show");

});
