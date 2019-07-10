
public class Main {

    public static void main(String[] args) {
        OperationHistory history = new OperationHistory();
        AStarVisualizer aStarVisualizer = new AStarVisualizer();
        aStarVisualizer.addObserver(history);
        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(aStarVisualizer.getGraphComponent().getGraph());
        aStarAlgorithm.addObserver(history);
        aStarAlgorithm.setVisualizer(aStarVisualizer);
        aStarVisualizer.setAlgorithm(aStarAlgorithm);
        AStarInterface aStarInterface = new AStarInterface(1000, 600, aStarVisualizer, aStarAlgorithm);
        aStarInterface.addObserver(history);
        aStarInterface.setVisible(true);
    }
}
