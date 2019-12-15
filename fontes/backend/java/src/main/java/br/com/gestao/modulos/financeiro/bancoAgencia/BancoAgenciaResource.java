package br.com.gestao.modulos.financeiro.bancoAgencia;

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
@RequestMapping("/financeiro/banco/agencias")
public class BancoAgenciaResource {

    @Autowired
    private BancoAgenciaRepository bancoAgenciaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private BancoAgenciaService bancoAgenciaService;

    @GetMapping
    public Page<BancoAgencia> todos(Pageable pageable, BancoAgenciaRepositoryFiltro bancoAgenciaRepositoryFiltro) {
        return bancoAgenciaRepository.findAll(pageable, bancoAgenciaRepositoryFiltro);
    }

    @GetMapping("/{key}")
    public ResponseEntity<BancoAgencia> porCodigo(@Valid @PathVariable String key) {
        return ResponseEntity.ok(bancoAgenciaService.buscarPor(key));
    }

    @PostMapping
    public ResponseEntity<BancoAgencia> salvar(@Valid @RequestBody BancoAgencia bancoAgencia, HttpServletResponse response) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(bancoAgenciaService.novo(bancoAgencia));
    }

    @PutMapping("/{key}")
    public ResponseEntity<BancoAgencia> atualizar(@Valid @PathVariable String key, @Valid @RequestBody BancoAgencia bancoAgencia) {
        return ResponseEntity.status(HttpStatus.OK).body(bancoAgenciaService.atualizar(key, bancoAgencia));
    }

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String key) {
        bancoAgenciaService.excluir(key);
    }
}
