package br.ufsm.csi.tapw.pilacoin.types;

public interface Observer<T> {
    void update(T subject);
}
