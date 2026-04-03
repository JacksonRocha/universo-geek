package br.com.algaworks.documentacao.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
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

        newsList.add(noticia(
                "OpenAI anuncia nova versão com raciocínio avançado",
                "https://chat.openai.com",
                "https://images.unsplash.com/photo-1677442136019-21780ecad995?q=80&w=800",
                "Hoje",
                "Redação Tech"
        ));
        newsList.add(noticia(
                "Nvidia lança nova geração de placas RTX com foco absoluto em IA",
                "https://www.nvidia.com",
                "https://images.unsplash.com/photo-1591488320449-011701bb6704?q=80&w=800",
                "Ontem",
                "Hardware BR"
        ));
        newsList.add(noticia(
                "SpaceX conclui com sucesso mais um pouso histórico da Starship",
                "https://www.spacex.com",
                "https://images.unsplash.com/photo-1541185933-ef5d8ed016c2?q=80&w=800",
                "Ontem",
                "Exploração Espacial"
        ));
        newsList.add(noticia(
                "Rumor: Novo console promete dobro de performance em ray tracing",
                "#",
                "https://images.unsplash.com/photo-1606144042871-2ed9a011ed02?q=80&w=800",
                "Esta Semana",
                "Mundo dos Games"
        ));
        newsList.add(noticia(
                "Apple pode integrar IA generativa nativa no próximo sistema",
                "https://www.apple.com",
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=800",
                "Esta Semana",
                "Tech Mobile"
        ));
        newsList.add(noticia(
                "Vazamento revela nova interface minimalista focada em nuvem",
                "#",
                "https://i.imgur.com/qHgwBWn.jpeg",
                "Semana Passada",
                "Software Update"
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

        animeList.add(noticia(
                "Solo Leveling: 2ª Temporada é oficialmente confirmada",
                "#",
                "https://image.tmdb.org/t/p/original/rC0PBShOwhAKLcuYNOi0q1RcVbe.jpg",
                "Lançamento",
                "Portal Otaku"
        ));
        animeList.add(noticia(
                "Deadpool & Wolverine quebra todos os recordes em trailer",
                "#",
                "https://image.tmdb.org/t/p/original/fzhJAuhgUV11BGLUtv4GQbSbNZ2.jpg",
                "Cinema",
                "Geek News"
        ));
        animeList.add(noticia(
                "One Piece: Renovação do Live-Action empolga base de fãs",
                "#",
                "https://image.tmdb.org/t/p/original/nI7Gc3EvuwXxzQboSAuF0LdR4kc.jpg",
                "Adaptação",
                "Netflix BR"
        ));
        animeList.add(noticia(
                "Vencedores do Anime Awards deste ano surpreendem a crítica",
                "#",
                "https://image.tmdb.org/t/p/original/xwvPoLglKNX9hKexF8qnBGB9R1s.jpg",
                "Premiação",
                "Mundo Anime"
        ));
        animeList.add(noticia(
                "Criador de obra lendária deixa legado inesquecível para todos",
                "#",
                "https://image.tmdb.org/t/p/original/43KESrZGndlKmbIpWc7i0YPg8kJ.jpg",
                "Homenagem",
                "Cultura Pop"
        ));
        animeList.add(noticia(
                "Novos episódios de RPG Fantasia prometem animação surreal",
                "#",
                "https://i.imgur.com/gfZfbOD.jpeg",
                "Temporada",
                "Animação"
        ));

        return animeList;
    }

    private Map<String, String> noticia(String title, String link, String image, String date, String author) {
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("link", link);
        map.put("image", image);
        map.put("date", date);
        map.put("author", author);
        return map;
    }
}
