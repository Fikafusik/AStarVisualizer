
import java.util.HashMap;

public class HeuristicFactory {
    private HashMap<String, IHeuristic> factory;

    HeuristicFactory() {
        factory = new HashMap<>();
    }

    IHeuristic getHeuristic(String heuristicName) {
        if (factory.containsKey(heuristicName)) {
            return factory.get(heuristicName);
        }

        switch (heuristicName) {
            case "Manhattan":
                factory.put(heuristicName, new ManhattanHeuristic());
                break;
            case "Chebyshev":
                factory.put(heuristicName, new ChebyshevHeuristic());
                break;
            case "Euclidean":
                factory.put(heuristicName, new EuclideanHeuristic());
        }

        return factory.get(heuristicName);
    }
}
