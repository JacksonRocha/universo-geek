package br.com.algaworks.documentacao.repository;

import br.com.algaworks.documentacao.model.InstituicaoFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituicaoFinanceiraRepository extends JpaRepository<InstituicaoFinanceira, Long> {
    List<InstituicaoFinanceira> findByUsuarioId(Long usuarioId);
}
