package de.legoshi.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

      //TODO : close Thread when stopping
      //TODO : nothing to do: waiting forever (cancel thread)

      public static FileData fileData;

      public static void main(String[] args) {
            launch(args);
      }

      @Override
      public void start(Stage primaryStage) {
            fileData = new FileData();
            try {
                  // System.out.println("PAAAAAAAAAATH: " + new File("../").getAbsolutePath());
                  Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
                  primaryStage.setResizable(false);
                  primaryStage.setTitle("DB-Helfer");
                  primaryStage.setScene(new Scene(root, 500, 300));
                  primaryStage.show();
            } catch (Exception e) {
                  e.printStackTrace();
            }
      }

}
