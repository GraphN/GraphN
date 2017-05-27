package graph;

import java.util.ArrayList;

/**
 * Created by Adrian on 22.03.2017.
 */
public class Vertex{
    private int id;
    private String description;

    public Vertex(int id){
        this.id = id;
        this.description = "";
    }

    public Vertex(int id, String description){
        this(id);
        this.description = description;
    }


    public int getId(){
        return id;
    }


    public void setId(int id){
        this.id = id;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vertex)) {
            return false;
        }
        return ((Vertex) other).getId() == id;
    }

    public String toString(){
        return id + "";
    }
}
