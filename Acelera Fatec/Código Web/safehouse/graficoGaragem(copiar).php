<?php
    try
    {
        $conecta = new PDO('mysql:host=localhost;port=3306;dbname=safehouse','root','');
        $conecta->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

        $consulta = $conecta->query("SELECT * FROM alarme");

        foreach($consulta as $linhas)
        {
            $senha = $linhas['senha'];
            $estadoatual = $linhas['estado'];
        }

        $consulta = $conecta->query("SELECT DISTINCT dataAcao,diaSemana FROM portao");

        $datas = array();
        $d = array();
        foreach($consulta as $linhas)
        {
            $d[] = $linhas['dataAcao'];
            $datas[] = utf8_encode($linhas['dataAcao']." - ".$linhas['diaSemana']);   
        }
        $Nvezes = array();
        for($x=0;$x<sizeof($datas);$x++)
        {
            $consulta = $conecta->query("SELECT COUNT(*) AS Nvezes FROM portao WHERE estado='Aberto' AND dataAcao='".$d[$x]."'");
            foreach($consulta as $linhas)
            {
                $Nvezes[]= $linhas['Nvezes'];
            }
        }
        
        $sd=sizeof($datas);
        date_default_timezone_set('America/Sao_Paulo');   
        $hora = date('H:i:s');
        $data = date('d/m/Y');
        $horaGrafico = $hora;
        $dataGrafico = $data;
        
        // utf8_encode()
        // echo json_encode($Nvezes[1]);
        
    }
    catch(PDOExcpetion $erro)
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
            google.charts.load('current', {'packages':['bar']});
            google.charts.setOnLoadCallback(drawChart);

            var datas = <?php echo json_encode($datas); ?>;
            var Nvezes = <?php echo json_encode($Nvezes); ?>;
            // alert(Nvezes);
            var horaGerada = <?php echo json_encode($horaGrafico); ?>;
            var dataGerada = <?php echo json_encode($dataGrafico); ?>;
            function drawChart() 
            {
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Data');
                data.addColumn('number', 'Número de vezes');    
                for(var i = 0; i< datas.length; i++ )
                {
                    data.addRows
                    ([
                        [ datas[i], parseInt(Nvezes[i]) ],
                    ]);
                }

                var options = 
                {
                    colors: ['blue'],
                    // chart: 
                    // {
                    //     title: 'Gráfico do portão',
                    //     subtitle: 'Status diário',
                    // }
                };

                var chart = new google.charts.Bar(document.getElementById('banner'));

                document.getElementById('dadosGrafico').innerHTML = "Gráfico gerado às "+horaGerada+" de "+dataGerada+".";
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
        <div id="header" style="height: 626px">
                <div  style="text-align: center;">
                    <img width="100" height="100" src="" alt="" style="border-radius: 50%;" id="img01">
                    <br><br>
                    <h1><a style="color: white;" id="nomeUsuario" ></a></h1>
                    <span style="color: gray;text-transform: uppercase;font-size: 15px">Safe House</span>
                </div>
                <div style="text-align: center;">
                    <h3><a style="color: yellow;" ><?php echo "Alarme: ".$estadoatual; ?></a></h3>
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

          <div id="main" style="height: 588px;">
            <h3 style="font-size: 17px"><b>Número diário de vezes em que o portão é aberto</b></h3>
            <div id="banner" style="min-height: 430px;margin-top: 50px;">
                       
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