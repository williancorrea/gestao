package br.com.gestao.modulos.financeiro.bancoAgencia;

import br.com.gestao.config.propriedades.GestaoApiProperties;
import br.com.gestao.gerenciadorErros.exceptions.EntidadeNaoEncontradaException;
import br.com.gestao.modulos.compartilhado.cidadeEstado.CidadeRepository;
import br.com.gestao.modulos.compartilhado.cidadeEstado.CidadeService;
import br.com.gestao.modulos.financeiro.banco.BancoService;
import br.com.gestao.modulos.financeiro.bancoAgencia.BancoAgencia;
import br.com.gestao.modulos.financeiro.bancoAgencia.BancoAgenciaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BancoAgenciaService {

    @Autowired
    private GestaoApiProperties gestaoApiProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BancoAgenciaRepository bancoAgenciaRepository;

    @Autowired
    private CidadeService cidadeService;

    @Autowired
    private BancoService bancoService;

    @Transactional
    public BancoAgencia novo(BancoAgencia obj) {
        return criarOuAtualizar(obj);
    }

    @Transactional
    public BancoAgencia atualizar(String key, BancoAgencia objNovo) {
        BancoAgencia objEncontrado = buscarPor(key);
        BeanUtils.copyProperties(objNovo, objEncontrado, "id", "uuid");
        return criarOuAtualizar(objNovo);
    }

    private BancoAgencia criarOuAtualizar(BancoAgencia obj) {
        obj.setCidade(cidadeService.buscarCidadePor(obj.getCidade().getKey()));
        obj.setBanco(bancoService.buscarPor(obj.getBanco().getKey()));
        return bancoAgenciaRepository.saveAndFlush(obj);
    }

    @Transactional
    public void excluir(String key) {
        BancoAgencia obj = buscarPor(key);
        excluir(obj);
    }

    private void excluir(BancoAgencia obj) {
        bancoAgenciaRepository.deleteById(obj.getId());
        bancoAgenciaRepository.flush();
    }

    public BancoAgencia buscarPor(String key) {
        if (!key.isEmpty()) {
            try {
                Optional<BancoAgencia> obj = Optional.empty();
                if (gestaoApiProperties.isIdentificadorPadraoId()) {
                    obj = bancoAgenciaRepository.findById(Long.parseLong(key));
                } else {
                    obj = bancoAgenciaRepository.findByUuid(key);
                }
                if (obj.isPresent()) {
                    return obj.get();
                }
            } catch (Exception e) {
                //  Vai para o limbo
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.banco-agencia-nao-encontrado", new Object[]{key}, LocaleContextHolder.getLocale()));
    }
}
