
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;
import javafx.beans.binding.DoubleBinding;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class AStarAlgorithm implements IObservable{
    private mxGraph graph;

    private Object source;
    private Object sink;

    private Object importantVertex;

    private HashMap<Object, Double> distances;
    private HashMap<Object, Double> heuristics;
    private HashMap<Object, Double> total;

    private HashMap<Object, Object> parent;
    private HashMap<Object, Boolean> visited;

    private PriorityQueue<MyPair> priorityQueue;

    private IHeuristic heuristic;
    private IObserver observer;

    private AStarVisualizer aStarVisualizer;

    public class MyPair implements Comparable<MyPair> {

        private Object vertex;
        private double total;

        MyPair(Object vertex, double total) {
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
        this.priorityQueue = new PriorityQueue<>();

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

    public void stepBack() {
        ((OperationHistory)this.observer).stepBack();
    }

    public void reset() {
        ((OperationHistory)this.observer).reset();
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

    private void init() {
        importantVertex = null;
    }

    public class NextStep extends UndoableOperation {

        Object oldVertex;
        double oldDistance;
        String oldColor;

        NextStep() {
        }

        @Override
        public void execute() {
            if (source == null) {
                System.out.println("сорс хуйня");
            }

            if (sink == null) {
                System.out.println("синк хуйня");
            }

            System.out.println("Source: " + ((mxCell)source).getValue());
            System.out.println("Source: " + ((mxCell)sink).getValue());

            // если алгоритм только запустили
            if (importantVertex == null) {
                importantVertex = source;
                priorityQueue.add(new MyPair(importantVertex, heuristic.getValue(new Point(((mxCell)importantVertex).getGeometry()), new Point(((mxCell)sink).getGeometry()))));
                aStarVisualizer.paintComponent(importantVertex, "black");
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
            aStarVisualizer.paintComponent(importantVertex, "gray");

            // сохраняем прошлую вершину
            oldVertex = importantVertex;

            // извлекаем из очереди новую вершину
            Object importantVertex = priorityQueue.poll().getVertex();
            System.out.println(((mxCell)importantVertex).getValue());

            // извлечённую вершину красим в чёрный цвет
            aStarVisualizer.paintComponent(importantVertex, "black");
            visited.put(importantVertex, true);

            // обходим инцидентные рёбра
            for (Object edge : graph.getOutgoingEdges(importantVertex) /*incidentEdges.get(importantVertex)*/) {
                mxCell edgeCell = (mxCell)edge;
                double tentative = distances.get(importantVertex) + Double.valueOf((String)edgeCell.getValue());
                Object targetVertex = edgeCell.getTarget();
                if (!visited.get(targetVertex) || tentative < distances.get(targetVertex)) {
                    double targetX = ((mxCell)targetVertex).getGeometry().getCenterX();
                    double targetY = ((mxCell)targetVertex).getGeometry().getCenterY();
                    Point targetPoint = new Point(targetX, targetY);

                    double sinkX = ((mxCell)sink).getGeometry().getCenterX();
                    double sinkY = ((mxCell)sink).getGeometry().getCenterY();
                    Point sinkPoint = new Point(sinkX, sinkY);

                    priorityQueue.add(new MyPair(targetVertex, tentative + heuristic.getValue(targetPoint, sinkPoint)));
                }
            }

        }

        @Override
        public void undo() {
            // покрасить oldVertex в чёрный цвет
            // priorityQueue.add(new MyPair(distances.get(oldVertex) + heuristic.getValue(oldVertex, sink), oldVertex))
            // покрасить currentVertex в белый, если ранее не посещалась, иначе в белый
            // надо сбросить всё, что добавили в прошлый раз
            // for (edge : oldVertex.getChildEdges)
            // double tentative = distances.get(oldVertex) + edge.getValue();
            // Object targetVertex = edge.getTarget();
            // if (!visited(targetVertex) || tentative < distances.get(targetVertex) {

            //      priorityQueue.add(new MyPair(tentative + heuristic.getValue(targetVertex, sink), targetVertex));
            // }
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