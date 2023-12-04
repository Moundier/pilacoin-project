package br.ufsm.csi.tapw.pilacoin.types;

public interface Observable<T> {
    void subscribe(Observer<T> observer);
    void unsubscribe(Observer<T> observer);
}
