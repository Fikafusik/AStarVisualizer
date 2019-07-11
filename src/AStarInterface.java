import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.io.File;

public class AStarInterface extends JFrame implements IObservable{
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
    private JMenu menuHelp;
    private JMenuItem menuItemReference;

    private IObserver observer;

    private HeuristicFactory heuristicFactory;
    private AStarAlgorithm aStarAlgorithm;
    private AStarVisualizer aStarVisualizer;
    int valueSlider;

    public AStarInterface(int width, int height, AStarVisualizer aStarVisualizer, AStarAlgorithm aStarAlgorithm) {
        this.aStarAlgorithm = aStarAlgorithm;
        this.aStarVisualizer = aStarVisualizer;
        this.setContentPane(this.splitPaneForeground);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(width, height);

        this.splitPaneForeground.setBottomComponent(aStarVisualizer.getGraphComponent());
        aStarVisualizer.setListenerEditVertex();

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                notifyObserver(new EditVertexMode(actionEvent));
            }
        };

        editingAddVertexGraph.addActionListener(listener);
        editingStartFinishVertex.addActionListener(listener);

        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                aStarVisualizer.clearGraph();
                ((OperationHistory)observer).reset();
                aStarAlgorithm.update(aStarVisualizer.getGraphComponent().getGraph());
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
                    aStarAlgorithm.update(aStarVisualizer.getGraphComponent().getGraph());
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

        this.menuBar = new JMenuBar();

        this.menu = new JMenu("Graph");
        this.menu.add(this.menuItemLoad);
        this.menu.add(this.menuItemSave);
        this.menuBar.add(this.menu);

        this.menuHelp = new JMenu("Help");
        this.menuItemReference = new JMenuItem();
        this.menuItemReference.setText("Reference");
        this.menuHelp.add(this.menuItemReference);

        this.menuBar.add(this.menuHelp);
        this.setJMenuBar(this.menuBar);

        AStarInterface component = this;



        this.menuItemReference.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(component, "If you are in mode of edit vertex:\n" +
                        "   2x click left button - add vertex\n" +
                        "   Click right button - delete vertex\n" +
                        "If you are in mode of edit start-finish:\n" +
                        "   Click left button - add source\n" +
                        "   Click right button - add sink\n");
            }
        });

        heuristicFactory = new HeuristicFactory();

        this.aStarAlgorithm.setHeuristic(heuristicFactory.getHeuristic("Manhattan"));
        heuristicSelection = "Manhattan";
        ActionListener listener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                notifyObserver(new HeuristicChange(actionEvent));
            }
        };
        manhattanDistanceRadioButton.addActionListener(listener1);
        chebyshevDistanceRadioButton.addActionListener(listener1);
        euclidianDistanceRadioButton.addActionListener(listener1);

        valueSlider = slider1.getValue();
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                slider1 = (JSlider)changeEvent.getSource();
                valueSlider = slider1.getValue();
                System.out.println(valueSlider);
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                while(aStarAlgorithm.stepNext();)//Чисто условность
//                        wait();

            }
        });


        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((OperationHistory)observer).undo();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    aStarAlgorithm.stepNext();
                } catch (NullPointerException e1){
                    String ex = new String();
                    for(StackTraceElement el : e1.getStackTrace())
                        ex += el.getMethodName() + "\n";
                    JOptionPane.showMessageDialog(component, e1.getMessage()+ "\n" + ex);
                } catch (NumberFormatException e2) {
                    JOptionPane.showMessageDialog(component, e2.getMessage());
                }
            }
        });
    }


    @Override
    public void addObserver(IObserver observer){
        this.observer = observer;
    }

    @Override
    public void removeObserver(IObserver observer){
        this.observer = null;
    }

    @Override
    public void notifyObserver(UndoableOperation operation){
        this.observer.handleEvent(operation);
    }

    public class EditVertexMode extends UndoableOperation{
        private ActionEvent actionEvent;

        public EditVertexMode(ActionEvent actionEvent){
            this.actionEvent = actionEvent;
        }

        @Override
        public void execute() {
            if(actionEvent == null)
                return;
            if (actionEvent.getActionCommand() == "Start and Finish") {
                aStarVisualizer.removeListenerEditVertex();
                aStarVisualizer.setListenerAddStartFinish();
            } else {
                aStarVisualizer.removeListenerAddStartFinish();
                aStarVisualizer.setListenerEditVertex();

            }
        }
        @Override
        public void undo() {
            if(actionEvent == null)
                return;
            if (actionEvent.getActionCommand() != "Start and Finish") {
                aStarVisualizer.removeListenerEditVertex();
                aStarVisualizer.setListenerAddStartFinish();
                editingStartFinishVertex.setSelected(true);
                editingAddVertexGraph.setSelected(false);
                editingStartFinishVertex.updateUI();
                editingAddVertexGraph.updateUI();
            } else {
                aStarVisualizer.removeListenerAddStartFinish();
                aStarVisualizer.setListenerEditVertex();
                editingStartFinishVertex.setSelected(false);
                editingAddVertexGraph.setSelected(true);
                editingStartFinishVertex.updateUI();
                editingAddVertexGraph.updateUI();

            }

        }
    }
    public String heuristicSelection;
    public class HeuristicChange extends UndoableOperation{

        private ActionEvent actionEvent;
        private String previousSelection;
        private String nextSelection;

        public HeuristicChange(ActionEvent actionEvent){
            this.actionEvent = actionEvent;
            //previousSelection = "Manhattan";
        }
        @Override
        public void execute() {
            previousSelection = heuristicSelection;
            if (buttonGroupDistances.getSelection() == manhattanDistanceRadioButton.getModel()) {
                aStarAlgorithm.setHeuristic(heuristicFactory.getHeuristic("Manhattan"));
                heuristicSelection = "Manhattan";
            }
            if(buttonGroupDistances.getSelection() == chebyshevDistanceRadioButton.getModel()) {
                aStarAlgorithm.setHeuristic(heuristicFactory.getHeuristic("Chebyshev"));
                heuristicSelection = "Chebyshev";
            }
            if(buttonGroupDistances.getSelection() == euclidianDistanceRadioButton.getModel()) {
                aStarAlgorithm.setHeuristic(heuristicFactory.getHeuristic("Euclidean"));
                heuristicSelection = "Euclidean";
            }
        }

        @Override
        public void undo() {
            heuristicSelection = previousSelection;
           // aStarAlgorithm.setHeuristic(heuristicFactory.getHeuristic(heuristicSelection));
            switch (heuristicSelection) {
                case "Chebyshev" :
                    chebyshevDistanceRadioButton.setSelected(true);
                    euclidianDistanceRadioButton.setSelected(false);
                    manhattanDistanceRadioButton.setSelected(false);
                    break;
                case "Manhattan" :
                    chebyshevDistanceRadioButton.setSelected(false);
                    euclidianDistanceRadioButton.setSelected(false);
                    manhattanDistanceRadioButton.setSelected(true);
                    break;
                case "Euclidean" :
                    chebyshevDistanceRadioButton.setSelected(false);
                    euclidianDistanceRadioButton.setSelected(true);
                    manhattanDistanceRadioButton.setSelected(false);
                    break;
            }
            chebyshevDistanceRadioButton.updateUI();
            euclidianDistanceRadioButton.updateUI();
            manhattanDistanceRadioButton.updateUI();
        }
    }
}

