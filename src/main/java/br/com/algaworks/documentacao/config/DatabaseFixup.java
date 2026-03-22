package br.com.algaworks.documentacao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFixup {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void fixCheckConstraints() {
        try {
            // Remove a constraint de check do enum tipo para permitir que os novos tipos (Cartão de Crédito/PIX) sejam aceitos pelo banco PostgreSQL
            jdbcTemplate.execute("ALTER TABLE instituicao_financeira DROP CONSTRAINT instituicao_financeira_tipo_check");
            System.out.println("Constraint velha removida com sucesso para permitir novos tipos de contas!");
        } catch (Exception e) {
            System.out.println("A constraint não existe ou já foi corrigida: " + e.getMessage());
        }
    }
}
