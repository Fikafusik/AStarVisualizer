import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class AStarAlgorithmTest {

    AStarAlgorithm alg;
    OperationHistory history;
    Object source;
    Object sink;

    @Before
    public void init(){
        history = new OperationHistory();
        mxGraph graph = new mxGraph();
        alg = new AStarAlgorithm(graph);
        alg.addObserver(history);
        mxICell o1 = new mxCell();
        source = o1;
        mxICell o2 = new mxCell();
        sink = o2;
        mxICell e1 = new mxCell();
        o1.setValue(1);
        o2.setValue(2);
        e1.setValue(12);
        o1.setGeometry(new mxGeometry());
        o2.setGeometry(new mxGeometry());
        e1.setGeometry(new mxGeometry());
        graph.addCell(o1);
        graph.addCell(o2);
        graph.addEdge(e1,graph.getDefaultParent(),o1,o2,1);
        alg.setSource(o1);
        alg.setSink(o2);
        setLogger();
        AStarVisualizer vis = new AStarVisualizer();
        alg.setVisualizer(vis).addObserver(history);
    }

    @After
    public void tearDown(){
        alg = null;
    }

    @Test
    public void setLogger() {
        TextPaneLogger logger = new TextPaneLogger(new JTextPane());
        Assert.assertEquals(alg.setLogger(logger), logger);
    }

    @Test
    public void isFinished() {
        Assert.assertEquals(alg.isFinished(), false);
    }

    @Test
    public void isValid() {
        Assert.assertEquals(alg.isValid(), true);
    }

    @Test
    public void update() {
        mxGraph graph = new mxGraph();
        Assert.assertEquals(alg.update(graph), graph);
    }

    @Test
    public void setVisualizer() {
        AStarVisualizer vis = new AStarVisualizer();
        Assert.assertEquals(alg.setVisualizer(vis), vis);
    }

    @Test
    public void setSource() {
        Object vertex = new Object();
        Assert.assertEquals(alg.setSource(vertex), vertex);
    }

    @Test
    public void setSink() {
        Object vertex = new Object();
        Assert.assertEquals(alg.setSink(vertex), vertex);
    }

    @Test
    public void setHeuristic() {
        IHeuristic heuristic = new ChebyshevHeuristic();
        Assert.assertEquals(alg.setHeuristic(heuristic), heuristic);
    }

    @Test
    public void stepNext() {
        Assert.assertEquals(alg.stepNext(), source);
    }

    @Test
    public void getResult() {
        Assert.assertEquals(alg.getResult(), "o1->o2");
    }

    @Test
    public void addObserver() {
        OperationHistory history = new OperationHistory();
        Assert.assertEquals(alg.addObserver(history), history);
    }
}