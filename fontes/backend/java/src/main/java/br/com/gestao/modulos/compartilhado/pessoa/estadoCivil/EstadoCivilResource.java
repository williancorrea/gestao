package br.com.gestao.modulos.compartilhado.pessoa.estadoCivil;

import br.com.gestao.utils.jpa.QueryFiltroPadrao;
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
@RequestMapping("/compartilhado/pessoa/estado-civil")
public class EstadoCivilResource {

    @Autowired
    private EstadoCivilRepository estadoCivilRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EstadoCivilService estadoCivilService;

    @GetMapping
    public Page<EstadoCivil> todos(Pageable pageable, QueryFiltroPadrao estadoCivilRepositoryFiltro) {
        return estadoCivilRepository.findAll(pageable, estadoCivilRepositoryFiltro);
    }

    @GetMapping("/{key}")
    public ResponseEntity<EstadoCivil> porCodigo(@Valid @PathVariable String key) {
        return ResponseEntity.ok(estadoCivilService.buscarPor(key));
    }

    @PostMapping
    public ResponseEntity<EstadoCivil> salvar(@Valid @RequestBody EstadoCivil estadoCivil, HttpServletResponse response) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoCivilService.novo(estadoCivil));
    }

    @PutMapping("/{key}")
    public ResponseEntity<EstadoCivil> atualizar(@Valid @PathVariable String key, @Valid @RequestBody EstadoCivil estadoCivil) {
        return ResponseEntity.status(HttpStatus.OK).body(estadoCivilService.atualizar(key, estadoCivil));
    }

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String key) {
        estadoCivilService.excluir(key);
    }
}
