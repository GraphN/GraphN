import Algorithms.Algorithm;
import Algorithms.BFS;
import graph.Graph;
import graph.Serialisation.ListEdgesTXT;
import graph.Stockage.AdjacencyStockage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
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

    @FXML
    private Button saveButton;
    @FXML
    private ChoiceBox<String> choiceButton;

    @FXML
    void initialize(){
        List<String> algo = Arrays.asList("BFS", "DFS", "Kruskall");
        for(String s:algo)
            choiceButton.getItems().add(s);
        choiceButton.setValue(algo.get(0));

        choiceButton.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                switch(choiceButton.getValue()){
                    case "DFS": saveButton.setVisible(true);
                        algorithme = "DFS";
                        break;
                    case "BFS": saveButton.setVisible(true);
                        algorithme = "BFS";
                        break;
                    case "Kruskall": saveButton.setVisible(true);
                        algorithme = "Kruskall";
                        break;
                }
            }
        });
    }

    @FXML
    private void handleOk(){
        //fileOpen
        ListEdgesTXT serialiseur = new ListEdgesTXT();
        Graph g = serialiseur.importGraph(fileOpen.getAbsolutePath(), new AdjacencyStockage());

        Algorithm algo = null;
        switch (algorithme){
            case "BFS":
                algo = new BFS(g, g.getVertex(0));
                break;
            case "DFS":
                algo = new BFS(g, g.getVertex(0));
                break;
            case "Kruskall":
                algo = new BFS(g, g.getVertex(0));
                break;
            default:
                System.out.println("Algo Not find !!");
                    // TODO: Error message
        }

        serialiseur.exportGraph(g, algo.getPath(), fileSave.getAbsolutePath());

    }
    @FXML
    private void handleOpen(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph");

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

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
    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }

}
