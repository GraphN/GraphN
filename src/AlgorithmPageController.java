import Algorithms.*;
import graph.Edge;
import graph.Stockage.EdgeListStockage;
import graph.UDiGraph;
import graph.Vertex;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by LBX on 03/04/2017.
 */
public class AlgorithmPageController
{
    private MainApp mainApp;

    @FXML
    private AnchorPane centerAlgoPage;

    public void setGraph(GraphDom g)
    {
        centerAlgoPage.getChildren().add(recreateAnchorWithXML(g));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    ArrayList<DrawEdge> edgeList;

    UDiGraph graphTest;

    Algorithm algo;

    LinkedList<Edge> path;

    int indexPath = 0;

    @FXML
    private void initialize()
    {
        initZoom();
        edgeList = new ArrayList<DrawEdge>();
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

        changeEdgeColor(Color.BLUE, 1);
    }
    @FXML
    private void handleStop(){
    }
    @FXML
    private void handleFastForward(){

        if(indexPath < path.size()) {
            Edge e = this.path.get(indexPath);
            Line test = graphDom.getEdge(e.getFrom().getId(), e.getTo().getId());

            for(int i = 0; i < edgeList.size() ; i++) {
                if (edgeList.get(i).getStartX() == test.getStartX()
                        && edgeList.get(i).getStartY() == test.getStartY()
                        && edgeList.get(i).getEndX() == test.getEndX()
                        && edgeList.get(i).getEndY() == test.getEndY())
                    changeEdgeColor(Color.BLUE, i);
            }
        }
        indexPath++;

    }
    @FXML
    private void handlePausePlay(){
    }

    @FXML
    private void handleKruskall(){
        this.algo = new Kruskall(graphTest);
        this.path = ((Kruskall) algo).visit(new EdgeVisit() {
            @Override
            public void applyFunction(Edge e) {

            }
        });
        for(Edge e : path)
            System.out.println("Edge : from " + e.getFrom() + "; to " + e.getTo());
    }
    @FXML
    private void handleDFS(){
        this.algo = new DFS(graphTest);
        this.path = ((DFS) algo).visit(graphTest.getVertex(0), new VertexVisit() {
            @Override
            public void applyFunction(Vertex v) {

            }
        });

        for(Edge e : path)
            System.out.println("Edge : from " + e.getFrom() + "; to " + e.getTo());
    }
    @FXML
    private void handleBFS(){
        this.algo = new BFS(graphTest);
        this.path = ((BFS) algo).visit(graphTest.getVertex(0), new VertexVisit() {
            @Override
            public void applyFunction(Vertex v) {

            }
        });
        for(Edge e : path)
            System.out.println("Edge : from " + e.getFrom() + "; to " + e.getTo());
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

            //List<Shape> node = createVertex(x, x, name);
            pane.getChildren().add(sPane);
        }
        for(int i=0; i<graph.getNbEdge(); i++) {
            DrawEdge drawEdge = graphDom.getDrawEdge(i);
            //adding edge created to pane (at index 0 to have vertexes on front of edges)
            pane.getChildren().add(0, drawEdge.getRoot());
            edgeList.add(drawEdge);

            graphTest.addEdge(graphTest.getVertex(graph.getFrom(i)), graphTest.getVertex(graph.getTo(i)));
//            System.out.println(graph.getFrom(i));
//            System.out.println(graph.getTo(i));
        }
       return pane;
    }

    private void changeEdgeColor(Color color, int edgeNb){
        DrawEdge edge = edgeList.get(edgeNb);
        pane.getChildren().remove(edge.getRoot());
        //TODO: methode dans drawedge edge.setStroke(color);
        pane.getChildren().add(0, edge.getRoot());
        //System.out.println(edge.getId());
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

}
