package graph;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by francoisquellec on 22.03.17.
 */
public abstract class GraphCommon {
    protected LinkedList<Edge>[] adjacencyLists;

    //Constructeur par defaut
    public GraphCommon(String filename){
        // On cree un scanner pour lire notre fichier
        Scanner scanner = new Scanner(filename);
        String line;
        int nbNode = 0;
        int nbEdges = 0;

        // On lit la premiere ligne pour connaitre les dimensions de notre graphe
        if(scanner.hasNextLine()){
            if(scanner.hasNextInt())
                nbNode = scanner.nextInt();
            if(scanner.hasNextInt())
                nbEdges = scanner.nextInt();
        }

        initList(nbNode);

        //  On lit les lignes suivante pour stocker le graphe

        while (scanner.hasNextLine()) {
            if(scanner.hasNextInt()){
                int node1 = scanner.nextInt();
                if(scanner.hasNextInt()){
                    int node2 = scanner.nextInt();
                    int weigth = 0;
                    if(scanner.hasNextInt()) weigth = scanner.nextInt();
                    addEdge(node1, node2, weigth);
                }
            }
        }
    }

    //On constructeur d'initialisation
    public GraphCommon(int V){
        initList(V);
    }

    protected void initList(int V){
        adjacencyLists = new LinkedList[V];
        for (int i = 0; i < V; i++)
            adjacencyLists[i] = new LinkedList<Edge>();
    }

    public abstract void addEdge(int v, int w);
    public abstract void addEdge(int v, int w, int weigth);
    public abstract LinkedList<Edge> adjacent(int v);
    public abstract int V();
}
