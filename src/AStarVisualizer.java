
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

public class AStarVisualizer extends JFrame {

    private JButton buttonNext;
    private JButton buttonPrevious;

    private JButton buttonPlay;
    private JButton buttonStop;
    private JButton buttonReset;

    private JSlider sliderAnimationDelay;
    private JTextField textFieldStepNumber;

    private JRadioButton radioButtonManhattanDistance;
    private JRadioButton radioButtonChebyshevDistance;
    private JRadioButton radioButtonEuclidianDistance;

    private ButtonGroup buttonGroupDistances;

    private JRadioButton radioButtonAddingVertices;
    private JRadioButton radioButtonDeletingVertices;
    private JRadioButton radioButtonAddingEdges;
    private JRadioButton radioButtonDeletingEdges;

    private ButtonGroup buttonGroupEditMode;

    private JEditorPane editorPaneLogs;
    private JScrollPane scrollPaneLogs;
    
    private JPanel panelGraphManager;
    private JPanel panelHeuristic;
    private JPanel panelAnimationManager;
    private JPanel panelStepByStepManager;
    private JPanel panelLogs;

    private JPanel panelUserOptions;
    private JPanel panelDrawnerForNastya;

    private JSplitPane splitPaneForeground;
    
    
    public AStarVisualizer(int width, int height) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setName("AStarVisualizer");
        this.setSize(width, height);

        this.buttonNext = new JButton();
        this.buttonNext.setName("buttonNext");
        this.buttonNext.setText("Next");
        
        this.buttonPrevious = new JButton();
        this.buttonPrevious.setName("buttonPrevious");
        this.buttonPrevious.setText("Previous");

        this.buttonReset = new JButton();
        this.buttonReset.setName("buttonReset");
        this.buttonReset.setText("Reset");

        this.buttonPlay = new JButton();
        this.buttonPlay.setName("buttonPlay");
        this.buttonPlay.setText("Play");

        this.buttonStop = new JButton();
        this.buttonStop.setName("buttonStop");
        this.buttonStop.setText("Stop");
        this.buttonStop.setEnabled(false);
        
        this.textFieldStepNumber = new JTextField();
        this.textFieldStepNumber.setName("textFieldStepNumber");
        this.textFieldStepNumber.setHorizontalAlignment(JTextField.CENTER);

        this.radioButtonManhattanDistance = new JRadioButton();
        this.radioButtonManhattanDistance.setName("radioButtonManhattanDistance");
        this.radioButtonManhattanDistance.setText("Manhattan distance");
        this.radioButtonManhattanDistance.setSelected(true);

        this.radioButtonChebyshevDistance = new JRadioButton();
        this.radioButtonChebyshevDistance.setName("radioButtonChebyshevDistance");
        this.radioButtonChebyshevDistance.setText("Chebyshev distance");

        this.radioButtonEuclidianDistance = new JRadioButton();
        this.radioButtonEuclidianDistance.setName("radioButtonEuclidianDistance");
        this.radioButtonEuclidianDistance.setText("Euclidian distance");

        this.buttonGroupDistances = new ButtonGroup();
        this.buttonGroupDistances.add(this.radioButtonManhattanDistance);
        this.buttonGroupDistances.add(this.radioButtonChebyshevDistance);
        this.buttonGroupDistances.add(this.radioButtonEuclidianDistance);

        this.radioButtonAddingVertices = new JRadioButton();
        this.radioButtonAddingVertices.setName("radioButtonAddingVertices");
        this.radioButtonAddingVertices.setText("Adding vertices");
        this.radioButtonAddingVertices.setSelected(true);

        this.radioButtonDeletingVertices = new JRadioButton();
        this.radioButtonDeletingVertices.setName("radioButtonDeletingVertices");
        this.radioButtonDeletingVertices.setText("Deleting vertices");

        this.radioButtonAddingEdges = new JRadioButton();
        this.radioButtonAddingEdges.setName("radioButtonAddingEdges");
        this.radioButtonAddingEdges.setText("Add edges");

        this.radioButtonDeletingEdges = new JRadioButton();
        this.radioButtonDeletingEdges.setName("radioButtonDeletingEdges");
        this.radioButtonDeletingEdges.setText("Deleting edges");

        this.buttonGroupEditMode = new ButtonGroup();
        this.buttonGroupEditMode.add(this.radioButtonAddingVertices);
        this.buttonGroupEditMode.add(this.radioButtonDeletingVertices);
        this.buttonGroupEditMode.add(this.radioButtonAddingEdges);
        this.buttonGroupEditMode.add(this.radioButtonDeletingEdges);

        this.sliderAnimationDelay = new JSlider();
        this.sliderAnimationDelay.setName("sliderAnimationDelay");
        this.sliderAnimationDelay.setMinimum(100);
        this.sliderAnimationDelay.setMaximum(1000);
        this.sliderAnimationDelay.setMajorTickSpacing(100);;
        this.sliderAnimationDelay.setValue(500);

        this.editorPaneLogs = new JEditorPane();
        this.editorPaneLogs.setName("editorPaneLogs");
        
        this.scrollPaneLogs = new JScrollPane(editorPaneLogs);
        this.scrollPaneLogs.setName("scrollPaneLogs");

        this.panelDrawnerForNastya = new JPanel();
        this.panelDrawnerForNastya.setName("panelDrawnerForNastya");
        this.panelDrawnerForNastya.setBorder(new TitledBorder("in my head!"));

        this.panelGraphManager = new JPanel();
        this.panelGraphManager.setName("panelGraphManager");
        this.panelGraphManager.setBorder(new TitledBorder("Graph manager"));
        // this.panelGraphManager.setLayout(new BoxLayout(this.panelGraphManager, BoxLayout.Y_AXIS));
        this.panelGraphManager.add(this.radioButtonDeletingEdges);
        this.panelGraphManager.add(this.radioButtonAddingEdges);
        this.panelGraphManager.add(this.radioButtonDeletingVertices);
        this.panelGraphManager.add(this.radioButtonAddingVertices);

        this.panelHeuristic = new JPanel();
        this.panelHeuristic.setName("panelHeuristic");
        this.panelHeuristic.setBorder(new TitledBorder("Heuristic"));
        this.panelHeuristic.setLayout(new BoxLayout(this.panelHeuristic, BoxLayout.Y_AXIS));
        this.panelHeuristic.add(this.radioButtonManhattanDistance);
        this.panelHeuristic.add(this.radioButtonChebyshevDistance);
        this.panelHeuristic.add(this.radioButtonEuclidianDistance);

        this.panelAnimationManager = new JPanel();
        this.panelAnimationManager.setName("panelAnimationManager");
        this.panelAnimationManager.setBorder(new TitledBorder("Animation manager"));
        this.panelAnimationManager.add(this.sliderAnimationDelay);
        this.panelAnimationManager.add(this.buttonReset);
        this.panelAnimationManager.add(this.buttonStop);
        this.panelAnimationManager.add(this.buttonPlay);

        this.panelStepByStepManager = new JPanel();
        this.panelStepByStepManager.setName("panelStepByStepManager");
        this.panelStepByStepManager.setBorder(new TitledBorder("Step by step manager"));
        this.panelStepByStepManager.add(this.buttonPrevious);
        this.panelStepByStepManager.add(this.textFieldStepNumber);
        this.panelStepByStepManager.add(this.buttonNext);

        this.panelLogs = new JPanel();
        this.panelLogs.setName("panelLogs");
        this.panelLogs.setBorder(new TitledBorder("Logs"));
        this.panelLogs.add(this.scrollPaneLogs);

        this.panelUserOptions = new JPanel();
        this.panelUserOptions.setName("panelUserOptions");
        this.panelUserOptions.setBorder(new TitledBorder("panelUserOptions"));
        this.panelUserOptions.setLayout(new BoxLayout(this.panelUserOptions, BoxLayout.PAGE_AXIS));
        this.panelUserOptions.add(this.panelGraphManager);
        this.panelUserOptions.add(this.panelHeuristic);
        this.panelUserOptions.add(this.panelStepByStepManager);
        this.panelUserOptions.add(this.panelAnimationManager);
        this.panelUserOptions.add(this.panelLogs);
        
        this.splitPaneForeground = new JSplitPane();
        this.splitPaneForeground.setName("splitPaneForeground");
        this.splitPaneForeground.setTopComponent(this.panelUserOptions);
        this.splitPaneForeground.setBottomComponent(this.panelDrawnerForNastya);

        this.add(this.splitPaneForeground);
    }
}
