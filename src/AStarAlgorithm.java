
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AStarAlgorithm implements IObservable{
    private mxGraph graph;

    private Object source;
    private Object sink;

    private Object importantVertex;
    private Double importantTotal;
    private HashMap<Object, Double> distances;

    private PriorityQueue<MyPair> priorityQueue;

    private HashMap<Object, Object> parent;

    private IHeuristic heuristic;
    private IObserver observer;

    private AStarVisualizer aStarVisualizer;

    public AStarAlgorithm(mxGraph graph) {
        this.graph = graph;
        this.source = null;
        this.sink = null;
        this.distances = new HashMap<>();
        this.parent = new HashMap<>();
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
        Double oldTotal;
        HashMap<Object, Double> oldDistances;
        HashMap<Object, Object> oldParent;
        ArrayList<MyPair> addedPairs;
        PriorityQueue<MyPair> oldQueue;

        public NextStep(){
            oldVertex = importantVertex;
            oldTotal = importantTotal;
            oldDistances = new HashMap<>();
            oldDistances.putAll(distances);
            oldParent = new HashMap<>();
            addedPairs = new ArrayList<>();
            oldQueue = new PriorityQueue<MyPair>();
            oldQueue.addAll(priorityQueue);
        }

        @Override
        public void execute() {

            if (source == null) {
                throw new NullPointerException("Add source vertex");
            }

            if (sink == null) {
                throw new NullPointerException("Add finish vertex");
            }

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
            aStarVisualizer.paintPast(importantVertex);

            // извлекаем из очереди новую вершину
            importantVertex = priorityQueue.peek().getVertex();
            importantTotal = priorityQueue.poll().getTotal();

            if (importantVertex == null) {
                throw new NullPointerException("impotent vertex - null");
            }

            aStarVisualizer.paintNow(importantVertex);

            // обходим инцидентные рёбра
            for (Object edge : graph.getOutgoingEdges(importantVertex)) {
                mxCell edgeCell = (mxCell)edge;

                double tentative = distances.get(importantVertex) + Double.valueOf((String)edgeCell.getValue());
                Object targetVertex = edgeCell.getTarget();

                if (!distances.containsKey(targetVertex) || tentative < distances.get(targetVertex)) {
                    aStarVisualizer.paintFuture(targetVertex);

                    if (distances.containsKey(targetVertex)) {
                        oldDistances.put(targetVertex, distances.get(targetVertex));
                    }
                    distances.put(targetVertex, tentative);

                    if (parent.containsKey(targetVertex)) {
                        oldParent.put(targetVertex, parent.get(targetVertex));
                    }
                    parent.put(targetVertex, importantVertex);

                    Point targetPoint = new Point(((mxCell)targetVertex).getGeometry());
                    Point sinkPoint = new Point(((mxCell)sink).getGeometry());

                    MyPair newPair = new MyPair(targetVertex, tentative + heuristic.getValue(targetPoint, sinkPoint));
                    addedPairs.add(newPair);
                    priorityQueue.add(newPair);
                }
            }

        }

        @Override
        public void undo() {
            importantVertex = oldVertex;
            importantTotal = oldTotal;
            priorityQueue = oldQueue;
            distances = oldDistances;
/*
            priorityQueue.add(new MyPair(oldVertex, oldTotal));

            // обходим инцидентные рёбра
            for (Object edge : graph.getOutgoingEdges(oldVertex)) {
                mxCell edgeCell = (mxCell)edge;

                Object targetVertex = edgeCell.getTarget();

                if (oldDistances.containsKey(targetVertex)) {
                    distances.put(targetVertex, oldDistances.get(targetVertex));
                } else {
                    distances.remove(targetVertex);
                }

                if (oldParent.containsKey(targetVertex)) {
                    parent.put(targetVertex, oldParent.get(targetVertex));
                } else {
                    parent.remove(targetVertex);
                }
            }

            for (MyPair pair: addedPairs) {
                priorityQueue.remove(pair);
            }*/
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
