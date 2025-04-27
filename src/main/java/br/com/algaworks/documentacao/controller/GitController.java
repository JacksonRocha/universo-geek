package br.com.algaworks.documentacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GitController {

    @GetMapping("/git")
    public String gitPage() {
        return "git";
    }
}
