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
	<body style="background: white;">
			<div style="background: white; width: 100%; height: 72%">
                <div >
					<br><br>
					<center>
					<h2><b>Gráficos</b></h2>
					</center>
                    <div style="margin-top: 67px">
						<div style="width: 50%;float: left;">
							<center>
                            <img width="100" height="100" src="https://img.icons8.com/color/96/000000/secured-by-alarm-system.png" alt="" style="border-radius: 50%;">
                            <h3><p><a href="http://192.168.0.109/safehouse/graficoPizza2.php" title="Clique">Percentual de Ativação</a></p></h3>
							</center>
						</div>
						<div style="width: 50%;float: left;">
							<center>
                            <img width="100" height="100" src="https://img.icons8.com/color/96/000000/open-window.png" alt="" style="border-radius: 50%;">
                            <h3><p><a href="http://192.168.0.109/safehouse/graficoTimeLine2.php" title="Clique">Linha do Tempo de Ativação</a></p></h3>
							</center>
						</div>
                        <div style="width: 50%;float: left;">
							<center>
							<img width="100" height="100" src="https://img.icons8.com/dusk/96/000000/garage-door-part-open.png" alt="" style="border-radius: 50%;">
                            <h3><p><a href="#" title="Clique">Gráfico da Garagem</a></p></h3>
							</center>
						</div>
                        <div style="width: 50%;float: left;">
							<center>
							<img width="100" height="100" src="https://img.icons8.com/color/96/000000/door-sensor-alarmed.png" alt="" style="border-radius: 50%;">
                            <h3><p><a href="#" title="Clique">Gráfico da Janela</a></p></h3>
							</center>
						</div>
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
