package br.com.algaworks.documentacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UniversoGeekController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("mensagem", "Bem-vindo ao Universo Geek! 🚀🌎");
        return "index";
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
    public String finances() {
        return "finances";
    }

    @GetMapping("/git")
    public String gitPage() {
        return "git";
    }

}
