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

public class DrawEdge {
    final private Color UNCOLORED = Color.web("da5630");
    final private Color COLORED = Color.web("42f45f");
    // Taille de la flèche
    final private double scale=50;

    private CubicCurve curve1;
    private Path arrowEnd;

    Group root;
    Text text;
    boolean directed;
    int bending;
    int bendFactor;

    /**
     * Constructor for creating the nonDiEdge
     * @param startX starting axial position X
     * @param startY starting axial position Y
     * @param endX ending axial position X
     * @param endY ending axial position Y
     * @param bending if 0: no bend, if 1: bend on 1 side, if 2: bend on the other
     * @param bendFactor choose the intensity of the bending
     */
    DrawEdge(double startX, double startY, double endX, double endY, int bending, int bendFactor){
        this(startX, startY, endX, endY, bending, bendFactor, false, null);
    }

    /**
     * Constructor for creating the diEdge
     * @param startX starting axial position X
     * @param startY starting axial position Y
     * @param endX ending axial position X
     * @param endY ending axial position Y
     * @param bending if 0: no bend, if 1: bend on 1 side, if 2: bend on the other
     * @param bendFactor choose the intensity of the bending
     * @param directed 1 if directed
     */
    DrawEdge(double startX, double startY, double endX, double endY, int bending, int bendFactor, boolean directed){
        this(startX, startY, endX, endY, bending, bendFactor, directed, null);
    }

    /**
     * Constructor for creating the 4 type of edges: nonDiEdge, weightedNonDiEdge, diEdge and weightedDiEdge
     * @param startX starting axial position X
     * @param startY starting axial position Y
     * @param endX ending axial position X
     * @param endY ending axial position Y
     * @param bending if 0: no bend, if 1: bend on 1 side, if 2: bend on the other
     * @param bendFactor choose the intensity of the bending
     * @param directed 1 if directed
     * @param text The weight of the Edge
     */
    DrawEdge(double startX, double startY, double endX, double endY, int bending, int bendFactor, boolean directed,  Text text){
        root = new Group();
        this.text = text;
        this.directed = directed;
        this.bending = bending;
        this.bendFactor = bendFactor;

        // Calcul de l'angle du trait
        double opp, adj, angle, sin, cos;
        adj =  Math.abs(endX - startX);
        opp = Math.abs(endY - startY);
        angle = Math.atan(opp/adj);
        sin = Math.sin(angle);
        cos = Math.cos(angle);

        double movX = 0, movY = 0, lblX = 0, lblY = 0;

        if(bending > 0) {
            movX = 40 * bendFactor * sin;
            movY = 40 * bendFactor * cos;

            if (startY < endY)
                movX *= -1;
            if (startX > endX)
                movY *= -1;
            if (bending == 2) {
                movX *= -1;
                movY *= -1;
            }
        }

        double controlX1, controlX2, controlY1, controlY2;
        controlX1 = controlX2 = (startX + endX) / 2 + movX;
        controlY1 = controlY2 = (startY + endY) / 2 + movY;

        curve1 = new CubicCurve(startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY);
        curve1.setStrokeWidth(4);
        curve1.setSmooth(true);
        curve1.setStroke(UNCOLORED);
        curve1.setFill(Color.TRANSPARENT);


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
                //FIXME Andrea vu que movX et movY sont égal à 0 si il n'y a pas de courbure je peux enlever la partie bendFactir non?
                /*if(bendFactor != 0) {
                    movX /= bendFactor;
                    movY /= bendFactor;
                }
                text.setTranslateX(controlX1 - movX/4 * bendFactor);
                text.setTranslateY(controlY1 - movY/4 * bendFactor);*/
                text.setTranslateX(controlX1 - movX/4);
                text.setTranslateY(controlY1 - movY/4);
                root.getChildren().addAll(curve1, arrowEnd, text);
            }
        }else{
            if(text == null)
                root.getChildren().addAll(curve1);
            else{
                //FIXME Andrea vu que movX et movY sont égal à 0 si il n'y a pas de courbure je peux enlever la partie bendFactir non?
                /*if(bendFactor != 0) {
                    movX /= bendFactor;
                    movY /= bendFactor;
                }
                text.setTranslateX(controlX1 - movX/4 * bendFactor);
                text.setTranslateY(controlY1 - movY/4 * bendFactor);*/
                text.setTranslateX(controlX1 - movX/4);
                text.setTranslateY(controlY1 - movY/4);
                root.getChildren().addAll(curve1, text);
            }
        }

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

    /**
     * set the edge to the color defined in a constant declared at the start of the function
     */
    void setColored() {
        curve1.setStroke(COLORED);
        if (directed) arrowEnd.setStroke(COLORED);
    }

    /**
     * set the edge to the color defined in a constant declared at the start of the function
     */
    void setUncolored() {
        curve1.setStroke(UNCOLORED);
        if(directed) arrowEnd.setStroke(UNCOLORED);
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
    int getBending() {
        return bending;
    }
    int getBendFactor() {
        return bendFactor;
    }
    boolean isDirected(){
        return directed;
    }



}
