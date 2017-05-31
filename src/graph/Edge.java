package graph;

/**
 * Represent an weighted directed Edge in our representation of graphs
 */
public class Edge implements Comparable<Edge>{
    private double weigth;

    // Les sommets qui connenctent l'arête Pour un graphe oriente v1 est le sommet de depart
    private Vertex v1, v2;

    /**
     * Constructor
     * @param v1 the starting vertex
     * @param v2 the ending vertex
     * @param weigth the weight of the edge
     */
    public Edge(Vertex v1, Vertex v2, double weigth){
        this.v1 = v1;
        this.v2 = v2;
        this.weigth = weigth;
    }

    /**
     * Constructor with default weight 0
     * @param v1 the starting vertex
     * @param v2 the ending vertex
     */
    public Edge(Vertex v1, Vertex v2){
        this(v1, v2, 0);
    }

    /**
     * Check if the edge contain the vertex v
     * @param v the vertex to find
     * @return true il if the edge contain the vertex v, false otherwise
     */
    public boolean contains(Vertex v){
        return v == v1 || v == v2;
    }

    /**
     * Getter of the source vertex
     * @return the source vertex
     */
    public Vertex getFrom(){ return v1;}

    /**
     * Getter of the target vertex
     * @return the target vertex
     */
    public Vertex getTo(){ return v2;}

    /**
     * Getter of the other vertex
     * @param v the vertex to ignore
     * @return the vertex that is not v, null if no one is v
     */
    public Vertex getOther(Vertex v){
        if(v1 == v)
            return v2;
        else if(v2 == v)
            return v1;
        else return null;
    }

    /**
     * Comparable override for priority queue handling
     * @param other the object to compare with
     * @return true if the two object are the same
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Edge)) {
            return false;
        }
        return other == this;
    }

    /**
     * Getter weight attribut
     * @return the weight of the edge
     */
    public double getWeigth(){return weigth;}

    /**
     * Display this object in a string
     * @return this object in string
     */
    public String toString(){
        return "(" + v1 + "; " + v2 + "; Poids : " + weigth + ")";
    }

    /**
     * Comparable override for priority queue handling
     * @param e the edge to compare with
     * @return true if the two object are the same
     */
    public int compareTo(Edge e){
        return (int)(weigth - e.weigth);
    }

}
