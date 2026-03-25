package br.com.algaworks.documentacao.dto;

import java.math.BigDecimal;
import java.util.List;

public class ResumoFinanceiroDTO {
    
    private BigDecimal saldoAtual;
    private BigDecimal receitasMes;
    private BigDecimal despesasMes;
    private BigDecimal receitasVencidas;
    private BigDecimal despesasVencidas;
    private BigDecimal receitasVencendoHoje;
    private BigDecimal despesasVencendoHoje;
    private BigDecimal receitasFuturas;
    private BigDecimal despesasFuturas;
    
    private List<ContaResumoDTO> saldosContas;
    private List<CategoriaResumoDTO> categoriasDespesas;
    
    private List<br.com.algaworks.documentacao.model.TransacaoFinanceira> receitasMesList = new java.util.ArrayList<>();
    private List<br.com.algaworks.documentacao.model.TransacaoFinanceira> despesasMesList = new java.util.ArrayList<>();
    private List<br.com.algaworks.documentacao.model.TransacaoFinanceira> despesasImediatasMesList = new java.util.ArrayList<>();
    private List<br.com.algaworks.documentacao.model.TransacaoFinanceira> despesasCartaoMesList = new java.util.ArrayList<>();
    
    private BigDecimal totalDespesasImediatas = BigDecimal.ZERO;
    private BigDecimal totalDespesasCartao = BigDecimal.ZERO;

    // Getters and Setters
    public List<br.com.algaworks.documentacao.model.TransacaoFinanceira> getReceitasMesList() { return receitasMesList; }
    public void setReceitasMesList(List<br.com.algaworks.documentacao.model.TransacaoFinanceira> receitasMesList) { this.receitasMesList = receitasMesList; }
    public List<br.com.algaworks.documentacao.model.TransacaoFinanceira> getDespesasMesList() { return despesasMesList; }
    public void setDespesasMesList(List<br.com.algaworks.documentacao.model.TransacaoFinanceira> despesasMesList) { this.despesasMesList = despesasMesList; }

    public BigDecimal getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(BigDecimal saldoAtual) { this.saldoAtual = saldoAtual; }
    public BigDecimal getReceitasMes() { return receitasMes; }
    public void setReceitasMes(BigDecimal receitasMes) { this.receitasMes = receitasMes; }
    public BigDecimal getDespesasMes() { return despesasMes; }
    public void setDespesasMes(BigDecimal despesasMes) { this.despesasMes = despesasMes; }
    public BigDecimal getReceitasVencidas() { return receitasVencidas; }
    public void setReceitasVencidas(BigDecimal receitasVencidas) { this.receitasVencidas = receitasVencidas; }
    public BigDecimal getDespesasVencidas() { return despesasVencidas; }
    public void setDespesasVencidas(BigDecimal despesasVencidas) { this.despesasVencidas = despesasVencidas; }
    public BigDecimal getReceitasVencendoHoje() { return receitasVencendoHoje; }
    public void setReceitasVencendoHoje(BigDecimal receitasVencendoHoje) { this.receitasVencendoHoje = receitasVencendoHoje; }
    public BigDecimal getDespesasVencendoHoje() { return despesasVencendoHoje; }
    public void setDespesasVencendoHoje(BigDecimal despesasVencendoHoje) { this.despesasVencendoHoje = despesasVencendoHoje; }
    public BigDecimal getReceitasFuturas() { return receitasFuturas; }
    public void setReceitasFuturas(BigDecimal receitasFuturas) { this.receitasFuturas = receitasFuturas; }
    public BigDecimal getDespesasFuturas() { return despesasFuturas; }
    public void setDespesasFuturas(BigDecimal despesasFuturas) { this.despesasFuturas = despesasFuturas; }
    public List<br.com.algaworks.documentacao.model.TransacaoFinanceira> getDespesasImediatasMesList() { return despesasImediatasMesList; }
    public void setDespesasImediatasMesList(List<br.com.algaworks.documentacao.model.TransacaoFinanceira> list) { this.despesasImediatasMesList = list; }
    public List<br.com.algaworks.documentacao.model.TransacaoFinanceira> getDespesasCartaoMesList() { return despesasCartaoMesList; }
    public void setDespesasCartaoMesList(List<br.com.algaworks.documentacao.model.TransacaoFinanceira> list) { this.despesasCartaoMesList = list; }
    public BigDecimal getTotalDespesasImediatas() { return totalDespesasImediatas; }
    public void setTotalDespesasImediatas(BigDecimal total) { this.totalDespesasImediatas = total; }
    public BigDecimal getTotalDespesasCartao() { return totalDespesasCartao; }
    public void setTotalDespesasCartao(BigDecimal total) { this.totalDespesasCartao = total; }
    public List<ContaResumoDTO> getSaldosContas() { return saldosContas; }
    public void setSaldosContas(List<ContaResumoDTO> saldosContas) { this.saldosContas = saldosContas; }
    public List<CategoriaResumoDTO> getCategoriasDespesas() { return categoriasDespesas; }
    public void setCategoriasDespesas(List<CategoriaResumoDTO> categoriasDespesas) { this.categoriasDespesas = categoriasDespesas; }
}
