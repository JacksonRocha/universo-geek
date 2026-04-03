package br.com.algaworks.documentacao.config;

import br.com.algaworks.documentacao.model.Role;
import br.com.algaworks.documentacao.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializerConfig {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            List<String> userRoles = Arrays.asList("WORK", "HOBBY", "DEVELOP", "FINANCES", "ADMIN");
            
            for (String roleName : userRoles) {
                if (!roleRepository.findByNome(roleName).isPresent()) {
                    roleRepository.save(new Role(roleName));
                }
            }
        };
    }
}
