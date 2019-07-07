import javax.swing.*;

public class AStarInterface extends JFrame {
    private JRadioButton manhattanDistanceRadioButton;
    private JRadioButton chebyshevDistanceRadioButton;
    private JRadioButton euclidianDistanceRadioButton;
    private JSlider slider1;
    private JButton resetButton;
    private JButton stopButton;
    private JButton startButton;
    private JButton nextButton;
    private JButton previousButton;
    private JRadioButton editingRadioButton;
    private JRadioButton animationRadioButton;
    private JSplitPane splitPaneForeground;

    public AStarInterface(int width, int height) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("AStarVisualizer");
        this.setSize(width, height);

        AStarVisualizer aStarVisualizer = new AStarVisualizer();
        // how splitPaneForeground can be constructed in this moment?
        this.splitPaneForeground.setBottomComponent(aStarVisualizer.getGraphComponent());

        this.setContentPane(this.splitPaneForeground);

        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(aStarVisualizer.getGraphComponent().getGraph());
        aStarAlgorithm.setSource(aStarVisualizer.c);
        aStarAlgorithm.setSink(aStarVisualizer.j);

        System.out.println(aStarAlgorithm.algorithm());
    }
}
