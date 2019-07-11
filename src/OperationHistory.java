import java.util.Stack;

public class OperationHistory implements IObserver, IUndoable {
    private java.util.Stack<UndoableOperation> history;

    OperationHistory() {
        history = new Stack<>();
    }

    private void push(UndoableOperation operation){
        history.push(operation);
    }

    private void pop(){
        history.pop();
    }

    public void undo(){
        if (history.empty())
            return;
        history.peek().undo();
        pop();
    }

    public void handleEvent(UndoableOperation operation){
        System.out.println("Pushing operation " + operation.toString());
        push(operation);
        operation.execute();
    }

    public void stepBack() {
        while ((!history.empty()) && !(history.peek() instanceof AStarAlgorithm.NextStep) )
            undo();
        undo();
        /*
        while(!(history.peek() instanceof AStarAlgorithm.NextStep)){
            undo();
        }
     */
    }

    public void reset(){
        while (!history.empty()){
            undo();
        }
    }
}
