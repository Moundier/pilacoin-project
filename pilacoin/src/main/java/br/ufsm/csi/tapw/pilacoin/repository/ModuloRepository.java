package br.ufsm.csi.tapw.pilacoin.repository;

import br.ufsm.csi.tapw.pilacoin.model.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Long> {
    Modulo findOneByNome(String nome);
}
