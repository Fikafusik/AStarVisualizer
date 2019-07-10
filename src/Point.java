import com.mxgraph.model.mxGeometry;

public class Point {
    double x;
    double y;

    public Point(double x, double y) {
        setX(x);
        setY(y);
    }

    public Point(mxGeometry geometry) {
        setX(geometry.getCenterX());
        setY(geometry.getCenterY());
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
