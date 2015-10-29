package pl.edu.agh.zbyszek.display;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.edu.agh.zbyszek.display.controller.DisplayController;

import java.io.IOException;
import java.net.URL;

public class Display extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        URL location = getClass().getResource("view/Display.fxml");
        FXMLLoader loader = new FXMLLoader(location);

        Pane root=null;
        try {
            root = (Pane)loader.load(location);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene=new Scene(root,1000,400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
