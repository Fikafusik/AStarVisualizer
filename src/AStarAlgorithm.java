
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AStarAlgorithm implements IObservable{
    private mxGraph graph;

    private Object source;
    private Object sink;

    private Object importantVertex;

    private HashMap<Object, Double> distances;

    private PriorityQueue<MyPair> priorityQueue;

    private HashMap<Object, Object> parent;
    private HashMap<Object, Boolean> visited;

    private IHeuristic heuristic;
    private IObserver observer;

    private AStarVisualizer aStarVisualizer;

    public AStarAlgorithm(mxGraph graph) {
        this.graph = graph;
        this.source = null;
        this.sink = null;
        this.distances = new HashMap<>();
        this.parent = new HashMap<>();
        this.visited = new HashMap<>();
        this.heuristic = new ManhattanHeuristic();
        this.priorityQueue = new PriorityQueue<>();
        this.importantVertex = null;
        this.observer = null;
    }

    public void update(mxGraph graph) {
        this.graph = graph;
//      this.source = null;
//      this.sink = null;
        this.distances = new HashMap<>();
        this.parent = new HashMap<>();
        this.visited = new HashMap<>();
        this.heuristic = new ManhattanHeuristic();
        this.priorityQueue = new PriorityQueue<>();
        this.importantVertex = null;

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

    public class MyPair implements Comparable<MyPair> {

        private Object vertex;
        private double total;

        MyPair(Object vertex, double total) {
            this.vertex = vertex;
            this.total = total;
        }

        public Object getVertex() {
            return (this.vertex);
        }

        public double getTotal() {
            return (this.total);
        }

        @Override
        public int compareTo(MyPair myPair) {
            return (int) (this.getTotal() - myPair.getTotal());
        }
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
            sink = this.newSink;
        }

        @Override
        public void undo() {
            sink = this.oldSink;
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

        Object oldVertex;
        double oldDistance;
        String oldColor;

        @Override
        public void execute() {

            if (source == null) {
                throw new NullPointerException("Add source vertex");
            }

            if (sink == null) {
                throw new NullPointerException("Add finish vertex");
            }

            System.out.println("Source: " + ((mxCell)source).getValue());
            System.out.println("Sink: " + ((mxCell)sink).getValue());

            // если алгоритм только запустили
            if (importantVertex == null) {
                importantVertex = source;
                distances.put(importantVertex, 0.0);
                priorityQueue.add(new MyPair(importantVertex, heuristic.getValue(new Point(((mxCell)importantVertex).getGeometry()), new Point(((mxCell)sink).getGeometry()))));
                aStarVisualizer.paintFuture(importantVertex);
                return;
            }

            // если путь уже найден
            if (importantVertex == sink) {
                System.out.println("path found...");
                return;
            }

            // если пути не существует
            if (priorityQueue.isEmpty()) {
                System.out.println("path not found...");
                return;
            }

            // ранее посещённую вершину красим в серый
//            aStarVisualizer.paintComponent(importantVertex, "gray");
            aStarVisualizer.paintPast(importantVertex);

            // сохраняем прошлую вершину
            oldVertex = importantVertex;

            // извлекаем из очереди новую вершину
            System.out.println(priorityQueue.size());
            importantVertex = priorityQueue.poll().getVertex();

            if (importantVertex == null) {
                throw new NullPointerException("impotent vertex - null");
            }

            System.out.println(((mxCell)importantVertex).getValue());

            // извлечённую вершину красим в чёрный цвет
            // aStarVisualizer.paintPast(importantVertex);

            aStarVisualizer.paintNow(importantVertex);
            visited.put(importantVertex, true);

            // обходим инцидентные рёбра
            for (Object edge : graph.getOutgoingEdges(importantVertex) /*incidentEdges.get(importantVertex)*/) {
                mxCell edgeCell = (mxCell)edge;

                double tentative = distances.get(importantVertex) + Double.valueOf((String)edgeCell.getValue());
                Object targetVertex = edgeCell.getTarget();

                if (!distances.containsKey(targetVertex) || tentative < distances.get(targetVertex)) {
                    aStarVisualizer.paintFuture(targetVertex);

                    parent.put(targetVertex, importantVertex);
                    distances.put(targetVertex, tentative);

                    Point targetPoint = new Point(((mxCell)targetVertex).getGeometry());
                    Point sinkPoint = new Point(((mxCell)sink).getGeometry());

                    priorityQueue.add(new MyPair(targetVertex, tentative + heuristic.getValue(targetPoint, sinkPoint)));
                }
            }

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
