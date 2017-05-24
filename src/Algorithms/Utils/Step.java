package Algorithms.Utils;

/**
 * Created by francoisquellec on 24.05.17.
 */
public class Step
{
    public enum TYPE{EDGE, VERTEX};

    private TYPE objectToColor;
    private String strutures;
    private String message;

    public Step(TYPE objectToColor){
        this.objectToColor = objectToColor;
    }

    public void setStructures(String text){
        this.strutures = strutures;
    }

    public void setMessage(String text){
        this.message = message;
    }


    public TYPE getType(){return objectToColor;}
    public String getMessage(){return message;}
    public String getStrutures(){return strutures;}
}
