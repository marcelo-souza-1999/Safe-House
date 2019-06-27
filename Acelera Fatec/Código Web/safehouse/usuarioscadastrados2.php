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

		$consulta = $conecta->query("SELECT COUNT(*) AS quantidade FROM usuario;");

		foreach($consulta as $linhas)
		{
			$quant = $linhas['quantidade'];
		}

		
		// echo $quant;

		$consulta = $conecta->query("SELECT nome, email, data_cadastro, hora_cadastro FROM usuario");

		// $data = array();

		$nomes = array();
		$emails = array();
		$dataCadastros = array();
		$horaCadastros = array();


		foreach($consulta as $linhas)
		{
			// $data[]=$linhas;
			$nomes[] = $linhas ['nome'];
			$emails[] = $linhas ['email'];
			$dataCadastros[] = $linhas ['data_cadastro'];
			$horaCadastros[] = $linhas ['hora_cadastro'];
		}
		// echo json_encode($nomes);
		// echo json_encode($emails);
		// echo json_encode($dataCadastros);
		// echo json_encode($horaCadastros);
		

	}
	catch(PDOExcpetion $erro)
	{
		echo "Ocorreu um erro ao verificar dados: = $erro";
	}
?>

<!DOCTYPE html>
  	<html>
    <head>
    	<link rel="icon" href="">
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Safe House</title>
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600,700,900" rel="stylesheet" />
		<link href="default.css" rel="stylesheet" type="text/css" media="all" />
		<link href="fonts.css" rel="stylesheet" type="text/css" media="all" />
      	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	    <script type="text/javascript">
	      	google.charts.load('current', {'packages':['table']});
	      	google.charts.setOnLoadCallback(drawTable);

	      	function drawTable() 
	      	{
	        	var data = new google.visualization.DataTable();
		        data.addColumn('string', 'Nome');
		        data.addColumn('string', 'Email');
		        data.addColumn('string', 'Data e Hora de Cadastro');
		       
		        var n = <?php echo json_encode($nomes);?>;
		        var e = <?php echo json_encode($emails);?>;
		        var d = <?php echo json_encode($dataCadastros);?>;
		        var h = <?php echo json_encode($horaCadastros);?>;
		        var q = <?php echo $quant;?>;
		        for (var i = 0; i < q; i++) 
		        {
		        	data.addRows([
		        		[n[i], e[i], d[i]+" às "+h[i]],
		        	]);
		        }
		        
		        var table = new google.visualization.Table(document.getElementById('table_div'));

		        var altura=0;
		        if(q>=9)
		        {
		        	q=9;
		        }
		        var altura = (45*q)+20;
		        var options = 
                {
                    showRowNumber: true,
                    'width':700,
                    'height': altura
                };
                table.draw(data,options);
		        // table.draw(data, {showRowNumber: true, width: '700', height: altura});
	      	}
	    </script>

    </head>

    <body >
    	  

		<div style="background: white; width: 100%; height: 84%">
			<div id="banner" style="height: 465px;" >
					<!-- <br><br> -->
				<br>
				<center>
                <h3 style="font-size: 22px;">Tabela contendo nome, email e data e hora de cadastro dos usuários da casa</h3>
				</center>
				<br>
                <center>
                    <div id="table_div" style="max-height: 200px;"></div>
                </center>
            </div>
            <center>
                <p style="margin-top: -62px" id="dadosGrafico"></p>
            </center>
        </div>
            
            <div style="background: white;">
            <center>
                          <h1><strong>Saiba tudo o que acontece na sua casa.</strong></h1>
                          </center>
          </div>
            <nav align="center" class="navbar fixed-bottom navbar-light" style="background-color: #ffffff;">
          Safe House 2019, Todos os direitos reservados.
                </nav>
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
        