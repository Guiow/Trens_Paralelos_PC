/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 16/05/2024
* Ultima alteracao.: 20/05/2024
* Nome.............: Trens Paralelos
* Funcao...........: Classe que realiza a exclusao mutua dos trens na parte compartilhada (trilho unico),
  e fornece em tempo real o status do algoritmo
*************************************************************** */
package model;

import javafx.application.Platform;
import javafx.scene.control.Label;

import util.*;

public class ExclusaoMutua
{
  private int tipoDeAlgoritmo;//define o algoritmo de exclusao mutua que sera utilizado
  private int[] variavelDeExclusaoMutua;//define o acesso ou vez dos trens nos algoritmos de exclusao mutua
  private boolean[] interesseTrilho1;//define o interesse de cada trem no trilho unico de cima apenas para solucao de person
  private boolean[] interesseTrilho2;//define o interesse de cada trem no trilho unico de baixo apenas para solucao de person
  
  private Label[] statusDoAlgoritmo;//labels que ira mostrar em tempo real o status do algoritmo para o usuario

  private final int STATUS_QUANTIDADE = 11;
  private final int TEMPO_DE_ESPERA = 200;
  
  /* ***************************************************************
  * Metodo: Construtor
  * Funcao: instancia a variavel de exclusao mutua e o vetor de status do algoritmo, definindo tambem suas posicoes
  * Parametros: resolucao = resolucao que sera usada para obter o valor de alinhamento dos trilhos
  * Retorno: nenhum
  *************************************************************** */
  public ExclusaoMutua(Resolucao resolucao)
  {
    variavelDeExclusaoMutua = new int[2];
    statusDoAlgoritmo = new Label[STATUS_QUANTIDADE];
    
    for (int i = 0; i < STATUS_QUANTIDADE; i++)
    {//cria os labels, os adicionam a uma classe css e os posiciona no layout X ideal
      statusDoAlgoritmo[i] = new Label();
      statusDoAlgoritmo[i].getStyleClass().add("statusDoAlgoritmo");
      statusDoAlgoritmo[i].setLayoutX(59);
    }//fim do for
    
    statusDoAlgoritmo[0].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 41);//posiciona o layout Y
    statusDoAlgoritmo[0].setId("nomeDoAlgoritmo");//o status de indice 0 representa o nome dos algoritmos
    
    statusDoAlgoritmo[1].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 121);//posiona os 4 proximos status em 
    statusDoAlgoritmo[2].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 151);//posicoes que serao usadas nos
    statusDoAlgoritmo[3].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 260);//tres algoritmos
    statusDoAlgoritmo[4].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 290);
    
    statusDoAlgoritmo[7].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 165);//posiciona os proximos status que
    statusDoAlgoritmo[5].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 183);//sao utilizados apenas no 
    statusDoAlgoritmo[8].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 197);//algoritmo de solucao de peterson
    statusDoAlgoritmo[9].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 303);
    statusDoAlgoritmo[6].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 322);
    statusDoAlgoritmo[10].setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 336);
  }//fim do construtor
  
  /* ***************************************************************
  * Metodo: reconfigurarAlgoritmo
  * Funcao: reinicia os valores das variaveis necessarias para o algoritmo de exclusao mutua e o status do algoritmo
  * Parametros: tipoDeAlgoritmo = define qual o algoritmo que o usuario escolheu para a exclusao mutua,
    localDeInicioTrem1 = valor que fornece a informacao se o trem 1 partiu de cima ou de baixo,
    localDeInicioTrem2 = valor que fornece a informacao se o trem 2 partiu de cima ou de baixo
  * Retorno: void
  *************************************************************** */
  public void reconfigurarAlgoritmo(int tipoDeAlgoritmo, int localDeInicioTrem1, int localDeInicioTrem2)
  {
    this.tipoDeAlgoritmo = tipoDeAlgoritmo;//salva o tipo de algoritmo escolhido
    variavelDeExclusaoMutua[0] = variavelDeExclusaoMutua[1] = 0;
    
    if (tipoDeAlgoritmo == 2)
    {//reinicia a vez dos trens de acordo com seus locais de partida com a variavel de exclusao mutua
      variavelDeExclusaoMutua[0] = localDeInicioTrem1 < 0 ? 1 : 2;
      variavelDeExclusaoMutua[1] = localDeInicioTrem2 < 0 ? 1 : 2;
    }//fim do if
    
    else if (tipoDeAlgoritmo == 3)
    {//reinicia o interesse dos trens em ambos os trilhos unicos
      interesseTrilho1 = new boolean[2];
      interesseTrilho2 = new boolean[2];
    }//fim do else if
    
    reconfigurarStatusDoAlgoritmo();//chama metodo que reconfigura status do algoritmo
  }//fim do metodo reconfigurarAlgoritmo
  
  
  /* ***************************************************************
  * Metodo: reconfigurarStatusDoAlgoritmo
  * Funcao: reconfigura os valores do status do algoritmo mostrado ao usuario
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void reconfigurarStatusDoAlgoritmo()
  {
    if (tipoDeAlgoritmo == 1)//verifica se o algoritmo escolhido e o de variavel de travamento
    {
      statusDoAlgoritmo[0].setText("Variável De\nTravamento");//muda o nome do algoritmo
      
      for (int i = 1; i < 5; i++)
      {
        if (i % 2 == 1)//status de indice impares sao reconfigurados dessa forma
          setStatusDoAlgoritmo(" Acesso", "acessoDeVariavelDeTravamento", i);//chama metodo que muda o status
     
        else//status de indice pares sao reconfigurados dessa forma
          setStatusDoAlgoritmo("PERMITIDO", "permitidoPassar", i);
      }//fim do for
      
      visibilidadeDeStatus(5, false);//oculta os labels de status de indice 5 para cima
    }//fim do if
    
    else if (tipoDeAlgoritmo == 2)//verifica se o algoritmo escolhido e o de explicita alternancia
    {
      statusDoAlgoritmo[0].setText("Explicita\nAlternancia");//muda o nome do algoritmo
      
      for (int i = 1; i < 5; i++)
      {
        if (i % 2 == 1)//status de indice impares sao reconfigurados dessa forma
          setStatusDoAlgoritmo("  Vez", "vezDeExplicitaAlternancia", i);//chama metodo que muda o status

        else//status de indice pares sao reconfigurados dessa forma
          trocaVezDoTremNoStatus(i / 2 - 1, i);//metodo que configura o status de explicita alternancia
      }//fim do for
      
      visibilidadeDeStatus(5, false);//oculta os labels de status de indice 5 para cima
    }//fim do else if
    
    else//se nao o algoritmo escolhido e o de solucao de peterson
    {
      statusDoAlgoritmo[0].setText("Solucão De\nPeterson");//muda o nome do algoritmo
      
      for (int i = 1; i < 5; i++)
      {//modifica os status de indice 1 a 4
        if (i % 2 == 1)//status de indice impares sao reconfigurados dessa forma
          setStatusDoAlgoritmo("      Vez", "vezDeSolucaoDePeterson", i);//chama metodo que muda o status

        else//status de indice pares sao reconfigurados dessa forma
          setStatusDoAlgoritmo("    Trem 1:", "textoTrem", i);
      }//fim do for
  
      for (int i = 5; i < STATUS_QUANTIDADE; i++)
      {
        statusDoAlgoritmo[i].setVisible(true);
    
        if (i < 7)//configura os status de indice 5 e 6
          setStatusDoAlgoritmo("    Trem 2:", "textoTrem", i);//chama metodo que muda o status
          
        else//configura os status de indice 7 a 10
          setStatusDoAlgoritmo(" Indiferente", "indiferenteSolucaoDePeterson", i);
      }//fim do for
    }//fim do else
  }//fim do metodo reconfigurarStatusDoAlgoritmo
  
  /* ***************************************************************
  * Metodo: setStatusDoAlgoritmo
  * Funcao: modifica o texto do status do algoritmo e o estilo de acordo com o indice do status
  * Parametros: statusNovoTexto = novo texto que ira substituir o texto antigo,
    cssId = o novo id css que ira substituir, indiceStatusTrilho = indice do status a ser substituido
  * Retorno: void
  *************************************************************** */
  private void setStatusDoAlgoritmo(String statusNovoTexto, String cssId, int indiceStatusTrilho)
  {
    Platform.runLater(() -> {
        statusDoAlgoritmo[indiceStatusTrilho].setText(statusNovoTexto);
        statusDoAlgoritmo[indiceStatusTrilho].setId(cssId);
    });
  }//fim do metodo setStatusDoAlgoritmo
  
  /* ***************************************************************
  * Metodo: visibilidadeDeStatus
  * Funcao: oculta ou deixa visivel os status de um indice qualquer ate o ultimo maior que ele
  * Parametros: 
  * Retorno: void
  *************************************************************** */
  private void visibilidadeDeStatus(int indiceStatus, boolean visibilidade)
  {
    for (int i = indiceStatus; i < STATUS_QUANTIDADE; i++)
      statusDoAlgoritmo[i].setVisible(visibilidade);
  }//fim do metodo visibilidadeDeStatus
  
  /* ***************************************************************
  * Metodo: executarAlgoritmoDeExclusaoMutua
  * Funcao: chama o algoritmo de exclusao mutua escolhido pelo usuario, que trava o trilho
  * Parametros: identificadorDoTrem = identifica qual o trem que esta chamando o metodo,
    qualTrilho = identifica qual o trilho que o trem esta passando, true = de cima, false = de baixo
  * Retorno: void
  *************************************************************** */
  public void executarAlgoritmoDeExclusaoMutua(int identificadorDoTrem, boolean qualTrilho)
    throws InterruptedException
  {
    switch (tipoDeAlgoritmo)//verifica qual algoritmo o usuario escolheu
    {
      case 1: 
        variavelDeTravamento(qualTrilho);//chama o algoritmo de variavel de travamento
        break;
      
      case 2:
        alternanciaExplicita(identificadorDoTrem, qualTrilho);//chama o algoritmo de alternanciaExplicita
        break;
        
      case 3:
        solucaoDePeterson(identificadorDoTrem, qualTrilho);//chama o algoritmo de solucaoDePeterson
        break;
    }//fim do swtich
  }//fim do metodo executarAlgoritmoDeExclusaoMutua
  
  /* ***************************************************************
  * Metodo: liberarTrilho
  * Funcao: chama o algoritmo de exclusao mutua escolhido pelo usuario, que libera o trilho
  * Parametros: identificadorDoTrem = identifica qual o trem que esta chamando o metodo,
    qualTrilho = identifica qual o trilho que o trem esta passando, true = de cima, false = de baixo
  * Retorno: void
  *************************************************************** */
  public void liberarTrilho(int identificadorDoTrem, boolean qualTrilho)
  {
    switch (tipoDeAlgoritmo)
    {
      case 1:
        setVariavelDeTravamento(qualTrilho);//libera o trilho com algoritmo de variavel de travamento 
        break;
      
      case 2:
        setVariavelDeAlternancia(3 - identificadorDoTrem,  qualTrilho);//libera o trilho com algoritmo de explicita alternancia 
        break;
      
      case 3:
        setInteresse(identificadorDoTrem - 1, qualTrilho);//libera o trilho com algoritmo de solucao de peterson
        break;
    }//fim do switch
  }//fim do metodo liberarTrilho
  
  /* ***************************************************************
  * Metodo: variavelDeTravamento
  * Funcao: executa o algoritmo de variavelDeTravamento quando o trem entra na zona critica
  * Parametros: qualTrilho = identifica qual o trilho que o trem esta passando, true = de cima, false = de baixo
  * Retorno: void
  *************************************************************** */
  private void variavelDeTravamento(boolean qualTrilho)
    throws InterruptedException
  {
    if (qualTrilho)
    {//trava o trilho de cima se tiver algum trem na zona critica
      while (variavelDeExclusaoMutua[0] != 0)
        Thread.sleep(TEMPO_DE_ESPERA);
        
      variavelDeExclusaoMutua[0] = 1;//trava o acesso
      setStatusDoAlgoritmo("PROIBIDO", "proibidoPassar", 2);//muda o status do algoritmo correspondente
    }//fim do if
      
    else
    {//trava o trilho de baixo se tiver algum trem na zona critica
      while (variavelDeExclusaoMutua[1] != 0)
        Thread.sleep(TEMPO_DE_ESPERA);
       
      variavelDeExclusaoMutua[1] = 1;//trava o acesso
      setStatusDoAlgoritmo("PROIBIDO", "proibidoPassar", 4);//muda o status do algoritmo correspondente
    }//fim do else
  }//fim do metodo variavelDeTravamento
  
  /* ***************************************************************
  * Metodo: setVariavelDeTravamento
  * Funcao: libera o acesso quando o trem sai da regiao critica quando o algoritmo de exclusao e variavel de travamento
  * Parametros: qualTrilho = identifica qual o trilho que o trem esta passando, true = de cima, false = de baixo
  * Retorno: void
  *************************************************************** */
  private void setVariavelDeTravamento(boolean qualTrilho)
  {
    if (qualTrilho)//verifica se o trem esta saindo do trilho unico de cima e libera o acesso
    {
      variavelDeExclusaoMutua[0] = 0;
      setStatusDoAlgoritmo("PERMITIDO", "permitidoPassar", 2);//muda o status do algoritmo correspondente
    }//fim do if
    
    else//se nao libera o acesso da regiao critica de baixo
    {
      variavelDeExclusaoMutua[1] = 0;
      setStatusDoAlgoritmo("PERMITIDO", "permitidoPassar", 4); //muda o status do algoritmo correspondente
    }//fim do else
  }//fim do metodo setVariavelDeTravamento
  
  /* ***************************************************************
  * Metodo: alternanciaExplicita
  * Funcao: executa o algoritmo de alternanciaExplicita quando o trem entra na zona Critica
  * Parametros: identificadorDoTrem = identifica qual o trem que esta chamando o metodo,
    qualTrilho = identifica qual o trilho que o trem esta passando, true = de cima, false = de baixo
  * Retorno: void
  *************************************************************** */
  private void alternanciaExplicita(int identificadorDoTrem, boolean qualTrilho)
    throws InterruptedException
  {
    if (qualTrilho)//verifica qual trilho o trem esta passando
      while (identificadorDoTrem != variavelDeExclusaoMutua[0])
        Thread.sleep(TEMPO_DE_ESPERA);
       
    else
      while (identificadorDoTrem != variavelDeExclusaoMutua[1])
        Thread.sleep(TEMPO_DE_ESPERA);
  }//fim do metodo alternanciaExplicita
  
  /* ***************************************************************
  * Metodo: setVariavelDeAlternancia
  * Funcao: libera o acesso quando o trem sai da regiao critica quando o algoritmo de exclusao e explicita alternancia
  * Parametros: identificadorDoTrem = identifica qual o trem que esta chamando o metodo,
    qualTrilho = identifica qual o trilho que o trem esta passando, true = de cima, false = de baixo
  * Retorno: void
  *************************************************************** */
  private void setVariavelDeAlternancia(int vezDoTrem, boolean qualTrilho)
  {
    if (qualTrilho)
    {//troca a vez para entrar no trem de cima para o trem oposto
      variavelDeExclusaoMutua[0] = vezDoTrem;
      trocaVezDoTremNoStatus(0, 2);//muda o status de vez para o outro trem
    }//fim do if
    else
    {//troca a vez para entrar no trem de baixo para o trem oposto
      variavelDeExclusaoMutua[1] = vezDoTrem;
      trocaVezDoTremNoStatus(1, 4);//muda o status de vez para o outro trem
    }//fim do else
  }//fim do metodo setVariavelDeAlternancia
   
  /* ***************************************************************
  * Metodo: trocaVezDoTremNoStatus
  * Funcao: troca a vez do trem quando um passa da regiao critica para ser a vez do outro trem 
  * Parametros: identificadorDoTrem = identifica qual o trem que esta chamando o metodo,
    qualTrilho = identifica qual o trilho que o trem esta passando, true = de cima, false = de baixo
  * Retorno: void
  *************************************************************** */
  private void trocaVezDoTremNoStatus(int indiceDaVariavelDeExclusao, int indiceDoStatus)
  {
    if (variavelDeExclusaoMutua[indiceDaVariavelDeExclusao] == 1)//verifica de quem e a vez
      setStatusDoAlgoritmo("Trem 1", "vezTremVermelho", indiceDoStatus);
    else
      setStatusDoAlgoritmo("Trem 2", "vezTremRoxo", indiceDoStatus);
  }//fim do metodo trocaVezDoTremNoStatus
  
  /* ***************************************************************
  * Metodo: solucaoDePeterson
  * Funcao: executa o algoritmo de solucao de peterson quando o trem entra na zona critica
  * Parametros: identificadorDoTrem = identifica qual o trem que esta chamando o metodo,
    qualTrilho = identifica qual o trilho que o trem esta passando, true = de cima, false = de baixo
  * Retorno: void
  *************************************************************** */
  private void solucaoDePeterson(int identificadorDoTrem, boolean qualTrilho)
    throws InterruptedException
  { 
    int outroTrem = 2 - identificadorDoTrem;//indentifica qual e o trem concorrente
    setStatusDaSolucaoDePeterson(--identificadorDoTrem, qualTrilho, true);//muda o status do interesse do trem
    
    if (qualTrilho)
    {//executa o algoritmo de solucao de peterson no trilho de cima
      interesseTrilho1[identificadorDoTrem] = true;//muda o interesse do trem para true
      variavelDeExclusaoMutua[0] = identificadorDoTrem;//verifica a vez do trem
      setStatusVezSolucaoDePeterson(identificadorDoTrem, 1);//muda o status de vez do trem

      while (variavelDeExclusaoMutua[0] == identificadorDoTrem && interesseTrilho1[outroTrem])
        Thread.sleep(TEMPO_DE_ESPERA);
    }//fim do if
    else
    {//executa o algoritmo de solucao de peterson no trilho de baixo
      interesseTrilho2[identificadorDoTrem] = true;//muda o interesse do trem para true
      variavelDeExclusaoMutua[1] = identificadorDoTrem;//verifica a vez do trem
      setStatusVezSolucaoDePeterson(identificadorDoTrem, 3);//muda o status de vez do trem
      
      while (variavelDeExclusaoMutua[1] == identificadorDoTrem && interesseTrilho2[outroTrem])
        Thread.sleep(TEMPO_DE_ESPERA);
    }//fim do else
  }//fim do metodo solucaoDePeterson
  
  /* ***************************************************************
  * Metodo: setInteresse
  * Funcao: muda o interesse do trem para false quando ele sai da regiao critica correspondente ao trilho de cima ou de baixo
  * Parametros: identificadorDoTrem = identifica qual o trem que esta chamando o metodo,
    qualTrilho = identifica qual o trilho que o trem esta passando, true = de cima, false = de baixo
  * Retorno: void
  *************************************************************** */
  private void setInteresse(int identificadorDoTrem, boolean qualTrilho)
  {
    if (qualTrilho)//se o trem passou pelo trem de cima muda o interesse para false
      interesseTrilho1[identificadorDoTrem] = false;
      
    else//se o trem passou pelo trem de baixo muda o interesse para false
      interesseTrilho2[identificadorDoTrem] = false;
    
    setStatusDaSolucaoDePeterson(identificadorDoTrem, qualTrilho, false);//muda o status de interesse
  }//fim do metodo setInteresse
  
  /* ***************************************************************
  * Metodo: setStatusVezSolucaoDePeterson
  * Funcao: muda o status do algoritmo de acordo com vez dele para acessar o trilho
  * Parametros: identificadorDoTrem = identifica qual o trem que esta chamando o metodo,
    indiceDoStatus = indice que corresponde ao status do algoritmo que armazena o label de vez
  * Retorno: void
  *************************************************************** */
  private void setStatusVezSolucaoDePeterson(int identificadorDoTrem, int indiceDoStatus)
  {
    Platform.runLater(() -> statusDoAlgoritmo[indiceDoStatus].setText(String.format("Vez: Trem %d", identificadorDoTrem + 1)));
  }//fim do metodo setStatusVezSolucaoDePeterson
  
  /* ***************************************************************
  * Metodo: setStatusDaSolucaoDePeterson
  * Funcao: muda o status de interesse dos trens ao passar pelo trilho unico
  * Parametros: identificadorDoTrem = identifica qual o trem que esta chamando o metodo,
    indiceDoStatus = indice que corresponde ao status do algoritmo que armazena o label de vez,
    interesse = ve se o trem esta interessado ou indiferente em entrar na regiao critica
  * Retorno: void
  *************************************************************** */
  private void setStatusDaSolucaoDePeterson(int identificadorDoTrem, boolean qualTrilho, boolean interesse)
  {
    int indiceDoStatus;
    //verifica qual e o indice do status de interesse a ser alterado
    if (identificadorDoTrem == 0)
    {
      if (qualTrilho)
        indiceDoStatus = 7;
        
      else
        indiceDoStatus = 9;
    }//fim do if
    
    else
    {
      if (qualTrilho)
        indiceDoStatus = 8;
        
      else
        indiceDoStatus = 10;
    }//fim do else
    
    if (interesse)//altera o status para interessado se for true ou indiferente se for false
      setStatusDoAlgoritmo(" Interessado", "interessadoSolucaoDePeterson", indiceDoStatus);
    else
      setStatusDoAlgoritmo(" Indiferente", "indiferenteSolucaoDePeterson", indiceDoStatus);
  }//fim do metodo setStatusDaSolucaoDePeterson
  
  /* ***************************************************************
  * Metodo: getStatusDoAlgoritmo
  * Funcao: retorna a referencia do vetor que armazena o status do algoritmo para ser adicionado ao pane
  * Parametros: nenhum
  * Retorno: Label[]
  *************************************************************** */
  public Label[] getStatusDoAlgoritmo()
  {
    return statusDoAlgoritmo;
  }//fim do metodo getStatusDoAlgoritmo
}//fim da classe ExclusaoMutua
