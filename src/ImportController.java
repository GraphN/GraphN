import Algorithms.*;
import Algorithms.Utils.Step;
import graph.Graph;
import graph.Serialisation.*;
import graph.Stockage.AdjacencyStockage;
import graph.Stockage.EdgeListStockage;
import graph.Stockage.StockageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

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
    private String algorithme = "";
    private String structure = "";


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
        List<String> algo = Arrays.asList("BFS", "DFS", "Kruskal", "Dijkstra", "Prim", "Bellman-Ford");
        for(String s:algo)
            choiceAlgo.getItems().add(s);
        choiceAlgo.setValue(algo.get(0));
        hideEndVertex();

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
        List<String> structures = Arrays.asList("EdgesList", "AdjacencyList");
        for(String s:structures)
            choiceStructure.getItems().add(s);
        choiceStructure.setValue(structures.get(0));

        choiceStructure.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                structure = choiceStructure.getValue();
            }
        });
    }

    @FXML
    private void handleApply(){
        int start = 0;
        int end = 0;
        //System.out.println(startVertex.getText());
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


        StockageType stockage = null;
        switch (choiceStructure.getValue()){
            case "EdgesList":
                stockage = new EdgeListStockage();
                break;
            case "AdjacencyList":
                stockage = new AdjacencyStockage();
                break;
            default:
                System.err.println("Unknown Stockage Type !");
                return;
        }

        Graph g = serialiseur.importGraph(fileOpen.getAbsolutePath(), stockage);

        Algorithm algo = null;
        switch (choiceAlgo.getValue()){
            case "BFS":
                algo = new BFS(g, g.getVertex(start));
                break;
            case "DFS":
                algo = new DFS(g, g.getVertex(start));
                break;
            case "Kruskal":
                algo = new Kruskall(g);
                break;
            case "Prim":
                algo = new Prim(g);
                break;
            case "Bellman-Ford":
                algo = new Bellman_Ford(g, g.getVertex(start), g.getVertex(end));
                break;
            case "Dijkstra":
                algo = new Dijkstra(g, g.getVertex(start), g.getVertex(end));
                break;
            default:
                System.err.println("Algorithme Not found !!");
        }

        try{
            LinkedList<Step> path = algo.getPath();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setHeaderText("The algorithm pass without problems");
            alert.setContentText("Do you want to save the result?");
            ButtonType buttonTypeSave = new ButtonType("Save");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeCancel);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("assets/css/alertGood.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == buttonTypeSave){
                handleSave();
            }

            if (fileSave != null) {
                serialiseur.exportGraph(g, path, fileSave.getAbsolutePath());
            }
        }catch(Exception e){
            alertMessage(e.getMessage());
        }

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
        fileChooser.getExtensionFilters().add(csvFilter);
        fileChooser.getExtensionFilters().add(txtFilter);

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
