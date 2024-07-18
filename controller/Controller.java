/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 22/03/2024
* Ultima alteracao.: 10/04/2024
* Nome.............: Trens Paralelos
* Funcao...........: Gerenciar e tratar os eventos dos botoes, sliders, e menus. Tambem tem a funcao de
  instanciar a classe Trilho.
*************************************************************** */
package controller;

import util.*;
import model.*;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.control.Slider;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.util.ResourceBundle;
import java.net.URL;


public class Controller implements Initializable {

  @FXML
  private Slider ajustaVelocidadeTrem1;

  @FXML
  private Slider ajustaVelocidadeTrem2;

  @FXML
  private MenuButton locaisIniciaisDosTrens;

  @FXML
  private HBox root; //raiz do scene
  
  @FXML
  private ImageView tabuaComPoster;
  
  private final int VELOCIDADE_INICIAL = 15;
  
  private MenuItem[] menuItens = new MenuItem[4];
  private Resolucao resolucao;
  private Trilho trilho;

  /* ***************************************************************
  * Metodo: initialize
  * Funcao: inicializa  e configura as variaveis necessarias para a GUI, antes da
    GUI ser carregada para o usuario
  * Parametros: url = util caso precise da localizacao do arquivo fxml, resourceBundle =
    resourceBundle associado ao arquivo fxml. Ambos necessarios para o Override
  * Retorno: void
  *************************************************************** */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) 
  {
    //Cria MenuItems, configura e os adicionam ao MenuButton
    for (int i = 0; i < menuItens.length; i++)
    {
      menuItens[i] = new MenuItem();
      menuItens[i].setOnAction(event -> {
        alteraLocalDeInicio(event);
      });
      menuItens[i].setStyle("-fx-background-color: #111111;");
      menuItens[i].setGraphic(new ImageView(String.format("img/SetaOpcao%d.png", i + 1)));
      locaisIniciaisDosTrens.getItems().add(menuItens[i]);
    }//fim do for
   
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
    
    tabuaComPoster.setImage(new Image(String.format("%sTabuaComPoster.png", resolucao.getImgDiretorio())));//configura imagens e
    root.getChildren().add(new ImageView(String.format("%sTabua.png", resolucao.getImgDiretorio())));     //adiciona ao layout
    
    root.setPrefHeight(resolucao.getAlturaMaxima());
    root.setPrefWidth(resolucao.getLarguraMaxima());
    
    trilho.iniciarThreads();//inicia as Threads que movimentam os trens                            
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
    ajustaVelocidadeTrem1.setValue(VELOCIDADE_INICIAL);//redefine o slider para o valor inicial
    ajustaVelocidadeTrem2.setValue(VELOCIDADE_INICIAL);
    trilho.getTrem1().setVelocidade(VELOCIDADE_INICIAL);//muda a velocidade do trem para a velocidade inicial do app
    trilho.getTrem2().setVelocidade(VELOCIDADE_INICIAL);
    trilho.resetarTrens();//metodo para resetar os trens
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
      if (event.getSource().equals(menuItens[0]))
      {
        trilho.getTrem1().setLocalDeInicio(LocalDeInicio.ESQUERDA_CIMA_RA);
        trilho.getTrem2().setLocalDeInicio(LocalDeInicio.DIREITA_CIMA_RA);
      }//fim do if
      
      else if (event.getSource().equals(menuItens[1]))
      {
        trilho.getTrem1().setLocalDeInicio(LocalDeInicio.ESQUERDA_CIMA_RA);
        trilho.getTrem2().setLocalDeInicio(LocalDeInicio.DIREITA_BAIXO_RA);
      }  //fim do else if
      
      else if(event.getSource().equals(menuItens[2]))
      {
        trilho.getTrem1().setLocalDeInicio(LocalDeInicio.ESQUERDA_BAIXO_RA);
        trilho.getTrem2().setLocalDeInicio(LocalDeInicio.DIREITA_CIMA_RA);
      }//fim do else if
    
      else
      {
        trilho.getTrem1().setLocalDeInicio(LocalDeInicio.ESQUERDA_BAIXO_RA);
        trilho.getTrem2().setLocalDeInicio(LocalDeInicio.DIREITA_BAIXO_RA);
      }//fim do else
    }//fim do if
    
    else
    {
      //quatro opcoes distintas, acionadas de acordo com o menu item clicado pelo usuario
      if (event.getSource().equals(menuItens[0]))//apenas se o usuario escolheu o tipo de resolucao RM
      {
        trilho.getTrem1().setLocalDeInicio(LocalDeInicio.ESQUERDA_CIMA_RM);
        trilho.getTrem2().setLocalDeInicio(LocalDeInicio.DIREITA_CIMA_RM);
      }//fim do if
      
      else if (event.getSource().equals(menuItens[1]))
      {
        trilho.getTrem1().setLocalDeInicio(LocalDeInicio.ESQUERDA_CIMA_RM);
        trilho.getTrem2().setLocalDeInicio(LocalDeInicio.DIREITA_BAIXO_RM);
      }  //fim do else if
      
      else if(event.getSource().equals(menuItens[2]))
      {
        trilho.getTrem1().setLocalDeInicio(LocalDeInicio.ESQUERDA_BAIXO_RM);
        trilho.getTrem2().setLocalDeInicio(LocalDeInicio.DIREITA_CIMA_RM);
      }//fim do else if
    
      else
      {
        trilho.getTrem1().setLocalDeInicio(LocalDeInicio.ESQUERDA_BAIXO_RM);
        trilho.getTrem2().setLocalDeInicio(LocalDeInicio.DIREITA_BAIXO_RM);
      }//fim do else
    }//fim do else
    
    reset(null);//reseta os trens
    
  }//fim do metodo alteraLocalDeInicio
}//fim da classe Controller
