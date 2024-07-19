/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 22/03/2024
* Ultima alteracao.: 20/05/2024
* Nome.............: Trens Paralelos
* Funcao...........: Instancia, gerencia e trata os eventos dos buttons, sliders, e menuButtons. Como tambem tem a funcao de
  instanciar a classe Trilho.
*************************************************************** */
package controller;

import model.*;
import util.*;

import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.control.Slider;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.ResourceBundle;
import java.net.URL;


public class Controller implements Initializable {

  @FXML
  private Slider ajustaVelocidadeTrem1;//para modificar a velocidade do trem1

  @FXML
  private Slider ajustaVelocidadeTrem2;//para modificar a velocidade do trem1

  @FXML
  private MenuButton locaisIniciaisDosTrens;//menu de opcoes das possiveis direcoes dos trens

  @FXML
  private HBox root; //raiz do scene
  
  @FXML
  private ImageView tabuaComPoster;//background esquerdo do trilho
  
  private MenuButton algoritmosDeExclusaoMutua;//menu de opcoes dos possiveis algoritmos
  private Resolucao resolucao;
  private Trilho trilho;
  
  private final int VELOCIDADE_INICIAL = 15;//velocidade inicial dos trens
  
  /* ***************************************************************
  * Metodo: initialize
  * Funcao: inicializa  e configura as variaveis necessarias para a GUI, antes da
    GUI ser carregada para o usuario, como menuButton de direcao e menuButton dos algoritmos
  * Parametros: url = util caso precise da localizacao do arquivo fxml, resourceBundle =
    resourceBundle associado ao arquivo fxml. Ambos necessarios para o Override
  * Retorno: void
  *************************************************************** */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) 
  {
    algoritmosDeExclusaoMutua = new MenuButton();
    MenuItem[] menuItens = new MenuItem[4];
    
    //Cria MenuItems, configura e os adicionam ao menuButton de direcao
    for (int i = 0; i < menuItens.length; i++)
    {
      menuItens[i] = new MenuItem();
      
      menuItens[i].setOnAction(event -> {
        alteraLocalDeInicio(event);//adiciona um ouvinte de eventos ao menuItem
      });
      //adiciona imagens aos menuItems
      menuItens[i].setGraphic(new ImageView(String.format("img/imgTelaPrincipal/SetaOpcao%d.png", i + 1)));
      locaisIniciaisDosTrens.getItems().add(menuItens[i]);//adicona os menuItems ao menuButton de direcao
    }//fim do for
 
    menuItens[0] = new MenuItem("Variável De\nTravamento");//cria 3 menuItems para o menuButton de algoritmo
    menuItens[1] = new MenuItem("Explicita\nAlternância");
    menuItens[2] = new MenuItem("Solução De\nPeterson");
    
    for (int i = 0; i < 3; i++)
    {
      menuItens[i].setOnAction(event -> {
        alteraAlgoritmoDeExclusaoMutua(event);//adiciona um ouvinte de eventos ao menuItem
      });
      algoritmosDeExclusaoMutua.getItems().add(menuItens[i]);//adicona os menuItems ao menuButton de algoritmos
    }//fim do for
    algoritmosDeExclusaoMutua.setId("menuButtonAlgoritmo");//adicionaa um cssId ao menuButton de algoritmo
    algoritmosDeExclusaoMutua.setLayoutX(40);
    
    ajustaVelocidadeTrem1.setMax(30);//ajusta o valor maximo de velocidade
    ajustaVelocidadeTrem2.setMax(30);
    ajustaVelocidadeTrem1.setValue(VELOCIDADE_INICIAL);//define valor da velocidade inicial como 15
    ajustaVelocidadeTrem2.setValue(VELOCIDADE_INICIAL);
  }//fim do metodo initialize
  
  /* ***************************************************************
  * Metodo: configuraResolucao
  * Funcao: Configura as variaveis importantes para o programa com base na resolucao escolhida
    pelo usuario na tela inicial.
  * Parametros: resolucao = Objeto resolucao com variaveis que serao usadas para configurar o programa
  * Retorno: void
  *************************************************************** */
  public void configuraResolucao(Resolucao resolucao)
  {
    this.resolucao = resolucao;
    
    trilho = new Trilho(resolucao);//cria um objeto Trilho.
    trilho.setPrefSize(resolucao.getLarguraDoTrilho(), resolucao.getAlturaMaxima());//configura tamanho preferencial do Trilho.
    root.getChildren().add(trilho);//adiciona Trilho ao layout.
    
    AnchorPane anchorPane = new AnchorPane();//cria um anchorPane e adiciona uma imagem para o background
    anchorPane.getChildren().add(new ImageView(String.format("%sTabua.png", resolucao.getImgDiretorio()))); 
    
    //cria um background para o status do algoritmo da classe ExclusaoMutua e o coloca na posicao certa
    ImageView imagemBackgroundAlgoritmo = new ImageView("img/imgTelaPrincipal/BackgroundDoAlgoritmo.png");
    imagemBackgroundAlgoritmo.setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo());
    imagemBackgroundAlgoritmo.setLayoutX(23);
    
    //ajusta o layoutY do menuButton de algoritmos
    algoritmosDeExclusaoMutua.setLayoutY(resolucao.getCoordenadaYDoBackgroundDoAlgoritmo() + 410);
    
    anchorPane.getChildren().add(imagemBackgroundAlgoritmo);//adiciona a imagem de background do algoritmo,
    anchorPane.getChildren().add(algoritmosDeExclusaoMutua);// o menuButton de algoritmos e os status do algoritmo
    anchorPane.getChildren().addAll(trilho.getRecursoCompartilhado().getStatusDoAlgoritmo());//ao anchor pane.
    
    tabuaComPoster.setImage(new Image(String.format("%sTabuaComPoster.png", resolucao.getImgDiretorio())));//adicona a imagem
    root.getChildren().add(anchorPane);//adiciona o anchor pena ao root
  
    root.setPrefHeight(resolucao.getAlturaMaxima());//configura altura e largura do root
    root.setPrefWidth(resolucao.getLarguraMaxima());
    
    trilho.iniciarThreads();//chama metodo para iniciar as threads
  }//fim do metodo configuraResolucao

  /* ***************************************************************
  * Metodo: reset
  * Funcao: reseta os trens para configuracoes iniciais
  * Parametros: event = evento de mouse gerado ao clicar no butao reset
  * Retorno: void
  *************************************************************** */
  @FXML
  void reset(MouseEvent event) 
  {
    trilho.resetarTrens();//metodo para resetar os trens
    
    ajustaVelocidadeTrem1.setValue(VELOCIDADE_INICIAL);//redefine o slider para o valor inicial
    ajustaVelocidadeTrem2.setValue(VELOCIDADE_INICIAL);
    
    trilho.getTrem1().setVelocidade(VELOCIDADE_INICIAL);//muda a velocidade do trem para a velocidade inicial do app
    trilho.getTrem2().setVelocidade(VELOCIDADE_INICIAL);
  }//fim do metodo reset

  /* ***************************************************************
  * Metodo: mudarVelocidade
  * Funcao: altera a velocidade dos trens de acordo com a alteracao do valor
    dos Sliders
  * Parametros: event = evento de mouse gerado ao arrastar o botao do slider
  * Retorno: void
  *************************************************************** */
  @FXML
  void mudarVelocidade(MouseEvent event)
  {
    if (event.getSource().equals(ajustaVelocidadeTrem1))//verifica qual dos sliders foram alterados
      trilho.getTrem1().setVelocidade((int) ajustaVelocidadeTrem1.getValue());
    else
      trilho.getTrem2().setVelocidade((int) ajustaVelocidadeTrem2.getValue());
  }//fim do metodo mudarVelocidade
  
  /* ***************************************************************
  * Metodo: alteraLocalDeInicio
  * Funcao: altera o local de inicio dos trens de acordo com o MenuItem clicado e reseta os
    trens para a posicao escolhida
  * Parametros: event = evento de mouse gerado ao clicar em um dos MenuItem
  * Retorno: void
  *************************************************************** */
  private void alteraLocalDeInicio(ActionEvent event)
  {
    if(resolucao.getResolucaoTipo().equals("RA"))//verifica se o usuario escolheu o tipo de resolucao RA
    {
      //quatro opcoes distintas, acionadas de acordo com o menu item clicado pelo usuario
      if (event.getSource().equals(locaisIniciaisDosTrens.getItems().get(0)))
        trilho.setLocalDeInicioDosTrens(LocalDeInicio.ESQUERDA_CIMA_RA, LocalDeInicio.DIREITA_CIMA_RA);
      
      else if (event.getSource().equals(locaisIniciaisDosTrens.getItems().get(1)))  
        trilho.setLocalDeInicioDosTrens(LocalDeInicio.ESQUERDA_CIMA_RA, LocalDeInicio.DIREITA_BAIXO_RA);

      else if(event.getSource().equals(locaisIniciaisDosTrens.getItems().get(2)))
        trilho.setLocalDeInicioDosTrens(LocalDeInicio.ESQUERDA_BAIXO_RA, LocalDeInicio.DIREITA_CIMA_RA);

      else 
        trilho.setLocalDeInicioDosTrens(LocalDeInicio.ESQUERDA_BAIXO_RA, LocalDeInicio.DIREITA_BAIXO_RA);
    }//fim do if
    
    else//se nao, o usuario escolheu o tipo de resolucao RM
    {
      //quatro opcoes distintas, acionadas de acordo com o menu item clicado pelo usuario
      if (event.getSource().equals(locaisIniciaisDosTrens.getItems().get(0)))
        trilho.setLocalDeInicioDosTrens(LocalDeInicio.ESQUERDA_CIMA_RM, LocalDeInicio.DIREITA_CIMA_RM);

      else if (event.getSource().equals(locaisIniciaisDosTrens.getItems().get(1)))
        trilho.setLocalDeInicioDosTrens(LocalDeInicio.ESQUERDA_CIMA_RM, LocalDeInicio.DIREITA_BAIXO_RM);

      else if(event.getSource().equals(locaisIniciaisDosTrens.getItems().get(2)))
        trilho.setLocalDeInicioDosTrens(LocalDeInicio.ESQUERDA_BAIXO_RM, LocalDeInicio.DIREITA_CIMA_RM);

      else
        trilho.setLocalDeInicioDosTrens(LocalDeInicio.ESQUERDA_BAIXO_RM, LocalDeInicio.DIREITA_BAIXO_RM);  
    }//fim do else
    
    reset(null);//reseta os trens
  }//fim do metodo alteraLocalDeInicio
  
  /* ***************************************************************
  * Metodo: alteraAlgoritmoDeExclusaoMutua
  * Funcao: modifica o algoritmo de exclusao mutua e reseta os trens
  * Parametros: event = evento de mouse gerado ao clicar em um dos MenuItem
  * Retorno: void
  *************************************************************** */
  private void alteraAlgoritmoDeExclusaoMutua(ActionEvent event)
  {
    if (event.getSource().equals(algoritmosDeExclusaoMutua.getItems().get(0)))
      trilho.setTipoDeAlgoritmo(1);
    
    else if (event.getSource().equals(algoritmosDeExclusaoMutua.getItems().get(1)))
      trilho.setTipoDeAlgoritmo(2);
    
    else
      trilho.setTipoDeAlgoritmo(3);
      
    reset(null);//reseta os trens
  }//fim do metodo alteraAlgoritmoDeExclusaoMutua
}//fim da classe Controller
