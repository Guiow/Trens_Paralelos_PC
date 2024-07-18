/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 28/03/2024
* Ultima alteracao.: 28/03/2024
* Nome.............: Trens Paralelos
* Funcao...........: armazenar valores que ajudam o programa a suportar mais que uma resolucao.
*************************************************************** */
package util;

public class Resolucao
{
  private String resolucaoTipo;//tipo da resolucao
  private int larguraMaxima;//largura em pixels
  private int alturaMaxima;//altura em pixels
  private int larguraDoTrilho;//largura da imagem de background do Trilho em pixels
  private String imgDiretorio;//caminho do diretorio de imagem
  
  //pontos especificos no eixo Y para promover a curva do trem no trilho de cima para baixo
  private int[] pontosDeAlinhamentoDeCima;
  private int[] pontosDeAlinhamentoDeBaixo;//o mesmo porem de baixo para cima
  
  /* ***************************************************************
  * Metodo: construtor
  * Funcao: armazenar os valores necessarios para o programa executar de acordo com a resolucao escolhida
  * Parametros: resolucaoTipo = tipo de resolucao escolhida pelo usuario
  * Retorno: nenhum
  *************************************************************** */
  public Resolucao(String resolucaoTipo)
  {
    this.resolucaoTipo = resolucaoTipo;
    
    if(resolucaoTipo.equals("RA"))//configura as variaveis caso o usuario escolha o tipo RA
    {
      larguraMaxima = 1000;
      alturaMaxima = 900;
      larguraDoTrilho = 600;
      imgDiretorio = "img/imgRA/";
      int[] valoresEspecificosCima = {82, 260, 469, 651, 910};//pontos de curva do trem de cima para baixo
      int[] valoresEspecificosBaixo = {729, 551, 342, 160, -110};//pontos de curva do trem de baixo para cima
      pontosDeAlinhamentoDeCima = valoresEspecificosCima;
      pontosDeAlinhamentoDeBaixo = valoresEspecificosBaixo;
    }//fim do if
    
    else//se nao configura as variaveis como tipo RM padrao
    {
      this.resolucaoTipo = "RM";
      larguraMaxima = 880;
      alturaMaxima = 720;
      larguraDoTrilho = 480;
      imgDiretorio = "img/imgRM/";
      int[] valoresEspecificosCima = {57, 202, 363, 510, 730};//pontos de curva do trem de cima para baixo
      int[] valoresEspecificosBaixo = {595, 448, 286, 140, -90};//pontos de curva do trem de baixo para cima
      pontosDeAlinhamentoDeCima = valoresEspecificosCima;
      pontosDeAlinhamentoDeBaixo = valoresEspecificosBaixo;
    }//fim do else
  }//fim do construtor
  
  
  /* ***************************************************************
  * Metodo: getResolucaoTipo
  * Funcao: retornar o tipo de resolucao
  * Parametros: nenhum
  * Retorno: String contendo o tipo de resolucao
  *************************************************************** */
  public String getResolucaoTipo()
  {
    return resolucaoTipo;
  }//fim do metodo getResolucaoTipo
  
  /* ***************************************************************
  * Metodo: getLarguraMaxima
  * Funcao: retornar a largura total da resolucao escolhida
  * Parametros: nenhum
  * Retorno: int
  *************************************************************** */
  public int getLarguraMaxima()
  {
    return larguraMaxima;
  }//fim do metodo getLarguraMaxima
  
  /* ***************************************************************
  * Metodo: getAlturaMaxima
  * Funcao: retornar a altura total da resolucao escolhida
  * Parametros: nenhum
  * Retorno: int
  *************************************************************** */
  public int getAlturaMaxima()
  {
    return alturaMaxima;
  }// fim do metodo getAlturaMaxima
  
  /* ***************************************************************
  * Metodo: getAlturaMaxima
  * Funcao: retornar a largura da imagem do trilho de acordo com a resolucao escolhida
  * Parametros: nenhum
  * Retorno: int
  *************************************************************** */
  public int getLarguraDoTrilho()
  {
    return larguraDoTrilho;
  }//fim do metodo getLarguraDoTrilho
  
  /* ***************************************************************
  * Metodo: getImgDiretorio
  * Funcao: retorna uma String que especifica o caminho para o diretorio das imagens
    correspondentes com o tipo de resolucao escolhida
  * Parametros: nenhum
  * Retorno: String com o caminho do diretorio
  *************************************************************** */
  public String getImgDiretorio()
  {
    return imgDiretorio;
  }//fim do metodo getImgDiretorio
  
  
  /* ***************************************************************
  * Metodo: getPontoDeAlinhamentoDeCima
  * Funcao: retorna um ponto Y que permite que o trem faca uma curva alinhado com o Trilho,
    recebe um indice para informar a proxima curva que o trem ira fazer e apos isso retorna
    o ponto Y que corresponde ao valor do proximo pixel que o trem ira fazer a curva.
    Para trens que vem de cima para baixo
  * Parametros: indice = indice que informa a curva que o trem esta fazendo
  * Retorno: int
  *************************************************************** */
  public int getPontoDeAlinhamentoDeCima(int indice)
  {
    return pontosDeAlinhamentoDeCima[indice];
  }// fim do metodo getPontoDeAlinhamentoDeCima
  
  /* ***************************************************************
  * Metodo: getPontoDeAlinhamentoDeCima
  * Funcao: retornar o ponto Y que permite que o trem faca uma curva alinhado com o trilho.
    Para trens que vem de baixo para cima
  * Parametros: indice = indice que informa a curva que o trem esta fazendo
  * Retorno: int
  *************************************************************** */
  public int getPontoDeAlinhamentoDeBaixo(int indice)
  {
    return pontosDeAlinhamentoDeBaixo[indice];
  }//fim do metodo getPontoDeAlinhamentoDeBaixo
}

