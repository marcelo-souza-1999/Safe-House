<?php
	
	header('Content-Type: text/html; charset=utf-8');

	try
	{
		$conecta = new PDO('mysql:host=localhost;port=3306;dbname=safehouse','root','');
		$conecta->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

		$consulta = $conecta->query("SELECT senha FROM alarme");

		$data = array();
		foreach($consulta as $linhas)
		{
			$data["dados"][]=$linhas;
		}
		if($data != null)
		{
			echo json_encode($data);
		}
		else
		{
			$data = "Defina uma senha";
			echo json_encode($data);
		}
		
		
	}
	catch(PDOException $erro)
	{
		echo "Ocorreu um erro ao recuperar a senha do Alarme: = $erro";
	}
?>