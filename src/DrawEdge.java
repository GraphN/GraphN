import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

/**
 * Created by LBX on 14/05/2017.
 */

// todo: Mettre la pointe des flèches au bord du cercle
public class DrawEdge {
    final private Color UNCOLORED = Color.web("da5630");
    final private Color COLORED = Color.web("42f45f");

    private CubicCurve curve1;
    private Path arrowEnd;
    Group root;
    Text text;
    boolean directed;
    DrawEdge(double startX, double startY, double endX, double endY){
        this(startX, startY, (startX+endX)/2, (startY+endY)/2, (startX+endX)/2, (startY+endY)/2, endX, endY, null, false);
    }
    DrawEdge(double startX, double startY, double endX, double endY, Text text){
        this(startX, startY, (startX+endX)/2, (startY+endY)/2, (startX+endX)/2, (startY+endY)/2, endX, endY, text, true);
    }

    DrawEdge(double startX, double startY, double endX, double endY, boolean directed){
        this(startX, startY, (startX+endX)/2, (startY+endY)/2, (startX+endX)/2, (startY+endY)/2, endX, endY, null, directed);
    }

    DrawEdge(double startX, double startY, double endX, double endY, boolean directed, Text text){
        this(startX, startY, (startX+endX)/2, (startY+endY)/2, (startX+endX)/2, (startY+endY)/2, endX, endY, text, directed);
    }

    DrawEdge(double startX, double startY, double controlX1,double controlY1,double controlX2,double controlY2, double endX, double endY, Text text, boolean directed){
        root = new Group();
        this.text = text;
        this.directed = directed;

        // bending curve
        //Rectangle srcRect1 = new Rectangle(100,100,50,50);
        //Rectangle dstRect1 = new Rectangle(300,300,50,50);


        curve1 = new CubicCurve(startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY);
        curve1.setStrokeWidth(4);
        curve1.setSmooth(true);
        curve1.setStroke(UNCOLORED);

        // Taille de la flèche
        double scale=50;
        // si on veut une flèche au début
        /*Point2D ori=eval(curve1,0);
        Point2D tan=evalDt(curve1,0).normalize().multiply(scale);
        Path arrowIni=new Path();
        arrowIni.getElements().add(new MoveTo(ori.getX()+0.2*tan.getX()-0.2*tan.getY(),
                ori.getY()+0.2*tan.getY()+0.2*tan.getX()));
        arrowIni.getElements().add(new LineTo(ori.getX(), ori.getY()));
        arrowIni.getElements().add(new LineTo(ori.getX()+0.2*tan.getX()+0.2*tan.getY(),
                ori.getY()+0.2*tan.getY()-0.2*tan.getX()));*/
        if(directed) {
            double paramEval = Math.sqrt(Math.pow(curve1.getEndX() - curve1.getStartX(), 2) +
                               Math.pow(curve1.getEndY() - curve1.getStartY(), 2));
            paramEval = (paramEval-7)/paramEval;
            Point2D ori = eval(curve1, paramEval);
            Point2D tan = evalDt(curve1, 1f).normalize().multiply(scale);
            arrowEnd = new Path();
            arrowEnd.setStrokeWidth(3);
            arrowEnd.setSmooth(true);
            arrowEnd.setStroke(UNCOLORED);
            arrowEnd.getElements().add(new MoveTo(ori.getX() - 0.2 * tan.getX() - 0.2 * tan.getY(),
                    ori.getY() - 0.2 * tan.getY() + 0.2 * tan.getX()));
            arrowEnd.getElements().add(new LineTo(ori.getX(), ori.getY()));
            arrowEnd.getElements().add(new LineTo(ori.getX() - 0.2 * tan.getX() + 0.2 * tan.getY(),
                    ori.getY() - 0.2 * tan.getY() - 0.2 * tan.getX()));
            if(text == null)
                root.getChildren().addAll(curve1, arrowEnd);
            else{
                text.setTranslateX((endX+startX)/2);
                text.setTranslateY((endY+startY)/2);
                root.getChildren().addAll(curve1, arrowEnd, text);
            }
        }
        else root.getChildren().add(curve1);

    }

    /**
     * Evaluate the cubic curve at a parameter 0<=t<=1, returns a Point2D
     * @param c the CubicCurve
     * @param t param between 0 and 1
     * @return a Point2D
     */
    private Point2D eval(CubicCurve c, double t){
        Point2D p=new Point2D(Math.pow(1-t,3)*c.getStartX()+
                3*t*Math.pow(1-t,2)*c.getControlX1()+
                3*(1-t)*t*t*c.getControlX2()+
                Math.pow(t, 3)*c.getEndX(),
                Math.pow(1-t,3)*c.getStartY()+
                        3*t*Math.pow(1-t, 2)*c.getControlY1()+
                        3*(1-t)*t*t*c.getControlY2()+
                        Math.pow(t, 3)*c.getEndY());
        return p;
    }

    /**
     * Evaluate the tangent of the cubic curve at a parameter 0<=t<=1, returns a Point2D
     * @param c the CubicCurve
     * @param t param between 0 and 1
     * @return a Point2D
     */
    private Point2D evalDt(CubicCurve c, float t){
        Point2D p=new Point2D(-3*Math.pow(1-t,2)*c.getStartX()+
                3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlX1()+
                3*((1-t)*2*t-t*t)*c.getControlX2()+
                3*Math.pow(t, 2)*c.getEndX(),
                -3*Math.pow(1-t,2)*c.getStartY()+
                        3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlY1()+
                        3*((1-t)*2*t-t*t)*c.getControlY2()+
                        3*Math.pow(t, 2)*c.getEndY());
        return p;
    }

    void setColored() {
        curve1.setStroke(COLORED);
        if (directed) arrowEnd.setStroke(COLORED);
    }

    void setUncolored() {
        curve1.setStroke(UNCOLORED);
        if(!directed) arrowEnd.setStroke(UNCOLORED);
    }

    void setStartX(double startX){
        curve1.setStartX(startX);
    }
    void setStartY(double startY){
        curve1.setStartY(startY);
    }
    void setEndX(double endX){
        curve1.setEndX(endX);
    }
    void setEndY(double endY){
        curve1.setEndY(endY);
    }
    void setControlX1(double controlX1){
        curve1.setControlX1(controlX1);
    }
    void setControlX2(double controlX2){
        curve1.setControlX2(controlX2);
    }
    void setControlY1(double controlY1){
        curve1.setControlY1(controlY1);
    }
    void setControlY2(double controlY2){
        curve1.setControlY2(controlY2);
    }

    double getEndX(){
        return curve1.getEndX();
    }
    double getStartX(){
        return curve1.getStartX();
    }
    double getEndY(){
        return curve1.getEndY();
    }
    double getStartY(){
        return curve1.getStartY();
    }

    Group getRoot(){
        return root;
    }
    Text getText(){
        return text;
    }

    boolean isDirected(){
        return directed;
    }



}
