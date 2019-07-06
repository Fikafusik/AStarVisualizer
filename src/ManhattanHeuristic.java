
public class ManhattanHeuristic implements IHeuristic {
    public double getValue(Point from, Point to) {
        return (Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY()));
    }
}
