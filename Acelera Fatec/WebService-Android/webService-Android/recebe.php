<?php

	header('Content-Type: text/html; charset=utf-8');
	$port='COM4';
	$recebe = $_GET['codigo'];
	
	try
	{
		if($recebe != null)
		{
			$conecta=fopen($port,'w+');
			sleep(2);
			fwrite($conecta,$recebe);
			fclose($conecta);
		}
	}
	catch(PDOException $erro)
	{
		echo "Ocorreu um erro ao receber dados: = $erro";
	}
?>