package graph;

import java.util.ArrayList;

/**
 * Created by Adrian on 22.03.2017.
 */
public class Vertex {
    private int id;
    private String name;

    public Vertex(int id){
        this.id = id;
    }

    public Vertex(int id, String name){
        this(id);
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setId(int id){
        this.id = id;
    }

    public String toString(){
        return id + "";
    }
}
