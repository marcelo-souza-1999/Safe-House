<?php
	
	header('Content-Type: text/html; charset=utf-8');

	$senha = $_POST['senha_Android'];
	
	try
	{
		$conecta = new PDO('mysql:host=localhost;port=3306;dbname=safehouse','root','');
		$conecta->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

		$comandoSQL = $conecta->query("UPDATE alarme SET senha='$senha'");

	}
	catch(PDOException $erro)
	{
		echo "Ocorreu um erro ao atualizar senha do alarme: = $erro";
	}
?>