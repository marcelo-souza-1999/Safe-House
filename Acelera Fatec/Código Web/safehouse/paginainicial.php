<?php
	try
	{
		$conecta = new PDO('mysql:host=localhost;port=3306;dbname=safehouse','root','');
		$conecta->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

		$consulta = $conecta->query("SELECT * FROM alarme");

		foreach($consulta as $linhas)
		{
			$senha = $linhas['senha'];
			$estado = $linhas['estado'];
		}	
		// echo $senha;
		// echo $estado;
	}
	catch(PDOExcpetion $erro)
	{
		echo "Ocorreu um erro ao verificar dados: = $erro";
	}
?>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Safe House</title>
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600,700,900" rel="stylesheet" />
		<link href="default.css" rel="stylesheet" type="text/css" media="all" />
		<link href="fonts.css" rel="stylesheet" type="text/css" media="all" />
	</head>
	<body onload ="Carregar()">
		<script type="text/javascript">
			function Carregar() 
			{
				var c = JSON.parse(sessionStorage.getItem('chave'));
				document.getElementById("nomeUsuario").innerHTML = c.nome;
				document.getElementById("img01").src=c.imagem;
			}
		</script>
		<div id="page" class="container">
			<div id="header" style="height: 600px">
				<div  style="text-align: center;">
					<img width="100" height="100" src="" alt="" style="border-radius: 50%;" id="img01">
					<br><br>
					<h1><a style="color: white;" id="nomeUsuario" ></a></h1>
					<span style="color: gray;text-transform: uppercase;font-size: 15px">Safe House</span>
				</div>
				<div style="text-align: center;">
					<h3><a style="color: yellow;" ><?php echo "Alarme: ".$estado; ?></a></h3>
					<br>
					<br>
				</div>
				<div id="menu" style="height: 425px;">
					<ul>
						<li class="current_page_item"><a href="http://localhost/safehouse/paginainicial.php" accesskey="1" title="">Página Inicial</a></li>
						<li><a href="http://localhost/safehouse/exibirgraficos.php" accesskey="2" title="">Exibir Gráficos</a></li>
						<li><a href="http://localhost/safehouse/usuarioscadastrados.php" accesskey="3" title="">Usuários Cadastrados</a></li>
						<!-- <li><a href="http://localhost/safehouse/alterarsenha.php" accesskey="4" title="">Alterar Senha</a></li> -->
						<li><a href="http://localhost/safehouse/sobre.php" accesskey="5" title="">Sobre e Integrantes da Equipe</a></li>
						<li><a accesskey="6" title="" style="cursor: pointer;border-bottom: 1px solid rgba(255,255,255,0.08);" onclick="Sair();">Sair</a></li>
						<script type="text/javascript">
							function Sair() 
							{
								// var user = firebase.auth().currentUser;
								// if (user != null) 
        //                         {
        //                             name = user.displayName;
        //                             email = user.email;
        //                             alert(name+"<br>"+email);
        //                         }
								firebase.auth().signOut().then(function() 
								{
									sessionStorage.clear();
									window.location.href='http://localhost/safehouse/index.php';
									// alert("Saiu");

								}).catch(function(error) 
								{
									alert("Erro: "+error);
								});
							}
						</script>
					</ul>
				</div>
			</div>
			<div id="main" style="height: 591px;">
				<div id="banner" style="height: 465px">
					<!-- Ver como coloca a câmera IP mostrando tudo ao vivo -->
					<!-- <video id="video" width="720" height="480" controls="controls" autoplay="autoplay">
				      <source src="videos/supernatural.mp4" type="video/mp4" />
				      <source src="videos/supernatural.ogv" type="video/ogv">
				      <source src="videos/supernatural.webm" type="video/webm">
				    </video> -->
				    <br><br>
				    <center>
					    <!-- <div style="border: 2px solid; width: 720px; height: 480px"> -->
					    	<img border="0" width="720" height="480" src="http://192.168.0.101:81/videostream.cgi?loginuse=admin&amp;loginpas=" alt="live câmera">
					    <!-- </div> -->
			    	</center>

					<!-- Teste de vídeos do Youtube -->
					<!-- <embed src="http://localhost/graficosGoogle/graficos.html" allowfullscreen="true" width="720" height="480"> -->

						<!-- <embed src="http://localhost/graficosGoogle/graficos.html" allowfullscreen="true" width="720" height="480"> -->

					<!-- <iframe  width="720" height="480" src="https://www.youtube.com/embed/5BNZVenr12s" frameborder="0" allowfullscreen></iframe>

					<iframe  width="720" height="480" src="https://www.youtube.com/embed/Gxy28omeCNg" frameborder="0" allowfullscreen></iframe> -->

					<!-- <iframe  width="720" height="480" src="link do vídeo" frameborder="0" allowfullscreen></iframe>			
					Para colocar videos do Youtube -> src="https://www.youtube.com/embed/(código do vídeo)

					<iframe  width="720" height="480" src="link do vídeo" frameborder="0" allowfullscreen></iframe> -->

				</div>

				<div>
					<div class="title">
						<h1 style="font-size: 34px"><strong>Saiba tudo o que acontece na sua casa.</strong></h1>
					</div>
					<br>
					<footer>
						<nav align="center" class="navbar fixed-bottom navbar-light" style="background-color: #ffffff;">
							Safe House 2019, Todos os direitos reservados.
						</nav>
					</footer>
				</div>
			</div>
		</div>

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