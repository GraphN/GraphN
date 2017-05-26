import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by kevin moreira on 13.04.2017.
 */

public class GraphDom {

    final DocumentBuilderFactory factory;
    final DocumentBuilder builder;
    final Document document;
    final Element racine;
    private String graphName;
    private int nbVertex = -1;
    private int nbEdge = 0;
    private ArrayList<Element> vertexes;
    private ArrayList<Element> edges;
    private String graphType;

    public GraphDom(String name) throws ParserConfigurationException
    {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();
        graphType = new String();

        graphName = name;
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        document= builder.newDocument();
        racine = document.createElement("UDiGraph");
        racine.setAttribute("name", name);
        document.appendChild(racine);
    }

    public void saveGraphXML(File file) throws TransformerException
    {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        final DOMSource source = new DOMSource(document);
        //final StreamResult sortie = new StreamResult(new File("./src/savedGraphsXML/"+name+".xml"));
        final StreamResult sortie = new StreamResult(file);
        //use to indent xml file
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, sortie);
    }

    public void addVertex(int posX, int posY)
    {
        nbVertex++;
        Element vertex = document.createElement("vertex");
        vertex.setAttribute("name", "ver_"+nbVertex);
        vertex.setAttribute("posX", String.valueOf(posX));
        vertex.setAttribute("posY", String.valueOf(posY));
        racine.appendChild(vertex);
        vertexes.add(vertex);
    }

    public  void setNameOfVertex(String name, int index)
    {
        vertexes.get(index).setAttribute("name", name);
    }

    public void addEdge(String vertexStart, String vertexEnd)
    {
        nbEdge++;
        Element edge1 = document.createElement("edge");
        edge1.setAttribute("name", "edge_"+nbEdge);
        edge1.setAttribute("start", vertexStart);
        edge1.setAttribute("end", vertexEnd);
        racine.setAttribute("graphType", "nonDiGraph");
        racine.appendChild(edge1);
        Element edge2 = document.createElement("edge");
        edge2.setAttribute("name", "edge_"+nbEdge);
        edge2.setAttribute("start", vertexEnd);
        edge2.setAttribute("end", vertexStart);
        racine.setAttribute("graphType", "nonDiGraph");
        racine.appendChild(edge1);
        racine.appendChild(edge2);
        edges.add(edge1);
        //edges.add(edge2);
    }
    public void addWeightedEdge(String vertexStart, String vertexEnd, String weight)
    {
        nbEdge++;
        Element edge1 = document.createElement("edge");
        edge1.setAttribute("name", "edge_"+nbEdge);
        edge1.setAttribute("start", vertexStart);
        edge1.setAttribute("end", vertexEnd);
        edge1.setAttribute("weight", weight);
        racine.setAttribute("graphType", "weightedNonDiGraph");
        racine.appendChild(edge1);
        Element edge2 = document.createElement("edge");
        edge2.setAttribute("name", "edge_"+nbEdge);
        edge2.setAttribute("start", vertexEnd);
        edge2.setAttribute("end", vertexStart);
        edge2.setAttribute("weight", weight);
        racine.setAttribute("graphType", "weightedNonDiGraph");
        racine.appendChild(edge1);
        racine.appendChild(edge2);
        edges.add(edge1);
        //edges.add(edge2);
    }

    public void addDiEdge(String vertexStart, String vertexEnd)
    {
        nbEdge++;
        Element edge = document.createElement("edge");
        edge.setAttribute("name", "edge_"+nbEdge);
        edge.setAttribute("start", vertexStart);
        edge.setAttribute("end", vertexEnd);
        racine.setAttribute("graphType", "diGraph");
        racine.appendChild(edge);
        edges.add(edge);
    }

    public void addWeightedDiEdge(String vertexStart, String vertexEnd, String weight)
    {
        nbEdge++;
        Element edge = document.createElement("edge");
        edge.setAttribute("name", "edge_"+nbEdge);
        edge.setAttribute("start", vertexStart);
        edge.setAttribute("end", vertexEnd);
        edge.setAttribute("weight", weight);
        racine.appendChild(edge);
        racine.setAttribute("graphType", "weightedDiGraph");
        edges.add(edge);
    }
    public String getName()
    {
        return graphName;
    }
    public int getNbVertex()
    {
        return nbVertex;
    }
    public int getNbEdge()
    {
        return nbEdge;
    }

    public void setPosOfVertex(String vertex, int posX, int posY)
    {
        NodeList nodes = racine.getChildNodes();

        for(int i = 0; i < nodes.getLength(); i++)
        {
            Element currentNode = (Element) nodes.item(i);

            if(currentNode.getNodeName().equals("vertex"))
            {
                if(currentNode.getAttribute("name").equals(vertex))
                {
                    currentNode.setAttribute("posX",String.valueOf(posX) );
                    currentNode.setAttribute("posY",String.valueOf(posY) );
                }
            }
        }
    }
    /*public int getPosXOfVertex(int index)
    {

       Element vertex = (Element) racine.getChildNodes().item(index);
       return Integer.valueOf(vertex.getAttribute("posX"));
    }
    public int getPosYOfVertex(int index)
    {
        Element vertex = (Element) racine.getChildNodes().item(index);
        return Integer.valueOf(vertex.getAttribute("posY"));
    }*/

    /**
     * get the position of vertex by his number
     * @param index
     * @return
     */
    public Point2D getPosOfVertex(int index)
    {

        Element node = vertexes.get(index);

        return new Point2D(Integer.valueOf(node.getAttribute("posX")),
                           Integer.valueOf(node.getAttribute("posY")));
    }

    /**
     * get the position of vertex by his name
     * @param name
     * @return
     */
    public Point2D getPosOfVertex(String name)
    {
        int i = 0;
        while(i <= nbVertex)
        {
            Element node = vertexes.get(i);
            if(node.getAttribute("name").equals(name))
            {
                return new Point2D(Integer.valueOf(node.getAttribute("posX")),
                                   Integer.valueOf(node.getAttribute("posY")));
            }
            i++;
        }

        return null;
    }
    public String getName(int index)
    {
        Element node = (Element) racine.getChildNodes().item(index);
        return node.getAttribute("name");
    }
    public String getNodeType(int index)
    {
        NodeList nodes = racine.getChildNodes();
        Element currentNode = (Element) nodes.item(index);
        return currentNode.getNodeName();
    }
    public Line getEdge(int index)
    {
        Element edge = (Element) edges.get(index);
        String vertex1 = edge.getAttribute("start");
        String vertex2 = edge.getAttribute("end");

//        System.out.println("vertex 1: " + vertex1);
//        System.out.println("vertex 2: " + vertex2);

        Point2D ver1 =  getPosOfVertex(vertex1);
        Point2D ver2 =  getPosOfVertex(vertex2);

//        System.out.println(ver1);
//        System.out.println(ver2);

        int startX = (int)ver1.getX();
        int startY = (int)ver1.getY();
        int endX = (int)ver2.getX();
        int endY = (int)ver2.getY();
        return new Line(startX, startY, endX, endY);
    }
    public DrawEdge getDrawEdge(int index){
        Element edge = (Element) edges.get(index);
        String vertex1 = edge.getAttribute("start");
        String vertex2 = edge.getAttribute("end");

        Point2D ver1 =  getPosOfVertex(vertex1);
        Point2D ver2 =  getPosOfVertex(vertex2);

        int startX = (int)ver1.getX();
        int startY = (int)ver1.getY();
        int endX = (int)ver2.getX();
        int endY = (int)ver2.getY();
        if(graphType.equals("nonDiGraph"))
            return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY);
        else if (graphType.equals("weightedNonDiGraph")) {
            return new DrawEdge((double) startX, (double) startY, (double) endX, (double) endY, false, new Text(edge.getAttribute("weight")));
        }
        else if (graphType.equals("diGraph"))
            return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, true);
        else if (graphType.equals("weightedDiGraph"))
            return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, true, new Text(edge.getAttribute("weight")));
        return null;
    }
    public DrawEdge getEdge(int from, int to)
    {
        //Element edge = (Element) edges.get(index);
        for(Element edge:edges){
            System.out.println("From: " + edge.getAttribute("start") + "end" + edge.getAttribute("end"));
            if(!edge.getAttribute("start").isEmpty() && !edge.getAttribute("end").isEmpty()) {
                if (Integer.parseInt(edge.getAttribute("start").replaceAll("[\\D]", "")) == from &&
                        Integer.parseInt(edge.getAttribute("end").replaceAll("[\\D]", "")) == to) {
                    String vertex1 = edge.getAttribute("start");
                    String vertex2 = edge.getAttribute("end");

                    Point2D ver1 = getPosOfVertex(vertex1);
                    Point2D ver2 = getPosOfVertex(vertex2);

                    int startX = (int) ver1.getX();
                    int startY = (int) ver1.getY();
                    int endX = (int) ver2.getX();
                    int endY = (int) ver2.getY();
                    if(graphType.equals("nonDiGraph"))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY);
                    else if (graphType.equals("weightedNonDiGraph"))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, false, new Text(edge.getAttribute("weight")));
                    else if (graphType.equals("diGraph"))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, true);
                    else if (graphType.equals("weightedDiGraph"))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, true, new Text(edge.getAttribute("weight")));
                    return null;                }
            }else {
                System.out.println("NUll");
            }
        }
        return null;
    }
    public double getEdgeWeigth(int i){
        if(graphType.equals("nonDiGraph")||graphType.equals("diGraph"))
            return 0;
        else
            return Double.parseDouble(edges.get(i).getAttribute("weight").replaceAll("[^1234567890.\\-]", ""));
    }
    public int getFrom(int index){
        return Integer.parseInt(edges.get(index).getAttribute("start").replaceAll("[\\D]", ""));
    }
    public int getTo(int index){
        return Integer.parseInt(edges.get(index).getAttribute("end").replaceAll("[\\D]", ""));
    }
    public String getEdgeStartName(int index)
    {
        Element edge = (Element) edges.get(index);
        return edge.getAttribute("start");
    }

    public String getEdgeEndName(int index)
    {
        Element edge = (Element) edges.get(index);
        return edge.getAttribute("end");
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public String getGraphType() {
        return graphType;
    }
}
