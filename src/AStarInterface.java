import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

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
    private JRadioButton editingAddVertexGraph;
    private JRadioButton editingStartFinishVertex;
    private JButton openButton;
    private JButton saveButton;

    public AStarInterface(int width, int height) {
        this.setContentPane(this.splitPaneForeground);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(width, height);
        AStarVisualizer aStarVisualizer = new AStarVisualizer();
        this.splitPaneForeground.setBottomComponent(aStarVisualizer.getGraphComponent());
        aStarVisualizer.setListenerEditVertex();
        this.setVisible(true);
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                if(actionEvent.getActionCommand() == "Start and Finish") {
                    aStarVisualizer.removeListenerEditVertex();
                    aStarVisualizer.setListenerAddStartFinish();
                }
                else {
                    aStarVisualizer.removeListenerAddStartFinish();
                    aStarVisualizer.setListenerEditVertex();
                }
            }
        };
        editingAddVertexGraph.addActionListener(listener);
        editingStartFinishVertex.addActionListener(listener);

        AStarInterface component = this;
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                FileFilter fileFilter = new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        if(file.isDirectory()) {
                            return true;
                        }
                        else {
                            return file.getName().toLowerCase().endsWith(".graphml");
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "GraphML Documents (*.graphml)";
                    }
                };
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setCurrentDirectory(new File("C:\\Users\\Nastya\\IdeaProjects\\AStarVisualizer\\MyGraphs"));
                int oprion = fileChooser.showOpenDialog(component);
                if (oprion == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    System.out.println("Folder Selected: " + file.getName());
                }
                else {
                    System.out.println("Open command canceled");
                }
            }
        });
    }
}
