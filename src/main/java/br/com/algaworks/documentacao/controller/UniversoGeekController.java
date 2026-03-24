package br.com.algaworks.documentacao.controller;

import br.com.algaworks.documentacao.dto.ResumoFinanceiroDTO;
import br.com.algaworks.documentacao.model.CartaoCredito;
import br.com.algaworks.documentacao.model.CategoriaTransacao;
import br.com.algaworks.documentacao.model.InstituicaoFinanceira;
import br.com.algaworks.documentacao.model.Usuario;
import br.com.algaworks.documentacao.repository.CartaoCreditoRepository;
import br.com.algaworks.documentacao.repository.CategoriaTransacaoRepository;
import br.com.algaworks.documentacao.repository.InstituicaoFinanceiraRepository;
import br.com.algaworks.documentacao.repository.UsuarioRepository;
import br.com.algaworks.documentacao.service.FinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class UniversoGeekController {

    @Autowired
    private FinanceiroService financeiroService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaTransacaoRepository categoriaRepository;

    @Autowired
    private InstituicaoFinanceiraRepository instituicaoRepository;

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepository;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("mensagem", "Bem-vindo ao Universo Geek!");
        return "home"; // templates/home.html (Thymeleaf)
    }


    @GetMapping("/helen")
    public String helen(Model model) {
        model.addAttribute("mensagemHelen", "Helen, eu te amo! 💖");
        return "helen";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/work")
    public String work() {
        return "work";
    }

    @GetMapping("/hobby")
    public String hobby() {
        return "hobby";
    }

    @GetMapping("/develop")
    public String develop() {
        return "develop";
    }

    @GetMapping("/finances")
    public String finances(Model model, Principal principal) {
        if (principal != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(principal.getName());
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                ResumoFinanceiroDTO resumo = financeiroService.obterResumoFinanceiro(usuario.getId());
                model.addAttribute("resumo", resumo);

                // Add lists for modals
                List<CategoriaTransacao> categorias = categoriaRepository.findByUsuarioId(usuario.getId());
                List<InstituicaoFinanceira> instituicoes = instituicaoRepository.findByUsuarioId(usuario.getId());
                List<CartaoCredito> cartoes = cartaoCreditoRepository.findByUsuarioId(usuario.getId());

                model.addAttribute("categorias", categorias);
                model.addAttribute("instituicoes", instituicoes);
                model.addAttribute("cartoes", cartoes);
            }
        }
        return "finances";
    }

    @GetMapping("/git")
    public String gitPage() {
        return "git";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/zueira")
    public String zueira() {
        return "zueira";
    }

}
