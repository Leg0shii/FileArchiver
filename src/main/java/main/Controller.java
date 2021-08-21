package main;

import javafx.scene.control.*;
import panes.Archiver;
import panes.DataLoader;
import panes.DataRoute;
import util.Dir;

import java.io.File;

public class Controller {

      //archiver
      public TextField tfield1;
      public Label label1;
      public ProgressBar progressBar;

      //data route
      public TextField from_lc;
      public TextField to_lc;
      public TextField from_km;
      public TextField to_km;
      public Button finddata;
      public ProgressBar progressBar_crd;
      public Label label_cdr;
      public TextField saveFolder;
      public TextField searchPath;
      public CheckBox cb_cdr;

      //data loader
      public Label label_ld;
      public TextField dir_ld;
      public ProgressBar progressBar_ld;
      public TextField text_x_ld;
      private boolean statusTable = false;

      //dev mode
      public Button button_dm;
      public ProgressBar progressBar_dm;
      public CheckBox checkbox_nul_dm;
      public TextField dir_dm;
      public Label label_dm;


      public void archiverClick() {

            progressBar.setProgress(0);
            String path = tfield1.getText();
            File startFile = new File(path);
            if (!Dir.fileExists(label1, startFile)) return;
            label1.setText("Starte...");

            Archiver task = new Archiver(startFile);
            progressBar.progressProperty().bind(task.progressProperty());
            label1.textProperty().bind(task.messageProperty());
            new Thread(task).start();
      }

      public void dataLoad() {
            String path = dir_ld.getText();
            String tablePath = text_x_ld.getText();

            File tableFileKM = new File(path);
            File tableFile = new File(tablePath);

            if ((!tableFile.exists()) && (!tableFileKM.exists())) {
                  System.out.println(tablePath + " doesnt exist!");
                  statusTable = false;
                  return;
            } else statusTable = true;

            System.out.println(tablePath + " File Exists (LC)!");
            System.out.println(tablePath + " File Exists (KM)!");

            DataLoader dataLoader = new DataLoader(tableFile, tableFileKM);
            progressBar_ld.progressProperty().bind(dataLoader.progressProperty());
            label_ld.textProperty().bind(dataLoader.messageProperty());
            new Thread(dataLoader).start();
      }

      public void findRouteData() {

            progressBar.setProgress(0);
            String lc_start = from_lc.getText();
            String lc_end = to_lc.getText();
            File saveFile = new File(saveFolder.getText());
            File searchFile = new File(searchPath.getText());
            DataRoute dataRoute = new DataRoute(searchFile, saveFile, lc_start, lc_end, "0", "0", false);

            if(cb_cdr.isSelected()) {
                  dataRoute = new DataRoute(searchFile, saveFile, lc_start, lc_end, from_km.getText(), to_km.getText(), true);
            }

            progressBar_crd.progressProperty().bind(dataRoute.progressProperty());
            label_cdr.textProperty().bind(dataRoute.messageProperty());
            new Thread(dataRoute).start();
      }

      public void devModeClick() {

      }

}
