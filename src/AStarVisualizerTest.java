import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class AStarVisualizerTest {
    AStarVisualizer aStarVisualizer;

    @Before
    public void setUp() throws Exception {
        aStarVisualizer = new AStarVisualizer();
        File file = new File(".\\Tests\\Test1.xml");
        if(file.isFile()) {
            aStarVisualizer.openGraph(file.getAbsolutePath());
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void openGraph(String absolutePath) throws Exception {
        File file = new File(".\\Tests\\Test1.xml");
        if(file.isFile()) {
            aStarVisualizer.openGraph(file.getAbsolutePath());
        }

    }

    @Test
    public void saveGraph() {
    }

    @Test
    public void getGraphComponent() {
    }

    @Test
    public void paintComponent() {
    }

    @Test
    public void paintPast() {
    }

    @Test
    public void paintNow() {
    }

    @Test
    public void paintFuture() {
    }

    @Test
    public void paintDefaultComponent() {
    }

    @Test
    public void setListenerEditVertex() {
    }

    @Test
    public void removeListenerEditVertex() {
    }

    @Test
    public void setListenerAddStartFinish() {
    }

    @Test
    public void removeListenerAddStartFinish() {
    }

    @Test
    public void clearGraph() {
    }

    @Test
    public void setSink() {
    }
}