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
            @RequestParam(defaultValue = "false") Boolean isParcelado,
            @RequestParam(required = false) Integer totalParcelas,
            Principal principal) {
        
        if (principal != null) {
            Optional<Usuario> userOpt = usuarioRepository.findByUsername(principal.getName());
            if (userOpt.isPresent()) {
                Usuario usuario = userOpt.get();
                CategoriaTransacao categoria = categoriaRepository.findById(categoriaId).orElse(null);
                InstituicaoFinanceira instituicao = instituicaoId != null ? instituicaoRepository.findById(instituicaoId).orElse(null) : null;

                int iteracoes = (isParcelado && totalParcelas != null && totalParcelas > 1) ? totalParcelas : 1;
                LocalDate dataBase = LocalDate.parse(dataVencimento);

                for (int i = 0; i < iteracoes; i++) {
                    TransacaoFinanceira transacao = new TransacaoFinanceira();
                    String descFinal = descricao;
                    if (isParcelado && totalParcelas > 1) {
                        descFinal += " (" + (i + 1) + "/" + totalParcelas + ")";
                    }
                    
                    transacao.setDescricao(descFinal);
                    transacao.setValor(valor);
                    transacao.setDataVencimento(dataBase.plusMonths(i));
                    
                    if (i == 0 && dataPagamento != null && !dataPagamento.isEmpty()) {
                        transacao.setDataPagamento(LocalDate.parse(dataPagamento));
                    }
                    
                    transacao.setTipo(tipo);
                    // Only first installment can be PAGO on creation usually, or all if not parcelado
                    transacao.setStatus(i == 0 ? status : StatusTransacao.PENDENTE);
                    transacao.setUsuario(usuario);
                    transacao.setCategoria(categoria);
                    transacao.setInstituicao(instituicao);
                    
                    if (isParcelado) {
                        transacao.setIsParcelado(true);
                        transacao.setTotalParcelas(totalParcelas);
                        transacao.setParcelaAtual(i + 1);
                    }

                    transacaoRepository.save(transacao);

                    // Update balance if PAGO
                    if (transacao.getStatus() == StatusTransacao.PAGO && instituicao != null) {
                        if (tipo == TipoTransacao.RECEITA) {
                            instituicao.setSaldoAtual(instituicao.getSaldoAtual().add(valor));
                        } else {
                            instituicao.setSaldoAtual(instituicao.getSaldoAtual().subtract(valor));
                        }
                        instituicaoRepository.save(instituicao);
                    }
                }
            }
        }
        return "redirect:/finances";
    }

    @PostMapping("/transacao/pagar")
    public String pagarTransacao(@RequestParam Long id, Principal principal) {
        if (principal != null) {
            transacaoRepository.findById(id).ifPresent(t -> {
                if (t.getUsuario().getUsername().equals(principal.getName()) && t.getStatus() != StatusTransacao.PAGO) {
                    t.setStatus(StatusTransacao.PAGO);
                    t.setDataPagamento(LocalDate.now());
                    transacaoRepository.save(t);
                    
                    if (t.getInstituicao() != null) {
                        InstituicaoFinanceira inst = t.getInstituicao();
                        if (t.getTipo() == TipoTransacao.RECEITA) {
                            inst.setSaldoAtual(inst.getSaldoAtual().add(t.getValor()));
                        } else {
                            inst.setSaldoAtual(inst.getSaldoAtual().subtract(t.getValor()));
                        }
                        instituicaoRepository.save(inst);
                    }
                }
            });
        }
        return "redirect:/finances";
    }

    @PostMapping("/instituicao/editar-saldo")
    public String editarSaldo(
            @RequestParam Long id,
            @RequestParam BigDecimal novoSaldo,
            Principal principal) {
        if (principal != null) {
            instituicaoRepository.findById(id).ifPresent(inst -> {
                if (inst.getUsuario().getUsername().equals(principal.getName())) {
                    inst.setSaldoAtual(novoSaldo);
                    instituicaoRepository.save(inst);
                }
            });
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
