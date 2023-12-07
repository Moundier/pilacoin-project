package br.ufsm.csi.tapw.blueprints;

public interface Observer<T> {
    void update(T subject);
}
