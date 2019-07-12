/**
 * Интерфейс семества эвристических функций
 */
interface IHeuristic {
    /**
     * Возвращает значение эвристической функции
     * @param from первая точка
     * @param to вторая точка
     * @return эвристическая оценка для двух точек
     */
    double getValue(Point from, Point to);
}
