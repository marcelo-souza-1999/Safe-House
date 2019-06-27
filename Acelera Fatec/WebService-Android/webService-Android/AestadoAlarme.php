<?php
	header('Content-Type: text/html; charset=utf-8');

	$dados=$_GET['dados_Alarme'];

	$dados2 = explode(".", $dados);
	$estado= $dados2[0];
	$horaAcao= $dados2[1];
	$dataAcao= $dados2[2];
    $diaSemana= $dados2[3];
  
    try
	{
        $conecta = new PDO('mysql:host=localhost;port=3306;dbname=safehouse','root','');
		$conecta->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

		// $inserir=$conecta->query("INSERT INTO registroAlarme(estado,horaAcao,dataAcao,diaSemana)
		// VALUES('".$estado."','".$horaAcao."','".$dataAcao."','".$diaSemana."')");

		$inserir=$conecta->query("INSERT INTO registroAlarme(estado,horaAcao,dataAcao,diaSemana)
		VALUES('".$estado."','".$horaAcao."','".$dataAcao."','".$diaSemana."')");
    }
	catch(PDOException $erro)
	{
		echo "Ocorreu um erro ao salvar o estado do Alarme: = $erro";
    } 
?>