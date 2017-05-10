package Algorithms;

import graph.Edge;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by francoisquellec on 24.03.17.
 */
public class Tarjan implements Algorithm{
    private LinkedList<Edge> edgesPath;



    public LinkedList<Edge> getPath(){
        return edgesPath;
    }

   
}
