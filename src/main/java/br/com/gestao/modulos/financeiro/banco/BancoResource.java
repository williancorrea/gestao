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

    @GetMapping("/{uuid}")
    public ResponseEntity<Banco> porCodigo(@Valid @PathVariable String uuid) {
        return ResponseEntity.ok(bancoService.buscarPor(uuid));
    }

    @PostMapping
    public ResponseEntity<Banco> salvar(@Valid @RequestBody Banco banco, HttpServletResponse response) throws Exception {
        banco = bancoRepository.saveAndFlush(banco);
        return ResponseEntity.status(HttpStatus.CREATED).body(banco);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Banco> atualizar(@Valid @PathVariable String uuid, @Valid @RequestBody Banco banco) {
        return ResponseEntity.status(HttpStatus.OK).body(bancoService.atualizar(uuid, banco));
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String uuid) {
        bancoService.excluir(uuid);
    }
}
