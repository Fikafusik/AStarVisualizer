import com.mxgraph.model.mxGeometry;

/**
 * Класс для представления координат точки на плоскости
 */
public class Point {
    /**
     * Абсцисса точки
     */
    double x;
    /**
     * Ордината точки
     */
    double y;

    /**
     * Конструктор точки по двум координатам.
     * @param x абсцисса.
     * @param y ордината.
     */
    public Point(double x, double y) {
        setX(x);
        setY(y);
    }

    /**
     * Конструктор точки по геометрии графического объекта.
     * Используются координаты центра объекта.
     * @param geometry геометрия графического объекта.
     */
    public Point(mxGeometry geometry) {
        setX(geometry.getCenterX());
        setY(geometry.getCenterY());
    }

    /**
     * Сеттер для абсциссы точки
     * @param x новая абсцисса
     */
    void setX(double x) {
        this.x = x;
    }

    /**
     * Геттер для абсциссы точки
     * @return абсцисса текущего экземпляра точки
     */
    double getX() {
        return (this.x);
    }

    /**
     * Сеттер для ординаты точки
     * @param y новая ордината
     */
    void setY(double y) {
        this.y = y;
    }

    /**
     * Геттер для ординаты точки
     * @return ордината текущего экземпляра точки
     */
    double getY() {
        return (this.y);
    }

}
