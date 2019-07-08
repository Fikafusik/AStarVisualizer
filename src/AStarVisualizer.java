import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxStylesheet;
import javafx.scene.control.DialogEvent;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

public class AStarVisualizer {
    private mxGraphComponent graphComponent;

    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    private ListenableGraph<String, DefaultEdge> g;
    private static int inc = 1;
    private static int widthDefault = 40;
    private static String styleDefault = "shape=ellipse";
    Object parent;
    MouseAdapter addDelVertex;

    public AStarVisualizer() {
        g = new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));
        jgxAdapter = new JGraphXAdapter<>(g);

        this.graphComponent = new mxGraphComponent(jgxAdapter);
        parent = jgxAdapter.getDefaultParent();

        this.graphComponent.setConnectable(true);
        this.graphComponent.setEventsEnabled(true);
//        jgxAdapter.insertVertex(parent, null, "v" + inc++, 20, 20, widthDefault, widthDefault, styleDefault);
//        jgxAdapter.insertVertex(parent, null, "v" + inc++, 60, 60, widthDefault, widthDefault, styleDefault);
        jgxAdapter.setAllowDanglingEdges(false);
        jgxAdapter.setCellsEditable(false);
        graphComponent.getViewport().setOpaque(true);
        this.graphComponent.getViewport().setOpaque(true);

/*        PopupMenu popupMenu = new PopupMenu("Select changes");
        popupMenu.add(new MenuItem());
        this.graphComponent.add(popupMenu);
*/        addDelVertex = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Object cell = graphComponent.getCellAt(mouseEvent.getX(), mouseEvent.getY());
                if (mouseEvent.getClickCount() == 2) {
                    if(mouseEvent.getButton() == mouseEvent.BUTTON1)
                        jgxAdapter.insertVertex(parent, null, "v" + inc++, mouseEvent.getX() - widthDefault/2, mouseEvent.getY() - widthDefault/2, widthDefault, widthDefault , styleDefault);

                    super.mouseClicked(mouseEvent);
                }
                if (mouseEvent.getClickCount() == 1) {
                    if (mouseEvent.getButton() == mouseEvent.BUTTON3) {
/*                        if (cell != null) {
                            for(Object child : graphComponent.getGraph().getEdges(cell))
                                jgxAdapter.getModel().remove(child);
                            jgxAdapter.getModel().remove(cell);
                            System.out.println("cell delete =" + jgxAdapter.getLabel(cell));
                        }
*/                    }
                    if(mouseEvent.getButton() == mouseEvent.BUTTON3 && cell != null) {
                        if(jgxAdapter.getModel().getGeometry(cell).getWidth() != 0)
                            jgxAdapter.getModel().setStyle(cell, "fillColor=red;shape=ellipse");
                        else
                            jgxAdapter.getModel().setStyle(cell, "strokeColor=orange");
                    } //В разработке
                }
                super.mouseClicked(mouseEvent);
            }
        };

        this.graphComponent.getGraphControl().addMouseListener(addDelVertex);

        this.graphComponent.getViewport().setBackground(new Color(155, 208, 249));


//        this.graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, (sender, evt) -> System.out.println("edge=" + evt.getProperties().toString()));

        //this.graphComponent.addListener(mxEvent.ADD_CELLS, (o, mxEventObject) -> System.out.println("cell - " + mxEventObject.getName() + " with properties: " + mxEventObject.getProperties()));
    }

    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    public void paintComponent(Object component){

        if(jgxAdapter.getModel().getGeometry(component).getWidth() != 0)
            jgxAdapter.getModel().setStyle(component, "fillColor=red;shape=ellipse");
        else
            jgxAdapter.getModel().setStyle(component,"strokeColor=orange");
        jgxAdapter.getModel().beginUpdate();
    }
}
