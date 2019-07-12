/**
 * Класс, реализующий интерфейс эвристической функции.
 * используется для нахождения Евклидова расстояния между двумя точками
 */
public class EuclideanHeuristic implements IHeuristic {
    /**
     * Возвращает значение эвристической функции: sqrt((x - goal.x)^2, (y - goal.y)^2)
     * @param from первая точка
     * @param to вторая точка
     * @return Евклидово расстояние между точками
     */
    public double getValue(Point from, Point to) {
        return Math.hypot(from.getX() - to.getX(), from.getY() - to.getY());
    }
}
