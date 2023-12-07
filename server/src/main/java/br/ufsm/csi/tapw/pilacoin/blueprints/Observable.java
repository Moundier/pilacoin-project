package br.ufsm.csi.tapw.pilacoin.blueprints;

public interface Observable<T> {
    void subscribe(Observer<T> observer);
    void unsubscribe(Observer<T> observer);
}
