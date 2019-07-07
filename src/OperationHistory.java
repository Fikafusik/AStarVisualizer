public class OperationHistory implements IObserver, IUndoable {
    private java.util.Stack<UndoableOperation> history;

    private void push(UndoableOperation operation){
        history.push(operation);
    }

    private void pop(){
        history.pop();
    }

    public void undo(){
        history.peek().undo();
        pop();
    }

    public void handleEvent(UndoableOperation operation){
        push(operation);
        operation.execute();
    }

}
