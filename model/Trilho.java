/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 22/03/2024
* Ultima alteracao.: 30/04/2024
* Nome.............: Trens Paralelos
* Funcao...........: Conter, criar, iniciar ambos os trems. Tambem conter a imagem de background onde os
  trems irao se movimentar.
*************************************************************** */
package model;

import util.*;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Trilho extends Pane
{
  private Trem trem1;
  private Trem trem2;
  
  private LocalDeInicio localDeInicioTrem1;
  private LocalDeInicio localDeInicioTrem2;
  
  private Resolucao resolucao;

  /* ***************************************************************
  * Metodo: Construtor
  * Funcao: Adiciona a imagem de background ao pane, instancia e adiciona os dois objetos Trem ao pane
  * Parametros: nenhum
  * Retorno: nenhum
  *************************************************************** */
  public Trilho (Resolucao resolucao)
  {
    getChildren().add(new ImageView(String.format("%sTrilho.png", resolucao.getImgDiretorio())));
    
    if (resolucao.getResolucaoTipo().equals("RA"))//coloca os trens em posicoes iniciais de acordo com a resolucao escolhida
    {
      localDeInicioTrem1 = LocalDeInicio.ESQUERDA_CIMA_RA;
      localDeInicioTrem2 = LocalDeInicio.DIREITA_CIMA_RA;
    }//fim do if
    else
    {
      localDeInicioTrem1 = LocalDeInicio.ESQUERDA_CIMA_RM;
      localDeInicioTrem2 = LocalDeInicio.DIREITA_CIMA_RM;
    }//fim do else
    
    this.resolucao = resolucao;
  }//fim do construtor
  
  /* ***************************************************************
  * Metodo: iniciarThreads
  * Funcao: Instancia, configura, reinicia e inicia as threads onde cada uma corresponde a um trem
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void iniciarThreads()
  {
    trem1 = new Trem("TremVermelho1.png", resolucao);//cria os dois trens
    trem2 = new Trem("TremRoxo2.png", resolucao);
    
    trem1.setLocalDeInicio(localDeInicioTrem1);//seta o local de inicio dos trens
    trem2.setLocalDeInicio(localDeInicioTrem2);
    
    trem1.setDaemon(true);//para terminar as threads quando o programa fechar
    trem2.setDaemon(true);
    
    trem1.start();//para executar as threads
    trem2.start();
    
    getChildren().add(trem1.getImageView());//adiciona as imagens dos trens ao trilho
    getChildren().add(trem2.getImageView());
  }//fim do metodo iniciarThreads

  /* ***************************************************************
  * Metodo: resetarTrens
  * Funcao: Interrompe as threads em execucao, e chama o metodo iniciarThreads para iniciar novamente
    as threads
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void resetarTrens()
  {
    trem1.interrupt();
    trem2.interrupt();
    getChildren().removeAll(trem1.getImageView(), trem2.getImageView());
    iniciarThreads();
  }//fim do metodo resetarTrens
  
    /* ***************************************************************
  * Metodo: setLocalDeInicioDosTrens
  * Funcao: muda a variavel localDeInicio de ambos os trens para que mudem o local de partida
  * Parametros: localDeInicioTrem1 = posicao de onde o trem1 ira partir, 
                localDeInicioTrem2 = posicao de onde o trem2 ira partir
  * Retorno: void
  *************************************************************** */
  public void setLocalDeInicioDosTrens(LocalDeInicio localDeInicioTrem1, LocalDeInicio localDeInicioTrem2)
  {
    this.localDeInicioTrem1 = localDeInicioTrem1;
    this.localDeInicioTrem2 = localDeInicioTrem2;
  }
  
  /* ***************************************************************
  * Metodo: getTrem1
  * Funcao: retornar a referencia do Trem1 para o controller poder fazer alteracoes por exemplo na
    velocidade do trem
  * Parametros: nenhum
  * Retorno: Trem correspondente ao trem2
  *************************************************************** */
  public Trem getTrem1() {
    return trem1;
  }//fim do metodo getTrem1

  /* ***************************************************************
  * Metodo: getTrem2
  * Funcao: retornar a referencia do Trem2
  * Parametros: nenhum
  * Retorno: Trem correspondente ao trem2
  *************************************************************** */
  public Trem getTrem2() {
    return trem2;
  }//fim do metodo getTrem2
}//fim da classe Trilho
