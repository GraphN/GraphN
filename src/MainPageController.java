/**
 * Created by LBX on 31/03/2017.
 */


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Screen;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// todo: Ajouter un bouton pour pouvoir supprimmer des trucs?
public class MainPageController {
    private MainApp mainApp;
    private ArrayList<GraphDom> listGraphXml; //list of xml files, from different tabs

    private final float RAYON_CERCLE = 10f;

    //////////////////////buttons active or not/////////////////////////////////////////////////////////////////////////
    private boolean vertex1Active = false;
    private boolean vertex1ActiveOnce = false;
    private boolean edge1Active = false;
    private boolean edge1ActiveOnce = false;
    private boolean eraserActive = false;
    private boolean eraserActiveOnce = false;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private float textTranslateY;
    private float textTranslateX1Digit;
    private float textTranslateX2Digits;

    // List for bind a circle and it's number
    //private ArrayList<ArrayList<Shape>> listCircleNumber;
    private Map<String,ArrayList<Shape>> mapNode;

    private ArrayList<DrawEdge> drawEdgesList;
    private ArrayList<ArrayList<DrawEdge>> drawEdgesGroupList;

    private boolean firstVerForEdge = true;
    private Circle circleStart;
    private Group groupStart;
    private String nameVertexStart;
    private Map<Tab, String> tabMap;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button vertex1Button;
    @FXML
    private Button edgeButton;
    @FXML
    private Button weightedEdgeButton;
    @FXML
    private Button diEdgeButton;
    @FXML
    private Button weightedDiEdgeButton;
    @FXML
    private Button eraserButton;
    @FXML
    private AnchorPane greyMain;
    private int indiceTab;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;

    private Line liveEdge;
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
        drawEdgesList = new ArrayList<>();
        drawEdgesGroupList = new ArrayList<>();
        tabMap = new HashMap<>();


        liveEdge = new Line();
        liveEdge.setStrokeWidth(4);
        liveEdge.setSmooth(true);
        liveEdge.setStroke(Color.web("da5630"));


        // TODO: tester avec d'autres pc, apparement pas lié à la résolution
        textTranslateY = 6;
        textTranslateX1Digit = -4.5f;
        textTranslateX2Digits = -8.5f;

        if(Screen.getPrimary().getBounds().getMaxX() == 1920 && Screen.getPrimary().getBounds().getMaxY() == 1080){
            textTranslateY = 6;
            textTranslateX1Digit = -4.5f;
            textTranslateX2Digits = -8.5f;
        }

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
        if(graphXml.getNbVertex() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Votre graphe est vide");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("assets/css/alert.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();
            return;
        }
        mainApp.showAlgoPage(graphXml);
    }
    @FXML
    private void handleNew()
    {
        //create new tab whitout name to get one name like new tab 3, 4 ..
        Tab tab = createNewTab("");
        tab.setOnClosed(new EventHandler<Event>(){
            @Override
            public void handle(Event e){
                if(tabPane.getTabs().size() < 1)
                    handleNew();
            }
        });
        tabPane.getTabs().add(tab);

        //creation of the xml file of this tab
        try {
            GraphDom currentGraphDom = new GraphDom(tab.getId());
            //adding this xml to the list
            listGraphXml.add(currentGraphDom);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private Group createVertex (double x, double y, String id){

        Circle circle_base = new Circle(RAYON_CERCLE, Color.web("da5630"));
        circle_base.setId(id);

        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();

        AnchorPane currentPage = (AnchorPane) currentTab.getContent();
        AnchorPane currentPane = (AnchorPane) currentPage.getChildren().get(0);

        Slider currentSlider = (Slider) currentPage.getChildren().get(1);
        System.out.println( currentPane.getTranslateX()+ "   " + currentPane.getTranslateY());

        circle_base.setOnMousePressed(nodeOnMousePressedEventHandler);
        circle_base.setOnMouseDragged(nodeOnMouseDraggedEventHandler);
        circle_base.setOnMouseEntered(nodeOnMouseEnteredEventHandler);
        circle_base.setOnMouseReleased(nodeOnMouseReleasedEventHandler);

        int nbVertex = Integer.parseInt(id.replaceAll("[\\D]", ""));
        Text text = new Text(""+nbVertex);
        //text.setFill(Color.WHITE);
        text.setId(id);
        text.setBoundsType(TextBoundsType.VISUAL);
        //centering the numbers
        if(nbVertex<10){
            text.setTranslateX(textTranslateX1Digit);
            text.setTranslateY(textTranslateY);
        }else{
            text.setTranslateX(textTranslateX2Digits);
            text.setTranslateY(textTranslateY);
        }
        text.setOnMousePressed(nodeOnMousePressedEventHandler);
        text.setOnMouseDragged(nodeOnMouseDraggedEventHandler);
        text.setOnMouseEntered(nodeOnMouseEnteredEventHandler);
        text.setOnMouseReleased(nodeOnMouseReleasedEventHandler);

        Group vertex = new Group();
        vertex.getChildren().addAll(circle_base,text);
        vertex.setId(id);
        vertex.setTranslateX((x - currentPane.getTranslateX() )/currentSlider.getValue());
        vertex.setTranslateY((y - currentPane.getTranslateY() )/currentSlider.getValue());
        System.out.println("x: "+x+ " y: " + y);

        return vertex;
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
//            List<Shape> node = createVertex(mouseEvent.getX(), mouseEvent.getY(), nameOfVertex);
//            currP.getChildren().addAll(node.get(0), node.get(1));
            Group node = createVertex(mouseEvent.getX(), mouseEvent.getY(), nameOfVertex);
            currP.getChildren().add(node);
            // Test de le flèche
            //currP.getChildren().add(new EdgeDraw(200,400,70,30,true, 1));
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
            // Si l'on a pas selectionné de fichier, ne fait rien
            if(graphOpen == null) return;
            //if the graph is null, its because there was a problem or when it was asked to choose a xml file,
            // the user canceled this acction, so we cant create a new tab
            //if(graphOpen!=null)

            //adding this xml to the list
            listGraphXml.add(graphOpen);
            Tab tab = createNewTab(graphOpen.getName());

            tab.setOnClosed(new EventHandler<Event>(){
                @Override
                public void handle(Event e){
                    if(tabPane.getTabs().size() < 1)
                        handleNew();
                }
            });

            //get the Anchorpane who we need to fill
            AnchorPane paneBack = (AnchorPane) tab.getContent();
            AnchorPane pane = (AnchorPane) paneBack.getChildren().get(0);

            //adding all vertex from xml
            int i = 0;
            while (i <= graphOpen.getNbVertex())
            {
                Point2D point = graphOpen.getPosOfVertex(i);
                int x = (int) point.getX();
                int y = (int) point.getY();
                String name = graphOpen.getName(i);
                // TODO: Régler le soucis qui fait que le 0 est derrière
                // Create vertex and add it to pane
                Group node = createVertex(x,y,name);
                pane.getChildren().add(node);

                i++;
            }

            tabMap.put(tab,graphOpen.getGraphType());

            DrawEdge drawEdge = null;

            i = 0;
            while (i < graphOpen.getNbGroup()) {
                ArrayList<DrawEdge> edges = graphOpen.getDrawEdges(i);
                drawEdgesGroupList.add(edges);

                for(int j = 0; j < edges.size(); j++) {
                    Group cirleStart = (Group) getChildrenVertexById(pane, graphOpen.getEdgeStartName(i, j));
                    Group cirleEnd = (Group) getChildrenVertexById(pane, graphOpen.getEdgeEndName(i, j));
                    drawEdgesList.add(edges.get(j));

                    moveVertexMoveEdgeListenerDraw(cirleStart, cirleEnd, drawEdgesList.indexOf(edges.get(j)), pane);
                    pane.getChildren().add(1, edges.get(j).getRoot());
                }
                i++;
            }

            tabPane.getTabs().add(tab);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleImport(){
        mainApp.showImportPage();
    }
    @FXML
    private void handleVectrice(MouseEvent mouseEvent)
    {
        // Desactivate edge or eraser if active
        desactivateEdge();
        desactivateEraser();

        // Reset to the first edge
        firstVerForEdge = true;

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
        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
        String graphType = tabMap.get(currentTab);
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
        if(graphType == null){
            tabMap.put(currentTab, "nonDiGraph");
            graphXml.setGraphType("nonDiGraph");
        } // todo: le setGraphType ne marche pas quand on sauve et relance
        else if(!graphType.equals("nonDiGraph")) {
            mouseEvent.consume();
            return;
        }
        //desactivate vertex or eraser if active
        desactivateVertex();
        desactivateEraser();

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
            edgeButton.setId("edgeButtonActivate");//let the button orange if he is used
        }
    }

    @FXML
    private void handleWeightedEdge(MouseEvent mouseEvent)
    {
        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
        String graphType = tabMap.get(currentTab);
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
        if(graphType == null){
            tabMap.put(currentTab, "weightedNonDiGraph");
            graphXml.setGraphType("weightedNonDiGraph");
        } // todo: le setGraphType ne marche pas quand on sauve et relance
        else if(!graphType.equals("weightedNonDiGraph")) {
            mouseEvent.consume();
            return;
        }
        //desactivate vertex or eraser if active
        desactivateVertex();
        desactivateEraser();

        // Reset to the first edge
        firstVerForEdge = true;

        int count = mouseEvent.getClickCount();
        if (count == 2 && !isOtherButtonActivate("edge1")) // if we double click, we can put infinite edges
        {
            edge1Active = true;
            edge1ActiveOnce = false;
            weightedEdgeButton.setId("weightedEdgeButtonActivate");//let the button orange if he is used
        } else if (count == 1 && (edge1ActiveOnce || edge1Active))// if we click and if we are activate, we desactive
        {
            edge1Active = false;
            edge1ActiveOnce = false;
            weightedEdgeButton.setId("weightedEdgeButton");// desactivate button of orange
        } else if (count == 1 /*&& (!edge1ActiveOnce || !edge1Active)*/ && !isOtherButtonActivate("edge1"))// if we clicked and we are desactivate, we active
        {
            edge1ActiveOnce = true;
            edge1Active = false;
            weightedEdgeButton.setId("weightedEdgeButtonActivate");//let the button orange if he is used
        }
    }
    // todo: changer correctement les boutons
    @FXML
    private void handleDiEdge(MouseEvent mouseEvent)
    {
        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
        String graphType = tabMap.get(currentTab);
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
        if(graphType == null){
            tabMap.put(currentTab, "diGraph");
            graphXml.setGraphType("diGraph");
        } // todo: le setGraphType ne marche pas quand on sauve et relance
        else if(!graphType.equals("diGraph")) {
            mouseEvent.consume();
            return;
        }
        //desactivate vertex or eraser if active
        desactivateVertex();
        desactivateEraser();

        // Reset to the first edge
        firstVerForEdge = true;

        int count = mouseEvent.getClickCount();
        if (count == 2 && (!edge1ActiveOnce || !edge1Active)) // if we double click, we can put infinite edges
        {
            edge1Active = true;
            edge1ActiveOnce = false;
            diEdgeButton.setId("diEdgeButtonActivate");//let the button orange if he is used
        } else if (count == 1 && (edge1ActiveOnce || edge1Active))// if we click and if we are activate, we desactive
        {
            edge1Active = false;
            edge1ActiveOnce = false;
            diEdgeButton.setId("diEdgeButton");// desactivate button of orange
        } else if (count == 1 && (!edge1ActiveOnce || !edge1Active))// if we clicked and we are desactivate, we active
        {
            edge1ActiveOnce = true;
            edge1Active = false;
            diEdgeButton.setId("diEdgeButtonActivate");//let the button orange if he is used
        }
    }
    @FXML
    private void handleWeightedDiEdge(MouseEvent mouseEvent)
    {
        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
        String graphType = tabMap.get(currentTab);
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
        if(graphType == null){
            tabMap.put(currentTab, "weightedDiGraph");
            graphXml.setGraphType("weightedDiGraph");
        } // todo: le setGraphType ne marche pas quand on sauve et relance
        else if(!graphType.equals("weightedDiGraph")) {
            mouseEvent.consume();
            return;
        }
        //desactivate vertex or eraser if active
        desactivateVertex();
        desactivateEraser();

        // Reset to the first edge
        firstVerForEdge = true;

        int count = mouseEvent.getClickCount();
        if (count == 2 && (!edge1ActiveOnce || !edge1Active)) // if we double click, we can put infinite edges
        {
            edge1Active = true;
            edge1ActiveOnce = false;
            weightedDiEdgeButton.setId("weightedDiEdgeButtonActivate");//let the button orange if he is used
        } else if (count == 1 && (edge1ActiveOnce || edge1Active))// if we click and if we are activate, we desactive
        {
            edge1Active = false;
            edge1ActiveOnce = false;
            weightedDiEdgeButton.setId("weightedDiEdgeButton");// desactivate button of orange
        } else if (count == 1 && (!edge1ActiveOnce || !edge1Active))// if we clicked and we are desactivate, we active
        {
            edge1ActiveOnce = true;
            edge1Active = false;
            weightedDiEdgeButton.setId("weightedDiEdgeButtonActivate");//let the button orange if he is used
        }
    }


    private void desactivateEdge(){
        edge1Active = false;
        edge1ActiveOnce = false;
        edgeButton.setId("edgeButton");// desactivate button of orange
        diEdgeButton.setId("diEdgeButton");
        weightedDiEdgeButton.setId("weightedDiEdgeButton");
        weightedEdgeButton.setId("weightedEdgeButton");
    }

    @FXML
    private void handleEraser(MouseEvent mouseEvent)
    {
        //desactivate vertex and edge if active
        desactivateVertex();
        desactivateEdge();

        // Reset to the first edge
        firstVerForEdge = true;

        int count = mouseEvent.getClickCount();
        if (count == 2 && (!eraserActiveOnce || !eraserActive)) // if we double click, we can put infinite edges
        {
            eraserActive = true;
            eraserActiveOnce = false;
            eraserButton.setId("eraserButtonActivate");//let the button orange if he is used
        } else if (count == 1 && (eraserActiveOnce ||eraserActive))// if we click and if we are activate, we desactive
        {
            eraserActive = false;
            eraserActiveOnce = false;
            eraserButton.setId("eraserButton");// desactivate button of orange
        } else if (count == 1 && (!eraserActiveOnce || !eraserActive))// if we clicked and we are desactivate, we active
        {
            eraserActive = false;
            eraserActiveOnce = true;
            eraserButton.setId("eraserButtonActivate");//let the button orange if he is used
        }
    }
    private void desactivateEraser(){
        eraserActive = false;
        eraserActiveOnce = false;
        eraserButton.setId("eraserButton");// desactivate button of orange
    }

    EventHandler<MouseEvent> nodeOnMouseEnteredEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    Shape shape = (Shape)t.getSource();
                    Group group = (Group)shape.getParent();
                    group.setCursor(Cursor.HAND);
                }
            };

    EventHandler<MouseEvent> nodeOnMousePressedEventHandler =
            // TODO: ajouter l'erase
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {

                    Shape shape = (Shape)t.getSource();
                    Group group = (Group)shape.getParent();

                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();

                    //get currentPane to know the slider position
                    AnchorPane anch = (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
                    Slider slider = (Slider) anch.getChildren().get(1);
                    double sliderValue = slider.getValue();

                    orgTranslateX = group.getTranslateX()*sliderValue;
                    orgTranslateY = group.getTranslateY()*sliderValue;

                    group.setCursor(Cursor.CLOSED_HAND);

                    //delete if the erease is activate
                    if(eraserActiveOnce || eraserActive)
                    {
                        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
                        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
                        graphXml.deleteVertex(Integer.valueOf(group.getId().substring(4)));



                        //if eraserActive just once, pass it to false, so we cannot erase again
                        if(eraserActiveOnce)
                        {
                            eraserActiveOnce = false;
                        }
                    }
                }
            };

    EventHandler<MouseEvent> nodeOnMouseReleasedEventHandler =
            // TODO: ajouter l'erase
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t)
                {
                    Shape shape = (Shape)t.getSource();
                    Group group = (Group)shape.getParent();
                    group.setCursor(Cursor.HAND);


                    //Do when the edge button is activate to draw a edge.
                    Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
                    AnchorPane currPage = (AnchorPane) currentTab.getContent();
                    AnchorPane currP = (AnchorPane) currPage.getChildren().get(0);
                    String graphType = tabMap.get(currentTab);

                    if(edge1Active||edge1ActiveOnce)
                    {
                        if (firstVerForEdge)
                        {
                            groupStart = group;
                            nameVertexStart = group.getId();
                            firstVerForEdge = false;

                            // Edge that follow the mouse
                            liveEdge.setStartX(groupStart.getTranslateX());
                            liveEdge.setStartY(groupStart.getTranslateY());
                            liveEdge.setEndX(groupStart.getTranslateX());
                            liveEdge.setEndY(groupStart.getTranslateY());

                            currPage.setOnMouseMoved(followMouseHandler);
                            currP.getChildren().add(0,liveEdge);

                        } else if(groupStart != group)
                        {
                            // TODO: ajouter un listener pour l'ERASER
                            Group groupEnd = group;
                            GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
                            DrawEdge drawEdge = null;
                            ArrayList<DrawEdge> drawEdges;
                            int gIndex;
                            Text weight;
                            switch (graphType) {
                                case "nonDiGraph":
                                    gIndex = graphXml.addEdge(nameVertexStart, groupEnd.getId());
                                    updateGroup(currentTab, gIndex);
                                    break;
                                case "weightedNonDiGraph":
                                    weight = new Text(""+mainApp.showWeightPage());
                                    // pour que le text puisse être modifiable
                                    weight.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        public void handle(MouseEvent event) {
                                            weight.setText(""+mainApp.showWeightPage());
                                            // todo: Il faudrait aussi modifier le graphXML
                                        }
                                    });
                                    gIndex = graphXml.addWeightedEdge(nameVertexStart, groupEnd.getId(),weight.getText());
                                    updateGroup(currentTab, gIndex);
                                    break;
                                case "diGraph":
                                    gIndex = graphXml.addDiEdge(nameVertexStart, groupEnd.getId());
                                    updateGroup(currentTab, gIndex);
                                    break;
                                case "weightedDiGraph":
                                    weight = new Text(""+mainApp.showWeightPage());
                                    // pour que le text puisse être modifiable
                                    weight.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        public void handle(MouseEvent event) {
                                            weight.setText(""+mainApp.showWeightPage());
                                            // todo: Il faudrait aussi modifier le graphXML
                                        }
                                    });
                                    gIndex = graphXml.addDiWeightedEdge(nameVertexStart, groupEnd.getId(),weight.getText());
                                    updateGroup(currentTab, gIndex);
                                    break;
                            }

                            // Edge that follow the mouse
                            currPage.setOnMouseMoved(null);
                            currP.getChildren().remove(0);

                            firstVerForEdge = true;
                            if(edge1ActiveOnce)
                            {
                                edge1ActiveOnce = false;
                                switch (graphType) {
                                    case "nonDiGraph":
                                        edgeButton.setId("edgeButton");
                                        break;
                                    case "weightedNonDiGraph":
                                        weightedEdgeButton.setId("weightedEdgeButton");
                                    case "diGraph":
                                        diEdgeButton.setId("diEdgeButton");
                                        break;
                                    case "weightedDiGraph":
                                        weightedDiEdgeButton.setId("weightedDiEdgeButton");
                                }
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

                    Shape shape = (Shape)t.getSource();
                    Group group = (Group)shape.getParent();
                    group.setCursor(Cursor.CLOSED_HAND);


                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = (orgTranslateX + offsetX)/sliderValue;
                    double newTranslateY = (orgTranslateY + offsetY)/sliderValue;
                    if(newTranslateX < RAYON_CERCLE) // 10 est le rayon du cercle
                        newTranslateX = RAYON_CERCLE;
                    if(newTranslateX > tabPane.getWidth() - RAYON_CERCLE)
                        newTranslateX = tabPane.getWidth() - RAYON_CERCLE;
                    if(newTranslateY <RAYON_CERCLE)
                        newTranslateY = RAYON_CERCLE;
                    // tab height = 33
                    if(newTranslateY > tabPane.getHeight() - RAYON_CERCLE -33)
                        newTranslateY = tabPane.getHeight() - RAYON_CERCLE -33;
                    group.setTranslateX(newTranslateX);
                    group.setTranslateY(newTranslateY);

                    //the vertex has been moved so we need to change it position in the xml
                    Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
                    GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
                    graphXml.setPosOfVertex(group.getId(),(int) newTranslateX, (int) newTranslateY);
                }
            };

    EventHandler<MouseEvent> followMouseHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    if(!edge1Active && !edge1ActiveOnce) {
                        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
                        AnchorPane currPage = (AnchorPane) currentTab.getContent();
                        AnchorPane currP = (AnchorPane) currPage.getChildren().get(0);

                        currPage.setOnMouseMoved(null);
                        currP.getChildren().remove(0);
                    }

                    liveEdge.setEndX(t.getX());
                    liveEdge.setEndY(t.getY());
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


    private void moveVertexMoveEdgeListenerDraw(Group groupStart, Group groupEnd, int drawEdgeIndex, AnchorPane currP)
    {

        //////////////////////////////////moving vertex move edges////////////////////
        groupEnd.translateXProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                DrawEdge drawEdge = drawEdgesList.get(drawEdgeIndex);
                double startX = drawEdge.getStartX();
                double startY = drawEdge.getStartY();
                double endX = drawEdge.getEndX();
                double endY = drawEdge.getEndY();
                Text text = drawEdge.getText();
                boolean directed = drawEdge.isDirected();
                int bending = drawEdge.getBending();
                int bendFactor = drawEdge.getBendFactor();
                currP.getChildren().remove(drawEdgesList.get(drawEdgeIndex).getRoot());
                drawEdgesList.remove(drawEdgeIndex);
                drawEdgesList.add(drawEdgeIndex,new DrawEdge(startX, startY, (double)newValue, endY, bending, bendFactor, directed, text));
                currP.getChildren().add(1,drawEdgesList.get(drawEdgeIndex).getRoot());
            }
        });
        groupEnd.translateYProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                DrawEdge drawEdge = drawEdgesList.get(drawEdgeIndex);
                double startX = drawEdge.getStartX();
                double startY = drawEdge.getStartY();
                double endX = drawEdge.getEndX();
                double endY = drawEdge.getEndY();
                Text text = drawEdge.getText();
                boolean directed = drawEdge.isDirected();
                int bending = drawEdge.getBending();
                int bendFactor = drawEdge.getBendFactor();
                currP.getChildren().remove(drawEdgesList.get(drawEdgeIndex).getRoot());
                drawEdgesList.remove(drawEdgeIndex);
                drawEdgesList.add(drawEdgeIndex,new DrawEdge(startX, startY, endX , (double)newValue, bending, bendFactor, directed, text));
                currP.getChildren().add(1,drawEdgesList.get(drawEdgeIndex).getRoot());
            }
        });
        groupStart.translateXProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                DrawEdge drawEdge = drawEdgesList.get(drawEdgeIndex);
                double startX = drawEdge.getStartX();
                double startY = drawEdge.getStartY();
                double endX = drawEdge.getEndX();
                double endY = drawEdge.getEndY();
                Text text = drawEdge.getText();
                boolean directed = drawEdge.isDirected();
                int bending = drawEdge.getBending();
                int bendFactor = drawEdge.getBendFactor();
                currP.getChildren().remove(drawEdgesList.get(drawEdgeIndex).getRoot());
                drawEdgesList.remove(drawEdgeIndex);
                drawEdgesList.add(drawEdgeIndex,new DrawEdge((double)newValue, startY, endX , endY, bending, bendFactor, directed, text));
                currP.getChildren().add(1,drawEdgesList.get(drawEdgeIndex).getRoot());
            }
        });

        groupStart.translateYProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                DrawEdge drawEdge = drawEdgesList.get(drawEdgeIndex);
                double startX = drawEdge.getStartX();
                double startY = drawEdge.getStartY();
                double endX = drawEdge.getEndX();
                double endY = drawEdge.getEndY();
                Text text = drawEdge.getText();
                boolean directed = drawEdge.isDirected();
                int bending = drawEdge.getBending();
                int bendFactor = drawEdge.getBendFactor();
                currP.getChildren().remove(drawEdgesList.get(drawEdgeIndex).getRoot());
                drawEdgesList.remove(drawEdgeIndex);
                drawEdgesList.add(drawEdgeIndex,new DrawEdge(startX, (double)newValue, endX , endY, bending, bendFactor, directed, text));
                currP.getChildren().add(1,drawEdgesList.get(drawEdgeIndex).getRoot());
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

            private Node getChildrenVertexById(AnchorPane pane, String id)
            {
                for(int i = 0; i < pane.getChildren().size(); i++)
                {
                    Node node = pane.getChildren().get(i);
                    if(node.getClass().equals(Group.class))
                    {
                        Group group = (Group) node;
                        if(group.getChildren().get(0).getClass().equals(Circle.class) && group.getId().equals(id) ) {
                            return node;
                        }
                    }
                }
                return null;
            }

    private void updateGroup(Tab tab, int index) {
        AnchorPane currPage = (AnchorPane) tab.getContent();
        AnchorPane currP = (AnchorPane) currPage.getChildren().get(0);
        GraphDom graphXml = getXmlOfThisTab(tab.getId());

        if(drawEdgesGroupList.size() == index) {
            drawEdgesGroupList.add(graphXml.getDrawEdges(index));
        }
        else {
            for(DrawEdge drawEdge: drawEdgesGroupList.get(index)) {
                currP.getChildren().remove(drawEdge.getRoot());
                drawEdgesList.remove(drawEdge);
            }

            drawEdgesGroupList.set(index, getXmlOfThisTab(tab.getId()).getDrawEdges(index));
        }
        for(int i = 0; i < drawEdgesGroupList.get(index).size(); i++) {
            Group cirleStart = (Group) getChildrenVertexById(currP, graphXml.getEdgeStartName(index, i));
            Group cirleEnd = (Group) getChildrenVertexById(currP, graphXml.getEdgeEndName(index, i));

            DrawEdge drawEdge = drawEdgesGroupList.get(index).get(i);

            drawEdgesList.add(drawEdge);
            moveVertexMoveEdgeListenerDraw(cirleStart, cirleEnd, drawEdgesList.indexOf(drawEdge), currP);
            currP.getChildren().add(1, drawEdge.getRoot());
        }
    }
}