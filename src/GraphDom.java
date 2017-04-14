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




    public GraphDom(String name) throws ParserConfigurationException {
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
    }

    public void addEdge(String vertexStart, String vertexEnd)
    {
        nbEdge++;
        Element edge = document.createElement("edge");
        edge.setAttribute("name", "edge_"+nbEdge);
        edge.setAttribute("start", vertexStart);
        edge.setAttribute("end", vertexEnd);
        racine.appendChild(edge);
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
    public int getPosXOfVertex(int index)
    {

       Element vertex = (Element) racine.getChildNodes().item(index);
       return Integer.valueOf(vertex.getAttribute("posX"));
    }
    public int getPosYOfVertex(int index)
    {
        Element vertex = (Element) racine.getChildNodes().item(index);
        return Integer.valueOf(vertex.getAttribute("posY"));
    }
    public String getVertexName(int index)
    {
        Element vertex = (Element) racine.getChildNodes().item(index);
        return vertex.getAttribute("name");
    }
}
