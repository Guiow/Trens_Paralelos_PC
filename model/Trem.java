/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 22/03/2024
* Ultima alteracao.: 30/04/2024
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

  private Resolucao resolucao;
  private ImageView imagemDoTrem;
  private LocalDeInicio local;
  
  private boolean estaParado;//para fazer verificacoes se o trem esta parado
  
  /* ***************************************************************
  * Metodo: Construtor
  * Funcao: chamar o construtor da superclasse para carregar a imagem e armazena os valores das 
    variaveis velocidade e resolucao
  * Parametros: imagemURL = URL da imagem, velocidade = representacao da velocidade,
    resolucao = objeto resolucao com valores que promovem alinhamento de trem e trilho
  * Retorno: nenhum
  *************************************************************** */
  public Trem(String imageURL, Resolucao resolucao)
  {
    imagemDoTrem = new ImageView(String.format("%s%s", resolucao.getImgDiretorio(), imageURL));
    this.resolucao = resolucao;
    velocidade = 15;
  }//fim do construtor
  
  /* ***************************************************************
  * Metodo: run
  * Funcao: Chamado quando a thread e iniciada tem a funcao de executar os respectivos metodos de movimento
    de acordo com a localizacao inicial e define a posicao X e Y e sentido inicial do trem.
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  @Override
  public void run()
  {
    try
    {
      while(true)//while infinito que termina quando o programa fecha ou uma excessao e lancada
      {
        Platform.runLater(() -> {// define o layout X e Y inicial e a sentido de rotacao inicial
          imagemDoTrem.setLayoutX(local.getLayoutXInicial());
          imagemDoTrem.setLayoutY(local.getLayoutYInicial());
          imagemDoTrem.setRotate(local.getRotacaoInicial());//0 sentido apontado para baixo 180 para cima
        });
      
        sleep(300);
        switch (local)//chama metodo correspondente ao movimento do trem de acordo com seu local inicial
        {
          case ESQUERDA_CIMA_RA:
          case ESQUERDA_CIMA_RM:
            movimentoDeCima(true);
            break;
          case DIREITA_CIMA_RA:
          case DIREITA_CIMA_RM:
            movimentoDeCima(false);
            break;
          case ESQUERDA_BAIXO_RA:
          case ESQUERDA_BAIXO_RM:
            movimentoDeBaixo(true);
            break;
          case DIREITA_BAIXO_RA:
          case DIREITA_BAIXO_RM:
            movimentoDeBaixo(false);
            break;
        }//fim do switch
      }//fim do while
    }//fim do try 
    catch (InterruptedException ex)
    {
      System.err.println("Resetando posicoes...");
    }//fim do catch
  }//fim do metodo run
  
  /* ***************************************************************
  * Metodo: movimentoDeCima
  * Funcao: Gerancia o movimento dos trens que vem de cima para baixo
  * Parametros: rotacao = Define para que lado o trem ira se movimentar primeiro. Se for true ele vai
    para direita. Se for false ele vai para esquerda
  * Retorno: void
  *************************************************************** */
  private void movimentoDeCima(boolean rotacao)
    throws InterruptedException
  {
    int count = 0;//conta de acordo com as curvas feitas pelo trem, quando for 5 quer dizer que o trem realizou todas as curvas
    
    while(true)
    {
      incrementaLayoutY(VALOR_DE_INCREMENTO);//incrementa o layout Y para mover o trem para baixo

      //verifica se esta no ponto Y certo para fazer proxima curva, se sim realiza a curva
      if (imagemDoTrem.getLayoutY() > resolucao.getPontoDeAlinhamentoDeCima(count))
      { 
        count++;
        if (count == 5)
         break;     
         
        else if (rotacao)//faz o trem alternar entre movimento de rotacao direita e esquerda
          movimentoCimaDeRotacaoDireita();
          
        else
          movimentoCimaDeRotacaoEsquerda();
          
        rotacao = !rotacao;//altera o proximo lado de rotacao
        Platform.runLater(() -> imagemDoTrem.setRotate(0));//Garante que a rotacao apos a curva sera de 0 graus
      }//fim do if
    }//fim do while
  }//fim do metodo movimentoDeCima

  /* ***************************************************************
  * Metodo: movimentoCimaDeRotacaoDireita
  * Funcao: auxilia o metodo movimentoDeCima para o trem rotacionar e fazer a curva para direita 
    corretamente sobre o trilho
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void movimentoCimaDeRotacaoDireita()
    throws InterruptedException
  {
    while (imagemDoTrem.getRotate() > -90)//enquanto o trem rotaciona ate 90 graus no sentido anti horario,
    {                       //tentando promover a animacao do movimento de rotacao  
      parar();
      sleep(velocidade);
      Platform.runLater(() ->
      {
        imagemDoTrem.setRotate(imagemDoTrem.getRotate() - 3);
        imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() + 1.3);
        imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() + 1);
      });//rotaciona aos poucos o trem
    }//fim do while
    
    moverParaPontoDeCurva(true);
        
    while(imagemDoTrem.getRotate() < 0)//enquanto o trem rotaciona de volta, para uma rotacao de 0 graus
    {
      parar();
      sleep(velocidade);
      Platform.runLater(() ->
      {
        imagemDoTrem.setRotate(imagemDoTrem.getRotate() + 3);
        imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() + 1);
        imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() + 1);
      });
    }//fim do while
  }//fim do metodo movimentoCimaDeRotacaoDireita
  
  /* ***************************************************************
  * Metodo: movimentoCimaDeRotacaoEsquerda
  * Funcao: auxilia o metodo movimentoDeCima para o trem rotacionar e fazer a curva para
    esquerda corretamente sobre otrilho
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void movimentoCimaDeRotacaoEsquerda()
    throws InterruptedException
  {
    //faz com que os metodos setLayoutY e setLayoutX movimente apenas o necessario para manter o alinhamento com o trilho

    while (imagemDoTrem.getRotate() < 90)//enquanto o trem rotaciona ate 90 graus no sentido horario
    {                      //tentando promover a animacao do movimento de rotacao  
      parar();
      sleep(velocidade);
      Platform.runLater(() ->
      {
        imagemDoTrem.setRotate(imagemDoTrem.getRotate() + 3);
        imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() + 1.3);
        imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() - 1);
      });//rotaciona aos poucos
    }//fim do while

    moverParaPontoDeCurva(false);

    while(imagemDoTrem.getRotate() > 0)//enquanto o trem rotaciona de volta, para uma rotacao de 0 graus
    {
      parar();
      sleep(velocidade);
      Platform.runLater(() -> {
        imagemDoTrem.setRotate(imagemDoTrem.getRotate() - 3);
        imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() - 1);
        imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() + 1);
      });
    }//fim do while
  }//fim do metodo movimentoCimaDeRotacaoEsquerda
  
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
    int count = 0;//conta de acordo com as curvas feitas pelo trem, quando for 5 quer dizer que o trem realizou todas as curvas
    
    while(true)
    {
      incrementaLayoutY(-VALOR_DE_INCREMENTO);//decrementa o layout Y para mover o trem para cima

      if (imagemDoTrem.getLayoutY() < resolucao.getPontoDeAlinhamentoDeBaixo(count))
      {
        count++;
        if (count == 5)
          break;
          
        else if (rotacao)//faz o trem alternar entre movimento de rotacao direita e esquerda
          movimentoBaixoDeRotacaoDireita();
          
        else
          movimentoBaixoDeRotacaoEsquerda();
   
        rotacao = !rotacao;//altera o proximo lado de rotacao
        Platform.runLater(() -> imagemDoTrem.setRotate(180));//Garante que a rotacao da imagem apos a curva sera de 180 graus
      }//fim do if
    }//fim do while
  }//fim do metodo movimentoDeBaixo
  
  /* ***************************************************************
  * Metodo: movimentoBaixoDeRotacaoDireita
  * Funcao: auxilia o metodo movimentoDeBaixo para o trem rotacionar e fazer a curva para 
    direita corretamente sobre o trilho
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void movimentoBaixoDeRotacaoDireita()
    throws InterruptedException
  {
    while (imagemDoTrem.getRotate() < 270)//enquanto a imagem do trem rotaciona ate 270 graus no sentido horario,
    {                        //tentando promover a animacao do movimento de rotacao  
      parar();
      sleep(velocidade);
      Platform.runLater(() ->
      {
        imagemDoTrem.setRotate(imagemDoTrem.getRotate() + 3);
        imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() - 1.3);
        imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() + 1);
      });//rotaciona aos poucos o trem
    }//fim do while
    
    
    moverParaPontoDeCurva(true);
   
    while(imagemDoTrem.getRotate() > 180)//enquanto o trem rotaciona de volta, para uma rotacao de 180 graus
    {
      parar();
      sleep(velocidade);
      Platform.runLater(() ->
      {
        imagemDoTrem.setRotate(imagemDoTrem.getRotate() - 3);
        imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() + 1);
        imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() - 1);
      });
    }//fim do while
  }//fim do metodo movimentoBaixoDeRotacaoDireita
  
  /* ***************************************************************
  * Metodo: movimentoBaixoDeRotacaoEsquerda
  * Funcao: auxilia o metodo movimentoDeBaixo para o trem rotacionar e fazer a curva para esquerda 
    corretamente sobre o trilho
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void movimentoBaixoDeRotacaoEsquerda()
    throws InterruptedException
  {
    while (imagemDoTrem.getRotate() > 90)//enquanto o trem rotaciona ate 90 graus no sentido anti horario
    {                     //tentando promover a animacao do movimento de rotacao  
      parar();               
      sleep(velocidade);
      Platform.runLater(() ->
      {
        imagemDoTrem.setRotate(imagemDoTrem.getRotate() - 3);
        imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() - 1.3);
        imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() - 1);
      });//rotaciona aos poucos
    }//fim do while

    moverParaPontoDeCurva(false);

    while(imagemDoTrem.getRotate() < 180)//enquanto o trem rotaciona de volta, para uma rotacao de 180 graus
    {
      parar();
      sleep(velocidade);
      Platform.runLater(() -> {
        imagemDoTrem.setRotate(imagemDoTrem.getRotate() + 3);
        imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() - 1);
        imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() - 1);
      });
    }//fim do while
  }//fim do metodo movimentoBaixoDeRotacaoEsquerda
  
  /* ***************************************************************
  * Metodo: moverParaPontoDeCurva
  * Funcao: move o trem para o ponto apropriado para ele realizar a proxima curva
  * Parametros: rotacao = Define para que lado o trem ira se movimentar
  * Retorno: void
  *************************************************************** */
  public void moverParaPontoDeCurva(boolean rotacao)
    throws InterruptedException
  {
    //diferenca entre a posicao X atual do trem com a posicao que ele devera ir para fazer uma curva alinhado com o trilho
    int quantidadeDeMovimento = Math.abs((int) imagemDoTrem.getLayoutX() - local.getPontoDeCurva(rotacao));
    
    //verifica se devera incrementar ou decrementar o layout X do trem
    int valorDeIncremento = rotacao ? VALOR_DE_INCREMENTO : -VALOR_DE_INCREMENTO;
    
    while (quantidadeDeMovimento > 0)//move o trem ate a posicao ideal
    {
      quantidadeDeMovimento -= VALOR_DE_INCREMENTO;
      incrementaLayoutX(valorDeIncremento);
    }//fim do while
  }//fim do metodo moverParaPontoDeCurva
  
  /* ***************************************************************
  * Metodo: incrementaLayoutX
  * Funcao: incrementa a posicao X de acordo com o valor de incremento
  * Parametros: valor = valor de incremento positivo ou negativo de acordo com a direcao
    do trem
  * Retorno: void
  *************************************************************** */
  private void incrementaLayoutX(int valor)
    throws InterruptedException
  {
    sleep(velocidade);
    parar();
    Platform.runLater(() -> imagemDoTrem.setLayoutX(imagemDoTrem.getLayoutX() + valor));
  }//fim do metodo incrementaLayoutX

  /* ***************************************************************
  * Metodo: incrementaLayoutY
  * Funcao: incrementa a posicao Y de acordo com o valor de incremento
  * Parametros: valor = valor de incremento positivo ou negativo de acordo com a direcao
    do trem
  * Retorno: void
  *************************************************************** */
  private void incrementaLayoutY(int valor)
    throws InterruptedException
  {
    sleep(velocidade);
    parar();
    Platform.runLater(() -> imagemDoTrem.setLayoutY(imagemDoTrem.getLayoutY() + valor));
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
    if (velocidade == 0)
    {
      estaParado = true;
    }
    else
    {
      this.velocidade = 33 - velocidade;
      estaParado = false;
    }
  }//fim do metodo setVelocidade
  
  /* ***************************************************************
  * Metodo: setLocalDeInicio
  * Funcao: altera o local de inicio do trem.
  * Parametros: local = novo local de inicio do trem
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
    while(estaParado)
      sleep(100);
  }//fim do metodo parar
  
}// fim da classe Trem
