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

        $consulta = $conecta->query("SELECT horaAcao,dataAcao FROM registroAlarme");

        $horas = array();
        $datas = array();

        foreach($consulta as $linhas)
        {
            $horas[] = $linhas ['horaAcao'];
            $datas[] = $linhas ['dataAcao'];
        }
        date_default_timezone_set('America/Sao_Paulo');

        $sh=sizeof($horas);
        $sd=sizeof($datas);
        $horas[$sh] = date('H:i:s');
        $datas[$sd] = date('d/m/Y');

        $horaGrafico = $horas[$sh];
        $dataGrafico = $datas[$sd];
        // echo date('H:i:s -  d/m/Y')."<br><br><br>";

        //Cálculo do tempo de alarme desativado (em segundos)
        $final=1;
        $inicial=0;
        $tempoDesativado=0;
        while ($final<sizeof($horas)) 
        {
            $hr1=explode(":", $horas[$final]);
            $hr2=explode(":", $horas[$inicial]);
            $h1=(int)$hr1[0];
            $m1=(int)$hr1[1];
            $s1=(int)$hr1[2];
            $h2=(int)$hr2[0];
            $m2=(int)$hr2[1];
            $s2=(int)$hr2[2];

            $hrs1=($h1*3600)+($m1*60)+$s1;

            $hrs2=($h2*3600)+($m2*60)+$s2;
            if($datas[$final] == $datas[$inicial])
            {
                // echo("Segundos 1: ".$hrs1."<br>");
                // echo("Segundos 2: ".$hrs2."<br>");
                $diferenca=$hrs1-$hrs2;
            }
            // if($datas[$final] != $datas[$inicial])
            else
            {
                $dt1=explode("/", $datas[$final]);
                $dt2=explode("/", $datas[$inicial]);

                $d1=(int)$dt1[0];
                $me1=(int)$dt1[1];
                $a1=(int)$dt1[2];
                $d2=(int)$dt2[0];
                $me2=(int)$dt2[1];
                $a2=(int)$dt2[2];

                $mes = array();
                $mes[0] = 0;
                $mes[1] = 31;
                if(($a1%4)==0)
                {
                    $mes[2] = 29;   
                }
                else
                {
                    $mes[2] = 28;
                }
                $mes[3] = 31;
                $mes[4] = 30;
                $mes[5] = 31;
                $mes[6] = 30;
                $mes[7] = 31;
                $mes[8] = 31;
                $mes[9] = 30;
                $mes[10] = 31;
                $mes[11] = 30;
                
                $dias1=0;
                for($x=0;$x<$me1;$x++)
                {
                    $dias1 +=$mes[$x];
                }
                $dias1+=($d1-1);

                $seg1=$dias1*86400;

                $soma1=$seg1+$hrs1;

                $dias2=0;
                // if($a2>$a1)
                // {
                $quantBissexto=0;
                for($x=$a1;$x<$a2;$x++)
                {
                    if(($x%4)==0)
                    {
                        $quantBissexto++;
                    }
                }
                $dias2=(($a2-$a1)*365)+$quantBissexto;
                if(($a2%4)==0)
                {
                    $mes[2] = 29;   
                }
                else
                {
                    $mes[2] = 28;
                }

                for($x=0;$x<$me2;$x++)
                {
                    $dias2 +=$mes[$x];
                }
                $dias2+=($d2-1);
                $seg2=$dias2*86400;
                $soma2=$seg2+$hrs2;

                // echo("Segundos 1: ".$soma1."<br>");
                // echo("Segundos 2: ".$soma2."<br>");
                $diferenca=$soma1-$soma2;
            }
            $tempoDesativado+=$diferenca;
            // echo($diferenca." segundos.<br><br>");
            $final+=2;
            $inicial+=2;
        }
        // echo("Tempo Desativado: ".$tempoDesativado." segundos.<br><br>");


        //Cálculo do tempo de alarme ativado (em segundos)
        $final=2;
        $inicial=1;
        $tempoAtivado=0;
        while ($final<sizeof($horas)) 
        {
            $hr1=explode(":", $horas[$final]);
            $hr2=explode(":", $horas[$inicial]);
            $h1=(int)$hr1[0];
            $m1=(int)$hr1[1];
            $s1=(int)$hr1[2];
            $h2=(int)$hr2[0];
            $m2=(int)$hr2[1];
            $s2=(int)$hr2[2];

            $hrs1=($h1*3600)+($m1*60)+$s1;

            $hrs2=($h2*3600)+($m2*60)+$s2;
            if($datas[$final] == $datas[$inicial])
            {
                // echo("Segundos 1: ".$hrs1."<br>");
                // echo("Segundos 2: ".$hrs2."<br>");
                $diferenca=$hrs1-$hrs2;
            }
            // if($datas[$final] != $datas[$inicial])
            else
            {
                $dt1=explode("/", $datas[$final]);
                $dt2=explode("/", $datas[$inicial]);

                $d1=(int)$dt1[0];
                $me1=(int)$dt1[1];
                $a1=(int)$dt1[2];
                $d2=(int)$dt2[0];
                $me2=(int)$dt2[1];
                $a2=(int)$dt2[2];

                $mes = array();
                $mes[0] = 0;
                $mes[1] = 31;
                if(($a1%4)==0)
                {
                    $mes[2] = 29;   
                }
                else
                {
                    $mes[2] = 28;
                }
                $mes[3] = 31;
                $mes[4] = 30;
                $mes[5] = 31;
                $mes[6] = 30;
                $mes[7] = 31;
                $mes[8] = 31;
                $mes[9] = 30;
                $mes[10] = 31;
                $mes[11] = 30;
                
                $dias1=0;
                for($x=0;$x<$me1;$x++)
                {
                    $dias1 +=$mes[$x];
                }
                $dias1+=($d1-1);

                $seg1=$dias1*86400;

                $soma1=$seg1+$hrs1;

                $dias2=0;
                // if($a2>$a1)
                // {
                $quantBissexto=0;
                for($x=$a1;$x<$a2;$x++)
                {
                    if(($x%4)==0)
                    {
                        $quantBissexto++;
                    }
                }
                $dias2=(($a2-$a1)*365)+$quantBissexto;
                if(($a2%4)==0)
                {
                    $mes[2] = 29;   
                }
                else
                {
                    $mes[2] = 28;
                }

                for($x=0;$x<$me2;$x++)
                {
                    $dias2 +=$mes[$x];
                }
                $dias2+=($d2-1);
                $seg2=$dias2*86400;
                $soma2=$seg2+$hrs2;

                // echo("Segundos 1: ".$soma1."<br>");
                // echo("Segundos 2: ".$soma2."<br>");
                $diferenca=$soma1-$soma2;
            }
            $tempoAtivado+=$diferenca;
            // echo($diferenca." segundos.<br><br>");
            $final+=2;
            $inicial+=2;
        }
        // echo("Tempo Ativado: ".$tempoAtivado." segundos.<br><br>");
    }
    catch(PDOException $erro)
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
       
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript">
            google.charts.load('current', {'packages':['corechart']});
            google.charts.setOnLoadCallback(drawChart);
            function drawChart() 
            {
                var data = new google.visualization.DataTable();
                var ativo = <?php echo $tempoAtivado; ?>;
                var desativo = <?php echo $tempoDesativado; ?>;
                var horaGerada = <?php echo json_encode($horaGrafico); ?>;
                var dataGerada = <?php echo json_encode($dataGrafico); ?>;
                data.addColumn('string', 'estado');
                data.addColumn('number', 'quantidade');
                data.addRows([
                  ['Ativado', ativo],
                  ['Desativado', desativo],
                ]);
                var options = 
                {
                    'title':'Percentual de tempo de ativação e desativação do alarme',
                    'width':1000,
                    'height':600
                };
                var chart = new google.visualization.PieChart(document.getElementById('banner'));
                document.getElementById('dadosGrafico').innerHTML = "Gráfico gerado às "+horaGerada+" de "+dataGerada+".";
                // var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
                chart.draw(data, options);
            }
        </script>
    </head>
    
    <body onload="Carregar()">
        <script type="text/javascript">
        function Carregar() 
        {
            var c = JSON.parse(sessionStorage.getItem('chave'));
            document.getElementById("nomeUsuario").innerHTML = c.nome;
            document.getElementById("img01").src=c.imagem;
        }
        </script>
        <div id="page" class="container">
    	 	<div id="header" style="height: 640px">
                <div  style="text-align: center;">
                    <img width="100" height="100" src="" alt="" style="border-radius: 50%;" id="img01">
                    <br><br>
                    <h1><a style="color: white;" id="nomeUsuario" ></a></h1>
                    <span style="color: gray;text-transform: uppercase;font-size: 15px">Safe House</span>
                </div>
                <div style="text-align: center;">
                    <h3><a style="color: yellow;" ><?php echo "Alarme: ".$estado; ?></a></h3>
                    <br>
                    <br>
                </div>
                <div id="menu" style="height: 425px;">
                    <ul>
                        <li><a href="http://localhost/safehouse/paginainicial.php" accesskey="1" title="">Página Inicial</a></li>
                        <li class="current_page_item"><a href="http://localhost/safehouse/exibirgraficos.php" accesskey="2" title="">Exibir Gráficos</a></li>
                        <li><a href="http://localhost/safehouse/usuarioscadastrados.php" accesskey="3" title="">Usuários Cadastrados</a></li>
                        <!-- <li class="current_page_item"><a href="http://localhost/safehouse/alterarsenha.php" accesskey="4" title="">Alterar Senha</a></li> -->
                        <li><a href="http://localhost/safehouse/sobre.php" accesskey="5" title="">Sobre e Integrantes da Equipe</a></li>
                        <li><a accesskey="6" title="" style="cursor: pointer;border-bottom: 1px solid rgba(255,255,255,0.08);" onclick="Sair();">Sair</a></li>
                        <script type="text/javascript">
                            function Sair() 
                            {
                                // var user = firebase.auth().currentUser;
                                // if (user != null) 
                                // {
                                //     name = user.displayName;
                                //     email = user.email;
                                //     alert(name+"<br>"+email);
                                // }
                                firebase.auth().signOut().then(function() 
                                {
                                    sessionStorage.clear();
                                    window.location.href='http://localhost/safehouse/index.php';
                                    // alert("Saiu");

                                }).catch(function(error) 
                                {
                                    alert("Erro: "+error);
                                });
                            }
                        </script>
                    </ul>
                </div>
            </div>    

      		<div id="main" style="height: 630px;">
      			<div id="banner" style="min-height: 465px;margin-top: -62px;">
		                   
      			</div>
                <p style="margin-top: -62px" id="dadosGrafico"></p>
     			<div id="welcome" style="">
        			<div class="title">
          				<h1><strong>Saiba tudo o que acontece na sua casa.</strong></h1>
        			</div>
        			<footer>
        				<nav align="center" class="navbar fixed-bottom navbar-light" style="background-color: #ffffff;">
          Safe House 2019, Todos os direitos reservados.
       					</nav>
        			</footer>
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
        