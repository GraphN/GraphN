import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

/**
 * Created by LBX on 10/05/2017.
 */
public class EdgeDraw extends Group {
    private final Line line;
    private static final double arrowLength = 20;
    private static final double arrowWidth = 7;

    public EdgeDraw(double startX, double startY, double endX, double endY){
        this(new Line(startX,startY,endX,endY));
    }
    public EdgeDraw(double startX, double startY, double endX, double endY, boolean directed){
        this(new Line(startX,startY,endX,endY), new Line(), new Line());
    }
    public EdgeDraw(double startX, double startY, double endX, double endY, boolean directed, double weight){
        this(new Line(startX,startY,endX,endY), new Line(), new Line(), new Text("" + weight));
    }
    private EdgeDraw(Line line){
        super(line);
        this.line = line;
        setStroke(Color.web("da5630"));
    }
    private EdgeDraw(Line line, Line arrow1, Line arrow2, Text text){
        super(line,arrow1,arrow2,text);
        this.line = line;
        drawArraw(arrow1,arrow2);
        text.setTranslateX((line.getEndX()+line.getStartX())/2);
        text.setTranslateY((line.getEndY()+line.getStartY())/2);
        setStroke(Color.web("da5630"));
    }
    private EdgeDraw(Line line, Line arrow1, Line arrow2){
        super(line,arrow1,arrow2);
        this.line = line;
        drawArraw(arrow1,arrow2);
        setStroke(Color.web("da5630"));
    }
    private void drawArraw(Line arrow1, Line arrow2){
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();

        double factor = arrowLength / Math.hypot(startX-endX, startY-endY);
        double factorO = arrowWidth / Math.hypot(startX-endX, startY-endY);

        // part in direction of main line
        double dx = (startX-endX) * factor;
        double dy = (startY-endY) * factor;

        // part ortogonal to main line
        double ox = (startX-endX) * factorO;
        double oy = (startY-endY) * factorO;

        arrow1.setStartX(endX + dx - oy);
        arrow1.setStartY(endY + dy + ox);
        arrow2.setStartX(endX + dx + oy);
        arrow2.setStartY(endY + dy - ox);
    }
    public void setStroke(Color color){
        line.setStrokeWidth(4);
        line.setSmooth(true);
        line.setStroke(color);
    }

    public void setStartX(double startX){
        line.setStartX(startX);
    };
    public void setStartY(double startY){
        line.setStartY(startY);
    };
    public void setEndX(double endX){
        line.setEndX(endX);
    };
    public void setEndY(double endY){
        line.setEndY(endY);
    };
}
