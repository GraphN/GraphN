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

    final Element racine;
    final Document document;
    final DocumentBuilder builder;
    final DocumentBuilderFactory factory;

    private int nbVertex = -1;
    private int nbEdge   = 0;

    private String graphName;
    private String graphType;
    private ArrayList<Element> edges;
    private ArrayList<Element> groups;
    private ArrayList<Element> vertexes;
    private ArrayList<ArrayList<Element>> edgesGroups;

    private final String POSX                = "posX";
    private final String POSY                = "posY";
    private final String EDGE                = "edge";
    private final String WEIGHT              = "weight";
    private final String VERTEX              = "vertex";
    private final String VERNAME             = "ver_";
    private final String DIGRAPH             = "diGraph";
    private final String GRAPHTYPE           = "graphType";
    private final String EDGEGROUP           = "edges_group";
    private final String GRAPHENAME          = "UDiGraph";
    private final String NONDIGRAPH          =  "nonDiGraph";
    private final String ATTRIBUTEND         = "end";
    private final String ATTRIBUTNAME        = "name";
    private final String ATTRIBUTEDGE        = "edge_";
    private final String ATTRIBUTSTART       = "start";
    private final String ATTRIBUTGROUP       = "group_";
    private final String WEIGHTEDDIGRAPH     = "weightedDiGraph";
    private final String WEIGHTEDNONDIGRAPH  = "weightedNonDiGraph";


    public GraphDom(String name) throws ParserConfigurationException
    {
        edges       = new ArrayList<>();
        groups      = new ArrayList<>();
        vertexes    = new ArrayList<>();
        graphType   = new String();
        edgesGroups = new ArrayList<>();

        graphName = name;
        factory   = DocumentBuilderFactory.newInstance();
        builder   = factory.newDocumentBuilder();
        document  = builder.newDocument();
        racine    = document.createElement(GRAPHENAME);
        racine.setAttribute(ATTRIBUTNAME, name);
        document.appendChild(racine);
    }

    public void saveGraphXML(File file) throws TransformerException
    {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer               = transformerFactory.newTransformer();
        final DOMSource source                      = new DOMSource(document);
        final StreamResult sortie                   = new StreamResult(file);

        //use to indent xml file
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, sortie);
    }

    public void addVertex(int posX, int posY)
    {
        nbVertex++;
        Element vertex = document.createElement(VERTEX);
        vertex.setAttribute(ATTRIBUTNAME, VERNAME+nbVertex);
        vertex.setAttribute(POSX, String.valueOf(posX));
        vertex.setAttribute(POSY, String.valueOf(posY));
        racine.appendChild(vertex);
        vertexes.add(vertex);
    }

    public  void setNameOfVertex(String name, int index)
    {
        vertexes.get(index).setAttribute(ATTRIBUTNAME, name);
    }
    public int addEdge(String vertexStart, String vertexEnd)
    {
        nbEdge++;
        Element group = getGroup(vertexStart, vertexEnd);
        Element edge1 = document.createElement(EDGE);

        edge1.setAttribute(ATTRIBUTNAME, ATTRIBUTEDGE + nbEdge);
        edge1.setAttribute(ATTRIBUTSTART, vertexStart);
        edge1.setAttribute(ATTRIBUTEND, vertexEnd);

        racine.setAttribute(GRAPHTYPE, NONDIGRAPH);
        group.appendChild(edge1);

        //group.appendChild(edge2);
        edgesGroups.get(getId(group)).add(edge1);

        //edgesGroups.get(getId(group)).add(edge2);

        edges.add(edge1);
        //edges.add(edge2);

        return getId(group);
    }
    public int addWeightedEdge(String vertexStart, String vertexEnd, String weight)
    {
        nbEdge++;
        Element group = getGroup(vertexStart, vertexEnd);
        Element edge1 = document.createElement(EDGE);

        edge1.setAttribute(ATTRIBUTNAME, ATTRIBUTEDGE + nbEdge);
        edge1.setAttribute(ATTRIBUTSTART, vertexStart);
        edge1.setAttribute(ATTRIBUTEND, vertexEnd);
        edge1.setAttribute(WEIGHT, weight);
        racine.setAttribute(GRAPHTYPE, WEIGHTEDNONDIGRAPH);
        racine.appendChild(edge1);

        group.appendChild(edge1);
        //group.appendChild(edge2);

        edgesGroups.get(getId(group)).add(edge1);
        //edgesGroups.get(getId(group)).add(edge2);

        edges.add(edge1);
        //edges.add(edge2);

        return getId(group);
    }

    public int addDiEdge(String vertexStart, String vertexEnd)
    {
        nbEdge++;
        Element group = getGroup(vertexStart, vertexEnd);
        Element edge = document.createElement(EDGE);

        edge.setAttribute(ATTRIBUTNAME, ATTRIBUTEDGE + nbEdge);
        edge.setAttribute(ATTRIBUTSTART, vertexStart);
        edge.setAttribute(ATTRIBUTEND, vertexEnd);
        racine.setAttribute(GRAPHTYPE, DIGRAPH);

        group.appendChild(edge);
        edgesGroups.get(getId(group)).add(edge);
        edges.add(edge);

        return getId(group);
    }

    public int addDiWeightedEdge(String vertexStart, String vertexEnd, String weight)
    {
        nbEdge++;
        Element group = getGroup(vertexStart, vertexEnd);
        Element edge = document.createElement(EDGE);

        edge.setAttribute(ATTRIBUTNAME, ATTRIBUTEDGE + nbEdge);
        edge.setAttribute(ATTRIBUTSTART, vertexStart);
        edge.setAttribute(ATTRIBUTEND, vertexEnd);
        edge.setAttribute(WEIGHT, weight);
        group.appendChild(edge);

        edgesGroups.get(getId(group)).add(edge);
        racine.setAttribute(GRAPHTYPE, WEIGHTEDDIGRAPH);
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

            if(currentNode.getNodeName().equals(VERTEX))
            {
                if(currentNode.getAttribute(ATTRIBUTNAME).equals(vertex))
                {
                    currentNode.setAttribute(POSX,String.valueOf(posX) );
                    currentNode.setAttribute(POSY,String.valueOf(posY) );
                }
            }
        }
    }

    /**
     * get the position of vertex by his number
     * @param index
     * @return
     */
    public Point2D getPosOfVertex(int index)
    {

        Element node = vertexes.get(index);

        return new Point2D(Integer.valueOf(node.getAttribute(POSX)),
                           Integer.valueOf(node.getAttribute(POSY)));
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
            if(node.getAttribute(ATTRIBUTNAME).equals(name))
            {
                return new Point2D(Integer.valueOf(node.getAttribute(POSX)),
                                   Integer.valueOf(node.getAttribute(POSY)));
            }
            i++;
        }

        return null;
    }
    public String getName(int index)
    {
        Element node = (Element) racine.getChildNodes().item(index);
        return node.getAttribute(ATTRIBUTNAME);
    }
    public String getVertexName(int index)
    {
        return vertexes.get(index).getAttribute(ATTRIBUTNAME);
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
        String vertex1 = edge.getAttribute(ATTRIBUTSTART);
        String vertex2 = edge.getAttribute(ATTRIBUTEND);

        Point2D ver1 =  getPosOfVertex(vertex1);
        Point2D ver2 =  getPosOfVertex(vertex2);

        int startX = (int)ver1.getX();
        int startY = (int)ver1.getY();
        int endX   = (int)ver2.getX();
        int endY   = (int)ver2.getY();
        return new Line(startX, startY, endX, endY);
    }

    public void deleteVertex(int index)
    {

        NodeList listEdges    = document.getElementsByTagName(EDGE);
        NodeList listvertexes = document.getElementsByTagName(VERTEX);
        NodeList listGroups   = document.getElementsByTagName(EDGEGROUP);

        Element vertex = (Element) vertexes.get(index);

        //removing group_edges connected to the vertex to remove
        for(int i = groups.size()-1; i >= 0 ; i--)
        {
            if( groups.get(i).getAttribute(ATTRIBUTSTART).equals(vertex.getAttribute(ATTRIBUTNAME)) ||
                groups.get(i).getAttribute(ATTRIBUTEND).equals(vertex.getAttribute(ATTRIBUTNAME))    )
            {
                Element edgeCurr = (Element) listGroups.item(i);

                //removing the element from the corresponding deleted edge
                edgeCurr.getParentNode().removeChild(edgeCurr);

                // removing from list
                groups.remove(i);
                edgesGroups.remove(i);
            }
        }

        //removing edges connected to the vertex to remove
        for(int i = edges.size()-1; i >= 0 ; i--)
        {
            if( edges.get(i).getAttribute(ATTRIBUTSTART).equals(vertex.getAttribute(ATTRIBUTNAME)) ||
                    edges.get(i).getAttribute(ATTRIBUTEND).equals(vertex.getAttribute(ATTRIBUTNAME))    )
            {
                // removing from list
                edges.remove(i);
            }
        }
        Element vertexCurr = (Element) listvertexes.item(index);

        //removing the element from the corresponding deleted edge
        vertexCurr.getParentNode().removeChild(vertexCurr);

        //removing from list
        vertexes.remove(index);

        //and now change the start and end of the edgesGroup
        for (int j = 0; j < listGroups.getLength(); j++)
        {
            String nameOfStart = ((Element)listGroups.item(j)).getAttribute(ATTRIBUTSTART);
            String nameOfEnd   = ((Element)listGroups.item(j)).getAttribute(ATTRIBUTEND);

            if(Integer.valueOf(nameOfStart.substring(4)) > index)
            {
                ((Element)listGroups.item(j)).setAttribute( ATTRIBUTSTART, nameOfStart.substring(0, 4) +
                        String.valueOf(Integer.valueOf(nameOfStart.substring(4))-1) );

                groups.get(j).setAttribute( ATTRIBUTSTART, nameOfStart.substring(0, 4) +
                        String.valueOf(Integer.valueOf(nameOfStart.substring(4))-1) );
            }
            if(Integer.valueOf(nameOfEnd.substring(4)) > index)
            {
                ((Element)listGroups.item(j)).setAttribute( ATTRIBUTEND, nameOfEnd.substring(0, 4) +
                        String.valueOf(Integer.valueOf(nameOfEnd.substring(4))-1) );

                groups.get(j).setAttribute( ATTRIBUTEND, nameOfEnd.substring(0, 4) +
                        String.valueOf(Integer.valueOf(nameOfEnd.substring(4))-1) );
            }

            ((Element) listGroups.item(j)).setAttribute(ATTRIBUTNAME, ATTRIBUTGROUP+j);
            groups.get(j).setAttribute(ATTRIBUTNAME, ATTRIBUTGROUP+j);
        }
        //and now the names of edges
        for (int j = 0; j < listEdges.getLength(); j++)
        {
            String nameOfStart = ((Element)listEdges.item(j)).getAttribute(ATTRIBUTSTART);
            String nameOfEnd   = ((Element)listEdges.item(j)).getAttribute(ATTRIBUTEND);

            if(Integer.valueOf(nameOfStart.substring(4)) > index)
            {
                ((Element)listEdges.item(j)).setAttribute( ATTRIBUTSTART, nameOfStart.substring(0, 4) +
                        String.valueOf(Integer.valueOf(nameOfStart.substring(4))-1) );

                edges.get(j).setAttribute( ATTRIBUTSTART, nameOfStart.substring(0, 4) +
                        String.valueOf(Integer.valueOf(nameOfStart.substring(4))-1) );
            }
            if(Integer.valueOf(nameOfEnd.substring(4)) > index)
            {
                ((Element)listEdges.item(j)).setAttribute( ATTRIBUTEND, nameOfEnd.substring(0, 4) +
                        String.valueOf(Integer.valueOf(nameOfEnd.substring(4))-1) );

                edges.get(j).setAttribute( ATTRIBUTEND, nameOfEnd.substring(0, 4) +
                        String.valueOf(Integer.valueOf(nameOfEnd.substring(4))-1) );
            }
            ((Element) listEdges.item(j)).setAttribute(ATTRIBUTNAME, ATTRIBUTEDGE + (j+1));
        }

        //we have here to change the name of the vertexes who are bigger than the deleted one.
        //for example if we have 5 vertexes : ver_0, ver_1, ver_2, ver_3, ver_4
        //if we delete ver_2, we have to change the name of ver_3 to ver_2 and ver_4 to ver_3
        for(int i = index; i < vertexes.size(); i++)
        {
            Element vCurr = (Element) listvertexes.item(i);
            int newIndex  = Integer.valueOf(vCurr.getAttribute(ATTRIBUTNAME).substring(4)) - 1;

            vCurr.setAttribute(ATTRIBUTNAME, vCurr.getAttribute(ATTRIBUTNAME).substring(0,4) + String.valueOf(newIndex));
        }
        nbVertex--;
    }

    public ArrayList<DrawEdge> getDrawEdges(int index)
    {
        ArrayList<Element> edges = edgesGroups.get(index);
        ArrayList<DrawEdge> res  = new ArrayList<>();

        for(int i = 0; i < edges.size(); i++)
        {
            String vertex1 = edges.get(i).getAttribute(ATTRIBUTSTART);
            String vertex2 = edges.get(i).getAttribute(ATTRIBUTEND);

            int bending    = 0;
            int bendFactor = 0;

            if (edges.size() > 1) {
                if (groups.get(index).getAttribute(ATTRIBUTSTART).equals(vertex1))
                    bending = (i % 2) + 1;
                else
                    bending = 2 - (i % 2);
            }
            bendFactor = i / 2;
            bendFactor++;

            Point2D ver1 =  getPosOfVertex(vertex1);
            Point2D ver2 =  getPosOfVertex(vertex2);

            int startX = (int)ver1.getX();
            int startY = (int)ver1.getY();
            int endX   = (int)ver2.getX();
            int endY   = (int)ver2.getY();

            if(graphType.equals(NONDIGRAPH))
                res.add(new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, bending, bendFactor));
            else if (graphType.equals(DIGRAPH))
                res.add(new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, bending, bendFactor, true));
            else if (graphType.equals(WEIGHTEDDIGRAPH))
                res.add(new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, bending, bendFactor, true, new Text(edges.get(i).getAttribute(WEIGHT))));
            else if (graphType.equals(WEIGHTEDNONDIGRAPH))
                res.add(new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, bending, bendFactor, false, new Text(edges.get(i).getAttribute(WEIGHT))));
        }

        return res;
    }

    public DrawEdge getEdge(int from, int to, double weight)
    {
        //Element edge = (Element) edges.get(index);
        for(Element edge:edges){
            if(!edge.getAttribute(ATTRIBUTSTART).isEmpty() && !edge.getAttribute(ATTRIBUTEND).isEmpty())
            {
                if (Integer.parseInt(edge.getAttribute(ATTRIBUTSTART).replaceAll("[\\D]", "")) == from &&
                        Integer.parseInt(edge.getAttribute(ATTRIBUTEND).replaceAll("[\\D]", "")) == to &&
                        (weight == 0 || Integer.parseInt(edge.getAttribute(WEIGHT).replaceAll("[\\D]", "")) == weight)) {

                    String vertex1 = edge.getAttribute(ATTRIBUTSTART);
                    String vertex2 = edge.getAttribute(ATTRIBUTEND);

                    Point2D ver1 = getPosOfVertex(vertex1);
                    Point2D ver2 = getPosOfVertex(vertex2);

                    int startX = (int) ver1.getX();
                    int startY = (int) ver1.getY();
                    int endX   = (int) ver2.getX();
                    int endY   = (int) ver2.getY();

                    if(graphType.equals(NONDIGRAPH))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0, 0);
                    else if (graphType.equals(WEIGHTEDNONDIGRAPH))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0, 0, false, new Text(edge.getAttribute(WEIGHT)));
                    else if (graphType.equals(DIGRAPH))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0, 0, true);
                    else if (graphType.equals(WEIGHTEDDIGRAPH))
                        return new DrawEdge((double)startX, (double) startY, (double) endX, (double) endY, 0, 0, true, new Text(edge.getAttribute(WEIGHT)));
                    return null;
                }
            }else {
                System.out.println("NUll");
            }
        }
        return null;
    }
    public double getEdgeWeigth(int gIndex, int eIndex)
    {
        if(graphType.equals(NONDIGRAPH)||graphType.equals(DIGRAPH))
            return 0;
        else
            return Double.parseDouble(edgesGroups.get(gIndex).get(eIndex).getAttribute(WEIGHT).replaceAll("[^1234567890.\\-]", ""));
    }
    public int getFrom(int gIndex, int eIndex){
        return Integer.parseInt(edgesGroups.get(gIndex).get(eIndex).getAttribute(ATTRIBUTSTART).replaceAll("[\\D]", ""));
    }
    public int getTo(int gIndex, int eIndex){
        return Integer.parseInt(edgesGroups.get(gIndex).get(eIndex).getAttribute(ATTRIBUTEND).replaceAll("[\\D]", ""));
    }
    public String getEdgeStartName(int index)
    {
        Element edge = (Element) edges.get(index);
        return edge.getAttribute(ATTRIBUTSTART);
    }

    public String getEdgeEndName(int index)
    {
        Element edge = (Element) edges.get(index);
        return edge.getAttribute(ATTRIBUTEND);
    }

    public String getEdgeStartName(int gIndex, int eIndex)
    {
        Element edge = (Element) edgesGroups.get(gIndex).get(eIndex);
        return edge.getAttribute(ATTRIBUTSTART);
    }

    public String getEdgeEndName(int gIndex, int eIndex)
    {
        Element edge = (Element) edgesGroups.get(gIndex).get(eIndex);
        return edge.getAttribute(ATTRIBUTEND);
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
            if (group.getAttribute(ATTRIBUTSTART).isEmpty() || group.getAttribute(ATTRIBUTEND).isEmpty())
                continue;
            if (Integer.parseInt(group.getAttribute(ATTRIBUTSTART).replaceAll("[\\D]", "")) == from &&
                    Integer.parseInt(group.getAttribute(ATTRIBUTEND).replaceAll("[\\D]", "")) == to)
                res = group;
        }

        if(res == null) {
            res = document.createElement(EDGEGROUP);
            res.setAttribute(ATTRIBUTNAME, ATTRIBUTGROUP+groups.size());
            res.setAttribute(ATTRIBUTSTART, VERNAME+from);
            res.setAttribute(ATTRIBUTEND, VERNAME+to);
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
