import Algorithms.*;
import graph.Graph;
import graph.Serialisation.*;
import graph.Stockage.AdjacencyStockage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LBX on 08/05/2017.
 */
public class ImportController {

    MainApp mainApp;
    private File fileOpen;
    private File fileSave;
    private String algorithme = "";
    private String structure = "";


    @FXML
    private TextField startVertex;
    @FXML
    private Button saveButton;
    @FXML
    private ChoiceBox<String> choiceAlgo;
    @FXML
    private ChoiceBox<String> choiceStructure;

    @FXML
    void initialize(){
        List<String> algo = Arrays.asList("BFS", "DFS", "Kruskal", "Dijkstra", "Prim", "Bellman-Ford");
        for(String s:algo)
            choiceAlgo.getItems().add(s);
        choiceAlgo.setValue(algo.get(0));

        choiceAlgo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                algorithme = choiceAlgo.getValue();
            }
        });
        List<String> structures = Arrays.asList("AdjacencyList", "EdgesList");
        for(String s:structures)
            choiceStructure.getItems().add(s);
        choiceStructure.setValue(algo.get(0));

        choiceStructure.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                structure = choiceStructure.getValue();
            }
        });
    }

    @FXML
    private void handleApply(){

        Serialiseur serialiseur = null;
        //fileOpen
        switch (fileOpen.getAbsolutePath().substring(fileOpen.getAbsolutePath().lastIndexOf('.') + 1)) {
            case "csv":
                serialiseur = new ListEdgesCSV();
                break;
            case "txt":
                serialiseur = new ListEdgesTXT();
                break;
            default:
                System.err.println("Wrong input type file !");
        }

        Graph g = serialiseur.importGraph(fileOpen.getAbsolutePath(), new AdjacencyStockage()); // TODO : Let the client choose the Stockage type

        Algorithm algo = null;
        switch (algorithme){
            case "BFS":
                algo = new BFS(g, g.getVertex(0));
                break;
            case "DFS":
                algo = new DFS(g, g.getVertex(0));
                break;
            case "Kruskal":
                algo = new Kruskall(g);
                break;
            case "Prim":
                algo = new Prim(g);
                break;
            case "Bellman-Ford":
                algo = new Bellman_Ford(g, g.getVertex(0), g.getVertex(1));// TODO : Let the client choose strt and end vertex
                break;
            case "Dijkstra":
                algo = new Dijkstra(g, g.getVertex(0), g.getVertex(1)); // TODO : Let the client choose strt and end vertex
                break;
            default:
                System.err.println("Algorithme Not find !!");
        }

        serialiseur.exportGraph(g, algo.getPath(), fileSave.getAbsolutePath());

    }
    @FXML
    private void handleOpen(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph");

        //Set extension filter
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(txtFilter);
        fileChooser.getExtensionFilters().add(csvFilter);

        //set initial directory
        File directory = new File("./DataTest");
        fileChooser.setInitialDirectory(directory);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            fileOpen = file;
        }
    }
    @FXML
    private void handleSave(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Graph");

        //Set extension filter
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(txtFilter);
        fileChooser.getExtensionFilters().add(csvFilter);

        //set initial directory
        File directory = new File("./DataTest");
        fileChooser.setInitialDirectory(directory);

        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            fileSave = file;
            /*try {
                graph.saveGraphXML(file);
            } catch (TransformerException e) {
                e.printStackTrace();
            }*/
        }
    }

    void alertMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("assets/css/alert.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
    }

    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }

}
