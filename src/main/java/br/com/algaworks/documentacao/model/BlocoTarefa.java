package br.com.algaworks.documentacao.model;

public enum BlocoTarefa {
    BACKLOG,
    A_FAZER,
    FAZENDO,
    CONCLUIDO,
    /* Legacy values to prevent crashes */
    PRIORIDADES,
    REUNIOES,
    ATIVIDADES_DO_DIA,
    JIRAS
}
