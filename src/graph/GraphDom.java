package graph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

    public void addVertex(double posX, double posY)
    {
        nbVertex++;
        Element vertex = document.createElement("vertex");
        vertex.setAttribute("name", "ver_"+nbVertex);
        vertex.setAttribute("posX", String.valueOf(posX));
        vertex.setAttribute("posY", String.valueOf(posY));
        racine.appendChild(vertex);
    }
    public String getName()
    {
        return graphName;
    }
    public int getNbVertex()
    {
        return nbVertex;
    }
}
