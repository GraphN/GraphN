package view;

import Algorithms.*;
import Algorithms.Utils.Step;
import graph.Graph;
import graph.Serialisation.*;
import graph.Stockage.AdjacencyStockage;
import graph.Stockage.EdgeListStockage;
import graph.Stockage.StockageType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import mainProgram.MainApp;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by LBX on 08/05/2017.
 */
public class ImportController {

    MainApp mainApp;
    private File fileOpen;
    private File fileSave;
    LinkedList<Step> path;


    @FXML
    private TextField startVertex;
    @FXML
    private Label startLabel;
    @FXML
    private Separator startSeparator;
    @FXML
    private TextField endVertex;
    @FXML
    private Label endingLabel;
    @FXML
    private Separator endingSeparator;
    @FXML
    private Button saveButton;
    @FXML
    private ChoiceBox<String> choiceAlgo;
    @FXML
    private ChoiceBox<String> choiceStructure;

    @FXML
    void initialize(){
        // Set the alorithm name in the ChoiceBox
        List<String> algo = Arrays.asList("BFS", "DFS", "Kruskal", "Dijkstra", "Prim", "Bellman-Ford");
        for(String s:algo)
            choiceAlgo.getItems().add(s);
        choiceAlgo.setValue(algo.get(0));
        hideEndVertex();

        // Hide or show the TextFields for the selection of the start and end vertex
        choiceAlgo.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            switch (algo.get(observableValue.getValue().intValue())){
                case "BFS":
                    hideEndVertex();
                    showStartVertex();
                    break;
                case "DFS":
                    hideEndVertex();
                    showStartVertex();
                    break;
                case "Kruskal":
                    hideEndVertex();
                    hideStartVertex();
                    break;
                case "Prim":
                    hideEndVertex();
                    hideStartVertex();
                    break;
                case "Bellman-Ford":
                    showEndVertex();
                    showStartVertex();
                    break;
                case "Dijkstra":
                    showEndVertex();
                    showStartVertex();
                    break;
                default:
                    System.err.println("Algorithm Not found !!");
            }
        });
        // Set the savetype name in a ChoiceBox
        List<String> structures = Arrays.asList("EdgesList", "AdjacencyList");
        for(String s:structures)
            choiceStructure.getItems().add(s);
        choiceStructure.setValue(structures.get(0));
        startVertex.setText("0");
        endVertex.setText("0");
    }

    private void showEndVertex(){
        endingLabel.setVisible(true);
        endingSeparator.setVisible(true);
        endVertex.setVisible(true);
    }
    private void hideEndVertex(){
        endingLabel.setVisible(false);
        endingSeparator.setVisible(false);
        endVertex.setVisible(false);
    }
    private void showStartVertex(){
        startLabel.setVisible(true);
        startSeparator.setVisible(true);
        startVertex.setVisible(true);
    }
    private void hideStartVertex(){
        startLabel.setVisible(false);
        startSeparator.setVisible(false);
        startVertex.setVisible(false);
    }

    /**
     * Open a window fot the selection of the .csv or .txt file that contain a graph
     */
    @FXML
    private void handleOpen(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph");

        //Set extension filter
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(csvFilter);
        fileChooser.getExtensionFilters().add(txtFilter);

        //set initial directory
        File directory = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        fileChooser.setInitialDirectory(directory);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            fileOpen = file;
        }
    }

    /**
     *  Get all the informations and apply the algorithm
     */
    @FXML
    private void handleApply(){
        int start = 0;
        int end = 0;

        if(!startVertex.getText().equals(""))
            start = Integer.parseInt(startVertex.getText().replaceAll("[\\D]", ""));
        if(!endVertex.getText().equals(""))
            end = Integer.parseInt(endVertex.getText().replaceAll("[^0-9]", ""));

        Serialiseur serialiseur = null;
        //fileOpen
        if(fileOpen==null){
            alertMessage("Open a file before apply the algorithm");
            return;
        }
        // Get the informations whether it's a .csv or a .txt
        serialiseur = getSerialiseur(fileOpen);
        if (serialiseur == null ) return;

        // Get the stockage type
        StockageType stockage;
        switch (choiceStructure.getValue()){
            case "EdgesList":
                stockage = new EdgeListStockage();
                break;
            case "AdjacencyList":
                stockage = new AdjacencyStockage();
                break;
            default:
                alertMessage("Unknown Stockage Type !");
                return;
        }

        // Build the graph
        Graph g;
        try{
            g = serialiseur.importGraph(fileOpen.getAbsolutePath(), stockage);
        }catch (Exception e){
            e.printStackTrace();
            alertMessage(e.getMessage());
            return;
        }

        // Get the algorighm choosen and apply it
        try{
            switch (choiceAlgo.getValue()){
                case "BFS":
                    path = g.accept(new BFS(), g.getVertex(start), null);
                    break;
                case "DFS":
                    path = g.accept(new DFS(), g.getVertex(start), null);
                    break;
                case "Kruskal":
                    path = g.accept(new Kruskall(), null, null);
                    break;
                case "Prim":
                    path = g.accept(new Prim(), null, null);
                    break;
                case "Bellman-Ford":
                    path = g.accept(new Bellman_Ford(), g.getVertex(start), g.getVertex(end));
                    break;
                case "Dijkstra":
                    path = g.accept(new Dijkstra(), g.getVertex(start), g.getVertex(end));
                    break;
                default:
                    alertMessage("Algorithme Not found !!");
                    return;
            }
            alertSuccess(g);

        }catch(Exception e){
            e.printStackTrace();
            alertMessage(e.getMessage());
        }

    }

    /**
     * Open a window for saving in txt or csv
     */
    @FXML
    private void handleSave(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Graph");

        //Set extension filter
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(csvFilter);
        fileChooser.getExtensionFilters().add(txtFilter);

        //set initial directory
        File directory = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        fileChooser.setInitialDirectory(directory);

        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            fileSave = file;
        }
    }

    /**
     * Create en alert that said that the program worked and ask for saving the result..
     * @param g the graph
     */
    void alertSuccess(Graph g){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save");
        alert.setHeaderText("The algorithm pass without problems");
        alert.setContentText("Do you want to save the result?");
        ButtonType buttonTypeSave = new ButtonType("Save");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeCancel);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/assets/css/alertGood.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeSave){
            handleSave();
        }
        // Save the subgraph
        if (fileSave != null) {
            Serialiseur serialiseur = getSerialiseur(fileOpen);
            if (serialiseur == null ) return;
            serialiseur.exportGraph(g, path, fileSave.getAbsolutePath());
        }
    }

    void alertMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/assets/css/alert.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
    }

    /**
     * Determine whether the file is a csv or txt
     * @param file
     * @return a Serializer for the right type of file
     */
    private Serialiseur getSerialiseur(File file){
        Serialiseur serialiseur;
        switch (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.') + 1)) {
            case "csv":
                serialiseur = new ListEdgesCSV();
                break;
            case "txt":
                serialiseur = new ListEdgesTXT();
                break;
            default:
                alertMessage("Wrong input type file !");
                return null;
        }
        return serialiseur;
    }

    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }

}
