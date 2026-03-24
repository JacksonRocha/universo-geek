package br.com.algaworks.documentacao.dto;

import br.com.algaworks.documentacao.model.BlocoTarefa;
import br.com.algaworks.documentacao.model.StatusTarefa;
import br.com.algaworks.documentacao.model.TarefaTrabalho;
import java.time.LocalDate;

public class TarefaTrabalhoDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String imageUrl;
    private LocalDate dataEntrega;
    private BlocoTarefa bloco;
    private StatusTarefa status;

    public TarefaTrabalhoDTO() {}

    public TarefaTrabalhoDTO(TarefaTrabalho tarefa) {
        this.id = tarefa.getId();
        this.titulo = tarefa.getTitulo();
        this.descricao = tarefa.getDescricao();
        this.imageUrl = tarefa.getImageUrl();
        this.dataEntrega = tarefa.getDataEntrega();
        this.bloco = tarefa.getBloco();
        this.status = tarefa.getStatus();
    }

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
}
