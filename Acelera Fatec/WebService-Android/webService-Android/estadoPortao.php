<?php
	
	header('Content-Type: text/html; charset=utf-8');
	$estado=$_POST['estado_Android'];
	$horaAcao=$_POST['hora_Android'];
	$dataAcao=$_POST['data_Android'];
	$diaSemana=$_POST['dia_Android'];

	try
	{
		$conecta = new PDO('mysql:host=localhost;port=3306;dbname=safehouse','root','');
		$conecta->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

		$inserir=$conecta->query("INSERT INTO portao(estado,horaAcao,dataAcao,diaSemana)VALUES
		('".$estado."','".$horaAcao."','".$dataAcao."','".$diaSemana."')");	
	}
	catch(PDOException $erro)
	{
		echo "Ocorreu um erro ao salvar o estado do Alarme: = $erro";
	}
?>