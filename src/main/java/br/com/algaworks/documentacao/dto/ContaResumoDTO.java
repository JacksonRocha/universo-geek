package br.com.algaworks.documentacao.dto;

import java.math.BigDecimal;

public class ContaResumoDTO {
    private Long id;
    private String nome;
    private String tipo;
    private BigDecimal saldo;
    private String logoUrl;

    public ContaResumoDTO(Long id, String nome, String tipo, BigDecimal saldo, String logoUrl) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.saldo = saldo;
        this.logoUrl = logoUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // Getters and Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getImageUrl() {
        if (logoUrl != null && !logoUrl.isEmpty()) return logoUrl;
        if (nome != null) {
            String sanitized = nome.toLowerCase().replaceAll("\\s+", "")
                .replaceAll("[áàãâä]", "a").replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i").replaceAll("[óòõôö]", "o")
                .replaceAll("[úùûü]", "u").replaceAll("[ç]", "c");
            return "/images/" + sanitized + ".png";
        }
        return null;
    }
}
