package br.com.algaworks.documentacao.dto;

import br.com.algaworks.documentacao.model.BlocoTarefa;
import br.com.algaworks.documentacao.model.StatusTarefa;
import br.com.algaworks.documentacao.model.TarefaTrabalho;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TarefaTrabalhoDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String imageUrl;
    private LocalDate dataEntrega;
    private BlocoTarefa bloco;
    private StatusTarefa status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAFazer;
    private LocalDateTime dataEmProgresso;
    private LocalDateTime dataConcluido;
    private Boolean urgente;
    private Boolean planejado;
    private String solicitante;
    private Integer horas;

    // Meeting Fields
    private String tema;
    private String assunto;
    private String projeto;
    private String participantes;
    private String duracao;
    private Boolean realizada;

    public TarefaTrabalhoDTO() {}

    public TarefaTrabalhoDTO(TarefaTrabalho tarefa) {
        this.id = tarefa.getId();
        this.titulo = tarefa.getTitulo();
        this.descricao = tarefa.getDescricao();
        this.imageUrl = tarefa.getImageUrl();
        this.dataEntrega = tarefa.getDataEntrega();
        this.bloco = tarefa.getBloco();
        this.status = tarefa.getStatus();
        this.dataCriacao = tarefa.getDataCriacao();
        this.dataAFazer = tarefa.getDataAFazer();
        this.dataEmProgresso = tarefa.getDataEmProgresso();
        this.dataConcluido = tarefa.getDataConcluido();
        this.urgente = tarefa.getUrgente();
        this.planejado = tarefa.getPlanejado();
        this.solicitante = tarefa.getSolicitante();
        this.horas = tarefa.getHoras();
        this.tema = tarefa.getTema();
        this.assunto = tarefa.getAssunto();
        this.projeto = tarefa.getProjeto();
        this.participantes = tarefa.getParticipantes();
        this.duracao = tarefa.getDuracao();
        this.realizada = tarefa.getRealizada();
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

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAFazer() { return dataAFazer; }
    public void setDataAFazer(LocalDateTime dataAFazer) { this.dataAFazer = dataAFazer; }

    public LocalDateTime getDataEmProgresso() { return dataEmProgresso; }
    public void setDataEmProgresso(LocalDateTime dataEmProgresso) { this.dataEmProgresso = dataEmProgresso; }

    public LocalDateTime getDataConcluido() { return dataConcluido; }
    public void setDataConcluido(LocalDateTime dataConcluido) { this.dataConcluido = dataConcluido; }

    public Boolean getUrgente() { return urgente; }
    public void setUrgente(Boolean urgente) { this.urgente = urgente; }

    public Boolean getPlanejado() { return planejado; }
    public void setPlanejado(Boolean planejado) { this.planejado = planejado; }

    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }

    public Integer getHoras() { return horas; }
    public void setHoras(Integer horas) { this.horas = horas; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }

    public String getProjeto() { return projeto; }
    public void setProjeto(String projeto) { this.projeto = projeto; }

    public String getParticipantes() { return participantes; }
    public void setParticipantes(String participantes) { this.participantes = participantes; }

    public String getDuracao() { return duracao; }
    public void setDuracao(String duracao) { this.duracao = duracao; }

    public Boolean getRealizada() { return realizada; }
    public void setRealizada(Boolean realizada) { this.realizada = realizada; }
}
