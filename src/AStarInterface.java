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

        // how splitPaneForeground can be constructed in this moment?
        this.splitPaneForeground.setBottomComponent(new AStarVisualizer().getGraphComponent());

        this.setContentPane(this.splitPaneForeground);
    }
}
