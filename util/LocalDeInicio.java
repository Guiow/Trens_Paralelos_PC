/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 22/03/2024
* Ultima alteracao.: 30/04/2024
* Nome.............: Trens Paralelos
* Funcao...........: Classe enum utilizada para criar as 4 possiveis localizacoes iniciais, cada uma
  delas armazena a posicao X e Y inicial na qual o trem ira comecar seu movimento pelo trilho
*************************************************************** */

package util;

public enum LocalDeInicio
{
  ESQUERDA_CIMA_RA(167, -100, 0, 240, 200), DIREITA_CIMA_RA(383, -100, 0, 349, 307),//valores usados caso escolhido alta resolucao
  ESQUERDA_BAIXO_RA(167, 900, 180, 240, 200), DIREITA_BAIXO_RA(383, 900, 180, 349, 307),
  
  ESQUERDA_CIMA_RM(133, -80, 0, 186, 166), DIREITA_CIMA_RM(307, -80, 0, 273, 254),// valores usados caso escolhido media resolucao
  ESQUERDA_BAIXO_RM(133, 720, 180, 186, 166), DIREITA_BAIXO_RM(307, 720, 180, 273, 254);

  private int layoutXInicial;
  private int layoutYInicial;
  private int rotacaoInicial;//rotacao inicial da imagem 0 graus para baixo 180 graus para cima
  private int pontoDeCurvaDireita;//ponto que faz o trem se alinhar com o trilho em uma curva para direita
  private int pontoDeCurvaEsquerda;//para esquerda
  
  /* ***************************************************************
  * Metodo: Construtor
  * Funcao: instanciar os Objetos LocalDeInicio
  * Parametros: layoutXIncial = posicao X inicial, layoutYInicial = posicao Y inicial,
    rotacaoInicial = graus de rotacao inicial da imagem,
    pontoDeCurvaDireita = ponto que promove o alinhamento entre trem e trilho durante uma curva no layout X para direita,
    pontoDeCurvaEsquerda =  ponto que promove o alinhamento entre trem e trilho durante uma curva no layout X para esquerda,
  * Retorno: nenhum
  *************************************************************** */
  LocalDeInicio(int layoutXInicial, int layoutYInicial, int rotacaoInicial, int pontoDeCurvaDireita, int pontoDeCurvaEsquerda)
  {
    this.layoutXInicial = layoutXInicial;
    this.layoutYInicial = layoutYInicial;
    this.rotacaoInicial = rotacaoInicial;
    this.pontoDeCurvaDireita = pontoDeCurvaDireita;
    this.pontoDeCurvaEsquerda = pontoDeCurvaEsquerda;
  }//fim do construtor

  /* ***************************************************************
  * Metodo: getLayoutXInicial
  * Funcao: retorna a posicao X inicial do trem
  * Parametros: nenhum
  * Retorno: valor int contendo a posicao X inicial do trem
  *************************************************************** */
  public int getLayoutXInicial()
  {
    return layoutXInicial;
  }//fim do getLayoutXInicial

  /* ***************************************************************
  * Metodo: getLayoutYInicial
  * Funcao: retorna a posicao Y inicial do trem
  * Parametros: nenhum
  * Retorno: valor int contendo a posicao Y inicial do trem
  *************************************************************** */
  public int getLayoutYInicial()
  {
    return layoutYInicial;
  }//fim do getLayoutYInicial
  
  /* ***************************************************************
  * Metodo: getRotacaoInicial
  * Funcao: retorna a rotacao inicial da imagem de acordo com a localizacao do trem
  * Parametros: nenhum
  * Retorno: valor int contendo a rotacao inicial em graus do trem, 0 para baixo e 180 para cima
  *************************************************************** */
  public int getRotacaoInicial() {
    return rotacaoInicial;
  }
  
  /* ***************************************************************
  * Metodo: getPontoDeCurva
  * Funcao: retornar o ponto de curva para alinhamento entre trem e trilho quando for necessario
  * Parametros: rotacao = para que lado o trem ira rotacionar para fazer a curva, true = direita, false = esquerda
  * Retorno: valor int contendo o ponto X que ajuda no alinhamento entre trem e trilho em uma curva
  *************************************************************** */
  public int getPontoDeCurva(boolean rotacao)
  {
    if (rotacao)
      return pontoDeCurvaDireita;
    
    return pontoDeCurvaEsquerda;
  }//fim do metodo getPontoDeCurva
}//fim da classe LocalDeInicio

