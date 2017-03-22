package graph;

import java.util.ArrayList;

/**
 * Created by Adrian on 22.03.2017.
 */
public abstract class Graph {
    private static int n;
    private ArrayList<Vertex> vertex;

    public Graph(int nVertex){
        int n = 0;
        for(int i = 0; i < nVertex; i++)
            vertex.add(new Vertex(n++));
    }
}
