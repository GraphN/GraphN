package graph;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Edge implements Comparable<Edge>{
    // Le poids de l'arête
    private int weigth;

    // Les sommets qui connenctent l'arête Pour un graphe oriente v1 est le sommet de depart
    private Vertex v1, v2;

    // Constructeur arête avec poids
    public Edge(Vertex v1, Vertex v2, int weigth){
        this.v1 = v1;
        this.v2 = v2;
        this.weigth = weigth;
    }

    // Constructeur arête non pondéré, poids = 0;
    public Edge(Vertex v1, Vertex v2){
        this(v1, v2, 0);
    }

    public int Weigth(){
        return weigth;
    }

    public Vertex getV1(){ return v1;}
    public Vertex getV2(){ return v2;}

    // Compares this object with the specified object for order. Returns a negative integer,
    // zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
    public int compareTo(Edge e){
        return this.weigth - e.weigth;
    }

}
