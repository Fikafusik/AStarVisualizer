import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.max;

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

        this.graphComponent.getViewport().setBackground(new Color(155, 208, 249));

//        this.graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, (sender, evt) -> System.out.println("edge=" + evt.getProperties().toString()));

//        this.graphComponent.addListener(mxEvent.ADD_CELLS, (o, mxEventObject) -> System.out.println("cell - " + mxEventObject.getName() + " with properties: " + mxEventObject.getProperties()));
    }

    public void openGraph(String filePath) {
        try
        {
            jgxAdapter.getModel().beginUpdate();
            Document document = mxXmlUtils.parseXml(mxUtils.readFile(filePath));
            mxCodec codec = new mxCodec(document);
            codec.decode(document.getDocumentElement(), jgxAdapter.getModel());

            this.graphComponent.setConnectable(true);
            this.graphComponent.setEventsEnabled(true);
            this.graphComponent.getViewport().setOpaque(true);
            jgxAdapter.setAllowDanglingEdges(false);
            jgxAdapter.setCellsEditable(false);
            graphComponent.getViewport().setOpaque(true);
            jgxAdapter.getModel().endUpdate();
            start = null;
            finish = null;
            inc = 1;
            for(Object vertex : graphComponent.getGraph().getChildVertices(jgxAdapter.getDefaultParent())) {
                inc = max(Integer.parseInt(jgxAdapter.getLabel(vertex).substring(1)), inc);
                if (jgxAdapter.getModel().getStyle(vertex).equals("fillColor=lightgreen;shape=ellipse")) {
                    start = vertex;
                }
                if (jgxAdapter.getModel().getStyle(vertex).equals("fillColor=lightyellow;shape=ellipse")) {
                    finish = vertex;
                }
            }
            inc++;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void saveGraph(String path) {
        try {
            System.out.println("call xml getting code");
            mxCodec codec = new mxCodec();
            String xml = mxUtils.getXml(codec.encode(jgxAdapter.getModel()));
            if(!path.endsWith(".xml"))
                path += ".xml";
            java.io.FileWriter fw = new java.io.FileWriter(path);
            fw.write(xml);
            fw.close();
        }
        catch(Exception ex) {
            System.out.println("ERROR : "+ex.getMessage());
        }
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
            jgxAdapter.getModel().setStyle(finish, "fillColor=lightyellow;shape=ellipse");
    }

    public void paintComponent(Object component, String color){
        if(jgxAdapter.getModel().getGeometry(component).getWidth() != 0)
            jgxAdapter.getModel().setStyle(component, "fillColor="+ color +";shape=ellipse");
        else
            jgxAdapter.getModel().setStyle(component,"strokeColor=" + color);
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
                        if (cell == null) {
                            jgxAdapter.insertVertex(jgxAdapter.getDefaultParent(), null, "v" + inc++, mouseEvent.getX() - widthDefault / 2, mouseEvent.getY() - widthDefault / 2, widthDefault, widthDefault, styleDefault);
                        }
                }
                if (mouseEvent.getClickCount() == 1) {
                    if (mouseEvent.getButton() == mouseEvent.BUTTON3) {
                        if (cell != null) {
                            for(Object child : graphComponent.getGraph().getEdges(cell))
                                jgxAdapter.getModel().remove(child);
                            if(cell == start)
                                start = null;
                            if(cell == finish)
                                finish = null;
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
                        if (mouseEvent.getButton() == mouseEvent.BUTTON1) {
                            if (cell == finish)
                                finish = null;
                            if (start != null)
                                jgxAdapter.getModel().setStyle(start, styleDefault);
                            start = cell;
                            paintStartComponent();
                        }
                        if (mouseEvent.getButton() == mouseEvent.BUTTON3) {
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

    public void removeListenerAddStartFinish() {
        this.jgxAdapter.setCellsMovable(true);
        this.graphComponent.getGraphControl().removeMouseListener(listenerAddStartFinishVertex);
    }
    public void clearGraph() {
        jgxAdapter.getModel().beginUpdate();
        System.out.println(inc);
        jgxAdapter.removeCells(graphComponent.getGraph().getChildVertices(jgxAdapter.getDefaultParent()));
        graphComponent.getGraphControl().removeAll();
        jgxAdapter.getModel().endUpdate();
        start = null;
        finish = null;
    }
}
