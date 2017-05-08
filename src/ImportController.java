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
    @FXML
    private Button saveButton;
    @FXML
    private ChoiceBox<String> choiceButton;

    @FXML
    void initialize(){
        List<String> algo = Arrays.asList("BFS", "DFS");
        for(String s:algo)
            choiceButton.getItems().add(s);
        choiceButton.setValue(algo.get(0));

        choiceButton.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                switch(choiceButton.getValue()){
                    case "DFS": saveButton.setVisible(true);
                        break;
                    case "BFS": saveButton.setVisible(false);
                        break;
                }
            }
        });
    }
    @FXML
    private void handleOk(){
        //fileOpen
        //fileSave
    }
    @FXML
    private void handleOpen(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph");

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        //set initial directory
        File directory = new File("./src/savedGraphsXML");
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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        //set initial directory
        File directory = new File("./src/savedGraphsXML");
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
