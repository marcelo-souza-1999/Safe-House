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
                    'width':'100%',
                    'height':'100%'
                };
                var chart = new google.visualization.PieChart(document.getElementById('banner'));
                document.getElementById('dadosGrafico').innerHTML = "Gráfico gerado às "+horaGerada+" de "+dataGerada+".";
                // var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
                chart.draw(data, options);
            }
        </script>
    </head>
    
    <body style="background: white;">
        
        	<div style="background: white; width: 100%; height: 72%">
      			<div id="banner" style="margin-top:20px">
		                   
                  </div>
                  <center>
                <p style="margin-top: -62px" id="dadosGrafico"></p>
                </center>
     			<!-- <div id="welcome" style="border: 2px solid">
        			<div class="title">
          				<h1><strong>Saiba tudo o que acontece na sua casa.</strong></h1>
        			</div>
        			<footer>
        				
        			</footer>
      			</div> -->
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
        