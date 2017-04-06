/**
 * Created by LBX on 31/03/2017.
 */

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MainPageController {
    private MainApp mainApp;
    @FXML
    private TabPane tabPane;

    private int indiceTab;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;

    public MainPageController(){
        indiceTab = 0;
    }
    @FXML
    private void initialize() {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        DraggingTabPaneSupport support = new DraggingTabPaneSupport();
        support.addSupport(tabPane);
    }
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private void handleLaunch(){
        mainApp.showAlgoPage();
    }
    @FXML
    private void handleNew(){
        Tab tab = new Tab();
        tab.setText("new tab"+ indiceTab++);
        // Ajouter tout ce qu'on veut dans la  tab
        AnchorPane pane = new AnchorPane();
        tab.setContent(pane);
        tabPane.getTabs().add(tab);

        // Add Vertex
        Circle circle_Red = new Circle(50.0f, Color.RED);
        circle_Red.setCenterX(pane.getLayoutX());
        circle_Red.setCenterY(pane.getLayoutY());
        circle_Red.setOnMousePressed(circleOnMousePressedEventHandler);
        circle_Red.setOnMouseDragged(circleOnMouseDraggedEventHandler);

        Group root = new Group();
        root.getChildren().add(circle_Red);
        pane.getChildren().add(root);
    }

    public void handleNewFromAlgoPage(){
        handleNew();
    }
    @FXML
    private void handleSave(){
    }
    @FXML
    private void handleOpen(){
    }
    @FXML
    private void handleImport(){
    }
    @FXML
    private void handleVectrice(){
    }
    @FXML
    private void handleVectrice2(){
    }
    @FXML
    private void handleEdge(){
    }
    @FXML
    private void handleDiEdge(){
    }
    @FXML
    private void handleDiEdgeWeighted(){
    }
    @FXML
    private void handleText(){
    }
    @FXML
    private void handleNote(){
    }

    EventHandler<MouseEvent> circleOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
                    orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    System.out.println(tabPane.getWidth()-((Circle)(t.getSource())).getRadius());
                    if(newTranslateX < 0)
                        newTranslateX = 0;

                    if(newTranslateX > tabPane.getWidth())
                        newTranslateX = tabPane.getWidth();

                    ((Circle)(t.getSource())).setTranslateX(newTranslateX);
                    ((Circle)(t.getSource())).setTranslateY(newTranslateY);
                }
            };


}
