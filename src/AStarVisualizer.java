import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.lang.Math.max;

public class AStarVisualizer implements IObservable{
    private mxGraphComponent graphComponent;
    private IObserver observer;
    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    private ListenableGraph<String, DefaultEdge> g;
    private static int inc = 1;
    private static int widthDefault = 40;
    private static final String styleDefault = "shape=ellipse";
    private static final String styleSource = "fillColor=lightgreen;shape=ellipse";
    private static final String styleSink = "fillColor=pink;shape=ellipse";
    private Object parent;
    private Object source;
    private Object sink;
    private MouseAdapter listenerEditVertex;
    private MouseAdapter listenerAddStartFinishVertex;
    private AStarAlgorithm aStarAlgorithm;
    private boolean editableStartFinish;

    public AStarAlgorithm setAlgorithm(AStarAlgorithm aStarAlgorithm){
        this.aStarAlgorithm = aStarAlgorithm;
        return this.aStarAlgorithm;
    }

    public AStarVisualizer() {
        editableStartFinish = true;
        g = new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));
        jgxAdapter = new JGraphXAdapter<>(g);

        source = null;
        sink = null;

        jgxAdapter.setCellsResizable(false);
        this.graphComponent = new mxGraphComponent(jgxAdapter);
        parent = jgxAdapter.getDefaultParent();

        this.graphComponent.setConnectable(true);
        this.graphComponent.setEventsEnabled(true);
        this.graphComponent.getViewport().setOpaque(true);
        jgxAdapter.setAllowDanglingEdges(false);
        graphComponent.getViewport().setOpaque(true);
        this.graphComponent.getViewport().setBackground(new Color(155, 208, 249));

        this.graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, (sender, evt) -> {

            Object cell = evt.getProperty("cell");
            setDefaultWeight(cell);
        });
    }

    private int setDefaultWeight(Object cell){
        Double x1 = ((mxCell)jgxAdapter.getModel().getTerminal(cell, true)).getGeometry().getCenterX();
        Double y1 = ((mxCell)jgxAdapter.getModel().getTerminal(cell, true)).getGeometry().getCenterY();
        Double x2 = ((mxCell)jgxAdapter.getModel().getTerminal(cell, false)).getGeometry().getCenterX();
        Double y2 = ((mxCell)jgxAdapter.getModel().getTerminal(cell, false)).getGeometry().getCenterY();

        jgxAdapter.getModel().setValue(cell, Integer.toString((int)Math.hypot(x1-x2,y1-y2)));
        return Integer.valueOf(jgxAdapter.getModel().getValue(cell).toString());
    }

    public mxGraph openGraph(String filePath) throws Exception {

            jgxAdapter.getModel().beginUpdate();
            Document document = mxXmlUtils.parseXml(mxUtils.readFile(filePath));
            mxCodec codec = new mxCodec(document);
            codec.decode(document.getDocumentElement(), jgxAdapter.getModel());

            this.graphComponent.setConnectable(true);
            this.graphComponent.setEventsEnabled(true);
            this.graphComponent.getViewport().setOpaque(true);
            jgxAdapter.setAllowDanglingEdges(false);

            jgxAdapter.setCellsResizable(false);
//            jgxAdapter.setCellsEditable(false);
            graphComponent.getViewport().setOpaque(true);
            jgxAdapter.getModel().endUpdate();
            source = null;
            aStarAlgorithm.setSource(source);
            sink = null;
            aStarAlgorithm.setSink(sink);
            inc = 1;
            for(Object vertex : graphComponent.getGraph().getChildVertices(jgxAdapter.getDefaultParent())) {
                inc = max(Integer.parseInt(jgxAdapter.getLabel(vertex)), inc);
                if (jgxAdapter.getModel().getStyle(vertex).equals(styleSource)) {
                    paintDefaultComponent(vertex);
                    source = vertex;
                    aStarAlgorithm.setSource(source);
                    paintStartComponent();
                }
                jgxAdapter.getModel().getGeometry(vertex).getCenterX();
                if (jgxAdapter.getModel().getStyle(vertex).equals(styleSink)) {
                    paintDefaultComponent(vertex);
                    sink = vertex;
                    aStarAlgorithm.setSink(sink);
                    paintFinishComponent();
                }
            }
            inc++;
            return graphComponent.getGraph();
    }

    public String saveGraph(String path) {
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
            return "ERROR : " + ex.getMessage();
        }
        return "";
    }

    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    private Object paintStartComponent() {
        if(source == null)
            return null;
        if(jgxAdapter.getModel().getGeometry(source).getWidth() != 0)
            paintComponent(source, "lightgreen");
        return null;
    }

    private Object paintFinishComponent() {
        if(sink == null)
            return null;
        if(jgxAdapter.getModel().getGeometry(sink).getWidth() != 0)
            paintComponent(sink, "pink");
        return sink;
    }

    public Object paintPath(Object object) {
        notifyObserver(new PaintComponent(object, "cornsilk"));
        return object;
    }

    private Object paintComponent(Object component, String color){
        notifyObserver(new PaintComponent(component,color));
        return component;
    }

    public class PaintComponent extends UndoableOperation{
        private String prevStyle;
        private String newColor;
        private Object component;

        public PaintComponent(Object component, String color) {
            this.component = component;
            prevStyle = jgxAdapter.getModel().getStyle(component);
            newColor = color;
        }

        @Override
        public void execute() {
            if(component == null)
                return;
            if(jgxAdapter.getModel().getGeometry(component).getWidth() != 0)
                jgxAdapter.getModel().setStyle(component, "fillColor=" + newColor + ";shape=ellipse");
            else
                jgxAdapter.getModel().setStyle(component,"strokeColor=" + newColor);
        }

        @Override
        public void undo() {
            jgxAdapter.getModel().setStyle(component, prevStyle);
        }
    }

    public Object paintPast(Object component) {
        paintComponent(component, "navy");
        return component;
    }

    public Object paintNow(Object component) {
        paintComponent(component, "blue");
        return component;
    }

    public Object paintFuture(Object component) {
        paintComponent(component, "aqua");
        return component;
    }

    public Object paintDefaultComponent(Object component){
        if(component == null)
            return null;
        if(jgxAdapter.getModel().getGeometry(component).getWidth() != 0)
            jgxAdapter.getModel().setStyle(component, styleDefault);
        else
            jgxAdapter.getModel().setStyle(component,"");
        return component;
    }

    public MouseListener[] setListenerEditVertex() {
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
                            if(Integer.parseInt(((mxCell)cell).getValue().toString()) == (inc - 1)) inc--;
                            jgxAdapter.getModel().remove(cell);
                            //System.out.println("cell delete =" + jgxAdapter.getLabel(cell));
                        }
                    }
                }
                super.mouseClicked(mouseEvent);
            }
        };
        this.graphComponent.getGraphControl().addMouseListener(listenerEditVertex);
        return this.graphComponent.getGraphControl().getMouseListeners();
    }

    public MouseListener[] removeListenerEditVertex() {
        this.graphComponent.getGraphControl().removeMouseListener(listenerEditVertex);
        return this.graphComponent.getGraphControl().getMouseListeners();
    }

    public MouseListener[] setListenerAddStartFinish() {
        this.jgxAdapter.setCellsMovable(false);
        if(!editableStartFinish){
            //Exception?
            return null;
        }
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
                            source = null;
                            notifyObserver(new SetSourceV(cell));
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
                            sink = null;
                            notifyObserver(new SetSinkV(cell));
                            aStarAlgorithm.setSink(sink);

//                            paintFinishComponent();
                        }
                    }
                }
                super.mouseClicked(mouseEvent);
            }
        };
        this.graphComponent.getGraphControl().addMouseListener(listenerAddStartFinishVertex);
        return this.graphComponent.getGraphControl().getMouseListeners();
    }

    public MouseListener[] removeListenerAddStartFinish() {
        this.jgxAdapter.setCellsMovable(true);
        this.graphComponent.getGraphControl().removeMouseListener(listenerAddStartFinishVertex);
        return this.graphComponent.getGraphControl().getMouseListeners();
    }

    public mxGraph clearGraph() {
        jgxAdapter.getModel().beginUpdate();
        //System.out.println(inc);
        inc = 1;
        jgxAdapter.removeCells(graphComponent.getGraph().getChildVertices(jgxAdapter.getDefaultParent()));
        graphComponent.getGraphControl().removeAll();
        jgxAdapter.getModel().endUpdate();
        return graphComponent.getGraph();
    }

    public Object setSink(Object vertex) {
        notifyObserver(new SetSinkV(vertex));
        return this.sink;
    }

    public Object setSource(Object vertex){
        notifyObserver(new SetSourceV(vertex));
        return this.source;
    }

    public boolean setStartFinishUneditable(){
        if(editableStartFinish && source != null && sink != null)
            notifyObserver(new SetStartFinishUneditable());
        return isStartFinishEditable();
    }

    public boolean isStartFinishEditable(){
        return editableStartFinish;
    }

    private class SetStartFinishUneditable extends UndoableOperation{

        @Override
        public void execute() {
            editableStartFinish = false;
        }

        @Override
        public void undo() {
            editableStartFinish = true;
        }
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
            jgxAdapter.getModel().setStyle(sink, styleSink);
        }

        @Override
        public void undo() {
            paintDefaultComponent(newSink);
            sink = this.oldSink;
            if(sink != null)
            jgxAdapter.getModel().setStyle(sink, styleSink);
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
            jgxAdapter.getModel().setStyle(source, styleSource);
        }

        @Override
        public void undo(){
            paintDefaultComponent(newSource);
            source = this.oldSource;
            if(source != null)
            jgxAdapter.getModel().setStyle(source, styleSource);
        }
    }

    @Override
    public IObserver addObserver(IObserver observer){
        this.observer = observer;
        return this.observer;
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

