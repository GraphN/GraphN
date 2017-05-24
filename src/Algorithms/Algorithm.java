package Algorithms;

import Algorithms.Utils.Step;
import graph.Edge;

import java.util.LinkedList;

/**
 * Created by francoisquellec on 29.04.17.
 */
public interface Algorithm {
    LinkedList<Step> getPath();
}
