/**
 * Интерфейс наблюдателя
 */
interface IObserver {
    /**
     * Обработка поступающих операций
     * @param operation отменяемая операция
     */
    void handleEvent(UndoableOperation operation);
}
