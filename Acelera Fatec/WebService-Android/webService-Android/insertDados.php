<?php
	
	header('Content-Type: text/html; charset=utf-8');
	
	$nome=$_POST['nome_Android'];
	$pathFoto=$_POST['caminho_Android'];
	$userAdm = "adm";
	$userComum = "comum";
	$email=$_POST['email_Android'];
	$dataCadastro=$_POST['dataCad_Android'];
	$horaCadastro=$_POST['horaCad_Android'];
	$nomeCasa=$_POST['casa_Android'];
	$resultado;

	try
	{
		$conecta = new PDO('mysql:host=localhost;port=3306;dbname=safehouse','root','');
		$conecta->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

		$consulta=$conecta->query("SELECT * FROM usuario WHERE alarmeNomeCasa='$nomeCasa'");

		foreach($consulta as $linha)
		{
			$resultado = $linha ['alarmeNomeCasa'];
		}

		if($resultado == $nomeCasa)
		{
			//Casa existe
			$inserir=$conecta->query("INSERT INTO usuario(ID,nome,caminho_foto,tipo,email,data_cadastro,hora_cadastro,alarmeNomeCasa) VALUES
			('','".$nome."','".$pathFoto."','".$userComum."','".$email."','".$dataCadastro."','".$horaCadastro."','".$nomeCasa."')");
		}
		else
		{
			//Casa não existe
			$inserir=$conecta->query("INSERT INTO usuario(ID,nome,caminho_foto,tipo,email,data_cadastro,hora_cadastro,alarmeNomeCasa) VALUES
			('','".$nome."','".$pathFoto."','".$userAdm."','".$email."','".$dataCadastro."','".$horaCadastro."','".$nomeCasa."')");
		}

	}
	catch(PDOException $erro)
	{
		echo "Ocorreu um erro ao inserir dados: = $erro";
	}
?>