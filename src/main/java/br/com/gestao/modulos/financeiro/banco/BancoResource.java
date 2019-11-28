package br.com.gestao.modulos.financeiro.banco;

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
@RequestMapping("/financeiro/bancos")
public class BancoResource {

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private BancoService bancoService;

    @GetMapping
    public Page<Banco> todos(BancoRepositoryFiltro bancoRepositoryFiltro, Pageable pageable) {
        return bancoRepository.findAll(bancoRepositoryFiltro, pageable);
    }

    @GetMapping("/{chave}")
    public ResponseEntity<Banco> porCodigo(@Valid @PathVariable String chave) {
        return ResponseEntity.ok(bancoService.buscarPor(chave));
    }

    @PostMapping
    public ResponseEntity<Banco> salvar(@Valid @RequestBody Banco banco, HttpServletResponse response) throws Exception {
        banco = bancoRepository.saveAndFlush(banco);
        return ResponseEntity.status(HttpStatus.CREATED).body(banco);
    }

    @PutMapping("/{chave}")
    public ResponseEntity<Banco> atualizar(@Valid @PathVariable String chave, @Valid @RequestBody Banco banco) {
        return ResponseEntity.status(HttpStatus.OK).body(bancoService.atualizar(chave, banco));
    }

    @DeleteMapping("/{chave}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String chave) {
        bancoService.excluir(chave);
    }
}
