package Algorithms;

import graph.Edge;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Chu_Liu implements Algorithm{
    private LinkedList<Edge> edgesPath;
    public LinkedList<Edge> getPath(){
        return edgesPath;
    }
}
