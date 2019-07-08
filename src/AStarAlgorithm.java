
import com.mxgraph.view.mxGraph;
import java.util.HashMap;
import java.util.Set;

public class AStarAlgorithm {
    // граф)
    private mxGraph graph;
    // начало
    private Object source;
    // конец
    private Object sink;
    // глобальная дистанция
    private HashMap<Object, Double> distances;
    // эвристика для каждой вершины
    private HashMap<Object, Double> heuristics;
    // эвристика + дистанция
    private HashMap<Object, Double> total;
    // предок вершины, из которого оптимально перешли
    private HashMap<Object, Object> parent;
    // посещена ли вершина
    private HashMap<Object, Boolean> visited;
    // текущая эвристика для поиска
    private IHeuristic heuristic;

    private static IHeuristic defaultHeuristic = new ManhattanHeuristic();

    public AStarAlgorithm(mxGraph graph) {
        this.graph = graph;
        this.source = null;
        this.sink = null;
        this.distances = new HashMap<>();
        this.heuristics = new HashMap<>();
        this.total = new HashMap<>();
        this.parent = new HashMap<>();
        this.heuristic = defaultHeuristic;
    }

    private void updateHeuristicValue(Object vertex) {
        // this.heuristics[vertex] = Double.valueOf(this.heuristic.getValue(vertex, this.sink));
        // this.total[vertex] = this.distances[vertex] + this.heuristics[vertex];
    }

    private void updateAllHeuristics() {

    }

    void setSource(Object vertex) {
        this.source = vertex;
    }

    void setSink(Object vertex) {
        this.sink = vertex;
    }

    public void stepNext() {

    }

    public void stepPrevious() {

    }

    public void reset() {

    }
}
