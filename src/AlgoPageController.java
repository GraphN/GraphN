import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

/**
 * Created by LBX on 03/04/2017.
 */
public class AlgoPageController
{
    private MainApp mainApp;
    private AnchorPane graph;
    @FXML
    private AnchorPane centerAlgoPage;
    public void setGraph(AnchorPane g)
    {
        graph = g;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize()
    {
        initZoom();
        Slider slider = new Slider();
        centerAlgoPage.getChildren().add(slider);
    }
    @FXML
    private Slider slider;

    @FXML
    private void handleNewFromResult(){
        mainApp.newTab();
    }

    @FXML
    private void handlePlay(){
    }
    @FXML
    private void handlePause(){
    }
    @FXML
    private void handleStop(){
    }
    @FXML
    private void handleFastForward(){
    }
    @FXML
    private void handlePausePlay(){
    }
    @FXML
    private void handleDFS(){
    }
    @FXML
    private void handleBFS(){
    }
    private void initZoom(){
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                // Do something
                System.out.println(new_val);
            }
        });
    }
}
