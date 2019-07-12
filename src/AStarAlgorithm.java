import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class AStarAlgorithm implements IObservable{
    private mxGraph graph;

    private boolean alreadyFound;
    private boolean alreadyNotFound;

    private TextPaneLogger logger;

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
        this.source = null;
        this.sink = null;
        update(graph);
    }

    public TextPaneLogger setLogger(TextPaneLogger logger){
        this.logger = logger;
        return this.logger;
    }

    public boolean isFinished(){
        return (alreadyFound || alreadyNotFound);
    }

    public boolean isValid() {
        return (source != null) && (sink != null);
    }

    public mxGraph update(mxGraph graph) {
        this.graph = graph;
        this.distances = new HashMap<>();
        this.parent = new HashMap<>();
        this.heuristic = new ManhattanHeuristic();
        this.priorityQueue = new PriorityQueue<>();
        this.importantVertex = null;
        this.alreadyNotFound = false;
        this.alreadyFound = false;
        return this.graph;
    }

    public AStarVisualizer setVisualizer(AStarVisualizer visualizer){
        this.aStarVisualizer = visualizer;
        return this.aStarVisualizer;
    }

    public Object setSource(Object vertex) {
        notifyObserver(new SetSource(vertex));
        return this.source;
    }

    public Object setSink(Object vertex) {
        notifyObserver(new SetSink(vertex));
        return this.sink;
    }

    public IHeuristic setHeuristic(IHeuristic newHeuristic) {
        notifyObserver(new SetHeuristic(newHeuristic));
        return this.heuristic;
    }

    public Object stepNext() {
        notifyObserver(new NextStep());
        return this.importantVertex;
    }

    public String getResult(){
        while(alreadyFound || alreadyNotFound){
            stepNext();
        }
        return pathRecovery();
    }

    private String pathRecovery() {
        Stack<Object> pathStack = new Stack<>();
        Object vertex = sink;
        while (vertex != null) {
            pathStack.push(vertex);
            vertex = parent.get(vertex);
        }
        StringBuilder pathBuilder = new StringBuilder();
        while (!pathStack.isEmpty()) {
            pathBuilder.append(((mxCell) pathStack.pop()).getValue().toString());
            if (!pathStack.isEmpty())
                pathBuilder.append(" -> ");
        }
        return (pathBuilder.toString());
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

    public class NextStep extends UndoableOperation {

        boolean oldAlreadyFound;
        boolean oldAlreadyNotFound;

        Object oldVertex;
        Double oldTotal;
        HashMap<Object, Double> oldDistances;
        HashMap<Object, Object> oldParent;
        PriorityQueue<MyPair> oldQueue;

        public NextStep() {
            oldAlreadyFound = alreadyFound;
            oldAlreadyNotFound = alreadyNotFound;

            oldVertex = importantVertex;
            oldTotal = importantTotal;

            oldDistances = new HashMap<>();
            oldDistances.putAll(distances);

            oldParent = new HashMap<>();
            oldParent.putAll(parent);

            oldQueue = new PriorityQueue<>();
            oldQueue.addAll(priorityQueue);
        }

        @Override
        public void execute(){

            if (source == null) {
                throw new AStarException("Add source vertex");
            }

            if (sink == null) {
                throw new AStarException("Add finish vertex");
            }

            // если алгоритм только запустили
            if (importantVertex == null) {
                logger.log("first step of algorithm!");
                importantVertex = source;
                distances.put(importantVertex, 0.0);
                priorityQueue.add(new MyPair(importantVertex, heuristic.getValue(new Point(((mxCell)importantVertex).getGeometry()), new Point(((mxCell)sink).getGeometry()))));
                aStarVisualizer.paintFuture(importantVertex);
                return;
            }

            // если путь уже найден
            if (importantVertex == sink) {
                if (alreadyFound) {
                    throw new AStarException("Path was already found");
                }
                alreadyFound = true;

                System.out.println("path found...");
                System.out.println(pathRecovery());

                Object vertex = sink;
                while (vertex != null) {
                    aStarVisualizer.paintPath(vertex);
                    if (parent.containsKey(vertex)) {
                        aStarVisualizer.paintPath(graph.getEdgesBetween(parent.get(vertex), vertex)[0]);
                    }
                    vertex = parent.get(vertex);
                }

                return;
            }

            // если пути не существует
            if (priorityQueue.isEmpty()) {
                if (alreadyNotFound) {
                    throw new AStarException("No path exist");
                }
                alreadyNotFound = true;

                System.out.println("path not found...");
                return;
            }

            // ранее посещённую вершину красим в серый
            aStarVisualizer.paintPast(importantVertex);

            // извлекаем из очереди новую вершину
            importantVertex = priorityQueue.peek().getVertex();
            importantTotal = priorityQueue.poll().getTotal();

            if (importantVertex == null) {
                throw new NullPointerException("important vertex - null");
            }

            aStarVisualizer.paintNow(importantVertex);

            logger.log("current vertex: " + ((mxCell)importantVertex).getValue());
            logger.log("> known distance: " + distances.get(importantVertex));
            // обходим инцидентные рёбра
            for (Object edge : graph.getOutgoingEdges(importantVertex)) {
                mxCell edgeCell = (mxCell)edge;
                double tentative;
                try {
                    tentative = distances.get(importantVertex) + Double.valueOf((String) edgeCell.getValue());
                }
                catch (Exception e) {
                    throw new AStarError("Invalid name for edge between vertices " + ((mxICell)importantVertex).getValue() + " and " + edgeCell.getTarget().getValue());
                }
                Object targetVertex = edgeCell.getTarget();
                logger.log("neighbor vertex: " + ((mxCell)targetVertex).getValue());
                if (!distances.containsKey(targetVertex)) {
                    logger.log("> distance: unknown");
                } else {
                    logger.log("> known distance: " + distances.get(targetVertex));
                }
                logger.log("> tentative value: " + tentative);

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

                    priorityQueue.add(new MyPair(targetVertex, tentative + heuristic.getValue(targetPoint, sinkPoint)));
                }
            }
            logger.log("----------------------------------------");
        }

        @Override
        public void undo() {
            alreadyFound = oldAlreadyFound;
            alreadyNotFound = oldAlreadyNotFound;
            importantVertex = oldVertex;
            importantTotal = oldTotal;
            priorityQueue = oldQueue;
            distances = oldDistances;
            parent = oldParent;

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
    public IObserver addObserver(IObserver observer){
        this.observer = observer;
        return this.observer;
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
