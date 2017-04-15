import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
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
    private int nbVertex = 0;
    private int nbEdge;
    private ArrayList<Element> vertexes;
    private ArrayList<Element> edges;




    public GraphDom(String name) throws ParserConfigurationException
    {
        vertexes = new ArrayList<>();
        edges = new ArrayList<>();

        graphName = name;
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        document= builder.newDocument();
        racine = document.createElement("Graphe");
        racine.setAttribute("name", name);
        document.appendChild(racine);
    }

    public void saveGraphXML(String name) throws TransformerException
    {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        final DOMSource source = new DOMSource(document);
        final StreamResult sortie = new StreamResult(new File("./src/savedGraphsXML/"+name+".xml"));
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

    public void addEdge(String vertexStart, String vertexEnd)
    {
        nbEdge++;
        Element edge = document.createElement("edge");
        edge.setAttribute("name", "edge_"+nbEdge);
        edge.setAttribute("start", vertexStart);
        edge.setAttribute("end", vertexEnd);
        racine.appendChild(edge);
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
        while(i < nbVertex)
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

        Point2D ver1 =  getPosOfVertex(vertex1);
        Point2D ver2 =  getPosOfVertex(vertex2);

        int startX = (int)ver1.getX();
        int startY = (int)ver1.getY();
        int endX = (int)ver2.getX();
        int endY = (int)ver2.getY();
        return new Line(startX, startY, endX, endY);
    }

}
