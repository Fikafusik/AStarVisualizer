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

    public void stepBack(){
        if(history.peek() instanceof AStarAlgorithm.NextStep)
            undo();
        while(!(history.peek() instanceof AStarAlgorithm.NextStep)){
            undo();
        }
    }

    public void reset(){
        while(!history.empty()){
            undo();
        }
    }
}
