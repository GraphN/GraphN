/**
 * Created by LBX on 31/03/2017.
 */

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.swing.text.html.ImageView;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainPageController {
    private MainApp mainApp;
    private boolean vertex1Active = false;
    private boolean vertex1ActiveOnce = false;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button vertex1Button;

    @FXML
    private AnchorPane greyMain;
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
    public void setGreyMain(Boolean b)
    {
        greyMain.setVisible(b);
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
        //mouselistener to add vertex etc
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                panePressed(event);
            }
        });
        // Slider of the current tab created
        Slider slider = new Slider();
        AnchorPane.setRightAnchor(slider, 2.0);
        AnchorPane.setBottomAnchor(slider, 5.0);
        pane.getChildren().add(slider);

        tab.setContent(pane);
        tabPane.getTabs().add(tab);

        // Add Vertex
       /* Circle circle_base = createVertex(100.0, 100.0);
        Group root = new Group();
        root.getChildren().add(circle_base);
        pane.getChildren().add(root);*/
    }

    public Circle createVertex(double x, double y)
    {
        Circle circle_base = new Circle(30.0f, Color.web("da5630"));
        circle_base.setTranslateX(x);
        circle_base.setTranslateY(y);
        circle_base.setOnMousePressed(circleOnMousePressedEventHandler);
        circle_base.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        circle_base.setOnMouseEntered(circleOnMouseEnteredEventHandler);
        circle_base.setOnMouseReleased(circleOnMouseReleasedEventHandler);
        return circle_base;
    }
    public void handleNewFromAlgoPage(){
        handleNew();
    }

    @FXML
    private void panePressed(MouseEvent mouseEvent){
        if(vertex1Active || vertex1ActiveOnce)
        {
            Circle circle = createVertex(mouseEvent.getX(), mouseEvent.getY());
            AnchorPane currPage = (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
            currPage.getChildren().add(circle);

            //if we have the click once. we desactivate it
            vertex1ActiveOnce = false;
            if(!vertex1Active)
                vertex1Button.setId("vertex1Button");//let the butonn orange if he is used
        }

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
    private void handleVectrice(MouseEvent mouseEvent) {
        int count = mouseEvent.getClickCount();
        if(count == 2) // if we double click, we can put infinite vertex
        {
            vertex1Active = true;
            vertex1ActiveOnce = false;
            vertex1Button.setId("vertex1ButtonActivate");//let the butonn orange if he is used
        }
        else if(count == 1 && (vertex1ActiveOnce||vertex1Active) )// if we click and if we are activate, we desactive
        {
            vertex1Active = false;
            vertex1ActiveOnce = false;
            vertex1Button.setId("vertex1Button");// desactivate button of orange

        }
        else if(count == 1 && (!vertex1ActiveOnce||!vertex1Active) )// if we clicked and we are desactivate, we active
        {
            vertex1ActiveOnce = true;
            vertex1Active = false;
            vertex1Button.setId("vertex1ButtonActivate");//let the butonn orange if he is used
        }

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

    EventHandler<MouseEvent> circleOnMouseEnteredEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    ((Circle)t.getSource()).setCursor(Cursor.HAND);
                }
            };

    EventHandler<MouseEvent> circleOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
                    orgTranslateY = ((Circle)(t.getSource())).getTranslateY();

                    ((Circle)t.getSource()).setCursor(Cursor.CLOSED_HAND);
                }
            };

    EventHandler<MouseEvent> circleOnMouseReleasedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    ((Circle) t.getSource()).setCursor(Cursor.HAND);
                }
            };

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    Circle c = (Circle) t.getSource();

                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    if(newTranslateX < c.getRadius())
                        newTranslateX = c.getRadius();

                    if(newTranslateX > tabPane.getWidth() - c.getRadius())
                        newTranslateX = tabPane.getWidth() - c.getRadius();

                    if(newTranslateY < c.getRadius())
                        newTranslateY = c.getRadius();

                    // tab height = 33
                    if(newTranslateY > tabPane.getHeight() - c.getRadius() -33)
                        newTranslateY = tabPane.getHeight() - c.getRadius() -33;

                    c.setTranslateX(newTranslateX);
                    c.setTranslateY(newTranslateY);
                }
            };


}
