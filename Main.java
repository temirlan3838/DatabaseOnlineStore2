package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("As4");
        primaryStage.setScene(new Scene(root, 760, 600));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
