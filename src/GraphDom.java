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
    private ArrayList<Element> groups;
    private ArrayList<ArrayList<Element>> edgesGroups;
    private String graphType;

    public GraphDom(String name) throws ParserConfigurationException
    {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();
        groups = new ArrayList<>();
        edgesGroups = new ArrayList<>();
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
    public int addEdge(String vertexStart, String vertexEnd)
    {
        nbEdge++;
        Element group = getGroup(vertexStart, vertexEnd);

        Element edge1 = document.createElement("edge");
        edge1.setAttribute("name", "edge_"+nbEdge);
        edge1.setAttribute("start", vertexStart);
        edge1.setAttribute("end", vertexEnd);
        Element edge2 = document.createElement("edge");
        edge2.setAttribute("name", "edge_"+nbEdge);
        edge2.setAttribute("start", vertexEnd);
        edge2.setAttribute("end", vertexStart);
        racine.setAttribute("graphType", "nonDiGraph");
        group.appendChild(edge1);
        group.appendChild(edge2);
        edgesGroups.get(getId(group)).add(edge1);
        edgesGroups.get(getId(group)).add(edge2);
        edges.add(edge1);
        //edges.add(edge2);

        return getId(group);
    }
    public int addWeightedEdge(String vertexStart, String vertexEnd, String weight)
    {
        nbEdge++;
        Element group = getGroup(vertexStart, vertexEnd);

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
        group.appendChild(edge1);
        group.appendChild(edge2);
        edgesGroups.get(getId(group)).add(edge1);
        edgesGroups.get(getId(group)).add(edge2);
        edges.add(edge1);
        //edges.add(edge2);

        return getId(group);
    }

    public int addDiEdge(String vertexStart, String vertexEnd)
    {
        nbEdge++;
        Element group = getGroup(vertexStart, vertexEnd);

        Element edge = document.createElement("edge");
        edge.setAttribute("name", "edge_"+nbEdge);
        edge.setAttribute("start", vertexStart);
        edge.setAttribute("end", vertexEnd);
        racine.setAttribute("graphType", "diGraph");
        group.appendChild(edge);
        edgesGroups.get(getId(group)).add(edge);
        edges.add(edge);

        return getId(group);
    }

    public int addEdgeWeighted(String vertexStart, String vertexEnd, String weight)
    {
        nbEdge++;
        Element group = getGroup(vertexStart, vertexEnd);

        Element edge = document.createElement("edge");
        edge.setAttribute("name", "edge_"+nbEdge);
        edge.setAttribute("start", vertexStart);
        edge.setAttribute("end", vertexEnd);
        edge.setAttribute("weight", weight);
        group.appendChild(edge);
        edgesGroups.get(getId(group)).add(edge);
        racine.setAttribute("graphType", "weightedDiGraph");
        edges.add(edge);

        return getId(group);
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
            return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0);
        else if (graphType.equals("weightedNonDiGraph")) {
            return new DrawEdge((double) startX, (double) startY, (double) endX, (double) endY, 0, false, new Text(edge.getAttribute("weight")));
        }
        else if (graphType.equals("diGraph"))
            return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0, true);
        else if (graphType.equals("weightedDiGraph"))
            return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0, true, new Text(edge.getAttribute("weight")));
        return null;
    }

    public ArrayList<DrawEdge> getDrawEdges(int index){
        ArrayList<Element> edges = edgesGroups.get(index);
        ArrayList<DrawEdge> res = new ArrayList<>();

        for(int i = 0; i < edges.size(); i++) {
            String vertex1 = edges.get(i).getAttribute("start");
            String vertex2 = edges.get(i).getAttribute("end");

            int bending = 0;
            if(graphType.equals("nonDiGraph")) {
                if (edges.size() > 2) {
                    bending = ((i/2) % 2) + 1;
                }
            }
            else {
                if (edges.size() > 1) {
                    if (groups.get(index).getAttribute("start").equals(vertex1))
                        bending = (i % 2) + 1;
                    else
                        bending = 2 - (i % 2);
                }
            }

            Point2D ver1 =  getPosOfVertex(vertex1);
            Point2D ver2 =  getPosOfVertex(vertex2);

            int startX = (int)ver1.getX();
            int startY = (int)ver1.getY();
            int endX = (int)ver2.getX();
            int endY = (int)ver2.getY();
            System.out.println(graphType);
            if(graphType.equals("nonDiGraph"))
                res.add(new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, bending));
            else if (graphType.equals("diGraph"))
                res.add(new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, bending, true));
            else if (graphType.equals("weightedDiGraph"))
                res.add(new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, bending, new Text(edges.get(i).getAttribute("weight"))));
        }

        return res;
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
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0);
                    else if (graphType.equals("weightedNonDiGraph"))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0, false, new Text(edge.getAttribute("weight")));
                    else if (graphType.equals("diGraph"))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0, true);
                    else if (graphType.equals("weightedDiGraph"))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0, true, new Text(edge.getAttribute("weight")));
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

    public String getEdgeStartName(int gIndex, int eIndex)
    {
        Element edge = (Element) edgesGroups.get(gIndex).get(eIndex);
        return edge.getAttribute("start");
    }

    public String getEdgeEndName(int gIndex, int eIndex)
    {
        Element edge = (Element) edgesGroups.get(gIndex).get(eIndex);
        return edge.getAttribute("end");
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public String getGraphType() {
        return graphType;
    }

    private Element getGroup(int from, int to) {
        Element res = null;

        if (from > to) {
            int tmp = to;
            to = from;
            from = tmp;
        }

        for(Element group:groups) {
            if (group.getAttribute("start").isEmpty() || group.getAttribute("end").isEmpty())
                continue;
            if (Integer.parseInt(group.getAttribute("start").replaceAll("[\\D]", "")) == from &&
                    Integer.parseInt(group.getAttribute("end").replaceAll("[\\D]", "")) == to)
                res = group;
        }

        if(res == null) {
            res = document.createElement("edges_group");
            res.setAttribute("name", "group_"+groups.size());
            res.setAttribute("start", "ver_"+from);
            res.setAttribute("end", "ver_"+to);
            racine.appendChild(res);
            groups.add(res);
            edgesGroups.add(new ArrayList<Element>());
        }

        return res;
    }

    public int getNbGroup() {
        return groups.size();
    }

    public static int getId(Element e) {
        return Integer.parseInt(e.getAttribute("name").replaceAll("[\\D]", ""));
    }

    private Element getGroup(String from, String to) {
        return getGroup(Integer.parseInt(from.replaceAll("[\\D]", "")), Integer.parseInt(to.replaceAll("[\\D]", "")));
    }
}
