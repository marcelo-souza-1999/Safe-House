<?php

	header('Content-Type: text/html; charset=utf-8');

	$senha = $_POST['senha_Android'];
	$estado = $_POST['estado_Android'];
	
	try
	{
		$conecta = new PDO('mysql:host=localhost;port=3306;dbname=safehouse','root','');
		$conecta->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

		$comandoSQL = $conecta->query("INSERT INTO alarme(senha,estado) VALUES('".$senha."','".$estado."')");
	}
	catch(PDOException $erro)
	{
		echo "Ocorreu um erro ao inserir senha do alarme: = $erro";
	}
?>