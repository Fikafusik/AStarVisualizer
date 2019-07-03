
import javax.swing.*;

public class AStarVisualizer extends JFrame {
    private JButton nextStepButton;
    private JButton prevStepButton;

    public AStarVisualizer() {
        this.setName("AStarVisualizer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200, 200, 200, 200);;

        nextStepButton = new JButton();
        nextStepButton.setText("->");

        prevStepButton = new JButton();
        prevStepButton.setText("<-");
    }
}
