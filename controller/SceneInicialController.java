/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 28/03/2024
* Ultima alteracao.: 29/03/2024
* Nome.............: Trens Paralelos
* Funcao...........: Classe criada para configurar a resolucao do programa de acordo
  com a opcao escolhida pelo usuario.
*************************************************************** */
package controller;

import util.*;

import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.util.ResourceBundle;
import java.io.IOException;
import java.net.URL;

public class SceneInicialController implements Initializable
{
  @FXML
  private ChoiceBox<String> escolheResolucao; //Criado para receber o valor de resolucao escolhido pelo usuario.
  private Resolucao resolucao;//Objeto Resolucao para configurar o programa a partir da escolha da resolucao.
  private String resolucaoMedia;//opcoes de resolucao.
  private String resolucaoAlta;


  /* ***************************************************************
  * Metodo: startApp
  * Funcao: Iniciar as funcoes do aplicativo com base na resolucao escolhida.
  * Parametros: event = evento de clique de mouse.
  * Retorno: void
  *************************************************************** */
  @FXML
  void startApp(MouseEvent event)
  {
    //tratamento de excecao caso o arquivo fxml no metodo swapScene seja deletado ou corrompido.
    try 
    {
      setResolucao();
      swapScene();
    }//fim do try
    catch(IOException exception)
    {
      System.out.println("Arquivo nao encontrado, Finalizando!");
      System.exit(1);
    }//fim do catch
  }
  
  /* ***************************************************************
  * Metodo: setResolucao
  * Funcao: Cria um objeto Resolucao com base no valor escolhido no ChoiceBox.
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void setResolucao()
  {
    if (escolheResolucao.getValue().equals(resolucaoAlta))
      resolucao = new Resolucao("RA");//RA = resolucao alta
    else
      resolucao = new Resolucao("RM");//RM = resolucao media
  }//fim do metodo setResolucao
  
  /* ***************************************************************
  * Metodo: swapScene
  * Funcao: Troca o Scene inicial pelo Scene principal no stage.
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void swapScene()
    throws IOException
  {
    Stage stage = (Stage) escolheResolucao.getScene().getWindow();//Cria uma referencia ao stage da GUI.
    
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ScenePrincipal.fxml"));// Carrega o arquivo fxml.
    Scene scene = new Scene(loader.load()); //Cria a scene com base no arquivo fxml.
    
    Controller controller = loader.getController();//Cria uma referencia para o Controller, e
    controller.configuraResolucao(resolucao);     //Configura o Programa com base na resolucao.
    
    stage.setScene(scene); //Muda o Scene atual para o Scene criado
    stage.setX(250);
    stage.setY(0);
  }//fim do metodo swapScene
  
  /* ***************************************************************
  * Metodo: initialize
  * Funcao: Configura o ChoiceBox para que apareca as opcoes corretas, antes que ele seja
    exibido para o usuario
  * Parametros: url = util caso precise da localizacao do arquivo fxml, resourceBundle =
    resourceBundle associado ao arquivo fxml, necessarios para sobrecrever o metodo initializable
  * Retorno: void
  *************************************************************** */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) 
  {
    resolucaoMedia = "880 X 720";
    resolucaoAlta = "1000 X 900";
    escolheResolucao.getItems().addAll(resolucaoMedia, resolucaoAlta);
    escolheResolucao.setValue(resolucaoMedia);
  }//fim do metodo initialize
}
