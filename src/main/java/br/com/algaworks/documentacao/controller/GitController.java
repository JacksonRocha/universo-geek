package br.com.algaworks.documentacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GitController {

    @GetMapping("/")
    public String home() {
        return "index";
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

    @GetMapping("/house")
    public String house() {
        return "house";
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

