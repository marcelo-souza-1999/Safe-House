function verificar()
{
  firebase.auth().onAuthStateChanged(function(user) 
  {
    if (user) 
    {
    // User is signed in.
    //window.location.assign("http://localhost/safehouse/paginainicial.php");
    // document.getElementById("user_div").style.display = "block";
    // document.getElementById("login_div").style.display = "none";

    // var user = firebase.auth().currentUser;

    // if(user != null)
    // {

    //   var email_id = user.email;
    //   // document.getElementById("user_para").innerHTML = "Welcome User : " + email_id;

    //   window.alert("Welcome User : " + email_id);

    // }

  } 
  else 
  {
    // No user is signed in.


    //window.alert('Erro');

    // document.getElementById("user_div").style.display = "none";
    // document.getElementById("login_div").style.display = "block";

  }
});
}


function login()
{

  var userEmail = document.getElementById("email_field").value;
  var userPass = document.getElementById("password_field").value;

  firebase.auth().signInWithEmailAndPassword(userEmail, userPass).catch(function(error) 
  {

    // Handle Errors here.
    var errorCode = error.code;
    var errorMessage = error.message;

    window.alert("Error : " + errorMessage);

    // ...
  });
  //verificar();
}

function logout()
{
  firebase.auth().signOut();
}
