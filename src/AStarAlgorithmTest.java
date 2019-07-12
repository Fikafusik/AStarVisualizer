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
    mxGraph graph;
    Object source;
    Object sink;
    Object danglingVertex;

    @Before
    public void setUp() throws Exception{
        history = new OperationHistory();
        graph = new mxGraph();
        alg = new AStarAlgorithm(graph);
        alg.addObserver(history);

        mxCell o1 = new mxCell();
        source = o1;
        mxCell o2 = new mxCell();
        mxCell o3 = new mxCell();
        mxCell o4 = new mxCell();
        danglingVertex = o4;
        sink = o3;
        mxCell e1 = new mxCell();
        mxCell e2 = new mxCell();

        //mxGeometry geom = new mxGeometry();
        o1.setGeometry(new mxGeometry());
        o2.setGeometry(new mxGeometry());
        o3.setGeometry(new mxGeometry());
        o4.setGeometry(new mxGeometry());
        e1.setGeometry(new mxGeometry());
        e2.setGeometry(new mxGeometry());

        e1.getGeometry().setWidth(0);
        e2.getGeometry().setWidth(0);
        o1.getGeometry().setWidth(2);
        o2.getGeometry().setWidth(2);
        o3.getGeometry().setWidth(2);
        o4.getGeometry().setWidth(2);

        graph.addCell(o1);
        graph.addCell(o2);
        graph.addCell(o3);
        graph.addCell(o4);
        graph.addCell(e1);
        graph.addCell(e2);

        e1.setEdge(true);
        e2.setEdge(true);

        graph.addEdge(e1, graph.getDefaultParent(), o1, o2,1);
        graph.addEdge(e2, graph.getDefaultParent(), o2, o3, 2);

        o1.setValue(1);
        o2.setValue(2);
        o3.setValue(3);
        o4.setValue(4);
        e1.setValue(new Double(12));
        e2.setValue(new Double(23));

        alg.setSource(o1);
        alg.setSink(o3);
        setLogger();
        AStarVisualizer vis = new AStarVisualizer();
        alg.setVisualizer(vis).addObserver(history);
    }

    @After
    public void tearDown() throws Exception{
        history.reset();
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

    @Test(expected = AStarException.class)
    public void NoPathExist(){
        alg.setSink(danglingVertex);
        alg.getResult();
    }

    @Test(expected = AStarException.class)
    public void NoSource(){
        alg.setSource(null);
        alg.getResult();
    }

    @Test(expected = AStarException.class)
    public void NoSink(){
        alg.setSink(null);
        alg.getResult();
    }

    @Test(expected = AStarException.class)
    public void tryingToSearchWhenFoundPath(){
        alg.getResult();
        alg.stepNext();
        alg.stepNext();
    }
}