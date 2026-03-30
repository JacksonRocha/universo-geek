package br.com.algaworks.documentacao.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "divida_terceiro")
public class DividaTerceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String devedor;

    @Column(nullable = false)
    private String item;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "total_parcelas", nullable = false)
    private Integer totalParcelas;

    @Column(name = "parcelas_pagas", nullable = false)
    private Integer parcelasPagas = 0;

    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @Column(name = "instituicao_id")
    private Long instituicaoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Helper for remaining value
    public BigDecimal getValorRestante() {
        if (totalParcelas == 0) return BigDecimal.ZERO;
        BigDecimal perParcela = valorTotal.divide(new BigDecimal(totalParcelas), 2, java.math.RoundingMode.HALF_UP);
        return perParcela.multiply(new BigDecimal(totalParcelas - parcelasPagas));
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDevedor() { return devedor; }
    public void setDevedor(String devedor) { this.devedor = devedor; }
    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public Integer getTotalParcelas() { return totalParcelas; }
    public void setTotalParcelas(Integer totalParcelas) { this.totalParcelas = totalParcelas; }
    public Integer getParcelasPagas() { return parcelasPagas; }
    public void setParcelasPagas(Integer parcelasPagas) { this.parcelasPagas = parcelasPagas; }
    public LocalDate getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }
    public Long getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(Long instituicaoId) { this.instituicaoId = instituicaoId; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
