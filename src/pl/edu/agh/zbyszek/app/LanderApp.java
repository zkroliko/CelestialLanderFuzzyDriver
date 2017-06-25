package pl.edu.agh.zbyszek.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LanderApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        URL location = getClass().getResource("view/Display.fxml");
        FXMLLoader loader = new FXMLLoader(location);

        try {
            Pane root = FXMLLoader.load(location);
            Scene scene=new Scene(root,1000,400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
