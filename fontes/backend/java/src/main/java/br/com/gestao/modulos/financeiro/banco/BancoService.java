package br.com.gestao.modulos.financeiro.banco;

import br.com.gestao.config.propriedades.GestaoApiProperties;
import br.com.gestao.gerenciadorErros.exceptions.EntidadeNaoEncontradaException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BancoService {

    @Autowired
    private GestaoApiProperties gestaoApiProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BancoRepository bancoRepository;

    public Banco novo(Banco obj) {
        return criarOuAtualizar(obj);
    }

    @Transactional
    public Banco atualizar(String key, Banco objNovo) {
        Banco objEncontrado = buscarPor(key);
        BeanUtils.copyProperties(objNovo, objEncontrado, "id", "uuid");
        return criarOuAtualizar(objNovo);
    }

    private Banco criarOuAtualizar(Banco banco) {
        return bancoRepository.saveAndFlush(banco);
    }

    @Transactional
    public void excluir(String key) {
        Banco obj = buscarPor(key);
        excluir(obj);
    }

    private void excluir(Banco obj) {
        bancoRepository.deleteById(obj.getId());
        bancoRepository.flush();
    }

    public Banco buscarPor(String key) {
        if (!key.isEmpty()) {
            try {
                Optional<Banco> obj = Optional.empty();
                if (gestaoApiProperties.isIdentificadorPadraoId()) {
                    obj = bancoRepository.findById(Long.parseLong(key));
                } else {
                    obj = bancoRepository.findByUuid(key);
                }
                if (obj.isPresent()) {
                    return obj.get();
                }
            } catch (Exception e) {
                //  Vai para o limbo
            }
        }
        throw new EntidadeNaoEncontradaException(messageSource.getMessage("recurso.banco-nao-encontrado", new Object[]{key}, LocaleContextHolder.getLocale()));
    }
}
