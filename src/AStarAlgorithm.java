
import com.mxgraph.view.mxGraph;
import java.util.HashMap;
import java.util.Set;

public class AStarAlgorithm implements IObservable{
    private mxGraph graph;

    private Object source;
    private Object sink;

    private HashMap<Object, Double> distances;
    private HashMap<Object, Double> heuristics;
    private HashMap<Object, Double> total;

    private HashMap<Object, Object> parent;
    private HashMap<Object, Boolean> visited;

    private IHeuristic heuristic;
    private IObserver observer;

    public AStarAlgorithm(mxGraph graph) {
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

        @Override
        public void execute() {

        }

        @Override
        public void undo() {

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
