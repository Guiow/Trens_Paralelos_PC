/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 22/03/2024
* Ultima alteracao.: 20/05/2024
* Nome.............: Trens Paralelos
* Funcao...........: Cria, instancia e interrompe as threads relacionadas ao trem, de forma que seja a classe que gerencia
  o processo de criaçao e de finalizacao dos trens
*************************************************************** */
package model;

import util.*;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Trilho extends Pane
{
  private Trem trem1;// cria os dos trens
  private Trem trem2;
  private LocalDeInicio localDeInicioTrem1;// salva o local de inicio de ambos os trens
  private LocalDeInicio localDeInicioTrem2;
  
  private Resolucao resolucao;// salva a resolucao escolhida
  
  private ExclusaoMutua recursoCompartilhado;//representa a parte compartilhada, trilho unico, dos trens
  private int tipoDeAlgoritmo;

  /* ***************************************************************
  * Metodo: Construtor
  * Funcao: Adiciona a imagem de background ao pane, instancia e adiciona os dois objetos Trem ao pane
  * Parametros: nenhum
  * Retorno: nenhum
  *************************************************************** */
  public Trilho (Resolucao resolucao)
  {
    getChildren().add(new ImageView(String.format("%sTrilho.png", resolucao.getImgDiretorio())));//adiciona a imagem do trilho
    
    if (resolucao.getResolucaoTipo().equals("RA"))//coloca os trens nas posicoes iniciais de acordo com a resolucao escolhida
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
    recursoCompartilhado = new ExclusaoMutua(resolucao);
    tipoDeAlgoritmo = 1;//algoritmo inicial sera a variavelDeTravamento
  }//fim do construtor
  
  /* ***************************************************************
  * Metodo: iniciarThreads
  * Funcao: Instancia, configura, reinicia e inicia as threads onde cada uma corresponde a um trem
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void iniciarThreads()
  {
    recursoCompartilhado.reconfigurarAlgoritmo(tipoDeAlgoritmo, localDeInicioTrem1.getLayoutYInicial(),
    localDeInicioTrem2.getLayoutYInicial());//reconfigura o recursoCompartilhado dos trens
    
    trem1 = new Trem("TremVermelho1.png", resolucao, recursoCompartilhado, 1);//recria os trens
    trem2 = new Trem("TremRoxo2.png", resolucao, recursoCompartilhado, 2);
    
    trem1.setLocalDeInicio(localDeInicioTrem1);//configura o local de inicio dos trens
    trem2.setLocalDeInicio(localDeInicioTrem2);
    
    trem1.setDaemon(true);//para terminar as threads quando o programa fechar
    trem2.setDaemon(true);
    
    trem1.start();//para começar a executar os threads
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
    trem1.interrupt();//interrompe as threads
    trem2.interrupt();
    getChildren().removeAll(trem1.getImageView(), trem2.getImageView());//remove as imagens paradas dos trens
    iniciarThreads();//reinicia as threads
  }//fim do metodo resetarTrens
  
  /* ***************************************************************
  * Metodo: setLocalDeInicioDosTrens
  * Funcao: muda a variavel localDeInicio de ambos os trens para que mudem o local de partida
  * Parametros: localDeInicioTrem1 = posicao de onde o trem1 ira partir 
                localDeInicioTrem2 = posicao de onde o trem2 ira partir
  * Retorno: void
  *************************************************************** */
  public void setLocalDeInicioDosTrens(LocalDeInicio localDeInicioTrem1, LocalDeInicio localDeInicioTrem2)
  {
    this.localDeInicioTrem1 = localDeInicioTrem1;
    this.localDeInicioTrem2 = localDeInicioTrem2;
  }//fim do metodo setLocalDeinicioDosTrens
  
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
  
  /* ***************************************************************
  * Metodo: setTipoDeAlgoritmo
  * Funcao: muda a variavel tipoDeAlgoritmo quando o usuario chamar um evento relacionado
  * Parametros: tipoDeAlgoritmo = muda para o algoritmo escolhido pelo usuario
  * Retorno: void
  *************************************************************** */
  public void setTipoDeAlgoritmo(int tipoDeAlgoritmo)
  {
    this.tipoDeAlgoritmo = tipoDeAlgoritmo;
  }//fim do metodo setTipoDeAlgoritmo
  
  /* ***************************************************************
  * Metodo: getRecursoCompartilhado
  * Funcao: retornar a referencia do recursoCompartilhado pelos trens
  * Parametros: nenhum
  * Retorno: ExclusaoMutua
  *************************************************************** */
  public ExclusaoMutua getRecursoCompartilhado()
  {
    return recursoCompartilhado;
  }//fim do metodo getRecursoCompartilhado
}//fim da classe Trilho
