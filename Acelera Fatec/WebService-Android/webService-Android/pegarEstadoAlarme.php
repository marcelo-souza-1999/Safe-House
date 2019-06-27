<?php
  	
	header('Content-Type: text/html; charset=utf-8');

	try
	{
		$conecta = new PDO('mysql:host=localhost;port=3306;dbname=safehouse','root','');
		$conecta->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

		$consulta = $conecta->query("SELECT estado FROM alarme");

		$data = array();

		foreach($consulta as $linhas)
		{
			$data["dados"][]=$linhas;
			//$data = $linha ['alarmeNomeCasa'];
		}
		echo json_encode($data);

	}
	catch(PDOException $erro)
	{
		echo "Ocorreu um erro ao verificar dados: = $erro";
	}
?>