import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AStarVisualizerTest {

    AStarVisualizer aStarVisualizer;
    mxGraph graph;
    Object component;

    @Before
    public void setUp() throws Exception {
        graph = new mxGraph();
        mxCell o1 = new mxCell();
        component = o1;
        mxCell o2 = new mxCell();
        mxCell o3 = new mxCell();
        mxCell o4 = new mxCell();
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

        OperationHistory history = new OperationHistory();

        aStarVisualizer = new AStarVisualizer();
        aStarVisualizer.addObserver(history);

        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(aStarVisualizer.getGraphComponent().getGraph());
        aStarAlgorithm.addObserver(history);
        aStarAlgorithm.setVisualizer(aStarVisualizer);

        aStarVisualizer.setAlgorithm(aStarAlgorithm);

        AStarInterface aStarInterface = new AStarInterface(1000, 600, aStarVisualizer, aStarAlgorithm);
        aStarInterface.addObserver(history);
    }

    @After
    public void tearDown() throws Exception {
        aStarVisualizer = null;
    }

    @Test
    public void setAlgorithm() {
        AStarAlgorithm alg = new AStarAlgorithm(graph);
        Assert.assertEquals(alg, aStarVisualizer.setAlgorithm(alg));
    }

    @Test(expected = java.lang.Exception.class)
    public void openGraph_Exception() throws Exception{
        aStarVisualizer.openGraph("./MyGraphs/11561ooho121.xml");
    }

    @Test
    public void saveGraph() {
        Assert.assertEquals("", aStarVisualizer.saveGraph("./MyGraphs/Boi.xml"));
    }

    @Test
    public void getGraphComponent() {
        aStarVisualizer.getGraphComponent();
    }

    @Test
    public void paintPath() {
        Assert.assertEquals(component, aStarVisualizer.paintPath(component));
    }

    @Test
    public void paintPast() {
        Assert.assertEquals(component, aStarVisualizer.paintPast(component));
    }

    @Test
    public void paintNow() {
        Assert.assertEquals(component, aStarVisualizer.paintNow(component));
    }

    @Test
    public void paintFuture() {
        Assert.assertEquals(component, aStarVisualizer.paintFuture(component));
    }

    @Test
    public void paintDefaultComponent() {
        Assert.assertEquals(component, aStarVisualizer.paintDefaultComponent(component));
    }

    @Test
    public void setListenerEditVertex() {
        aStarVisualizer.setListenerEditVertex();
    }

    @Test
    public void removeListenerEditVertex() {
        aStarVisualizer.setListenerEditVertex();
    }

    @Test
    public void setListenerAddStartFinish() {
        aStarVisualizer.setListenerAddStartFinish();
    }

    @Test
    public void removeListenerAddStartFinish() {
        aStarVisualizer.removeListenerAddStartFinish();
    }

    @Test
    public void clearGraph() {
        aStarVisualizer.clearGraph();
    }

    @Test
    public void setSink() {
        Assert.assertEquals(component, aStarVisualizer.setSink(component));
    }

    @Test
    public void setSource() {
        Assert.assertEquals(component, aStarVisualizer.setSource(component));
    }

    @Test
    public void setStartFinishUneditable() {
        Assert.assertEquals(true, aStarVisualizer.setStartFinishUneditable());
    }

    @Test
    public void isStartFinishEditable() {
        Assert.assertEquals(true, aStarVisualizer.isStartFinishEditable());
    }

    @Test
    public void addObserver() {
        OperationHistory history = new OperationHistory();
        Assert.assertEquals(history, aStarVisualizer.addObserver(history));
    }
}