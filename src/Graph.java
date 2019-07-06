import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Graph {
    private mxGraphComponent component;

    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    private ListenableGraph<String, DefaultEdge> g;
    private static int inc = 1;

    public Graph() {
        g = new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));
        jgxAdapter = new JGraphXAdapter<>(g);

        this.component = new mxGraphComponent(jgxAdapter);
        Object parent = jgxAdapter.getDefaultParent();

        this.component.setConnectable(true); //Тыкабельность
        this.component.setEventsEnabled(true);
//        this.component.setPageVisible(true); //Видимость страницы

        Object c = jgxAdapter.insertVertex(parent, null, "v" + inc++, 20, 20, 20, 20, "shape=ellipse");
        Object j = jgxAdapter.insertVertex(parent, null, "v" + inc++, 40, 40, 20, 20, "shape=ellipse");
        jgxAdapter.setAllowDanglingEdges(false);

        jgxAdapter.setCellsEditable(false);

        this.component.getViewport().setOpaque(true);
        this.component.getViewport().setBackground(new Color(155, 208, 249));

        this.component.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2) {
                    jgxAdapter.insertVertex(parent, null, "v" + inc++, mouseEvent.getX(), mouseEvent.getY(), 20, 20 , "shape=ellipse");
                }
                if(mouseEvent.getClickCount() == 1) {

                    if(mouseEvent.getButton() == mouseEvent.BUTTON3) {

                        Object cell = component.getCellAt(mouseEvent.getX(), mouseEvent.getY());
                        if (cell != null) {
                            jgxAdapter.getModel().remove(cell);
                            System.out.println("cell=" + jgxAdapter.getLabel(cell));
                        }
                    }
                }
                super.mouseClicked(mouseEvent);
            }
        });
        System.out.println(component.getCells(component.getLayoutAreaSize().getRectangle()).length);

/*        for(mxICell cell: jgxAdapter.getCellToVertexMap().keySet()) {
            System.out.println("OUT : x - " + cell.getGeometry().getPoint().x + ", y - " + cell.getGeometry().getPoint().y);
        }
*/        this.component.addMouseWheelListener(mouseWheelEvent -> {
            if(mouseWheelEvent.getWheelRotation() < 0) {
                component.zoomIn();
            }
            else
                component.zoomOut();
        });
        this.component.getConnectionHandler().addListener(mxEvent.CONNECT, (sender, evt) -> System.out.println("edge=" + evt.getProperties().toString()));

//        this.component.addListener(mxEvent.ADD_CELLS, (o, mxEventObject) -> System.out.println("cell - " + mxEventObject.getName() + " with properties: " + mxEventObject.getProperties()));
    }

    public mxGraphComponent getComponent() {
        return component;
    }

}
