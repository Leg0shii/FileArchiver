package de.legoshi.main;

import de.legoshi.util.LoadingState;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import de.legoshi.panes.Archiver;
import de.legoshi.panes.DataLoader;
import de.legoshi.panes.DataRoute;
import de.legoshi.util.DirHelper;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    
    //archiver
    public TextField tfield1;
    public Label archLog;
    public ProgressBar progressBar;
    
    //data route
    public TextField lcEntryStart;
    public TextField lcEntryEnd;
    public TextField kmEntryStart;
    public TextField kmEntryEnd;
    
    public Button searcherButton;
    public ProgressBar searcherProgressbar;
    
    public TextField searchPath;
    public TextField saveFolder;
    
    public Label searcherLog;
    
    //data loader
    public Label loadLog;
    public TextField loadDirKM;
    public TextField loadDirDGN;
    public Button loadButton;
    public ProgressBar loadProgressBar;
    
    public GridPane startGrid;
    public GridPane archiveGrid;
    public GridPane searcherSelectGrid;
    public GridPane loadFilesGrid;
    public GridPane searcherGrid;
    
    // public Label loadLog;
    private boolean selectedKM = false;
    private boolean selectedDGN = false;
    
    private boolean tableKM = false;
    private boolean tableDGN = false;
    
    //buttons
    public Button searchDataButton;
    public Button archiveButton;
    public Button creditsButton;
    
    //credits
    public GridPane creditsGrid;
    
    private boolean isRunning = false;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startGrid.setVisible(true);
        loadProgressBar.setProgress(0);
    }
    
    public void switchToMenu() {
        if (!isRunning) {
            unloadScreen();
            startGrid.setVisible(true);
            selectedDGN = false;
            selectedKM = false;
            
            searcherLog.labelForProperty().unbind();
            searcherProgressbar.progressProperty().unbind();
            loadLog.textProperty().unbind();
            loadProgressBar.progressProperty().unbind();
            archLog.labelForProperty().unbind();
            progressBar.progressProperty().unbind();
            
            archLog.setText("");
            searcherLog.setText("");
            loadLog.setText("");
            
            lcEntryStart.setText("");
            lcEntryEnd.setText("");
            kmEntryStart.setText("");
            kmEntryEnd.setText("");
            saveFolder.setText("");
        }
    }
    
    public void openPaypal() {
        if (!isRunning) {
            try {
                Desktop.getDesktop().browse(new URL("https://www.paypal.com/donate/?hosted_button_id=7Z769V5MYSC9G").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void copyBTC() {
        if (!isRunning) {
            StringSelection stringSelection = new StringSelection("bc1qr8emdvwmqjl3zvrxc5v05d8sguc8vypre3ujhq");
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }
    
    public void switchToArchiver() {
        if (!isRunning) {
            unloadScreen();
            archiveGrid.setVisible(true);
        }
    }
    
    public void switchToSearcher() {
        if (!isRunning) {
            unloadScreen();
            searcherSelectGrid.setVisible(true);
        }
    }
    
    public void switchToCredits() {
        unloadScreen();
        creditsGrid.setVisible(true);
    }
    
    public void unloadScreen() {
        if (!isRunning) {
            startGrid.setVisible(false);
            archiveGrid.setVisible(false);
            searcherSelectGrid.setVisible(false);
            loadFilesGrid.setVisible(false);
            searcherGrid.setVisible(false);
            creditsGrid.setVisible(false);
            
            loadDirDGN.setDisable(false);
            loadDirKM.setDisable(false);
        }
    }
    
    public void switchToSearcherDGN() {
        if (!isRunning) {
            unloadScreen();
            
            selectedDGN = true;
            if (tableDGN) loadSearcherGrid();
            else {
                loadFilesGrid.setVisible(true);
                loadDirKM.setDisable(true);
            }
        }
    }
    
    public void switchToSearcherKM() {
        if (!isRunning) {
            unloadScreen();
            
            selectedKM = true;
            if (tableKM) loadSearcherGrid();
            else loadFilesGrid.setVisible(true);
        }
    }
    
    private void loadSearcherGrid() {
        if (!isRunning) {
            unloadScreen();
            searcherGrid.setVisible(true);
            if (selectedDGN) {
                lcEntryStart.setPromptText("Von: 0000AA");
                lcEntryEnd.setPromptText("Bis: 9999ZZ");
                kmEntryStart.setPromptText("");
                kmEntryEnd.setPromptText("");
                kmEntryStart.setDisable(true);
                kmEntryEnd.setDisable(true);
            } else {
                lcEntryStart.setPromptText("Von: 0000");
                lcEntryEnd.setPromptText("Bis: 9999");
                kmEntryStart.setPromptText("Von: -100");
                kmEntryEnd.setPromptText("Bis: 100");
                kmEntryStart.setDisable(false);
                kmEntryEnd.setDisable(false);
            }
        }
    }
    
    public void archiverClick() {
        if (!isRunning) {
            progressBar.setProgress(0);
            String path = tfield1.getText();
            File startFile = new File(path);
            if (!DirHelper.fileExists(archLog, startFile)) return;
            
            archLog.setText("Starte...");
            
            Archiver task = new Archiver(startFile);
            progressBar.progressProperty().bind(task.progressProperty());
            archLog.textProperty().bind(task.messageProperty());
            
            isRunning = true;
            new Thread(task).start();
            task.setOnSucceeded(e -> {
                isRunning = false;
                
                archLog.labelForProperty().unbind();
                progressBar.progressProperty().unbind();
            });
        }
    }
    
    public void dataLoad() {
        if (!isRunning) {
            DataLoader dataLoader = new DataLoader();
            dataLoader.setFileDGN(new File(loadDirDGN.getText()));
            dataLoader.setFileKM(new File(loadDirKM.getText()));
            
            loadLog.textProperty().bind(dataLoader.messageProperty());
            loadProgressBar.progressProperty().bind(dataLoader.progressProperty());
            
            if (tableKM) {
                loadSearcherGrid();
                return;
            }
            
            if (selectedDGN && tableDGN) {
                loadSearcherGrid();
                return;
            }
            
            dataLoader.setDgn(!tableDGN);
            dataLoader.setKm(selectedKM);
            
            isRunning = true;
            new Thread(dataLoader).start();
            dataLoader.setOnSucceeded(e -> {
                if (dataLoader.getValue() == LoadingState.DGN_SUCCESS) tableDGN = true;
                else if (dataLoader.getValue() == LoadingState.KM_SUCCESS) tableKM = true;
                else if (dataLoader.getValue() == LoadingState.SUCCESS) {
                    tableKM = true;
                    tableDGN = true;
                }
                
                isRunning = false;
                
                if (selectedDGN && tableDGN) loadSearcherGrid();
                else if (selectedKM && tableDGN && tableKM) loadSearcherGrid();
                
                loadLog.labelForProperty().unbind();
                loadProgressBar.progressProperty().unbind();
            });
        }
    }
    
    public void findRouteData() {
        if (!isRunning) {
            progressBar.setProgress(0);
            String lcStart = lcEntryStart.getText();
            String lcEnd = lcEntryEnd.getText();
            
            File saveFile = new File(saveFolder.getText());
            File searchFile = new File(searchPath.getText());
            
            DataRoute dataRoute;
            if (selectedKM) {
                String kmStart = kmEntryStart.getText();
                String kmEnd = kmEntryEnd.getText();
                dataRoute = new DataRoute(searchFile, saveFile, lcStart, lcEnd, kmStart, kmEnd);
            } else dataRoute = new DataRoute(searchFile, saveFile, lcStart, lcEnd);
            searcherLog.textProperty().bind(dataRoute.messageProperty());
            searcherProgressbar.progressProperty().bind(dataRoute.progressProperty());
            
            isRunning = true;
            new Thread(dataRoute).start();
            dataRoute.setOnSucceeded(e -> {
                isRunning = false;
                searcherLog.textProperty().unbind();
                searcherProgressbar.progressProperty().unbind();
                Platform.runLater(() -> {
                    lcEntryStart.setText("");
                    lcEntryEnd.setText("");
                    kmEntryStart.setText("");
                    kmEntryEnd.setText("");
                    saveFolder.setText("");
                    searcherProgressbar.setProgress(0);
                });
            });
        }
    }
}
