import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.sun.jdi.DoubleValue;
import javafx.scene.effect.Light;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.max;

public class AStarVisualizer implements IObservable{
    private mxGraphComponent graphComponent;
    private IObserver observer;
    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    private ListenableGraph<String, DefaultEdge> g;
    private static int inc = 1;
    private static int widthDefault = 40;
    private static String styleDefault = "shape=ellipse";
    private static String styleSource = "fillColor=lightgreen;shape=ellipse";
    private static String styleSink = "fillColor=pink;shape=ellipse";
    private Object parent;
    private Object source;
    private Object sink;
    private MouseAdapter listenerEditVertex;
    private MouseAdapter listenerAddStartFinishVertex;
    private AStarAlgorithm aStarAlgorithm;

    public void setAlgorithm(AStarAlgorithm aStarAlgorithm){
        this.aStarAlgorithm = aStarAlgorithm;
    }

    public AStarVisualizer() {

        g = new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));
        jgxAdapter = new JGraphXAdapter<>(g);

        jgxAdapter.setCellsResizable(false);
        this.graphComponent = new mxGraphComponent(jgxAdapter);
        parent = jgxAdapter.getDefaultParent();

        this.graphComponent.setConnectable(true);
        this.graphComponent.setEventsEnabled(true);
        this.graphComponent.getViewport().setOpaque(true);
        jgxAdapter.setAllowDanglingEdges(false);
//        jgxAdapter.setCellsEditable(false);
        graphComponent.getViewport().setOpaque(true);
//        jgxAdapter.setDefaultLoopStyle();
//        jgxAdapter.cellLabelChanged();
        this.graphComponent.getViewport().setBackground(new Color(155, 208, 249));
        this.graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, (sender, evt) -> {

            Object cell = evt.getProperty("cell");
            setDefaultWeight(cell);
        });
/*        this.graphComponent.getConnectionHandler().addListener(mxEvent.LABEL_CHANGED, new mxEventSource.mxIEventListener() {
            @Override
            public void invoke(Object o, mxEventObject mxEventObject) {
                System.out.println("HEL");
                Object cell = mxEventObject.getProperty("cell");
                if(jgxAdapter.getModel().getValue(cell).equals(""))
                    setDefaultWeight(cell);
            }
        });
*/    }

    private void setDefaultWeight(Object cell){
        Double x1 = ((mxCell)jgxAdapter.getModel().getTerminal(cell, true)).getGeometry().getCenterX();
        Double y1 = ((mxCell)jgxAdapter.getModel().getTerminal(cell, true)).getGeometry().getCenterY();
        Double x2 = ((mxCell)jgxAdapter.getModel().getTerminal(cell, false)).getGeometry().getCenterX();
        Double y2 = ((mxCell)jgxAdapter.getModel().getTerminal(cell, false)).getGeometry().getCenterY();

        jgxAdapter.getModel().setValue(cell, Integer.toString((int)Math.hypot(x1-x2,y1-y2)));
    }
//        this.graphComponent.addListener(mxEvent.ADD_CELLS, (o, mxEventObject) -> System.out.println("cell - " + mxEventObject.getName() + " with properties: " + mxEventObject.getProperties()));


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
//            jgxAdapter.setCellsEditable(false);
            graphComponent.getViewport().setOpaque(true);
            jgxAdapter.getModel().endUpdate();
            source = null;
            aStarAlgorithm.setSource(source);
            sink = null;
            aStarAlgorithm.setSink(sink);
            inc = 1;
            for(Object vertex : graphComponent.getGraph().getChildVertices(jgxAdapter.getDefaultParent())) {
                inc = max(Integer.parseInt(jgxAdapter.getLabel(vertex).substring(1)), inc);
                if (jgxAdapter.getModel().getStyle(vertex).equals(styleSource)) {
                    source = vertex;
                    aStarAlgorithm.setSource(source);
                }
                jgxAdapter.getModel().getGeometry(vertex).getCenterX();
                if (jgxAdapter.getModel().getStyle(vertex).equals(styleSink)) {
                    sink = vertex;
                    aStarAlgorithm.setSink(sink);
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
        if(source == null)
            return;
        if(jgxAdapter.getModel().getGeometry(source).getWidth() != 0)
            jgxAdapter.getModel().setStyle(source, styleSource);
    }

    private void paintFinishComponent() {
        if(sink == null)
            return;
        if(jgxAdapter.getModel().getGeometry(sink).getWidth() != 0)
            jgxAdapter.getModel().setStyle(sink, styleSink);
    }

    public void paintComponent(Object component, String color){
        if(component == null)
            return;
        if(jgxAdapter.getModel().getGeometry(component).getWidth() != 0)
            jgxAdapter.getModel().setStyle(component, "fillColor="+ color +";shape=ellipse");
        else
            jgxAdapter.getModel().setStyle(component,"strokeColor=" + color);
    }

    public void paintPast(Object component) {
        paintComponent(component, "navy");
    }

    public void paintNow(Object component) {
        paintComponent(component, "blue");
    }

    public void paintFuture(Object component) {
        paintComponent(component, "aqua");
    }

    public void paintDefaultComponent(Object component){
        if(component == null)
            return;
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
                            jgxAdapter.insertVertex(jgxAdapter.getDefaultParent(), null, inc++, mouseEvent.getX() - widthDefault / 2, mouseEvent.getY() - widthDefault / 2, widthDefault, widthDefault, styleDefault);
                        }
                }
                if (mouseEvent.getClickCount() == 1) {
                    if (mouseEvent.getButton() == mouseEvent.BUTTON3) {
                        if (cell != null) {
                            for(Object child : graphComponent.getGraph().getEdges(cell))
                                jgxAdapter.getModel().remove(child);
                            if(cell == source) {
                                source = null;
                                aStarAlgorithm.setSource(source);
                            }
                            if(cell == sink) {
                                sink = null;
                                aStarAlgorithm.setSink(sink);
                            }
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
                            if (cell == sink) {
                                sink = null;
                                aStarAlgorithm.setSink(sink);
                            }
                            if (source != null)
                                jgxAdapter.getModel().setStyle(source, styleDefault);

                            notifyObserver(new SetSourceV(cell));
                            //source = cell;
                            aStarAlgorithm.setSource(source);
//                            paintStartComponent();
                        }
                        if (mouseEvent.getButton() == mouseEvent.BUTTON3) {
                            if(cell == source) {
                                source = null;
                                aStarAlgorithm.setSource(source);
                            }
                            if (sink != null)
                                jgxAdapter.getModel().setStyle(sink, styleDefault);
                            notifyObserver(new SetSinkV(cell));
                            //sink = cell;
                            aStarAlgorithm.setSink(sink);

//                            paintFinishComponent();
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
        source = null;
        aStarAlgorithm.setSource(source);
        sink = null;
        aStarAlgorithm.setSink(sink);
    }


    void setSink(Object vertex) {
        notifyObserver(new SetSinkV(vertex));
    }

    public class SetSinkV extends UndoableOperation {

        private Object oldSink;
        private Object newSink;

        SetSinkV(Object vertex) {
            this.oldSink = sink;
            this.newSink = vertex;
        }

        @Override
        public void execute() {
            paintDefaultComponent(oldSink);
            sink = this.newSink;
            paintFinishComponent();
        }

        @Override
        public void undo() {
            paintDefaultComponent(newSink);
            sink = this.oldSink;
            paintFinishComponent();
        }
    }


    public class SetSourceV extends UndoableOperation {

        private Object oldSource;
        private Object newSource;

        SetSourceV(Object vertex) {
            this.oldSource = source;
            this.newSource = vertex;
        }

        @Override
        public void execute(){
            paintDefaultComponent(oldSource);
            source = this.newSource;
            paintStartComponent();
        }

        @Override
        public void undo(){
            paintDefaultComponent(newSource);
            source = this.oldSource;
            paintStartComponent();
        }
    }

    @Override
    public void addObserver(IObserver observer){
        this.observer = observer;
    }

    @Override
    public void removeObserver(IObserver observer){
        this.observer = null;
    }

    @Override
    public void notifyObserver(UndoableOperation operation){
        if(operation == null) System.out.println("NULLL OPERATION");
        this.observer.handleEvent(operation);
    }
}

