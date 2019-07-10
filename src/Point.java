import com.mxgraph.model.mxGeometry;

import java.io.Serializable;

public class Point implements Serializable {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(mxGeometry geometry) {
        this.x = geometry.getCenterX();
        this.y = geometry.getCenterY();
    }

    void setX(double x) {
        this.x = x;
    }

    double getX() {
        return (this.x);
    }

    void setY(double y) {
        this.y = y;
    }

    double getY() {
        return (this.y);
    }

}
