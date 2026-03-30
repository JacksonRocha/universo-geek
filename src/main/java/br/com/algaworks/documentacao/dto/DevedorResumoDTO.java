package br.com.algaworks.documentacao.dto;

import br.com.algaworks.documentacao.model.DividaTerceiro;
import java.util.List;
import java.util.ArrayList;

public class DevedorResumoDTO {
    private String nome;
    private List<DividaTerceiro> dividas = new ArrayList<>();

    public DevedorResumoDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public List<DividaTerceiro> getDividas() { return dividas; }
    public void setDividas(List<DividaTerceiro> dividas) { this.dividas = dividas; }
}
