package br.com.algaworks.documentacao.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefa_trabalho")
public class TarefaTrabalho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String imageUrl;

    private LocalDate dataEntrega;

    @Enumerated(EnumType.STRING)
    private BlocoTarefa bloco;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAFazer;
    private LocalDateTime dataEmProgresso;
    private LocalDateTime dataConcluido;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (this.status == StatusTarefa.A_FAZER) {
            dataAFazer = LocalDateTime.now();
        } else if (this.status == StatusTarefa.FAZENDO) {
            dataEmProgresso = LocalDateTime.now();
        } else if (this.status == StatusTarefa.CONCLUIDO) {
            dataConcluido = LocalDateTime.now();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDate getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDate dataEntrega) { this.dataEntrega = dataEntrega; }

    public BlocoTarefa getBloco() { return bloco; }
    public void setBloco(BlocoTarefa bloco) { this.bloco = bloco; }

    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAFazer() { return dataAFazer; }
    public void setDataAFazer(LocalDateTime dataAFazer) { this.dataAFazer = dataAFazer; }

    public LocalDateTime getDataEmProgresso() { return dataEmProgresso; }
    public void setDataEmProgresso(LocalDateTime dataEmProgresso) { this.dataEmProgresso = dataEmProgresso; }

    public LocalDateTime getDataConcluido() { return dataConcluido; }
    public void setDataConcluido(LocalDateTime dataConcluido) { this.dataConcluido = dataConcluido; }
}
