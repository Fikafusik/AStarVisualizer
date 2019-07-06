
public class ChebyshevHeuristic implements IHeuristic {
    public double getValue(Point from, Point to) {
        return (Math.max(Math.abs(from.getX() - to.getX()), Math.abs(from.getY() - to.getY())));
    }
}
