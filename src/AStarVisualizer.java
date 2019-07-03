
import javax.swing.*;

public class AStarVisualizer extends JFrame {

    // готово
    private JButton buttonNext;
    private JButton buttonPrevious;
    private JButton buttonPlay;
    private JButton buttonStop;
    private JButton buttonReset;

    private JTextField textFieldStepNumber;

    // готово
    private JRadioButton radioButtonManhattanDistance;
    private JRadioButton radioButtonChebyshevDistance;
    private JRadioButton radioButtonEuclidianDistance;
    private JRadioButton radioButtonAddingVertices;
    private JRadioButton radioButtonDeletingVertices;
    private JRadioButton radioButtonAddingEdges;
    private JRadioButton radioButtonDeletingEdges;

    // private JTabbedPane tabbedPaneOperationMode;

    private JEditorPane editorPaneLogs;

    private JPanel panelGraphManager;
    private JPanel panelHeuristic;
    private JPanel panelAnimationManager;
    private JPanel panelStepByStepManager;
    private JPanel panelLogs;
    
    // private JSplitPane splitPaneForeground;
    private JSlider sliderAnimationDelay;

    // private System.Windows.Forms.PictureBox pictureBox1; // рисовать

    
    public AStarVisualizer() {
        this.setName("AStarVisualizer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200, 50, 1000, 700);;
        
        this.setLayout(null);


        this.buttonPlay = new JButton();
        // this.buttonPlay.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.buttonPlay.setLocation(232, 70);
        this.buttonPlay.setName("button1");
        this.buttonPlay.setSize(75, 23);
        this.buttonPlay.setText("Play");
        // this.buttonPlay.UseVisualStyleBackColor = true;
        

        this.buttonStop = new JButton();
        // this.buttonStop.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.buttonStop.setEnabled(false);
        this.buttonStop.setLocation(302, 71);
        this.buttonStop.setName("buttonStop");
        this.buttonStop.setSize(75, 23);
        this.buttonStop.setText("Stop");
        // this.buttonStop.UseVisualStyleBackColor = true;
        

        this.buttonReset = new JButton();
        this.buttonReset.setLocation(6, 70);
        this.buttonReset.setName("buttonReset");
        this.buttonReset.setSize(75, 23);
        this.buttonReset.setText("Reset");
        // this.buttonReset.UseVisualStyleBackColor = true;

        
        this.buttonNext = new JButton();
        //this.buttonNext.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.buttonNext.setLocation(386, 19);
        this.buttonNext.setName("buttonNext");
        this.buttonNext.setSize(75, 23);
        this.buttonNext.setText("Next");
        // this.buttonNext.UseVisualStyleBackColor = true;
 

        this.buttonPrevious = new JButton();
        this.buttonPrevious.setLocation(6, 19);
        this.buttonPrevious.setName("buttonPrevious");
        this.buttonPrevious.setSize(75, 23);
        this.buttonPrevious.setText("Previous");
        // this.buttonPrevious.UseVisualStyleBackColor = true;


        this.textFieldStepNumber = new JTextField();
        // this.textFieldStepNumber.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
        // | System.Windows.Forms.AnchorStyles.Right)));
        this.textFieldStepNumber.setLocation(87, 21);
        this.textFieldStepNumber.setName("textFieldStepNumber");
        this.textFieldStepNumber.setSize(293, 20);
        this.textFieldStepNumber.setHorizontalAlignment(JTextField.CENTER);


        this.radioButtonManhattanDistance = new JRadioButton();
        // this.radioButtonManhattanDistance.AutoSize = true;
        this.radioButtonManhattanDistance.setSelected(true);
        this.radioButtonManhattanDistance.setLocation(6, 19);
        this.radioButtonManhattanDistance.setName("radioButtonManhattanDistance");
        this.radioButtonManhattanDistance.setSize(119, 17);
        this.radioButtonManhattanDistance.setText("Manhattan distance");
        // this.radioButtonManhattanDistance.UseVisualStyleBackColor = true;
        

        this.radioButtonChebyshevDistance = new JRadioButton();
        // this.radioButtonChebyshevDistance.AutoSize = true;
        this.radioButtonChebyshevDistance.setLocation(6, 42);
        this.radioButtonChebyshevDistance.setName("radioButtonChebyshevDistance");
        this.radioButtonChebyshevDistance.setSize(121, 17);
        this.radioButtonChebyshevDistance.setText("Chebyshev distance");
        // this.radioButtonChebyshevDistance.UseVisualStyleBackColor = true;
        

        this.radioButtonEuclidianDistance = new JRadioButton();
        // this.radioButtonEuclidianDistance.AutoSize = true;
        this.radioButtonEuclidianDistance.setLocation(6, 65);
        this.radioButtonEuclidianDistance.setName("radioButtonEuclidianDistance");
        this.radioButtonEuclidianDistance.setSize(111, 17);
        this.radioButtonEuclidianDistance.setText("Euclidian distance");
        // this.radioButtonEuclidianDistance.UseVisualStyleBackColor = true;


        this.radioButtonAddingVertices = new JRadioButton();
        // this.radioButtonAddingVertices.AutoSize = true;
        this.radioButtonAddingVertices.setSelected(true);
        this.radioButtonAddingVertices.setLocation(6, 19);
        this.radioButtonAddingVertices.setName("radioButtonAddingVertices");
        this.radioButtonAddingVertices.setSize(84, 17);
        this.radioButtonAddingVertices.setText("Adding vertices");
        // this.radioButtonAddingVertices.UseVisualStyleBackColor = true;
        

        this.radioButtonDeletingVertices = new JRadioButton();
        // this.radioButtonDeletingVertices.AutoSize = true;
        this.radioButtonDeletingVertices.setLocation(6, 42);
        this.radioButtonDeletingVertices.setName("radioButtonDeletingVertices");
        this.radioButtonDeletingVertices.setSize(104, 17);
        this.radioButtonDeletingVertices.setText("Deleting vertices");
        // this.radioButtonDeletingVertices.UseVisualStyleBackColor = true;
        

        this.radioButtonAddingEdges = new JRadioButton();
        // this.radioButtonAddingEdges.AutoSize = true;
        this.radioButtonAddingEdges.setLocation(6, 65);
        this.radioButtonAddingEdges.setName("radioButtonAddingEdges");
        this.radioButtonAddingEdges.setSize(76, 17);
        this.radioButtonAddingEdges.setText("Add edges");
        // this.radioButtonAddingEdges.UseVisualStyleBackColor = true;
        

        this.radioButtonDeletingEdges = new JRadioButton();
        // this.radioButtonDeletingEdges.AutoSize = true;
        this.radioButtonDeletingEdges.setLocation(6, 88);
        this.radioButtonDeletingEdges.setName("radioButtonDeletingEdges");
        this.radioButtonDeletingEdges.setSize(96, 17);
        this.radioButtonDeletingEdges.setText("Deleting edges");
        // this.radioButtonDeletingEdges.UseVisualStyleBackColor = true;

        
        this.sliderAnimationDelay = new JSlider();
        // this.sliderAnimationDelay.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
        // | System.Windows.Forms.AnchorStyles.Right)));
        this.sliderAnimationDelay.setLocation(6, 19);
        this.sliderAnimationDelay.setMinimum(100);
        this.sliderAnimationDelay.setMaximum(1000);
        this.sliderAnimationDelay.setName("sliderAnimationDelay");
        this.sliderAnimationDelay.setSize(452, 45);
        this.sliderAnimationDelay.setMajorTickSpacing(100);;
        this.sliderAnimationDelay.setValue(500);


        this.editorPaneLogs = new JEditorPane();
        // this.editorPaneLogs.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
        // | System.Windows.Forms.AnchorStyles.Left) 
        // | System.Windows.Forms.AnchorStyles.Right)));
        this.editorPaneLogs.setLocation(6, 19);
        this.editorPaneLogs.setName("editorPaneLogs");
        this.editorPaneLogs.setSize(455, 175);
        this.editorPaneLogs.setText("");


        this.panelGraphManager = new JPanel();
        // this.panelGraphManager.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
        // | System.Windows.Forms.AnchorStyles.Right)));
        this.panelGraphManager.add(this.radioButtonDeletingEdges);
        this.panelGraphManager.add(this.radioButtonAddingEdges);
        this.panelGraphManager.add(this.radioButtonDeletingVertices);
        this.panelGraphManager.add(this.radioButtonAddingVertices);
        this.panelGraphManager.setLocation(6, 6);
        this.panelGraphManager.setName("panelGraphManager");
        this.panelGraphManager.setSize(467, 111);
        // this.panelGraphManager.setText("Graph manager");

        
        this.panelHeuristic = new JPanel();
        // this.panelHeuristic.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
        // | System.Windows.Forms.AnchorStyles.Right)));
        this.panelHeuristic.add(this.radioButtonEuclidianDistance);
        this.panelHeuristic.add(this.radioButtonChebyshevDistance);
        this.panelHeuristic.add(this.radioButtonManhattanDistance);
        this.panelHeuristic.setLocation(6, 6);
        this.panelHeuristic.setName("panelHeuristic");
        this.panelHeuristic.setSize(464, 88);
        // this.panelHeuristic.setText("Heuristic");


        this.panelAnimationManager = new JPanel();
        // this.panelAnimationManager.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
        // | System.Windows.Forms.AnchorStyles.Right)));
        this.panelAnimationManager.add(this.buttonPlay);
        this.panelAnimationManager.add(this.buttonStop);
        this.panelAnimationManager.add(this.buttonReset);
        // this.panelAnimationManager.add(this.sliderAnimationDelay);
        this.panelAnimationManager.setLocation(6, 100);
        this.panelAnimationManager.setName("panelAnimationManager");
        this.panelAnimationManager.setSize(464, 99);
        // this.panelAnimationManager.setText("Animation manager");

        
        this.panelStepByStepManager = new JPanel();
        // this.panelStepByStepManager.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
        // | System.Windows.Forms.AnchorStyles.Right)));
        this.panelStepByStepManager.add(this.buttonNext);
        this.panelStepByStepManager.add(this.buttonPrevious);
        this.panelStepByStepManager.add(this.textFieldStepNumber);
        this.panelStepByStepManager.setLocation(6, 205);
        this.panelStepByStepManager.setName("panelStepByStepManager");
        this.panelStepByStepManager.setSize(467, 48);
        // this.panelStepByStepManager.setText("Step by step manager");

        
        this.panelLogs = new JPanel();
        // this.panelLogs.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
        // | System.Windows.Forms.AnchorStyles.Left) 
        // | System.Windows.Forms.AnchorStyles.Right)));
        this.panelLogs.add(this.textFieldStepNumber);
        this.panelLogs.setLocation(6, 259);
        this.panelLogs.setName("panelLogs");
        this.panelLogs.setSize(467, 200);
        // this.panelLogs.setText("Logs");

        

        /*
        this.tabbedPaneOperationMode = new JTabbedPane();
        // this.tabbedPaneOperationMode.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
        // | System.Windows.Forms.AnchorStyles.Left) 
        // | System.Windows.Forms.AnchorStyles.Right)));
        this.tabbedPaneOperationMode.Controls.Add(this.tabPage1);
        this.tabbedPaneOperationMode.Controls.Add(this.tabPage2);
        this.tabbedPaneOperationMode.setLocation(3, 3);
        this.tabbedPaneOperationMode.setName("tabbedPaneOperationMode");
        this.tabbedPaneOperationMode.setSize(487, 491);
        // 
        // tabPage1
        // 
        this.tabPage1.Controls.Add(this.panelGraphManager);
        this.tabPage1.Location = new System.Drawing.Point(4, 22);
        this.tabPage1.Name = "tabPage1";
        this.tabPage1.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage1.Size = new System.Drawing.Size(479, 465);
        this.tabPage1.TabIndex = 0;
        this.tabPage1.Text = "tabPage1";
        this.tabPage1.UseVisualStyleBackColor = true;
        // 
        // tabPage2
        // 
        this.tabPage2.Controls.Add(this.panelLogs);
        this.tabPage2.Controls.Add(this.panelStepByStepManager);
        this.tabPage2.Controls.Add(this.panelAnimationManager);
        this.tabPage2.Controls.Add(this.panelHeuristic);
        this.tabPage2.Location = new System.Drawing.Point(4, 22);
        this.tabPage2.Name = "tabPage2";
        this.tabPage2.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage2.Size = new System.Drawing.Size(479, 465);
        this.tabPage2.TabIndex = 1;
        this.tabPage2.Text = "tabPage2";
        this.tabPage2.UseVisualStyleBackColor = true;


        this.splitPaneForeground = new JSplitPane();
        // 
        // splitContainer1
        // 
        this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.splitContainer1.Location = new System.Drawing.Point(0, 0);
        this.splitContainer1.Name = "splitContainer1";
        // 
        // splitContainer1.Panel1
        // 
        this.splitContainer1.Panel1.Controls.Add(this.pictureBox1);
        // 
        // splitContainer1.Panel2
        // 
        this.splitContainer1.Panel2.Controls.Add(this.tabbedPaneOperationMode);
        this.splitContainer1.Size = new System.Drawing.Size(1159, 497);
        this.splitContainer1.SplitterDistance = 662;
        this.splitContainer1.TabIndex = 0;
        
        
        // 
        // pictureBox1
        // 
        this.pictureBox1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
        | System.Windows.Forms.AnchorStyles.Left) 
        | System.Windows.Forms.AnchorStyles.Right)));
        this.pictureBox1.Image = global::AStarVisualizerInterfacePrototype.Properties.Resources.meow;
        this.pictureBox1.Location = new System.Drawing.Point(3, 3);
        this.pictureBox1.Name = "pictureBox1";
        this.pictureBox1.Size = new System.Drawing.Size(656, 491);
        this.pictureBox1.TabIndex = 0;
        this.pictureBox1.TabStop = false;
        // 
        // Form1
        // 
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(1159, 497);
        this.Controls.Add(this.splitContainer1);
        this.Name = "Form1";
        this.Text = "Form1";
        this.Load += new System.EventHandler(this.Form1_Load);
        this.splitContainer1.Panel1.ResumeLayout(false);
        this.splitContainer1.Panel2.ResumeLayout(false);
        ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).EndInit();
        this.splitContainer1.ResumeLayout(false);
        this.tabbedPaneOperationMode.ResumeLayout(false);
        this.tabPage1.ResumeLayout(false);
        this.tabPage2.ResumeLayout(false);
        this.panelHeuristic.ResumeLayout(false);
        this.panelHeuristic.PerformLayout();
        this.panelAnimationManager.ResumeLayout(false);
        this.panelAnimationManager.PerformLayout();
        ((System.ComponentModel.ISupportInitialize)(this.sliderAnimationDelay)).EndInit();
        this.panelStepByStepManager.ResumeLayout(false);
        this.panelStepByStepManager.PerformLayout();
        this.panelGraphManager.ResumeLayout(false);
        this.panelGraphManager.PerformLayout();
        ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
        this.panelLogs.ResumeLayout(false);
        this.ResumeLayout(false);

        */
    }
}
