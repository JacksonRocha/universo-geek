package br.com.algaworks.documentacao.repository;

import br.com.algaworks.documentacao.model.CategoriaTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaTransacaoRepository extends JpaRepository<CategoriaTransacao, Long> {
    List<CategoriaTransacao> findByUsuarioId(Long usuarioId);
}
