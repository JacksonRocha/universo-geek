package br.com.algaworks.documentacao.repository;

import br.com.algaworks.documentacao.model.DividaTerceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DividaTerceiroRepository extends JpaRepository<DividaTerceiro, Long> {
    List<DividaTerceiro> findByUsuarioId(Long usuarioId);
}
