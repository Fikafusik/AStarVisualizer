
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

class Pair implements Comparable<Pair> {
    public Double first;
    public Object second;

    Pair(Double first, Object second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Pair pair) {
        if (this.first > pair.first) {
            return 1;
        } else if (this.first < pair.first) {
            return -1;
        } else {
            return 0;
        }
    }
}

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

    private PriorityQueue<Pair> priorityQueue;

    private static IHeuristic defaultHeuristic = new ManhattanHeuristic();

    public AStarAlgorithm(mxGraph graph) {
        this.graph = graph;
        this.source = null;
        this.sink = null;
        this.distances = new HashMap<>();
        this.heuristics = new HashMap<>();
        this.total = new HashMap<>();
        this.parent = new HashMap<>();
        this.visited = new HashMap<>();
        this.heuristic = defaultHeuristic;
        this.priorityQueue = new PriorityQueue<>();
    }

    public String algorithm() {
        for (Object v : this.graph.getChildVertices(this.graph.getDefaultParent())) {
            this.distances.put(v, Double.POSITIVE_INFINITY);
            // this.heuristics.put(v, this.heuristic.getValue(v, this.sink));
            this.parent.put(v, null);
            this.visited.put(v, false);
        }

        this.distances.put(this.source, 0.0);
        this.total.put(this.source, this.heuristics.get(this.source));


        priorityQueue.add(new Pair(this.total.get(this.source), this.source));
        Object nextVertex;
        while (!priorityQueue.isEmpty()) {
            nextVertex = priorityQueue.peek().second;
            if (nextVertex.equals(this.sink)) {
                return restorePath(this.sink);
            }
            priorityQueue.poll();
            visited.put(nextVertex, true);
            for (Object e : graph.getChildEdges(nextVertex)) {
                Object vertex = ((mxCell)e).getTarget();
                double tentantive = distances.get(nextVertex) + (double)((mxCell)e).getValue();
                if (!visited.get(((mxCell)e).getTarget()) || tentantive < distances.get(((mxCell)e).getTarget())) {
                    parent.put(vertex, nextVertex);
                    distances.put(vertex, tentantive);
                    total.put(vertex, distances.get(vertex) + heuristics.get(vertex));
                    priorityQueue.add(new Pair(this.total.get(vertex), vertex));
                }
            }
        }
        return (null);
    }

    private String restorePath(Object vertex) {
        Stack<Object> pathStack = new Stack<>();
        do {
            pathStack.push(vertex);
            vertex = this.parent.get(vertex);
        } while (!vertex.equals(this.sink));
        StringBuilder pathStringBuilder = new StringBuilder();
        while (!pathStack.empty()) {
            pathStringBuilder.append(((mxCell)pathStack.pop()).getValue().toString());
            pathStringBuilder.append(" ");
        }
        pathStringBuilder.append('\n');
        return (pathStringBuilder.toString());
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
