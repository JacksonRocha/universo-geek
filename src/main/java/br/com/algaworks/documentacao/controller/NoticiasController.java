package br.com.algaworks.documentacao.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/noticias")
public class NoticiasController {

    @GetMapping("/tecmundo")
    public List<Map<String, String>> getTecmundoNews() {
        List<Map<String, String>> newsList = new ArrayList<>();

        // =====================================================================================
        // 🔹 NOTÍCIAS DE TECNOLOGIA
        // Como alterar:
        // - "title": O título da notícia que aparece em destaque.
        // - "link": A URL completa para onde o usuário vai ao clicar (ex: https://g1.globo...)
        //           (Se deixar "#" ele não redireciona para lugar nenhum).
        // - "image": O link da foto de capa. Você pode pegar fotos do Google Imagens ou Unsplash.
        // =====================================================================================

        newsList.add(Map.of(
                "title", "OpenAI anuncia nova versão com raciocínio avançado",
                "link", "https://chat.openai.com",
                "image", "https://images.unsplash.com/photo-1677442136019-21780ecad995?q=80&w=800",
                "date", "Hoje",
                "author", "Redação Tech"
        ));
        newsList.add(Map.of(
                "title", "Nvidia lança nova geração de placas RTX com foco absoluto em IA",
                "link", "https://www.nvidia.com",
                "image", "https://images.unsplash.com/photo-1591488320449-011701bb6704?q=80&w=800",
                "date", "Ontem",
                "author", "Hardware BR"
        ));
        newsList.add(Map.of(
                "title", "SpaceX conclui com sucesso mais um pouso histórico da Starship",
                "link", "https://www.spacex.com",
                "image", "https://images.unsplash.com/photo-1541185933-ef5d8ed016c2?q=80&w=800",
                "date", "Ontem",
                "author", "Exploração Espacial"
        ));
        newsList.add(Map.of(
                "title", "Rumor: Novo console promete dobro de performance em ray tracing",
                "link", "#",
                "image", "https://images.unsplash.com/photo-1606144042871-2ed9a011ed02?q=80&w=800",
                "date", "Esta Semana",
                "author", "Mundo dos Games"
        ));
        newsList.add(Map.of(
                "title", "Apple pode integrar IA generativa nativa no próximo sistema",
                "link", "https://www.apple.com",
                "image", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=800",
                "date", "Esta Semana",
                "author", "Tech Mobile"
        ));
        newsList.add(Map.of(
                "title", "Vazamento revela nova interface minimalista focada em nuvem",
                "link", "#",
                "image", "https://i.imgur.com/qHgwBWn.jpeg",
                "date", "Semana Passada",
                "author", "Software Update"
        ));

        return newsList;
    }

    @GetMapping("/smartanimes")
    public List<Map<String, String>> getSmartAnimes() {
        List<Map<String, String>> animeList = new ArrayList<>();

        // =====================================================================================
        // 🔹 MUNDO GEEK E ANIMES
        // Como alterar:
        // - "title": O título do episódio ou anime em destaque.
        // - "link": O link oficial (ex: Crunchyroll, Netflix, ou YouTube).
        // - "image": Link de uma imagem com boa resolução (16:9 de preferência).
        // =====================================================================================

        animeList.add(Map.of(
                "title", "Solo Leveling: 2ª Temporada é oficialmente confirmada",
                "link", "#",
                "image", "https://image.tmdb.org/t/p/original/rC0PBShOwhAKLcuYNOi0q1RcVbe.jpg",
                "date", "Lançamento",
                "author", "Portal Otaku"
        ));
        animeList.add(Map.of(
                "title", "Deadpool & Wolverine quebra todos os recordes em trailer",
                "link", "#",
                "image", "https://image.tmdb.org/t/p/original/fzhJAuhgUV11BGLUtv4GQbSbNZ2.jpg",
                "date", "Cinema",
                "author", "Geek News"
        ));
        animeList.add(Map.of(
                "title", "One Piece: Renovação do Live-Action empolga base de fãs",
                "link", "#",
                "image", "https://image.tmdb.org/t/p/original/nI7Gc3EvuwXxzQboSAuF0LdR4kc.jpg",
                "date", "Adaptação",
                "author", "Netflix BR"
        ));
        animeList.add(Map.of(
                "title", "Vencedores do Anime Awards deste ano surpreendem a crítica",
                "link", "#",
                "image", "https://image.tmdb.org/t/p/original/xwvPoLglKNX9hKexF8qnBGB9R1s.jpg",
                "date", "Premiação",
                "author", "Mundo Anime"
        ));
        animeList.add(Map.of(
                "title", "Criador de obra lendária deixa legado inesquecível para todos",
                "link", "#",
                "image", "https://image.tmdb.org/t/p/original/43KESrZGndlKmbIpWc7i0YPg8kJ.jpg",
                "date", "Homenagem",
                "author", "Cultura Pop"
        ));
        animeList.add(Map.of(
                "title", "Novos episódios de RPG Fantasia prometem animação surreal",
                "link", "#",
                "image", "https://i.imgur.com/gfZfbOD.jpeg",
                "date", "Temporada",
                "author", "Animação"
        ));

        return animeList;
    }
}
