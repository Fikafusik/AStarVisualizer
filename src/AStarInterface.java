import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AStarInterface extends JFrame implements IObservable{
    private JRadioButton manhattanDistanceRadioButton;
    private JRadioButton chebyshevDistanceRadioButton;
    private JRadioButton euclidianDistanceRadioButton;
    private JSlider sliderAnimationDelay;
    private JButton resetButton;
    private JButton stopButton;
    private JButton startButton;
    private JButton nextButton;
    private JButton previousButton;
    private JSplitPane splitPaneForeground;
    private JRadioButton editingAddVertexGraph;
    private JRadioButton editingStartFinishVertex;
    private JButton cleanButton;
    private JTextPane textLogs;
    private ButtonGroup buttonGroupDistances;
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem menuItemLoad;
    private JMenuItem menuItemSave;
    private JMenu menuHelp;
    private JMenuItem menuItemReference;
    private Timer timerAnimation;
    public String heuristicSelection;


    private IObserver observer;

    private HeuristicFactory heuristicFactory;
    private AStarAlgorithm aStarAlgorithm;
    private AStarVisualizer aStarVisualizer;

    public AStarInterface(int width, int height, AStarVisualizer aStarVisualizer, AStarAlgorithm aStarAlgorithm) {
        this.aStarAlgorithm = aStarAlgorithm;
        this.aStarVisualizer = aStarVisualizer;
        this.setContentPane(this.splitPaneForeground);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(width, height);

        TextPaneLogger logger = new TextPaneLogger(textLogs);
        aStarAlgorithm.setLogger(logger);
        this.splitPaneForeground.setBottomComponent(aStarVisualizer.getGraphComponent());
        aStarVisualizer.setListenerEditVertex();

        ActionListener listener = actionEvent -> notifyObserver(new EditVertexMode(actionEvent));

        editingAddVertexGraph.addActionListener(listener);
        editingStartFinishVertex.addActionListener(listener);

        cleanButton.addActionListener(new ClearActionListener());

        this.menuItemLoad = new JMenuItem();
        this.menuItemLoad.setText("Load");
        this.menuItemLoad.addActionListener(new MenuItemLoadActionListener());

        this.menuItemSave = new JMenuItem();
        this.menuItemSave.setText("Save as...");
        this.menuItemSave.addActionListener(new MenuItemSaveActionListener());

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

        this.menuItemReference.addActionListener(new MenuItemReference());

        heuristicFactory = new HeuristicFactory();

        heuristicSelection = "Manhattan";
        ActionListener listener1 = actionEvent -> notifyObserver(new HeuristicChange(actionEvent));
        manhattanDistanceRadioButton.addActionListener(listener1);
        chebyshevDistanceRadioButton.addActionListener(listener1);
        euclidianDistanceRadioButton.addActionListener(listener1);

        this.timerAnimation = new Timer(sliderAnimationDelay.getValue(), new NextButtonActionListener());
        sliderAnimationDelay.addChangeListener(e -> timerAnimation.setDelay(sliderAnimationDelay.getValue()));
        startButton.addActionListener(actionEvent -> {
            aStarVisualizer.setStartFinishUneditable();
            timerAnimation.start();
        });
        stopButton.addActionListener(e -> timerAnimation.stop());

        previousButton.addActionListener(e->((OperationHistory)observer).stepBack());

        nextButton.addActionListener(new NextButtonActionListener());
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((OperationHistory)observer).reset();
            }
        });
    }

    public class NextButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(aStarAlgorithm.isFinished() || !aStarAlgorithm.isValid()){
                timerAnimation.stop();
            }
            aStarVisualizer.setStartFinishUneditable();
            try {
                aStarAlgorithm.stepNext();
            } catch (AStarException e1){
//                    String ex = new String();
//                   for(StackTraceElement el : e1.getStackTrace())
//                        ex += el.getMethodName() + "\n";
                JOptionPane.showMessageDialog(AStarInterface.this, e1.getMessage()+ "\n" /*+ ex*/);
            } catch(AStarError aStarError){
                JOptionPane.showMessageDialog(AStarInterface.this, aStarError.getMessage()+ "\n");
                ((OperationHistory)observer).reset();
            } catch (NumberFormatException e2) {
                JOptionPane.showMessageDialog(AStarInterface.this, e2.getMessage());
            }
        }
    }

    public class MenuItemReference implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JOptionPane.showMessageDialog(AStarInterface.this, "If you are in mode of edit vertex:\n" +
                    "   2x click left button - add vertex\n" +
                    "   Click right button - delete vertex\n" +
                    "If you are in mode of edit start-finish:\n" +
                    "   Click left button - add source\n" +
                    "   Click right button - add sink\n" +
                    "During algorithm visualization:\n" +
                    "   Blue color node - currently processing node\n" +
                    "   Cyan color node - nodes, that are currently in queue\n" +
                    "   Black color node - nodes, that are already have been processed\n");
        }
    }

    public class ClearActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ((OperationHistory)observer).reset();
            aStarVisualizer.clearGraph();
            aStarAlgorithm.update(aStarVisualizer.getGraphComponent().getGraph());
        }
    }

    public class MenuItemLoadActionListener implements ActionListener {
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
            int option = fileChooser.showOpenDialog(AStarInterface.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                ((OperationHistory)observer).reset();
                try {
                    aStarVisualizer.openGraph(fileChooser.getSelectedFile().getAbsolutePath());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(AStarInterface.this, "Failed opening graph\n" + e.getMessage()+ "\n");
                    aStarVisualizer.clearGraph();
                    //e.printStackTrace();
                    return;
                }
                aStarAlgorithm.update(aStarVisualizer.getGraphComponent().getGraph());
                //System.out.println("Hel");
            }
        }
    }

    public class MenuItemSaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(".\\MyGraphs"));
            int option = fileChooser.showSaveDialog(AStarInterface.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                aStarVisualizer.saveGraph(fileChooser.getSelectedFile().getAbsolutePath());
                File file = fileChooser.getSelectedFile();
                System.out.println("File Selected: " + file.getAbsolutePath());
            }
        }
    }

    @Override
    public IObserver addObserver(IObserver observer){
        this.observer = observer;
        return this.observer;
    }

    @Override
    public void removeObserver(IObserver observer){
        this.observer = null;
    }

    @Override
    public void notifyObserver(UndoableOperation operation) {
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

