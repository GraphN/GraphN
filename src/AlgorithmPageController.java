import Algorithms.*;
import Algorithms.Utils.EdgeVisit;
import Algorithms.Utils.Step;
import Algorithms.Utils.VertexVisit;
import graph.*;
import graph.Stockage.EdgeListStockage;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.*;
import java.util.List;


/**
 * Created by LBX on 03/04/2017.
 */
public class AlgorithmPageController {
    private MainApp mainApp;

    @FXML
    private AnchorPane centerAlgoPage;

    @FXML
    private ToggleButton bfs;

    @FXML
    private ToggleButton dfs;

    @FXML
    private ToggleButton prim;

    @FXML
    private ToggleButton dijkstra;

    @FXML
    private ToggleButton bellman;

    @FXML
    private ToggleButton kruskall;


    @FXML
    private ListView<String> description;

    @FXML
    private ListView<String> structure;

    public void setGraph(GraphDom g)
    {
        centerAlgoPage.getChildren().add(recreateAnchorWithXML(g));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    ArrayList<DrawEdge> edgeList;

    ArrayList<StackPane> vertexList;

    Graph graphTest;

    Algorithm algo;

    LinkedList<Step> path;

    final private Color UNCOLORED = Color.web("da5630");
    final private Color COLORED = Color.web("42f45f");

    int indexPath = 0;

    private Timer timer;

    @FXML
    private SplitPane splitPane;

    @FXML
    private void initialize()
    {
        initZoom();
        edgeList = new ArrayList<DrawEdge>();
        vertexList = new ArrayList<>();
        timer = new Timer();
        description.setCellFactory(lv -> new ListCell<String>() {

            private final Text text;
            {
                text = new Text();
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(text);

                // bind wrapping width to available size
                text.wrappingWidthProperty().bind(Bindings.createDoubleBinding(() -> {
                    Insets padding = getPadding();
                    return getWidth() - padding.getLeft() - padding.getRight() -1;
                }, widthProperty(), paddingProperty()));

            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    text.setText(null);
                } else {
                    text.setText(item);
                }
                text.setFill(Color.WHITE);
            }

        });
        structure.setCellFactory(lv -> new ListCell<String>() {

            private final Text text;
            {
                text = new Text();
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(text);

                // bind wrapping width to available size
                text.wrappingWidthProperty().bind(Bindings.createDoubleBinding(() -> {
                    Insets padding = getPadding();
                    return getWidth() - padding.getLeft() - padding.getRight() -1;
                }, widthProperty(), paddingProperty()));

            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    text.setText(null);
                } else {
                    text.setText(item);
                }
                text.setFill(Color.WHITE);
            }

        });
    }
    @FXML
    private Slider slider;


    @FXML
    private void handleNewFromResult(){
        mainApp.newTab();
    }

    @FXML
    private void handlePlayTimer(){
        timer.schedule(new TimerListener(), 1000, 2000);
    }
    @FXML
    private void handlePause(){
        timer.cancel();
        // Once a timer is cancelled we can't reuse it
        timer = new Timer();
    }
    @FXML

    private void handleStop(){
        for (DrawEdge edge : edgeList) {
            edge.setUncolored();
        }

        for (int vertex = 0; vertex < vertexList.size(); vertex++)
            setUncoloredVertex(vertex);

        indexPath = 0;
        handlePause();
        description.getItems().clear();
        structure.getItems().clear();
        path = null;
        algo = null;

        activateButtons();
    }

    @FXML
    private void handlePlay(){
        while(colorNextEdge());
    }
    @FXML
    private void handleStepByStep(){
        colorNextEdge();
    }

    @FXML
    private void handleKruskall(){
        setDividerPosition(0.2);
        this.algo = new Kruskall(graphTest);
        this.path = algo.getPath();

        desactivateButtons(kruskall);

    }
    @FXML
    private void handleDFS(){
        setDividerPosition(0.2);
        int startVertex = mainApp.showVertex(graphTest.getVertexsList().size()-1, "Start Vertex");
        //pane.getChildren().remove(edgeList.get(0).getRoot());
        this.algo = new DFS(graphTest, graphTest.getVertex(startVertex));
        this.path = algo.getPath();

        desactivateButtons(dfs);
    }
    @FXML
    private void handleBFS(){
        setDividerPosition(0.2);
        int startVertex = mainApp.showVertex(graphTest.getVertexsList().size()-1, "Start Vertex");
        this.algo = new BFS(graphTest, graphTest.getVertex(startVertex));
        this.path = algo.getPath();

        desactivateButtons(bfs);
    }

    @FXML
    private void handleBellman(){
        setDividerPosition(0.2);
        int startVertex = mainApp.showVertex(graphTest.getVertexsList().size()-1, "Start Vertex");
        int endVertex = mainApp.showVertex(graphTest.getVertexsList().size()-1, "End Vertex");

        this.algo = new Bellman_Ford(graphTest, graphTest.getVertex(startVertex), graphTest.getVertex(endVertex));
        this.path = algo.getPath();

        desactivateButtons(bellman);
    }
    @FXML
    private void handleDijkstra(){
        setDividerPosition(0.2);
        int startVertex = mainApp.showVertex(graphTest.getVertexsList().size()-1, "Start Vertex");
        int endVertex = mainApp.showVertex(graphTest.getVertexsList().size()-1, "End Vertex");
        //pane.getChildren().remove(edgeList.get(0).getRoot());
        this.algo = new Dijkstra(graphTest, graphTest.getVertex(startVertex), graphTest.getVertex(endVertex));
        this.path = algo.getPath();

        desactivateButtons(dijkstra);
    }
    @FXML
    private void handlePrim(){
        setDividerPosition(0.2);
        this.algo = new Prim(graphTest);
        this.path = algo.getPath();

        desactivateButtons(prim);
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

    GraphDom graphDom;
    AnchorPane pane = new AnchorPane();
    private AnchorPane recreateAnchorWithXML(GraphDom graph) {
        //AnchorPane pane = new AnchorPane();
        graphTest = new UDiGraph(graph.getNbVertex()+1, new EdgeListStockage());

        graphDom = graph;

        if(graph.getGraphType().equals("nonDiGraph") || graph.getGraphType().equals("weightedNonDiGraph"))
            graphTest = new UDiGraph(graph.getNbVertex()+1, new EdgeListStockage());
        else
            graphTest = new DiGraph(graph.getNbVertex()+1, new EdgeListStockage());

        //adding all vertex from xml
        for(int i = 0; i <= graph.getNbVertex(); i++)
        {
            // Le -10 c'est pour le décalage du stackpane, à vérifier sur d'autres écrans
            Point2D point = graph.getPosOfVertex(i);
            int x = (int) point.getX()-10;
            int y = (int) point.getY()-10;
            String name = graph.getName(i);

            //adding vertex created to pane

            int nbVertex = Integer.parseInt(name.replaceAll("[\\D]", ""));

            Circle circle_base = new Circle(10.0f, Color.web("da5630"));
            circle_base.setId(name);
            circle_base.setTranslateX(x);
            circle_base.setTranslateY(y);
            Text text = new Text(""+nbVertex);
            //text.setFill(Color.WHITE);
            text.setId(name);
            text.setBoundsType(TextBoundsType.VISUAL);
            text.setTranslateX(x);
            text.setTranslateY(y);


            StackPane sPane = new StackPane();
            sPane.getChildren().addAll(circle_base,text);

            vertexList.add(sPane);

            //List<Shape> node = createVertex(x, x, name);
            pane.getChildren().add(sPane);
        }
        for(int i=0; i<graph.getNbEdge(); i++) {
            DrawEdge drawEdge = graphDom.getDrawEdge(i);
            //adding edge created to pane (at index 0 to have vertexes on front of edges)
            pane.getChildren().add(0, drawEdge.getRoot());
//            System.out.println("" +drawEdge.getRoot().getChildren().get(0) + drawEdge.getRoot().getChildren().get(1));

            edgeList.add(drawEdge);
            System.out.println("\nedge weigth : " + graphDom.getEdgeWeigth(i));
            graphTest.addEdge(graphTest.getVertex(graph.getFrom(i)), graphTest.getVertex(graph.getTo(i)), (int)graphDom.getEdgeWeigth(i));
//            System.out.println(graph.getFrom(i));
//            System.out.println(graph.getTo(i));
        }

        for(Edge e : graphTest.getEdgesList()) {
            System.out.println("test : " + e + " poids : " + e.getWeigth());
        }
        return pane;
    }

    void setColoredVertex(int i){
        Circle circle = (Circle)vertexList.get(i).getChildren().get(0);
        circle.setFill(COLORED);
    }
    void setUncoloredVertex(int i){
        Circle circle = (Circle)vertexList.get(i).getChildren().get(0);
        circle.setFill(UNCOLORED);
    }
    void addTextVertex(int i, String weight){
        removeTextVertex(i);
        Circle circle = (Circle)vertexList.get(i).getChildren().get(0);
        Text text =  new Text(weight);
        text.setTranslateX(circle.getTranslateX());
        text.setTranslateY(circle.getTranslateY()-20);
        vertexList.get(i).getChildren().add(text);
    }
    void removeTextVertex(int i){
        if (vertexList.get(i).getChildren().size() > 2)
            vertexList.get(i).getChildren().remove(2);
    }

    private Circle createVertexShape(double x, double y, String id)
    {
        Circle circle_base = new Circle(10.0f, Color.web("da5630"));
        circle_base.setId(id);
        circle_base.setTranslateX(x);
        circle_base.setTranslateY(y);
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

        return text;
    }
    private List<Shape> createVertex (double x, double y, String id){
        ArrayList<Shape> node = new ArrayList<>();
        node.add(createVertexShape(x,y,id));
        node.add(createVertexNumber(x,y,id));

        return node;
    }

    private Boolean colorNextEdge() {
        boolean hasChange = false;
        if(path == null || indexPath >= path.size())
            return hasChange;

        Edge e = this.path.get(indexPath).getEdge();
        Vertex v = this.path.get(indexPath).getVertex();
        System.out.println(e);
        System.out.println(v);

        if (e != null) {
            DrawEdge test = graphDom.getEdge(e.getFrom().getId(), e.getTo().getId());

            // TODO : On ne devrait pas avoir a faire ça !!!! mais avec ça ça marche ...
            if (test == null)
                test = graphDom.getEdge(e.getTo().getId(), e.getFrom().getId());

            for (int i = 0; i < edgeList.size(); i++) {
                if (edgeList.get(i) != null)
                    if ((edgeList.get(i).getStartX() == test.getStartX()
                            && edgeList.get(i).getStartY() == test.getStartY()
                            && edgeList.get(i).getEndX() == test.getEndX()
                            && edgeList.get(i).getEndY() == test.getEndY())
                            || (!edgeList.get(i).isDirected()
                            && edgeList.get(i).getEndY() == test.getStartY()
                            && edgeList.get(i).getStartX() == test.getEndX()
                            && edgeList.get(i).getStartY() == test.getEndY())) {
                        edgeList.get(i).setColored();
                    }
            }

            hasChange = true;
        }

        if(v != null) {
            setColoredVertex(v.getId());
            addTextVertex(v.getId(),v.getDescription());
            hasChange = true;

        }

        // Update the descriptions
        if (hasChange){

            description.getItems().add(this.path.get(indexPath).getMessage());
            structure.getItems().add(this.path.get(indexPath).getStrutures());
            description.scrollTo(description.getItems().size() - 1);
            structure.scrollTo(structure.getItems().size() - 1);

            Node n1 = description.lookup(".scroll-bar");
            if (n1 instanceof ScrollBar) {
                final ScrollBar bar1 = (ScrollBar) n1;
                Node n2 = structure.lookup(".scroll-bar");
                if (n2 instanceof ScrollBar) {
                    final ScrollBar bar2 = (ScrollBar) n2;
                    bar1.valueProperty().bindBidirectional(bar2.valueProperty());
                }
            }
        }

        indexPath++;
        return hasChange;
    }

    private void setDividerPosition(double position) {
        splitPane.setDividerPosition(0,position);
    }

    /*class TimerListener extends TimerTask {
            @Override
            public void run() {
                System.out.print("hello");
                if(!colorNextEdge())
                    timer.cancel();
            }
        }*/
    class TimerListener extends TimerTask {
        @Override
        public void run() {
            System.out.print("hello");
            // FIXME Andrea J'ai du mettre un runLater pour que ça marche, ça fait une threadception mais bon ça marche
            // FIXME Parce qu'on peut pas appeller une fonction javafx dans un thread non application
            Platform.runLater(() -> {
                if(!colorNextEdge())
                    timer.cancel();
            });
        }
    }

    private void desactivateButtons(ToggleButton avoid){
        bfs.setDisable(true);
        prim.setDisable(true);
        dfs.setDisable(true);
        dijkstra.setDisable(true);
        bellman.setDisable(true);
        kruskall.setDisable(true);
    }
    private void activateButtons(){
        bfs.setDisable(false);
        bfs.setSelected(false);
        prim.setDisable(false);
        prim.setSelected(false);

        dfs.setDisable(false);
        dfs.setSelected(false);

        dijkstra.setDisable(false);
        dijkstra.setSelected(false);

        bellman.setDisable(false);
        bellman.setSelected(false);

        kruskall.setDisable(false);
        kruskall.setSelected(false);

    }
}
