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
//    @PreAuthorize("hasAuthority('ROLE_LISTAR_BANCO') and #oauth2.hasScope('read')")
    public Page<Banco> findAll(BancoRepositoryFiltro bancoRepositoryFiltro, Pageable pageable) {
        return bancoRepository.findAll(bancoRepositoryFiltro, pageable);
    }

    @GetMapping("/{key}")
//    @PreAuthorize("hasAuthority('ROLE_LISTAR_BANCO') and #oauth2.hasScope('read')")
    public ResponseEntity<Banco> findOne(@Valid @PathVariable String key) {
//        Banco bancoEncontrado = bancoService.buscarPorKey(key);
//        return bancoEncontrado != null ? ResponseEntity.ok(bancoEncontrado) : ResponseEntity.notFound().build();
        return null;
    }

    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_SALVAR_BANCO') and #oauth2.hasScope('write')")
    public ResponseEntity<Banco> save(@Valid @RequestBody Banco banco, HttpServletResponse response) throws Exception {
        banco = bancoRepository.saveAndFlush(banco);
//        publisher.publishEvent(new EventResourceCreated(this, response, banco.getKey()));
        return ResponseEntity.status(HttpStatus.CREATED).body(banco);
    }

    @PutMapping("/{key}")
//    @PreAuthorize("hasAuthority('ROLE_ATUALIZAR_BANCO') and #oauth2.hasScope('write')")
    public ResponseEntity<Banco> update(@Valid @PathVariable String key, @Valid @RequestBody Banco banco) {
        return ResponseEntity.status(HttpStatus.OK).body(bancoService.update(key, banco));
    }

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasAuthority('ROLE_DELETAR_BANCO') and #oauth2.hasScope('write')")
    public void delete(@PathVariable String key) {
//        bancoRepository.deleteById(bancoService.buscarPorKey(key).getId());
    }
}
