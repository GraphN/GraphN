package Algorithms.Utils;

import graph.Edge;
import graph.Vertex;

/**
 * Class that represent a step in graphic mode, A step can contain an edge or a vertex to color and 2
 * String messages respectively for structures states and description of the step
 */
public class Step {
    private String structures;
    private String message;
    private Edge edge;
    private Vertex vertex;

    /**
     * Setter structure
     * @param text the text to display on the algorithm page screen
     */
    public void setStructures(String text){
        this.structures = text;
    }

    /**
     * Setter Message
     * @param text the text to display on the algorithm page screen
     */
    public void setMessage(String text){
        this.message = text;
    }

    /**
     * Setter edge
     * @param e The edge to color on the algorithm page screen
     */
    public void setEdge(Edge e) {
        edge = e;
    }

    /**
     * Setter vertex
     * @param v The vertex to color on the algorithm page screen
     */
    public void setVertex(Vertex v) {
        vertex = v;
    }

    /**
     * Getter Message
     * @return the text to display on the algorithm page screen, "" if no text
     */
    public String getMessage(){return message;}

    /**
     * Getter Structure
     * @return the text to display on the algorithm page screen, "" if no text
     */
    public String getStructures(){return structures;}

    /**
     * Getter Edge
     * @return The edge to color, null if no one
     */
    public Edge getEdge(){return edge;}

    /**
     * Getter Vertex
     * @return The vertex to color, null if no one
     */
    public Vertex getVertex(){return vertex;}
}
