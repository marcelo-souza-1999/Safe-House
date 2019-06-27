<!DOCTYPE html>
<html>
    <head>
        <!-- <meta name="google-signin-client_id" content="851760339321-2kbsgppnrkis982a30hijlgb4fh0tg2q.apps.googleusercontent.com"> -->
        <!--Import Google Icon <FONT> </FONT>nt-->
        <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <!--Import materialize.css-->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/css/materialize.min.css">
        <!--JavaScript at end of body for optimized loading-->
        <!-- <script type="text/javascript" src="js/materialize.min.js"></script> -->
        <link rel="icon" href= "#">
        <title>Safe House</title>

        <style type="text/css">
            html,
            body 
            {
                height: 100%;
            }
            
            html 
            {
                display: table;
                margin: auto;
            }
            
            body 
            {
                display: table-cell;
                vertical-align: middle;
            }
            
            .margin 
            {
                margin: 0 !important;
            }
        </style>
        <!--Let browser know website is optimized for mobile-->
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    </head>

    <body class="light-green lighten-5">
        <div class="cabecalhoLogin">
            <div class="nav-wrapper container">
                <ul class="right hide-on-med-and-down"></ul>
            </div>
            <div class="container">
                <div class="row">
                    <div class="col s12 m8 offset-m2">
                        <div class="card">
                            <div class="card-image">
                                <img src="imgsLogin/login.jpg">
                                <span class="card-title">
                                    <h2><b>Login</b></h2>
                                    <h6><b>Alarme da sua casa</b></h6>
                                </span>
                            </div>
                            <div class="card-content">
                                <div class="input-field">
                                    <input class="validate" id="email_field" type="email" name="cxemail">
                                    <label for="email">Email</label>
                                </div>
                                <div class="row">
                                    <div class="col s12 m8 l9">
                                        <div class="input-field">
                                            <input id="password_field" type="password" name="cxsenha">
                                            <label for="password">Senha</label>
                                        </div>
                                    </div>
                                    <div class="col s12 m4 l3">
                                        <div class="input-field">
                                            <input type="checkbox" id="remember-me" />
                                            <label for="remember-me">Lembrar</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-action blue-grey lighten-3">
                                    <div class="center-align">
                                        <!-- <form method="post">
                                            <input type="text" name="cxemail2" id="cxemail2" style="display: inline;">
                                        </form> -->
                                        <button class="btn blue-grey darken-1" type="submit" onclick="EntrarComEmailSenha()">Entrar</button>
                                        <script type="text/javascript">
                                            function EntrarComEmailSenha()
                                            {
                                                var userEmail = document.getElementById("email_field").value;
                                                var userPass = document.getElementById("password_field").value;
                                                firebase.auth().signInWithEmailAndPassword(userEmail, userPass).then(function(result) 
                                                {
                                                    var user = firebase.auth().currentUser;
                                                    var name, email, photoUrl, uid, emailVerified;

                                                    if (user != null) 
                                                    {
                                                        name = user.displayName;
                                                        email = user.email;
                                                        photoUrl = user.photoURL;
                                                        // emailVerified = user.emailVerified;
                                                        // uid = user.uid;  // The user's ID, unique to the Firebase project. Do NOT use
                                                        //              // this value to authenticate with your backend server, if
                                                        //              // you have one. Use User.getToken() instead.

                                                        user.providerData.forEach(function (profile) 
                                                        {
                                                            console.log("Sign-in provider: " + profile.providerId);
                                                            console.log("  Provider-specific UID: " + profile.uid);
                                                            console.log("  Name: " + profile.displayName);
                                                            console.log("  Email: " + profile.email);
                                                            console.log("  Photo URL: " + profile.photoURL);
                                                        });
                                                        // alert(email+"\n"+name+"\n"+photoUrl);
                                                        var b = {'email': email,'nome':name, 'imagem':photoUrl};
                                                        // var b = {'email': email, };
                                                        b = JSON.stringify(b);
                                                        sessionStorage.setItem('chave', b);
                                                        document.location.href='http://localhost/safehouse/paginainicial.php';
                                                    }

                                                }).catch(function(error) 
                                                {
                                                    // Handle Errors here.
                                                    var errorCode = error.code;
                                                    var errorMessage = error.message;
                                                    window.alert("Error : " + errorMessage);
                                                });
                                                //verificar();
                                            }
                                        </script> 
                                        <button class="btn blue-grey darken-1" onclick="logout()">Sair</button> 
                                        <script type="text/javascript">
                                                function logout()
                                                {
                                                    firebase.auth().signOut();
                                                    alert("Saiu");
                                                } 

                                            </script>
                                        <br><br>
                                        <center>
                                            <input type="image" src="images/EntrarGoogle.png" width="275px" height="40px" onclick="EntrarComGoogle()"> 
                                        <!-- <img src="images/EntrarGoogle.png" width="275px" height="40px" onclick="onSignIn()"> -->
                                        
                                            <script type="text/javascript">
                                                function EntrarComGoogle() 
                                                {
                                                    var provider = new firebase.auth.GoogleAuthProvider();
                                                    firebase.auth().signInWithPopup(provider).then(function(result) 
                                                    {
                                                        // var token = result.credential.accessToken;
                                                        // var user = result.user;
                                                        
                                                        var user = firebase.auth().currentUser;
                                                        var name, email, photoUrl, uid, emailVerified;
                                                        if (user != null) 
                                                        {
                                                            name = user.displayName;
                                                            email = user.email;
                                                            photoUrl = user.photoURL;
                                                            var b = {'email': email,'nome':name, 'imagem':photoUrl};
                                                            b = JSON.stringify(b);
                                                            sessionStorage.setItem('chave', b);
                                                            alert("Email: "+email+"\n"+"Nome: "+name+"\n"+"URL: "+photoUrl);
                                                            document.location.href='http://localhost/safehouse/paginainicial.php';
                                                        }

                                                        

                                                        // Testar();
                                                         
                                                      // ...
                                                    }).catch(function(error) 
                                                    {
                                                        // Handle Errors here.
                                                        var errorCode = error.code;
                                                        var errorMessage = error.message;
                                                        // The email of the user's account used.
                                                        var email = error.email;
                                                        // The firebase.auth.AuthCredential type that was used.
                                                        var credential = error.credential;
                                                        alert(error);
                                                      // ...
                                                    }); 
                                                }
                                                    
                                                // function Testar() 
                                                // {
                                                //     firebase.auth().onAuthStateChanged(function(user) 
                                                //     {
                                                //         if (user) 
                                                //         {
                                                //             // User is signed in.
                                                //             var displayName = user.displayName;
                                                //             var email = user.email;
                                                //             var emailVerified = user.emailVerified;
                                                //             var photoURL = user.photoURL;
                                                //             var isAnonymous = user.isAnonymous;
                                                //             var uid = user.uid;
                                                //             var providerData = user.providerData;
                                                //             alert(displayName+"\n"+photoURL);
                                                            

                                                //             var b = {'email': email,'nome':displayName, 'imagem':photoURL};
                                                //             b = JSON.stringify(b);
                                                //             sessionStorage.setItem('chave', b);
                                                //             document.location.href='http://localhost/safehouse/paginainicial.php';
                                                //             // document.loc.assign("http://localhost/safehouse/paginainicial.php");
                                                //             // ...
                                                //         } 
                                                //         else 
                                                //         {
                                                //             // User is signed out.
                                                //             // ...
                                                //         }
                                                //     });
                                                // }
                                            </script>
                                        </center>
                                        <br>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col s3 right-align">
                                <a href="http://localhost/alarme/registrarUsuario.php">Cadastre-se</a>
                            </div>
                            <div class="col s8 right-align">
                                <a href="#" class="">Esqueci a senha</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--Import jQuery before materialize.js-->
        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/js/materialize.min.js"></script>
       <!-- The core Firebase JS SDK is always required and must be listed first -->
        
        <script src="https://www.gstatic.com/firebasejs/6.0.2/firebase.js"></script>
        <!-- TODO: Add SDKs for Firebase products that you want to use
        https://firebase.google.com/docs/web/setup#config-web-app -->

        <script>
        // Your web app's Firebase configuration
            var firebaseConfig = {
                apiKey: "AIzaSyApN-Z5YWO-ufPxpHuFvNLdiAw5KKE0l8Y",
                authDomain: "safe-house-92904.firebaseapp.com",
                databaseURL: "https://safe-house-92904.firebaseio.com",
                projectId: "safe-house-92904",
                storageBucket: "safe-house-92904.appspot.com",
                messagingSenderId: "741654113833",
                appId: "1:741654113833:web:98795e652fc30858"
            };
            // Initialize Firebase
            firebase.initializeApp(firebaseConfig);
        </script>
    </body>
</html>