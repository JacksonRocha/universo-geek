package br.com.algaworks.documentacao.dto;

import java.math.BigDecimal;

public class CategoriaResumoDTO {
    private String nome;
    private BigDecimal total;
    private BigDecimal percentual;
    private String cor;

    public CategoriaResumoDTO(String nome, BigDecimal total, BigDecimal percentual, String cor) {
        this.nome = nome;
        this.total = total;
        this.percentual = percentual;
        this.cor = cor;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getPercentual() { return percentual; }
    public void setPercentual(BigDecimal percentual) { this.percentual = percentual; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
}
