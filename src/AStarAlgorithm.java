
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;


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

    private void start() {
        for (Object v : this.graph.getChildVertices(this.graph.getDefaultParent())) {
            this.distances.put(v, Double.POSITIVE_INFINITY);
            // this.heuristics.put(v, this.heuristic.getValue(v, this.sink));
            this.parent.put(v, null);
            this.visited.put(v, false);
        }

        this.distances.put(this.source, 0.0);
        this.total.put(this.source, this.heuristics.get(this.source));

        /*
        std::priority_queue<std::pair<double, size_t>, std::vector<std::pair<double, size_t>>, std::greater<std::pair<double, size_t>>> pq;
        pq.push(std::make_pair(total[from], N - from));

        size_t new_from;

        while (!pq.empty()) {
            new_from = N - pq.top().second;
            if (new_from == to) {
                return (recovery_path(from, to, parent));
            }

            pq.pop();
            visited[new_from] = true;

            for (size_t i = 0; i < m_graph[new_from].size(); ++i) {
                size_t v = m_graph[new_from][i].second;
                double tentantive = distances[new_from] + m_graph[new_from][i].first;
                if (!visited[v] || tentantive < distances[v]) {
                    parent[v] = new_from;
                    distances[v] = tentantive;
                    total[v] = distances[v] + heuristics[v];
                    pq.push(std::make_pair (total[v], N - v));
                }
            }
        }
        */
    }

    public String restorePath(Object vertex) {
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
