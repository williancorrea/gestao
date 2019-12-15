package br.com.gestao.modulos.compartilhado.cidadeEstado;

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
@RequestMapping("/compartilhado/cidades")
public class CidadeResource {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private CidadeService cidadeService;

    @GetMapping
    public Page<Cidade> todos(Pageable pageable, QueryFiltroPadrao filtro) {
        return cidadeRepository.findAll(pageable, filtro);
    }

    @GetMapping("/{key}")
    public ResponseEntity<Cidade> porCodigo(@Valid @PathVariable String key) {
        return ResponseEntity.ok(cidadeService.buscarCidadePor(key));
    }

    @PostMapping
    public ResponseEntity<Cidade> salvar(@Valid @RequestBody Cidade cidade, HttpServletResponse response) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(cidadeService.novo(cidade));
    }

    @PutMapping("/{key}")
    public ResponseEntity<Cidade> atualizar(@Valid @PathVariable String key, @Valid @RequestBody Cidade cidade) {
        return ResponseEntity.status(HttpStatus.OK).body(cidadeService.atualizar(key, cidade));
    }

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String key) {
        cidadeService.excluir(key);
    }
}
