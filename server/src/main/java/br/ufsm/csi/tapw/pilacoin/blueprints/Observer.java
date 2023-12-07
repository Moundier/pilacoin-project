package br.ufsm.csi.tapw.pilacoin.blueprints;

public interface Observer<T> {
    void update(T subject);
}
