package br.com.algaworks.documentacao.repository;

import br.com.algaworks.documentacao.model.TarefaTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarefaTrabalhoRepository extends JpaRepository<TarefaTrabalho, Long> {
    List<TarefaTrabalho> findByUsuarioId(Long usuarioId);
}
