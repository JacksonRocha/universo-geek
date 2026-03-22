package br.com.algaworks.documentacao.repository;

import br.com.algaworks.documentacao.model.TransacaoFinanceira;
import br.com.algaworks.documentacao.model.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransacaoFinanceiraRepository extends JpaRepository<TransacaoFinanceira, Long> {
    List<TransacaoFinanceira> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT t FROM TransacaoFinanceira t WHERE t.usuario.id = :usuarioId AND t.dataVencimento >= :startDate AND t.dataVencimento <= :endDate")
    List<TransacaoFinanceira> findByUsuarioIdAndDataVencimentoBetween(
        @Param("usuarioId") Long usuarioId, 
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT t FROM TransacaoFinanceira t WHERE t.usuario.id = :usuarioId AND t.dataVencimento < :hoje AND t.status = 'PENDENTE'")
    List<TransacaoFinanceira> findVencidosByUsuarioId(@Param("usuarioId") Long usuarioId, @Param("hoje") LocalDate hoje);

    @Query("SELECT t FROM TransacaoFinanceira t WHERE t.usuario.id = :usuarioId AND t.dataVencimento = :hoje AND t.status = 'PENDENTE'")
    List<TransacaoFinanceira> findVencendoHojeByUsuarioId(@Param("usuarioId") Long usuarioId, @Param("hoje") LocalDate hoje);

}
