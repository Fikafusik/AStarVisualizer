/**
 * Класс, реализующий интерфейс эвристической функции.
 * используется для нахождения Чебышевского расстояния между двумя точками
 */
public class ChebyshevHeuristic implements IHeuristic {
    /**
     * Возвращает значение эвристической функции: max(|x - goal.x|, |y - goal.y|)
     * @param from первая точка
     * @param to вторая точка
     * @return Чебышевское расстояние между точками
     */
    public double getValue(Point from, Point to) {
        return (Math.max(Math.abs(from.getX() - to.getX()), Math.abs(from.getY() - to.getY())));
    }
}
