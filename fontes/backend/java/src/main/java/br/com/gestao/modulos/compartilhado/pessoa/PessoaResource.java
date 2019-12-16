package br.com.gestao.modulos.compartilhado.pessoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compartilhado/pessoas")
public class PessoaResource {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public Page<Pessoa> findAll(PessoaRepositoryFiltro filtro, Pageable paginavel) {
        return pessoaRepository.findAll(paginavel, filtro);
    }

    @GetMapping("/{key}")
    public ResponseEntity<Pessoa> findOne(@Valid @PathVariable String key) {
        Pessoa pessoaEncontrada = pessoaService.buscarPor(key);
        return pessoaEncontrada != null ? ResponseEntity.ok(pessoaEncontrada) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Pessoa> save(@Valid @RequestBody Pessoa obj, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.novo(obj));
    }

    @PutMapping("/{key}/motorista")
    public ResponseEntity<Pessoa> updateMotorista(@Valid @PathVariable String key, @Valid @RequestBody Pessoa Pessoa) {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.atualizarMotorista(key, Pessoa));
    }

    @PutMapping("/{key}/cliente-fornecedor")
    public ResponseEntity<Pessoa> updateClienteFornecedor(@Valid @PathVariable String key, @Valid @RequestBody Pessoa Pessoa) {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.atualizarClienteFornecedor(key, Pessoa));
    }

    @GetMapping("/cmbClientes")
    public List<Pessoa> listAllComboBoxClientes(PessoaRepositoryFiltro filtro, Pageable pageable) {
        return pessoaService.pesquisaClienteFornecedorCmb(filtro, pageable);
    }

    @GetMapping("/cmbEmpresaOuFilial")
    public List<Pessoa> listAllComboBoxEmpresaRosinha(PessoaRepositoryFiltro filtro, Pageable pageable) {
        return pessoaService.pesquisaEmpresaRosinhaCmb(filtro, pageable);
    }

    @GetMapping("/cmbRepresentanteComercial")
    public List<Pessoa> listAllComboBoxRepresentanteComercialEmpresaRosinha(PessoaRepositoryFiltro filtro, Pageable pageable) {
        return pessoaService.pesquisaRepresentanteComercialEmpresaRosinhaCmb(filtro, pageable);
    }

    @GetMapping("/cmbMotoristas")
    public List<Pessoa> listAllComboBoxMotoristas(PessoaRepositoryFiltro filtro, Pageable pageable) {
        return pessoaService.pesquisaMotoristaCmb(filtro, pageable);
    }

    @GetMapping("/cmbClientes/cpf/{cpf}")
    public ResponseEntity<Pessoa> buscarPorCPF(@Valid @PathVariable String cpf) {
        Optional<Pessoa> p = pessoaService.buscarPorCPF(cpf);
        if (p.isPresent()) {
            return ResponseEntity.ok(p.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/cmbClientes/cnpj/{cnpj}")
    public ResponseEntity<Pessoa> buscarPorCNPJ(@Valid @PathVariable String cnpj) {
        Optional<Pessoa> p = pessoaService.buscarPorCNPJ(cnpj);
        if (p.isPresent()) {
            return ResponseEntity.ok(p.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{key}/motorista/ativar")
    public ResponseEntity<Pessoa> motoristaAtivar(@Valid @PathVariable String key) {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.motoristaAtivar(key));
    }

    @PutMapping("/{key}/motorista/desativar")
    public ResponseEntity<Pessoa> motoristaDesativar(@Valid @PathVariable String key) {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.motoristaDesativar(key));
    }

}
