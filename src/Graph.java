import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

public class Graph {
    private mxGraphComponent component;

    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    private ListenableGraph<String, DefaultEdge> g;
    private static int inc = 1;

    public Graph() {
        g = new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));
        jgxAdapter = new JGraphXAdapter<>(g);
        this.component = new mxGraphComponent(jgxAdapter);
        mxGraphModel graphModel = (mxGraphModel)component.getGraph().getModel();
        Collection<Object> cells = graphModel.getCells().values();
        mxUtils.setCellStyles(component.getGraph().getModel(), cells.toArray(), mxConstants.STYLE_ENDFILL, mxConstants.SHAPE_ELLIPSE);

        this.jgxAdapter.setCellStyle("CURVE");
        this.component.setConnectable(true); //Тыкабельность
        this.component.setEventsEnabled(true);
//        this.component.setGridVisible(true); //Видимость сетки
//        this.component.setPageVisible(true); //Видимость страницы

        g.addVertex("v" + inc++);

        this.component.getViewport().setOpaque(true);
        this.component.getViewport().setBackground(new Color(155, 208, 249));

        this.component.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2) {
                    g.addVertex("v" + inc++);
//                mouseEvent.getLocationOnScreen()
                    System.out.println("HELLO");
                }
                super.mouseClicked(mouseEvent);
            }
        });

//        this.component.addMouseListener(new MouseEvents());
        this.component.addMouseWheelListener(mouseWheelEvent -> {
            if(mouseWheelEvent.getWheelRotation() < 0) {
                component.zoomIn();
            }
            else
                component.zoomOut();
        });
        this.component.getConnectionHandler().addListener(mxEvent.CONNECT, new mxEventSource.mxIEventListener() {
            public void invoke(Object sender, mxEventObject evt) {
                System.out.println("edge=" + evt.getProperties().toString());
            }
        });

//        MouseEvent event = new Mous;
//      this.component.getConnectionHandler().mousePressed(event);

        this.component.addListener(mxEvent.ADD_CELLS, new mxEventSource.mxIEventListener() {
            @Override
            public void invoke(Object o, mxEventObject mxEventObject) {
                System.out.println("cell - " + mxEventObject.getName() + " with properties: " + mxEventObject.getProperties());
            }
        });

    }

    public mxGraphComponent getComponent() {
        return component;
    }

}
