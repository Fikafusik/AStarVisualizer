import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JButton cleanButton;
    private ButtonGroup buttonGroupDistances;
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem menuItemLoad;
    private JMenuItem menuItemSave;
    private JMenuItem menuItemReference;
    private HeuristicFactory heuristicFactory;
    private AStarAlgorithm aStarAlgorithm;

    public AStarInterface(int width, int height, AStarVisualizer aStarVisualizer, AStarAlgorithm aStarAlgorithm) {
        this.aStarAlgorithm = aStarAlgorithm;
        this.setContentPane(this.splitPaneForeground);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(width, height);

        this.splitPaneForeground.setBottomComponent(aStarVisualizer.getGraphComponent());
        aStarVisualizer.setListenerEditVertex();


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

        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                aStarVisualizer.clearGraph();
            }
        });

        this.menuItemLoad = new JMenuItem();
        this.menuItemLoad.setText("Load");
        this.menuItemLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileFilter fileFilter = new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        if(file.isDirectory()) {
                            return true;
                        }
                        else {
                            return file.getName().toLowerCase().endsWith(".xml");
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "XML Documents (*.xml)";
                    }
                };
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setCurrentDirectory(new File(".\\MyGraphs"));
                int oprion = fileChooser.showOpenDialog(AStarInterface.this);
                if (oprion == JFileChooser.APPROVE_OPTION) {
                    aStarVisualizer.openGraph(fileChooser.getSelectedFile().getAbsolutePath());
                    File file = fileChooser.getSelectedFile();
                    System.out.println("Folder Selected: " + file.getName());
                }
                else {
                    System.out.println("Open command canceled");
                }
            }
        });

        this.menuItemSave = new JMenuItem();
        this.menuItemSave.setText("Save as...");
        this.menuItemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(".\\MyGraphs"));
                int oprion = fileChooser.showSaveDialog(AStarInterface.this);
                if (oprion == JFileChooser.APPROVE_OPTION) {
                    aStarVisualizer.saveGraph(fileChooser.getSelectedFile().getAbsolutePath());
                    File file = fileChooser.getSelectedFile();
                    System.out.println("File Selected: " + file.getAbsolutePath());
                }
                else {
                    System.out.println("Open command canceled");
                }
            }
        });

        this.menu = new JMenu("Graph");
        this.menu.add(this.menuItemLoad);
        this.menu.add(this.menuItemSave);

        this.menuBar = new JMenuBar();
        this.menuBar.add(this.menu);

        this.setJMenuBar(this.menuBar);

        heuristicFactory = new HeuristicFactory();

        AStarInterface component = this;
        this.aStarAlgorithm.setHeuristic(heuristicFactory.getHeuristic("Manhattan"));

        ActionListener listener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if (buttonGroupDistances.getSelection() == manhattanDistanceRadioButton.getModel()) {
                    component.aStarAlgorithm.setHeuristic(heuristicFactory.getHeuristic("Manhattan"));
                    System.out.println("MAN");
                }
                if(buttonGroupDistances.getSelection() == chebyshevDistanceRadioButton.getModel()) {
                    component.aStarAlgorithm.setHeuristic(heuristicFactory.getHeuristic("Chebyshev"));
                    System.out.println("CHE");
                }
                if(buttonGroupDistances.getSelection() == euclidianDistanceRadioButton.getModel()) {
                    component.aStarAlgorithm.setHeuristic(heuristicFactory.getHeuristic("Euclidean"));
                    System.out.println("EUC");
                }
            }
        };
        manhattanDistanceRadioButton.addActionListener(listener1);
        chebyshevDistanceRadioButton.addActionListener(listener1);
        euclidianDistanceRadioButton.addActionListener(listener1);

        this.nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aStarAlgorithm.stepNext();
            }
        });

        this.previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aStarAlgorithm.stepNext();
            }
        });
    }
}

