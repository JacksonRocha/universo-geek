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

    @Autowired
    private br.com.algaworks.documentacao.repository.DividaTerceiroRepository dividaTerceiroRepository;

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
            @RequestParam(required = false) String cor,
            Principal principal) {
        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName()).ifPresent(user -> {
                CategoriaTransacao cat = new CategoriaTransacao();
                cat.setNome(nome);
                
                // If cor is not provided, generate a random pleasant one
                if (cor == null || cor.trim().isEmpty()) {
                    String[] vibrantColors = {"#3b82f6", "#10b981", "#ef4444", "#f59e0b", "#8b5cf6", "#ec4899", "#06b6d4"};
                    cat.setCor(vibrantColors[(int) (Math.random() * vibrantColors.length)]);
                } else {
                    cat.setCor(cor);
                }
                
                cat.setUsuario(user);
                categoriaRepository.save(cat);
            });
        }
        return "redirect:/finances";
    }

    @PostMapping("/divida-terceiro")
    public String salvarDividaTerceiro(
            @RequestParam String devedor,
            @RequestParam String item,
            @RequestParam BigDecimal valorTotal,
            @RequestParam Integer totalParcelas,
            @RequestParam String dataCompra,
            @RequestParam(required = false) Long instituicaoId,
            Principal principal) {
        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName()).ifPresent(user -> {
                DividaTerceiro divida = new DividaTerceiro();
                divida.setDevedor(devedor);
                divida.setItem(item);
                divida.setValorTotal(valorTotal);
                divida.setTotalParcelas(totalParcelas);
                divida.setDataCompra(LocalDate.parse(dataCompra));
                divida.setInstituicaoId(instituicaoId);
                divida.setUsuario(user);
                divida.setParcelasPagas(0);
                dividaTerceiroRepository.save(divida);

                // Se houver instituição vinculada, cria as transações financeiras correspondentes
                if (instituicaoId != null) {
                    InstituicaoFinanceira inst = instituicaoRepository.findById(instituicaoId).orElse(null);
                    // Busca uma categoria "Terceiros" ou usa a primeira disponível
                    CategoriaTransacao cat = categoriaRepository.findAll().stream()
                            .filter(c -> c.getNome().equalsIgnoreCase("Terceiros") && c.getUsuario().getId().equals(user.getId()))
                            .findFirst()
                            .orElse(categoriaRepository.findAll().stream()
                                    .filter(c -> c.getUsuario().getId().equals(user.getId()))
                                    .findFirst().orElse(null));

                    if (cat != null) {
                        BigDecimal valorParcela = valorTotal.divide(new BigDecimal(totalParcelas), 2, java.math.RoundingMode.HALF_UP);
                        LocalDate dataBase = LocalDate.parse(dataCompra);

                        for (int i = 0; i < totalParcelas; i++) {
                            TransacaoFinanceira trans = new TransacaoFinanceira();
                            trans.setDescricao(item + " (" + (i + 1) + "/" + totalParcelas + ") - " + devedor);
                            trans.setValor(valorParcela);
                            trans.setDataVencimento(dataBase.plusMonths(i));
                            trans.setTipo(TipoTransacao.DESPESA);
                            trans.setStatus(StatusTransacao.PENDENTE);
                            trans.setUsuario(user);
                            trans.setCategoria(cat);
                            trans.setInstituicao(inst);
                            trans.setIsParcelado(true);
                            trans.setTotalParcelas(totalParcelas);
                            trans.setParcelaAtual(i + 1);
                            transacaoRepository.save(trans);
                        }
                    }
                }
            });
        }
        return "redirect:/finances";
    }

    @PostMapping("/divida-terceiro/pagar-parcela")
    public String pagarParcelaTerceiro(@RequestParam Long id, Principal principal) {
        if (principal != null) {
            dividaTerceiroRepository.findById(id).ifPresent(d -> {
                if (d.getUsuario().getUsername().equals(principal.getName())) {
                    if (d.getParcelasPagas() < d.getTotalParcelas()) {
                        d.setParcelasPagas(d.getParcelasPagas() + 1);
                        dividaTerceiroRepository.save(d);

                        // Cria uma transação de RECEITA para refletir o reembolso do terceiro
                        TransacaoFinanceira trans = new TransacaoFinanceira();
                        trans.setDescricao("Reembolso: " + d.getDevedor() + " - " + d.getItem() + " (" + d.getParcelasPagas() + "/" + d.getTotalParcelas() + ")");
                        
                        BigDecimal valorParcela = d.getValorTotal().divide(new BigDecimal(d.getTotalParcelas()), 2, java.math.RoundingMode.HALF_UP);
                        trans.setValor(valorParcela);
                        trans.setDataVencimento(LocalDate.now());
                        trans.setDataPagamento(LocalDate.now());
                        trans.setTipo(TipoTransacao.RECEITA);
                        trans.setStatus(StatusTransacao.PAGO);
                        trans.setUsuario(d.getUsuario());
                        
                        // Busca categoria "Terceiros"
                        CategoriaTransacao cat = categoriaRepository.findAll().stream()
                                .filter(c -> c.getNome().equalsIgnoreCase("Terceiros") && c.getUsuario().getId().equals(d.getUsuario().getId()))
                                .findFirst().orElse(null);
                        
                        if (cat == null) {
                             cat = categoriaRepository.findAll().stream()
                                    .filter(c -> c.getUsuario().getId().equals(d.getUsuario().getId()))
                                    .findFirst().orElse(null);
                        }
                        trans.setCategoria(cat);

                        if (d.getInstituicaoId() != null) {
                            instituicaoRepository.findById(d.getInstituicaoId()).ifPresent(inst -> {
                                trans.setInstituicao(inst);
                                inst.setSaldoAtual(inst.getSaldoAtual().add(valorParcela));
                                instituicaoRepository.save(inst);
                            });
                        }
                        
                        transacaoRepository.save(trans);
                    }
                }
            });
        }
        return "redirect:/finances";
    }

    @PostMapping("/divida-terceiro/excluir")
    public String excluirDividaTerceiro(@RequestParam Long id, Principal principal) {
        if (principal != null) {
            dividaTerceiroRepository.findById(id).ifPresent(d -> {
                if (d.getUsuario().getUsername().equals(principal.getName())) {
                    dividaTerceiroRepository.delete(d);
                }
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
