
public class Main {

    public static void main(String[] args) {
        AStarVisualizer aStarVisualizer = new AStarVisualizer();
        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(aStarVisualizer.getGraphComponent().getGraph());

        AStarInterface aStarInterface = new AStarInterface(1000, 600, aStarVisualizer);
        aStarInterface.setAlgorithm(aStarAlgorithm);
        aStarInterface.setVisible(true);
    }
}
