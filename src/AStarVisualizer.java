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

public class AStarVisualizer {
    private mxGraphComponent graphComponent;

    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    private ListenableGraph<String, DefaultEdge> g;
    private static int inc = 1;

    public AStarVisualizer() {
        g = new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));
        jgxAdapter = new JGraphXAdapter<>(g);

        this.graphComponent = new mxGraphComponent(jgxAdapter);
        Object parent = jgxAdapter.getDefaultParent();

        this.graphComponent.setConnectable(true); //�������������
        this.graphComponent.setEventsEnabled(true);
        // this.graphComponent.setPageVisible(true); //��������� ��������

        // TODO: смотреть нижний TODO
        Object c = jgxAdapter.insertVertex(parent, null, "v" + inc++, 20, 20, 30, 30, "shape=ellipse");
        Object j = jgxAdapter.insertVertex(parent, null, "v" + inc++, 40, 40, 30, 30, "shape=ellipse");
        jgxAdapter.setAllowDanglingEdges(false);

        jgxAdapter.setCellsEditable(false);

        this.graphComponent.getViewport().setOpaque(true);
        this.graphComponent.getViewport().setBackground(new Color(155, 208, 249));

        this.graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    // TODO: нужно вынести магические константы (как вариант: радиус вершины передавать в конструктор
                    jgxAdapter.insertVertex(parent, null, "v" + inc++, mouseEvent.getX()-15, mouseEvent.getY()-15, 30, 30 , "shape=ellipse");
                }
                if (mouseEvent.getClickCount() == 1) {
                    if (mouseEvent.getButton() == mouseEvent.BUTTON3) {
                        Object cell = graphComponent.getCellAt(mouseEvent.getX(), mouseEvent.getY());
                        if (cell != null) {
                            jgxAdapter.getModel().remove(cell);
                            System.out.println("cell=" + jgxAdapter.getLabel(cell));
                        }
                    }
                }
                super.mouseClicked(mouseEvent);
            }
        });

        System.out.println(graphComponent.getCells(graphComponent.getLayoutAreaSize().getRectangle()).length);

        /*
        for(mxICell cell: jgxAdapter.getCellToVertexMap().keySet()) {
            System.out.println("OUT : x - " + cell.getGeometry().getPoint().x + ", y - " + cell.getGeometry().getPoint().y);
        }
        */

        this.graphComponent.addMouseWheelListener(mouseWheelEvent -> {
            if (mouseWheelEvent.getWheelRotation() < 0) {
                graphComponent.zoomIn();
            } else {
                graphComponent.zoomOut();
            }
        });

        this.graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, (sender, evt) -> System.out.println("edge=" + evt.getProperties().toString()));

        // this.graphComponent.addListener(mxEvent.ADD_CELLS, (o, mxEventObject) -> System.out.println("cell - " + mxEventObject.getName() + " with properties: " + mxEventObject.getProperties()));
    }

    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

}
