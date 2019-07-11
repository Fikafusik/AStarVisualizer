import java.util.Stack;

public class OperationHistory implements IObserver, IUndoable {
    private java.util.Stack<UndoableOperation> history;

    OperationHistory() {
        history = new Stack<>();
    }

    private void push(UndoableOperation operation){
        //System.out.println("Pushing operation " + operation.toString());
        history.push(operation);
    }

    private UndoableOperation pop(){
        //System.out.println("Popping operation " + history.peek().toString());
        return history.pop();
    }

    public void undo(){
        if (history.empty())
            return;
        history.peek().undo();
        pop();
    }

    public void handleEvent(UndoableOperation operation){
        operation.execute();
        push(operation);
    }

    public void stepBack() {
        while ((!history.empty()) && !(history.peek() instanceof AStarAlgorithm.NextStep) )
            undo();
        undo();
        while ((!history.empty()) && !(history.peek() instanceof AStarAlgorithm.NextStep) )
            undo();
    }

    public void reset(){
        while (!history.empty()){
            undo();
        }
    }
}
