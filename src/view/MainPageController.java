package view; /**
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mainProgram.MainApp;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPageController
{
    private ArrayList<GraphDom> listGraphXml; //list of xml files, from different tabs
    private MainApp mainApp;

    private final float RAYON_CERCLE = 10f;


    private final String EDGE1                            = "edge1";
    private final String VERTEX1                          = "vertex1";
    private final String DIGRAPH                          = "diGraph";
    private final String NONDIGRAPH                       = "nonDiGraph";
    private final String EDGEBUTTON                       = "edgeButton";
    private final String ERASEBUTTON                      = "eraserButton";
    private final String ORANGECOLOR                      = "da5630";
    private final String DIEDGEBUTTON                     = "diEdgeButton";
    private final String TABNAMEPREFIX                    = "new tab";
    private final String VERTEX1BUTTON                    = "vertex1Button";
    private final String WEIGHTEDDIGRAPH                  = "weightedDiGraph";
    private final String PREFIXVERTEXNAME                 = "ver_";
    private final String WEIGHTEDNONDIGRAPH               = "weightedNonDiGraph";
    private final String EDGEBUTTONACTIVATE               = "edgeButtonActivate";
    private final String WEIGHTEDEDGEBUTTON               = "weightedEdgeButton";
    private final String WEIGHTEDDIEDGEBUTTON             = "weightedDiEdgeButton";
    private final String DIEDGEBUTTONACTIVATE             = "diEdgeButtonActivate";
    private final String ERASERBUTTONACTIVATE             = "eraserButtonActivate";
    private final String VERTEX1BUTTONACTIVATE            = "vertex1ButtonActivate";
    private final String WEIGHTEDEDGEBUTTONACTIVATE       = "weightedEdgeButtonActivate";
    private final String WEIGHTEDDIEDGEBUTTONACTIVATE     = "weightedDiEdgeButtonActivate";

    //////////////////////buttons active or not/////////////////////////////////////////////////////////////////////////
    private boolean vertex1Active     = false;
    private boolean vertex1ActiveOnce = false;

    private boolean edge1Active     = false;
    private boolean edge1ActiveOnce = false;

    private boolean eraserActive     = false;
    private boolean eraserActiveOnce = false;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private float textTranslateX2Digits;
    private float textTranslateX1Digit;
    private float textTranslateY;

    private Map<Tab, ArrayList<ArrayList<DrawEdge>>> tabDrawEdgesList;

    private boolean firstVerForEdge = true;
    private Map<Tab, String> tabMap;
    private String nameVertexStart;
    private Group groupStart;
    private Tab cTab;

    @FXML
    private Button weightedDiEdgeButton;
    @FXML
    private Button weightedEdgeButton;
    @FXML
    private Button vertex1Button;
    @FXML
    private Button diEdgeButton;
    @FXML
    private AnchorPane greyMain;
    @FXML
    private Button eraserButton;
    @FXML
    private Button edgeButton;
    @FXML
    private TabPane tabPane;

    private double orgTranslateX, orgTranslateY;
    private double orgSceneX, orgSceneY;
    private int indiceTab;

    private Line liveEdge;

    /**
     * we can get the current pane, who is inside of the current Tab
     * @return AnchorePane who contains the graph part of the current Tab
     */
    public Pane getGraphPane()
    {
        Tab currentTab      = (Tab)tabPane.getSelectionModel().getSelectedItem();
        AnchorPane currPage = (AnchorPane) currentTab.getContent();

        return currPage;
    }

    /**
     * constructor put 0 to the indiceTab.
     */
    public MainPageController(){
        indiceTab = 0;
    }

    /**
     * called when, the window is shown.
     * instanciation of some variables
     * and call hadleNew to create first Tab
     */
    @FXML
    private void initialize()
    {
        listGraphXml = new ArrayList<>();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);

        //tabDrawEdgesList.get(cTab) = new ArrayList<>();
        tabDrawEdgesList   = new HashMap<>();
        tabMap             = new HashMap<>();

        DraggingTabPaneSupport support = new DraggingTabPaneSupport();
        handleNew();
        support.addSupport(tabPane);

        liveEdge = new Line();
        liveEdge.setSmooth(true);
        liveEdge.setStrokeWidth(4);
        liveEdge.setStroke(Color.web(ORANGECOLOR));


        textTranslateY        = 6;
        textTranslateX1Digit  = -4.5f;
        textTranslateX2Digits = -8.5f;

        if(Screen.getPrimary().getBounds().getMaxX() == 1920 && Screen.getPrimary().getBounds().getMaxY() == 1080)
        {
            textTranslateY        = 6;
            textTranslateX1Digit  = -4.5f;
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

    /**
     *
     * @param b boolean to choose if greyMain, the principal AnchorPane, is visible or not
     */
    public void setGreyMain(Boolean b)
    {
        greyMain.setVisible(b);
    }

    /**
     * Called when click on the Launch button (up-right)
     * if everything is oke, call the showAlgoPage() of the mainApp,
     * to launch the new window
     */
    @FXML
    private void handleLaunch()
    {
        //get the xml of the current graph to send it to showAlgoPage()
        Tab currentTab    = (Tab)tabPane.getSelectionModel().getSelectedItem();
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());

        //if number of vertex in the xml is -1, (no vertexes in the graph)
        // we cannot launch the page of graph's resolution, so alert message pops up
        if(graphXml.getNbVertex() < 0)
        {
            Alert alert           = new Alert(Alert.AlertType.ERROR, "Votre graphe est vide");
            DialogPane dialogPane = alert.getDialogPane();

            dialogPane.getStylesheets().add(getClass().getResource("/assets/css/alert.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();

            return;
        }

        //calling the showAlgoPage, whit the current view.GraphDom (graphxml)
        mainApp.showAlgoPage(graphXml);
    }

    /**
     * create a new Tab whit a OnClosedListener
     */
    @FXML
    private void handleNew()
    {
        //create new tab whitout name to get one name like new tab 3, 4 ..
        Tab tab = createNewTab("");

        tab.setOnClosed(new EventHandler<Event>(){
            @Override
            public void handle(Event e)
            {
                //if we closed the last tab => we create a new Tab,
                // we don't want to have no Tab in the TabPane
                if(tabPane.getTabs().size() < 1) {
                    tabDrawEdgesList.remove(cTab);
                    handleNew();
                }
            }
        });

        tabPane.getTabs().add(tab);
        cTab = tab;
        tabDrawEdgesList.put(tab, new ArrayList());

        //creation of the xml file of this tab
        try {
            GraphDom currentGraphDom = new GraphDom(tab.getId());

            //adding this xml to the list
            listGraphXml.add(currentGraphDom);
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * to create a vertex, and his number, 0.... n-1
     * @param x position x of the vertex to create
     * @param y position y of the vertex to create
     * @param id name of the vertex to create
     * @return group constitued of a Circle (the vertex), and a Text on it (the number of the vertex)
     */
    private Group createVertex (double x, double y, String id){

        Circle circle_base = new Circle(RAYON_CERCLE, Color.web(ORANGECOLOR));
        circle_base.setId(id);

        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();

        AnchorPane currentPage = (AnchorPane) currentTab.getContent();
        AnchorPane currentPane = (AnchorPane) currentPage.getChildren().get(0);

        //we get the current slider, of the current Tab (the slider of other Tabs can be in other positions)
        Slider currentSlider = (Slider) currentPage.getChildren().get(1);

        //adding Event Listeners to the circle
        circle_base.setOnMousePressed(nodeOnMousePressedEventHandler);
        circle_base.setOnMouseDragged(nodeOnMouseDraggedEventHandler);
        circle_base.setOnMouseEntered(nodeOnMouseEnteredEventHandler);
        circle_base.setOnMouseReleased(nodeOnMouseReleasedEventHandler);

        int nbVertex = Integer.parseInt(id.replaceAll("[\\D]", ""));
        Text text    = new Text(""+nbVertex);

        text.setId(id);
        text.setBoundsType(TextBoundsType.VISUAL);

        //centering the numbers
        if(nbVertex<10)
        {
            text.setTranslateX(textTranslateX1Digit);
            text.setTranslateY(textTranslateY);
        }
        else
        {
            text.setTranslateX(textTranslateX2Digits);
            text.setTranslateY(textTranslateY);
        }

        //adding Event Listeners to the Text
        text.setOnMousePressed(nodeOnMousePressedEventHandler);
        text.setOnMouseDragged(nodeOnMouseDraggedEventHandler);
        text.setOnMouseEntered(nodeOnMouseEnteredEventHandler);
        text.setOnMouseReleased(nodeOnMouseReleasedEventHandler);

        //creating the Group
        Group vertex = new Group();
        vertex.getChildren().addAll(circle_base,text);
        vertex.setId(id);
        vertex.setTranslateX((x - currentPane.getTranslateX() )/currentSlider.getValue());
        vertex.setTranslateY((y - currentPane.getTranslateY() )/currentSlider.getValue());
        return vertex;
    }

    public void handleNewFromAlgoPage(GraphDom graphAlgoPage){
/*
 *********************************************************************************************************************
 ***Cette fonction est commenté car nous avons jusqu'au dernier jour tenté de faire fonctionner handleNewFromAlgo ****
 ***Cette fonction était en optionnelle dans notre cahier des charges. Elle permettait, après avoir appliquer un  ****
 ***algorithme sur un graphe, de pouvoir recupérer le graphe résultant, et de le charger sur la main page, pour  *****
 ***pouvoir l'enregister, continuer de travailler dessus (ajout de nouveaux sommets, suppression de certains, ou *****
 ***relancer un deuxième algorithme sur ce graphe). Comme ce n'était pas tout à fait en ordre, nous le commentons ****
 *********************************************************************************************************************
            //if no graphDom had been selected, we do nothing
            if(graphAlgoPage == null) return;

            //adding this xml to the list
            listGraphXml.add(graphAlgoPage);
            Tab tab = createNewTab(graphAlgoPage.getName());

            tab.setOnClosed(new EventHandler<Event>(){
                @Override
                public void handle(Event e){
                    if(tabPane.getTabs().size() < 1)
                        handleNew();
                }
            });

            //get the Anchorpane who we need to fill
            AnchorPane paneBack = (AnchorPane) tab.getContent();
            AnchorPane pane     = (AnchorPane) paneBack.getChildren().get(0);

            //adding all vertex from xml
            int i = 0;
            while (i <= graphAlgoPage.getNbVertex())
            {
                Point2D point = graphAlgoPage.getPosOfVertex(i);
                int x = (int) point.getX();
                int y = (int) point.getY();

                String name = graphAlgoPage.getVertexName(i);
                // Create vertex and add it to pane
                Group node = createVertex(x,y,name);
                pane.getChildren().add(node);

                i++;
            }

            tabMap.put(tab,graphAlgoPage.getGraphType());

            view.DrawEdge drawEdge = null;

            i = 0;
            while (i < graphAlgoPage.getNbGroup())
            {
                ArrayList<view.DrawEdge> edges = graphAlgoPage.getDrawEdges(i);
                tabDrawEdgesList.get(cTab).add(edges);

                for(int j = 0; j < edges.size(); j++)
                {
                    Group cirleStart = (Group) getChildrenVertexById(pane, graphAlgoPage.getEdgeStartName(i, j));
                    Group cirleEnd   = (Group) getChildrenVertexById(pane, graphAlgoPage.getEdgeEndName(i, j));

                    moveVertexMoveEdgeListenerDraw(cirleStart, cirleEnd, i, j, tab, pane);
                    pane.getChildren().add(1, edges.get(j).getRoot());
                }
                i++;
            }

            tabPane.getTabs().add(tab);
            */
    }

    /**
     * called when we click on the Pane (center of the page)
     * If some vertex adding button is active, we have to add
     * a vertex at the position of the click
     *
     * @param mouseEvent The mouse Event when we clicked on the pane. critical info, of position etc.
     */
    @FXML
    private void panePressed(MouseEvent mouseEvent)
    {
        Tab currentTab = (Tab)tabPane.getSelectionModel().getSelectedItem();
        cTab = currentTab;


        //if the vertex button is active (once or always)
        if(vertex1Active || vertex1ActiveOnce)
        {
            //get currentPane to know the slider position
            AnchorPane anch    = (AnchorPane) currentTab.getContent();
            Slider slider      = (Slider) anch.getChildren().get(1);
            double sliderValue = slider.getValue();

            //adding this vertex to the xml file
            GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
            // If slider
            //graphXml.addVertex((int)(mouseEvent.getX()*sliderValue), (int)(mouseEvent.getY()*sliderValue));
            graphXml.addVertex((int)mouseEvent.getX(), (int)mouseEvent.getY());

            String nameOfVertex = PREFIXVERTEXNAME + graphXml.getNbVertex();

            AnchorPane currPage = (AnchorPane) currentTab.getContent();
            AnchorPane currP    = (AnchorPane) currPage.getChildren().get(0);

            Group node = createVertex(mouseEvent.getX(), mouseEvent.getY(), nameOfVertex);
            currP.getChildren().add(node);


            //if we have the click once. we desactivate it
            vertex1ActiveOnce = false;
            if(!vertex1Active)
                vertex1Button.setId(VERTEX1BUTTON);//let the butonn orange if he is used
        }
    }

    /**
     * when we click the save Button, we call showSavePage of the mainApp,
     * to have popup to save the file
     */
    @FXML
    private void handleSave()
    {
        Tab currentTab    = (Tab)tabPane.getSelectionModel().getSelectedItem();
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());

        mainApp.showSavePage(graphXml);
    }

    /**
     * when we click the open Button, we call showOpenPage of the mainApp,
     * to have popup to open the file.
     *  when its done, we verify if the graphDom is ok, if it is we have to create
     *  a Tab and read view.GraphDom to construct graphically its components
     */
    @FXML
    private void handleOpen()
    {
        try {
            GraphDom graphOpen = mainApp.showOpenPage();

            //if no graphDom had been selected, we do nothing
            if(graphOpen == null) return;

            //adding this view.GraphDom to the list
            listGraphXml.add(graphOpen);

            //creating new Tab whit the gxml Name
            Tab tab = createNewTab(graphOpen.getName());

            //onClosed listener of Tab
            tab.setOnClosed(new EventHandler<Event>(){
                @Override
                public void handle(Event e)
                {
                    if(tabPane.getTabs().size() < 1)
                    {
                        tabDrawEdgesList.remove(cTab);
                        handleNew();
                    }
                }
            });

            cTab = tab;
            tabDrawEdgesList.put(tab, new ArrayList());

            //get the Anchorpane who we need to fill
            AnchorPane paneBack = (AnchorPane) tab.getContent();
            AnchorPane pane     = (AnchorPane) paneBack.getChildren().get(0);

            //adding all vertex from view.GraphDom
            int i = 0;
            while (i <= graphOpen.getNbVertex())
            {
                //getting position of vertexes in the view.GraphDom
                Point2D point = graphOpen.getPosOfVertex(i);
                int x = (int) point.getX();
                int y = (int) point.getY();

                String name = graphOpen.getVertexName(i);

                // Create vertex and add it to pane
                Group node = createVertex(x,y,name);
                pane.getChildren().add(node);

                i++;
            }

            tabMap.put(tab,graphOpen.getGraphType());

            DrawEdge drawEdge = null;

            //adding all edges of the view.GraphDom
            i = 0;
            while (i < graphOpen.getNbGroup())
            {
                ArrayList<DrawEdge> edges = graphOpen.getDrawEdges(i);
                tabDrawEdgesList.get(cTab).add(edges);

                for(int j = 0; j < edges.size(); j++)
                {
                    Group cirleStart = (Group) getChildrenVertexById(pane, graphOpen.getEdgeStartName(i, j));
                    Group cirleEnd   = (Group) getChildrenVertexById(pane, graphOpen.getEdgeEndName(i, j));

                    moveVertexMoveEdgeListenerDraw(cirleStart, cirleEnd, i, j, tab, pane);
                    pane.getChildren().add(1, edges.get(j).getRoot());
                }
                i++;
            }

            tabPane.getTabs().add(tab);

        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * when the import button is clicked, we call the showImportPage() of mainApp,
     * to pop up the import Window
     */
    @FXML
    private void handleImport(){
        mainApp.showImportPage();
    }

    /**
     * called when helpButton is clicked. Show a new Window whit help
     */
    @FXML
    private void handleHelp()
    {
        //stage contening help
        Stage helpPage = new Stage();
        helpPage.setTitle("help Page");
        helpPage.setMinHeight(500);
        helpPage.setMinWidth(500);
        helpPage.setResizable(false);

        ScrollPane page = new ScrollPane();
        Image help1     = new Image(getClass().getResourceAsStream("/assets/img/help1.png"));

        page.setContent(new ImageView(help1));

        page.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        page.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        page.setPannable(true);
        page.setMaxHeight(600);
        page.setMaxWidth(600);

        Group root  = new Group();
        Scene scene = new Scene(root, 590, 590);
        helpPage.setScene(scene);

        root.getChildren().add(page);
        helpPage.show();
    }

    /**
     * when the buttton vertex is clicked we have to look at the MouseEvent
     * to see if its double-clicked or just clicked. We activate some boolean
     * with the differents mouseEvents
     * @param mouseEvent mouseEvent of the click
     */
    @FXML
    private void handleVectrice(MouseEvent mouseEvent)
    {
        // Desactivate edge or eraser if active
        desactivateEdge();
        desactivateEraser();

        // Reset to the first edge
        firstVerForEdge = true;

        int count = mouseEvent.getClickCount();
        if (count == 2 && !isOtherButtonActivate(VERTEX1)) // if we double click, we can put infinite vertex
        {
            vertex1Active     = true;
            vertex1ActiveOnce = false;

            vertex1Button.setId(VERTEX1BUTTONACTIVATE);//let the button orange if he is used
        }
        else if (count == 1 && (vertex1ActiveOnce || vertex1Active))// if we click and if we are activate, we desactive
        {
            vertex1Active     = false;
            vertex1ActiveOnce = false;
            vertex1Button.setId(VERTEX1BUTTON);// desactivate button of orange
        }
        else if (count == 1 && !isOtherButtonActivate(VERTEX1))// if we clicked and we are desactivate, we active
        {
            vertex1ActiveOnce = true;
            vertex1Active     = false;
            vertex1Button.setId(VERTEX1BUTTONACTIVATE);//let the button orange if he is used
        }
    }

    /**
     * called when need to desactivate the activation of vertex button
     */
    private void desactivateVertex()
    {
        vertex1Active     = false;
        vertex1ActiveOnce = false;
        vertex1Button.setId(VERTEX1BUTTON);// desactivate button of orange
    }

    /**
     * when the buttton edge is clicked we have to look at the MouseEvent
     * to see if its double-clicked or just clicked. We activate some boolean
     * with the differents mouseEvents
     * @param mouseEvent mouseEvent of the click
     */
    @FXML
    private void handleEdge(MouseEvent mouseEvent)
    {
        Tab currentTab    = (Tab)tabPane.getSelectionModel().getSelectedItem();
        String graphType  = tabMap.get(currentTab);
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());

        // if graphType is null, it's beacause it's the first time
        // we click on a edge button (directed, weighted etc.),
        // so we set the corresponding type to the dom
        if(graphType == null)
        {
            tabMap.put(currentTab, NONDIGRAPH);
            graphXml.setGraphType(NONDIGRAPH);
        }
        else if(!graphType.equals(NONDIGRAPH))
        {
            mouseEvent.consume();
            return;
        }

        //desactivate vertex or eraser if active
        desactivateVertex();
        desactivateEraser();

        // Reset to the first edge
        firstVerForEdge = true;

        int count = mouseEvent.getClickCount();
        if (count == 2 && !isOtherButtonActivate(EDGE1)) // if we double click, we can put infinite edges
        {
            edge1Active     = true;
            edge1ActiveOnce = false;

            edgeButton.setId(EDGEBUTTONACTIVATE);//let the button orange if he is used
        }
        else if (count == 1 && (edge1ActiveOnce || edge1Active))// if we click and if we are activate, we desactive
        {
            edge1Active = false;
            edge1ActiveOnce = false;
            edgeButton.setId(EDGEBUTTON);// desactivate button of orange
        }
        else if (count == 1 && !isOtherButtonActivate(EDGE1))// if we clicked and we are desactivate, we active
        {
            edge1ActiveOnce = true;
            edge1Active = false;
            edgeButton.setId(EDGEBUTTONACTIVATE);//let the button orange if he is used
        }
    }

    /**
     * when the buttton weightededge is clicked we have to look at the MouseEvent
     * to see if its double-clicked or just clicked. We activate some boolean
     * with the differents mouseEvents
     * @param mouseEvent mouseEvent of the click
     */
    @FXML
    private void handleWeightedEdge(MouseEvent mouseEvent)
    {
        Tab currentTab    = (Tab)tabPane.getSelectionModel().getSelectedItem();
        String graphType  = tabMap.get(currentTab);
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());

        // if graphType is null, it's beacause it's the first time
        // we click on a edge button (directed, weighted etc.),
        // so we set the corresponding type to the dom
        if(graphType == null)
        {
            tabMap.put(currentTab, WEIGHTEDNONDIGRAPH);
            if (graphXml != null)
            {
                graphXml.setGraphType(WEIGHTEDNONDIGRAPH);
            }
        }
        else if(!graphType.equals(WEIGHTEDNONDIGRAPH))
        {
            mouseEvent.consume();
            return;
        }
        //desactivate vertex or eraser if active
        desactivateVertex();
        desactivateEraser();

        // Reset to the first edge
        firstVerForEdge = true;

        int count = mouseEvent.getClickCount();
        if (count == 2 && !isOtherButtonActivate(EDGE1)) // if we double click, we can put infinite edges
        {
            edge1Active     = true;
            edge1ActiveOnce = false;
            weightedEdgeButton.setId(WEIGHTEDEDGEBUTTONACTIVATE);//let the button orange if he is used
        }
        else if (count == 1 && (edge1ActiveOnce || edge1Active))// if we click and if we are activate, we desactive
        {
            edge1Active     = false;
            edge1ActiveOnce = false;
            weightedEdgeButton.setId(WEIGHTEDEDGEBUTTON);// desactivate button of orange
        }
        else if (count == 1 /*&& (!edge1ActiveOnce || !edge1Active)*/ && !isOtherButtonActivate(EDGE1))// if we clicked and we are desactivate, we active
        {
            edge1Active     = false;
            edge1ActiveOnce = true;
            weightedEdgeButton.setId(WEIGHTEDEDGEBUTTONACTIVATE);//let the button orange if he is used
        }
    }

    /**
     * when the buttton diedge is clicked we have to look at the MouseEvent
     * to see if its double-clicked or just clicked. We activate some boolean
     * with the differents mouseEvents
     * @param mouseEvent mouseEvent of the click
     */
    @FXML
    private void handleDiEdge(MouseEvent mouseEvent)
    {
        Tab currentTab    = (Tab)tabPane.getSelectionModel().getSelectedItem();
        String graphType  = tabMap.get(currentTab);
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());

        // if graphType is null, it's beacause it's the first time
        // we click on a diedge button (directed, weighted etc.),
        // so we set the corresponding type to the dom
        if(graphType == null)
        {
            tabMap.put(currentTab, DIGRAPH);
            if (graphXml != null) {
                graphXml.setGraphType(DIGRAPH);
            }
        }
        else if(!graphType.equals(DIGRAPH))
        {
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
            edge1Active     = true;
            edge1ActiveOnce = false;
            diEdgeButton.setId(DIEDGEBUTTONACTIVATE);//let the button orange if he is used
        }
        else if (count == 1 && (edge1ActiveOnce || edge1Active))// if we click and if we are activate, we desactive
        {
            edge1Active     = false;
            edge1ActiveOnce = false;
            diEdgeButton.setId(DIEDGEBUTTON);// desactivate button of orange
        }
        else if (count == 1 && (!edge1ActiveOnce || !edge1Active))// if we clicked and we are desactivate, we active
        {
            edge1Active     = false;
            edge1ActiveOnce = true;
            diEdgeButton.setId(DIEDGEBUTTONACTIVATE);//let the button orange if he is used
        }
    }

    /**
     * when the buttton weighteddiedge is clicked we have to look at the MouseEvent
     * to see if its double-clicked or just clicked. We activate some boolean
     * with the differents mouseEvents
     * @param mouseEvent mouseEvent of the click
     */
    @FXML
    private void handleWeightedDiEdge(MouseEvent mouseEvent)
    {
        Tab currentTab    = (Tab)tabPane.getSelectionModel().getSelectedItem();
        String graphType  = tabMap.get(currentTab);
        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());

        // if graphType is null, it's beacause it's the first time
        // we click on a weighteddiedge button (directed, weighted etc.),
        // so we set the corresponding type to the dom
        if(graphType == null)
        {
            tabMap.put(currentTab, WEIGHTEDDIGRAPH);
            if (graphXml != null)
            {
                graphXml.setGraphType(WEIGHTEDDIGRAPH);
            }
        }
        else if(!graphType.equals(WEIGHTEDDIGRAPH))
        {
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
            weightedDiEdgeButton.setId(WEIGHTEDDIEDGEBUTTONACTIVATE);//let the button orange if he is used
        }
        else if (count == 1 && (edge1ActiveOnce || edge1Active))// if we click and if we are activate, we desactive
        {
            edge1Active = false;
            edge1ActiveOnce = false;
            weightedDiEdgeButton.setId(WEIGHTEDDIEDGEBUTTON);// desactivate button of orange
        }
        else if (count == 1 && (!edge1ActiveOnce || !edge1Active))// if we clicked and we are desactivate, we active
        {
            edge1ActiveOnce = true;
            edge1Active = false;
            weightedDiEdgeButton.setId(WEIGHTEDDIEDGEBUTTONACTIVATE);//let the button orange if he is used
        }
    }

    /**
     * desactivate all edges button, called when we click in vertex button for example
     */
    private void desactivateEdge()
    {
        edge1Active     = false;
        edge1ActiveOnce = false;

        // desactivate button of orange
        edgeButton.setId(EDGEBUTTON);
        diEdgeButton.setId(DIEDGEBUTTON);
        weightedDiEdgeButton.setId(WEIGHTEDDIEDGEBUTTON);
        weightedEdgeButton.setId(WEIGHTEDEDGEBUTTON);
    }


    /**
     * when the buttton eraser is clicked we have to look at the MouseEvent
     * to see if its double-clicked or just clicked. We activate some boolean
     * with the differents mouseEvents
     * @param mouseEvent mouseEvent of the click
     */
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
            eraserButton.setId(ERASERBUTTONACTIVATE);//let the button orange if he is used
        }
        else if (count == 1 && (eraserActiveOnce ||eraserActive))// if we click and if we are activate, we desactive
        {
            eraserActive = false;
            eraserActiveOnce = false;
            eraserButton.setId(ERASEBUTTON);// desactivate button of orange
        }
        else if (count == 1 && (!eraserActiveOnce || !eraserActive))// if we clicked and we are desactivate, we active
        {
            eraserActive = false;
            eraserActiveOnce = true;
            eraserButton.setId(ERASERBUTTONACTIVATE);//let the button orange if he is used
        }
    }

    /**
     * desactivate the eraser Button
     */
    private void desactivateEraser()
    {
        eraserActive     = false;
        eraserActiveOnce = false;
        eraserButton.setId(ERASEBUTTON);// desactivate button of orange
    }

    /**
     * listener when we are above some element with the mouse
     */
    private EventHandler<MouseEvent> nodeOnMouseEnteredEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t)
                {
                    Shape shape = (Shape)t.getSource();
                    Group group = (Group)shape.getParent();
                    group.setCursor(Cursor.HAND);

                }
            };

    /**
     * listener when the mouse is pressed, above elements (vertexes)
     * deleting if the eraser button is activate
     */
    private EventHandler<MouseEvent> nodeOnMousePressedEventHandler =

            new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent t)
                {
                    Shape shape = (Shape)t.getSource();
                    Group group = (Group)shape.getParent();

                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();

                    //get currentPane to know the slider position
                    AnchorPane anch    = (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
                    Slider slider      = (Slider) anch.getChildren().get(1);
                    double sliderValue = slider.getValue();

                    orgTranslateX = group.getTranslateX()*sliderValue;
                    orgTranslateY = group.getTranslateY()*sliderValue;

                    group.setCursor(Cursor.CLOSED_HAND);

                    //delete if the erease is activate
                    if(eraserActiveOnce || eraserActive)
                    {
                        Tab currentTab    = (Tab)tabPane.getSelectionModel().getSelectedItem();
                        GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
                        if (graphXml != null)
                        {
                            graphXml.deleteVertex(Integer.valueOf(group.getId().substring(4)));
                        }

                        //deleting graphical elements
                        AnchorPane ap = (AnchorPane) group.getParent();
                        ap.getChildren().clear();

                        //adding all vertex from xml
                        int i = 0;
                        while (i <= graphXml.getNbVertex())
                        {
                            Point2D point = graphXml.getPosOfVertex(i);
                            int x = (int) point.getX();
                            int y = (int) point.getY();

                            String name = graphXml.getVertexName(i);
                            Group node  = createVertex(x,y,name);

                            ap.getChildren().add(node);

                            i++;
                        }

                        DrawEdge drawEdge = null;

                        tabDrawEdgesList.get(cTab).clear();

                        i = 0;
                        while (i < graphXml.getNbGroup())
                        {
                            ArrayList<DrawEdge> edges = graphXml.getDrawEdges(i);
                            tabDrawEdgesList.get(cTab).add(edges);

                            for(int j = 0; j < edges.size(); j++)
                            {
                                Group cirleStart = (Group) getChildrenVertexById(ap, graphXml.getEdgeStartName(i, j));
                                Group cirleEnd   = (Group) getChildrenVertexById(ap, graphXml.getEdgeEndName(i, j));

                                moveVertexMoveEdgeListenerDraw(cirleStart, cirleEnd, i, j, currentTab, ap);
                                ap.getChildren().add(1, edges.get(j).getRoot());
                            }
                            i++;
                        }

                        //if eraserActive just once, desactivate , so we cannot erase again
                        if(eraserActiveOnce)
                        {
                            desactivateEraser();
                        }
                    }
                }
            };

    /**
     * when the mouse click is released in the principal pane, to draw edges
     * we verify if we click in a vertex, and if its the first we click, to have
     * start of the edge, et second click to have the end.
     */
    private EventHandler<MouseEvent> nodeOnMouseReleasedEventHandler =

            new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent t)
                {
                    Shape shape = (Shape)t.getSource();
                    Group group = (Group)shape.getParent();
                    group.setCursor(Cursor.HAND);


                    //Do when the edge button is activate to draw a edge.
                    Tab currentTab      = (Tab)tabPane.getSelectionModel().getSelectedItem();
                    AnchorPane currPage = (AnchorPane) currentTab.getContent();
                    AnchorPane currP    = (AnchorPane) currPage.getChildren().get(0);
                    String graphType    = tabMap.get(currentTab);

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

                        }
                        else if(groupStart != group)
                        {

                            Group groupEnd    = group;
                            GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
                            DrawEdge drawEdge = null;
                            ArrayList<DrawEdge> drawEdges;
                            int gIndex;
                            Text weight;
                            switch (graphType)
                            {
                                case NONDIGRAPH:

                                    if(graphXml.getEdge(Integer.parseInt(groupStart.getId().replaceAll("[\\D]", "")), Integer.parseInt(groupEnd.getId().replaceAll("[\\D]", "")), 0)!=null) break;
                                    gIndex = graphXml.addEdge(nameVertexStart, groupEnd.getId());
                                    updateGroup(currentTab, gIndex);
                                    break;
                                case WEIGHTEDNONDIGRAPH:

                                      weight = new Text(""+mainApp.showWeightPage());
                                    //so we can modify the text
                                    weight.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        public void handle(MouseEvent event) {
                                            weight.setText(""+mainApp.showWeightPage());
                                        }
                                    });
                                    gIndex = graphXml.addWeightedEdge(nameVertexStart, groupEnd.getId(),weight.getText());
                                    updateGroup(currentTab, gIndex);
                                    break;
                                case DIGRAPH:

                                    gIndex = graphXml.addDiEdge(nameVertexStart, groupEnd.getId());
                                    updateGroup(currentTab, gIndex);
                                    break;
                                case WEIGHTEDDIGRAPH:

                                    weight = new Text(""+mainApp.showWeightPage());
                                    // so we can modify the text
                                    weight.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        public void handle(MouseEvent event) {
                                            weight.setText(""+mainApp.showWeightPage());
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
                                switch (graphType)
                                {
                                    case NONDIGRAPH:

                                        edgeButton.setId(EDGEBUTTON);
                                        break;
                                    case WEIGHTEDNONDIGRAPH:

                                        weightedEdgeButton.setId(WEIGHTEDEDGEBUTTON);
                                        break;
                                    case DIGRAPH:

                                        diEdgeButton.setId(DIEDGEBUTTON);
                                        break;
                                    case WEIGHTEDDIGRAPH:

                                        weightedDiEdgeButton.setId(WEIGHTEDDIEDGEBUTTON);
                                }
                            }
                        }
                    }
                }
            };

    /**
     * on mousedragged listener to move the vertex when we stay clicked and move in the pane
     */
    EventHandler<MouseEvent> nodeOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    //get currentPane to know the slider position
                    AnchorPane anch    = (AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent();
                    Slider slider      = (Slider) anch.getChildren().get(1);
                    double sliderValue = slider.getValue();

                    Shape shape = (Shape)t.getSource();
                    Group group = (Group)shape.getParent();
                    group.setCursor(Cursor.CLOSED_HAND);

                    double offsetX       = t.getSceneX() - orgSceneX;
                    double offsetY       = t.getSceneY() - orgSceneY;
                    double newTranslateX = (orgTranslateX + offsetX)/sliderValue;
                    double newTranslateY = (orgTranslateY + offsetY)/sliderValue;

                    if(newTranslateX < RAYON_CERCLE)
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

                    //the vertex has been moved so we need to change it position in the graphDom
                    Tab currentTab    = (Tab)tabPane.getSelectionModel().getSelectedItem();
                    GraphDom graphXml = getXmlOfThisTab(currentTab.getId());
                    graphXml.setPosOfVertex(group.getId(),(int) newTranslateX, (int) newTranslateY);
                }
            };


    private EventHandler<MouseEvent> followMouseHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    if(!edge1Active && !edge1ActiveOnce) {
                        Tab currentTab      = (Tab)tabPane.getSelectionModel().getSelectedItem();
                        AnchorPane currPage = (AnchorPane) currentTab.getContent();
                        AnchorPane currP    = (AnchorPane) currPage.getChildren().get(0);

                        currPage.setOnMouseMoved(null);
                        currP.getChildren().remove(0);
                    }

                    liveEdge.setEndX(t.getX());
                    liveEdge.setEndY(t.getY());
                }
            };

    /**
     * return the graphDom associated with the name
     * @param name name of the xml file
     * @return graphDom with name name
     */
    private GraphDom getXmlOfThisTab(String name)
    {
        for (GraphDom aListGraphXml : listGraphXml)
        {
            if (aListGraphXml.getName().equals(name))
                return aListGraphXml;
        }
        return null;
    }

    /**
     *
     * @param name name of the button
     * @return boolean true if an other button is activate
     */
    private boolean isOtherButtonActivate(String name)
    {
        switch (name) {
            case VERTEX1:
                return (edge1Active || edge1ActiveOnce);
            case EDGE1:
                return (vertex1Active || vertex1ActiveOnce);
            default:
                return false;
        }
    }

    /**
     * create new Tab with a specifiated name
     * @param tabName name
     * @return Tab created
     */
    private Tab createNewTab(String tabName)
    {
        Tab tab = new Tab();
        if(tabName.equals(""))
            tabName = TABNAMEPREFIX + indiceTab++;
        tab.setText(tabName);
        tab.setId(tabName);

        // adding Panes in the tab
        AnchorPane paneBack = new AnchorPane();
        AnchorPane pane     = new AnchorPane();

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

        //listener when the value of slider change
        slider.valueProperty().addListener(new ChangeListener<Number>()
        {
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
                if(event.getDeltaY()>0)
                {
                    slider.setValue(slider.getValue()*1.05);
                }
                else
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
                else if(event.getCode() == KeyCode.ESCAPE)
                {
                    // Desactivate edge if active
                    desactivateEdge();

                    //desactivate vertex if active
                    desactivateVertex();
                }
            }
        });

        tabPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE)
                {
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

    /**
     * when we move an vertex, we have to move the edges connected to it
     *
     * @param groupStart vertex (circle) start
     * @param groupEnd   vertex (circle) end
     * @param gIndex     group index in the list
     * @param eIndex     edge index in the list
     * @param tab        current Tab
     * @param currP      current Anchorpane
     */
    private void moveVertexMoveEdgeListenerDraw(Group groupStart, Group groupEnd, int gIndex, int eIndex, Tab tab, AnchorPane currP)
    {
        //////////////////////////////////moving vertex move edges////////////////////
        groupEnd.translateXProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                DrawEdge drawEdge = tabDrawEdgesList.get(tab).get(gIndex).get(eIndex);
                double startX     = drawEdge.getStartX();
                double startY     = drawEdge.getStartY();
                double endX       = drawEdge.getEndX();
                double endY       = drawEdge.getEndY();
                Text text         = drawEdge.getText();
                boolean directed  = drawEdge.isDirected();
                int bending       = drawEdge.getBending();
                int bendFactor    = drawEdge.getBendFactor();

                currP.getChildren().remove(drawEdge.getRoot());
                tabDrawEdgesList.get(tab).get(gIndex).remove(eIndex);
                tabDrawEdgesList.get(tab).get(gIndex).add(eIndex, new DrawEdge(startX, startY,
                                                                        (double)newValue,endY,
                                                                        bending, bendFactor,
                                                                        directed, text));

                currP.getChildren().add(1,tabDrawEdgesList.get(tab).get(gIndex).get(eIndex).getRoot());
            }
        });

        groupEnd.translateYProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                DrawEdge drawEdge = tabDrawEdgesList.get(tab).get(gIndex).get(eIndex);
                double startX     = drawEdge.getStartX();
                double startY     = drawEdge.getStartY();
                double endX       = drawEdge.getEndX();
                double endY       = drawEdge.getEndY();
                Text text         = drawEdge.getText();
                boolean directed  = drawEdge.isDirected();
                int bending       = drawEdge.getBending();
                int bendFactor    = drawEdge.getBendFactor();

                currP.getChildren().remove(drawEdge.getRoot());
                tabDrawEdgesList.get(tab).get(gIndex).remove(eIndex);
                tabDrawEdgesList.get(tab).get(gIndex).add(eIndex, new DrawEdge(startX, startY, endX,
                                                                        (double)newValue, bending,
                                                                        bendFactor, directed, text));
                currP.getChildren().add(1,tabDrawEdgesList.get(tab).get(gIndex).get(eIndex).getRoot());
            }
        });
        groupStart.translateXProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                DrawEdge drawEdge = tabDrawEdgesList.get(tab).get(gIndex).get(eIndex);
                double startX     = drawEdge.getStartX();
                double startY     = drawEdge.getStartY();
                double endX       = drawEdge.getEndX();
                double endY       = drawEdge.getEndY();
                Text text         = drawEdge.getText();
                boolean directed  = drawEdge.isDirected();
                int bending       = drawEdge.getBending();
                int bendFactor    = drawEdge.getBendFactor();

                currP.getChildren().remove(drawEdge.getRoot());
                tabDrawEdgesList.get(tab).get(gIndex).remove(eIndex);
                tabDrawEdgesList.get(tab).get(gIndex).add(eIndex, new DrawEdge((double)newValue, startY, endX,
                                                                        endY, bending, bendFactor, directed, text));
                currP.getChildren().add(1,tabDrawEdgesList.get(tab).get(gIndex).get(eIndex).getRoot());
            }
        });

        groupStart.translateYProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                DrawEdge drawEdge = tabDrawEdgesList.get(tab).get(gIndex).get(eIndex);
                double startX     = drawEdge.getStartX();
                double startY     = drawEdge.getStartY();
                double endX       = drawEdge.getEndX();
                double endY       = drawEdge.getEndY();
                Text text         = drawEdge.getText();
                boolean directed  = drawEdge.isDirected();
                int bending       = drawEdge.getBending();
                int bendFactor    = drawEdge.getBendFactor();

                currP.getChildren().remove(drawEdge.getRoot());
                tabDrawEdgesList.get(tab).get(gIndex).remove(eIndex);
                tabDrawEdgesList.get(tab).get(gIndex).add(eIndex, new DrawEdge(startX, (double)newValue, endX,
                                                                        endY, bending, bendFactor, directed, text));
                currP.getChildren().add(1,tabDrawEdgesList.get(tab).get(gIndex).get(eIndex).getRoot());
            }
        });
    }

    /**
     *
     * @param pane anchorPane to search for the vertex
     * @param id id of the vertex to find
     * @return Node (vertex corresponding of the id, or null)
     */
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

    private void updateGroup(Tab tab, int index)
    {
        AnchorPane currPage = (AnchorPane) tab.getContent();
        AnchorPane currP    = (AnchorPane) currPage.getChildren().get(0);
        GraphDom graphXml   = getXmlOfThisTab(tab.getId());

        if(tabDrawEdgesList.get(tab).size() == index)
        {
            if (graphXml != null) {
                tabDrawEdgesList.get(tab).add(graphXml.getDrawEdges(index));
            }
        }
        else
        {
            for(int i=0; i < tabDrawEdgesList.get(tab).get(index).size(); i++)
            {
                currP.getChildren().remove(tabDrawEdgesList.get(cTab).get(index).get(i).getRoot());
            }
            tabDrawEdgesList.get(cTab).set(index, getXmlOfThisTab(tab.getId()).getDrawEdges(index));
        }

        for(int i = 0; i < tabDrawEdgesList.get(cTab).get(index).size(); i++)
        {
            Group cirleStart = null;
            if (graphXml != null)
            {
                cirleStart = (Group) getChildrenVertexById(currP, graphXml.getEdgeStartName(index, i));
            }
            Group cirleEnd   = null;
            if (graphXml != null) {
                cirleEnd = (Group) getChildrenVertexById(currP, graphXml.getEdgeEndName(index, i));
            }

            DrawEdge drawEdge = tabDrawEdgesList.get(cTab).get(index).get(i);

            moveVertexMoveEdgeListenerDraw(cirleStart, cirleEnd, index, i, tab, currP);
            currP.getChildren().add(1, drawEdge.getRoot());
        }
    }
}