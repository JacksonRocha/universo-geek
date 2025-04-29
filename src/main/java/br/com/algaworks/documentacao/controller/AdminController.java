package br.com.algaworks.documentacao.controller;

import br.com.algaworks.documentacao.model.Role;
import br.com.algaworks.documentacao.model.Usuario;
import br.com.algaworks.documentacao.repository.RoleRepository;
import br.com.algaworks.documentacao.repository.UsuarioRepository;
import br.com.algaworks.documentacao.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;

    public AdminController(UserService userService, RoleRepository roleRepository,
                           UsuarioRepository usuarioRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/admin/register")
    public String showRegisterForm(Model model) {
        return "register";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String permission) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);

        Role role = roleRepository.findByNome(permission)
                .orElseThrow(() -> new IllegalArgumentException("Permissão inválida: " + permission));

        usuario.setRoles(Collections.singleton(role));

        userService.createUser(usuario);

        return "redirect:/login?registered";
    }

    @PostMapping("/admin/users/{userId}/remove-role")
    public String removeRoleFromUser(@PathVariable Long userId, @RequestParam String roleName) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        Role role = roleRepository.findByNome(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Permissão não encontrada"));

        usuario.getRoles().remove(role);
        usuarioRepository.save(usuario);

        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/{userId}/delete")
    public String deleteUser(@PathVariable Long userId) {
        usuarioRepository.deleteById(userId);
        return "redirect:/admin/users";
    }


}

