/**
 * Created by LBX on 31/03/2017.
 */
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static java.awt.event.KeyEvent.VK_KP_DOWN;

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
        handleNew();
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
        //AnchorPane paneBack = new AnchorPane();
        AnchorPane paneBack = new AnchorPane();
        AnchorPane pane = new AnchorPane();

        pane.setPrefSize(paneBack.getWidth(), paneBack.getHeight());
        //mouselistener to add vertex etc
        paneBack.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                panePressed(event);
            }
        });


        // Slider of the current tab created
        Slider slider = new Slider();
        slider.setMin(1.);
        slider.setMax(5.);
        slider.setValue(1.);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                pane.setScaleX(slider.getValue());
                pane.setScaleY(slider.getValue());

                //to have the zoom at the center
                pane.setTranslateX(-((paneBack.getWidth()*(slider.getValue()-1)) / 2));
                pane.setTranslateY(-((paneBack.getHeight()*(slider.getValue()-1)) / 2));
            }
        });

        //change the view, if you press the right arrow, the main page move right etc
        tabPane.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.LEFT && pane.getTranslateX()*(-1) > 0)
                {
                    pane.setTranslateX(pane.getTranslateX()+20);
                }
                else if(event.getCode() == KeyCode.RIGHT && pane.getTranslateX()*(-1) < paneBack.getWidth()*(slider.getValue() -1))
                {

                    System.out.println(pane.getTranslateX() +"  "+paneBack.getWidth()*(slider.getValue() -1));
                    pane.setTranslateX(pane.getTranslateX() - 20);
                }
                else if(event.getCode() == KeyCode.UP && pane.getTranslateY()*(-1) > 0)
                {
                    pane.setTranslateY(pane.getTranslateY() + 20);
                }
                else if(event.getCode() == KeyCode.DOWN && pane.getTranslateY()*(-1) < paneBack.getHeight()*(slider.getValue() -1))
                {
                    pane.setTranslateY(pane.getTranslateY() - 20);
                }
            }
        });


        //delete focus on slider, to stop de right, left on slider
        slider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pane.requestFocus();
            }

    });
        AnchorPane.setRightAnchor(slider, 2.0);
        AnchorPane.setBottomAnchor(slider, 5.0);
        paneBack.getChildren().add(pane);
        paneBack.getChildren().add(slider);

        tab.setContent(paneBack);
        tabPane.getTabs().add(tab);
        // Add Vertex
       /* Circle circle_base = createVertex(100.0, 100.0);
        Group root = new Group();
        root.getChildren().add(circle_base);
        pane.getChildren().add(root);*/
    }
    public Circle createVertex(double x, double y)
    {
        Circle circle_base = new Circle(10.0f, Color.web("da5630"));
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
            AnchorPane currP = (AnchorPane) currPage.getChildren().get(0);
            currP.getChildren().add(circle);
            //if we have the click once. we desactivate it
            vertex1ActiveOnce = false;
            if(!vertex1Active)
                vertex1Button.setId("vertex1Button");//let the butonn orange if he is used
        }
    }

    @FXML
    private void sliderMove(){
        System.out.println("slider");
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
            vertex1Button.setId("vertex1ButtonActivate");//let the button orange if he is used
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