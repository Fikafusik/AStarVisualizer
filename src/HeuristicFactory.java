
import java.util.HashMap;

/**
 * Помесь Фабричного Метода и Абстрактной Фабрики.
 * Позволяет получать необходимый экземпляра класса эвристической функции по имени,
 * а также переиспользует ранее созданные объекты, что позволяет значительно сократить
 * затраты по памяти в ряде случаев работы программы
 */
public class HeuristicFactory {
    /**
     * Ассоциативный контейнер для хранения соответствия названия эвристической функции
     * и экземпляра класса эвристической функции
     */
    private HashMap<String, IHeuristic> factory;

    HeuristicFactory() {
        factory = new HashMap<>();
    }

    /**
     * Основной метод класса. Возвращает экземпляр фристической функции по её названию
     * @param heuristicName название требуемой эвристической функции
     * @return экземпляр соответствующего названию класса эвристических функций
     */
    IHeuristic getHeuristic(String heuristicName) {
        if (factory.containsKey(heuristicName)) {
            return factory.get(heuristicName);
        }

        switch (heuristicName) {
            case "Manhattan":
                factory.put(heuristicName, new ManhattanHeuristic());
                break;
            case "Chebyshev":
                factory.put(heuristicName, new ChebyshevHeuristic());
                break;
            case "Euclidean":
                factory.put(heuristicName, new EuclideanHeuristic());
                break;
            default:
                // TODO: throw exception
        }

        return factory.get(heuristicName);
    }
}
