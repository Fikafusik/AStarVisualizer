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
        this.setContentPane(this.splitPaneForeground);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(width, height);
        this.splitPaneForeground.setBottomComponent(new AStarVisualizer().getGraphComponent());
        this.setVisible(true);
    }
}
