package br.com.algaworks.documentacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

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

}
