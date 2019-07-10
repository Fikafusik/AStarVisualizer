
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarAlgorithm implements IObservable{
    private Graph graph;

    private Object source;
    private Object sink;

    private HashMap<Object, Double> distances;
    private HashMap<Object, Double> heuristics;
    private HashMap<Object, Double> total;

    private HashMap<Object, Object> parent;
    private HashMap<Object, Boolean> visited;

    private PriorityQueue<PriorityVertex> queue;

    private IHeuristic heuristic;
    private IObserver observer;

    private AStarVisualizer aStarVisualizer;

    public class PriorityVertex {
        private Vertex vertex;
        private int priority;

        public PriorityVertex(Vertex vertex, int priority){
            this.vertex = vertex;
            this.priority = priority;
        }

        public int getPriority(){
            return priority;
        }

        public Vertex getVertex(){
            return vertex;
        }
    }

    public class AStarComparator implements Comparator<PriorityVertex> {
        public int compare(PriorityVertex x, PriorityVertex y){
            return x.getPriority() - y.getPriority();
        }
    }

    public AStarAlgorithm(Graph graph) {
        this.queue = new PriorityQueue<PriorityVertex>(new AStarComparator());
        this.graph = graph;
        this.source = null;
        this.sink = null;
        this.distances = new HashMap<>();
        this.heuristics = new HashMap<>();
        this.total = new HashMap<>();
        this.parent = new HashMap<>();
        this.visited = new HashMap<>();
        this.heuristic = new ManhattanHeuristic();
        this.observer = null;
    }

    public void setVisualizer(AStarVisualizer visualizer){
        aStarVisualizer = visualizer;
    }

    void setSource(Object vertex) {
        notifyObserver(new SetSource(vertex));
    }

    void setSink(Object vertex) {
        notifyObserver(new SetSink(vertex));
    }

    void setHeuristic(IHeuristic newHeuristic) {
        notifyObserver(new SetHeuristic(newHeuristic));
    }

    public void stepNext() {
        notifyObserver(new NextStep());
    }

    public class SetSource extends UndoableOperation {

        private Object oldSource;
        private Object newSource;

        SetSource(Object vertex) {
            this.oldSource = source;
            this.newSource = vertex;
        }

        @Override
        public void execute(){
            source = this.newSource;
        }

        @Override
        public void undo(){
            source = this.oldSource;
        }
    }

    public class SetSink extends UndoableOperation {

        private Object oldSink;
        private Object newSink;

        SetSink(Object vertex) {
            this.oldSink = sink;
            this.newSink = vertex;
        }

        @Override
        public void execute() {
            source = this.newSink;
        }

        @Override
        public void undo() {
            source = this.oldSink;
        }
    }

    public class SetHeuristic extends UndoableOperation {

        private IHeuristic oldHeuristic;
        private IHeuristic newHeuristic;

        SetHeuristic(IHeuristic newHeuristic) {
            this.oldHeuristic = heuristic;
            this.newHeuristic = newHeuristic;
        }

        @Override
        public void execute() {
            heuristic = this.newHeuristic;
        }

        @Override
        public void undo() {
            heuristic = this.oldHeuristic;
        }
    }

    public class NextStep extends UndoableOperation{
        private PriorityVertex oldCurrent;
        @Override
        public void execute() {

            if(queue.isEmpty()){
                queue.add(new PriorityVertex((Vertex)source, 0));
            }
            oldCurrent = queue.peek();
            Vertex current = queue.remove().getVertex();
            if(current == null) current = (Vertex)source;
            for(Object e : graph.edgesOf(current)){
                aStarVisualizer.paintComponent(e, "black");
                Edge edge = (Edge)e;
                Vertex vertex = (Vertex)(graph.getEdgeTarget(e));
                aStarVisualizer.paintComponent(vertex, "red");
                if(vertex == sink){
                    System.out.println("Done");
                    return;
                }
                double w = edge.getWeight();
                double h = heuristic.getValue(current.getPoint(), vertex.getPoint());
                double distance = w + distances.get(current);
                distances.put(vertex, distance);
                queue.add(new PriorityVertex(vertex,(int)(distance + h)));

            }

        }

        @Override
        public void undo() {
            queue.add(oldCurrent);
        }
    }

    @Override
    public void addObserver(IObserver observer){
        this.observer = observer;
    }

    @Override
    public void removeObserver(IObserver observer){
        this.observer = null;
    }

    @Override
    public void notifyObserver(UndoableOperation operation){
        this.observer.handleEvent(operation);
    }

}
