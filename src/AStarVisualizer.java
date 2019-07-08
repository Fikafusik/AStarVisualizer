import com.mxgraph.swing.mxGraphComponent;
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
    private static int widthDefault = 40;
    private static String styleDefault = "shape=ellipse";
    Object parent;
    Object start;
    Object finish;
    MouseAdapter listenerEditVertex;
    MouseAdapter listenerAddStartFinishVertex;

    public AStarVisualizer() {
        g = new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));
        jgxAdapter = new JGraphXAdapter<>(g);

        this.graphComponent = new mxGraphComponent(jgxAdapter);
        parent = jgxAdapter.getDefaultParent();

        this.graphComponent.setConnectable(true);
        this.graphComponent.setEventsEnabled(true);
        this.graphComponent.getViewport().setOpaque(true);
        jgxAdapter.setAllowDanglingEdges(false);
        jgxAdapter.setCellsEditable(false);
        graphComponent.getViewport().setOpaque(true);

        start = jgxAdapter.insertVertex(parent, null, "v" + inc++, 60, 200, widthDefault, widthDefault, styleDefault);
        finish = jgxAdapter.insertVertex(parent, null, "v" + inc++, 540, 200, widthDefault, widthDefault, styleDefault);
        Object v3 = jgxAdapter.insertVertex(parent, null, "v" + inc++, 380, 50, widthDefault, widthDefault, styleDefault);
        Object v4 = jgxAdapter.insertVertex(parent, null, "v" + inc++, 400, 350, widthDefault, widthDefault, styleDefault);
        Object v5 = jgxAdapter.insertVertex(parent, null, "v" + inc++, 200, 50, widthDefault, widthDefault, styleDefault);
        Object v6 = jgxAdapter.insertVertex(parent, null, "v" + inc++, 280, 350, widthDefault, widthDefault, styleDefault);
        jgxAdapter.insertEdge(parent, null, null, start, v5);
        jgxAdapter.insertEdge(parent, null, null, start, v6);
        jgxAdapter.insertEdge(parent, null, null, v5, v3);
        jgxAdapter.insertEdge(parent, null, null, v3, start);
        jgxAdapter.insertEdge(parent, null, null, v3, v4);
        jgxAdapter.insertEdge(parent, null, null, v4, finish);
        paintStartComponent();
        paintFinishComponent();


        this.graphComponent.getViewport().setBackground(new Color(155, 208, 249));

//        setListenerAddStartFinish();
//        setListenerEditVertex();

//        this.graphComponent.getGraphControl().addMouseListener(listenerEditVertex);

//        this.graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, (sender, evt) -> System.out.println("edge=" + evt.getProperties().toString()));

//        this.graphComponent.addListener(mxEvent.ADD_CELLS, (o, mxEventObject) -> System.out.println("cell - " + mxEventObject.getName() + " with properties: " + mxEventObject.getProperties()));
    }

    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    private void paintStartComponent() {
        if(jgxAdapter.getModel().getGeometry(start).getWidth() != 0)
            jgxAdapter.getModel().setStyle(start, "fillColor=lightgreen;shape=ellipse");
    }

    private void paintFinishComponent() {
        if(jgxAdapter.getModel().getGeometry(finish).getWidth() != 0)
            jgxAdapter.getModel().setStyle(finish, "fillColor=yellow;shape=ellipse");
    }

    public void paintComponent(Object component){
        if(jgxAdapter.getModel().getGeometry(component).getWidth() != 0)
            jgxAdapter.getModel().setStyle(component, "fillColor=red;shape=ellipse");
        else
            jgxAdapter.getModel().setStyle(component,"strokeColor=orange");
    }

    public void paintDefaultComponent(Object component){
        if(jgxAdapter.getModel().getGeometry(component).getWidth() != 0)
            jgxAdapter.getModel().setStyle(component, styleDefault);
        else
            jgxAdapter.getModel().setStyle(component,"");
    }

    public void setListenerEditVertex() {
        listenerEditVertex = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Object cell = graphComponent.getCellAt(mouseEvent.getX(), mouseEvent.getY());
                if (mouseEvent.getClickCount() == 2) {
                    if(mouseEvent.getButton() == mouseEvent.BUTTON1)
                        if(cell == null)
                            jgxAdapter.insertVertex(parent, null, "v" + inc++, mouseEvent.getX() - widthDefault/2, mouseEvent.getY() - widthDefault/2, widthDefault, widthDefault , styleDefault);

                }
                if (mouseEvent.getClickCount() == 1) {
                    if (mouseEvent.getButton() == mouseEvent.BUTTON3) {
                        if (cell != null) {
                            for(Object child : graphComponent.getGraph().getEdges(cell))
                                jgxAdapter.getModel().remove(child);
                            jgxAdapter.getModel().remove(cell);
                            System.out.println("cell delete =" + jgxAdapter.getLabel(cell));
                        }
                    }
                }
                super.mouseClicked(mouseEvent);
            }
        };
        this.graphComponent.getGraphControl().addMouseListener(listenerEditVertex);
    }

    public void removeListenerEditVertex() {
        this.graphComponent.getGraphControl().removeMouseListener(listenerEditVertex);
    }

    public void setListenerAddStartFinish() {
        this.jgxAdapter.setCellsMovable(false);
        listenerAddStartFinishVertex = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Object cell = graphComponent.getCellAt(mouseEvent.getX(), mouseEvent.getY());
                if(cell != null ) {
                    if (jgxAdapter.getModel().getGeometry(cell).getWidth() != 0) {
                        if (mouseEvent.getButton() == mouseEvent.BUTTON3) { //Обработать случай старт = финиш
                            if (cell == finish)
                                finish = null;
                            if (start != null)
                                jgxAdapter.getModel().setStyle(start, styleDefault);
                            start = cell;
                            paintStartComponent();
                        }
                        if (mouseEvent.getButton() == mouseEvent.BUTTON1) {
                            if(cell == start)
                                start = null;
                            if (finish != null)
                                jgxAdapter.getModel().setStyle(finish, styleDefault);
                            finish = cell;
                            paintFinishComponent();
                        }
                    }
                }
                super.mouseClicked(mouseEvent);
            }
        };
        this.graphComponent.getGraphControl().addMouseListener(listenerAddStartFinishVertex);
    }

    public void removeAddStartFinish() {
        this.jgxAdapter.setCellsMovable(true);
        this.graphComponent.getGraphControl().removeMouseListener(listenerAddStartFinishVertex);
    }
}
