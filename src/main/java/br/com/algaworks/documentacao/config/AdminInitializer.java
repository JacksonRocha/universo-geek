package br.com.algaworks.documentacao.config;

import br.com.algaworks.documentacao.model.Role;
import br.com.algaworks.documentacao.model.Usuario;
import br.com.algaworks.documentacao.repository.RoleRepository;
import br.com.algaworks.documentacao.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {


    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (usuarioRepository.findByUsername("JacksonRocha").isEmpty()) {
            Role adminRole = roleRepository.findByNome("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

            Usuario admin = new Usuario();
            admin.setUsername("JacksonRocha");
            admin.setPassword(passwordEncoder.encode("Jackson@184736")); // senha segura
            admin.getRoles().add(adminRole);

            usuarioRepository.save(admin);
            System.out.println("Usuário ADMIN criado com sucesso.");
        }
    }
}


