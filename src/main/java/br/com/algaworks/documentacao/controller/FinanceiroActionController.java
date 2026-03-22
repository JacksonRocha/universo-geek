package br.com.algaworks.documentacao.controller;

import br.com.algaworks.documentacao.model.*;
import br.com.algaworks.documentacao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/finances")
public class FinanceiroActionController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TransacaoFinanceiraRepository transacaoRepository;

    @Autowired
    private InstituicaoFinanceiraRepository instituicaoRepository;

    @Autowired
    private CategoriaTransacaoRepository categoriaRepository;

    @PostMapping("/transacao")
    public String salvarTransacao(
            @RequestParam String descricao,
            @RequestParam BigDecimal valor,
            @RequestParam String dataVencimento,
            @RequestParam(required = false) String dataPagamento,
            @RequestParam TipoTransacao tipo,
            @RequestParam StatusTransacao status,
            @RequestParam Long categoriaId,
            @RequestParam(required = false) Long instituicaoId,
            Principal principal) {
        
        if (principal != null) {
            Optional<Usuario> userOpt = usuarioRepository.findByUsername(principal.getName());
            if (userOpt.isPresent()) {
                TransacaoFinanceira transacao = new TransacaoFinanceira();
                transacao.setDescricao(descricao);
                transacao.setValor(valor);
                transacao.setDataVencimento(LocalDate.parse(dataVencimento));
                if (dataPagamento != null && !dataPagamento.isEmpty()) {
                    transacao.setDataPagamento(LocalDate.parse(dataPagamento));
                }
                transacao.setTipo(tipo);
                transacao.setStatus(status);
                transacao.setUsuario(userOpt.get());
                
                categoriaRepository.findById(categoriaId).ifPresent(transacao::setCategoria);
                if (instituicaoId != null) {
                    instituicaoRepository.findById(instituicaoId).ifPresent(transacao::setInstituicao);
                }

                transacaoRepository.save(transacao);

                // Update Instituicao Saldo if Pago
                if (status == StatusTransacao.PAGO && instituicaoId != null && transacao.getInstituicao() != null) {
                    InstituicaoFinanceira inst = transacao.getInstituicao();
                    if (tipo == TipoTransacao.RECEITA) {
                        inst.setSaldoAtual(inst.getSaldoAtual().add(valor));
                    } else if (tipo == TipoTransacao.DESPESA) {
                        inst.setSaldoAtual(inst.getSaldoAtual().subtract(valor));
                    }
                    instituicaoRepository.save(inst);
                }
            }
        }
        return "redirect:/finances";
    }

    @PostMapping("/instituicao")
    public String salvarInstituicao(
            @RequestParam String nome,
            @RequestParam TipoInstituicao tipo,
            @RequestParam(required = false) String saldoInicial,
            Principal principal) {
        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName()).ifPresent(user -> {
                InstituicaoFinanceira inst = new InstituicaoFinanceira();
                inst.setNome(nome);
                inst.setTipo(tipo);
                
                BigDecimal saldoParsed = BigDecimal.ZERO;
                if (saldoInicial != null && !saldoInicial.trim().isEmpty()) {
                    try {
                        saldoParsed = new BigDecimal(saldoInicial);
                    } catch (NumberFormatException e) {
                        try {
                            saldoParsed = new BigDecimal(saldoInicial.replace(",", "."));
                        } catch (Exception ex) {
                            saldoParsed = BigDecimal.ZERO;
                        }
                    }
                }
                
                inst.setSaldoAtual(saldoParsed);
                inst.setUsuario(user);
                instituicaoRepository.save(inst);
            });
        }
        return "redirect:/finances";
    }

    @PostMapping("/categoria")
    public String salvarCategoria(
            @RequestParam String nome,
            @RequestParam String cor,
            Principal principal) {
        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName()).ifPresent(user -> {
                CategoriaTransacao cat = new CategoriaTransacao();
                cat.setNome(nome);
                cat.setCor(cor);
                cat.setUsuario(user);
                categoriaRepository.save(cat);
            });
        }
        return "redirect:/finances";
    }

    @PostMapping("/transacao/excluir")
    public String excluirTransacao(@RequestParam Long id, Principal principal) {
        if (principal != null) {
            transacaoRepository.findById(id).ifPresent(t -> {
                if (t.getUsuario().getUsername().equals(principal.getName())) {
                    transacaoRepository.delete(t);
                }
            });
        }
        return "redirect:/finances";
    }

    @PostMapping("/categoria/editar")
    public String editarCategoria(
            @RequestParam Long id,
            @RequestParam String nome,
            @RequestParam String cor,
            Principal principal) {
        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName()).ifPresent(user -> {
                categoriaRepository.findById(id).ifPresent(cat -> {
                    if (cat.getUsuario().getId().equals(user.getId())) {
                        cat.setNome(nome);
                        cat.setCor(cor);
                        categoriaRepository.save(cat);
                    }
                });
            });
        }
        return "redirect:/finances";
    }

    @PostMapping("/categoria/excluir")
    public String excluirCategoria(
            @RequestParam Long id,
            Principal principal) {
        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName()).ifPresent(user -> {
                categoriaRepository.findById(id).ifPresent(cat -> {
                    if (cat.getUsuario().getId().equals(user.getId())) {
                        categoriaRepository.delete(cat);
                    }
                });
            });
        }
        return "redirect:/finances";
    }
}
