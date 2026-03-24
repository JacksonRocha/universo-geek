package br.com.algaworks.documentacao.controller;

import br.com.algaworks.documentacao.dto.TarefaTrabalhoDTO;
import br.com.algaworks.documentacao.model.BlocoTarefa;
import br.com.algaworks.documentacao.model.StatusTarefa;
import br.com.algaworks.documentacao.model.TarefaTrabalho;
import br.com.algaworks.documentacao.model.Usuario;
import br.com.algaworks.documentacao.repository.TarefaTrabalhoRepository;
import br.com.algaworks.documentacao.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
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
        if (usuarioOpt.isEmpty()) return ResponseEntity.status(401).build();

        List<TarefaTrabalho> tarefas = tarefaRepository.findByUsuarioId(usuarioOpt.get().getId());
        List<TarefaTrabalhoDTO> dtos = tarefas.stream().map(TarefaTrabalhoDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<TarefaTrabalhoDTO> createTask(@RequestBody TarefaTrabalhoDTO dto, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(principal.getName());
        if (usuarioOpt.isEmpty()) return ResponseEntity.status(401).build();

        TarefaTrabalho nova = new TarefaTrabalho();
        nova.setTitulo(dto.getTitulo());
        nova.setDescricao(dto.getDescricao());
        nova.setImageUrl(dto.getImageUrl());
        nova.setDataEntrega(dto.getDataEntrega());
        nova.setBloco(dto.getBloco() == null ? BlocoTarefa.ATIVIDADES_DO_DIA : dto.getBloco());
        nova.setStatus(dto.getStatus() == null ? StatusTarefa.A_FAZER : dto.getStatus());
        nova.setUsuario(usuarioOpt.get());

        TarefaTrabalho salva = tarefaRepository.save(nova);
        return ResponseEntity.ok(new TarefaTrabalhoDTO(salva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaTrabalhoDTO> updateTask(@PathVariable Long id, @RequestBody TarefaTrabalhoDTO dto, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<TarefaTrabalho> tarefaOpt = tarefaRepository.findById(id);
        if (tarefaOpt.isEmpty() || !tarefaOpt.get().getUsuario().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).build();
        }

        TarefaTrabalho tarefa = tarefaOpt.get();
        if(dto.getTitulo() != null) tarefa.setTitulo(dto.getTitulo());
        if(dto.getDescricao() != null) tarefa.setDescricao(dto.getDescricao());
        if(dto.getImageUrl() != null) tarefa.setImageUrl(dto.getImageUrl());
        if(dto.getDataEntrega() != null) tarefa.setDataEntrega(dto.getDataEntrega());
        if(dto.getBloco() != null) tarefa.setBloco(dto.getBloco());
        if(dto.getStatus() != null) tarefa.setStatus(dto.getStatus());

        TarefaTrabalho atualizada = tarefaRepository.save(tarefa);
        return ResponseEntity.ok(new TarefaTrabalhoDTO(atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<TarefaTrabalho> tarefaOpt = tarefaRepository.findById(id);
        if (tarefaOpt.isEmpty() || !tarefaOpt.get().getUsuario().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).build();
        }

        tarefaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
