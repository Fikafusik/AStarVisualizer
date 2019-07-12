import java.util.Stack;

/**
 * Класс, позволяющий хранить цепочку действий, обрабатывать поступающие действия и
 * отменять ранее выполненные операции
 */
public class OperationHistory implements IObserver, IUndoable {
    /**
     * Стек для хранения истории полученных операций
     */
    private java.util.Stack<UndoableOperation> history;

    OperationHistory() {
        history = new Stack<>();
    }

    /**
     * Добавление в стек новой операции
     * @param operation любая отменяемая операции
     */
    private void push(UndoableOperation operation){
        System.out.println("Pushing operation " + operation.toString());
        history.push(operation);
    }

    /**
     * Удаление из стека последней добавленной операции
     * @return
     */
    private UndoableOperation pop(){
        System.out.println("Popping operation " + history.peek().toString());
        return history.pop();
    }

    /**
     * Отмена последнего действия
     */
    public void undo(){
        if (history.empty())
            return;
        history.peek().undo();
        pop();
    }

    /**
     * Обработка поступающей операции: её выполнение и сохранение в стек
     * @param operation отменяемая операция
     */
    public void handleEvent(UndoableOperation operation){
        operation.execute();
        push(operation);
    }

    /**
     * Костыль для того, чтобы можно было отменять шаг работы алгоритма A*
     */
    public void stepBack() {
        while ((!history.empty()) && !(history.peek() instanceof AStarAlgorithm.NextStep) )
            undo();
        undo();
        while ((!history.empty()) && !(history.peek() instanceof AStarAlgorithm.NextStep) )
            undo();
    }

    /**
     * Отмена всех операций, выполненных ранее
     */
    public void reset(){
        while (!history.empty()){
            undo();
        }
    }
}
