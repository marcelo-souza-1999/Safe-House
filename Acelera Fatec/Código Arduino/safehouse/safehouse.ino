#include <LiquidCrystal.h> //lcd
#include <Keypad.h> //keypad
#include <Ethernet.h> //shield
#include <SPI.h> //shield
#include <Servo.h>
#include<string.h> //trabalhar com strings
#include "RTClib.h"

#define portaHTTP 80 //porta de conexão
#define SERVO1 8 // Porta Digital 6 PWM
#define SERVO2 9

RTC_DS3231 rtc;

char diasDaSemana[7][12] = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
char data[11];
char hora[9];
char diaSemana[8];

//byte mac[] = { 0x00, 0xAA, 0xBB, 0xCC, 0xDE, 0x02};  //Conexão com o Servidor Apache Local
byte servidor[] = {192,168,0,109}; //IP do meu computador
//IP do Auditório da Fatec: 192,168,0,124

EthernetClient clienteArduino;

//////SENSORES///////
int pinoSensor = 3;
int janelaSensor = 5;

///////LED'S//////////
const int verde = 11;
const int vermelho = 12;

///RELE - FITA LED///
int porta_rele1 = 6;

/////SOMZIM/////
int buzzer = 4;

//////VARIÁVEIS//////////
int janela = 0;
int presenca = 0;
int alarme = 0;
int erro = 0;
Servo s1; // Variável Servo
Servo s2;
int pos1 = 96, pos2 = 3; // Posição Servo
int min1 = 0, min2 = pos2;
int max1 = pos1, max2 = 83;
char controle;
char inputa;
int oldalarme = 5, oldjanela = 5, oldpresenca = 5, errocont = -1 ;
char oldcontrole = '5';
/////VAR KEYPAD ///////
char auxiliar = ' ';
char senhaDefinida[] = "123456";
char senhaInserida[7];
const byte rows = 4; //number of the keypad's rows and columns
const byte cols = 4;
char keyMap [rows] [cols] = { //define the cymbols on the buttons of the keypad

  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};

////////////////KEYPAD & LCD/////////////////
byte rowPins [rows] = {31, 33, 35, 37};
byte colPins [cols] = {39, 41, 43, 45};

Keypad myKeypad = Keypad( makeKeymap(keyMap), rowPins, colPins, rows, cols);

LiquidCrystal lcd (30, 32, 34, 36, 38, 40); // pins of the LCD. (RS, E, D4, D5, D6, D7)

void setup()
{
    Serial.begin(9600);

    //Conectando Ethernet Shield na rede wifi e atribuindo um endereço ip ao mesmo
    //Ethernet.begin(mac);
    /*while(Ethernet.begin(mac) == 0)
    {
        Serial.println("Falha ao conectar-se com a rede...");
        Ethernet.begin(mac);
    }  
    Serial.print("Conectado a rede, no ip: ");
    Serial.println(Ethernet.localIP());*/

////////////////////////////////////TIME capture/////////////////////////////
   
    if (! rtc.begin()) 
    {
        Serial.println("Couldn't find RTC");
        while (1);
    }
    if (rtc.lostPower()) 
    {
        Serial.println("RTC lost power, lets set the time!");
        //ano,mes,dia,hora,minuto,segundo
        //rtc.adjust(DateTime(2019, 6, 8, 10, 53, 40));
    }

    s1.attach(SERVO1);
    s2.attach(SERVO2);
    s1.write(pos1); // Inicia motor
    s2.write(pos2);
    pinMode(janelaSensor, INPUT);
    pinMode(pinoSensor, INPUT);
    pinMode(buzzer, OUTPUT);
    pinMode(verde, OUTPUT);
    pinMode(vermelho, OUTPUT);
    pinMode(porta_rele1, OUTPUT);
    lcd.begin(16, 2);
}

void loop()
{
  SalvarTudo();
  char digito = myKeypad.getKey();  //// RECEBE O VALOR QUE VEM DO KEYPAD 
  
  if (digito == 'A') /////// OPÇÃO PARA LIGAR O ALARME
  {
    auxiliar = digito;
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("SAFE HOUSE");
    lcd.setCursor(0, 1);
    lcd.print("Inserindo senha");
    int x = 1;
    while (x <= 6) ////// INSERINDO SENHA
    {
      char digito = myKeypad.getKey();  ///// SALVANDO CARACTERE POR CARACTERE EM UMA VARIÁVEL

      if (digito == '1' || digito == '2' || digito == '3' || digito == '4' || digito == '5' || digito == '6' || digito == '7' || digito == '8' || digito == '9' || digito == '0')
      { ///// CONDICIONANDO A ENTRADA/////
        senhaInserida[x - 1] = digito; //// SALVANDO EM UM VETOR CADA DIGITO
        if (x == 1)
        {
          lcd.clear();
        }
        lcd.setCursor(0, 0);
        lcd.print("Senha: ");
        lcd.setCursor(x - 1, 1);
        lcd.print("*");
        x++;
      }
    }

    if (strcmp(senhaInserida, senhaDefinida) == 0) //// COMPARANDO SE A SENHA DIGITADA ESTA CORRETA
    {
      alarme = 1;
    }
    else
    {
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("SAFE HOUSE");
      lcd.setCursor(0, 1);
      lcd.print("Senha incorreta");
      erro ++;     ////////////////pode ser usado em grafico
      delay(2000);
    }
  }
  else if (digito == 'D') ///// OPÇÃO PARA DESLIGAR O ALARME
  {
    auxiliar = digito;
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("SAFE HOUSE");
    lcd.setCursor(0, 1);
    lcd.print("Inserindo senha");

    int x = 1;
    while (x <= 6)
    {
      char digito = myKeypad.getKey();

      if (digito == '1' || digito == '2' || digito == '3' || digito == '4' || digito == '5' || digito == '6' || digito == '7' || digito == '8' || digito == '9' || digito == '0')
      {
        senhaInserida[x - 1] = digito;
        if (x == 1)
        {
          lcd.clear();
        }
        lcd.setCursor(0, 0);
        lcd.print("Senha: ");
        lcd.setCursor(x - 1, 1);
        lcd.print("*");
        x++;
      }
    }
    if (strcmp(senhaInserida, senhaDefinida) == 0)
    {
      alarme = 0;
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("SAFE HOUSE");
      lcd.setCursor(0, 1);
      lcd.print("Alarme desativado");
      delay(2000);
    }
    else
    {
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("SAFE HOUSE");
      lcd.setCursor(0, 1);
      lcd.print("Senha incorreta");
      delay(2000);
    }
  }
  else
  {
    if (digito != auxiliar) ////// OPÇÃO PARA APRESENTAR STATUS DO ALARME
    {
      auxiliar = digito;
      if (alarme == 0)
      {
        ExibirDesativacao();
      }
      else
      {
        ExibirAtivacao();
      }
    }
  }
  inputa = Serial.read();
  if (inputa == '0' || inputa == '1')
  {
    if (inputa == '0')
    {
      alarme = 0;
    }
    else if (inputa == '1')
    {
      alarme = 1;
    }
  }
    janela = digitalRead(janelaSensor);  ///ATIVA O SENSOR DO PORTA JANELA
    presenca = digitalRead(pinoSensor);  ///ATIVA O SENSOR DE PRESENÇA
//    Serial.println(janela);
  if (alarme == 1)
  {
    ligaAlarme();
    if(alarme != oldalarme)
    {
      oldalarme = alarme;
      ExibirAtivacao();
      if(clienteArduino.connect(servidor, portaHTTP))
      {
            clienteArduino.print("GET /webService-Fatec/AestadoAlarme.php");
            clienteArduino.print("?dados_Alarme=");
            clienteArduino.print("Ativado.");
            clienteArduino.print(hora);
            clienteArduino.print(".");
            clienteArduino.print(data);
            clienteArduino.print(".");
            clienteArduino.print(diaSemana);
          clienteArduino.println(" HTTP/1.0");
  
          clienteArduino.println("Host: 192.168.0.106");
          clienteArduino.println("Connection: close");
          clienteArduino.println();
          clienteArduino.stop();
      }
      
    }
    janela = digitalRead(janelaSensor);  ///ATIVA O SENSOR DO PORTA JANELA
    presenca = digitalRead(pinoSensor);  ///ATIVA O SENSOR DE PRESENÇA
//    Serial.print(janela);
//    Serial.print("   ");
//    Serial.println(presenca);
    if (janela == 1 || presenca == 1)
    {
      rele_on();
      buzzerON();
    }
    if (digito == 'C')
    {
      rele_off();
      buzzerOFF();
    }
  }
  else
  {
    desligaAlarme();
    if(alarme != oldalarme)
    {
       oldalarme = alarme;
       ExibirDesativacao();
       if(clienteArduino.connect(servidor, portaHTTP))
      {
            clienteArduino.print("GET /webService-Fatec/AestadoAlarme.php");
            clienteArduino.print("?dados_Alarme=");
            clienteArduino.print("Desativado.");
            clienteArduino.print(hora);
            clienteArduino.print(".");
            clienteArduino.print(data);
            clienteArduino.print(".");
            clienteArduino.print(diaSemana);
          
          clienteArduino.println(" HTTP/1.0");
  
          clienteArduino.println("Host: 192.168.0.106");
          clienteArduino.println("Connection: close");
          clienteArduino.println();
          clienteArduino.stop();
      }
    }
    buzzerOFF();
    if (inputa == '2' || inputa == '3')
    {
      controle = inputa;
      if (controle == '2' && pos1 != min1 && pos2 != max2)
      {
        abrirPortao();
      }
      else if (controle == '3' && pos1 != max1 && pos2 != min2)
      {
        fecharPortao();
      }
    }
  }
  if (digito == 'B')
  {
    if (digitalRead(porta_rele1) == 1)
    {
      rele_off();
    }
    else
    {
      rele_on();
    }
  }

  //////////////////////////////////////////TESTE SHIELD///////////////////////////////////////////////////

//   if(alarme != oldalarme)      ///////// Enviar dados do alarme /////////////
//   {
//      Serial.println("Ok");
//      oldalarme = alarme;
//      if(clienteArduino.connect(servidor, portaHTTP))
//      {
//          if(oldalarme == 1)
//          {
//            
//            clienteArduino.print("GET /webService-Fatec/AestadoAlarme.php");
//            clienteArduino.print("?dados_Alarme=");
//            clienteArduino.print("Ativado.");
//            clienteArduino.print(hora);
//            clienteArduino.print(".");
//            clienteArduino.print(data);
//            clienteArduino.print(".");
//            clienteArduino.print(diaSemana);
//          }
//          if(oldalarme == 0)
//          {
//            clienteArduino.print("GET /webService-Fatec/AestadoAlarme.php");
//            clienteArduino.print("?dados_Alarme=");
//            clienteArduino.print("Desativado.");
//            clienteArduino.print(hora);
//            clienteArduino.print(".");
//            clienteArduino.print(data);
//            clienteArduino.print(".");
//            clienteArduino.print(diaSemana);
//          }
//          clienteArduino.println(" HTTP/1.0");
//  
//          clienteArduino.println("Host: 192.168.0.106");
//          clienteArduino.println("Connection: close");
//          clienteArduino.println();
//          clienteArduino.stop();
//      }
//   }
   if(controle != oldcontrole)     ///////// Enviar dados do portão /////////////
   {
      oldcontrole = controle;
      if(clienteArduino.connect(servidor, portaHTTP))
      {
          //clienteArduino.println("POST /arduinoSQL/enviar.php HTTP/1.0");
              
          if(oldcontrole == '2')
          {            
            clienteArduino.print("GET /webService-Fatec/AestadoPortao.php?dados_Portao=Aberto.");
            clienteArduino.print(hora);
            clienteArduino.print(".");
            clienteArduino.print(data);
            clienteArduino.print(".");
            clienteArduino.print(diaSemana);
          }
          if(oldcontrole == '3')
          {
              clienteArduino.print("GET /webService-Fatec/AestadoPortao.php?dados_Portao=Fechado.");
              clienteArduino.print(hora);
              clienteArduino.print(".");
              clienteArduino.print(data);
              clienteArduino.print(".");
              clienteArduino.print(diaSemana);            
          }
          clienteArduino.println(" HTTP/1.0");
  
          clienteArduino.println("Host: 192.168.0.106");
          clienteArduino.println("Connection: close");
          clienteArduino.println();
          clienteArduino.stop();
      }
   }
   if(janela != oldjanela || presenca != oldpresenca)     ///////// Enviar dados do sensor /////////////
   {
      oldjanela = janela;
      oldpresenca = presenca;
      if(clienteArduino.connect(servidor, portaHTTP))
      {
    //clienteArduino.println("POST /arduinoSQL/enviar.php HTTP/1.0");
          
          
          if(oldjanela == 1 || oldpresenca == 1)
          {
              clienteArduino.print("GET /webService-Fatec/Ajanela.php");
              clienteArduino.print("?dados_Janela=");
              clienteArduino.print("Alerta.");
              clienteArduino.print(hora);
              clienteArduino.print(".");
              clienteArduino.print(data);
              clienteArduino.print(".");
              clienteArduino.print(diaSemana);
          }
          if(oldjanela == 0 && oldpresenca == 0)
          {
            clienteArduino.print("GET /webService-Fatec/Ajanela.php");
            clienteArduino.print("?dados_Janela=");
            clienteArduino.print("Seguro.");            
            clienteArduino.print(hora);
            clienteArduino.print(".");
            clienteArduino.print(data);
            clienteArduino.print(".");
            clienteArduino.print(diaSemana);
          }
          clienteArduino.println(" HTTP/1.0");  
          clienteArduino.println("Host: 192.168.0.106");
          clienteArduino.println("Connection: close");
          clienteArduino.println();
          clienteArduino.stop();
    }
   }
   if(erro != errocont)     ///////// Enviar dados do portão /////////////
   {
      errocont = erro;
      if(clienteArduino.connect(servidor, portaHTTP))
      {
          //clienteArduino.println("POST /arduinoSQL/enviar.php HTTP/1.0");
             
          clienteArduino.print("GET /webService-Fatec/Aerrocontador.php?dados_erro=");
          clienteArduino.print(erro);
          clienteArduino.print(".");
          clienteArduino.print(hora);
          clienteArduino.print(".");
          clienteArduino.print(data);
          clienteArduino.print(".");
          clienteArduino.print(diaSemana);            
          
          clienteArduino.println(" HTTP/1.0");
          clienteArduino.println("Host: 192.168.0.106");
          clienteArduino.println("Connection: close");
          clienteArduino.println();
          clienteArduino.stop();
      }
   }   
   else
   {
      //Serial.println("Falha na conexão com o servidor");
      clienteArduino.stop();
   }
///////////////////////////////////////TIME  capture //////////////////////////  
    
    
//    SalvarHora();
//    SalvarDiaSemana();
}

/////////////////////////////////////////////////////////////////////////////////FUNÇÕES personalizadas///////////////////////////////////////////////////////////////////////////////////////////////////////
void rele_off() {
  digitalWrite(porta_rele1, LOW); //Desliga rele 1
}

void rele_on() {
  digitalWrite(porta_rele1, HIGH);  //Liga rele 1
}

void ligaAlarme() {  //Função que ativa o alarme, o LED Verde e fita de led acende
  verdeFuncao();
}

void desligaAlarme() { //Função que desativa o alarme, o LED Vermelho acende e fita de led apaga
  vermelhoFuncao();
}

void verdeFuncao() {
  digitalWrite(verde, HIGH);
  digitalWrite(vermelho, LOW);
}

void vermelhoFuncao() {
  digitalWrite(verde, LOW);
  digitalWrite(vermelho, HIGH);
}

void buzzerOFF()
{
  digitalWrite(buzzer, LOW);
}

void buzzerON()
{
  digitalWrite(buzzer, HIGH);
  //  float seno;
  //  int frequencia;
  //    for(int f=0; f<180; f++){
  //    seno=(sin(160*3.1416/180)); //converte graus para radiando e depois obtém o janelaor do seno
  //    frequencia = 2000+(int(seno*1000)); //gera uma frequência a partir do janelaor do seno
  //    digitalWrite(buzzer,frequencia);
  //    delay(2);
  //    }
}
void abrirPortao()
{
  pos1 = max1;
  pos2 = min2;
  while (pos1 >= min1 && pos2 <= max2)
  {
    s1.write(pos1);
    //Serial.println(pos1);
    pos1 -= 6;
    s2.write(pos2);
    //Serial.println(pos2);
    pos2 += 5;
    delay(200);
  }
}
void fecharPortao()
{
  pos1 = min1;
  pos2 = max2;
  while (pos1 <= max1 && pos2 >= min2)
  {
    s1.write(pos1);
    //Serial.println(pos1);
    pos1 += 6;
    s2.write(pos2);
    //Serial.println(pos2);
    pos2 -= 5;
    delay(200);
  }
}
void ExibirAtivacao()
{
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Safe House");
  lcd.setCursor(0, 1);
  lcd.print("Ativado");
}

void ExibirDesativacao()
{
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Safe House");
  lcd.setCursor(0, 1);
  lcd.print("Desativado");
}
/////////////////////////////////TIME capture function /////////////////////
void SalvarTudo()
{
    int d,m,a;
    char d1[3], m1[3];
    char d2[3]={'0'},m2[3]={'0'};
    char d3[3], m3[3],a3[5];
    DateTime now = rtc.now(); 
    d=now.day();
    m=now.month();
    a=now.year();

    if(d<10)
    {
        sprintf(d1,"%i",d); //conversão para string
        strcat(d2,d1); //concatenando na variável d2 
        strcpy(d3,d2);
    }
    else
    {
        sprintf(d3,"%i",d);
    }

    if(m<10)
    {
        sprintf(m1,"%i",m); //conversão para string
        strcat(m2,m1); //concatenando na variável m2 
        strcpy(m3,m2);
    }
    else
    {
        sprintf(m3,"%i",m);
    }
    sprintf(a3,"%i",a);
//    char data[11];

    strcpy(data,d3);
    strcat(data,"/");
    strcat(data,m3);
    strcat(data,"/");
    strcat(data,a3);

    int h,mi,s;
    char h1[3], mi1[3],s1[3];
    char h2[3]={'0'}, mi2[3]={'0'}, s2[3]={'0'};
    char h3[3], mi3[3], s3[3];
    
    h=now.hour();
    mi=now.minute();
    s=now.second();

    if(h<10)
    {
        sprintf(h1,"%i",h); //conversão para string
        strcat(h2,h1); //concatenando na variável h2 
        strcpy(h3,h2);
    }
    else
    {
        sprintf(h3,"%i",h);
    }

    if(mi<10)
    {
        sprintf(mi1,"%i",mi); //conversão para string
        strcat(mi2,mi1); //concatenando na variável mi2 
        strcpy(mi3,mi2);
    }
    else
    {
        sprintf(mi3,"%i",mi);
    }

    if(s<10)
    {
        sprintf(s1,"%i",s); //conversão para string
        strcat(s2,s1); //concatenando na variável s2 
        strcpy(s3,s2);
    }
    else
    {
        sprintf(s3,"%i",s);
    }
    
    strcpy(hora,h3);
    strcat(hora,":");
    strcat(hora,mi3);
    strcat(hora,":");
    strcat(hora,s3);

      
    strcpy(diaSemana,diasDaSemana[now.dayOfTheWeek()]);
//    Serial.println(diaSemana);
//    Serial.println(hora);
//    Serial.println(data);
}
