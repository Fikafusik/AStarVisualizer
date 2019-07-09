
public class EuclideanHeuristic implements IHeuristic {
    public double getValue(Point from, Point to) {
        return Math.hypot(from.getX() - to.getX(), from.getY() - to.getY());
    }
}
