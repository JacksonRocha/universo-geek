package br.com.algaworks.documentacao.controller;

import br.com.algaworks.documentacao.model.Role;
import br.com.algaworks.documentacao.model.Usuario;
import br.com.algaworks.documentacao.repository.RoleRepository;
import br.com.algaworks.documentacao.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class UserManagementController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public UserManagementController(UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "user-list"; // Aqui será o novo HTML
    }

    @PostMapping("/users/{id}/add-role")
    public String addRoleToUser(@PathVariable Long id, @RequestParam String roleName) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Role role = roleRepository.findByNome(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " + roleName));

        usuario.getRoles().add(role);
        usuarioRepository.save(usuario);

        return "redirect:/admin/users";
    }
}
