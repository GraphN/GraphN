/**
 * Created by LBX on 31/03/2017.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

public class MainPageController {
    private MainApp mainApp;
    private ArrayList<GraphDom> listGraphXml; //list of xml files, from different tabs

    //////////////////////buttons active or not/////////////////////////////////////////////////////////////////////////
    private boolean vertex1Active = false;
    private boolean vertex1ActiveOnce = false;
    private boolean edge1Active = false;
    private boolean edge1ActiveOnce = false;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean firstVerForEdge = true;
    private Circle circleStart;
    private String nameVertexStart;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button vertex1Button;
    @FXML
    private Button edgeButton;
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
        listGraphXml = new ArrayList<>();
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
    private void handleLaunch()
    {
        //get the xml of the current graph to send it to showAlgoPage()
        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());

        mainApp.showAlgoPage(graphXml);
    }
    @FXML
    private void handleNew()
    {
        //create new tab whitout name to get one name like new tab 3, 4 ..
        Tab tab = createNewTab("");
        tabPane.getTabs().add(tab);

        //creation of the xml file of this tab
        try {
            GraphDom currentGraphDom = new GraphDom(tab.getId());
            //adding this xml to the list
            listGraphXml.add(currentGraphDom);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        /*Tab tab = new Tab();
        String tabName = "new tab"+ indiceTab++;
        tab.setText(tabName);
        tab.setId(tabName);

        //creation of the xml file of this tab
        try {
            GraphDom currentGraphDom = new GraphDom(tabName);
            //adding this xml to the list
            listGraphXml.add(currentGraphDom);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // Ajouter tout ce qu'on veut dans la  tab
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
*/
    }
    private Circle createVertex(double x, double y, String id)
    {
        Circle circle_base = new Circle(10.0f, Color.web("da5630"));
        circle_base.setId(id);
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
            Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();

            //adding this vertex to the xml file
            GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
            graphXml.addVertex((int)mouseEvent.getX(), (int)mouseEvent.getY());

            String nameOfVertex = "ver_"+graphXml.getNbVertex();

            //creation of a vertex (circle) in good positions
            Circle circle = createVertex(mouseEvent.getX(), mouseEvent.getY(), nameOfVertex);

            AnchorPane currPage = (AnchorPane) currentTab.getContent();
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
    private void handleSave()
    {
        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
        mainApp.showSavePage(graphXml);
    }
    @FXML
    private void handleOpen()
    {

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////Probleme code a factoriser. redondance avec fonction createAnchorPaneWithXml de la classe AlgoPageContr//
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        try {
            GraphDom graphOpen = mainApp.showOpenPage();

            //adding this xml to the list
            listGraphXml.add(graphOpen);
            Tab tab = createNewTab(graphOpen.getName());

            //get the Anchorpane who we need to fill
            AnchorPane paneBack = (AnchorPane) tab.getContent();
            AnchorPane pane = (AnchorPane) paneBack.getChildren().get(0);

            //adding all vertex from xml
            int i = 0;
            while (i < graphOpen.getNbVertex())
            {
                Point2D point = graphOpen.getPosOfVertex(i);
                int x = (int) point.getX();
                int y = (int) point.getY();
                String name = graphOpen.getName(i);

                //adding vertex created to pane
                pane.getChildren().add(createVertex(x, y, name));

                i++;
            }
            i = 0;
            while (i < graphOpen.getNbEdge())
            {
                //adding all edges from xml
                Line edge  = graphOpen.getEdge(i);
                edge.setStrokeWidth(4);
                edge.setSmooth(true);
                edge.setStroke(Color.web("da5630"));

                //getting vertexes start and end of this line, to add listeneners
                Circle cirleStart = (Circle) getChildrenVertexById(pane, graphOpen.getEdgeStartName(i));
                Circle cirleEnd = (Circle) getChildrenVertexById(pane, graphOpen.getEdgeEndName(i));
                //creating listener for moving edge when moving vertexes
                moveVertexMoveEdgeListener(cirleStart, cirleEnd, edge);
                //adding edge created to pane (at index 0 to have vertexes on front of edges)
                pane.getChildren().add(0,edge);

                i++;
            }
            tabPane.getTabs().add(tab);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleImport(){
    }
    @FXML
    private void handleVectrice(MouseEvent mouseEvent)
    {
        int count = mouseEvent.getClickCount();
        if (count == 2 && !isOtherButtonActivate("vertex1")) // if we double click, we can put infinite vertex
        {
            vertex1Active = true;
            vertex1ActiveOnce = false;
            vertex1Button.setId("vertex1ButtonActivate");//let the button orange if he is used
        } else if (count == 1 && (vertex1ActiveOnce || vertex1Active))// if we click and if we are activate, we desactive
        {
            vertex1Active = false;
            vertex1ActiveOnce = false;
            vertex1Button.setId("vertex1Button");// desactivate button of orange
        } else if (count == 1 && !isOtherButtonActivate("vertex1"))// if we clicked and we are desactivate, we active
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
    private void handleEdge(MouseEvent mouseEvent)
    {
        int count = mouseEvent.getClickCount();
        if (count == 2 && !isOtherButtonActivate("edge1")) // if we double click, we can put infinite edges
        {
            edge1Active = true;
            edge1ActiveOnce = false;
            edgeButton.setId("edgeButtonActivate");//let the button orange if he is used
        } else if (count == 1 && (edge1ActiveOnce || edge1Active))// if we click and if we are activate, we desactive
        {
            edge1Active = false;
            edge1ActiveOnce = false;
            edgeButton.setId("edgeButton");// desactivate button of orange
        } else if (count == 1 /*&& (!edge1ActiveOnce || !edge1Active)*/ && !isOtherButtonActivate("edge1"))// if we clicked and we are desactivate, we active
        {
            edge1ActiveOnce = true;
            edge1Active = false;
            edgeButton.setId("edgeButtonActivate");//let the butonn orange if he is used
        }
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

                    //get currentPane to know the slider position
                    AnchorPane anch = (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
                    Slider slider = (Slider) anch.getChildren().get(1);
                    double sliderValue = slider.getValue();

                    orgTranslateX = ((Circle)(t.getSource())).getTranslateX()*sliderValue;
                    orgTranslateY = ((Circle)(t.getSource())).getTranslateY()*sliderValue;
                    ((Circle)t.getSource()).setCursor(Cursor.CLOSED_HAND);
                }
            };
    EventHandler<MouseEvent> circleOnMouseReleasedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t)
                {
                    Circle circle = (Circle) t.getSource();
                    circle.setCursor(Cursor.HAND);

                    //Do when the edge button is activate to draw a edge.
                    if(edge1Active||edge1ActiveOnce)
                    {
                        if (firstVerForEdge)
                        {
                            circleStart = circle;
                            nameVertexStart = circle.getId();
                            firstVerForEdge = false;
                        } else
                        {
                            Line currentLine = new Line();
                            currentLine.setStartX(circleStart.getTranslateX());
                            currentLine.setStartY(circleStart.getTranslateY());
                            currentLine.setEndX(circle.getTranslateX());
                            currentLine.setEndY(circle.getTranslateY());
                            currentLine.setStrokeWidth(4);
                            currentLine.setSmooth(true);
                            currentLine.setStroke(Color.web("da5630"));

                            Circle circleEnd = (Circle) t.getSource();
                            moveVertexMoveEdgeListener(circleStart, circleEnd, currentLine);
                            ////////////////////////////////////////////////moving vertex move edges////////////////////
                            /*((Circle) t.getSource()).translateXProperty().addListener(new ChangeListener<Number>()
                            {
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                                {
                                    currentLine.setEndX((double)newValue);

                                }
                            });
                            ((Circle) t.getSource()).translateYProperty().addListener(new ChangeListener<Number>()
                            {
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                                {
                                    currentLine.setEndY((double)newValue);

                                }
                            });
                            circleStart.translateXProperty().addListener(new ChangeListener<Number>()
                            {
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                                {
                                    currentLine.setStartX((double)newValue);
                                }
                            });

                            circleStart.translateYProperty().addListener(new ChangeListener<Number>()
                            {
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                                {
                                    currentLine.setStartY((double)newValue);
                                }
                            });*/
                            ////////////////////////////////////////////////////////////////////////////////////////////

                            Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
                            AnchorPane currPage = (AnchorPane) currentTab.getContent();
                            AnchorPane currP = (AnchorPane) currPage.getChildren().get(0);
                            currP.getChildren().add(0,currentLine); // add at index 0, to have the line behind vertexes

                            //adding the edge in the xml
                            GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
                            graphXml.addEdge(nameVertexStart, circle.getId());
                            firstVerForEdge = true;
                            if(edge1ActiveOnce)
                            {
                                edge1ActiveOnce = false;
                                edgeButton.setId("edgeButton");
                            }
                        }
                    }
                }
            };
    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    //get currentPane to know the slider position
                    AnchorPane anch = (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
                    Slider slider = (Slider) anch.getChildren().get(1);
                    double sliderValue = slider.getValue();

                    Circle c = (Circle) t.getSource();
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = (orgTranslateX + offsetX)/sliderValue;
                    double newTranslateY = (orgTranslateY + offsetY)/sliderValue;
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

                    //the vertex has been moved so we need to change it position in the xml
                    Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
                    GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
                    graphXml.setPosOfVertex(c.getId(),(int) newTranslateX, (int) newTranslateY);
                }
            };

            private GraphDom getXmlOfThisTab(String name)
            {
                for(int i = 0; i < listGraphXml.size(); i++)
                {
                    if(listGraphXml.get(i).getName().equals(name))
                        return listGraphXml.get(i);
                }
                return null;
            }

            private boolean isOtherButtonActivate(String name)
            {
                if(name.equals("vertex1"))
                    return (edge1Active || edge1ActiveOnce);
                else if(name.equals("edge1"))
                    return (vertex1Active || vertex1ActiveOnce);
                else
                    return false;
            }

            private Tab createNewTab(String tabName)
            {
                Tab tab = new Tab();
                if(tabName.equals(""))
                    tabName = "new tab"+ indiceTab++;
                tab.setText(tabName);
                tab.setId(tabName);

                // adding Panes in the tab
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
                AnchorPane.setRightAnchor(slider, 2.0);
                AnchorPane.setBottomAnchor(slider, 5.0);
                paneBack.getChildren().add(pane);
                paneBack.getChildren().add(slider);

                tab.setContent(paneBack);
                return tab;
            }

            private void moveVertexMoveEdgeListener(Circle circleStart, Circle circleEnd, Line currentLine)
            {
                //////////////////////////////////moving vertex move edges////////////////////
                circleEnd.translateXProperty().addListener(new ChangeListener<Number>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                    {
                        currentLine.setEndX((double)newValue);

                    }
                });
                circleEnd.translateYProperty().addListener(new ChangeListener<Number>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                    {
                        currentLine.setEndY((double)newValue);

                    }
                });
                circleStart.translateXProperty().addListener(new ChangeListener<Number>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                    {
                        currentLine.setStartX((double)newValue);
                    }
                });

                circleStart.translateYProperty().addListener(new ChangeListener<Number>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
                    {
                        currentLine.setStartY((double)newValue);
                    }
                });
                ////////////////////////////////////////////////////////////////////////////////////////////

            }

            private Node getChildrenVertexById(AnchorPane pane, String id)
            {
                for(int i = 0; i < pane.getChildren().size(); i++)
                {
                    Node node = pane.getChildren().get(i);
                    System.out.println(node.getClass());
                    if(node.getClass().equals(Circle.class))
                    {
                        Circle circle = (Circle) node;
                        if(circle.getId().equals(id))
                            return node;
                    }
                }
                return null;
            }
}