
interface IObservable {
    IObserver addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObserver(UndoableOperation operation);
}
