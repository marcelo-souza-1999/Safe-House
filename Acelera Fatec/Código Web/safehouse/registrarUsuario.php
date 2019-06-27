<!DOCTYPE html>
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/css/materialize.min.css">
    <link rel="icon" href= "images/LogoIcone.png">
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

    <div class="container" style="width: 550px">
                <form class="login-form" method="post" action="CadastroUsuario.php">
                    <div class="card" style="width: 550px">
                        <div style="background-image: url(imgsLogin/login.jpg);height: 200px;width: 550px;text-align: center;">
                                    <center id="centro">
                                        <br>
                                        <div style="width: 110px; height: 110px;margin-top: 2px;" id="imgPerfil">
                                            <!-- inserção de imagem aqui -->
                                        </div>
                                    </center>
                                
                                <input type="file" onchange="AtualizarFoto(this.files);" accept=".jpg, .jpeg, .png" />    
                                <script>
                                    function AtualizarFoto(files) 
                                    {
                                        if(files[0])
                                        {
                                            var no = document.getElementById('imgPerfil');
                                            if(no.parentNode)
                                            {
                                                no.parentNode.removeChild(no);
                                            }
                                            var divImagem = document.createElement("div");
                                            divImagem.style.width="110px";
                                            divImagem.style.height="110px";
                                            divImagem.style.marginTop="2px";
                                            divImagem.id="imgPerfil";

                                            var arquivo = files[0];
                                            var imagem = document.createElement("img");
                                            imagem.style.width="110px";
                                            imagem.style.height="110px";
                                            imagem.style.borderRadius="50%";
                                            imagem.file = arquivo;
                                            divImagem.appendChild(imagem);
                                            centro.appendChild(divImagem);
                                            var reader = new FileReader();
                                            reader.onload = (function(aImg) 
                                            {
                                                return function(e) 
                                                {
                                                    aImg.src = e.target.result;
                                                    // alert(aImg.src);
                                                };
                                            })(imagem);
                                            reader.readAsDataURL(arquivo);
                                        }
                                    }
                                </script>
                        </div>

                        <div class="card-content"  style="width: 550px">
                            <form style="border: 2px solid">
                                <div class="input-field">
                                    <input  id="text" type="text" name="cxnome">
                                    <label for="email">Nome</label>
                                </div>
                                <div class="input-field">
                                    <input class="validate" id="email" type="email">
                                    <label for="email">Email</label>
                                </div>  
                                <div class="input-field">
                                    <input class="validate" id="email" type="email" name="cxEmail">
                                    <label for="email">Confirme seu Email</label>
                                </div>  
                                <div class="input-field">
                                    <input id="password" type="password">
                                    <label for="password">Senha</label>
                                </div>  
                                <div class="input-field">
                                    <input id="password" type="password" name="cxSenha">
                                    <label for="password">Repita a Senha</label>  
                                </div><br>
                                <div class="card-action blue-grey lighten-3">
                                    <div class="center-align">
                                        <button href="http://localhost/safehouse/paginainicial.php" class="btn blue-grey darken-1" type="submit">Cadastrar</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                </form>
    </div>


    <!--Import jQuery before materialize.js-->
    <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/js/materialize.min.js"></script>
</body>

</html>