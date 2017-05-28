package graph;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Edge implements Comparable<Edge>{
    // Le poids de l'arête
    private double weigth;

    // Les sommets qui connenctent l'arête Pour un graphe oriente v1 est le sommet de depart
    private Vertex v1, v2;

    // Constructeur arête avec poids
    public Edge(Vertex v1, Vertex v2, double weigth){
        this.v1 = v1;
        this.v2 = v2;
        this.weigth = weigth;
    }

    // Constructeur arête non pondéré, poids = 0;
    public Edge(Vertex v1, Vertex v2){
        this(v1, v2, 0);
    }

    public boolean contains(Vertex v){
        return v == v1 || v == v2;
    }
    public Vertex getFrom(){ return v1;}

    public Vertex getTo(){ return v2;}

    public Vertex getOther(Vertex v){
        if(v1 == v)
            return v2;
        else if(v2 == v)
            return v1;
        else return null;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Edge)) {
            return false;
        }
        //return ((Edge) other).weigth == weigth && ((Edge) other).v1 == v1 && ((Edge) other).v2 == v2;
        return other == this;
    }

    public double getWeigth(){return weigth;}

    public String toString(){
        return "(" + v1 + "; " + v2 + "; Poids : " + weigth + ")";
    }

    // Compare two edges for priority
    public int compareTo(Edge e){
        return (int)(weigth - e.weigth);
    }

}
