/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 22/03/2024
* Ultima alteracao.: 28/03/2024
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
  private Thread threadTrem1;
  private Thread threadTrem2;

  /* ***************************************************************
  * Metodo: Construtor
  * Funcao: Adiciona a imagem de background ao pane, instancia e adiciona os dois objetos Trem ao pane
  * Parametros: nenhum
  * Retorno: nenhum
  *************************************************************** */
  public Trilho (Resolucao resolucao)
  {
    getChildren().add(new ImageView(String.format("%sTrilho.png", resolucao.getImgDiretorio())));
    
    trem1 = new Trem(String.format("%sTremVermelho1.png", resolucao.getImgDiretorio()), resolucao);
    trem2 = new Trem(String.format("%sTremRoxo2.png", resolucao.getImgDiretorio()), resolucao);
      
    if (resolucao.getResolucaoTipo().equals("RA"))//coloca os trens em posicoes iniciais de acordo com a resolucao escolhida
    {
      trem1.setLocalDeInicio(LocalDeInicio.ESQUERDA_CIMA_RA);
      trem2.setLocalDeInicio(LocalDeInicio.DIREITA_CIMA_RA);
    }//fim do if
    else
    {
      trem1.setLocalDeInicio(LocalDeInicio.ESQUERDA_CIMA_RM);
      trem2.setLocalDeInicio(LocalDeInicio.DIREITA_CIMA_RM);
    }//fim do else
    
    getChildren().add(trem1);
    getChildren().add(trem2);
  }//fim do construtor
  
  
  /* ***************************************************************
  * Metodo: iniciarThreads
  * Funcao: Instancia, configura e inicia as threads onde cada uma corresponde a um trem
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void iniciarThreads()
  {
    threadTrem1 = new Thread(trem1);
    threadTrem2 = new Thread(trem2);
    threadTrem1.setDaemon(true);//para terminar as threads quando o programa fechar
    threadTrem2.setDaemon(true);
    threadTrem1.start();//para executar as threads
    threadTrem2.start();
  }//fim do metodo iniciarThreads

  /* ***************************************************************
  * Metodo: resetarTrens
  * Funcao: Interrompe as threads em execucao, e chama o metodo iniciarThreads para instanciar novamente
    as threads
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void resetarTrens()
  {
    threadTrem1.interrupt();
    threadTrem2.interrupt();
    iniciarThreads();
  }//fim do metodo resetarTrens
  
  
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
