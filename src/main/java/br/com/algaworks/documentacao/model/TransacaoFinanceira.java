package br.com.algaworks.documentacao.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacao_financeira")
public class TransacaoFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTransacao status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaTransacao categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instituicao_id")
    private InstituicaoFinanceira instituicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartao_credito_id")
    private CartaoCredito cartaoCredito;

    @Column(name = "terceiro_emprestimo")
    private String terceiroEmprestimo; // Name if lent to someone

    @Column(name = "is_emprestimo")
    private Boolean isEmprestimo = false;

    @Column(name = "local_gasto")
    private String localGasto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public TipoTransacao getTipo() { return tipo; }
    public void setTipo(TipoTransacao tipo) { this.tipo = tipo; }
    public StatusTransacao getStatus() { return status; }
    public void setStatus(StatusTransacao status) { this.status = status; }
    public CategoriaTransacao getCategoria() { return categoria; }
    public void setCategoria(CategoriaTransacao categoria) { this.categoria = categoria; }
    public InstituicaoFinanceira getInstituicao() { return instituicao; }
    public void setInstituicao(InstituicaoFinanceira instituicao) { this.instituicao = instituicao; }
    public CartaoCredito getCartaoCredito() { return cartaoCredito; }
    public void setCartaoCredito(CartaoCredito cartaoCredito) { this.cartaoCredito = cartaoCredito; }
    public String getTerceiroEmprestimo() { return terceiroEmprestimo; }
    public void setTerceiroEmprestimo(String terceiroEmprestimo) { this.terceiroEmprestimo = terceiroEmprestimo; }
    public Boolean getIsEmprestimo() { return isEmprestimo; }
    public void setIsEmprestimo(Boolean emprestimo) { isEmprestimo = emprestimo; }
    public String getLocalGasto() { return localGasto; }
    public void setLocalGasto(String localGasto) { this.localGasto = localGasto; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
