package Algorithms.Utils;

import graph.Edge;
import graph.Vertex;

/**
 * Created by francoisquellec on 24.05.17.
 */
public class Step
{
    public enum TYPE{EDGE, VERTEX};

    private TYPE objectToColor;
    private String strutures;
    private String message;
    private Edge edge;
    private Vertex vertex;

    public Step(TYPE objectToColor){
        this.objectToColor = objectToColor;
    }

    public void setStructures(String text){
        this.strutures = text;
    }

    public void setMessage(String text){
        this.message = text;
    }

    public void setEdge(Edge e) {
        edge = e;
    }

    public void setEdge(Vertex v) {
        vertex = v;
    }


    public TYPE getType(){return objectToColor;}
    public String getMessage(){return message;}
    public String getStrutures(){return strutures;}
    public Edge getEdge(){return edge;}
    public Vertex getVertex(){return vertex;}
}
