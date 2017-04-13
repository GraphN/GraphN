import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by LBX on 03/04/2017.
 */
public class AlgorithmPageController
{
    private MainApp mainApp;
    private GraphDom graph;

    @FXML
    private AnchorPane centerAlgoPage;

    public void setGraph(GraphDom g)
    {
        centerAlgoPage.getChildren().add(recreateAnchorWithXML(g));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize()
    {
        initZoom();
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

    private AnchorPane recreateAnchorWithXML(GraphDom graph)
    {
        AnchorPane pane = new AnchorPane();

        //adding all vertex from
        for(int i = 0; i < graph.getNbVertex(); i++)
        {
            int x = graph.getPosXOfVertex(i);
            int y = graph.getPosYOfVertex(i);
            String name = graph.getVertexName(i);
            //adding vertex created to pane
            pane.getChildren().add(createVertex(x, y, name));
        }


       return pane;
    }

    public Circle createVertex(double x, double y, String id) {
        Circle circle_base = new Circle(10.0f, Color.web("da5630"));
        circle_base.setId(id);
        circle_base.setTranslateX(x);
        circle_base.setTranslateY(y);
        return circle_base;
    }
}
