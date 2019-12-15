package br.com.gestao.modulos.financeiro.bancoExtratoAbreviacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/financeiro/banco/extrato-abreviacao")
public class BancoExtratoAbreviacaoResource {

    @Autowired
    private BancoExtratoAbreviacaoRepository bancoExtratoAbreviacaoRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private BancoExtratoAbreviacaoService bancoExtratoAbreviacaoService;

    @GetMapping
    public Page<BancoExtratoAbreviacao> todos(Pageable pageable, BancoExtratoAbreviacaoRepositoryFiltro bancoExtratoAbreviacaoRepositoryFiltro) {
        return bancoExtratoAbreviacaoRepository.findAll(pageable, bancoExtratoAbreviacaoRepositoryFiltro);
    }

    @GetMapping("/{key}")
    public ResponseEntity<BancoExtratoAbreviacao> porCodigo(@Valid @PathVariable String key) {
        return ResponseEntity.ok(bancoExtratoAbreviacaoService.buscarPor(key));
    }

    @PostMapping
    public ResponseEntity<BancoExtratoAbreviacao> salvar(@Valid @RequestBody BancoExtratoAbreviacao bancoExtratoAbreviacao, HttpServletResponse response) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(bancoExtratoAbreviacaoService.novo(bancoExtratoAbreviacao));
    }

    @PutMapping("/{key}")
    public ResponseEntity<BancoExtratoAbreviacao> atualizar(@Valid @PathVariable String key, @Valid @RequestBody BancoExtratoAbreviacao bancoExtratoAbreviacao) {
        return ResponseEntity.status(HttpStatus.OK).body(bancoExtratoAbreviacaoService.atualizar(key, bancoExtratoAbreviacao));
    }

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String key) {
        bancoExtratoAbreviacaoService.excluir(key);
    }
}
