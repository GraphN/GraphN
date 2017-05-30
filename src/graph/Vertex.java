package graph;

/**
 * Represent a Vertex in our representation of graphs
 */
public class Vertex{
    private int id;
    private String description;

    /**
     * Constructor
     * @param id the id of the vertex
     */
    public Vertex(int id){
        this.id = id;
        this.description = "";
    }

    /**
     * Constructor
     * @param id the id of the vertex
     * @param description an extra description of the vertex
     */
    public Vertex(int id, String description){
        this(id);
        this.description = description;
    }


    /**
     * Getter id
     * @return id
     */
    public int getId(){
        return id;
    }


    /**
     * Setter id
     * @param id the new id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Getter Description
     * @return the description
     */
    public String getDescription(){
        return description;
    }

    /**
     * Setter description
     * @param description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Override equals methode for comparing two vertex by id
     * @param other the other object to compare with
     * @return true if the two object have the same ids
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vertex)) {
            return false;
        }
        return ((Vertex) other).getId() == id;
    }

    /**
     * Getter id in string
     * @return the id parse in string
     */
    public String toString(){
        return id + "";
    }
}
