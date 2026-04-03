package br.com.algaworks.documentacao.controller;

import br.com.algaworks.documentacao.dto.TarefaTrabalhoDTO;
import br.com.algaworks.documentacao.model.BlocoTarefa;
import br.com.algaworks.documentacao.model.StatusTarefa;
import br.com.algaworks.documentacao.model.TarefaTrabalho;
import br.com.algaworks.documentacao.model.Usuario;
import br.com.algaworks.documentacao.repository.TarefaTrabalhoRepository;
import br.com.algaworks.documentacao.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/work/tasks")
public class WorkRestController {

    @Autowired
    private TarefaTrabalhoRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<TarefaTrabalhoDTO>> getTasks(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(principal.getName());
        if (!usuarioOpt.isPresent()) return ResponseEntity.status(401).build();

        List<TarefaTrabalho> tarefas = tarefaRepository.findByUsuarioId(usuarioOpt.get().getId());
        List<TarefaTrabalhoDTO> dtos = tarefas.stream().map(TarefaTrabalhoDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<TarefaTrabalhoDTO> createTask(@RequestBody TarefaTrabalhoDTO dto, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(principal.getName());
        if (!usuarioOpt.isPresent()) return ResponseEntity.status(401).build();

        TarefaTrabalho nova = new TarefaTrabalho();
        nova.setTitulo(dto.getTitulo());
        nova.setDescricao(dto.getDescricao());
        nova.setImageUrl(dto.getImageUrl());
        nova.setDataEntrega(dto.getDataEntrega());
        nova.setBloco(dto.getBloco() == null ? BlocoTarefa.BACKLOG : dto.getBloco());
        nova.setStatus(StatusTarefa.valueOf(nova.getBloco().name()));
        nova.setUrgente(dto.getUrgente() != null && dto.getUrgente());
        nova.setPlanejado(dto.getPlanejado() != null && dto.getPlanejado());
        nova.setSolicitante(dto.getSolicitante());
        nova.setHoras(dto.getHoras() == null ? 0 : dto.getHoras());

        // Map Meeting Fields
        nova.setTema(dto.getTema());
        nova.setAssunto(dto.getAssunto());
        nova.setProjeto(dto.getProjeto());
        nova.setParticipantes(dto.getParticipantes());
        nova.setDuracao(dto.getDuracao());
        nova.setRealizada(dto.getRealizada() != null && dto.getRealizada());
        nova.setUsuario(usuarioOpt.get());

        TarefaTrabalho salva = tarefaRepository.save(nova);
        return ResponseEntity.ok(new TarefaTrabalhoDTO(salva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaTrabalhoDTO> updateTask(@PathVariable Long id, @RequestBody TarefaTrabalhoDTO dto, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<TarefaTrabalho> tarefaOpt = tarefaRepository.findById(id);
        if (!tarefaOpt.isPresent() || !tarefaOpt.get().getUsuario().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).build();
        }

        TarefaTrabalho tarefa = tarefaOpt.get();
        if (dto.getTitulo() != null) tarefa.setTitulo(dto.getTitulo());
        if (dto.getDescricao() != null) tarefa.setDescricao(dto.getDescricao());
        if (dto.getImageUrl() != null) tarefa.setImageUrl(dto.getImageUrl());
        if (dto.getDataEntrega() != null) tarefa.setDataEntrega(dto.getDataEntrega());
        if (dto.getBloco() != null && dto.getBloco() != tarefa.getBloco()) {
            tarefa.setBloco(dto.getBloco());
            tarefa.setStatus(StatusTarefa.valueOf(tarefa.getBloco().name()));

            if (tarefa.getStatus() == StatusTarefa.A_FAZER) {
                if (tarefa.getDataAFazer() == null) tarefa.setDataAFazer(LocalDateTime.now());
            } else if (tarefa.getStatus() == StatusTarefa.FAZENDO) {
                if (tarefa.getDataEmProgresso() == null) tarefa.setDataEmProgresso(LocalDateTime.now());
            } else if (tarefa.getStatus() == StatusTarefa.CONCLUIDO) {
                if (tarefa.getDataConcluido() == null) tarefa.setDataConcluido(LocalDateTime.now());
            }
        }
        if (dto.getUrgente() != null) tarefa.setUrgente(dto.getUrgente());
        if (dto.getPlanejado() != null) tarefa.setPlanejado(dto.getPlanejado());
        if (dto.getSolicitante() != null) tarefa.setSolicitante(dto.getSolicitante());
        if (dto.getHoras() != null) tarefa.setHoras(dto.getHoras());

        // Update Meeting Fields
        if (dto.getTema() != null) tarefa.setTema(dto.getTema());
        if (dto.getAssunto() != null) tarefa.setAssunto(dto.getAssunto());
        if (dto.getProjeto() != null) tarefa.setProjeto(dto.getProjeto());
        if (dto.getParticipantes() != null) tarefa.setParticipantes(dto.getParticipantes());
        if (dto.getDuracao() != null) tarefa.setDuracao(dto.getDuracao());
        if (dto.getRealizada() != null) tarefa.setRealizada(dto.getRealizada());

        TarefaTrabalho atualizada = tarefaRepository.save(tarefa);
        return ResponseEntity.ok(new TarefaTrabalhoDTO(atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<TarefaTrabalho> tarefaOpt = tarefaRepository.findById(id);
        if (!tarefaOpt.isPresent() || !tarefaOpt.get().getUsuario().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).build();
        }

        tarefaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadProxy(@RequestParam("image") MultipartFile file) {
        String[] clientIds = {"546c25a59c58ad7", "b97c88b9015c7ed", "1df677d33bcc3fd", "4920a0584799042"};
        RestTemplate restTemplate = new RestTemplate();

        for (String clientId : clientIds) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Client-ID " + clientId);
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("image", file.getResource());

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.postForEntity("https://api.imgur.com/3/image", requestEntity, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response.getBody());
                    if (root.path("success").asBoolean()) {
                        Map<String, Object> res = new HashMap<>();
                        res.put("success", true);
                        res.put("link", root.path("data").path("link").asText());
                        return ResponseEntity.ok(res);
                    }
                }
            } catch (Exception e) {
                System.err.println("Upload failed with Client-ID " + clientId + ": " + e.getMessage());
                // Continue to next ID
            }
        }

        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "Todos os limites do Imgur excedidos no momento.");
        return ResponseEntity.status(429).body(error);
    }
}
