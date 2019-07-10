import com.mxgraph.model.mxCell;

import java.io.Serializable;

public class Vertex implements Serializable {
    private String name;
    private Point point;

    public Vertex(Point point, String name){
        setPoint(point);
        setName(name);
    }

    public void setPoint(Point point){
        this.point = point;
    }
    public Point getPoint(){
        return point;
    }
    public void setName(String name){
        this.name = name;
    }
    @Override
    public String toString(){
        return name;
    }
}
