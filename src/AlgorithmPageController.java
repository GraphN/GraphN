import Algorithms.*;
import Algorithms.Utils.Step;
import graph.*;
import graph.Stockage.EdgeListStockage;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by LBX on 03/04/2017.
 */
public class AlgorithmPageController {
    private MainApp mainApp;

    GraphDom graphNewFromResult;

    GraphDom graphDom;
    AnchorPane pane = new AnchorPane();

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

    @FXML
    private Slider slider;

    ArrayList<DrawEdge> edgeList;

    ArrayList<StackPane> vertexList;

    Graph graph;

    LinkedList<Step> path;

    final private Color UNCOLORED = Color.web("da5630");
    final private Color COLORED = Color.web("42f45f");

    int indexPath = 0;

    private Timer timer;


    @FXML
    private SplitPane splitPane;

    /**
     * Function called when the window is show
     * work like a default constructor for JavaFx class
     */
    @FXML
    private void initialize() {
        edgeList = new ArrayList<DrawEdge>();
        vertexList = new ArrayList<>();
        timer = new Timer();

        // init the way that the text is displayed in the ListView
        description.setCellFactory(lv -> new ListCell<String>() {

            private final Text text;

            {
                text = new Text();
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(text);

                // bind wrapping width to available size
                text.wrappingWidthProperty().bind(Bindings.createDoubleBinding(() -> {
                    Insets padding = getPadding();
                    return getWidth() - padding.getLeft() - padding.getRight() - 1;
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
                    return getWidth() - padding.getLeft() - padding.getRight() - 1;
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

    /**
     * Open the subgraph returned by the algorithm in the mainPage.
     */
    @FXML
    private void handleNewFromResult() {
        mainApp.newFromAlgoPage(graphNewFromResult);
    }

    /**
     * Run colorNextEdge until the end of the graph with a period of 2s
     */
    @FXML
    private void handlePlayTimer() {
        timer.schedule(new TimerListener(), 1000, 2000);
    }

    /**
     * Stop the timer
     */
    @FXML
    private void handlePause() {
        timer.cancel();
        // Once a timer is cancelled we can't reuse it
        timer = new Timer();
    }

    /**
     * Reset the page at the initial state
     */
    @FXML
    private void handleStop() {
        for (DrawEdge edge : edgeList) {
            edge.setUncolored();
        }

        for (int vertex = 0; vertex < vertexList.size(); vertex++) {
            setUncoloredVertex(vertex);
            removeTextVertex(vertex);
        }

        for (Vertex v : graph.getVertexsList())
            v.setDescription(" ");

        indexPath = 0;
        handlePause();
        description.getItems().clear();
        structure.getItems().clear();
        path = null;

        activateButtons();

        try {
            graphNewFromResult = new GraphDom(graphDom.getName());
        } catch (Exception e) {
            System.err.println("ParserException");
        }
    }

    /**
     * Color all edges
     */
    @FXML
    private void handlePlay() {
        while (colorNextEdge()) ;
    }

    /**
     * Color one edge
     */
    @FXML
    private void handleStepByStep() {
        colorNextEdge();
    }


    /**
     * Apply BFS Algorithm
     */
    @FXML
    private void handleBFS() {
        try {
            setDividerPosition(0.2);
            int startVertex = mainApp.showVertex(graph.getVertexsList().size() - 1, "Start Vertex");
            this.path = graph.accept(new BFS(), graph.getVertex(startVertex), null);

            setColoredVertex(startVertex);
            desactivateButtons(bfs);
        } catch (Exception e) {
            e.printStackTrace();
            alertMessage(e.getMessage());
            handleStop();
        }
    }

    /**
     * Apply DFS Algorithm
     */
    @FXML
    private void handleDFS() {
        try {
            setDividerPosition(0.2);
            int startVertex = mainApp.showVertex(graph.getVertexsList().size() - 1, "Start Vertex");
            //pane.getChildren().remove(edgeList.get(0).getRoot());
            this.path = graph.accept(new DFS(), graph.getVertex(startVertex), null);

            setColoredVertex(startVertex);
            desactivateButtons(dfs);
        } catch (Exception e) {
            e.printStackTrace();
            alertMessage(e.getMessage());
            handleStop();
        }
    }

    /**
     * Apply Dijkstra Algorithm
     */
    @FXML
    private void handleDijkstra() {
        try {
            setDividerPosition(0.2);
            int startVertex = mainApp.showVertex(graph.getVertexsList().size() - 1, "Start Vertex");
            int endVertex = mainApp.showVertex(graph.getVertexsList().size() - 1, "End Vertex");
            //pane.getChildren().remove(edgeList.get(0).getRoot());
            this.path = graph.accept(new Dijkstra(), graph.getVertex(startVertex), graph.getVertex(endVertex));
            setColoredVertex(startVertex);
            desactivateButtons(dijkstra);
        } catch (Exception e) {
            e.printStackTrace();
            alertMessage(e.getMessage());
            handleStop();
        }
    }

    /**
     * Apply Kruskal Algorithm
     */
    @FXML
    private void handleKruskall() {
        try {
            setDividerPosition(0.2);
            this.path = graph.accept(new Kruskall(), null, null);
            desactivateButtons(kruskall);
        } catch (Exception e) {
            e.printStackTrace();
            alertMessage(e.getMessage());
            handleStop();
        }
    }

    /**
     * Apply Prim Algorithm
     */
    @FXML
    private void handlePrim() {
        try {
            setDividerPosition(0.2);
            this.path = graph.accept(new Prim(), null, null);
            desactivateButtons(prim);
        } catch (Exception e) {
            e.printStackTrace();
            alertMessage(e.getMessage());
            handleStop();
        }
    }

    /**
     * Apply Bellman Algorithm
     */
    @FXML
    private void handleBellman() {
        try {
            setDividerPosition(0.2);
            int startVertex = mainApp.showVertex(graph.getVertexsList().size() - 1, "Start Vertex");
            int endVertex = mainApp.showVertex(graph.getVertexsList().size() - 1, "End Vertex");

            this.path = graph.accept(new Bellman_Ford(), graph.getVertex(startVertex), graph.getVertex(endVertex));

            setColoredVertex(startVertex);
            desactivateButtons(bellman);
        } catch (Exception e) {
            e.printStackTrace();
            alertMessage(e.getMessage());
            handleStop();
        }
    }

    /**
     * Add all the edges and vertexes from the graphDom create in the main page on a new pane.
     * Then the pane is added to the page.
     *
     * @param graphD
     * @return an AnchorPane
     */
    private AnchorPane recreateAnchorWithXML(GraphDom graphD) {
        //AnchorPane pane = new AnchorPane();

        graphDom = graphD;

        try {
            graphNewFromResult = new GraphDom(graphDom.getName());
        } catch (Exception e) {
            System.err.println("ParserException");
        }

        if (graphDom.getGraphType().equals("nonDiGraph") || graphDom.getGraphType().equals("weightedNonDiGraph")) {
            System.out.println("Algorithme page, construct graph with  " + (graphDom.getNbVertex()) + " vertexs");
            graph = new UDiGraph(graphDom.getNbVertex() + 1, new EdgeListStockage());
        } else {
            System.out.println("Algorithme page, construct Digraph with  " + (graphDom.getNbVertex()) + " vertexs");
            graph = new DiGraph(graphDom.getNbVertex() + 1, new EdgeListStockage());
        }

        //adding all vertex from xml
        for (int i = 0; i <= graphDom.getNbVertex(); i++) {
            // Le -10 c'est pour le décalage du stackpane, à vérifier sur d'autres écrans
            Point2D point = graphDom.getPosOfVertex(i);
            int x = (int) point.getX() - 10;
            int y = (int) point.getY() - 10;
            String name = graphDom.getVertexName(i);

            //adding vertex created to pane

            int nbVertex = Integer.parseInt(name.replaceAll("[\\D]", ""));

            Circle circle_base = new Circle(10.0f, Color.web("da5630"));
            circle_base.setId(name);
            circle_base.setTranslateX(x);
            circle_base.setTranslateY(y);
            Text text = new Text("" + nbVertex);
            //text.setFill(Color.WHITE);
            text.setId(name);
            text.setBoundsType(TextBoundsType.VISUAL);
            text.setTranslateX(x);
            text.setTranslateY(y);


            StackPane sPane = new StackPane();
            sPane.getChildren().addAll(circle_base, text);

            vertexList.add(sPane);

            //List<Shape> node = createVertex(x, x, name);
            pane.getChildren().add(sPane);
        }

        for (int i = 0; i < graphDom.getNbGroup(); i++) {
            ArrayList<DrawEdge> edges = graphDom.getDrawEdges(i);

            for (int j = 0; j < edges.size(); j++) {
                pane.getChildren().add(0, edges.get(j).getRoot());
                edgeList.add(edges.get(j));

                if (graphDom.getGraphType().equals("weightedDiGraph") || graphDom.getGraphType().equals("weightedNonDiGraph"))
                    graph.addEdge(graph.getVertex(graphDom.getFrom(i, j)), graph.getVertex(graphDom.getTo(i, j)), graphDom.getEdgeWeigth(i, j));
                else
                    graph.addEdge(graph.getVertex(graphDom.getFrom(i, j)), graph.getVertex(graphDom.getTo(i, j)));
            }
        }

        return pane;
    }

    /**
     * Set a vertex to the color define in the start of the
     *
     * @param i index of the vertex
     */
    void setColoredVertex(int i) {
        Circle circle = (Circle) vertexList.get(i).getChildren().get(0);
        circle.setFill(COLORED);
    }

    /**
     * Set a vertex to the color define in the start of the
     *
     * @param i index of the vertex
     */
    void setUncoloredVertex(int i) {
        Circle circle = (Circle) vertexList.get(i).getChildren().get(0);
        circle.setFill(UNCOLORED);
    }

    /**
     * Add a weight to an vertex
     *
     * @param i      index of the vertex
     * @param weight weight of the vertex
     */
    void addTextVertex(int i, String weight) {
        removeTextVertex(i);
        Circle circle = (Circle) vertexList.get(i).getChildren().get(0);
        Text text = new Text(weight);
        text.setTranslateX(circle.getTranslateX());
        text.setTranslateY(circle.getTranslateY() - 20);
        vertexList.get(i).getChildren().add(text);
    }

    /**
     * remove the weight on an edge
     *
     * @param i index of the edge
     */

    void removeTextVertex(int i) {
        if (vertexList.get(i).getChildren().size() > 2)
            vertexList.get(i).getChildren().remove(2);
    }

    /**
     * Color the edges and the vertexes using the step list gived by the alrogithm.
     * This function also show the description of the steps
     *
     * @return false if there is no more edge to color
     */
    private Boolean colorNextEdge() {
        boolean hasChange = false;
        if (path == null || indexPath >= path.size())
            return hasChange;

        Edge e = this.path.get(indexPath).getEdge();
        Vertex v = this.path.get(indexPath).getVertex();
        // TODO: effacer à la fin
        System.out.println(e);
        System.out.println(v);

        if (e != null) {
            DrawEdge test = graphDom.getEdge(e.getFrom().getId(), e.getTo().getId(), e.getWeigth());

            // TODO : On ne devrait pas avoir a faire ça !!!! mais avec ça ça marche ...
            if (test == null)
                test = graphDom.getEdge(e.getTo().getId(), e.getFrom().getId(), e.getWeigth());

            for (int i = 0; i < edgeList.size(); i++) {
                if (edgeList.get(i) != null) {
                    if (((edgeList.get(i).getStartX() == test.getStartX()
                            && edgeList.get(i).getStartY() == test.getStartY()
                            && edgeList.get(i).getEndX() == test.getEndX()
                            && edgeList.get(i).getEndY() == test.getEndY())
                            || (!edgeList.get(i).isDirected()
                            && edgeList.get(i).getEndY() == test.getStartY()
                            && edgeList.get(i).getStartX() == test.getEndX()
                            && edgeList.get(i).getStartY() == test.getEndY()))
                            && (test.getText().getText() == null || test.getText().getText() != null && (edgeList.get(i).getText().getText().equals(test.getText().getText())))) {
                        edgeList.get(i).setColored();

                        // FIXME: Essais pour newFromResult
                       /* boolean startExists = false, endExists = false;
                        System.out.println("TESTEU" +graphNewFromResult.getNbVertex());
                        for(int j = 0; i<=graphNewFromResult.getNbVertex(); i++) {
                            if (graphNewFromResult.getPosOfVertex(j) == new Point2D((int) edgeList.get(i).getStartX(), (int) edgeList.get(i).getStartY()))
                                startExists = true;
                            if (graphNewFromResult.getPosOfVertex(j) == new Point2D((int) edgeList.get(i).getEndX(), (int) edgeList.get(i).getEndX()))
                                endExists = true;
                        }
                        if(!startExists)
                            graphNewFromResult.addVertex((int) edgeList.get(i).getStartX(), (int) edgeList.get(i).getStartY());
                        if(!endExists)
                            graphNewFromResult.addVertex((int) edgeList.get(i).getEndX(), (int) edgeList.get(i).getEndX());
                        graphNewFromResult.addEdge(graphNewFromResult.getVertexName(0), graphNewFromResult.getVertexName(1));*/
                    }
                }
            }
        }

        if (v != null) {
            setColoredVertex(v.getId());
            addTextVertex(v.getId(), v.getDescription());
        }

        // Update the descriptions

        description.getItems().add(this.path.get(indexPath).getMessage());
        structure.getItems().add(this.path.get(indexPath).getStructures());
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

        indexPath++;
        return true;
    }

    private void setDividerPosition(double position) {
        splitPane.setDividerPosition(0, position);
    }

    /*class TimerListener extends TimerTask {
            @Override
            public void run() {
                System.out.print("hello");
                if(!colorNextEdge())
                    timer.cancel();
            }
        }*/

    /**
     * Color the edges with a period without impacting the program
     */
    class TimerListener extends TimerTask {
        @Override
        public void run() {
            Platform.runLater(() -> {
                if (!colorNextEdge())
                    timer.cancel();
            });
        }
    }

    private void desactivateButtons(ToggleButton avoid) {
        bfs.setDisable(true);
        prim.setDisable(true);
        dfs.setDisable(true);
        dijkstra.setDisable(true);
        bellman.setDisable(true);
        kruskall.setDisable(true);
    }

    private void activateButtons() {
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

    /**
     * Set the graph builded in the main page
     *
     * @param g graphDom
     */
    public void setGraph(GraphDom g) {
        centerAlgoPage.getChildren().add(recreateAnchorWithXML(g));
    }

    /**
     * Referency to the mainApp for lauching the start and end vertex question
     * and the newFromResult
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Permet d'ouvrir un popup d'erreur
     *
     * @param message
     */
    void alertMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("assets/css/alert.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
    }

    @FXML
    void handleScreenShot(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Screen");

        //Set extension filter
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(pngFilter);
        fileChooser.getExtensionFilters().add(jpgFilter);

        //set initial directory
        File directory = new File("./src/savedGraphsXML");
        fileChooser.setInitialDirectory(directory);

        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            switch (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.') + 1)) {
                case "jpg":
                case "png":
                    WritableImage imagePNG = pane.snapshot(new SnapshotParameters(), null);
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(imagePNG, null), "png", file);
                    } catch (IOException e) {
                        // TODO: handle exception here
                    }
                    break;
                default:
                    System.err.println("Wrong input type file !");
            }
        }
    }


}
