/**
 * Класс, реализующий интерфейс эвристической функции.
 * используется для нахождения Манхэттенского расстояния между двумя точками
 */
public class ManhattanHeuristic implements IHeuristic {
    /**
     * Возвращает значение эвристической функции: |x - goal.x| + |y - goal.y|
     * @param from первая точка
     * @param to вторая точка
     * @return Манхэттенское расстояние между точками
     */
    public double getValue(Point from, Point to) {
        return (Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY()));
    }
}
