
public class Main {

    public static void main(String[] args) {
        OperationHistory history = new OperationHistory();
        AStarVisualizer aStarVisualizer = new AStarVisualizer();
        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(aStarVisualizer.getGraphComponent().getGraph());
        aStarAlgorithm.addObserver(history);

        AStarInterface aStarInterface = new AStarInterface(1000, 600, aStarVisualizer);
        aStarInterface.setAlgorithm(aStarAlgorithm);
        aStarInterface.setVisible(true);
    }
}
