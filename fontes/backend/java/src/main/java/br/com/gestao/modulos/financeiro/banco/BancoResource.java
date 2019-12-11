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

    @GetMapping("/{key}")
    public ResponseEntity<Banco> porCodigo(@Valid @PathVariable String key) {
        return ResponseEntity.ok(bancoService.buscarPor(key));
    }

    @PostMapping
    public ResponseEntity<Banco> salvar(@Valid @RequestBody Banco banco, HttpServletResponse response) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(bancoService.novo(banco));
    }

    @PutMapping("/{key}")
    public ResponseEntity<Banco> atualizar(@Valid @PathVariable String key, @Valid @RequestBody Banco banco) {
        return ResponseEntity.status(HttpStatus.OK).body(bancoService.atualizar(key, banco));
    }

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String key) {
        bancoService.excluir(key);
    }
}
