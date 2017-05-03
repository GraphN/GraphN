/**
 * Created by LBX on 31/03/2017.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import sun.security.provider.SHA;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class MainPageController {
    private MainApp mainApp;
    private ArrayList<GraphDom> listGraphXml; //list of xml files, from different tabs

    //////////////////////buttons active or not/////////////////////////////////////////////////////////////////////////
    private boolean vertex1Active = false;
    private boolean vertex1ActiveOnce = false;
    private boolean edge1Active = false;
    private boolean edge1ActiveOnce = false;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // List for bind a circle and it's number
    //private ArrayList<ArrayList<Shape>> listCircleNumber;
    private Map<String,ArrayList<Shape>> mapNode;

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
        mapNode = new HashMap<String, ArrayList<Shape>>();
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
    private Circle createVertexShape(double x, double y, String id)
    {
        Circle circle_base = new Circle(10.0f, Color.web("da5630"));
        circle_base.setId(id);
        circle_base.setTranslateX(x);
        circle_base.setTranslateY(y);
        circle_base.setOnMousePressed(nodeOnMousePressedEventHandler);
        circle_base.setOnMouseDragged(nodeOnMouseDraggedEventHandler);
        circle_base.setOnMouseEntered(nodeOnMouseEnteredEventHandler);
        circle_base.setOnMouseReleased(nodeOnMouseReleasedEventHandler);
        return circle_base;
    }
    private Text createVertexNumber(double x, double y, String id){
        //number of the vertex
        int nbVertex = Integer.parseInt(id.replaceAll("[\\D]", ""));
        Text text = new Text(""+nbVertex);
        //text.setFill(Color.WHITE);
        text.setId(id);
        text.setBoundsType(TextBoundsType.VISUAL);
        //centering the numbers
        if(nbVertex<10){
            text.setTranslateX(x-4.5);
            text.setTranslateY(y+6);
        }else{
            text.setTranslateX(x-8.5);
            text.setTranslateY(y+6);
        }
        text.setOnMousePressed(nodeOnMousePressedEventHandler);
        text.setOnMouseDragged(nodeOnMouseDraggedEventHandler);
        text.setOnMouseEntered(nodeOnMouseEnteredEventHandler);
        text.setOnMouseReleased(nodeOnMouseReleasedEventHandler);

        return text;
    }
    private List<Shape> createVertex (double x, double y, String id){
        ArrayList<Shape> node = new ArrayList<>();
        node.add(createVertexShape(x,y,id));
        node.add(createVertexNumber(x,y,id));
        mapNode.put(id.toString(),node);
        System.out.println("x: "+x+ " y: " + y);

        return node;
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

            //creation of a number (text) in good positions
            //Text text = createVertexNumber(mouseEvent.getX(), mouseEvent.getY(), nameOfVertex);

            //creation of a vertex (circle) in good positions
            //Circle circle = createVertexShape(mouseEvent.getX(), mouseEvent.getY(), nameOfVertex);

            AnchorPane currPage = (AnchorPane) currentTab.getContent();
            AnchorPane currP = (AnchorPane) currPage.getChildren().get(0);
            // Creation of a vertex
            List<Shape> node = createVertex(mouseEvent.getX(), mouseEvent.getY(), nameOfVertex);
            currP.getChildren().addAll(node.get(0), node.get(1));

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

            //if the graph is null, its because there was a problem or when it was asked to choose a xml file,
            // the user canceled this acction, so we cant create a new tab
            //if(graphOpen!=null)

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
                List<Shape> node = createVertex(x,y,name);
                pane.getChildren().addAll(node.get(0),node.get(1));

                i++;
            }

                i = 0;
                while (i < graphOpen.getNbEdge()) {
                    //adding all edges from xml
                    Line edge = graphOpen.getEdge(i);
                    edge.setStrokeWidth(4);
                    edge.setSmooth(true);
                    edge.setStroke(Color.web("da5630"));

                    //getting vertexes start and end of this line, to add listeneners
                    Circle cirleStart = (Circle) getChildrenVertexById(pane, graphOpen.getEdgeStartName(i));
                    Circle cirleEnd = (Circle) getChildrenVertexById(pane, graphOpen.getEdgeEndName(i));
                    //creating listener for moving edge when moving vertexes
                    moveVertexMoveEdgeListener(cirleStart, cirleEnd, edge);
                    //adding edge created to pane (at index 0 to have vertexes on front of edges)
                    pane.getChildren().add(0, edge);

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
        // Desactivate edge if active
        desactivateEdge();

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
    private void desactivateVertex(){
        vertex1Active = false;
        vertex1ActiveOnce = false;
        vertex1Button.setId("vertex1Button");// desactivate button of orange
    }
    @FXML
    private void handleVectrice2(){
    }
    @FXML
    private void handleEdge(MouseEvent mouseEvent)
    {
        //desactivate vertex if active
        desactivateVertex();

        // Reset to the first edge
        firstVerForEdge = true;

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

    private void desactivateEdge(){
        edge1Active = false;
        edge1ActiveOnce = false;
        edgeButton.setId("edgeButton");// desactivate button of orange
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

    private Text edgeWeightText(Line line, int weight){

        Text text = new Text();

        text.setTranslateX((line.getEndX()+line.getStartX())/2);
        text.setTranslateY((line.getEndY()+line.getStartY())/2);
        // Faudrait un calcul pour centrer en fonction de l'angle
        //System.out.println((line.getEndX()+line.getStartX())/2);
        //System.out.println((line.getEndY()+line.getStartY())/2);

        text.setText(""+weight);

        return text;
    }

    EventHandler<MouseEvent> nodeOnMouseEnteredEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    Circle circle;
                    Text text;
                    if(t.getSource().getClass() == Circle.class){
                        circle = (Circle) t.getSource();
                        text = (Text) mapNode.get(circle.getId().toString()).get(1);
                    }
                    else{
                        text = (Text) t.getSource();
                        circle = (Circle) mapNode.get(text.getId().toString()).get(0);
                    }

                    circle.setCursor(Cursor.HAND);
                    text.setCursor(Cursor.HAND);
                }
            };


    EventHandler<MouseEvent> nodeOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {

                    Circle circle;
                    Text text;
                    if(t.getSource().getClass() == Circle.class){
                        circle = (Circle) t.getSource();
                        text = (Text) mapNode.get(circle.getId().toString()).get(1);
                    }
                    else{
                        text = (Text) t.getSource();
                        circle = (Circle) mapNode.get(text.getId().toString()).get(0);
                    }

                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();

                    //get currentPane to know the slider position
                    AnchorPane anch = (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
                    Slider slider = (Slider) anch.getChildren().get(1);
                    double sliderValue = slider.getValue();

                    orgTranslateX = circle.getTranslateX()*sliderValue;
                    orgTranslateY = circle.getTranslateY()*sliderValue;

                    circle.setCursor(Cursor.CLOSED_HAND);
                    text.setCursor(Cursor.CLOSED_HAND);
                }
            };

    EventHandler<MouseEvent> nodeOnMouseReleasedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t)
                {
                    Circle circle;
                    Text text;
                    if(t.getSource().getClass() == Circle.class){
                        circle = (Circle) t.getSource();
                        text = (Text) mapNode.get(circle.getId().toString()).get(1);
                    }
                    else{
                        text = (Text) t.getSource();
                        circle = (Circle) mapNode.get(text.getId().toString()).get(0);
                    }
                    circle.setCursor(Cursor.HAND);
                    text.setCursor(Cursor.HAND);

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

                            Circle circleEnd = circle;
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
                            // à mettre si il y a un poid à l'edge
                            //currP.getChildren().add(1, edgeWeightText(currentLine,1));
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

    EventHandler<MouseEvent> nodeOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    //get currentPane to know the slider position
                    AnchorPane anch = (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
                    Slider slider = (Slider) anch.getChildren().get(1);
                    double sliderValue = slider.getValue();

                    Circle circle;
                    Text text;
                    if(t.getSource().getClass() == Circle.class){
                        circle = (Circle) t.getSource();
                        text = (Text) mapNode.get(circle.getId().toString()).get(1);
                    }
                    else{
                        text = (Text) t.getSource();
                        circle = (Circle) mapNode.get(text.getId().toString()).get(0);
                    }

                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = (orgTranslateX + offsetX)/sliderValue;
                    double newTranslateY = (orgTranslateY + offsetY)/sliderValue;
                    if(newTranslateX < circle.getRadius())
                        newTranslateX = circle.getRadius();
                    if(newTranslateX > tabPane.getWidth() - circle.getRadius())
                        newTranslateX = tabPane.getWidth() - circle.getRadius();
                    if(newTranslateY < circle.getRadius())
                        newTranslateY = circle.getRadius();
                    // tab height = 33
                    if(newTranslateY > tabPane.getHeight() - circle.getRadius() -33)
                        newTranslateY = tabPane.getHeight() - circle.getRadius() -33;
                    circle.setTranslateX(newTranslateX);
                    circle.setTranslateY(newTranslateY);

                    if(Integer.parseInt(text.getId().toString().replaceAll("[\\D]", ""))<10){
                        text.setTranslateX(newTranslateX-4.5);
                        text.setTranslateY(newTranslateY+6);
                    }else{
                        text.setTranslateX(newTranslateX-8.5);
                        text.setTranslateY(newTranslateY+6);
                    }

                    //the vertex has been moved so we need to change it position in the xml
                    Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
                    GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
                    graphXml.setPosOfVertex(circle.getId(),(int) newTranslateX, (int) newTranslateY);
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

                paneBack.setFocusTraversable(true);
                pane.setFocusTraversable(true);

                pane.setPrefSize(paneBack.getWidth(), paneBack.getHeight());

                //mouselistener to add vertex etc
                paneBack.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        panePressed(event);
                        paneBack.requestFocus();
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

                // Scroll de la souris zoom
                paneBack.setOnScroll(new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        if(event.getDeltaY()>0){
                            slider.setValue(slider.getValue()*1.05);
                        }else
                            slider.setValue(slider.getValue()*0.95);
                    }
                });

                //change the view, if you press the right arrow, the main page move right etc
                paneBack.setOnKeyPressed(new EventHandler<KeyEvent>() {

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
                        else if(event.getCode() == KeyCode.ESCAPE){
                            // Desactivate edge if active
                            desactivateEdge();
                            //desactivate vertex if active
                            desactivateVertex();
                        }
                    }
                });

                tabPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent event) {
                        if(event.getCode() == KeyCode.ESCAPE){
                            // Desactivate edge if active
                            desactivateEdge();
                            //desactivate vertex if active
                            desactivateVertex();
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