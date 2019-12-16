package br.com.gestao.modulos.compartilhado.pessoa;

import br.com.gestao.config.propriedades.GestaoApiProperties;
import br.com.gestao.gerenciadorErros.exceptions.EntidadeNaoEncontradaException;
import br.com.gestao.gerenciadorErros.exceptions.RegraDeNegocioException;
import br.com.gestao.modulos.compartilhado.cidadeEstado.CidadeService;
import br.com.gestao.modulos.compartilhado.pessoa.estadoCivil.EstadoCivilService;
import br.com.gestao.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private GestaoApiProperties gestaoApiProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EstadoCivilService estadoCivilService;

    @Autowired
    private CidadeService cidadeService;

    @Transactional
    public Pessoa novo(Pessoa obj) {
        obj.setId(null);
        return criarOuAtualizar(obj);
    }

    @Transactional
    public Pessoa atualizar(String key, Pessoa objNovo) {
        Pessoa objEncontrado = buscarPor(key);
        BeanUtils.copyProperties(objNovo, objEncontrado, "id", "uuid");
        return criarOuAtualizar(objNovo);
    }

    private Pessoa criarOuAtualizar(Pessoa obj) {
        obj = validarPessoa(obj);
        obj = obj.getTipo().equals(PessoaTipo.FISICA) ? this.validarPessoaFisica(obj) : this.validarPessoaJuridica(obj);
        return pessoaRepository.saveAndFlush(obj);
    }

    @Transactional
    public void excluir(String key) {
        Pessoa obj = buscarPor(key);
        excluir(obj);
    }

    private void excluir(Pessoa obj) {
        pessoaRepository.deleteById(obj.getId());
        pessoaRepository.flush();
    }

    public Pessoa buscarPor(String key) {
        if (!key.isEmpty()) {
            try {
                Optional<Pessoa> obj = Optional.empty();
                if (gestaoApiProperties.isIdentificadorPadraoId()) {
                    obj = pessoaRepository.findById(Long.parseLong(key));
                } else {
                    obj = pessoaRepository.findByUuid(key);
                }
                if (obj.isPresent()) {
                    return obj.get();
                }
            } catch (Exception e) {
                //  Vai para o limbo
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.pessoa-nao-encontrado", new Object[]{key}, LocaleContextHolder.getLocale()));
    }

    /**
     * Prepara os dados de Cliente/Fornecedor para atualização
     */
    @Transactional
    public Pessoa atualizarClienteFornecedor(String key, Pessoa pessoa) {
        Pessoa objEncontrado = buscarPor(key);

        objEncontrado.setImagem(pessoa.getImagem());
        objEncontrado.setNome(pessoa.getNome());
        objEncontrado.setFantasia(pessoa.getFantasia());
        objEncontrado.setEmail(pessoa.getEmail());
        objEncontrado.setTelefone1(pessoa.getTelefone1());
        objEncontrado.setTelefone1Obs(pessoa.getTelefone1Obs());
        objEncontrado.setTelefone2(pessoa.getTelefone2());
        objEncontrado.setTelefone2Obs(pessoa.getTelefone2Obs());
        objEncontrado.setCidade(pessoa.getCidade());
        objEncontrado.setBairro(pessoa.getBairro());
        objEncontrado.setCep(pessoa.getCep());
        objEncontrado.setEndereco(pessoa.getEndereco());
        objEncontrado.setObs(pessoa.getObs());

        if (objEncontrado.getTipo().equals(PessoaTipo.FISICA)) {
            objEncontrado.getPessoaFisica().setRg(pessoa.getPessoaFisica().getRg());
            objEncontrado.setPessoaJuridica(null);
        } else {
            objEncontrado.getPessoaJuridica().setInscricaoEstadual(pessoa.getPessoaJuridica().getInscricaoEstadual());
            objEncontrado.setPessoaFisica(null);
        }

        return criarOuAtualizar(objEncontrado);
    }

    /**
     * Prepara os dados de motorista para atualização
     */
    @Transactional
    public Pessoa atualizarMotorista(String key, Pessoa pessoa) {
        Pessoa objEncontrado = buscarPor(key);

        objEncontrado.setImagem(pessoa.getImagem());
        objEncontrado.setNome(pessoa.getNome());
        objEncontrado.setFantasia(pessoa.getFantasia());
        objEncontrado.getPessoaFisica().setRg(pessoa.getPessoaFisica().getRg());
        objEncontrado.setEmail(pessoa.getEmail());
        objEncontrado.setTelefone1(pessoa.getTelefone1());
        objEncontrado.setTelefone1Obs(pessoa.getTelefone1Obs());
        objEncontrado.setTelefone2(pessoa.getTelefone2());
        objEncontrado.setTelefone2Obs(pessoa.getTelefone2Obs());
        objEncontrado.setCidade(pessoa.getCidade());
        objEncontrado.setBairro(pessoa.getBairro());
        objEncontrado.setCep(pessoa.getCep());
        objEncontrado.setEndereco(pessoa.getEndereco());
        objEncontrado.setObs(pessoa.getObs());
        objEncontrado.getPessoaFisica().setCnhNumero(pessoa.getPessoaFisica().getCnhNumero());
        objEncontrado.getPessoaFisica().setOrgaoRg(pessoa.getPessoaFisica().getOrgaoRg());
        objEncontrado.getPessoaFisica().setCnhPrimeiraHabilitacao(pessoa.getPessoaFisica().getCnhPrimeiraHabilitacao());
        objEncontrado.getPessoaFisica().setCnhEmissaoData(pessoa.getPessoaFisica().getCnhEmissaoData());
        objEncontrado.getPessoaFisica().setCnhEmissaoCidade(pessoa.getPessoaFisica().getCnhEmissaoCidade());
        objEncontrado.getPessoaFisica().setCnhVencimento(pessoa.getPessoaFisica().getCnhVencimento());
        objEncontrado.getPessoaFisica().setDataNascimento(pessoa.getPessoaFisica().getDataNascimento());
        objEncontrado.getPessoaFisica().setNomeMae(pessoa.getPessoaFisica().getNomeMae());
        objEncontrado.getPessoaFisica().setNomePai(pessoa.getPessoaFisica().getNomePai());
        objEncontrado.getPessoaFisica().setSexo(pessoa.getPessoaFisica().getSexo());
        objEncontrado.getPessoaFisica().setCnhCategoria(pessoa.getPessoaFisica().getCnhCategoria());

        return criarOuAtualizar(objEncontrado);
    }

    public Optional<Pessoa> buscarPorCPF(String cpf) {
        Optional<Pessoa> p = Optional.empty();
        if (StringUtils.isNotBlank(cpf)) {
            p = pessoaRepository.findByPessoaFisicaCpf(Utils.formatarMascaraTexto(cpf, "###.###.###-##"));
            if (p.isPresent()) {
                return p;
            }
        }
        throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-fisica-cpf-nao-encontrado-nao-encontrado", new Object[]{cpf}, LocaleContextHolder.getLocale()));
    }

    public Optional<Pessoa> buscarPorCNPJ(String cnpj) {
        Optional<Pessoa> p = Optional.empty();
        if (StringUtils.isNotBlank(cnpj)) {
            p = pessoaRepository.findByPessoaJuridicaCnpj(Utils.formatarMascaraTexto(cnpj, "##.###.###/####-##"));
            if (p.isPresent()) {
                return p;
            }
        }
        throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-juridica-cnpj-nao-encontrado", new Object[]{cnpj}, LocaleContextHolder.getLocale()));
    }

    public List<Pessoa> pesquisaClienteFornecedorCmb(PessoaRepositoryFiltro filtro, Pageable pageable) {
        filtro.setSomenteAtivo(true);
        return pessoaRepository.findAll(pageable, filtro).getContent();
    }

    public List<Pessoa> pesquisaMotoristaCmb(PessoaRepositoryFiltro filtro, Pageable pageable) {
        filtro.setMotorista(true);
        filtro.setSomenteAtivo(true);
        return pessoaRepository.findAll(pageable, filtro).getContent();
    }

    public List<Pessoa> pesquisaEmpresaRosinhaCmb(PessoaRepositoryFiltro filtro, Pageable pageable) {
        filtro.setEmpresaPrincipalOuFilial(true);
        filtro.setSomenteAtivo(true);
        return pessoaRepository.findAll(pageable, filtro).getContent();
    }

    public List<Pessoa> pesquisaRepresentanteComercialEmpresaRosinhaCmb(PessoaRepositoryFiltro filtro, Pageable pageable) {
        filtro.setRepresentanteComercial(true);
        filtro.setSomenteAtivo(true);
        return pessoaRepository.findAll(pageable, filtro).getContent();
    }

    @Transactional
    public Pessoa motoristaAtivar(String key) {
        Pessoa p = buscarPor(key);
        if (p.getTipo().equals(PessoaTipo.JURIDICA)) {
            throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-juridica-nao-pode-ser-motorista", null, LocaleContextHolder.getLocale()));
        }
        p.getPessoaFisica().setMotoristaInativo(false);
        return criarOuAtualizar(p);
    }

    @Transactional
    public Pessoa motoristaDesativar(String key) {
        Pessoa p = buscarPor(key);
        if (p.getTipo().equals(PessoaTipo.JURIDICA)) {
            throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-juridica-nao-pode-ser-motorista", null, LocaleContextHolder.getLocale()));
        }
        p.getPessoaFisica().setMotoristaInativo(true);
        return criarOuAtualizar(p);
    }

    public Pessoa validarPessoa(Pessoa pessoa) {
        Pessoa pessoaEncontrada = pessoa.getId() != null ? this.buscarPor(pessoa.getKey()) : null;

        if (pessoa.isPessoaFisica()) {
            if (pessoa.getId() == null) {
                pessoa.getPessoaFisica().setId(null);
            }
            pessoa.setPessoaJuridica(null);
            pessoa.getPessoaFisica().setCpf(Utils.formatarMascaraTexto(pessoa.getPessoaFisica().getCpf(), "###.###.###-##"));
            pessoa = this.validarPessoaFisica(pessoa);
        } else {
            if (pessoa.getId() == null) {
                pessoa.getPessoaJuridica().setId(null);
            }
            pessoa.setPessoaFisica(null);
            pessoa.getPessoaJuridica().setCnpj(Utils.formatarMascaraTexto(pessoa.getPessoaJuridica().getCnpj(), "##.###.###/####-##"));
            pessoa = this.validarPessoaJuridica(pessoa);
        }

        if (pessoaEncontrada != null) {
            // IMPEDE A ALTERACAO DO CPF - PARA CADASTROS JA REALIZADOS
            if (pessoa.getPessoaFisica() != null && !pessoa.getPessoaFisica().getCpf().equals(pessoaEncontrada.getPessoaFisica().getCpf())) {
                throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-fisica-cpf-nao-pode-ser-alterado", new Object[]{pessoa.getPessoaFisica().getCpf()}, LocaleContextHolder.getLocale()));
            }
            // IMPEDE A ALTERACAO DO CNPJ - PARA CADASTROS JA REALIZADOS
            if (pessoa.getPessoaJuridica() != null && !pessoa.getPessoaJuridica().getCnpj().equals(pessoaEncontrada.getPessoaJuridica().getCnpj())) {
                throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-juridica-cnpj-nao-pode-ser-alterado", new Object[]{pessoa.getPessoaFisica().getCpf()}, LocaleContextHolder.getLocale()));
            }
        }

        pessoa.setCidade(cidadeService.buscarCidadePor(pessoa.getCidade().getKey()));

        return pessoa;
    }

    private Pessoa validarPessoaFisica(Pessoa pessoa) {
        // GARANTE QUE OS DADOS DE PESSOA FISICA EXITEM
        if (pessoa.getPessoaFisica() == null) {
            throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-fisica-cpf-nao-encontrado", new Object[]{pessoa.getPessoaFisica().getCpf()}, LocaleContextHolder.getLocale()));
        }
        // GARANTE UM CPF VALIDO
        if (Utils.validarCPF(pessoa.getPessoaFisica().getCpf()) == false) {
            throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-fisica-cpf-invalido", new Object[]{pessoa.getPessoaFisica().getCpf()}, LocaleContextHolder.getLocale()));
        }
        // GARANTE UM CPF UNICO
        if (this.pessoaRepository.verificarCPFJaCadastrado(pessoa.getPessoaFisica().getCpf(), pessoa.getId())) {
            throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-fisica-ja-cadastrada", new Object[]{pessoa.getPessoaFisica().getCpf()}, LocaleContextHolder.getLocale()));
        }
        // VERIFICA SE O ESTADO CIVIL EXISTE NA BASE DE DADOS
        if (pessoa.getPessoaFisica().getEstadoCivil() != null) {
            pessoa.getPessoaFisica().setEstadoCivil(estadoCivilService.buscarPor(pessoa.getPessoaFisica().getEstadoCivil().getKey()));
        }

        pessoa.getPessoaFisica().setPessoa(pessoa);
        return pessoa;
    }

    private Pessoa validarPessoaJuridica(Pessoa pessoa) {
        // GARANTE QUE OS DADOS DE PESSOA JURIDICA EXISTE
        if (pessoa.getPessoaJuridica() == null) {
            throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-juridica-cnpj-nao-encontrado", new Object[]{pessoa.getPessoaJuridica().getCnpj()}, LocaleContextHolder.getLocale()));
        }
        // GARANTE UM CNPJ VALIDO
        if (Utils.validarCNPJ(pessoa.getPessoaJuridica().getCnpj()) == false) {
            throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-juridica-cnpj-invalido", new Object[]{pessoa.getPessoaJuridica().getCnpj()}, LocaleContextHolder.getLocale()));
        }
        // GARANTE UM CNPJ UNICO
        if (this.pessoaRepository.verificarCNPJJaCadastrado(pessoa.getPessoaJuridica().getCnpj(), pessoa.getId())) {
            throw new RegraDeNegocioException(messageSource.getMessage("recurso.pessoa-juridica-ja-cadastrada", new Object[]{pessoa.getPessoaJuridica().getCnpj()}, LocaleContextHolder.getLocale()));
        }

        pessoa.getPessoaJuridica().setPessoa(pessoa);
        return pessoa;
    }
}
