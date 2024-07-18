/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 22/03/2024
* Ultima alteracao.: 28/03/2024
* Nome.............: Trens Paralelos
* Funcao...........: Classe utilizada para iniciar o programa e exibir a GUI inicial.
*************************************************************** */
import controller.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

public class Principal extends Application
{
  /* ***************************************************************
  * Metodo: start
  * Funcao: Inicializa a GUI, carrega e exibe o SceneInicial para o usuario.
  * Parametros: stage = parametro Stage para armazenar e exibir os scenes.
  * Retorno: void
  *************************************************************** */
  @Override
  public void start(Stage stage)
    throws IOException
  {
    FXMLLoader loader = new FXMLLoader(Principal.class.getResource("view/SceneInicial.fxml"));// Carrega o  arquivo fxml
    Scene scene = new Scene(loader.load()); //Carrega a scene
    stage.setResizable(false);
    stage.setScene(scene);
    stage.setTitle("Trens Paralelos");
    stage.show();
  }// fim do metodo start
  
  /* ***************************************************************
  * Metodo: main
  * Funcao: chama o metodo que inicializa a GUI.
  * Parametros: args = argumentos de prompt, sem ultilidade para o este programa.
  * Retorno: void
  *************************************************************** */
  public static void main(String[] args)
  {
    SceneInicialController sceneInicialController;
    Controller controller;
    launch();
  }//fim do metodo main
  
}// fim da classe Principal
