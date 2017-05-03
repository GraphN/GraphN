import Algorithms.Algorithm;
import Algorithms.BFS;
import Algorithms.VertexVisit;
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

    ArrayList<Line> edgeList;

    UDiGraph graphTest;

    Algorithm algo;

    LinkedList<Edge> path;

    int indexPath = 0;

    @FXML
    private void initialize()
    {
        initZoom();
        edgeList = new ArrayList<Line>();
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

        Edge e = this.path.get(indexPath);
        Line test = graphDom.getEdge(e.getFrom().getId(), e.getTo().getId());
        for(Line line: edgeList){
            if(line.equals(test)){
                changeEdgeColor(Color.BLUE, 1);
                break;
            }
        }
        indexPath++;
    }
    @FXML
    private void handlePausePlay(){
    }
    @FXML
    private void handleDFS(){
    }
    @FXML
    private void handleBFS(){
        this.algo = new BFS(graphTest);
        this.path = ((BFS) algo).visit(graphTest.getVertex(0), new VertexVisit() {
            @Override
            public void applyFunction(Vertex v) {

            }
        });
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
        graphTest = new UDiGraph(graph.getNbVertex(), new EdgeListStockage());

        graphDom = graph;

        //adding all vertex from xml
        for(int i = 0; i < graph.getNbVertex(); i++)
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
            //adding all edges from xml
            Line edge = graph.getEdge(i);
            edge.setStrokeWidth(4);
            edge.setSmooth(true);
            edge.setStroke(Color.web("da5630"));

            //adding edge created to pane (at index 0 to have vertexes on front of edges)
            pane.getChildren().add(0, edge);
            edgeList.add(edge);

            graphTest.addEdge(graphTest.getVertex(graph.getFrom(i)-1), graphTest.getVertex(graph.getTo(i)-1));
            System.out.println(graph.getFrom(i));
            System.out.println(graph.getTo(i));
        }
       return pane;
    }

    private void changeEdgeColor(Color color, int edgeNb){
        Line edge = edgeList.get(edgeNb);
        pane.getChildren().remove(edge);
        edge.setStroke(color);
        pane.getChildren().add(0, edge);
        System.out.println(edge.getId());
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
