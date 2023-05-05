const loginText = document.querySelector(".title-text .login");
const loginForm = document.querySelector("form.login");
const loginBtn = document.querySelector("label.login");
const signupBtn = document.querySelector("label.signup");
const signupLink = document.querySelector("form .signup-link a");
signupBtn.onclick = (() => {
  loginForm.style.marginLeft = "-50%";
  loginText.style.marginLeft = "-50%";
});
loginBtn.onclick = (() => {
  loginForm.style.marginLeft = "0%";
  loginText.style.marginLeft = "0%";
});
signupLink.onclick = (() => {
  signupBtn.click();
  return false;
});

document.getElementById("hello").addEventListener("submit", (event) => {
  event.preventDefault()
})
document.getElementById("hello2").addEventListener("submit", (event) => {
  event.preventDefault()
})



firebase.auth().onAuthStateChanged((user) => {
  if (user) {
    location.replace("index.html")
  }
});

function login() {
  const email = document.querySelector(".email").value
  const password = document.querySelector(".psw").value
  firebase.auth().signInWithEmailAndPassword(email, password)
    .catch((error) => {
      error.message
    })
}

// Set up our register function

function signUp() {
  const email = document.querySelector(".email2").value
  const password = document.querySelector(".psw2").value


  firebase.auth().createUserWithEmailAndPassword(email, password).then((userCredential) => {

    //writeUserData(user,org)
  })
    .catch((error) => {
      console.log(error)
    });
}

document.querySelector(".log2").addEventListener("click", () => {
  var org = {
    email: document.querySelector(".email2").value,
    password: document.querySelector(".psw2").value,
    confirmpassword: document.querySelector(".psw3").value,
  }
  console.log(org)
  writeUserData(org)
})

function writeUserData(org) {
  console.log(org)
  firebase.database().ref("user/" + org.password).set(org);
}