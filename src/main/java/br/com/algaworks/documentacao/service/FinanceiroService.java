package br.com.algaworks.documentacao.service;

import br.com.algaworks.documentacao.dto.*;
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

    @Autowired
    private br.com.algaworks.documentacao.repository.DividaTerceiroRepository dividaTerceiroRepository;

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
            boolean isCartao = t.getInstituicao() != null && t.getInstituicao().getTipo() == TipoInstituicao.CARTAO_CREDITO;
            
            // Regra de Data: Se for Cartão, usa a regra da Fatura (dia 3). Caso contrário, usa Mês do Calendário.
            boolean isNoCiclo = isCartao ? pertenceAFatura(t.getDataVencimento(), mesAtual) : YearMonth.from(t.getDataVencimento()).equals(mesAtual);
            
            boolean isHoje = t.getDataVencimento().equals(hoje);
            boolean isVencida = t.getDataVencimento().isBefore(hoje) && t.getStatus() != StatusTransacao.PAGO;

            if (t.getTipo() == TipoTransacao.RECEITA) {
                if (isNoCiclo) {
                    resumo.getReceitasMesList().add(t);
                    resumo.setReceitasMes(resumo.getReceitasMes().add(t.getValor()));
                }
                if (isVencida) resumo.setReceitasVencidas(resumo.getReceitasVencidas().add(t.getValor()));
                if (isHoje && t.getStatus() != StatusTransacao.PAGO) resumo.setReceitasVencendoHoje(resumo.getReceitasVencendoHoje().add(t.getValor()));
                if (t.getDataVencimento().isAfter(hoje) && t.getDataVencimento().isBefore(hoje.plusDays(15))) {
                    resumo.setReceitasFuturas(resumo.getReceitasFuturas().add(t.getValor()));
                }
            } else if (t.getTipo() == TipoTransacao.DESPESA) {
                if (isNoCiclo) {
                    resumo.getDespesasMesList().add(t);
                    resumo.setDespesasMes(resumo.getDespesasMes().add(t.getValor()));
                    
                    // Categorização: Imediato vs Cartão de Crédito
                    if (isCartao) {
                        resumo.getDespesasCartaoMesList().add(t);
                        resumo.setTotalDespesasCartao(resumo.getTotalDespesasCartao().add(t.getValor()));
                    } else {
                        resumo.getDespesasImediatasMesList().add(t);
                        resumo.setTotalDespesasImediatas(resumo.getTotalDespesasImediatas().add(t.getValor()));
                    }
                    
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
                    conta.getId(),
                    conta.getNome(), 
                    conta.getTipo().name(), 
                    conta.getSaldoAtual(), 
                    null
            ));
        }
        // O saldo atual é o que sobra das Receitas menos as Despesas (Imediatas + Cartão)
        BigDecimal saldoCalculado = resumo.getReceitasMes().subtract(resumo.getTotalDespesasImediatas().add(resumo.getTotalDespesasCartao()));
        resumo.setSaldoAtual(saldoCalculado);
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

        // Despesas de Terceiros - Agrupadas por Devedor
        List<DividaTerceiro> todasDividas = dividaTerceiroRepository.findByUsuarioId(usuarioId);
        Map<String, DevedorResumoDTO> devedoresMap = new java.util.LinkedHashMap<>();
        
        for (DividaTerceiro d : todasDividas) {
            String nomeDevedor = d.getDevedor();
            devedoresMap.putIfAbsent(nomeDevedor, new DevedorResumoDTO(nomeDevedor));
            devedoresMap.get(nomeDevedor).getDividas().add(d);
            
            // Logic: Include third-party debts in expenses if they were registered on a card/account
            // But user says: "deve entrar no saldo devedor quando eu fazer a ação de Pagar Parcela"
            // Actually, if Helen owes me, it's not MY expense yet, it's a "loan".
            // However, the card purchase ALREADY entered my expenses if it was registered as a TransacaoFinanceira.
        }
        resumo.setDevedores(new java.util.ArrayList<>(devedoresMap.values()));

        return resumo;
    }

    /**
     * Determina se uma transação pertence à fatura do mês de referência.
     * Regra: Dia 03 até 02 do mês seguinte.
     */
    private boolean pertenceAFatura(LocalDate data, YearMonth mesReferencia) {
        int diaFechamento = 3;
        LocalDate inicioFatura = mesReferencia.atDay(diaFechamento);
        LocalDate fimFatura = mesReferencia.plusMonths(1).atDay(diaFechamento - 1);
        
        return (data.isEqual(inicioFatura) || data.isAfter(inicioFatura)) && 
               (data.isEqual(fimFatura) || data.isBefore(fimFatura));
    }
}
