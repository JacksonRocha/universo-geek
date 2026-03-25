package br.com.algaworks.documentacao.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SchemaFixer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void fixSchema() {
        try {
            System.out.println("Applying Schema Fix: Dropping legacy check constraints...");
            
            // Drop constraints that limit the enum values in the database
            jdbcTemplate.execute("ALTER TABLE tarefa_trabalho DROP CONSTRAINT IF EXISTS tarefa_trabalho_bloco_check");
            jdbcTemplate.execute("ALTER TABLE tarefa_trabalho DROP CONSTRAINT IF EXISTS tarefa_trabalho_status_check");
            
            System.out.println("Schema Fix applied successfully.");
        } catch (Exception e) {
            System.err.println("Failed to apply schema fix: " + e.getMessage());
        }
    }
}
