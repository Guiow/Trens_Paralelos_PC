/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 22/03/2024
* Ultima alteracao.: 24/05/2024
* Nome.............: Trens Paralelos
* Funcao...........: Criar trens que irao ter a funcao principal do aplicativo, que e se mover pelo
  trilho em paralelo com outros trens.
*************************************************************** */
package model;

import util.*;

import javafx.application.Platform;//contem um metodo usado para fazer alteracoes com threads na GUI com segurança
import javafx.scene.image.ImageView;

public class Trem extends Thread
{
  private final int VALOR_DE_INCREMENTO = 2;//variavel que representa a quantidade de pixels movida a cada iteracao
  private int velocidade; //variavel que representa velocidade, quanto menor o valor maior a velocidade
  private int identificadorDoTrem;

  private Resolucao resolucao;//tipo de resolucao para o trem se movimentar de acordo
  private ImageView imagemDoTrem;//imagem do trem
  private LocalDeInicio local;//representa o local de partida do trem
  private ExclusaoMutua recursoCompartilhado;//objeto que e compartilhada pelos trens
  
  private boolean pararTrem;//para fazer verificacoes se o trem esta parado
  
  /* ***************************************************************
  * Metodo: Construtor
  * Funcao: chamar o construtor da superclasse para carregar a imagem e armazena os valores das 
    variaveis velocidade e resolucao
  * Parametros: imagemURL = URL da imagem, velocidade = representacao da velocidade,
    resolucao = objeto resolucao com valores que promovem alinhamento de trem e trilho
  * Retorno: nenhum
  *************************************************************** */
  public Trem(String imageURL, Resolucao resolucao, ExclusaoMutua recursoCompartilhado, int identificadorDoTrem)
  {
    imagemDoTrem = new ImageView(String.format("%s%s", resolucao.getImgDiretorio(), imageURL));//cria a imagem do trem
    this.resolucao = resolucao;
    this.recursoCompartilhado = recursoCompartilhado;
    this.identificadorDoTrem = identificadorDoTrem;
    velocidade = 15;//velocidade inicial
  }//fim do construtor
  
  /* ***************************************************************
  * Metodo: run
  * Funcao: Chamado quando a thread e iniciada tem a funcao de executar o metodo de movimento para o trem
    comecar a se movimentar
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  @Override
  public void run()
  {
    try
    {
      movimento();//chama o metodo que movimenta os trens
    }//fim do try 
    catch (InterruptedException ex)
    {
      System.out.printf("Resetando posicao Trem %d%n", identificadorDoTrem);
    }//fim do catch
  }//fim do metodo run
  
  /* ***************************************************************
  * Metodo: movimento
  * Funcao: define a posicao inicial do trem e escolhe o metodo movimento ideal para o trem, de acordo com seu local de partida
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void movimento()
    throws InterruptedException
  {
    while(true)//while infinito que termina quando o programa fecha ou uma excessao e lancada
    {
      Platform.runLater(() -> {// define a posicao do layout X e Y inicial e a sentido de rotacao inicial
        imagemDoTrem.setLayoutX(local.getLayoutXInicial());
        imagemDoTrem.setLayoutY(local.getLayoutYInicial());
        imagemDoTrem.setRotate(local.getRotacaoInicial());//0 sentido apontado para baixo 180 para cima
      });//fim do runLater
      
      sleep(300);//aguarda um pequeno tempo para o trem nao voltar instantaneamente apos sair da tela
      
      switch (local)//chama metodo correspondente ao movimento do trem de acordo com seu local inicial
      {
        case ESQUERDA_CIMA_RA:
        case ESQUERDA_CIMA_RM:
          movimentoDeCima(true);//parametro que indica a rotacao do trem, true = direita, false = esquerda
          break;
        case DIREITA_CIMA_RA:
        case DIREITA_CIMA_RM:
          movimentoDeCima(false);
          break;
        case ESQUERDA_BAIXO_RA:
        case ESQUERDA_BAIXO_RM:
          movimentoDeBaixo(true);//parametro que indica a rotacao do trem, true = direita, false = esquerda
          break;
        case DIREITA_BAIXO_RA:
        case DIREITA_BAIXO_RM:
          movimentoDeBaixo(false);
          break;
      }//fim do switch
    }//fim do while
  }//fim do metodo movimento
  
  /* ***************************************************************
  * Metodo: movimentoDeCima
  * Funcao: Gerancia o movimento dos trens que vem de cima para baixo
  * Parametros: rotacao = Define para que lado o trem ira se movimentar. Se for true ele vai
    para direita. Se for false ele vai para esquerda
  * Retorno: void
  *************************************************************** */
  private void movimentoDeCima(boolean rotacao)
    throws InterruptedException
  {
    int count = 0;//conta de acordo com as curvas feitas pelo trem, quando for 5 quer dizer que o trem realizou todas as curvas
    boolean qualTrilho = true;//indica em qual trilho unico o trem esta passando, true = de cima, false = de baixo
    boolean possoLiberarTrilho = false;//libera a chamada do metodo que destrava o trilho caso seja true
    
    while(true)
    {
      incrementaLayoutY(VALOR_DE_INCREMENTO);//incrementa o layout Y para mover o trem para baixo

      //verifica se esta na posicao Y ideal para fazer proxima curva, se sim realiza a curva
      if (imagemDoTrem.getLayoutY() > resolucao.getPontoDeAlinhamentoDeCima(count))
      { 
        if (++count == 5)//para o loop infinito quando o count e = 5
          break;  
        
        if (count % 2 == 1)//quando o count e impar quer dizer que a proxima curva esta entrando na regiao critica
        {
          //chama metodo que trava o acesso ao trilho que o trem esta passando
          recursoCompartilhado.executarAlgoritmoDeExclusaoMutua(identificadorDoTrem, qualTrilho);
          qualTrilho = !qualTrilho;//quando passar pelo trilho unico de cima inverte-se para indicar que o proximo e o de baixo
        }//fim do if
        else
          possoLiberarTrilho = true;//quando o count e par quer dizer que o trem ja passou da regiao critica e pode libera-la
             
        if (rotacao)//faz o trem alternar entre movimento de rotacao para direita e para esquerda
          movimentoDeRotacao(true, -3, 1, 1.3, qualTrilho, possoLiberarTrilho);
          
        else
          movimentoDeRotacao(false, 3, -1, 1.3, qualTrilho, possoLiberarTrilho);
          
        rotacao = !rotacao;//altera o proximo lado de rotacao
        possoLiberarTrilho = false;
        Platform.runLater(() -> imagemDoTrem.setRotate(0));//Garante que a rotacao apos a curva sera de 0 graus
      }//fim do if
    }//fim do while
  }//fim do metodo movimentoDeCima
   
  /* ***************************************************************
  * Metodo: movimentoDeBaixo
  * Funcao: Gerancia o movimento dos trens que vem de baixo para cima
  * Parametros: rotacao = Define para que lado o trem ira se movimentar primeiro. Se for true ele vai
    para direita. Se for false ele vai para esquerda
  * Retorno: void
  *************************************************************** */
  public void movimentoDeBaixo(boolean rotacao)
    throws InterruptedException
  {
    int count = 4;//conta de acordo com as curvas feitas pelo trem, quando for -1 quer dizer que o trem realizou todas as curvas
    boolean qualTrilho = false;//indica em qual trilho unico o trem esta passando, true = de cima, false = de baixo
    boolean possoLiberarTrilho = false;//libera a chamada do metodo que destrava o trilho caso seja true
    
    while(true)
    {
      incrementaLayoutY(-VALOR_DE_INCREMENTO);//decrementa o layout Y para mover o trem para cima

      if (imagemDoTrem.getLayoutY() < resolucao.getPontoDeAlinhamentoDeBaixo(count))
      {
        if (--count == -1)//para o loop infinito quando o count e = -1
          break;
           
        if (count % 2 == 1)//quando o count e impar quer dizer que a proxima curva esta entrando na regiao critica
        {
          //chama metodo que trava o acesso ao trilho que o trem esta passando
          recursoCompartilhado.executarAlgoritmoDeExclusaoMutua(identificadorDoTrem, qualTrilho);
          qualTrilho = !qualTrilho;//quando passar pelo trilho unico de baixo inverte-se para indicar que o proximo e o de cima
        }
        else
          possoLiberarTrilho = true;//quando o count e par quer dizer que o trem ja passou da regiao critica e pode libera-la
          
        if (rotacao)//faz o trem alternar entre movimento de rotacao direita e esquerda
          movimentoDeRotacao(true, 3, 1, -1.3, qualTrilho, possoLiberarTrilho);
        else
          movimentoDeRotacao(false, -3, -1, -1.3, qualTrilho, possoLiberarTrilho);
   
        rotacao = !rotacao;//altera o proximo lado de rotacao
        possoLiberarTrilho = false;
        Platform.runLater(() -> imagemDoTrem.setRotate(180));//Garante que a rotacao da imagem apos a curva sera de 180 graus
      }//fim do if
    }//fim do while
  }//fim do metodo movimentoDeBaixo

  /* ***************************************************************
  * Metodo: movimentoDeRotacao
  * Funcao: Tem como funcao gerenciar a rotacao dos trens, que saem de todas as direcoes, corretamente sobre o trilho
  * Parametros: rotacao = Define para qual lado o trem ira na hora do movimento, 
    incrementoDeRotacao = valor que ira incrementar a rotacao do trem de aos poucos, 
    incrementoDaCoordenadaX = valor que ira incrementar a coordenada X do trem aos poucos, 
    incrementoDaCoordenadaY = valor que ira incrementar a coordenada Y do trem aos poucos,
    qualTrilho = indica o trilho unico pelo qual o trem esta passando, true = de cima, false = de baixo,
    possoLiberarTrilho = valor que verifica se pode liberar o trilho pelo qual o trem passou
  * Retorno: void
  *************************************************************** */
  private void movimentoDeRotacao (boolean rotacao, int incrementoDeRotacao, int incrementoDaCoordenadaX,
  double incrementoDaCoordenadaY, boolean qualTrilho, boolean possoLiberarTrilho)
    throws InterruptedException
  {
    //chama o metodo que realmente rotaciona o trem, utiliza-se uma sobrecarga de nomes
    movimentoDeRotacao(incrementoDeRotacao, incrementoDaCoordenadaX, incrementoDaCoordenadaY);
    moverParaPontoDeCurva(rotacao);//chama o metodo que deixa o trem na coordenada X certa para fazer a proxima curva
    
    if (possoLiberarTrilho)
      recursoCompartilhado.liberarTrilho(identificadorDoTrem, !qualTrilho);//libera o acesso trilho unico
          
    movimentoDeRotacao(-incrementoDeRotacao, incrementoDaCoordenadaX, incrementoDaCoordenadaY);//re-rotaciona o trem
  }//fim do metodo movimentoDeRotacao
  
  /* ***************************************************************
  * Metodo: movimentoDeRotacao
  * Funcao: Tem como funcao rotacionar os trens, que saem de todas as direcoes, corretamente sobre o trilho
  * Parametros: incrementoDeRotacao = valor que ira incrementar a rotacao do trem de aos poucos, 
    incrementoDaCoordenadaX = valor que ira incrementar a coordenada X do trem aos poucos, 
    incrementoDaCoordenadaY = valor que ira incrementar a coordenada Y do trem aos poucos
  * Retorno: void
  *************************************************************** */
  private void movimentoDeRotacao(int incrementoDeRotacao, int incrementoCoordenadaX, double incrementoCoordenadaY)
    throws InterruptedException
  {
    for(int i = 0; i < 30; i++)
    {
      parar();//verifica se o trem tem de parar
      sleep(velocidade);//da um tempo para rotacionar suavemente de acordo com a velocidade
      Platform.runLater(() ->
      {//rotaciona aos poucos o trem
        imagemDoTrem.setRotate(imagemDoTrem.getRotate() + incrementoDeRotacao);
        imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() + incrementoCoordenadaX);
        imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() + incrementoCoordenadaY);
      });
    }//fim do for
  }//fim do metodo movimentoDeRotacao
  
  /* ***************************************************************
  * Metodo: moverParaPontoDeCurva
  * Funcao: move o trem para a posicao X apropriada para ele realizar a proxima curva
  * Parametros: rotacao = Define para que lado o trem ira se movimentar
  * Retorno: void
  *************************************************************** */
  public void moverParaPontoDeCurva(boolean rotacao)
    throws InterruptedException
  {
    //diferenca entre a posicao X atual do trem com a posicao que ele devera ir para fazer uma curva alinhado com o trilho
    int quantidadeDeMovimento = Math.abs((int) imagemDoTrem.getLayoutX() - local.getPontoDeCurva(rotacao));
    
    //verifica se devera incrementar ou decrementar a posicao do layout X do trem
    int valorDeIncremento = rotacao ? VALOR_DE_INCREMENTO : -VALOR_DE_INCREMENTO;
    
    while (quantidadeDeMovimento > 0)//move o trem ate a posicao ideal
    {
      quantidadeDeMovimento -= VALOR_DE_INCREMENTO;
      incrementaLayoutX(valorDeIncremento);
    }//fim do while
  }//fim do metodo moverParaPontoDeCurva
  
  
  /* ***************************************************************
  * Metodo: incrementaLayoutX
  * Funcao: incrementa a posicao X de acordo com o valor de incremento e a velocidade
  * Parametros: valor = valor de incremento positivo ou negativo de acordo com a direcao do trem
  * Retorno: void
  *************************************************************** */
  private void incrementaLayoutX(int valor)
    throws InterruptedException
  {
    sleep(velocidade);//para icrementar a posicao do layout X aos poucos
    parar();//verifica se o trem parou
    Platform.runLater(() -> imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() + valor));//metodo que incrementa a posicao X
  }//fim do metodo incrementaLayoutX

  /* ***************************************************************
  * Metodo: incrementaLayoutY
  * Funcao: incrementa a posicao Y de acordo com o valor de incremento e a velocidade
  * Parametros: valor = valor de incremento positivo ou negativo de acordo com a direcao do trem
  * Retorno: void
  *************************************************************** */
  private void incrementaLayoutY(int valor)
    throws InterruptedException
  {
    sleep(velocidade);//para icrementar a posicao do layout Y aos poucos
    parar();//verifica se o trem parou
    Platform.runLater(() -> imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() + valor));//metodo que incrementa a posicao Y
  }//fim do metodo incrementaLayoutY
  
  /* ***************************************************************
  * Metodo: getImageView
  * Funcao: retorna a referencia da imagem quando necessario
  * Parametros: nenhum
  * Retorno: ImageView
  *************************************************************** */
  public ImageView getImageView()
  {
    return imagemDoTrem;
  }//fim do metodo getImageView

  /* ***************************************************************
  * Metodo: setVelocidade
  * Funcao: altera a velocidade do trem ou modifica a variavel estaParado
    para parar o trem.
  * Parametros: velocidade = valor da nova velocidade
  * Retorno: void
  *************************************************************** */
  public void setVelocidade(int velocidade)
  {
    if (velocidade == 0)//se a velocidade e igual a zero para o trem
      pararTrem = true;//quer dizer que o trem esta parado e deve ser travado
   
    else
    {
      this.velocidade = 33 - velocidade;//quanto maior o slider da velocidade menor o tempo de sleep
      pararTrem = false;//quer dizer que o trem nao esta parado
    }//fim do else
  }//fim do metodo setVelocidade
  
  /* ***************************************************************
  * Metodo: setLocalDeInicio
  * Funcao: altera o local de inicio do trem.
  * Parametros: local = novo local de partida do trem
  * Retorno: void
  *************************************************************** */
  public void setLocalDeInicio(LocalDeInicio local)
  {
    this.local = local;
  }//fim do metodo setLocalDeInicio
  
  /* ***************************************************************
  * Metodo: parar
  * Funcao: para o trem ate que o valor do Slider seja diferente de 0
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void parar()
    throws InterruptedException
  {
    while(pararTrem)//trava o trem enquanto a variavel pararTrem estiver como true
      sleep(100);
  }//fim do metodo parar
}// fim da classe Trem
