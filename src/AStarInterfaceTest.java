import com.mxgraph.view.mxGraph;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AStarInterfaceTest {

    AStarInterface aStarInterface;

    @Before
    public void setUp() throws Exception {
        mxGraph graph = new mxGraph();
        aStarInterface = new AStarInterface(12,12, new AStarVisualizer(), new AStarAlgorithm(graph));
    }

    @After
    public void tearDown() throws Exception {
        aStarInterface = null;
    }

    @Test
    public void addObserver() {
        OperationHistory history = new OperationHistory();
        Assert.assertEquals(history, aStarInterface.addObserver(history));
    }

}