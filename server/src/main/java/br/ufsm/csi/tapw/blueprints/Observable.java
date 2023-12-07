package br.ufsm.csi.tapw.blueprints;

public interface Observable<T> {
    void subscribe(Observer<T> observer);
    void unsubscribe(Observer<T> observer);
}
