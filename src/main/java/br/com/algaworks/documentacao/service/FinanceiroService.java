package br.com.algaworks.documentacao.service;

import br.com.algaworks.documentacao.dto.CategoriaResumoDTO;
import br.com.algaworks.documentacao.dto.ContaResumoDTO;
import br.com.algaworks.documentacao.dto.ResumoFinanceiroDTO;
import br.com.algaworks.documentacao.model.*;
import br.com.algaworks.documentacao.repository.InstituicaoFinanceiraRepository;
import br.com.algaworks.documentacao.repository.TransacaoFinanceiraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceiroService {

    @Autowired
    private TransacaoFinanceiraRepository transacaoRepository;

    @Autowired
    private InstituicaoFinanceiraRepository instituicaoRepository;

    public ResumoFinanceiroDTO obterResumoFinanceiro(Long usuarioId) {
        ResumoFinanceiroDTO resumo = new ResumoFinanceiroDTO();
        LocalDate hoje = LocalDate.now();
        YearMonth mesAtual = YearMonth.now();

        // Inicializando valores com zero
        resumo.setSaldoAtual(BigDecimal.ZERO);
        resumo.setReceitasMes(BigDecimal.ZERO);
        resumo.setDespesasMes(BigDecimal.ZERO);
        resumo.setReceitasVencidas(BigDecimal.ZERO);
        resumo.setDespesasVencidas(BigDecimal.ZERO);
        resumo.setReceitasVencendoHoje(BigDecimal.ZERO);
        resumo.setDespesasVencendoHoje(BigDecimal.ZERO);
        resumo.setReceitasFuturas(BigDecimal.ZERO);
        resumo.setDespesasFuturas(BigDecimal.ZERO);

        List<TransacaoFinanceira> transacoes = transacaoRepository.findByUsuarioId(usuarioId);

        BigDecimal totalDespesasMesChart = BigDecimal.ZERO;
        Map<CategoriaTransacao, BigDecimal> despesasPorCategoria = new HashMap<>();

        for (TransacaoFinanceira t : transacoes) {
            boolean isMesAtual = YearMonth.from(t.getDataVencimento()).equals(mesAtual);
            boolean isHoje = t.getDataVencimento().equals(hoje);
            boolean isVencida = t.getDataVencimento().isBefore(hoje) && t.getStatus() != StatusTransacao.PAGO;
            boolean isFutura = t.getDataVencimento().isAfter(hoje.plusDays(14)); // Exemplo: futuro é além de 14 dias

            if (t.getTipo() == TipoTransacao.RECEITA) {
                if (isMesAtual) {
                    resumo.getReceitasMesList().add(t);
                    resumo.setReceitasMes(resumo.getReceitasMes().add(t.getValor()));
                }
                if (isVencida) resumo.setReceitasVencidas(resumo.getReceitasVencidas().add(t.getValor()));
                if (isHoje && t.getStatus() != StatusTransacao.PAGO) resumo.setReceitasVencendoHoje(resumo.getReceitasVencendoHoje().add(t.getValor()));
                if (t.getDataVencimento().isAfter(hoje) && t.getDataVencimento().isBefore(hoje.plusDays(15))) {
                    resumo.setReceitasFuturas(resumo.getReceitasFuturas().add(t.getValor()));
                }
            } else if (t.getTipo() == TipoTransacao.DESPESA) {
                if (isMesAtual) {
                    resumo.getDespesasMesList().add(t);
                    resumo.setDespesasMes(resumo.getDespesasMes().add(t.getValor()));
                    totalDespesasMesChart = totalDespesasMesChart.add(t.getValor());
                    despesasPorCategoria.put(t.getCategoria(), despesasPorCategoria.getOrDefault(t.getCategoria(), BigDecimal.ZERO).add(t.getValor()));
                }
                if (isVencida) resumo.setDespesasVencidas(resumo.getDespesasVencidas().add(t.getValor()));
                if (isHoje && t.getStatus() != StatusTransacao.PAGO) resumo.setDespesasVencendoHoje(resumo.getDespesasVencendoHoje().add(t.getValor()));
                if (t.getDataVencimento().isAfter(hoje) && t.getDataVencimento().isBefore(hoje.plusDays(15))) {
                    resumo.setDespesasFuturas(resumo.getDespesasFuturas().add(t.getValor()));
                }
            }
        }

        // Saldos das Contas
        List<InstituicaoFinanceira> contas = instituicaoRepository.findByUsuarioId(usuarioId);
        List<ContaResumoDTO> saldosContas = new ArrayList<>();
        BigDecimal saldoTotal = BigDecimal.ZERO;
        
        for (InstituicaoFinanceira conta : contas) {
            saldoTotal = saldoTotal.add(conta.getSaldoAtual());
            saldosContas.add(new ContaResumoDTO(
                    conta.getNome(), 
                    conta.getTipo().name(), 
                    conta.getSaldoAtual(), 
                    null
            ));
        }
        resumo.setSaldoAtual(saldoTotal);
        resumo.setSaldosContas(saldosContas);

        // Categorias Despesas Chart
        List<CategoriaResumoDTO> categorias = new ArrayList<>();
        if (totalDespesasMesChart.compareTo(BigDecimal.ZERO) > 0) {
            for (Map.Entry<CategoriaTransacao, BigDecimal> entry : despesasPorCategoria.entrySet()) {
                BigDecimal total = entry.getValue();
                BigDecimal percentual = total.multiply(new BigDecimal("100")).divide(totalDespesasMesChart, 1, RoundingMode.HALF_UP);
                String cor = entry.getKey().getCor();
                if (cor == null || cor.isEmpty()) cor = "#000000"; // fallback
                categorias.add(new CategoriaResumoDTO(entry.getKey().getNome(), total, percentual, cor));
            }
        }
        
        // Ordenar categorias por percentual desc
        categorias.sort((c1, c2) -> c2.getPercentual().compareTo(c1.getPercentual()));
        resumo.setCategoriasDespesas(categorias);

        return resumo;
    }
}
