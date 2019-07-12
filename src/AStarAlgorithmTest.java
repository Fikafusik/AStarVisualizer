import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
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
        mxCell o1 = new mxCell();
        source = o1;
        mxCell o2 = new mxCell();
        mxCell o3 = new mxCell();
        sink = o3;
        mxCell e1 = new mxCell();
        mxCell e2 = new mxCell();
        o1.setValue(1);
        o2.setValue(2);
        o3.setValue(3);
        e1.setValue(12);
        e2.setValue(10);
        //mxGeometry geom = new mxGeometry();
        o1.setGeometry(new mxGeometry());
        o2.setGeometry(new mxGeometry());
        o3.setGeometry(new mxGeometry());
        e1.setGeometry(new mxGeometry());
        e2.setGeometry(new mxGeometry());
        graph.addCell(o1);
        graph.addCell(o2);
        graph.addCell(o3);
        graph.addCell(e1);
        graph.addCell(e2);
        graph.addEdge(e1, graph.getDefaultParent(), o1, o2,1);
        graph.addEdge(e2, graph.getDefaultParent(), o2, o3, 2);
        Object[] edges = {e1, e2};
        graph.addAllEdges(edges);

        alg.setSource(o1);
        alg.setSink(o3);
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
        Assert.assertEquals(logger, alg.setLogger(logger));
    }

    @Test
    public void isFinished() {
        Assert.assertEquals(false, alg.isFinished());
    }

    @Test
    public void isValid() {
        Assert.assertEquals(true, alg.isValid());
    }

    @Test
    public void update() {
        mxGraph graph = new mxGraph();
        Assert.assertEquals(graph, alg.update(graph));
    }

    @Test
    public void setVisualizer() {
        AStarVisualizer vis = new AStarVisualizer();
        Assert.assertEquals(vis, alg.setVisualizer(vis));
    }

    @Test
    public void setSource() {
        Object vertex = new Object();
        Assert.assertEquals(vertex, alg.setSource(vertex));
    }

    @Test
    public void setSink() {
        Object vertex = new Object();
        Assert.assertEquals(vertex, alg.setSink(vertex));
    }

    @Test
    public void setHeuristic() {
        IHeuristic heuristic = new ChebyshevHeuristic();
        Assert.assertEquals(heuristic, alg.setHeuristic(heuristic));
    }

    @Test
    public void stepNext() {
        Assert.assertEquals(source, alg.stepNext());
    }

    @Test
    public void getResult() {
        Assert.assertEquals("1 -> 2 -> 3", alg.getResult());
    }

    @Test
    public void addObserver() {
        OperationHistory history = new OperationHistory();
        Assert.assertEquals(history, alg.addObserver(history));
    }
}